package example.WebShopTrening.BasketService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import example.WebShopTrening.CustomExceptions.ResourceNotFoundException;
import example.WebShopTrening.Dto.BasketItemDto;
import example.WebShopTrening.ProductService.ProductService;
import example.WebShopTrening.repositories.BasketRepository;

@Service
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
public class BasketService implements IBasketService {

	private final BasketRepository basketRepository;
	private final ProductService productService;
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	public BasketService(BasketRepository basketRepository, ProductService productService) {
		this.basketRepository = basketRepository;
		this.productService = productService;
	}

	@Override
	public Iterable<Basket> allBaskets() {
		logger.info("Executing" + getClass());
		return basketRepository.findAll();
	}

	@Override
	public Optional<Basket> getBasketById(Long id) {
		logger.info("Executing" + getClass() + "input:" + id);
		return basketRepository.findById(id);
	}

	@Override
	public Optional<Basket> getBasketByUserId(Long userId) {
		logger.info("Executing" + getClass() + "input:" + userId);
		return basketRepository.findByUserId(userId);
	}

	@Override
	public Basket addOrUpdateItem(Long basketId, Long userId, BasketItemDto itemDto) {
	    logger.info("Executing addOrUpdateItem " + getClass() + " basketId: " + basketId + " userId: " + userId + " Input: " + itemDto);

	    var product = productService.getProduct(itemDto.getProductId());

	    if (basketId == null) {
	        logger.info("Creating new basket");
	        
	        Basket basket = new Basket();
	        basket.setUserId(userId);
	        basket.setStatus(BasketStatus.ACTIVE);

	        BasketItem basketItem = new BasketItem();
	        basketItem.setProduct(product);
	        basketItem.setQuantity(itemDto.getQuantity());
	        basketItem.setUnitPrice(product.getPrice());
	        basketItem.setBasket(basket);

	        basket.getItems().add(basketItem);  // Use basket's items list directly
	        basket.setTotalAmount(product.getPrice() * itemDto.getQuantity());

	        logger.info("New basket Created");
	        return basketRepository.save(basket);
	    }

	    logger.info("Updating basket");

	    Basket basket = basketRepository.findById(basketId).get();
	    boolean itemExists = false;

	    for (BasketItem existingItem : basket.getItems()) {
	        logger.info("Checking item");
	        if (existingItem.getProduct().getId().equals(product.getId())) {
	            existingItem.setQuantity(itemDto.getQuantity());
	            existingItem.setUnitPrice(product.getPrice());
	            itemExists = true;
	            break;
	        }
	    }

	    if (!itemExists) {
	        logger.info("Adding new item");
	        BasketItem newItem = new BasketItem();
	        newItem.setProduct(product);
	        newItem.setQuantity(itemDto.getQuantity());
	        newItem.setUnitPrice(product.getPrice());
	        newItem.setBasket(basket);
	        basket.getItems().add(newItem);  // Add to existing list
	    }

	    basket.setTotalAmount(calculateBasketTotal(basket.getItems()));

	    logger.info("Basket updated");
	    return basketRepository.save(basket);
	}

	@Override
	public Basket removeItem(Long basketId, Long itemId) {
		logger.info("Executing" + getClass() + "basket id: " + basketId + "item id: " + itemId);
		Basket basket = basketRepository.findById(basketId).get();
		basket.getItems().removeIf(item -> item.getId().equals(itemId));
		basket.setTotalAmount(calculateBasketTotal(basket.getItems()));
		return basketRepository.save(basket);
	}

	@Override
	public void deleteBasket(Long id) {
		logger.info("Executing delete from " + getClass() + " input: " + id);
		if (!basketRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product with ID " + id + " not found");
		}

		basketRepository.deleteById(id);
	}

	private Double calculateBasketTotal(List<BasketItem> items) {
		return items.stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
	}
}
