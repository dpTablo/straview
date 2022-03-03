package com.dptablo.straview.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.dptablo.straview" })
@EntityScan(basePackages = { "com.dptablo.straview.dto" })
public class ApplicationConfiguration {
}
