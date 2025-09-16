package com.cba.datamigration.model;

import com.cba.datamigration.dto.ProductDTO;
import com.cba.datamigration.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ProductModel {

    // Cache foreign key lookups
    private Map<String, Integer> parentCache = new HashMap<>();
    private Map<String, Integer> customerCache = new HashMap<>();
    private Map<String, Integer> machineCache = new HashMap<>();
    private Map<String, Integer> productCache = new HashMap<>();
    private Map<String, Integer> agreementCache = new HashMap<>();

    private void getParentId(Connection conn, List<ProductDTO> productDTOS) throws SQLException {
        // Collect unique clnCodes.
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
//        for (Map.Entry<String, Integer> entry : parentCache.entrySet()) {
//            if (entry.getValue() < 0 && entry.getKey().startsWith("CLN")) {
//                System.out.println(entry.getKey());
//            }
//
//        }
        System.out.println("Total entries in parentCache: " + parentCache.size());

    }


    private void getProductId(Connection conn, List<ProductDTO> productDTOS) throws SQLException {
        // Collect unique clnCodes.
        Set<String> salecodes = productDTOS.stream()
                .map(ProductDTO::getSaleCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toSet());

        if (salecodes.isEmpty()) return;

        // Build SQL with IN clause
        String sql = "SELECT saleCode, id FROM product WHERE saleCode IN (" +
                salecodes.stream().map(code -> "?").collect(Collectors.joining(",")) + ")";
        System.out.println(sql);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (String code : salecodes) {
                ps.setString(index++, code);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productCache.put(rs.getString("saleCode"), rs.getInt("id"));
                }
            }
        }

        // Default -1 for not found
        for (String code : salecodes) {
            productCache.putIfAbsent(code, -1);
        }
//        for (Map.Entry<String, Integer> entry : parentCache.entrySet()) {
//            if (entry.getValue() < 0 && entry.getKey().startsWith("CLN")) {
//                System.out.println(entry.getKey());
//            }
//
//        }
        System.out.println("Total entries in ProductChas: " + productCache.size());

    }

    private void getAgreementId(Connection conn, List<ProductDTO> productDTOS) throws SQLException {
        // Collect unique clnCodes.
        Set<String> salecodes = productDTOS.stream()
                .map(ProductDTO::getSaleCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toSet());

        if (salecodes.isEmpty()) return;

        // Build SQL with IN clause
        String sql = "SELECT saleCode, id FROM service_agreement WHERE saleCode IN (" +
                salecodes.stream().map(code -> "?").collect(Collectors.joining(",")) + ")";
        System.out.println(sql);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (String code : salecodes) {
                ps.setString(index++, code);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    agreementCache.put(rs.getString("saleCode"), rs.getInt("id"));
                }
            }
        }

        // Default -1 for not found
        for (String code : salecodes) {
            agreementCache.putIfAbsent(code, -1);
        }
