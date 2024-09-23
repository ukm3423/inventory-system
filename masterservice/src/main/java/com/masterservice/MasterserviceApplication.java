package com.masterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * * ===========================================================================
 * * ======================== Module : Master-Service ==========================
 * * ======================== Created By : Umesh Kumar =========================
 * * ======================== Created On : 04-06-2024 ==========================
 * * ===========================================================================
 * * | Code Status : On
 */

@SpringBootApplication
@EnableDiscoveryClient
public class MasterserviceApplication {

	public static void main(String[] args) {	
		SpringApplication.run(MasterserviceApplication.class, args);
	}

}
