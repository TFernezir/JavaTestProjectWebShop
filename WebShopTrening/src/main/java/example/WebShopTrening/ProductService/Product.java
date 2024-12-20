package example.WebShopTrening.entitets;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import example.WebShopTrening.CustomValidations.NotEmptyOrWhitespace;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Name is required") 
    @NotEmptyOrWhitespace 
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0") 
    @Column(nullable = false)
    private Double price;
    
    private String about;
    
    public Product() {
    	
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getAbout() {
        return about;
    }
    
    public void setAbout(String about) {
        this.about = about;
    }
}