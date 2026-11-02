package com.smartcity.model;

import com.smartcity.model.enums.DeviceStatus;
import com.smartcity.model.enums.ZoneType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "zones")
public class Zone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ZoneType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Zone parentZone;

    @OneToMany(mappedBy = "parentZone", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Zone> subZones = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "power_status", length = 20)
    private DeviceStatus powerStatus = DeviceStatus.ON;

    @Column(name = "energy_consumption")
    private Double energyConsumption = 0.0;

    public void addSubZone(Zone zone) {
        subZones.add(zone);
        zone.setParentZone(this);
    }

    public void removeSubZone(Zone zone) {
        subZones.remove(zone);
        zone.setParentZone(null);
    }
}