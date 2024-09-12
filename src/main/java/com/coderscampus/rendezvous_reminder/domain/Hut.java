package com.coderscampus.rendezvous_reminder.domain;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "huts")
public class Hut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "hut", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvailabilityDate> availabilityDates;

    // Constructors, getters, and setters
    public Hut() {
    }

    public Hut(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AvailabilityDate> getAvailabilityDates() {
        return availabilityDates;
    }

    public void setAvailabilityDates(List<AvailabilityDate> availabilityDates) {
        this.availabilityDates = availabilityDates;
    }
}
