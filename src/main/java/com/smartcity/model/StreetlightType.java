package com.smartcity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "streetlight_types")
public class StreetlightType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "wattage", nullable = false)
    private Integer wattage;

    @Column(name = "color_temperature", nullable = false, length = 20)
    private String colorTemperature;
}