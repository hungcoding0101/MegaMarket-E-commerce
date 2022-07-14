package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Data
public class StaticResourceMapper {

	@Id
	@GeneratedValue(strategy =  GenerationType.SEQUENCE)
		Integer id;
	
	private String fileKey;
	
	private String fileName;
}
