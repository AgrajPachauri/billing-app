package com.app.billing.controller;

import com.app.billing.model.Entry;
import com.app.billing.repository.EntryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    @Autowired
    private EntryRepository repo;

    // Add Entry
    @PostMapping
    public Entry addEntry(@RequestBody Entry entry) {

        // Auto set date & time if not provided
        if (entry.getDate() == null) {
            entry.setDate(LocalDate.now());
        }

        entry.setTime(LocalTime.now());

        return repo.save(entry);
    }

    // Get entries by date (logs)
    @GetMapping("/by-date")
    public List<Entry> getByDate(@RequestParam String date) {
        return repo.findByDate(LocalDate.parse(date));
    }

    // Get entries for billing (month)
    @GetMapping("/by-customer-month")
    public List<Entry> getByCustomerMonth(
            @RequestParam Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return repo.findByCustomerIdAndDateBetween(
                customerId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }

    // Test
    @GetMapping("/test")
    public String test() {
        return "Entry API working";
    }
}