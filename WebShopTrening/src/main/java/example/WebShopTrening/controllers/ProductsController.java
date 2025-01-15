package example.WebShopTrening.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import example.WebShopTrening.ProductService.IProductService;
import example.WebShopTrening.ProductService.Product;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
	
    private final IProductService productService;
	
	public ProductsController(IProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<Product>> getProducts(
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortDir,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String search
            ) {
		Sort.Direction direction = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
		return ResponseEntity.ok(productService.findAllWithFilters(pageable, search));
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
