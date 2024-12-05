package example.WebShopTrening;

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

import example.WebShopTrening.CustomExceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
	private final ProductRepository productRepository;
	
	private ProductsController(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Product>> getAllProducts ()
	{        
		var products = productRepository.findAll();
		
    	return ResponseEntity.ok(products);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProduct (@PathVariable Long productId)
	{
		var product = productRepository.findById(productId);		

		return ResponseEntity.ok(product.get());
		
	}
	
	@PostMapping()
	public ResponseEntity<String> createProduct(@Valid @RequestBody Product newProduct, UriComponentsBuilder ucb)
	{
		var product = new Product(
				null,
				newProduct.name(),
				newProduct.price(),
				newProduct.about()
				);
		
		var savedProduct = productRepository.save(product);
		
		URI location = ucb.path("/products/{id}")
                .buildAndExpand(savedProduct.id())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable Long id,@Valid @RequestBody Product newProduct)
	{
		var product = new Product(id, newProduct.name(), newProduct.price(), newProduct.about());
		
		productRepository.save(product);
		
		return ResponseEntity.ok(product);
	}

	
	@DeleteMapping("/{productId}")
	public  ResponseEntity<Object> deleteProduct(@PathVariable Long productId){
		productRepository.findById(productId)
		        .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found"));
		
        productRepository.deleteById(productId); 
        return ResponseEntity.ok().build();
	    }		
}
