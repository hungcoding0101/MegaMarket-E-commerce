package com.hung.Ecommerce.Repository;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.QueryHints;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.common.SortMode;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.DTO.SearchProductResult;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductFilterTypes;
import com.hung.Ecommerce.Model.Product_SortTypes;

@Repository
public class ProductRepositoryImp implements ProductRepository{

	@PersistenceContext
	private EntityManager entityManager;

@Override
public Product findById(int id, boolean fetchOrNot) {
	
	CriteriaBuilder CB = entityManager.getCriteriaBuilder();
	CriteriaQuery<Product> CQ = CB.createQuery(Product.class);
	Root<Product> root = CQ.from(Product.class);
	
	CQ.select(root).where(CB.equal(root.get("id"), id));
	Query query = entityManager.createQuery(CQ)	;
	
	if(fetchOrNot == true) {
		EntityGraph<Product> graph = entityManager.createEntityGraph(Product.class);
		graph.addAttributeNodes("categories");
		graph.addAttributeNodes("characteristics");
		graph.addSubgraph("productOptions");
		graph.addSubgraph("receivedRatings").addAttributeNodes("images");
		graph.addAttributeNodes("otherImagesURLS");
		graph.addAttributeNodes("specificDetails");
		graph.addAttributeNodes("orders");
		graph.addAttributeNodes("paymentMethods");
		
		query.setHint("javax.persistence.fetchgraph", graph); 
	}	
	
	List<Product> result =  query.getResultList();
	
	
	if(result.isEmpty()) {return null;}
	return result.get(0);
}

@SuppressWarnings("unchecked")
@Override
	public List<Product> findAll(boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> CQ = CB.createQuery(Product.class);
		Root<Product> root = CQ.from(Product.class);
		
		CQ.select(root);
		Query query = entityManager.createQuery(CQ);
		
		if(fetchOrNot) {
			EntityGraph entityGraph = entityManager.getEntityGraph("product.fetch.1");
			query.setHint("javax.persistence.fetchgraph", entityGraph);
		}
		
		List<Product> result =  query.getResultList();
			
		return result;
	}


@Override
	public List<Product> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																boolean fetchOrNot) {
	
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> CQ = CB.createQuery(Product.class);
		Root<Product> root = CQ.from(Product.class);

//		
//		if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
//		else{CQ.orderBy(CB.desc(root.get(propertyName)));}
		
		CQ.select(root).distinct(true).where(CB.equal(root.get(propertyName), value));
		
		Query query = entityManager.createQuery(CQ);
	
		if(fetchOrNot) {
			EntityGraph entityGraph = entityManager.getEntityGraph("product.fetch.1");
			query.setHint("javax.persistence.loadgraph", entityGraph);
		}	
		List<Product> result = query.getResultList();

		return result;
	}

@Override
public List<Product> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
		boolean fetchOrNot) {
	CriteriaBuilder CB = entityManager.getCriteriaBuilder();
	CriteriaQuery<Product> CQ = CB.createQuery(Product.class);
	Root<Product> root = CQ.from(Product.class);
	
	CQ.select(root).distinct(true).where(root.get(propertyName).in(values));
	
	Query query = entityManager.createQuery(CQ);
	
	if(fetchOrNot) {
		EntityGraph entityGraph = entityManager.getEntityGraph("product.fetch.1");
		query.setHint("javax.persistence.loadgraph", entityGraph);
	}	
	
	List<Product> result = query.getResultList();

	return result;
}


@Override
public List<Product> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){

	
	CriteriaBuilder CB = entityManager.getCriteriaBuilder();
	CriteriaQuery<Product> CQ = CB.createQuery(Product.class);
	Root<Product> root = CQ.from(Product.class);
	
	
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
	if(fetchOrNot) {
		EntityGraph entityGraph = entityManager.getEntityGraph("product.fetch.1");
		query.setHint("javax.persistence.fetchgraph", entityGraph);
	}	
	
	List<Product> result =  query.getResultList();

	return result;
}
	

@Override
public void save(Product theOne) {		
	entityManager.persist(theOne);
}

@Override
	public void reconnect(Product player) {		
		entityManager.refresh(player);
	}

@Override
public Product update(Product player) {
	
	return (Product) entityManager.merge(player);
}

@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
														Object newValue) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaUpdate<Product> CD = CB.createCriteriaUpdate(Product.class);
		Root<Product> root = CD.from(Product.class);
	
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
	Product reservation = entityManager.find(Product.class, id);	
	entityManager.remove(reservation);		
}



