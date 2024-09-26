package com.coderscampus.rendezvous_reminder.initializer;

import com.coderscampus.rendezvous_reminder.domain.AvailabilityDate;
import com.coderscampus.rendezvous_reminder.domain.Hut;
import com.coderscampus.rendezvous_reminder.repository.AvailabilityDateRepository;
import com.coderscampus.rendezvous_reminder.repository.EmailRepository;
import com.coderscampus.rendezvous_reminder.repository.HutRepository;
import com.coderscampus.rendezvous_reminder.service.EmailService;
import com.coderscampus.rendezvous_reminder.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Component
public class InitialScanRunner {

    @Autowired
    private ReservationService reservationService;

    @EventListener(ApplicationReadyEvent.class)
    public void runInitialScan() {
        System.out.println("Application started. Running initial scan...");
        reservationService.initialScan();
    }

}
