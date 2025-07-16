package com.cba.datamigration.mapper;

import com.cba.datamigration.dto.CustomerDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainClientRowMapper implements RowMapper<CustomerDTO> {

    @Override
    public CustomerDTO mapRow(String[] row) {
        return new CustomerDTO(
                row[0], // ClnCode
                row[1], // ClnName
                row[2], // ClnSegment
                row[3], // ClnSector
                row[4], // ClnIndustry
                row[5], // NotSure
                row[6], // addedBy
                parseDateTime(row[7]), // addedOn
                row.length > 8 ? row[8] : null // ClnPriority
        );
    }

    private LocalDateTime parseDateTime(String input) {
        try {
            if (input != null && !input.trim().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm[:ss]]");
                return LocalDateTime.parse(input.trim(), formatter);
            }
        } catch (Exception e) {
            // You can log the error or return null if parsing fails
        }
        return null;
    }
}
