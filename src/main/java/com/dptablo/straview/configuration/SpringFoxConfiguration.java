//package com.dptablo.straview.configuration;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.ReflectionUtils;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
//import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//public class SpringFoxConfig {
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.dptablo.straview.controller"))
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Straview Swagger")
//                .description("Straview swagger config")
//                .version("1.0")
//                .build();
//    }
//
//    @Bean
//    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
//        /**
//         * spring 5.2, swagger 3.0.0 에서의 PathPatternParser 이슈 해결을 위한 조치
//         * https://dptablo.github.io/spring/spring%20boot/springfox/swagger/springboot-2.6-springfox-swagger-issue
//         */
//        return new BeanPostProcessor() {
//            @Override
//            public Object postProcessAfterInitialization(Object bean, String beanName)
//                    throws BeansException {
//                if (bean instanceof WebMvcRequestHandlerProvider
//                        || bean instanceof WebFluxRequestHandlerProvider) {
//                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
//                }
//                return bean;
//            }
//
//            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
//                    List<T> mappings) {
//                List<T> copy = mappings.stream()
//                        .filter(mapping -> mapping.getPatternParser() == null)
//                        .collect(Collectors.toList());
//                mappings.clear();
//                mappings.addAll(copy);
//            }
//
//            @SuppressWarnings("unchecked")
//            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
//                try {
//                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
//                    field.setAccessible(true);
//                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
//                } catch (IllegalArgumentException | IllegalAccessException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        };
//    }
//}
