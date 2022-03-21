package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TraumaInterventionPrediction {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("--initial-id or --redcap-token parameter is required, " +
					"ie --initial-id=12000 --redcap-token=<REDCAP_TOKEN>");
		} else {
			SpringApplication.run(TraumaInterventionPrediction.class, args);
		}
	}

}
