package com.hung.Ecommerce.Model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Data
public class Notification {

	@Id
	@GeneratedValue(strategy =  GenerationType.SEQUENCE)
	@JsonView(value = {Views.JSONNotification_public.class})
		private Integer id;
	
	@ManyToOne
	@NotNull
		private User targetUser;
	
	@NotBlank(message = "{Validation.Presence}")
	@JsonView(value = {Views.JSONNotification_public.class})
		private String subject;
	
	@JsonView(value = {Views.JSONNotification_public.class})
		private String contents;
	
	@Enumerated(EnumType.STRING)
	@JsonView(value = {Views.JSONNotification_public.class})
		private NotificationStatus status; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm, dd-MM-yyyy")
	@PastOrPresent
	@JsonView(value = {Views.JSONNotification_public.class})
		private LocalDateTime dateSent;
	
}
