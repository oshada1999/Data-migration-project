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
    private static final String INSERT_SQL = "INSERT INTO customer (" +
            "parent_customer_id, address, district, province, town, join_date, " +
            "contact_person_name, contact_person_phone, contact_person_designation, " +
            "contact_fax, contact_email, created_by, created_at, updated_by, updated_at, " +
            "is_deleted, is_active, cusCode, latitude, longitude) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public void saveChildDataToTable(List<ChildDTO> batch) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Step 1: Cache all clnCodes with their parent IDs
            Map<String, Integer> parentMap = getParentIds(conn, batch);

            try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_SQL)) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();

                for (ChildDTO child : batch) {
                    Integer parentId = parentMap.get(child.getClnCode());

                    if (parentId != null) {
                        int paramIndex = 1;

                        insertStmt.setInt(paramIndex++, parentId);
                        insertStmt.setString(paramIndex++, child.getAddress());
                        insertStmt.setString(paramIndex++, child.getDistrict());
                        insertStmt.setString(paramIndex++, child.getProvince());
                        insertStmt.setString(paramIndex++, child.getLocation());

                        insertStmt.setDate(paramIndex++, child.getJoinDate());

                        insertStmt.setString(paramIndex++, child.getContactPer());
                        insertStmt.setString(paramIndex++, child.getPhone());
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
                        insertStmt.setDouble(paramIndex++, 0); // latitude
                        insertStmt.setDouble(paramIndex++, 0); // longitude

                        insertStmt.addBatch(); // Batch insert
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
}
