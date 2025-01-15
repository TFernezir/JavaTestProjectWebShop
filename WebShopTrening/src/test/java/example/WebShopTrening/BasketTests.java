package example.WebShopTrening;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import example.WebShopTrening.BasketService.BasketItem;
import example.WebShopTrening.ProductService.Product;
import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class BasketTests {

    @Autowired
    TestRestTemplate restTemplate;

    private BasketItem createTestBasketItem(Long productId, Integer quantity, Double price) {
        BasketItem item = new BasketItem();
        Product product = new Product();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(price);
        return item;
    }

    @Test
    void getAllBasketsShouldReturnBaskets() {
        ResponseEntity<String> response = restTemplate.getForEntity("/basket", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray baskets = documentContext.read("$");
        assertThat(baskets.size()).isGreaterThan(0);
    }
    
    @Test
    void getBasketByIdShouldReturnBasket() {
        ResponseEntity<String> response = restTemplate.getForEntity("/basket/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        ArrayList<BasketItem> basketItems = documentContext.read("$.items");
        Number totalAmount = documentContext.read("$.totalAmount");
        assertThat(basketItems.size()).isEqualTo(2);
        assertThat(totalAmount).isEqualTo(100.0);
    }

    @Test
    void getBasketShouldReturnErrorWhenNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/basket/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void addItemToBasketShouldCreateNewBasket() {
        BasketItem newItem = createTestBasketItem(1L, 2, 10.0);
        ResponseEntity<String> response = restTemplate.postForEntity("/basket/items?userId=1", newItem, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        Number totalAmount = documentContext.read("$.totalAmount");
        String status = documentContext.read("$.status");

        assertThat(id).isNotNull();
        assertThat(totalAmount).isEqualTo(20.0);
        assertThat(status).isEqualTo("ACTIVE");
        
        ResponseEntity<String> getAll = restTemplate.getForEntity("/basket", String.class);
        DocumentContext basketsContext = JsonPath.parse(getAll.getBody());
        JSONArray baskets = basketsContext.read("$");
        assertThat(baskets.size()).isEqualTo(3);
    }

    @Test
    void addItemShouldFailWithInvalidQuantity() {
        BasketItem invalidItem = createTestBasketItem(1L, -1, 10.0);
        ResponseEntity<String> response = restTemplate.postForEntity("/basket/items?userId=1", invalidItem, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Field [QUANTITY] Quantity must be greater than 0");
    }

    @Test
    void addItemShouldFailWithInvalidPrice() {
        BasketItem invalidItem = createTestBasketItem(1L, 1, -10.0);
        ResponseEntity<String> response = restTemplate.postForEntity("/basket/items?userId=1", invalidItem, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Field [UNITPRICE] Price must be greater than 0");
    }

    @Test
    void updateBasketItemShouldSucceedUpdatingItem() {
        ResponseEntity<String> response = restTemplate.postForEntity("/basket/items?userId=1",
                createTestBasketItem(1L, 1, 10.0), String.class);
        DocumentContext createContext = JsonPath.parse(response.getBody());
        Number basketId = createContext.read("$.id");
        
        ResponseEntity<String> updateResponse = restTemplate.postForEntity(
                "/basket/items?basketId=" + basketId + "&userId=1", 
                createTestBasketItem(1L, 2, 15.0),
                String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteBasketShouldSucceed() {
        BasketItem newItem = createTestBasketItem(1L, 2, 10.0);
        ResponseEntity<String> response = restTemplate.postForEntity("/basket/items?userId=1", newItem, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            "/basket/" + id,
            HttpMethod.DELETE,
            null,
            String.class
        );
        
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/basket/" + id, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void removeItemShouldSucceed() {
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/basket/items?userId=1",
                createTestBasketItem(1L, 1, 10.0), String.class);

        DocumentContext createContext = JsonPath.parse(createResponse.getBody());
        Number basketId = createContext.read("$.id");
        Number itemId = createContext.read("$.items[0].id");
        
        ResponseEntity<String> removeResponse = restTemplate.exchange(
                "/basket/" + basketId + "/items/" + itemId,
                HttpMethod.DELETE, 
                null, 
                String.class);

        assertThat(removeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext removeContext = JsonPath.parse(removeResponse.getBody());
        JSONArray items = removeContext.read("$.items");
        Number totalAmount = removeContext.read("$.totalAmount");

        assertThat(items.size()).isEqualTo(0);
        assertThat(totalAmount).isEqualTo(0.0);
    }
}