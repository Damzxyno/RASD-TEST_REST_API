package com.damzxyno.salesportaltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.damzxyno.salesportaltest",
		"com.damzxyno.rasdspringapi",
		"com.damzxyno.rasdspringui"
})
public class SalesPortalTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesPortalTestApplication.class, args);
	}

}
