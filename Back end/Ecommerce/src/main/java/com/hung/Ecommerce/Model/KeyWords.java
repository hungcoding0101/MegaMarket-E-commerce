package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import com.fasterxml.jackson.annotation.JsonView;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Indexed
public class KeyWords {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.SEQUENCE)
	@GenericField(sortable = Sortable.YES, name = "id_sort", projectable = Projectable.YES)
		private Integer id;
	
	@FullTextField( projectable = Projectable.YES)
	@KeywordField(name = "word_sort", projectable = Projectable.YES, sortable = Sortable.YES)
		private String word;
}
