package ru.akirakozov.sd.refactoring.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductRepository {

    public void executeSql(String sql, ResultSetHandler resultSetHandler) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                if (resultSetHandler != null) {
                    ResultSet rs = stmt.executeQuery(sql);

                    resultSetHandler.handle(rs);

                    rs.close();
                } else {
                    stmt.executeUpdate(sql);
                }
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeSql(String sql) {
        executeSql(sql, null);
    }

    @FunctionalInterface
    public interface ResultSetHandler {
        void handle(ResultSet rs) throws Exception;
    }
}
