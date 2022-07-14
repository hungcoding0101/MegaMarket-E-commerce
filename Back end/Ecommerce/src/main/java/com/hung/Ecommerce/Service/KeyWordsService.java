package com.hung.Ecommerce.Service;

import java.util.List;

import com.hung.Ecommerce.Model.KeyWords;

public interface KeyWordsService {
	
	public void save(KeyWords word);
	public List<String> search(String KeyWords, int offset, int limit);
}
