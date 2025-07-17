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
        conn.setAutoCommit(false);

        String insertSql = "INSERT INTO parent_customer " +
                "(name, is_vat, is_sscl, business_type, inserted_at, created_at, created_by, saved_at, updated_at, updated_by, is_deleted, is_active, clnCode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            for (MainClientDTO c : customers) {
                ps.setString(1, c.getClnName());
                ps.setBoolean(2, false);
                ps.setBoolean(3, false);
                ps.setString(4, "Migration System Added");

                // Audit fields (you can customize how these are generated)
                LocalDateTime now = LocalDateTime.now();
                ps.setTimestamp(5, Timestamp.valueOf(now)); // inserted_at
                ps.setTimestamp(6, Timestamp.valueOf(now)); // created_at
                ps.setString(7, "Migration system");                  // created_by
                ps.setTimestamp(8, Timestamp.valueOf(now)); // saved_at
                ps.setTimestamp(9, Timestamp.valueOf(now)); // updated_at
                ps.setString(10, "Migration system");                 // updated_by
                ps.setBoolean(11, false);                   // is_deleted
                ps.setBoolean(12, true);                    // is_active
                ps.setString(13, c.getClnCode());   //clnCode

                ps.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException("Error saving customer data to main_client table", e);
        }
    }
}
