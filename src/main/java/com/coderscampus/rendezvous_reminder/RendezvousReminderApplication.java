package com.coderscampus.rendezvous_reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import service.ReservationService;

import java.time.LocalDate;

@SpringBootApplication
public class RendezvousReminderApplication {


	public static void main(String[] args) {
		SpringApplication.run(RendezvousReminderApplication.class, args);
	}

}
