package com.hung.Ecommerce.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hung.Ecommerce.Model.KeyWords;


public interface KeyWordsRepository{
	
	public void save(KeyWords word);
	public List<String> search(String KeyWords, int offset, int limit);
}
