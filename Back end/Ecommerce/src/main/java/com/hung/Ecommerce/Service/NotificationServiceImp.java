package com.hung.Ecommerce.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.Message;
import com.hung.Ecommerce.Model.Notification;
import com.hung.Ecommerce.Model.NotificationStatus;
import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Repository.NotificationRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
public class NotificationServiceImp implements NotificationService{

	@Autowired
		private NotificationRepository notificationRepository;
		
	@Autowired
		private UserService userService;


	@Transactional(readOnly = true)
	@Override
	public Notification findById(int id, boolean fetchOrNot) {
		return notificationRepository.findById(id, fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Notification> findAll(boolean fetchOrNot) {
		return notificationRepository.findAll(fetchOrNot);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Notification> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return notificationRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}
	
	
	@Transactional(readOnly = true)
	@Override
		public List<Notification> findManyByProperty(String propertyName, List<Object> values,
				boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
			return notificationRepository.findManyByProperty(propertyName, values, trueIsAscending_falseIsDescending,
						fetchOrNot);
		}

	
	@Transactional(readOnly = true)
	@Override
	public List<Notification> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return notificationRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Transactional
	@Override
	public void save(Notification theOne) {
		 notificationRepository.save(theOne);
	}

	@Transactional
	@Override
	public void reconnect(Notification player) {
		notificationRepository.reconnect(player);
	}

	@Transactional
	@Override
	public Notification update(Notification player) {
		return notificationRepository.update(player);
	}

	@Transactional
	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return notificationRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Transactional
	@Override
	public void delete(int id) {
		notificationRepository.delete(id);
	}

	@Transactional
	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return notificationRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}

	@Transactional
	@Override
	public void updateStatus(List<String> ids, String userName) throws Exception{
		try {			
			List<User> matchedUser = userService.findByProperty("username", userName, false, false);
			
			if(!matchedUser.isEmpty()) {
				User user = matchedUser.get(0);
				List<Notification> notifications = notificationRepository.findManyByProperty("id", new ArrayList<Object>(ids) ,false, false);
				for(int i = 0; i < notifications.size(); i++) {
						Notification notification = notifications.get(i);
						if(notification.getTargetUser().equals(user) && notification.getStatus().equals(NotificationStatus.NEW)) {
							notifications.get(i).setStatus(NotificationStatus.READ);
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}
}
