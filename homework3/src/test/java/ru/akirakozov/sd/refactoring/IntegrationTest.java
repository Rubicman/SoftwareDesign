package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IntegrationTest {
    private static final String TEST_DATABASE = "test.db";
    private static final String BASE_URL = "http://localhost:8081";

    private Server server;
    private HttpClient httpClient;

    @BeforeEach
    public void beforeEach() throws Exception {
        server = Main.startServer();
        httpClient = HttpClient.newHttpClient();
    }

    @BeforeAll
    static void dropDatabase() {
        try {
            Files.deleteIfExists(Path.of(TEST_DATABASE));
        } catch (IOException e) {
            // nothing
        }
    }

    @AfterEach
    public void afterEach() throws Exception {
        server.stop();
        dropDatabase();
    }

    @Test
    public void launchTest() {
    }

    @Test
    public void successResponseTest() {
        sendRequest("/get-products");
    }

    @Test
    void emptyProductsTest() {
        HttpResponse<String> actualResponse = sendRequest("/get-products");
        String expectedHtml = getExpectedHtml(List.of(), "");
        assertEquals(HttpServletResponse.SC_OK, actualResponse.statusCode());
        assertEquals(expectedHtml, actualResponse.body());
    }

    @Test
    void addProductTest() {
        Product product = new Product("1", 1);
        HttpResponse<String> actualAddResponse = sendRequest("/add-product?name=%s&price=%d".formatted(product.getName(), product.getPrice()));
        assertEquals(HttpServletResponse.SC_OK, actualAddResponse.statusCode());
        assertEquals("OK" + System.lineSeparator(), actualAddResponse.body());
        HttpResponse<String> actualGetResponse = sendRequest("/get-products");
        String expectedHtml = getExpectedHtml(List.of(product), "");
        assertEquals(expectedHtml, actualGetResponse.body());
    }

    @Test
    void addManyProductsTest() {
        List<Product> products = List.of(
                new Product("1", 1),
                new Product("1", 2),
                new Product("2", 2),
                new Product("3", 3)
        );
        products.forEach(product -> {
            HttpResponse<String> actualResponse = sendRequest("/add-product?name=%s&price=%d".formatted(product.getName(), product.getPrice()));
            assertEquals("OK" + System.lineSeparator(), actualResponse.body());
        });

        HttpResponse<String> actualResponse = sendRequest("/get-products?name=1");
        String expectedHtml = getExpectedHtml(products, "");
        assertEquals(expectedHtml, actualResponse.body());
    }

    @Test
    void queriesTest() {
        List<Product> products = List.of(
                new Product("1", 1),
                new Product("3", 2),
                new Product("5", 5),
                new Product("2", 1),
                new Product("4", 3)
        );

        products.forEach(product -> {
            HttpResponse<String> actualResponse = sendRequest("/add-product?name=%s&price=%d".formatted(product.getName(), product.getPrice()));
            assertEquals("OK" + System.lineSeparator(), actualResponse.body());
        });

        HttpResponse<String> actualResponse = sendRequest("/query?command=max");
        String expectedHtml = getExpectedHtml(List.of(products.get(2)), "<h1>Product with max price: </h1>" + System.lineSeparator());
        assertEquals(expectedHtml, actualResponse.body());

        actualResponse = sendRequest("/query?command=min");
        expectedHtml = getExpectedHtml(List.of(products.get(0)), "<h1>Product with min price: </h1>" + System.lineSeparator());
        assertEquals(expectedHtml, actualResponse.body());

        actualResponse = sendRequest("/query?command=sum");
        expectedHtml = "<html><body>" + System.lineSeparator() +
                "Summary price: " + System.lineSeparator() +
                12 + System.lineSeparator() +
                "</body></html>" + System.lineSeparator();
        assertEquals(expectedHtml, actualResponse.body());

        actualResponse = sendRequest("/query?command=count");
        expectedHtml = "<html><body>" + System.lineSeparator() +
                "Number of products: " + System.lineSeparator() +
                5 + System.lineSeparator() +
                "</body></html>" + System.lineSeparator();
        assertEquals(expectedHtml, actualResponse.body());
    }

    private String getExpectedHtml(List<Product> products, String prefix) {
        return "<html><body>" + System.lineSeparator() +
                prefix +
                products.stream()
                        .map(product -> product.getName() + "\t" + product.getPrice() + "</br>" + System.lineSeparator())
                        .collect(Collectors.joining()) +
                "</body></html>" + System.lineSeparator();
    }

    private HttpResponse<String> sendRequest(String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(BASE_URL + path))
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            fail("Exception must not be thrown", e);
        }
        throw new RuntimeException("Must not be here");
    }

}
