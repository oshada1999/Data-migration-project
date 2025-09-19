package com.cba.datamigration.mapper;

import com.cba.datamigration.dto.ServiceAgreementDTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ServiceAgreementRowMapper implements RowMapper<ServiceAgreementDTO> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public ServiceAgreementDTO mapRow(String[] row) {
//        Date saEndDate = parseDate(row, 9);
//        Date saStartDate = parseDate(row, 16);

        return new ServiceAgreementDTO(
                row.length > 0 ? row[0] : null,   // SACode
                row.length > 1 ? row[1] : null,   // SA_Period
                parseDouble(row, 2),          // CumAmount
                parseDouble(row, 3),          // Discount
                row.length > 4 ? row[4] : null,   // VatType
                parseDouble(row, 5),          // Vat
                row.length > 6 ? row[6] : null,   // NbtType
                parseDouble(row, 7),          // Nbtz
                parseDouble(row, 8),          // Total
                row.length > 9 ? row[9] : null,                        // SA_EndDate (LocalDate)
                row.length > 10 ? row[10] : null,            // service_count
                row.length > 11 ? row[11] : null, // SA_Id
                row.length > 12 ? row[12] : null, // SA_Category_remark
                row.length > 13 ? row[13] : null, // FollowUp_Category
                row.length > 14 ? row[14] : null, // Phone
                row.length > 15 ? row[15] : null, // ContactPer
                row.length > 16 ? row[16] : null,                      // SAStartDate (LocalDate)
                row.length > 17 ? row[17] : null, // ClnCode
                row.length > 18 ? row[18] : null, // delivery_method
                row.length > 19 ? row[19] : null, // Status
                row.length > 20 ? row[20] : null, // SA_status
                row.length > 21 ? row[21] : null, // SA_Type
                row.length > 22 ? row[22] : null, // SaleCode
                row.length > 23 ? row[23] : null,  // SA_Category
                row.length > 24 ? row[24] : null  // warranty_period
        );
    }

    private Date parseDate(String[] row, int index) {
        String value = getValue(row, index);
        if (value == null || value.isEmpty()) return null;

        try {
            java.util.Date parsed = DATE_FORMAT.parse(value);
            return new Date(parsed.getTime());
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + row[index]); // Use `index`, not hardcoded 15
            e.printStackTrace();
            return null;
        }
    }

    private Double parseDouble(String[] row, int index) {
        String value = getValue(row, index);

        if (value == null || value.isEmpty()) return null;
        //System.out.println("new value : " + value);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + value + "Index" + index);
            return null;
        }
    }

    private String getValue(String[] row, int index) {
        if (row != null && row.length > index && row[index] != null) {
            String value = row[index].trim();
            return value.equalsIgnoreCase("NULL") ? null : value;
        }
        return null;
    }

    private Integer parseInteger(String[] row, int index) {
        if (row.length > index && row[index] != null && !row[index].trim().isEmpty()) {
            try {
                return Integer.parseInt(row[index].trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid Integer at index " + index + ": " + row[index]);
            }
        }
        return null;
    }
}
