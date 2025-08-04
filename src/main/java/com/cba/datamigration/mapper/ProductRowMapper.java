package com.cba.datamigration.mapper;

import com.cba.datamigration.dto.ProductDTO;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductRowMapper implements RowMapper<ProductDTO> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public ProductDTO mapRow(String[] row) {
        ProductDTO dto = new ProductDTO();

        // Set string fields
        dto.setSaleCode(getValue(row, 0));
        dto.setCusCode(getValue(row, 1));
        dto.setMcnCode(getValue(row, 2));
        dto.setSerial(getValue(row, 3));
        dto.setDate(parseDate(row, 4));
        dto.setWarranty(getValue(row, 5));
        dto.setServices(getValue(row, 6));
        dto.setOther(getValue(row, 7));
        dto.setSaAttn(getValue(row, 8));
        dto.setContactPer(getValue(row, 9));
        dto.setPhone(getValue(row, 10));
        dto.setEmail(getValue(row, 11));
        dto.setFax(getValue(row, 12));
        dto.setStatus(getValue(row, 13));
        dto.setOwner(getValue(row, 14));
        dto.setTid(getValue(row, 15));
        dto.setMid(getValue(row, 16));
        dto.setUser(getValue(row, 17));
        dto.setEditDate(parseDate(row, 18));
        dto.setTime(getValue(row, 19));
        dto.setInvNo(getValue(row, 20));
        dto.setInvDate(parseDate(row, 22));
        dto.setBlock(getValue(row, 23));
        dto.setSpecialInfo(getValue(row, 24));
        dto.setSimNumber(getValue(row, 25));
        dto.setServiceReq(getValue(row, 26));

        // Set numeric fields
        dto.setPrice(parseDouble(row, 21));
        dto.setRent(parseDouble(row, 27));
        dto.setLatitude(parseDouble(row, 28));
        dto.setLongitude(parseDouble(row, 29));

        return dto;
    }

    private String getValue(String[] row, int index) {
        if (row != null && row.length > index && row[index] != null) {
            String value = row[index].trim();
            return value.equalsIgnoreCase("NULL") ? null : value;
        }
        return null;
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
    try {
        return Double.parseDouble(value);
    } catch (NumberFormatException e) {
        System.err.println("Invalid number format: " + value);
        return null;
    }
}
}
