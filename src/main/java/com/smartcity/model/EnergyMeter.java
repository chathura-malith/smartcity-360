package com.smartcity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "energy_meters")
public class EnergyMeter implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meter_code", nullable = false, unique = true, length = 50)
    private String meterCode;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "raw_reading_kwh", nullable = false)
    private Double rawReadingKwh = 0.0;

    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated = false;

    @Column(name = "is_compressed")
    private Boolean isCompressed = false;

    @Column(name = "processed_payload", columnDefinition = "TEXT")
    private String processedPayload;

    @Column(name = "last_updated", insertable = false, updatable = false)
    private Timestamp lastUpdated;
}