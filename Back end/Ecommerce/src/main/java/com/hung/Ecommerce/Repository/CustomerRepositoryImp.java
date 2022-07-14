package com.hung.Ecommerce.Repository;

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

import org.springframework.stereotype.Repository;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.Customer;
import com.hung.Ecommerce.Model.Customer;

@Repository
public class CustomerRepositoryImp implements CustomerRepository{

	@PersistenceContext
		private EntityManager entityManager;
	
	
	@Override
	public Customer findById(int id, boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> CQ = CB.createQuery(Customer.class);
		Root<Customer> root = CQ.from(Customer.class);
		
		CQ.select(root).where(CB.equal(root.get("id"), id));
		Query query = entityManager.createQuery(CQ);
		
		
		if(fetchOrNot == true) {
			EntityGraph entityGraph = entityManager.getEntityGraph("customer.fetch.1");
			query.setHint("javax.persistence.fetchgraph", entityGraph);
		}
		
		List<Customer> result =  query.getResultList();
		
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
		public List<Customer> findAll(boolean fetchOrNot) {
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<Customer> CQ = CB.createQuery(Customer.class);
			Root<Customer> root = CQ.from(Customer.class);
			
			CQ.select(root);
			Query query = entityManager.createQuery(CQ);
		
			if(fetchOrNot == true) {
				EntityGraph entityGraph = entityManager.getEntityGraph("customer.fetch.1");
				query.setHint("javax.persistence.fetchgraph", entityGraph);
			}
			
			List<Customer> result =  query.getResultList();
			return result;
		}



	@Override
		public List<Customer> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																	boolean fetchOrNot) {
				
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<Customer> CQ = CB.createQuery(Customer.class);
			Root<Customer> root = CQ.from(Customer.class);

		
//			
//			if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
//			else{CQ.orderBy(CB.desc(root.get(propertyName)));}
			
			CQ.select(root).distinct(true).where(CB.equal(root.get(propertyName), value));
			
			Query query = entityManager.createQuery(CQ);
			if(fetchOrNot == true) {
				EntityGraph entityGraph = entityManager.getEntityGraph("customer.fetch.1");
				query.setHint("javax.persistence.fetchgraph", entityGraph);
			}
			
			List<Customer> result =  query.getResultList();
			
			return result;
		}


	@Override
	public List<Customer> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																	boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){

		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> CQ = CB.createQuery(Customer.class);
		Root<Customer> root = CQ.from(Customer.class);
		
		
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
		if(fetchOrNot == true) {
			EntityGraph entityGraph = entityManager.getEntityGraph("customer.fetch.1");
			query.setHint("javax.persistence.fetchgraph", entityGraph);
		}
		
		List<Customer> result =  query.getResultList();
		
		return result;
	}
		
	
	@Override
	public void save(Customer theOne) {
			
		entityManager.persist(theOne);
	}
	
	@Override
		public void reconnect(Customer player) {
				
			entityManager.refresh(player);
		}
	
	@Override
	public Customer update(Customer player) {
		
		return (Customer) entityManager.merge(player);
	}

	@Override
		public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
															Object newValue) {
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
			CriteriaUpdate<Customer> CD = CB.createCriteriaUpdate(Customer.class);
			Root<Customer> root = CD.from(Customer.class);
		
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
		Customer reservation = entityManager.find(Customer.class, id);	
		entityManager.remove(reservation);		
	}

	
	
	@Override
		public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
				
			CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
			CriteriaDelete<Customer> CD = CB.createCriteriaDelete(Customer.class);
			Root<Customer> root = CD.from(Customer.class);
			
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
