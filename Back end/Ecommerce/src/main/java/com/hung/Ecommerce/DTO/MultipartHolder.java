package com.hung.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultipartHolder {

	private byte[] fileData;
	private String fileName;
	private String fileContentType;
}
