package dev.luisjohann.ofxmsdata.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "dev.luisjohann.ofxmsdata.clients")
public class FeignConfig {

}
