package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.Model.Role;
import com.hung.Ecommerce.Model.StaticResourceMapper;
import com.hung.Ecommerce.Repository.SelectionOrder;
import com.hung.Ecommerce.Repository.StaticResourceMapperRepository;

@Service
@Transactional
public class StaticResourceMapperServiceImp implements StaticResourceMapperService{

	private StaticResourceMapperRepository staticResourceMapperRepository;
	
	@Autowired
		public StaticResourceMapperServiceImp(StaticResourceMapperRepository staticResourceMapperRepository, UserService userService) {
			super();
			this.staticResourceMapperRepository = staticResourceMapperRepository;
		}

	@Override
	public StaticResourceMapper findById(int id, boolean fetchOrNot) {
		return staticResourceMapperRepository.findById(id, fetchOrNot);
	}


	@Override
	public List<StaticResourceMapper> findAll(boolean fetchOrNot) {
		return staticResourceMapperRepository.findAll(fetchOrNot);
	}

	@Override
	public List<StaticResourceMapper> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return staticResourceMapperRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<StaticResourceMapper> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return staticResourceMapperRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(StaticResourceMapper theOne) {
		 staticResourceMapperRepository.save(theOne);
	}

	@Override
	public void reconnect(StaticResourceMapper staticResourceMapper) {
		staticResourceMapperRepository.reconnect(staticResourceMapper);
	}

	@Override
	public StaticResourceMapper update(StaticResourceMapper staticResourceMapper) {
		return staticResourceMapperRepository.update(staticResourceMapper);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return staticResourceMapperRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		staticResourceMapperRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return staticResourceMapperRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
	

	
}
