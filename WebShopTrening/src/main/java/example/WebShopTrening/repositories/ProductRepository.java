package example.WebShopTrening.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import example.WebShopTrening.ProductService.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT MAX(p.price) FROM Product p")
    Double findMaxPrice();
	
	List<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
	
//    List<Product> findByPriceBetweenAndNameContainingIgnoreCaseOrAboutContainingIgnoreCase(
//    		Double minPrice, 
//    		Double maxPrice,
//    		String name, 
//    		String about,    		
//    		Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice " +
            "AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.about) LIKE LOWER(CONCAT('%', :search, '%')))")
     List<Product> findByFilters(
         @Param("minPrice") Double minPrice,
         @Param("maxPrice") Double maxPrice,
         @Param("search") String search,
         Pageable pageable
     );
}