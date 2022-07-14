package com.hung.Ecommerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.Ecommerce.Repository.IndexRepository;

@Transactional
@Service
public class IndexService {

	@Autowired
		IndexRepository indexRepository;
	
	public void Reindexing() throws InterruptedException {
			indexRepository.Reindexing();
	}
}
