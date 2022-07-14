package com.hung.Ecommerce.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.extractor.builtin.BuiltinContainerExtractors;
import org.springframework.stereotype.Repository;

@Repository
public class IndexRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public void Reindexing() throws InterruptedException {
		SearchSession searchSession = Search.session( entityManager );
		searchSession.massIndexer().threadsToLoadObjects(15).batchSizeToLoadObjects(20).startAndWait(); 
	}
}
