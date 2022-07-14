package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.Comment;
import com.hung.Ecommerce.Model.Message;
import com.hung.Ecommerce.Repository.MessageRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class MessageServiceImp implements MessageService{

private MessageRepository messageRepository;
	
	@Autowired
		public MessageServiceImp(MessageRepository messageRepository) {
			super();
			this.messageRepository = messageRepository;
		}

	@Override
	public Message findById(int id, boolean fetchOrNot) {
		return messageRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<Message> findAll(boolean fetchOrNot) {
		return messageRepository.findAll(fetchOrNot);
	}

	@Override
	public List<Message> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return messageRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<Message> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return messageRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(Message theOne) {
		 messageRepository.save(theOne);
	}

	@Override
	public void reconnect(Message message) {
		messageRepository.reconnect(message);
	}

	@Override
	public Message update(Message message) {
		return messageRepository.update(message);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return messageRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		messageRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return messageRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}
