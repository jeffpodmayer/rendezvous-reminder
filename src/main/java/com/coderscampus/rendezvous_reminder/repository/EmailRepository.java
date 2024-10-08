package com.coderscampus.rendezvous_reminder.repository;

import com.coderscampus.rendezvous_reminder.domain.AvailabilityDate;
import com.coderscampus.rendezvous_reminder.domain.Email;
import com.coderscampus.rendezvous_reminder.domain.Hut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    void deleteByEmailAddress(String emailAddress);
}
