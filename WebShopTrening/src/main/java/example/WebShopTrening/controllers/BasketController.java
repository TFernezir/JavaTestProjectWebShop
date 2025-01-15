package example.WebShopTrening.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.WebShopTrening.BasketService.Basket;
import example.WebShopTrening.BasketService.IBasketService;
import example.WebShopTrening.Dto.BasketItemDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/basket")
public class BasketController {
	private final IBasketService basketService;
	
	public BasketController(IBasketService basketService) {
		this.basketService = basketService;
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Basket>> allBaskets()
	{
		return ResponseEntity.ok(basketService.allBaskets());
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        return basketService.getBasketById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Basket> getBasketByUserId(@PathVariable Long userId) {
        return basketService.getBasketByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/items")
    public ResponseEntity<Basket> addOrUpdateItem(
            @RequestParam(required = false) Long basketId,
            @RequestParam Long userId,
            @Valid 
            @RequestBody BasketItemDto item) {
        Basket updatedBasket = basketService.addOrUpdateItem(basketId, userId, item);
        return ResponseEntity.ok(updatedBasket);
    }

    @DeleteMapping("/{basketId}/items/{itemId}")
    public ResponseEntity<Basket> removeItem(
            @PathVariable Long basketId,
            @PathVariable Long itemId) {
        Basket updatedBasket = basketService.removeItem(basketId, itemId);
        return ResponseEntity.ok(updatedBasket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        basketService.deleteBasket(id);
        return ResponseEntity.noContent().build();
    }
}