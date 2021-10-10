package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    private static final String CONNECTION_URL = "jdbc:sqlite:test.db";

    public static Server startServer() throws Exception {
        try (Connection c = DriverManager.getConnection(CONNECTION_URL)) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductRepository productRepository = new ProductRepository(CONNECTION_URL);

        context.addServlet(new ServletHolder(new AddProductServlet(productRepository)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productRepository)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productRepository)),"/query");

        server.start();
        return server;
    }

    public static void main(String[] args) throws Exception {
        startServer().join();
    }
}
