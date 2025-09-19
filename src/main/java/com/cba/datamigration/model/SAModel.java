package com.cba.datamigration.model;

import com.cba.datamigration.dto.ServiceAgreementDTO;
import com.cba.datamigration.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class SAModel {
    private final Map<String, String> saTypeMap = new HashMap<>();

    public void saveWarrantyTemplate(List<ServiceAgreementDTO> serviceAgreementDTOS) throws SQLException {
        if (serviceAgreementDTOS == null || serviceAgreementDTOS.isEmpty()) {
            System.out.println("No service agreements to process");
            return;
        }

        final int BATCH_SIZE = 1000;
        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Store unique combinations of period and service count
            Map<String, Map.Entry<Integer, Integer>> uniqueCombinations = new HashMap<>();

            for (ServiceAgreementDTO sa : serviceAgreementDTOS) {
                int period = 0;

                if (!sa.getPeriod().equalsIgnoreCase("null") && !sa.getPeriod().trim().isEmpty()) {
                    try {
                        period = (int) Double.parseDouble(sa.getPeriod().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid period: " + sa.getPeriod());
                        continue;
                    }
                } else if (!sa.getWarrantyPeriod().equalsIgnoreCase("null") && !sa.getWarrantyPeriod().trim().isEmpty()) {
                    try {
                        period = (int) Double.parseDouble(sa.getWarrantyPeriod().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid warranty period: " + sa.getWarrantyPeriod());
                        continue;
                    }
                }

                // Skip warranties < 3 months
                if (period < 3) {
                    continue;
                }

                int services = 0;
                try {
                    services = (int) Double.parseDouble(sa.getServiceCount());
                    if (services > 20) {
                        services = 0;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid service count: " + sa.getServiceCount());
                    continue;
                }

                // Normalize warranty periods
                period = normalizeWarrantyPeriod(period);

                // Map SA to LABOUR_ONLY and WA to WARRANTY
                String mappedSaType = sa.getSaType().equals("SA") ? "LABOUR_ONLY" :
                        sa.getSaType().equals("WA") ? "WARRANTY" : sa.getSaType();

                // Create unique key for this combination using mapped type
                String key = period + "-" + services + "-" + mappedSaType;
                uniqueCombinations.putIfAbsent(key, Map.entry(period, services));
                saTypeMap.put(key, mappedSaType);
            }

            // Check existing combinations in database
            Set<String> existingCombinations = getExistingCombinations(conn);

            // Insert new combinations
            String insertSql = "INSERT INTO service_agreement_template " +
                    "(duration_months, service_count, sr_category_id, service_agreement_type, name, is_default, " +
                    "inserted_at, created_at, created_by, saved_at, updated_at, updated_by, is_deleted, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                int count = 0;
                for (Map.Entry<String, Map.Entry<Integer, Integer>> entry : uniqueCombinations.entrySet()) {
                    if (existingCombinations.contains(entry.getKey())) {
                        //System.out.println("Skipping existing combination: " + entry.getKey());
                        continue;
                    }

                    int period = entry.getValue().getKey();
                    int services = entry.getValue().getValue();
                    String saType = saTypeMap.get(entry.getKey());

                    ps.setInt(1, period);
                    ps.setInt(2, services);
                    ps.setLong(3, 1);  // sr_category_id
                    ps.setString(4, saType);  // Use mapped type (LABOUR_ONLY or WARRANTY)
                    ps.setString(5, String.format("%s (%d months) - %d service", saType, period, services));
                    ps.setBoolean(6, true);  // is_default

                    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                    ps.setTimestamp(7, now);  // inserted_at
                    ps.setTimestamp(8, now);  // created_at
                    ps.setString(9, "Migration System");  // created_by
                    ps.setTimestamp(10, now); // saved_at
                    ps.setTimestamp(11, now); // updated_at
                    ps.setString(12, "Migration System"); // updated_by
                    ps.setBoolean(13, false); // is_deleted
                    ps.setBoolean(14, true);  // is_active

                    ps.addBatch();
                    count++;

                    if (count % BATCH_SIZE == 0) {
                        ps.executeBatch();
                    }
                }

                if (count % BATCH_SIZE != 0) {
                    ps.executeBatch();
                }
            }

            conn.commit();
            //System.out.println("Successfully processed " + uniqueCombinations.size() + " service agreement templates");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            //System.err.println("Failed to save service agreement templates: " + e.getMessage());
            throw new SQLException("Error saving service agreement data", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }

    private int normalizeWarrantyPeriod(int period) {
        if (period >= 3 && period < 6) return 3;
        else if (period >= 6 && period < 12) return 6;
        else if (period >= 12 && period < 24) return 12;
        else if (period >= 24 && period < 36) return 24;
        else if (period >= 36 && period < 48) return 36;
        else if (period >= 48 && period < 60) return 48;
        else if (period >= 60) return 60;
        return period;
    }

    private Set<String> getExistingCombinations(Connection conn) throws SQLException {
        Set<String> existingCombinations = new HashSet<>();
        String checkExistingSql = "SELECT duration_months, service_count, service_agreement_type FROM service_agreement_template";

        try (PreparedStatement ps = conn.prepareStatement(checkExistingSql)) {
            var rs = ps.executeQuery();
            while (rs.next()) {
                String combination = rs.getInt("duration_months") + "-" +
                        rs.getInt("service_count") + "-" +
                        rs.getString("service_agreement_type");
                existingCombinations.add(combination);
            }
        }
        return existingCombinations;
    }
}
