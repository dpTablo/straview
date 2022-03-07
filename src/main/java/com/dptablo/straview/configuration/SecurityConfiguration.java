package com.dptablo.straview.configuration;

import com.dptablo.straview.security.jwt.JwtAccessDeniedHandler;
import com.dptablo.straview.security.jwt.JwtAuthenticationEntryPoint;
import com.dptablo.straview.security.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:app.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Value("{$straview.admin.account:straview}")
    private String straviewAdminAccount;

    @Value("{$straview.admin.password:straview1234}")
    private String straviewAdminPassword;

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

            .and().authorizeHttpRequests()
                .antMatchers("/page/**",
                        "/api/auth/authenticate",
                        "/api/auth/strava/authenticate").permitAll()
                .anyRequest().authenticated()

            .and().httpBasic().disable();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)

                .withUser(straviewAdminAccount)
                .password(straviewAdminPassword)
                .roles("ADMIN")

                .and()
                    .withUser("user1")
                    .password(passwordEncoder.encode("1111"))
                    .roles("USER")

                .and()
                    .withUser("user2")
                    .password(passwordEncoder.encode("2222"))
                    .roles("USER");
    }
}
