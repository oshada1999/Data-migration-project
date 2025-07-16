package com.cba.datamigration.mapper;

import com.cba.datamigration.dto.MachineDTO;

public class MachineRowMapper implements RowMapper<MachineDTO> {

    @Override
    public MachineDTO mapRow(String[] row) {
        return new MachineDTO(
                row[0], // McnCode
                row[1], // McnName
                row[2], // Model
                row[3], // Make
                row[4], // Warranty
                parseInteger(row[5]),
                row[6], // DepCode
                parseInteger(row[7]),
                row[8], // EstimateServiceTime
                row[9], // McnCategory
                row[10], // AddedBy
                null, // AddedOn
                row.length > 12 ? row[12] : null
        );
    }

    private Integer parseInteger(String s) {
        try {
            return (s != null && !s.isEmpty()) ? Integer.parseInt(s.trim()) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
