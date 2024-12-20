package example.WebShopTrening.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import example.WebShopTrening.ProductService.IProductService;
import example.WebShopTrening.entitets.Product;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
	
    private final IProductService productService;
	
	public ProductsController(IProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<Iterable<Product>> getAllProducts() {
		Iterable<Product> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
		Product product = productService.getProduct(productId);

		return ResponseEntity.ok(product);
	}

	@PostMapping()
	public ResponseEntity<String> createProduct(@Valid @RequestBody Product newProduct, UriComponentsBuilder ucb) {
		Product savedProduct = productService.createProduct(newProduct);

		URI location = ucb.path("/products/{id}")
				.buildAndExpand(savedProduct.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable Long id, @Valid @RequestBody Product newProduct) {
		Product product = productService.updateProduct(id, newProduct);

		return ResponseEntity.ok(product);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.ok().build();
	}
}
