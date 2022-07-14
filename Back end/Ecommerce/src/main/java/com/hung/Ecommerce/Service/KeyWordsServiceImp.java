package com.hung.Ecommerce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Model.KeyWords;
import com.hung.Ecommerce.Repository.KeyWordsRepository;

@Service
@Transactional
public class KeyWordsServiceImp implements KeyWordsService{
	
	@Autowired
		private KeyWordsRepository keyWordsRepository; 

	@Override
	public List<String> search(String KeyWords, int offset, int limit) {
		return keyWordsRepository.search(KeyWords, offset, limit);
	}

	@Override
	public void save(KeyWords word) {
			keyWordsRepository.save(word);
	}

}
