package example.WebShopTrening.BasketService;

import java.util.Optional;

import example.WebShopTrening.Dto.BasketItemDto;

public interface IBasketService {
	Iterable<Basket> allBaskets();
	Optional<Basket> getBasketById(Long id);
	Optional<Basket> getBasketByUserId(Long userId);
	Basket addOrUpdateItem(Long basketId, Long userId, BasketItemDto item);
    Basket removeItem(Long basketId, Long itemId);
	void deleteBasket(Long id);
}
