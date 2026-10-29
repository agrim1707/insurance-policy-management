package com.lab.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Insurance Policy Management System.
 * Spring Boot auto-configuration boots an embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class InsurancePolicyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsurancePolicyManagementApplication.class, args);
    }
}
