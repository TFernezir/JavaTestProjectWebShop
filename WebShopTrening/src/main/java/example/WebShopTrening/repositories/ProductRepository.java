package example.WebShopTrening.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.WebShopTrening.entitets.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}