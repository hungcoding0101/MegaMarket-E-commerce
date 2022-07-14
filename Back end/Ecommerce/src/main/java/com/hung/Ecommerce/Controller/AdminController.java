package com.hung.Ecommerce.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hung.Ecommerce.CustomExceptions.GeneralException;
import com.hung.Ecommerce.DTO.RequestToAddKeyWords;
import com.hung.Ecommerce.Model.KeyWords;
import com.hung.Ecommerce.Repository.KeyWordsRepository;
import com.hung.Ecommerce.Service.KeyWordsService;

@RestController
@RequestMapping("/mrAdmin")
public class AdminController {

	@Autowired
		private KeyWordsService keyWordsService;
	
	@PostMapping("/addKeyWords")
	public void addKeyWords(@RequestBody RequestToAddKeyWords words) throws GeneralException{
			try {
					for(String word: words.getWords()) {
						KeyWords keyWord = new KeyWords();
						keyWord.setWord(word);
						keyWordsService.save(keyWord);
					}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GeneralException("");
			}
	}
}
