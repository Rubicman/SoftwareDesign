package ru.akirakozov.sd.refactoring.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductRepository {
    private final String connectionUrl;

    public ProductRepository(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public void executeSql(String sql, ResultSetHandler resultSetHandler) {
        try {
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                Statement statement = connection.createStatement();
                if (resultSetHandler != null) {
                    ResultSet resultSet = statement.executeQuery(sql);

                    resultSetHandler.handle(resultSet);

                    resultSet.close();
                } else {
                    statement.executeUpdate(sql);
                }
                statement.close();
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
