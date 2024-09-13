package com.coderscampus.rendezvous_reminder.repository;

import com.coderscampus.rendezvous_reminder.domain.Hut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HutRepository extends JpaRepository<Hut, Long> {
    Optional<Hut> findByName(String name);
}
