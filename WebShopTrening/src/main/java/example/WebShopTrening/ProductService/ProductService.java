package example.WebShopTrening.ProductService;

import java.util.NoSuchElementException;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import example.WebShopTrening.CustomExceptions.ResourceNotFoundException;
import example.WebShopTrening.entitets.Product;
import example.WebShopTrening.repositories.ProductRepository;

@Service
@Secured({ "USER", "ADMIN" })
public class ProductService implements IProductService {

	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Override
	public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

	@Override
	 public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
    }
	 
	@Override
	public Product createProduct(Product newProduct) {
	    Product product = new Product();
	    product.setName(newProduct.getName());
	    product.setPrice(newProduct.getPrice());
	    product.setAbout(newProduct.getAbout());
	    
	    return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Long id, Product newProduct) {
	    Product product = productRepository.findById(id).get();
	    product.setName(newProduct.getName());
	    product.setPrice(newProduct.getPrice());
	    product.setAbout(newProduct.getAbout());
	    
	    return productRepository.save(product);
	}

	@Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product with ID " + productId + " not found");
        }
        productRepository.deleteById(productId);
    }
}