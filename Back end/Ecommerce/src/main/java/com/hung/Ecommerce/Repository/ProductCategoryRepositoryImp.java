package com.hung.Ecommerce.Repository;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.graph.GraphSemantic;
import org.hibernate.search.engine.search.common.SortMode;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.DTO.SearchProductResult;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Model.ProductCategory;
import com.hung.Ecommerce.Model.ProductCategory;

@Repository
public class ProductCategoryRepositoryImp implements ProductCategoryRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public ProductCategory findById(int id, boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> CQ = CB.createQuery(ProductCategory.class);
		Root<ProductCategory> root = CQ.from(ProductCategory.class);
		
		CQ.select(root).where(CB.equal(root.get("id"), id));
		Query query = entityManager.createQuery(CQ);
		
		if(fetchOrNot == true) {
			EntityGraph<ProductCategory> graph = entityManager.createEntityGraph(ProductCategory.class);
			graph.addAttributeNodes("children");
			query.setHint("javax.persistence.fetchgraph", graph);
		}
		List<ProductCategory> result =  query.getResultList();
		
		
		if(result.isEmpty()) {return null;}
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
		public List<ProductCategory> findAll(boolean fetchOrNot) {
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<ProductCategory> CQ = CB.createQuery(ProductCategory.class);
			Root<ProductCategory> root = CQ.from(ProductCategory.class);
			
			CQ.select(root);
			Query query = entityManager.createQuery(CQ);
			List<ProductCategory> result =  query.getResultList();
			
			if(fetchOrNot == true) {
				EntityGraph entityGraph = entityManager.getEntityGraph("category.fetch.1");
				query.setHint("javax.persistence.loadgraph", entityGraph);
			}
			
			return result;
		}



	@Override
		public List<ProductCategory> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																	boolean fetchOrNot) {
		
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<ProductCategory> CQ = CB.createQuery(ProductCategory.class);
			Root<ProductCategory> root = CQ.from(ProductCategory.class);

		
//			
//			if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
//			else{CQ.orderBy(CB.desc(root.get(propertyName)));}
			
			CQ.select(root).distinct(true).where(CB.equal(root.get(propertyName), value));
			
			Query query = entityManager.createQuery(CQ);
			
			if(fetchOrNot == true) {
				EntityGraph entityGraph = entityManager.getEntityGraph("category.fetch.1");
				query.setHint("javax.persistence.loadgraph", entityGraph);
		//		root.fetch("children", JoinType.LEFT);
			}
			
			List<ProductCategory> result = query.getResultList();
			
			return result;
		}

	
	@Override
	public List<ProductCategory> findManyByProperty(String propertyName, List<Object> values,
			boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> CQ = CB.createQuery(ProductCategory.class);
		Root<ProductCategory> root = CQ.from(ProductCategory.class);
		
		CQ.select(root).distinct(true).where(root.get(propertyName).in(values));
		
		Query query = entityManager.createQuery(CQ);
		
		if(fetchOrNot == true) {
			EntityGraph entityGraph = entityManager.getEntityGraph("category.fetch.1");
			query.setHint("javax.persistence.loadgraph", entityGraph);
		}
		
		List<ProductCategory> result = query.getResultList();

		return result;
	}

	@Override
	public List<ProductCategory> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																	boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){

		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> CQ = CB.createQuery(ProductCategory.class);
		Root<ProductCategory> root = CQ.from(ProductCategory.class);
		
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Order> allOrders = new ArrayList<Order>();
		
		conditions.forEach((propertyName ,desiredValue) -> {
			Predicate predicate = CB.equal(root.get(propertyName), desiredValue);
			predicates.add(predicate);
		});
			
		if(orders != null) {
			orders.forEach((propertyName, orderToSort) ->{
				if(orderToSort == SelectionOrder.ASC) {
					allOrders.add(CB.asc(root.get(propertyName)));
				}
				
				else {allOrders.add(CB.desc(root.get(propertyName)));}
			});
		}

		Predicate finalPredicate;
		
		if(TrueIsAnd_FalseIsOr == true) {finalPredicate = CB.and(predicates.toArray(new Predicate[] {}));}
		else {finalPredicate = CB.or(predicates.toArray(new Predicate[] {}));}
		
		CQ.select(root).where(finalPredicate);
		
		if(!allOrders.isEmpty()) {CQ.orderBy(allOrders);}
		
		Query query = entityManager.createQuery(CQ);
		List<ProductCategory> result =  query.getResultList();

		
		if(fetchOrNot == true) {
			EntityGraph entityGraph = entityManager.getEntityGraph("category.fetch.1");
			query.setHint("javax.persistence.fetchgraph", entityGraph);
		}

		return result;
	}
	

@Override
public void save(ProductCategory theOne) {
		
	entityManager.persist(theOne);
}

@Override
	public void reconnect(ProductCategory player) {
			
		entityManager.refresh(player);
	}

@Override
public ProductCategory update(ProductCategory player) {
	
	return (ProductCategory) entityManager.merge(player);
}

@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
														Object newValue) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaUpdate<ProductCategory> CD = CB.createCriteriaUpdate(ProductCategory.class);
		Root<ProductCategory> root = CD.from(ProductCategory.class);
	
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		conditions.forEach((s,o) -> {
			Predicate predicate = CB.equal(root.get(s), o);
			predicates.add(predicate);
		});
		
		Predicate finalPredicate;
		
		if(TrueIsAnd_FalseIsOr == true) {finalPredicate = CB.and(predicates.toArray(new Predicate[] {}));}
		else {finalPredicate = CB.or(predicates.toArray(new Predicate[] {}));}
		
		CD.set(targetField, newValue);
		CD.where(finalPredicate);
		return entityManager.createQuery(CD).executeUpdate();
}

@Override
public void delete(int id) {	
	ProductCategory reservation = entityManager.find(ProductCategory.class, id);	
	entityManager.remove(reservation);		
}



@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaDelete<ProductCategory> CD = CB.createCriteriaDelete(ProductCategory.class);
		Root<ProductCategory> root = CD.from(ProductCategory.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		conditions.forEach((s,o) -> {
			Predicate predicate = CB.equal(root.get(s), o);
			predicates.add(predicate);
		});
		
		Predicate finalPredicate;
		if(TrueIsAnd_FalseIsOr == true) {finalPredicate = CB.and(predicates.toArray(new Predicate[] {}));}
		else {finalPredicate = CB.or(predicates.toArray(new Predicate[] {}));}
		
		CD.where(finalPredicate);
		return entityManager.createQuery(CD).executeUpdate();
	}

	
	public List<ProductCategory> searchByDepth(int depth, int offset, int limit){
		SearchSession session = Search.session(entityManager);
		
		SearchQuery<ProductCategory> query =  session.search(ProductCategory.class)
															.where(f -> f.match().field("depth").matching(depth))
															.sort(f -> f.score()
																	.then().field("title_sort").asc()
																	
															).toQuery();
		List<ProductCategory> matches = query.fetchHits(offset, limit);
		return matches;
	}

	
	@Override
		public List<String> getSuggestions(String keyWords, int offset, int limit) {
				SearchSession session = Search.session(entityManager);
				
				SearchQuery<String> query =  session.search(ProductCategory.class)
																	.select(f -> f.field("title", String.class))
																	.where(f -> f.bool()
																					 .should( f.phrase().field("title").matching(keyWords)).boost(2.0f)
																					 .should(f.match().field("title").matching(keyWords))
																				).toQuery();
				List<String> matches = query.fetchHits(offset, limit);
				return matches;
		}

	
		
	
}
