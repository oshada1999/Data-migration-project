package com.cba.datamigration.mapper;

import com.cba.datamigration.dto.ChildDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class ChildRowMapper implements RowMapper<ChildDTO> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public ChildDTO mapRow(String[] row) {
        Date joinDate = null;
        if (row.length > 15 && row[15] != null && !row[15].isEmpty()) {
            try {
                java.util.Date parsedDate = DATE_FORMAT.parse(row[15]);
                joinDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                System.err.println("Error parsing date: " + row[15]);
                e.printStackTrace();
            }
        }

        return new ChildDTO(
                row[0],  // CusCode
                row[1],  // ClnCode
                row[2],  // Address
                row[3],  // Location
                row[4],  // Province
                row[5],  // ContactPer
                row[6],  // SaAttn
                row[7],  // Phone
                row[8],  // Fax
                row[9],  // Email
                row[10], // CusType
                row[11], // AddedBy
                row[12], // AddedOn
                row.length > 13 ? row[13] : null, // ID
                row.length > 14 ? row[14] : null, // District
                joinDate  // JoinDate
        );
    }
}