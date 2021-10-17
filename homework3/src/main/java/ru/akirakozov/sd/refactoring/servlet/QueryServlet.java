package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.service.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends BaseServlet {

    public QueryServlet(ProductRepository productRepository, HtmlUtils htmlUtils) {
        super(productRepository, htmlUtils);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            productRepository.executeSql(
                    "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                    resultSet -> htmlUtils.addHtml(response.getWriter(), "<h1>Product with max price: </h1>", getProducts(resultSet))
            );
        } else if ("min".equals(command)) {
            productRepository.executeSql(
                    "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                    resultSet -> htmlUtils.addHtml(response.getWriter(), "<h1>Product with min price: </h1>", getProducts(resultSet))
            );
        } else if ("sum".equals(command)) {
            productRepository.executeSql("SELECT SUM(price) FROM PRODUCT",
                    resultSet -> {
                        resultSet.next();
                        htmlUtils.addHtml(response.getWriter(), "Summary price: ", resultSet.getInt(1));
                    });
        } else if ("count".equals(command)) {
            productRepository.executeSql("SELECT COUNT(*) FROM PRODUCT",
                    resultSet -> {
                        resultSet.next();
                        htmlUtils.addHtml(response.getWriter(), "Number of products: ", resultSet.getInt(1));
                    });
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        setHeaders(response);
    }

}
