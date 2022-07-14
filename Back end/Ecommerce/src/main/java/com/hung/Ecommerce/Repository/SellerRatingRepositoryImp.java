package com.hung.Ecommerce.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.Model.SellerRating;
import com.hung.Ecommerce.Model.SellerRating;
import com.hung.Ecommerce.Model.SellerRating;

@Repository
public class SellerRatingRepositoryImp implements SellerRatingRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SellerRating findById(int id, boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<SellerRating> CQ = CB.createQuery(SellerRating.class);
		Root<SellerRating> root = CQ.from(SellerRating.class);
		
		CQ.select(root).where(CB.equal(root.get("id"), id));
		Query query = entityManager.createQuery(CQ);
		List<SellerRating> result =  query.getResultList();
		
		if(fetchOrNot == true) {
			String[] fetchObjects = {"comment"};
			
			for(String object: fetchObjects) {
				CriteriaBuilder newCB = entityManager.getCriteriaBuilder();
				CriteriaQuery<SellerRating> newCQ = newCB.createQuery(SellerRating.class);
				Root<SellerRating> newRoot = newCQ.from(SellerRating.class);
				
					newRoot.fetch(object, JoinType.LEFT);
					newCQ.select(newRoot).distinct(true).where(newRoot.in(result));
					result = entityManager.createQuery(newCQ).getResultList();
				
			}
		}
		
		if(result.isEmpty()) {return null;}
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
		public List<SellerRating> findAll(boolean fetchOrNot) {
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<SellerRating> CQ = CB.createQuery(SellerRating.class);
			Root<SellerRating> root = CQ.from(SellerRating.class);
			
			CQ.select(root);
			Query query = entityManager.createQuery(CQ);
			List<SellerRating> result =  query.getResultList();
			
			if(fetchOrNot == true) {
				String[] fetchObjects = {"comment"};
				
				for(String object: fetchObjects) {
					CriteriaBuilder newCB = entityManager.getCriteriaBuilder();
					CriteriaQuery<SellerRating> newCQ = newCB.createQuery(SellerRating.class);
					Root<SellerRating> newRoot = newCQ.from(SellerRating.class);
					
						newRoot.fetch(object, JoinType.LEFT);
						newCQ.select(newRoot).distinct(true).where(newRoot.in(result));
						result = entityManager.createQuery(newCQ).getResultList();
					
				}
			}
			
			return result;
		}



	@Override
		public List<SellerRating> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																	boolean fetchOrNot) {
		
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<SellerRating> CQ = CB.createQuery(SellerRating.class);
			Root<SellerRating> root = CQ.from(SellerRating.class);

		
//			
//			if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
//			else{CQ.orderBy(CB.desc(root.get(propertyName)));}
			
			CQ.select(root).distinct(true).where(CB.equal(root.get(propertyName), value));
			
			Query query = entityManager.createQuery(CQ);
			List<SellerRating> result = query.getResultList();
			
			if(fetchOrNot == true) {
					String[] fetchObjects = {"comment"};
					
					for(String object: fetchObjects) {
						CriteriaBuilder newCB = entityManager.getCriteriaBuilder();
						CriteriaQuery<SellerRating> newCQ = newCB.createQuery(SellerRating.class);
						Root<SellerRating> newRoot = newCQ.from(SellerRating.class);
						
							newRoot.fetch(object, JoinType.LEFT);
							newCQ.select(newRoot).distinct(true).where(newRoot.in(result));
							result = entityManager.createQuery(newCQ).getResultList();
						
					}
			}
			
			return result;
		}


	@Override
	public List<SellerRating> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																	boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){

		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<SellerRating> CQ = CB.createQuery(SellerRating.class);
		Root<SellerRating> root = CQ.from(SellerRating.class);
		
		
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
		List<SellerRating> result =  query.getResultList();

		
		if(fetchOrNot == true) {
			String[] fetchObjects = {"comment"};
			
			for(String object: fetchObjects) {
				CriteriaBuilder newCB = entityManager.getCriteriaBuilder();
				CriteriaQuery<SellerRating> newCQ = newCB.createQuery(SellerRating.class);
				Root<SellerRating> newRoot = newCQ.from(SellerRating.class);
				
					newRoot.fetch(object, JoinType.LEFT);
					newCQ.select(newRoot).distinct(true).where(newRoot.in(result));
					result = entityManager.createQuery(newCQ).getResultList();
				
			}
		}

		return result;
	}
	

@Override
public void save(SellerRating theOne) {
		
	entityManager.persist(theOne);
}

@Override
	public void reconnect(SellerRating player) {
			
		entityManager.refresh(player);
	}

@Override
public SellerRating update(SellerRating player) {
	
	return (SellerRating) entityManager.merge(player);
}

@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
														Object newValue) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaUpdate<SellerRating> CD = CB.createCriteriaUpdate(SellerRating.class);
		Root<SellerRating> root = CD.from(SellerRating.class);
	
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
	SellerRating reservation = entityManager.find(SellerRating.class, id);	
	entityManager.remove(reservation);		
}



@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaDelete<SellerRating> CD = CB.createCriteriaDelete(SellerRating.class);
		Root<SellerRating> root = CD.from(SellerRating.class);
		
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
}
