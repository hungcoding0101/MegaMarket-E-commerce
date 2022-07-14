package com.hung.Ecommerce.Service;

import java.util.List;

import com.hung.Ecommerce.Model.Notification;
import com.hung.Ecommerce.Model.User;

public interface NotificationService extends AppService<Notification> {

	public List<Notification> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
	public void updateStatus(List<String> ids, String userName) throws Exception;
}
