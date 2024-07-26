package dev.luisjohann.ofxmsdata;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class OfxmsdataApplication {

	@Value("${application.timezone:America/Sao_Paulo}")
	private String applicationTimeZone;

	public static void main(String[] args) {
		SpringApplication.run(OfxmsdataApplication.class, args);
	}

	@PostConstruct
	public void executeAfterMain() {
		TimeZone.setDefault(TimeZone.getTimeZone(applicationTimeZone));
	}
}
