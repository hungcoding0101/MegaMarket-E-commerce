package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hung.Ecommerce.Model.AbstractRequest;
import com.hung.Ecommerce.Repository.AbstractRequestRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
public class AbstractRequestServiceImp implements AbstractRequestService{

private AbstractRequestRepository abstractRequestRepository;
	
	@Autowired
		public AbstractRequestServiceImp(AbstractRequestRepository abstractRequestRepository) {
			super();
			this.abstractRequestRepository = abstractRequestRepository;
		}

	@Override
	public AbstractRequest findById(int id, boolean fetchOrNot) {
		return abstractRequestRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<AbstractRequest> findAll(boolean fetchOrNot) {
		return abstractRequestRepository.findAll(fetchOrNot);
	}

	@Override
	public List<AbstractRequest> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return abstractRequestRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<AbstractRequest> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return abstractRequestRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(AbstractRequest theOne) {
		 abstractRequestRepository.save(theOne);
	}

	@Override
	public void reconnect(AbstractRequest abstractRequest) {
		abstractRequestRepository.reconnect(abstractRequest);
	}

	@Override
	public AbstractRequest update(AbstractRequest abstractRequest) {
		return abstractRequestRepository.update(abstractRequest);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return abstractRequestRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		abstractRequestRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return abstractRequestRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}
