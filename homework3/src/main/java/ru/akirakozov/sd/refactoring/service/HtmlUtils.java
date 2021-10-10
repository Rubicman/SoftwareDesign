package ru.akirakozov.sd.refactoring.service;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlUtils {
    public void addHtml(PrintWriter writer, String body) {
        writer.println("<html><body>");
        if (!body.isEmpty()) {
            writer.println(body);
        }
        writer.println("</body></html>");
    }

    public void addHtml(PrintWriter writer, List<Product> products) {
        addHtml(writer, getProductString(products));
    }

    public void addHtml(PrintWriter writer, String article, List<Product> products) {
        addHtml(writer, article + System.lineSeparator() + getProductString(products));
    }

    public void addHtml(PrintWriter writer, String article, int value) {
        addHtml(writer, article + System.lineSeparator() + value);
    }

    private String getProductString(List<Product> products) {
        return products.stream()
                .map(product -> product.getName() + "\t" + product.getPrice() + "</br>")
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
