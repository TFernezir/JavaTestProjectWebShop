package example.WebShopTrening.BasketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import example.WebShopTrening.entitets.Basket;
import example.WebShopTrening.entitets.BasketItem;
import example.WebShopTrening.entitets.BasketStatus;
import example.WebShopTrening.repositories.BasketRepository;

@Service
@Secured({ "USER", "ADMIN" })
public class BasketService implements IBasketService{
	private final BasketRepository basketRepository;
	
	public BasketService(BasketRepository basketRepository) {
		this.basketRepository = basketRepository;
	}
	
	@Override
	public Iterable<Basket> allBaskets() {
        return basketRepository.findAll();
	}
	@Override
	public Optional<Basket> getBasketById(Long id) {
        return basketRepository.findById(id);
    }
	
	@Override
    public Optional<Basket> getBasketByUserId(Long userId) {
        return basketRepository.findByUserId(userId);
    }
	
	 @Override
	    public Basket addOrUpdateItem(Long basketId, Long userId, BasketItem item) {
	        Basket basket;
	        List<BasketItem> items = new ArrayList<>();

	        if (basketId == null) {
	            basket = new Basket();
	            basket.setUserId(userId);
	            basket.setStatus(BasketStatus.ACTIVE);
	            
	            item.setBasket(basket);
	            items.add(item);
	            basket.setItems(items);
	            basket.setTotalAmount(item.getUnitPrice() * item.getQuantity());
	            
	            return basketRepository.save(basket);
	        }

	        basket = basketRepository.findById(basketId).get();
	        boolean itemExists = false;

	        for (BasketItem existingItem : basket.getItems()) {
	            if (existingItem.getProduct().getId().equals(item.getProduct().getId())) {
	                existingItem.setQuantity(item.getQuantity());
	                existingItem.setUnitPrice(item.getUnitPrice());
	                itemExists = true;
	                items.add(existingItem);
	            } else {
	                items.add(existingItem);
	            }
	        }

	        if (!itemExists) {
	            item.setBasket(basket);
	            items.add(item);
	        }

	        basket.setItems(items);
	        basket.setTotalAmount(calculateBasketTotal(items));
	        
	        return basketRepository.save(basket);
	    }
	
	@Override
    public Basket removeItem(Long basketId, Long itemId) {
        Basket basket = basketRepository.findById(basketId).get();
        basket.getItems().removeIf(item -> item.getId().equals(itemId));
        basket.setTotalAmount(calculateBasketTotal(basket.getItems()));
        return basketRepository.save(basket);
    }

	@Override
	public void deleteBasket(Long id)
	{
		basketRepository.deleteById(id);
	}
	
    private Double calculateBasketTotal(List<BasketItem> items) {
        return items.stream()
            .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
            .sum();
    }
}
