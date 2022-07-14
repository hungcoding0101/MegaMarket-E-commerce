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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.DTO.UploadProductRequest;

@Repository
public class UploadProductRequestRepositoryImp implements UploadProductRequestRepository{

	@PersistenceContext
	private EntityManager entityManager;

@Override
public UploadProductRequest findById(int id, boolean fetchOrNot) {
	
	CriteriaBuilder CB = entityManager.getCriteriaBuilder();
	CriteriaQuery<UploadProductRequest> CQ = CB.createQuery(UploadProductRequest.class);
	Root<UploadProductRequest> root = CQ.from(UploadProductRequest.class);
	
	CQ.select(root).where(CB.equal(root.get("id"), id));
	
	Query query = entityManager.createQuery(CQ);
	List<UploadProductRequest> result = query.getResultList();
	if(result.isEmpty()) {return null;}
	return result.get(0);
}

@SuppressWarnings("unchecked")
@Override
	public List<UploadProductRequest> findAll(boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<UploadProductRequest> CQ = CB.createQuery(UploadProductRequest.class);
		Root<UploadProductRequest> root = CQ.from(UploadProductRequest.class);
		
		CQ.select(root);
		Query query = entityManager.createQuery(CQ);
		return query.getResultList();
	}



@Override
	public List<UploadProductRequest> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																boolean fetchOrNot) {
	
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<UploadProductRequest> CQ = CB.createQuery(UploadProductRequest.class);
		Root<UploadProductRequest> root = CQ.from(UploadProductRequest.class);
		
		if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
		else{CQ.orderBy(CB.desc(root.get(propertyName)));}
		
		CQ.select(root).where(CB.equal(root.get(propertyName), value));
		
		Query query = entityManager.createQuery(CQ);
		return query.getResultList();
	}


@Override
	public List<UploadProductRequest> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																	boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){
	
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<UploadProductRequest> CQ = CB.createQuery(UploadProductRequest.class);
		Root<UploadProductRequest> root = CQ.from(UploadProductRequest.class);
		
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
	return query.getResultList();
}
	

@Override
public void save(UploadProductRequest theOne) {
		
	entityManager.persist(theOne);
	entityManager.flush();
}

@Override
	public void reconnect(UploadProductRequest uploadProductRequest) {
			
		entityManager.refresh(uploadProductRequest);
	}

@Override
public UploadProductRequest update(UploadProductRequest uploadProductRequest) {
	
	return (UploadProductRequest) entityManager.merge(uploadProductRequest);
}

@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
														Object newValue) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaUpdate<UploadProductRequest> CD = CB.createCriteriaUpdate(UploadProductRequest.class);
		Root<UploadProductRequest> root = CD.from(UploadProductRequest.class);
	
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
	UploadProductRequest reservation = entityManager.find(UploadProductRequest.class, id);	
	entityManager.remove(reservation);		
}



@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaDelete<UploadProductRequest> CD = CB.createCriteriaDelete(UploadProductRequest.class);
		Root<UploadProductRequest> root = CD.from(UploadProductRequest.class);
		
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
