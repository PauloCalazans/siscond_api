package br.com.siscond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi //Enable open api 3.0.3 spec
public class SiscondApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiscondApplication.class, args);
	}

}
