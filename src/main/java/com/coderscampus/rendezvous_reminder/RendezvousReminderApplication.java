package com.coderscampus.rendezvous_reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class RendezvousReminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RendezvousReminderApplication.class, args);
	}
}