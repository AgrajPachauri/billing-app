package com.app.billing.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;
    private String ownerName;
    private String address;
    private String contact;

    private String gstNumber;

    private String logoPath;
    private String qrPath;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}