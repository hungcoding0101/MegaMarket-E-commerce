package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Repository.UploadProductRequestRepository;
import com.hung.Ecommerce.DTO.UploadProductRequest;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class UploadProductRequestServiceImp implements UploadProductRequestService{

	private UploadProductRequestRepository uploadProductRequestRepository;
	private AbstractRequestService abstractRequestService;

@Autowired
	public UploadProductRequestServiceImp(UploadProductRequestRepository uploadProductRequestRepository,
			AbstractRequestService abstractRequestService) {
		super();
		this.uploadProductRequestRepository = uploadProductRequestRepository;
		this.abstractRequestService = abstractRequestService;
	}

	@Override
	public UploadProductRequest findById(int id, boolean fetchOrNot) {
		return uploadProductRequestRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<UploadProductRequest> findAll(boolean fetchOrNot) {
		return uploadProductRequestRepository.findAll(fetchOrNot);
	}

	@Override
	public List<UploadProductRequest> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return uploadProductRequestRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<UploadProductRequest> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return uploadProductRequestRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(UploadProductRequest theOne) {
		 uploadProductRequestRepository.save(theOne);
	}

	@Override
	public void reconnect(UploadProductRequest uploadProductRequest) {
		uploadProductRequestRepository.reconnect(uploadProductRequest);
	}

	@Override
	public UploadProductRequest update(UploadProductRequest uploadProductRequest) {
		return uploadProductRequestRepository.update(uploadProductRequest);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return uploadProductRequestRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		uploadProductRequestRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return uploadProductRequestRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}
