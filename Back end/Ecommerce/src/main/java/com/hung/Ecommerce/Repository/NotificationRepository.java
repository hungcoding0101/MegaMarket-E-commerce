package com.hung.Ecommerce.Repository;

import java.util.List;

import com.hung.Ecommerce.Model.Notification;

public interface NotificationRepository extends CustomRepository<Notification> {

	public List<Notification> findManyByProperty(String propertyName, List<Object> values, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot);
}
