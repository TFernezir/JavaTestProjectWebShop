package example.WebShopTrening.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import example.WebShopTrening.ProductService.Product;
import example.WebShopTrening.ProductService.ProductPage;
import example.WebShopTrening.ProductService.ProductSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class ProductCriteriaRepository {
	private final EntityManager entityManager;
	private final CriteriaBuilder criteriaBuilder;
	
	public ProductCriteriaRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
	}
	
	public Page<Product> findAllWithFilters(ProductPage productPage, 
											ProductSearchCriteria productSearchCriteria){
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> productRoot = criteriaQuery.from(Product.class);
		Predicate predicate = getPredicate(productSearchCriteria, productRoot);
		criteriaQuery.where(predicate);
		setOrder(productPage, criteriaQuery, productRoot);
		
		TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult(productPage.getPageNumber() * productPage.getPageSize());
		typedQuery.setMaxResults(productPage.getPageSize());
		
		 Pageable pageable = getPageable(productPage);
	        
        long productsCount = getProductsCount(predicate);
	        
        return new PageImpl<>(typedQuery.getResultList(), pageable, productsCount);
	}

	private Predicate getPredicate(ProductSearchCriteria productSearchCriteria, 
									Root<Product> productRoot) {
		List<Predicate> predicates = new ArrayList<>();
	    
	    if (productSearchCriteria != null) {
	        if (Objects.nonNull(productSearchCriteria.getName())) {
	            predicates.add(
	            		criteriaBuilder.like(productRoot.get("name"), 
	            				"%" + productSearchCriteria.getName() + "%"));
	        }
	        
	        if (Objects.nonNull(productSearchCriteria.getMinPrice())) {
	            predicates.add(
	            		criteriaBuilder.greaterThanOrEqualTo(
	            				productRoot.get("price"), 
	            				productSearchCriteria.getMinPrice()));
	        }
	        
	        if (Objects.nonNull(productSearchCriteria.getMaxPrice())) {
	            predicates.add(
	            		criteriaBuilder.lessThanOrEqualTo(
	            				productRoot.get("price"), 
	            				productSearchCriteria.getMaxPrice()));
	        }
	    }
	    
	    return predicates.isEmpty() ? 
	        criteriaBuilder.conjunction() : 
	        criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
	
	private void setOrder(
		    ProductPage productPage,              
		    CriteriaQuery<Product> criteriaQuery, 
		    Root<Product> productRoot         
		) {	 
		    if (productPage.getSortDirection().equals(Sort.Direction.ASC)) {
		        criteriaQuery.orderBy(
		            criteriaBuilder.asc(                   
		                productRoot.get(productPage.getSortBy())  
		            )
		        );
		    } else {
			        criteriaQuery.orderBy(
			            criteriaBuilder.desc(
			                productRoot.get(productPage.getSortBy()
			            )
			        )
	            );
		    }
		}
	
	 private Pageable getPageable(ProductPage productPage) {
	        Sort sort = Sort.by(productPage.getSortDirection(), productPage.getSortBy());
	        return PageRequest.of(productPage.getPageNumber(), productPage.getPageSize(), sort);
	    }
	    
	    private long getProductsCount(Predicate predicate) {
	        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
	        Root<Product> countRoot = countQuery.from(Product.class);
	        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
	        return entityManager.createQuery(countQuery).getSingleResult();
	    }
}
