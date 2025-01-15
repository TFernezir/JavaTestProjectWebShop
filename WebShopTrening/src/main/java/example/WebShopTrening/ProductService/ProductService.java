package example.WebShopTrening.ProductService;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import example.WebShopTrening.CustomExceptions.ResourceNotFoundException;
import example.WebShopTrening.repositories.ProductRepository;

@Service
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
public class ProductService implements IProductService {

	private final ProductRepository productRepository;
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	@Cacheable("products")
    public List<Product> findAllWithFilters(Pageable pageable, String search) {
		logger.info("Executing findAllWithFilters with " + pageable + " and search string " + search);
		if (search == null || search.trim().isEmpty()) {
	        return productRepository.findAll(pageable).getContent();
	    }
        return productRepository.findByNameContainingIgnoreCaseOrAboutContainingIgnoreCase(search, search, pageable).stream().toList();
	}

	@Override
	@Cacheable("productCache")
	 public Product getProduct(Long productId) {
		logger.info("Executing " + getClass() + " input: " + productId);
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
    }
	 
	@Override
	@CachePut(value = "productCache", key = "#result.id")
	public Product createProduct(Product newProduct) {
	    logger.info("Executing " + getClass() + " input: " + newProduct);
	    Product product = new Product();
	    product.setName(newProduct.getName());
	    product.setPrice(newProduct.getPrice());
	    product.setAbout(newProduct.getAbout());

	    return productRepository.save(product);
	}

	@Override
	@CachePut(value = "productCache", key = "#id")
	public Product updateProduct(Long id, Product newProduct) {
		logger.info("Executing " + getClass() + " with id: " + id + " input: " + newProduct);
	    Product product = productRepository.findById(id).get();
	    product.setName(newProduct.getName());
	    product.setPrice(newProduct.getPrice());
	    product.setAbout(newProduct.getAbout());
	    
	    return productRepository.save(product);
	}

	@Override
	@CacheEvict(value = "productCache", key = "#productId")
    public void deleteProduct(Long productId) {
		logger.info("Executing delete from " + getClass() + " input: " + productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product with ID " + productId + " not found");
        }
        productRepository.deleteById(productId);
    }
}