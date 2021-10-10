package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductRepository productRepository;

    public GetProductsServlet(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        productRepository.executeSql("SELECT * FROM PRODUCT", resultSet -> {
            response.getWriter().println("<html><body>");

            while (resultSet.next()) {
                String  name = resultSet.getString("name");
                int price  = resultSet.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");
        });

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
