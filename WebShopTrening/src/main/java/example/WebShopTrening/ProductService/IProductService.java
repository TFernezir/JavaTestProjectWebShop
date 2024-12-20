package example.WebShopTrening.ProductService;

import example.WebShopTrening.entitets.Product;

public interface IProductService {

	Iterable<Product> getAllProducts();

	Product getProduct(Long productId);

	Product createProduct(Product newProduct);

	Product updateProduct(Long id, Product newProduct);

	void deleteProduct(Long productId);

}