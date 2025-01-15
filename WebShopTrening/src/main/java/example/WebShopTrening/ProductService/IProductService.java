package example.WebShopTrening.ProductService;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface IProductService {

	List<Product> findAllWithFilters(Pageable pagable, String search);

	Product getProduct(Long productId);

	Product createProduct(Product newProduct);

	Product updateProduct(Long id, Product newProduct);

	void deleteProduct(Long productId);

}