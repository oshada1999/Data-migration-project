package com.cba.datamigration.model;

import com.cba.datamigration.dto.ProductDTO;
import com.cba.datamigration.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductModel {

    // Cache foreign key lookups
    private Map<String, Integer> parentCache = new HashMap<>();
    private Map<String, Integer> customerCache = new HashMap<>();
    private Map<String, Integer> machineCache = new HashMap<>();

    private void getParentId(Connection conn, List<ProductDTO> productDTOS) throws SQLException {
        // Collect unique clnCodes
        Set<String> clnCodes = productDTOS.stream()
                .map(ProductDTO::getOwner)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toSet());

        if (clnCodes.isEmpty()) return;

        // Build SQL with IN clause
        String sql = "SELECT clnCode, id FROM parent_customer WHERE clnCode IN (" +
                clnCodes.stream().map(code -> "?").collect(Collectors.joining(",")) + ")";
        System.out.println(sql);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (String code : clnCodes) {
                ps.setString(index++, code);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    parentCache.put(rs.getString("clnCode"), rs.getInt("id"));
                }
            }
        }

        // Default -1 for not found
        for (String code : clnCodes) {
            parentCache.putIfAbsent(code, -1);
        }
        for (Map.Entry<String, Integer> entry : parentCache.entrySet()) {
            if (entry.getValue() < 0) {
                System.out.println(entry.getKey());
            }

        }
        System.out.println("Total entries in parentCache: " + parentCache.size());

    }


    private void getCustomerId(Connection conn, List<ProductDTO> productDTOS) throws SQLException {
        Set<String> cusCodes = productDTOS.stream()
                .map(ProductDTO::getCusCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toSet());

        if (cusCodes.isEmpty()) return;

        String sql = "SELECT cusCode, id FROM customer WHERE cusCode IN (" +
                cusCodes.stream().map(code -> "?").collect(Collectors.joining(",")) + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (String code : cusCodes) {
                ps.setString(index++, code);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    customerCache.put(rs.getString("cusCode"), rs.getInt("id"));
                }
            }
        }

        for (String code : cusCodes) {
            customerCache.putIfAbsent(code, -1);
        }

        for (Map.Entry<String, Integer> entry : customerCache.entrySet()) {
            if (entry.getValue() < 0) {
                System.out.println(entry.getKey());
            }

        }
        System.out.println("Total entries in parentCache: " + customerCache.size());
    }


    private void getMachineId(Connection conn, List<ProductDTO> productDTOS) throws SQLException {
        Set<String> mcnCodes = productDTOS.stream()
                .map(ProductDTO::getMcnCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toSet());

        if (mcnCodes.isEmpty()) return;

        String sql = "SELECT mcnCode, id FROM product_model WHERE mcnCode IN (" +
                mcnCodes.stream().map(code -> "?").collect(Collectors.joining(",")) + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (String code : mcnCodes) {
                ps.setString(index++, code);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    machineCache.put(rs.getString("mcnCode"), rs.getInt("id"));
                }
            }
        }

        for (String code : mcnCodes) {
            machineCache.putIfAbsent(code, -1);
        }
        for (Map.Entry<String, Integer> entry : machineCache.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println(entry.getKey());
            }

        }
        System.out.println("Total entries in parentCache: " + machineCache.size());
    }


    private boolean getIsActive(int status) {
        if (status == 1) {
            return true;
        } else if (status == 2 || status == 0) {
            return false;
        }
        return true;
    }

    public void saveProduct(List<ProductDTO> productDTOS) throws SQLException {
        final int BATCH_SIZE = 1000; // You can adjust the size
        Connection conn = DBConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        String insertSql = "INSERT INTO product (customer_id, owner_id, product_model_id, " +
                "product_sub_status, serial, purchase_order_number, issue_date, expiry_date, " +
                "merchant_id, terminal_id, location, price, paid_amount, current_sa_id, " +
                "inserted_at, created_at, created_by, saved_at, updated_at, updated_by, " +
                "is_deleted, is_active, saleCode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Preload all caches before batch loop
        getParentId(conn, productDTOS);
        getCustomerId(conn, productDTOS);
        getMachineId(conn, productDTOS);

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            int count = 0;
            for (ProductDTO product : productDTOS) {
                String clnCode = product.getOwner();
                String cusCode = product.getCusCode();
                String mcnCode = product.getMcnCode();

                int parentID = parentCache.getOrDefault(clnCode, -1);
                int customerID = customerCache.getOrDefault(cusCode, -1);
                int machineID = machineCache.getOrDefault(mcnCode, -1);
                boolean isActive = getIsActive(Integer.parseInt(product.getStatus()));

                if (parentID != -1 && customerID != -1 && machineID != -1) {
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp timestamp = Timestamp.valueOf(now);

                    ps.setInt(1, customerID);
                    ps.setInt(2, parentID);
                    ps.setInt(3, machineID);
                    ps.setInt(4, 1); // Default product_sub_status
                    ps.setString(5, product.getSerial());
                    ps.setString(6, "0"); // purchase_order_number
                    ps.setDate(7, product.getDate());
                    ps.setDate(8, null); // expiry_date
                    ps.setString(9, (product.getMid() == null || product.getMid().isEmpty()) ? null : product.getMid());
                    ps.setString(10, product.getTid());
                    ps.setString(11, "UNKNOWN"); // location
                    ps.setBigDecimal(12, BigDecimal.valueOf(product.getPrice()));
                    ps.setBigDecimal(13, BigDecimal.valueOf(product.getPrice()));
                    ps.setObject(14, null); // current_sa_id
                    ps.setTimestamp(15, timestamp);
                    ps.setTimestamp(16, timestamp);
                    ps.setString(17, "Migration System");
                    ps.setTimestamp(18, timestamp);
                    ps.setTimestamp(19, timestamp);
                    ps.setString(20, "Migration System");
                    ps.setBoolean(21, false);
                    ps.setBoolean(22, isActive);
                    ps.setString(23, product.getSaleCode());

                    ps.addBatch();
                    count++;
                } else {
                    System.out.println( product.getSaleCode());
                }

                // Execute and commit every BATCH_SIZE
                if (count % BATCH_SIZE == 0) {
                    ps.executeBatch();
                    conn.commit();
                    System.out.println("Inserted batch of size: " + BATCH_SIZE);
                }
            }

            // Insert remaining records
            if (count % BATCH_SIZE != 0) {
                ps.executeBatch();
                conn.commit();
                System.out.println("Inserted final batch of size: " + (count % BATCH_SIZE));
            }
        } catch (Exception e) {
            conn.rollback();
            System.err.println("Insert failed: " + e.getMessage());
            throw new RuntimeException("Error saving product data", e);
        }
    }

}
