package com.hung.Ecommerce.DTO;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestToUpdateAddresses {

	@NotEmpty(message = "You sent no data")
		private List<String> addresses;
}
