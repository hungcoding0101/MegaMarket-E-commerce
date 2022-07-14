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

import com.hung.Ecommerce.Model.Message;
import com.hung.Ecommerce.Model.Message;
import com.hung.Ecommerce.Model.Message;
import com.hung.Ecommerce.Model.Message;

@Repository
public class MessageRepositoryImp implements MessageRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Message findById(int id, boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> CQ = CB.createQuery(Message.class);
		Root<Message> root = CQ.from(Message.class);
		
		CQ.select(root).where(CB.equal(root.get("id"), id));
		Query query = entityManager.createQuery(CQ);
		List<Message> result =  query.getResultList();
		
		if(result.isEmpty()) {return null;}
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
		public List<Message> findAll(boolean fetchOrNot) {
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<Message> CQ = CB.createQuery(Message.class);
			Root<Message> root = CQ.from(Message.class);
			
			CQ.select(root);
			Query query = entityManager.createQuery(CQ);
			List<Message> result =  query.getResultList();
			
			return result;
		}



	@Override
		public List<Message> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																	boolean fetchOrNot) {
		
			
			CriteriaBuilder CB = entityManager.getCriteriaBuilder();
			CriteriaQuery<Message> CQ = CB.createQuery(Message.class);
			Root<Message> root = CQ.from(Message.class);

		
//			
//			if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
//			else{CQ.orderBy(CB.desc(root.get(propertyName)));}
			
			CQ.select(root).distinct(true).where(CB.equal(root.get(propertyName), value));
			
			Query query = entityManager.createQuery(CQ);
			List<Message> result = query.getResultList();
			
			return result;
		}


	@Override
	public List<Message> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																	boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){

		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> CQ = CB.createQuery(Message.class);
		Root<Message> root = CQ.from(Message.class);
		
		
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
		List<Message> result =  query.getResultList();

		return result;
	}
	

@Override
public void save(Message theOne) {
		
	entityManager.persist(theOne);
}

@Override
	public void reconnect(Message player) {
			
		entityManager.refresh(player);
	}

@Override
public Message update(Message player) {
	
	return (Message) entityManager.merge(player);
}

@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
														Object newValue) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaUpdate<Message> CD = CB.createCriteriaUpdate(Message.class);
		Root<Message> root = CD.from(Message.class);
	
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
	Message reservation = entityManager.find(Message.class, id);	
	entityManager.remove(reservation);		
}



@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaDelete<Message> CD = CB.createCriteriaDelete(Message.class);
		Root<Message> root = CD.from(Message.class);
		
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
