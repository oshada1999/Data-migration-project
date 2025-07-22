package com.cba.datamigration.model;

import com.cba.datamigration.dto.ChildDTO;
import com.cba.datamigration.util.DBConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class ChildModel {

    private static final String SELECT_ID_SQL = "SELECT clnCode, id FROM parent_customer WHERE clnCode IN (%s)";
    private static final String SELECT_Cus_ID_SQL = "SELECT cusCode, id FROM customer WHERE cusCode IN (%s)";
    private static final String INSERT_SQL = "INSERT INTO customer (" +
            "parent_customer_id, address, district, province, town, join_date, " +
            "contact_person_name, contact_person_phone, contact_person_designation, " +
            "contact_fax, contact_email, created_by, created_at, updated_by, updated_at, " +
            "is_deleted, is_active, cusCode, latitude, longitude) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String PHONE_INSERT_SQL = "INSERT INTO `customer_phone number` (" +
            "customer_id, phone, inserted_at, created_at, created_by, " +
            "saved_at, updated_at, updated_by, is_deleted, is_active) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    public void saveChildDataToTable(List<ChildDTO> batch) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Step 1: Cache all clnCodes with their parent IDs
            Map<String, Integer> parentMap = getParentIds(conn, batch);

            try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_SQL)) {
                LocalDateTime now = LocalDateTime.now();

                for (ChildDTO child : batch) {
                    Integer parentId = parentMap.get(child.getClnCode());


                    if (parentId != null) {
                        int paramIndex = 1;

                        insertStmt.setInt(paramIndex++, parentId);
                        insertStmt.setString(paramIndex++, child.getAddress());
                        insertStmt.setString(paramIndex++, child.getDistrict().toUpperCase());

                        insertStmt.setString(paramIndex++, child.getProvince().toUpperCase());
                        insertStmt.setString(paramIndex++, child.getLocation().toUpperCase());

                        insertStmt.setDate(paramIndex++, child.getJoinDate());

                        insertStmt.setString(paramIndex++, child.getContactPer());
                        insertStmt.setString(paramIndex++, null);
                        insertStmt.setString(paramIndex++, "Migration System Added Please Update"); // contact_person_designation
                        insertStmt.setString(paramIndex++, child.getFax());
                        insertStmt.setString(paramIndex++, child.getEmail());
                        insertStmt.setString(paramIndex++, "Migration System"); // created_by
                        insertStmt.setTimestamp(paramIndex++, Timestamp.valueOf(now));
                        insertStmt.setString(paramIndex++, "Migration System"); // updated_by
                        insertStmt.setTimestamp(paramIndex++, Timestamp.valueOf(now));
                        insertStmt.setBoolean(paramIndex++, false); // is_deleted
                        insertStmt.setBoolean(paramIndex++, true);  // is_active
                        insertStmt.setString(paramIndex++, child.getCusCode());
                        insertStmt.setDouble(paramIndex++, 0.0); // latitude
                        insertStmt.setDouble(paramIndex++, 0.0); // longitude

                        insertStmt.addBatch(); // Batch insert
                    }else {
                        System.out.println(child.getClnCode());
                    }
                }

                insertStmt.executeBatch(); // Execute all at once
                conn.commit(); // Commit once
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Error saving child data", e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, Integer> getParentIds(Connection conn, List<ChildDTO> batch) throws SQLException {
        Map<String, Integer> resultMap = new HashMap<>();

        Set<String> clnCodes = batch.stream()
                .map(ChildDTO::getClnCode)
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toSet());

        if (clnCodes.isEmpty()) return resultMap;

        // Prepare dynamic IN clause
        String placeholders = clnCodes.stream().map(code -> "?").collect(Collectors.joining(", "));
        String query = String.format(SELECT_ID_SQL, placeholders);

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int index = 1;
            for (String code : clnCodes) {
                stmt.setString(index++, code);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String code = rs.getString("clnCode");
                    int id = rs.getInt("id");
                    resultMap.put(code, id);
                }
            }
        }

        return resultMap;
    }

    public void phoneDataToTable(List<ChildDTO> batch) {
        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Step 1: Cache all cusCodes with their customer IDs
            Map<String, Integer> cusMap = getCustomerIds(conn, batch);

            try (PreparedStatement insertStmt = conn.prepareStatement(PHONE_INSERT_SQL)) {
                LocalDateTime now = LocalDateTime.now();

                for (ChildDTO child : batch) {
                    Integer cusId = cusMap.get(child.getCusCode());

                    if (cusId != null) {
                        String rawPhones = child.getPhone();
                        if (rawPhones != null && !rawPhones.trim().isEmpty()) {
                            String[] phoneNumbers = rawPhones.split("/");

                            for (String phone : phoneNumbers) {
                                phone = phone.trim();
                                System.out.println("Phone " + phone);
                                if (!phone.isEmpty()) {
                                    int paramIndex = 1;

                                    insertStmt.setInt(paramIndex++, cusId);                         // customer_id
                                    insertStmt.setString(paramIndex++, phone.equals("") ? "-" : phone);// phone
                                    insertStmt.setTimestamp(paramIndex++, Timestamp.valueOf(now));  // inserted_at
                                    insertStmt.setTimestamp(paramIndex++, Timestamp.valueOf(now));  // created_at
                                    insertStmt.setString(paramIndex++, "Migration System");         // created_by
                                    insertStmt.setTimestamp(paramIndex++, Timestamp.valueOf(now));  // saved_at
                                    insertStmt.setTimestamp(paramIndex++, Timestamp.valueOf(now));  // updated_at
                                    insertStmt.setString(paramIndex++, "Migration System");         // updated_by
                                    insertStmt.setBoolean(paramIndex++, false);                     // is_deleted
                                    insertStmt.setBoolean(paramIndex++, true);                      // is_active

                                    insertStmt.addBatch();
                                }
                            }
                        }
                    } else {
                        System.out.println("Missing customer ID for cusCode: " + child.getCusCode());
                    }
                }

                insertStmt.executeBatch(); // Execute all at once
                conn.commit(); // Commit once
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Error saving phone data", e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, Integer> getCustomerIds(Connection conn, List<ChildDTO> batch) throws SQLException {
        Map<String, Integer> resultMap = new HashMap<>();

        Set<String> cusCodes = batch.stream()
                .map(ChildDTO::getCusCode)
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toSet());

        if (cusCodes.isEmpty()) return resultMap;

        // Prepare dynamic IN clause
        String placeholders = cusCodes.stream().map(code -> "?").collect(Collectors.joining(", "));
        String query = String.format(SELECT_Cus_ID_SQL, placeholders);

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int index = 1;
            for (String code : cusCodes) {
                stmt.setString(index++, code);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String code = rs.getString("cusCode");
                    int id = rs.getInt("id");
                    resultMap.put(code, id);
                }
            }
        }

        return resultMap;
    }
}
