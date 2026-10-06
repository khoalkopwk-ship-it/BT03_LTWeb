package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JPAConfig {
    private static final EntityManagerFactory FACTORY = createFactory();

    private JPAConfig() {
    }

    private static EntityManagerFactory createFactory() {
        Map<String, Object> overrides = new HashMap<>();

        putIfPresent(overrides, "jakarta.persistence.jdbc.url",
                firstNonBlank(System.getenv("DB_URL"), System.getProperty("db.url")));
        putIfPresent(overrides, "jakarta.persistence.jdbc.user",
                firstNonBlank(System.getenv("DB_USER"), System.getProperty("db.user")));
        putIfPresent(overrides, "jakarta.persistence.jdbc.password",
                firstNonBlank(System.getenv("DB_PASSWORD"), System.getProperty("db.password")));

        return Persistence.createEntityManagerFactory("dataSource", overrides);
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void close() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }

    private static void putIfPresent(Map<String, Object> map, String key, String value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
