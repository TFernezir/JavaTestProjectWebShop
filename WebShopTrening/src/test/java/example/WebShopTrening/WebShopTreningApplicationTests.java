package example.WebShopTrening;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class WebShopTreningApplicationTests {
	
	@Autowired
    TestRestTemplate restTemplate;

	@Test
	void getAllProductsShouldReturnAllProducts() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray products = documentContext.read("$");
        assertThat(products.size()).isGreaterThan(0);
	}
	
	@Test
	void getProductShouldReturnProduct() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number productId = documentContext.read("$.id");
        assertThat(productId).isEqualTo(1);
        
        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Laptop");
        
        String about = documentContext.read("$.about");
        assertThat(about).isEqualTo("Jeftini laptop");
        
        Double price = documentContext.read("$.price");
        assertThat(price).isEqualTo(321);
	}
	
	@Test
    void getProductShouldReturnErrorWhenNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/products/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Resource not found at path: /products/999");
    }
	
   @Test
    void getProductShouldReturnErrorWhenInvalidIdType() {
        ResponseEntity<String> response = restTemplate.getForEntity("/products/abc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("[productId] should be of type [java.lang.Long]");
    }
   
   @Test 
   void createProductShouldCreateNewProduct() {
       Product newProduct = new Product(null, "New Product", 149.99, "New Description");
       ResponseEntity<String> createResponse = 
           restTemplate.postForEntity("/products", newProduct, String.class);       
       assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
       
       URI locationOfNewProduct = createResponse.getHeaders().getLocation();
       ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewProduct, String.class);
       assertThat(locationOfNewProduct).isNotNull();
       
       DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
       Number id = documentContext.read("$.id");
       Double price = documentContext.read("$.price");
       
       assertThat(id).isNotNull();
       assertThat(price).isEqualTo(149.99);
       
   }
   
   @Test
   void createProductShouldFailWithEmptyName() {
       Product invalidProduct = new Product(null, "", 99.99, "Test Description");
       ResponseEntity<String> response = 
           restTemplate.postForEntity("/products", invalidProduct, String.class);
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       
       DocumentContext documentContext = JsonPath.parse(response.getBody());
       String message = documentContext.read("$.message");
       assertThat(message).isEqualTo("Field [NAME] String cannot be empty or whitespace");
   }
   
   @Test
   void createProductShouldFailWithInvalidPrice() {
       Product invalidProduct = new Product(null, "cz", -1.0, "");
       ResponseEntity<String> response = 
           restTemplate.postForEntity("/products", invalidProduct, String.class);
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       
       DocumentContext documentContext = JsonPath.parse(response.getBody());
       String message = documentContext.read("$.message");
       assertThat(message).contains("Field [PRICE] Price must be greater than 0");
   }
   
   @Test
   void updateProductShouldSucceedWithValidInput() {
       Product validUpdate = new Product(1L, "Updated Product", 150.00, "Updated Description");
       ResponseEntity<String> response = restTemplate.exchange(
           "/products/1",
           HttpMethod.PUT,
           new HttpEntity<>(validUpdate),
           String.class
       );
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       DocumentContext documentContext = JsonPath.parse(response.getBody());
       String name = documentContext.read("$.name");
       Number price = documentContext.read("$.price");
       assertThat(name).isEqualTo("Updated Product");
       assertThat(price).isEqualTo(150.00);
   }
   
   @Test
   void updateProductShouldFailWithEmptyName() {
       Product invalidUpdate = new Product(1L, "", 99.99, "Test Description");
       ResponseEntity<String> response = restTemplate.exchange(
           "/products/1",
           HttpMethod.PUT,
           new HttpEntity<>(invalidUpdate),
           String.class
       );
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       DocumentContext documentContext = JsonPath.parse(response.getBody());
       String message = documentContext.read("$.message");
       assertThat(message).isEqualTo("Field [NAME] String cannot be empty or whitespace");
   }
   
   @Test
   void updateProductShouldFailWithNegativePrice() {
       Product invalidUpdate = new Product(1L, "Test Product", -10.0, "Test Description");
       ResponseEntity<String> response = restTemplate.exchange(
           "/products/1",
           HttpMethod.PUT,
           new HttpEntity<>(invalidUpdate),
           String.class
       );
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       DocumentContext documentContext = JsonPath.parse(response.getBody());
       String message = documentContext.read("$.message");
       assertThat(message).isEqualTo("Field [PRICE] Price must be greater than 0");
   }
   
   @Test
   void deleteProductShouldSucceedWithValidId() {
       // First create a product to delete
       Product newProduct = new Product(null, "Product to Delete", 99.99, "Test Description");
       ResponseEntity<String> createResponse = restTemplate.postForEntity("/products", newProduct, String.class);
       String location = createResponse.getHeaders().getLocation().getPath();
       
       // Then delete it
       ResponseEntity<String> deleteResponse = restTemplate.exchange(
           location,
           HttpMethod.DELETE,
           null,
           String.class
       );
       
       assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
       
       // Verify it's deleted
       ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
       assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
   }
   
   @Test
   void deleteProductShouldFailWithInvalidId() {
       ResponseEntity<String> response = restTemplate.exchange(
           "/products/999",
           HttpMethod.DELETE,
           null,
           String.class
       );
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
       DocumentContext documentContext = JsonPath.parse(response.getBody());
       String message = documentContext.read("$.message");
       assertThat(message).contains("Product with ID 999 not found");
   }
}
