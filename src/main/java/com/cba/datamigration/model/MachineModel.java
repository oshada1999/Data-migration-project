package com.cba.datamigration.model;

import com.cba.datamigration.dto.MachineDTO;
import com.cba.datamigration.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class MachineModel {

    private Integer getOrInsertAndGetId(Connection conn, String tableName, String columnName, String value,
                                        String foreignKeyName, Integer foreignKeyValue, String mcnCode) throws SQLException {

        // Step 1: Try selecting the existing ID
        String selectSql;
        if (foreignKeyName != null && foreignKeyValue != null) {
            selectSql = "SELECT id FROM " + tableName + " WHERE " + columnName + " = ? AND " + foreignKeyName + " = ?";
        } else {
            selectSql = "SELECT id FROM " + tableName + " WHERE " + columnName + " = ?";
        }

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, value);
            if (foreignKeyName != null && foreignKeyValue != null) {
                selectStmt.setInt(2, foreignKeyValue);
            }

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // Step 2: Prepare to insert with audit fields
        String insertSql;
        if (foreignKeyName != null && foreignKeyValue != null && mcnCode != null) {
            insertSql = "INSERT INTO " + tableName + " (" + columnName + ", " + foreignKeyName + ", mcnCode, " +
                    "created_at, created_by, updated_at, updated_by, is_deleted, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (foreignKeyName != null && foreignKeyValue != null) {
            insertSql = "INSERT INTO " + tableName + " (" + columnName + ", " + foreignKeyName + ", " +
                    "created_at, created_by, updated_at, updated_by, is_deleted, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            insertSql = "INSERT INTO " + tableName + " (" + columnName + ", " +
                    "created_at, created_by, updated_at, updated_by, is_deleted, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            int paramIndex = 1;

            insertStmt.setString(paramIndex++, value);

            if (foreignKeyName != null && foreignKeyValue != null) {
                insertStmt.setInt(paramIndex++, foreignKeyValue);
            }

            if (mcnCode != null && foreignKeyName != null && foreignKeyValue != null) {
                insertStmt.setString(paramIndex++, mcnCode);
            }

            // Add audit fields
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            insertStmt.setTimestamp(paramIndex++, now);     // created_at
            insertStmt.setString(paramIndex++, "migration system");   // created_by
            insertStmt.setTimestamp(paramIndex++, now);     // updated_at
            insertStmt.setString(paramIndex++, "migration system");   // updated_by
            insertStmt.setBoolean(paramIndex++, false);     // is_deleted
            insertStmt.setBoolean(paramIndex++, true);      // is_active

            insertStmt.executeUpdate();

            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Failed to insert into " + tableName);
            }
        }
    }

    public void saveMachineDataToTables(List<MachineDTO> machines) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            int total = machines.size();
            for (MachineDTO c : machines) {
                System.out.println( "Total ithuru" + total--);
                Integer categoryId = getOrInsertAndGetId(conn, "product_category", "name", c.getMcnName(), null, null, null);
                Integer brandId = getOrInsertAndGetId(conn, "product_brand", "name", c.getMake(), "product_category_id", categoryId, null);
                getOrInsertAndGetId(conn, "product_model", "name", c.getModel(), "product_brand_id", brandId, c.getMcnCode());
            }

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException("Error saving machine-related data", e);
        }
    }
}