//        for (Map.Entry<String, Integer> entry : parentCache.entrySet()) {
//            if (entry.getValue() < 0 && entry.getKey().startsWith("CLN")) {
//                System.out.println(entry.getKey());
//            }
//
//        }
        System.out.println("Total entries in agreementChas: " + agreementCache.size());

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
            if (entry.getValue() < 0 && entry.getKey().startsWith("CUS")) {
                System.out.println("Customer :" + entry.getKey());
            }

        }
        System.out.println("Total entries in customer: " + customerCache.size());
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
//        for (Map.Entry<String, Integer> entry : machineCache.entrySet()) {
//            if (entry.getValue() > 0) {
//                System.out.println(entry.getKey());
//            }
//
//        }
        System.out.println("Total entries in machine: " + machineCache.size());
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
                    System.out.println(product.getSaleCode());
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

    public void saveWarrantyTemplate(List<ProductDTO> products) throws SQLException {

        final int BATCH_SIZE = 1000;
        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Use Set to avoid duplicate warranty-services combos
            Set<String> uniqueWarrantyServices = new HashSet<>();

            for (ProductDTO product : products) {
                int warranty = Integer.parseInt(product.getWarranty());
                int services = Integer.parseInt(product.getServices());

                // If services > 20 → reset to 0
                if (services > 20) {
                    services = 0;
                }

                // Skip warranties < 3
                if (warranty < 3) {
                    continue;
                }

                // Normalize warranty
                if (warranty >= 3 && warranty < 6) warranty = 3;
                else if (warranty >= 6 && warranty < 12) warranty = 6;
                else if (warranty >= 12 && warranty < 24) warranty = 12;
                else if (warranty >= 24 && warranty < 36) warranty = 24;
                else if (warranty >= 36 && warranty < 48) warranty = 36;
                else if (warranty >= 48 && warranty < 60) warranty = 48;
                else if (warranty == 60) warranty = 60;

                // Combine warranty+services as a unique key
                String key = warranty + "-" + services;
                uniqueWarrantyServices.add(key);
            }

            String insertSql = "INSERT INTO service_agreement_template " +
                    "(duration_months, service_count, sr_category_id, service_agreement_type, name, is_default, " +
                    "inserted_at, created_at, created_by, saved_at, updated_at, updated_by, is_deleted, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                int count = 0;

                for (String warrantyService : uniqueWarrantyServices) {
                    String[] parts = warrantyService.split("-");
                    int warranty = Integer.parseInt(parts[0]);
                    int service = Integer.parseInt(parts[1]);

                    ps.setInt(1, warranty);
                    ps.setInt(2, service); // corrected here
                    ps.setLong(3, 3);
                    ps.setString(4, "WARRANTY");
                    ps.setString(5, "WARRANTY (" + warranty + " months) - " + service + " service");
                    ps.setBoolean(6, true);
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(9, "Migration System Created!");
                    ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(12, "Migration System Created!");
                    ps.setBoolean(13, false);
                    ps.setBoolean(14, true);

                    ps.addBatch();

                    if (++count % BATCH_SIZE == 0) {
                        ps.executeBatch();
                    }
                }

                // Execute remaining batch
                ps.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Insert failed: " + e.getMessage());
            throw new RuntimeException("Error saving product data", e);
        }
    }


    public void saveWarrantyAgreement(List<ProductDTO> products) throws SQLException {
        final int BATCH_SIZE = 1000;
        Connection conn = null;

        // Simple cache for templateId
        Map<String, Integer> templateCache = new HashMap<>();

        String searchSql = "SELECT id FROM service_agreement_template " +
                "WHERE is_active = ? AND duration_months = ? " +
                "AND service_agreement_type = ? AND service_count = ? AND sr_category_id = ?";

        String insertSql = "INSERT INTO service_agreement (" +
                "owner_id, sa_template_id, issue_date, termination_date, sa_price, " +
                "gross_total, net_total, paid_amount, vat_amount, sscl_amount, remark, " +
                "is_terminated, contact_person_name, contact_person_phone, contact_person_designation, " +
                "current_node_id, current_workflow_id, inserted_at, created_at, created_by, " +
                "saved_at, updated_at, updated_by, is_deleted, is_active, saleCode" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Load parent IDs (assuming this method populates parentCache)
            getParentId(conn, products);
            getCustomerId(conn, products);
            getMachineId(conn, products);

            try (PreparedStatement searchStmt = conn.prepareStatement(searchSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                int batchCount = 0;

                for (ProductDTO product : products) {
                    int warranty = Integer.parseInt(product.getWarranty());
                    int services = Integer.parseInt(product.getServices());

                    // If services > 20 → reset to 0
                    if (services > 20) {
                        services = 0;
                    }

                    // Skip warranties < 3
                    if (warranty < 3) {
                        continue;
                    }

                    // Normalize warranty
                    if (warranty >= 3 && warranty < 6) warranty = 3;
                    else if (warranty >= 6 && warranty < 12) warranty = 6;
                    else if (warranty >= 12 && warranty < 24) warranty = 12;
                    else if (warranty >= 24 && warranty < 36) warranty = 24;
                    else if (warranty >= 36 && warranty < 48) warranty = 36;
                    else if (warranty >= 48 && warranty < 60) warranty = 48;
                    else if (warranty == 60) warranty = 60;

                    // Build cache key (include sr_category_id = 3)
                    String key = warranty + "-" + services + "-3";

                    // Check cache first
                    if (!templateCache.containsKey(key)) {
                        // Query DB only if not in cache
                        searchStmt.setBoolean(1, true);
                        searchStmt.setInt(2, warranty);
                        searchStmt.setString(3, "WARRANTY");
                        searchStmt.setInt(4, services);
                        searchStmt.setInt(5, 3);

                        try (ResultSet rs = searchStmt.executeQuery()) {
                            if (rs.next()) {
                                int templateId = rs.getInt("id");
                                templateCache.put(key, templateId); // Save to cache
                            } else {
                                System.out.println("No matching template found for product: " + product.getSaleCode());
                                templateCache.put(key, -1); // still cache to avoid re-querying
                            }
                        } catch (SQLException e) {
                            System.err.println("search failed: " + e.getMessage());
                            throw new RuntimeException("Error search data", e);
                        }
                    }

                    // Get parentId + templateId
                    String clnCode = product.getOwner();
                    String cusCode = product.getCusCode();
                    String mcnCode = product.getMcnCode();

                    int customerID = customerCache.getOrDefault(cusCode, -1);
                    int machineID = machineCache.getOrDefault(mcnCode, -1);
                    int parentId = parentCache.getOrDefault(clnCode, -1);

                    int templateId = templateCache.getOrDefault(key, -1);
                    System.out.println("Customer ID: " + customerID + "machineID: " + machineID + "parentID: " + parentId + "templateID: " + templateId );
                    // Skip if no template or parent found
                    if (parentId != -1 && templateId != -1 && customerID != -1 && machineID != -1) {

                        String startDateStr = String.valueOf(product.getDate());
                        int warrantyMonths = Integer.parseInt(product.getWarranty());
                        LocalDate currentDate = LocalDate.now();
                        // Parse the start date
                        LocalDate startDate = LocalDate.parse(startDateStr);

                        // Calculate expiry date by adding months
                        LocalDate expiryDate = startDate.plusMonths(warrantyMonths);

                        // Format the output (optional)
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        boolean isTerminated = currentDate.isAfter(expiryDate);
                        boolean isActive = true;
                        if (isTerminated) {
                            isActive = false;
                        }

                        // Set parameters for insert
                        insertStmt.setLong(1, parentId); // owner_id
                        insertStmt.setLong(2, templateId); // sa_template_id
                        insertStmt.setDate(3, product.getDate()); // issue_date (current date)
                        insertStmt.setDate(4, Date.valueOf(expiryDate.format(formatter))); // termination_date (null)
                        insertStmt.setBigDecimal(5, BigDecimal.ZERO); // sa_price (default 0)
                        insertStmt.setBigDecimal(6, BigDecimal.ZERO); // gross_total (default 0)
                        insertStmt.setBigDecimal(7, BigDecimal.ZERO); // net_total (default 0)
                        insertStmt.setBigDecimal(8, BigDecimal.ZERO); // paid_amount (default 0)
                        insertStmt.setBigDecimal(9, BigDecimal.ZERO); // vat_amount (default 0)
                        insertStmt.setBigDecimal(10, BigDecimal.ZERO); // sscl_amount (default 0)
                        insertStmt.setString(11, product.getOther()); // remark (empty)
                        insertStmt.setBoolean(12, isTerminated); // is_terminated (false)
                        insertStmt.setString(13, product.getContactPer()); // contact_person_name (empty)
                        insertStmt.setString(14, product.getPhone()); // contact_person_phone (empty)
                        insertStmt.setString(15, null); // contact_person_designation (empty)
                        insertStmt.setNull(16, 0); // current_node_id (null)
                        insertStmt.setNull(17, 0); // current_workflow_id (null)
                        insertStmt.setTimestamp(18, new Timestamp(System.currentTimeMillis())); // inserted_at
                        insertStmt.setTimestamp(19, new Timestamp(System.currentTimeMillis())); // created_at
                        insertStmt.setString(20, "Migration System Added!"); // created_by
                        insertStmt.setTimestamp(21, new Timestamp(System.currentTimeMillis())); // saved_at
                        insertStmt.setTimestamp(22, new Timestamp(System.currentTimeMillis())); // updated_at
                        insertStmt.setString(23, "Migration System Added!"); // updated_by
                        insertStmt.setBoolean(24, false); // is_deleted
                        insertStmt.setBoolean(25, isActive); // is_active
                        insertStmt.setString(26, product.getSaleCode()); // salecode


                        insertStmt.addBatch();
                        batchCount++;
                    } else {
                        System.out.println(product.getSaleCode());
                    }
                    // Execute and commit every BATCH_SIZE
                    if (batchCount % BATCH_SIZE == 0) {
                        insertStmt.executeBatch();
                        conn.commit();
                        System.out.println("Inserted batch of size: " + BATCH_SIZE);
                    }
                }

                // Execute remaining batches
                if (batchCount % BATCH_SIZE != 0) {
                    insertStmt.executeBatch();
                    System.out.println("Inserted final batch of size: " + (batchCount % BATCH_SIZE));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Insert failed: " + e.getMessage());
            throw new RuntimeException("Error saving product data", e);
        }
    }

    public void saveWarrantyProduct(List<ProductDTO> products) throws SQLException {
        final int BATCH_SIZE = 1000;
        Connection conn = DBConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        String insertSqlProduct = "INSERT INTO service_agreement_product (" +
                "service_agreement_id, price, issue_date, termination_date, product_id, " +
                "inserted_at, created_at, created_by, saved_at, updated_at, updated_by, " +
                "is_deleted, is_active" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        getProductId(conn, products);
        getAgreementId(conn, products);

        try (PreparedStatement ps = conn.prepareStatement(insertSqlProduct)) {
            int count = 0;
            for (ProductDTO product : products) {
                int warranty = Integer.parseInt(product.getWarranty());
                // Skip warranties < 3
                if (warranty < 3) {
                    continue;
                }
                String saleCode = product.getSaleCode();

                int productId = productCache.getOrDefault(saleCode, -1);
                int agreementId = agreementCache.getOrDefault(saleCode, -1);

                String startDateStr = String.valueOf(product.getDate());
                int warrantyMonths = Integer.parseInt(product.getWarranty());
                // Parse the start date
                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate currentDate = LocalDate.now();
                // Calculate expiry date by adding months
                LocalDate expiryDate = startDate.plusMonths(warrantyMonths);

                // Format the output (optional)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                boolean isTerminated = currentDate.isAfter(expiryDate);
                boolean isActive = true;
                if (isTerminated) {
                    isActive = false;
                }

                if (productId != -1 && agreementId != -1) {
                    ps.setLong(1, agreementId);
                    ps.setBigDecimal(2, BigDecimal.ZERO);
                    ps.setDate(3, product.getDate());
                    ps.setDate(4, Date.valueOf(expiryDate.format(formatter)));
                    ps.setLong(5, productId);
                    ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    ps.setString(8, "Migration System Added!");
                    ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                    ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
                    ps.setString(11, "Migration System Added!");
                    ps.setBoolean(12, false);
                    ps.setBoolean(13, isActive);

                    ps.addBatch();
                    count++;
                } else {
                    System.out.println(product.getSaleCode());
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
