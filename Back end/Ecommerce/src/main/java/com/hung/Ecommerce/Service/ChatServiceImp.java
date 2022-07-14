package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.CartItem;
import com.hung.Ecommerce.Model.Chat;
import com.hung.Ecommerce.Repository.ChatRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class ChatServiceImp implements ChatService{

	private ChatRepository chatRepository;
	
	@Autowired
		public ChatServiceImp(ChatRepository chatRepository) {
			super();
			this.chatRepository = chatRepository;
		}

	@Override
	public Chat findById(int id, boolean fetchOrNot) {
		return chatRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<Chat> findAll(boolean fetchOrNot) {
		return chatRepository.findAll(fetchOrNot);
	}

	@Override
	public List<Chat> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return chatRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<Chat> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return chatRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(Chat theOne) {
		 chatRepository.save(theOne);
	}

	@Override
	public void reconnect(Chat chat) {
		chatRepository.reconnect(chat);
	}

	@Override
	public Chat update(Chat chat) {
		return chatRepository.update(chat);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return chatRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		chatRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return chatRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}
