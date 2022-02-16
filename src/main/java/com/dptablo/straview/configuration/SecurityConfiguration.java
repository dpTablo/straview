package com.dptablo.straview.configuration;

import com.dptablo.straview.security.jwt.JwtAccessDeniedHandler;
import com.dptablo.straview.security.jwt.JwtAuthenticationEntryPoint;
import com.dptablo.straview.security.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()

            .and()
                .csrf().disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/authenticate").permitAll()
                .anyRequest().authenticated()

            .and().httpBasic().disable();

//            .and()
//                .formLogin()
//                .loginProcessingUrl("/")
//                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//
//            .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())




        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)

                .withUser("user1")
                .password(passwordEncoder.encode("1111"))
                .roles("USER")

                .and()
                    .withUser("user2")
                    .password(passwordEncoder.encode("2222"))
                    .roles("USER");
    }
}
