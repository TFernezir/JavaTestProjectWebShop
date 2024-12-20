package example.WebShopTrening.BasketService;

import java.util.Optional;

import example.WebShopTrening.entitets.Basket;
import example.WebShopTrening.entitets.BasketItem;

public interface IBasketService {
	Iterable<Basket> allBaskets();
	Optional<Basket> getBasketById(Long id);
	Optional<Basket> getBasketByUserId(Long userId);
	Basket addOrUpdateItem(Long basketId, Long userId, BasketItem item);
    Basket removeItem(Long basketId, Long itemId);
	void deleteBasket(Long id);
}
