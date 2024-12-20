package example.WebShopTrening.repositories;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import example.WebShopTrening.entitets.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;

@Repository
public class ProductCriteriaRepository {
	private final EntityManager entityManager;
	private final CriteriaBuilder criteriaBuilder;
	
	public ProductCriteriaRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
	}
	
	public Page<Product> findAllWithFilters(){
		
	}
}
