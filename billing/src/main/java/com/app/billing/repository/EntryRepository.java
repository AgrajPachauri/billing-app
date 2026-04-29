package com.app.billing.repository;

import com.app.billing.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    List<Entry> findByCustomerId(Long customerId);

    List<Entry> findByDate(LocalDate date);

    List<Entry> findByCustomerIdAndDateBetween(Long customerId, LocalDate start, LocalDate end);
}