package com.smartcity.pattern.flyweight;

import com.smartcity.model.StreetlightType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class StreetlightTypeFactory {

    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    private final ConcurrentHashMap<String, StreetlightType> typeCache = new ConcurrentHashMap<>();

    @Transactional
    public StreetlightType getStreetlightType(String modelName, Integer wattage, String colorTemperature) {
        String key = modelName.toLowerCase() + "-" + wattage + "-" + colorTemperature.toLowerCase();

        if (!typeCache.containsKey(key)) {

            List<StreetlightType> existingTypes = entityManager.createQuery(
                            "SELECT s FROM StreetlightType s WHERE s.modelName = :model AND" +
                                    " s.wattage = :wattage AND s.colorTemperature = :color", StreetlightType.class)
                    .setParameter("model", modelName)
                    .setParameter("wattage", wattage)
                    .setParameter("color", colorTemperature)
                    .getResultList();

            if (!existingTypes.isEmpty()) {
                typeCache.put(key, existingTypes.get(0));
            } else {
                StreetlightType newType = new StreetlightType();
                newType.setModelName(modelName);
                newType.setWattage(wattage);
                newType.setColorTemperature(colorTemperature);

                entityManager.persist(newType);
                entityManager.flush();

                typeCache.put(key, newType);
                System.out.println("New Flyweight Object Created: " + key);
            }
        } else {
            System.out.println("Reused Existing Flyweight Object from Cache: " + key);
        }

        return typeCache.get(key);
    }
}