@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaDelete<Product> CD = CB.createCriteriaDelete(Product.class);
		Root<Product> root = CD.from(Product.class);
		
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

	@Override
	public SearchProductResult searchByName(String keyWords, int offset, int limit, Product_SortTypes sortType,
																			Map<String, String[]> filters) {
			SearchSession session = Search.session(entityManager);
			AggregationKey<Map<String, Long>> countsBySeller = AggregationKey.of( "countsBySeller" );
			AggregationKey<Map<String, Long>> countsByBrand = AggregationKey.of( "countsByBrand" );
			SearchQuery<Product> query =  session.search(Product.class)
																.where(f -> f.bool(b -> {
																	
																			if(filters != null && !filters.isEmpty()) {
																					if(filters.containsKey(ProductFilterTypes.RATING_SCORE.toString())) {
																						try {
																							String[] scores = filters.get(ProductFilterTypes.RATING_SCORE.toString());
																							if(scores.length > 0) {
																								Double score = Double.parseDouble(scores[0]);	
																								b.filter(f.range().field("averageRatingScore").atLeast(score));
																							}
																							
																						} catch (Exception e) {
																							e.printStackTrace();
																						}
																					}
																														
																					if(filters.containsKey(ProductFilterTypes.BRAND.toString())) {
																						String[] brands = filters.get(ProductFilterTypes.BRAND.toString());
																						if(brands.length > 0) {
																							b.filter(f.terms().field("brand_sort").matchingAny(Arrays.asList(brands)));
																						}
																					}
																					
																					if(filters.containsKey(ProductFilterTypes.SELLER.toString())) {
																						String[] sellers = filters.get(ProductFilterTypes.SELLER.toString());
																						if(sellers.length > 0) {
																							b.filter(f.terms().field("seller.name_sort").matchingAny(Arrays.asList(sellers)));
																						}
																					}
																			}	

																			b.must(
																					f.bool()
																					.should(f.phrase().field("name").matching(keyWords)).boost(3.0f)
																					.should(f.match().field("name").matching(keyWords))
																				);
																		}
																	)
																)
																.loading(o -> o.graph("product.fetch.1", GraphSemantic.FETCH))
																.sort(f -> f.composite(s -> {

																		if(sortType != null) {
																				if(sortType.toString().equals("PRICE_ASC")){
																						s.add(f.field("defaultUnitPrice").asc());
																				}
																				
																				else if(sortType.toString().equals("PRICE_DESC")){
																						s.add(f.field("defaultUnitPrice").desc());
																				}	
																				
																				else if(sortType.toString().equals("RATING")){
																						s.add(f.field("averageRatingScore").desc());
																				}
																				
																				else if(sortType.toString().equals("SALES")){
																						s.add(f.field("totalSales").desc());
																				}		
																		 }
																		
																		else {
																				s.add(f.score());
																		}
																		
																		s.add(f.field("orders.quantity").mode(SortMode.SUM).desc());
																		s.add(f.field("cartItems.quantity").mode(SortMode.SUM).desc());
																		s.add(f.field("id_sort").desc());
																})
																)
																.aggregation(countsBySeller, f -> f.terms().field( "seller.name_sort", String.class))
																.aggregation(countsByBrand, f -> f.terms().field( "brand_sort", String.class))
																.toQuery();
			
			out.println("HERE: KEYWORD IN REPO: " + keyWords);
			SearchResult<Product> result = query.fetch(offset, limit);
			List<Product> matches = result.hits();
			Long matchTotal = query.fetchTotalHitCount();
			return new SearchProductResult(matches, matchTotal, result.aggregation(countsBySeller),
																	result.aggregation(countsByBrand));
	}

	@Override
	public SearchProductResult searchByCategory(String keyWords, int offset, int limit, Product_SortTypes sortType,
																				Map<String, String[]> filters) {
			SearchSession session = Search.session(entityManager);
			AggregationKey<Map<String, Long>> countsBySeller = AggregationKey.of( "countsBySeller" );
			AggregationKey<Map<String, Long>> countsByBrand = AggregationKey.of( "countsByBrand" );
			SearchQuery<Product> query =  session.search(Product.class)
																			.where(f -> f.bool(b -> {
																				
																				if(filters != null && !filters.isEmpty()) {
																					if(filters.containsKey(ProductFilterTypes.RATING_SCORE.toString())) {
																						try {
																							String[] scores = filters.get(ProductFilterTypes.RATING_SCORE.toString());
																							if(scores.length > 0) {
																								Double score = Double.parseDouble(scores[0]);	
																								b.filter(f.range().field("averageRatingScore").atLeast(score));
																							}
																							
																						} catch (Exception e) {
																							e.printStackTrace();
																						}
																					}
																														
																					if(filters.containsKey(ProductFilterTypes.BRAND.toString())) {
																						String[] brands = filters.get(ProductFilterTypes.BRAND.toString());
																						if(brands.length > 0) {
																							b.filter(f.terms().field("brand_sort").matchingAny(Arrays.asList(brands)));
																						}
																					}
																					
																					if(filters.containsKey(ProductFilterTypes.SELLER.toString())) {
																						String[] sellers = filters.get(ProductFilterTypes.SELLER.toString());
																						if(sellers.length > 0) {
																							b.filter(f.terms().field("seller.name_sort").matchingAny(Arrays.asList(sellers)));
																						}
																					}
																				}
																				
																				b.must(
																						(f.phrase().field("categories.title").matching(keyWords)).boost(3.0f)
																					);
																			}
																		)
																	)

																.loading(o -> o.graph("product.fetch.1", GraphSemantic.FETCH))
																.sort(f -> f.composite(s -> {
																		if(sortType != null) {
																				if(sortType.toString().equals("PRICE_ASC")){
																						s.add(f.field("defaultUnitPrice").asc());
																				}
																				
																				else if(sortType.toString().equals("PRICE_DESC")){
																					s.add(f.field("defaultUnitPrice").desc());
																				}
																				
																				else if(sortType.toString().equals("RATING")){
																					s.add(f.field("averageRatingScore").desc());
																				}
																				
																				else if(sortType.toString().equals("SALES")){
																					s.add(f.field("totalSales").desc());
																				}		
																		 }
																		
																		else {
																			s.add(f.score());
																		}
																		
																		s.add(f.field("orders.quantity").mode(SortMode.SUM).desc());
																		s.add(f.field("cartItems.quantity").mode(SortMode.SUM).desc());
																		s.add(f.field("id_sort").desc());
																})
																)
																.aggregation(countsBySeller, f -> f.terms().field( "seller.name_sort", String.class))
																.aggregation(countsByBrand, f -> f.terms().field( "brand_sort", String.class))
																.toQuery();

			SearchResult<Product> result = query.fetch(offset, limit);
			List<Product> matches = result.hits();
			Long matchTotal = query.fetchTotalHitCount();
			return new SearchProductResult(matches, matchTotal, result.aggregation(countsBySeller),
																	result.aggregation(countsByBrand));
	}

}
