package example.WebShopTrening.ProductService;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import example.WebShopTrening.CustomExceptions.ResourceNotFoundException;
import example.WebShopTrening.repositories.ProductCriteriaRepository;
import example.WebShopTrening.repositories.ProductRepository;

@Service
//@Secured({ "ROLE_USER", "ROLE_ADMIN" })
public class ProductService implements IProductService {

	private final ProductRepository productRepository;
	private final ProductCriteriaRepository productCriteriaRepository;
	
	public ProductService(ProductRepository productRepository, 
						ProductCriteriaRepository productCriteriaRepository) {
		this.productRepository = productRepository;
		this.productCriteriaRepository = productCriteriaRepository;
	}

	@Override
    public Page<Product> findAllWithFilters(ProductPage productPage, ProductSearchCriteria searchCriteria) {

        return productCriteriaRepository.findAllWithFilters(productPage, searchCriteria);
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