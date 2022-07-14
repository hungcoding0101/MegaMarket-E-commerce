package com.hung.Ecommerce.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Immutable;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hung.Ecommerce.CustomAnnotation.UniqueEmail;
import com.hung.Ecommerce.CustomAnnotation.UniquePhoneNumber;
import com.hung.Ecommerce.CustomAnnotation.UniqueUserName;
import com.hung.Ecommerce.Util.JsonConverter.Views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Indexed
public abstract class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonView(value = {Views.JSONUser_public.class})
	@GenericField(sortable = Sortable.YES, name = "id_sort")
		private Integer id;
	
	@Version
	@JsonView(value = {Views.JSONUser_public.class })
		private Long version;
	
	@OneToMany(mappedBy = "targetUser", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONUser_private.class})
	@OrderBy(value = "dateSent DESC")
	@JsonIgnoreProperties(value = {"targetUser"})
	@EqualsAndHashCode.Exclude
		private Set<Notification> notifications;
	
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	@JsonView(value = {Views.JSONUser_private.class})
	@OrderBy(value = "lastTimeModified DESC")
	@EqualsAndHashCode.Exclude
		private Set<Chat> chats;
	
	
//	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z ]*$" , message = "{Validation.Format.UserName}")
	@NotBlank(message = "{Validation.Format.UserName}")
	@JsonView(value = {Views.JSONUser_public.class })
	@FullTextField
	@KeywordField(name = "name_sort", projectable = Projectable.YES, sortable = Sortable.YES, aggregable = Aggregable.YES)
		private String username;
	
	
	@Pattern(regexp = ".{8,}", message = "{Validation.Format.Password}")
	@NotBlank(message = "{Validation.Format.Password}")
		private String password;
	
	
	@Email(message = "{Validation.Format.Email}")
	@NotBlank(message = "{Validation.Format.Email}")
	@JsonView(value = {Views.JSONUser_public.class })
	@Column(unique = true)
	@KeywordField(sortable = Sortable.YES, name = "email_sort")
		private String email;
	
	
	@Pattern(regexp = "^0[\\d]{9}$", message = "{Validation.Format.PhoneNumber}")
	@NotBlank(message = "{Validation.Format.PhoneNumber}")
	@JsonView(value = {Views.JSONUser_public.class })
	@Column(unique = true)
	@KeywordField(sortable = Sortable.YES, name = "phoneNumber_sort")
		private String phoneNumber;
	
	@GenericField
		private Long passwordsLastChange;
	
	@NotNull
	@GenericField
		private boolean isValid;
	
    @Enumerated(EnumType.STRING)
    @JsonView(value = {Views.JSONUser_private.class})
    @GenericField(sortable = Sortable.YES)
		private Role role;

    
//---------------------------------------------------------------------------
	public void addNotification(Notification notification) {
		this.notifications.add(notification);
		notification.setTargetUser(this);
	}
	
	public void addNotifications(List<Notification> notifications) {
		for(Notification notification: notifications) {
		addNotification(notification);
		}
	}
	
	public void removeNotification(Notification notification) {
		notification.setTargetUser(null);
		this.notifications.remove(notification);
	}
	
	public void removeNotifications() {
		 Iterator<Notification> iterator = this.notifications.iterator();
		 while (iterator.hasNext()) {
			 Notification notification = iterator.next();
			 notification.setTargetUser(null);
			 iterator.remove();
		 }
	}
}	
