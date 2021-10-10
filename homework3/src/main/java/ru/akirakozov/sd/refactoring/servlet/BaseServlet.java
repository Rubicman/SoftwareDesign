package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.service.HtmlUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseServlet extends HttpServlet {
    protected final ProductRepository productRepository;
    protected final HtmlUtils htmlUtils;

    protected BaseServlet(ProductRepository productRepository, HtmlUtils htmlUtils) {
        this.productRepository = productRepository;
        this.htmlUtils = htmlUtils;
    }

    protected void setHeaders(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected List<Product> getProducts(ResultSet resultSet) throws Exception {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(new Product(resultSet.getString("name"), resultSet.getInt("price")));
        }
        return products;
    }
}
