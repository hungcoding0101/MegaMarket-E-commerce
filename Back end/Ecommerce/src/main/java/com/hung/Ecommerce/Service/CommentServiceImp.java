package com.hung.Ecommerce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.Chat;
import com.hung.Ecommerce.Model.Comment;
import com.hung.Ecommerce.Repository.CommentRepository;
import com.hung.Ecommerce.Repository.SelectionOrder;

@Service
@Transactional
public class CommentServiceImp implements CommentService{

private CommentRepository commentRepository;
	
	@Autowired
		public CommentServiceImp(CommentRepository commentRepository) {
			super();
			this.commentRepository = commentRepository;
		}

	@Override
	public Comment findById(int id, boolean fetchOrNot) {
		return commentRepository.findById(id, fetchOrNot);
	}

	@Override
	public List<Comment> findAll(boolean fetchOrNot) {
		return commentRepository.findAll(fetchOrNot);
	}

	@Override
	public List<Comment> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
			boolean fetchOrNot) {
		return commentRepository.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public List<Comment> findByProperties(Map<String, Object> conditions, Map<String, SelectionOrder> orders, boolean TrueIsAnd_FalseIsOr, boolean fetchOrNot) throws IllegalArgumentException {
		return commentRepository.findByProperties(conditions, orders, TrueIsAnd_FalseIsOr, fetchOrNot);
	}

	@Override
	public void save(Comment theOne) {
		 commentRepository.save(theOne);
	}

	@Override
	public void reconnect(Comment comment) {
		commentRepository.reconnect(comment);
	}

	@Override
	public Comment update(Comment comment) {
		return commentRepository.update(comment);
	}

	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
			Object newValue) {
		return commentRepository.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
	}

	@Override
	public void delete(int id) {
		commentRepository.delete(id);
	}

	@Override
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		return commentRepository.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
	}
}
