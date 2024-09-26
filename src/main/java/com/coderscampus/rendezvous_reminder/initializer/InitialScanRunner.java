package com.coderscampus.rendezvous_reminder.initializer;

import com.coderscampus.rendezvous_reminder.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class InitialScanRunner {

    @Autowired
    @Lazy
    private ReservationService reservationService;

    @EventListener(ApplicationReadyEvent.class)
    public void runInitialScan() {
        System.out.println("Application started. Running initial scan...");
        reservationService.initialScan();
    }

}
