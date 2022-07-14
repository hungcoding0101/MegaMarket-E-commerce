package com.hung.Ecommerce.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;

import com.hung.Ecommerce.Model.KeyWords;
import com.hung.Ecommerce.Model.ProductCategory;

@Repository
public class KeyWordsRepositoryImp implements KeyWordsRepository{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
		public void save(KeyWords word) {
			entityManager.persist(word);
		}
	
	@Override
	public List<String> search(String keyWords, int offset, int limit){
			SearchSession session = Search.session(entityManager);
			
			SearchQuery<String> query =  session.search(KeyWords.class)
					.select(f -> f.field("word", String.class))
					.where(f -> f.bool()
									 .should( f.phrase().field("word").matching(keyWords)).boost(2.0f)
//									 .should(f.match().field("word").matching(keyWords))
								).toQuery();
			List<String> matches = query.fetchHits(offset, limit);
			return matches;
	}
}
