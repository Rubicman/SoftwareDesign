package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.service.HtmlUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductRepository productRepository;
    private final HtmlUtils htmlUtils;

    public GetProductsServlet(ProductRepository productRepository, HtmlUtils htmlUtils) {
        this.productRepository = productRepository;
        this.htmlUtils = htmlUtils;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        productRepository.executeSql("SELECT * FROM PRODUCT", resultSet -> {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getString("name"), resultSet.getInt("price")));
            }
            htmlUtils.addHtml(response.getWriter(), products);
        });

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
