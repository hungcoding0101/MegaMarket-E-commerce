package com.hung.Ecommerce.Service;

import java.security.NoSuchAlgorithmException;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.hung.Ecommerce.DTO.RequestToSignUp;
import com.hung.Ecommerce.DTO.RequestToUpdateOrder;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;
import com.hung.Ecommerce.DTO.UpdateOrderResult;
import com.hung.Ecommerce.DTO.UploadProductResult;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.Seller;

public interface SellerService extends AppService<Seller> {

	public void signUp(RequestToSignUp request) throws Exception;
	public void logOut(String accessToken, String refreshToken);
	public UploadProductResult uploadProduct(RequestToUploadProduct requestToUpload, MultipartFile avatarImage
			, MultipartFile[] otherImages
			, MultipartFile[] optionImages
					) throws NoSuchAlgorithmException;
	
	public UpdateOrderResult updateOrder(RequestToUpdateOrder[] requests, OAuth2Authentication authentication) throws Exception;
}
