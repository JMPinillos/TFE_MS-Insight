package com.msinsight.services.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementFiller<T> {
    void fill(PreparedStatement ps, T item) throws SQLException;
}
