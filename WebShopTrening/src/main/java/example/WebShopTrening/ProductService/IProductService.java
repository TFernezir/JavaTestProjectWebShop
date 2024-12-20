package example.WebShopTrening.ProductService;

import org.springframework.data.domain.Page;

public interface IProductService {

	Page<Product> findAllWithFilters(ProductPage productPage, ProductSearchCriteria searchCriteria);

	Product getProduct(Long productId);

	Product createProduct(Product newProduct);

	Product updateProduct(Long id, Product newProduct);

	void deleteProduct(Long productId);

}