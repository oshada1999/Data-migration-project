package com.cba.datamigration.model;

import com.cba.datamigration.dto.MainClientDTO;
import com.cba.datamigration.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class MainClientModel {

    public void saveMainClientDataToTable(List<MainClientDTO> customers) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        conn.setAutoCommit(false); // transactional control

        String insertSql = "INSERT INTO parent_customer " +
                "(name, is_vat, is_sscl, business_type, inserted_at, created_at, created_by, saved_at, updated_at, updated_by, is_deleted, is_active, clnCode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {

            for (MainClientDTO c : customers) {
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);

                ps.setString(1, c.getClnName());
                ps.setBoolean(2, false);
                ps.setBoolean(3, false);
                ps.setString(4, "INDIVIDUAL");
                ps.setTimestamp(5, timestamp);
                ps.setTimestamp(6, timestamp);
                ps.setString(7, "Migration system");
                ps.setTimestamp(8, timestamp);
                ps.setTimestamp(9, timestamp);
                ps.setString(10, "Migration system");
                ps.setBoolean(11, false);
                ps.setBoolean(12, true);
                ps.setString(13, c.getClnCode());

                ps.addBatch();

            }
            ps.executeBatch();
            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            System.err.println("Insert failed: " + e.getMessage());
            throw new RuntimeException("Error saving customer data to parent_customer table", e);
        }
    }
}
