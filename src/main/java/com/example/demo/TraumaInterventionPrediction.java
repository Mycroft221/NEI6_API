package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TraumaInterventionPrediction {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("--initial-id parameter is required, ie --initial-id=12000");
		} else {
			SpringApplication.run(TraumaInterventionPrediction.class, args);
		}
	}

}
