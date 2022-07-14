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

import com.hung.Ecommerce.Model.StaticResourceMapper;

@Repository
public class StaticResourceMapperRepositoryImp implements StaticResourceMapperRepository{

	@PersistenceContext
	private EntityManager entityManager;

@Override
public StaticResourceMapper findById(int id, boolean fetchOrNot) {
	
	CriteriaBuilder CB = entityManager.getCriteriaBuilder();
	CriteriaQuery<StaticResourceMapper> CQ = CB.createQuery(StaticResourceMapper.class);
	Root<StaticResourceMapper> root = CQ.from(StaticResourceMapper.class);
	
	CQ.select(root).where(CB.equal(root.get("id"), id));
	
	Query query = entityManager.createQuery(CQ);
	List<StaticResourceMapper> result = query.getResultList();
	if(result.isEmpty()) {return null;}
	return result.get(0);
}

@SuppressWarnings("unchecked")
@Override
	public List<StaticResourceMapper> findAll(boolean fetchOrNot) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<StaticResourceMapper> CQ = CB.createQuery(StaticResourceMapper.class);
		Root<StaticResourceMapper> root = CQ.from(StaticResourceMapper.class);
		
		CQ.select(root);
		Query query = entityManager.createQuery(CQ);
		return query.getResultList();
	}



@Override
	public List<StaticResourceMapper> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																boolean fetchOrNot) {
	
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder();
		CriteriaQuery<StaticResourceMapper> CQ = CB.createQuery(StaticResourceMapper.class);
		Root<StaticResourceMapper> root = CQ.from(StaticResourceMapper.class);
		
		if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
		else{CQ.orderBy(CB.desc(root.get(propertyName)));}
		
		CQ.select(root).where(CB.equal(root.get(propertyName), value));
		
		Query query = entityManager.createQuery(CQ);
		return query.getResultList();
	}


@Override
public List<StaticResourceMapper> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders,
																boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot){

	
	CriteriaBuilder CB = entityManager.getCriteriaBuilder();
	CriteriaQuery<StaticResourceMapper> CQ = CB.createQuery(StaticResourceMapper.class);
	Root<StaticResourceMapper> root = CQ.from(StaticResourceMapper.class);
	
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
public void save(StaticResourceMapper theOne) {
		
	entityManager.persist(theOne);
}

@Override
	public void reconnect(StaticResourceMapper staticResourceMapper) {
			
		entityManager.refresh(staticResourceMapper);
	}

@Override
public StaticResourceMapper update(StaticResourceMapper staticResourceMapper) {
	
	return (StaticResourceMapper) entityManager.merge(staticResourceMapper);
}

@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
														Object newValue) {
		
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaUpdate<StaticResourceMapper> CD = CB.createCriteriaUpdate(StaticResourceMapper.class);
		Root<StaticResourceMapper> root = CD.from(StaticResourceMapper.class);
	
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
	StaticResourceMapper reservation = entityManager.find(StaticResourceMapper.class, id);	
	entityManager.remove(reservation);		
}



@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			
		CriteriaBuilder CB = entityManager.getCriteriaBuilder(); 
		CriteriaDelete<StaticResourceMapper> CD = CB.createCriteriaDelete(StaticResourceMapper.class);
		Root<StaticResourceMapper> root = CD.from(StaticResourceMapper.class);
		
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
