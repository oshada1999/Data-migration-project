package com.cba.datamigration.mapper;

public interface RowMapper<T> {
    T mapRow(String[] row);
}
