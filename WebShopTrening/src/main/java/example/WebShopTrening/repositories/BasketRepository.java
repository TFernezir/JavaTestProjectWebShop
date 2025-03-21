package example.WebShopTrening.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.WebShopTrening.BasketService.Basket;
import example.WebShopTrening.BasketService.BasketStatus;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
	
	Optional<Basket> findByUserIdAndStatus(Long userId, BasketStatus status);
}
