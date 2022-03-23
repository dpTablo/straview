package com.dptablo.straview.service;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.common.OptionalConsumer;
import com.dptablo.straview.dto.entity.*;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.exception.AuthenticationException;
import com.dptablo.straview.reactive.StravaWebClientFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StravaActivityStreamService {
    private final StravaWebClientFactory stravaWebClientFactory;
    private final ApplicationProperty applicationProperty;

    /**
     * <p>Strava의 액티비티에 대한 스트림 데이터를 요청합니다.</p>
     *
     * @param activity 조회할 대상 액티비티
     * @param streamTypes 조회할 스트림 데이터 종류
     * @return 요청에 성공한 액티비티 스트림 데이터
     * @throws AuthenticationException
     * @throws JsonProcessingException
     */
    public List<ActivityStream> getActivityStreams(SummaryActivity activity, List<ActivityStreamType> streamTypes) throws AuthenticationException, JsonProcessingException {
        WebClient webClient = stravaWebClientFactory.createApiWebClient();
        Mono<JsonNode> jsonNodeMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(applicationProperty.getStravaApiV3UrlActivitiesStreams(activity.getActivityId()))
                        .queryParam("keys", streamTypesToString(streamTypes))
                        .queryParam("key_by_type", "true")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class);

        JsonNode json = jsonNodeMono.block();

        List<ActivityStream> list = new ArrayList<>();
        streamTypes.stream().forEach(activityStreamType -> {
            OptionalConsumer.of(createActivityStream(activity, activityStreamType, json))
                    .ifPresent(activityStream -> list.add(activityStream));
        });
        return list;
    }

    private String streamTypesToString(List<ActivityStreamType> streamTypes) {
        return streamTypes.stream()
                .map(ActivityStreamType::getValue)
                .collect(Collectors.joining(","));
    }

    private Optional<ActivityStream> createActivityStream(SummaryActivity activity, ActivityStreamType type, JsonNode rootNode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ActivityStream activityStream = objectMapper.treeToValue(
                    rootNode.get(type.getValue()),
                    type.getActivityStreamEntityClass());
            activityStream.setSummaryActivity(activity);
            activityStream.setType(type);

            return Optional.ofNullable(activityStream);
        } catch (Throwable e) {
            return Optional.empty();
        }
    }
}
