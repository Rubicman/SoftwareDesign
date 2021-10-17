package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.service.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends BaseServlet {


    public GetProductsServlet(ProductRepository productRepository, HtmlUtils htmlUtils) {
        super(productRepository, htmlUtils);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        productRepository.executeSql(
                "SELECT * FROM PRODUCT",
                resultSet -> htmlUtils.addHtml(response.getWriter(), getProducts(resultSet))
        );

        setHeaders(response);
    }
}
