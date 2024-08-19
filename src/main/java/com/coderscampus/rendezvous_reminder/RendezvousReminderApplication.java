package com.coderscampus.rendezvous_reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import service.ReservationService;

import java.time.LocalDate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RendezvousReminderApplication {


	public static void main(String[] args) {
		SpringApplication.run(RendezvousReminderApplication.class, args);
	}

}
