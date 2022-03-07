package com.dptablo.straview.controller;

import com.dptablo.straview.ApplicationProperty;
import com.dptablo.straview.controller.rest.JwtAuthenticationController;
import com.dptablo.straview.dto.entity.User;
import com.dptablo.straview.dto.enumtype.Role;
import com.dptablo.straview.security.jwt.JwtAccessDeniedHandler;
import com.dptablo.straview.security.jwt.JwtAuthenticationEntryPoint;
import com.dptablo.straview.service.JwtAuthenticationService;
import com.dptablo.straview.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JwtAuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class JwtAuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    private JwtAuthenticationService jwtAuthenticationService;

    @MockBean
    private ApplicationProperty applicationProperty;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        given(applicationProperty.getStraviewAdminAccount()).willReturn("straview");
        given(applicationProperty.getStraviewAdminPassword()).willReturn("straview1234");
    }

    @Test
    @WithMockUser(value = "USER")
    public void authenticate() throws Exception {
        //given
        String token = "dfskllksfdkljf3489q4c4f";

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);

        User user = User.builder()
                .userId("user1")
                .password("1234")
                .roles(roles)
                .build();

        given(userService.findByUserId(user.getUserId())).willReturn(user);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", user.getUserId());
        params.add("password", user.getPassword());

        //when & then
        doReturn(Optional.of(token)).when(jwtAuthenticationService).authenticate(user.getUserId(), user.getPassword());
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/authenticate").params(params))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
        assertThat(map.get("userId")).isEqualTo(user.getUserId());
        assertThat(((List)map.get("roles")).contains("ADMIN"));
        assertThat(map.get("jwtToken")).isEqualTo(token);
    }

    @Test
    public void authenticate_emptyToken() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "user1");
        params.add("password", "1111");

        //when & then
        doReturn(Optional.empty()).when(jwtAuthenticationService).authenticate(anyString(), anyString());
        mockMvc.perform(post("/api/auth/authenticate").params(params))
                .andExpect(status().is5xxServerError());
    }

}