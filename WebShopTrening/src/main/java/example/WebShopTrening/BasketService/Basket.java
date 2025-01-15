package example.WebShopTrening.BasketService;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "BASKETS")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "basket", 
    		cascade = CascadeType.ALL, 
    		orphanRemoval = true)
    private List<BasketItem> items = new ArrayList<>();
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private BasketStatus status;
    
    @PositiveOrZero
    @Column(nullable = false)
    private Double totalAmount;

    public Basket() {}
    
    public Basket(Long userId, BasketStatus status) {
        this.userId = userId;
        this.status = status;
        this.totalAmount = 0.0;
        this.items = new ArrayList<>();
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public List<BasketItem> getItems() {
        return items;
    }
    
    public BasketStatus getStatus() {
        return status;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    // Existing setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public void setStatus(BasketStatus status) {
        this.status = status;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    // Missing setter
    public void setItems(List<BasketItem> items) {
        this.items = items;
    }
    
    // Helper methods
    public void addItem(BasketItem item) {
        items.add(item);
        item.setBasket(this);
    }
    
    public void removeItem(BasketItem item) {
        items.remove(item);
        item.setBasket(null);
    }
}