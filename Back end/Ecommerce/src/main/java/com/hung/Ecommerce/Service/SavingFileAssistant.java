package com.hung.Ecommerce.Service;

import java.io.File;
import static java.lang.System.*;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hung.Ecommerce.DTO.MultipartHolder;
import com.hung.Ecommerce.Model.StaticResourceMapper;
import com.hung.Ecommerce.Util.ByteToHex;

@Service()
@Transactional
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SavingFileAssistant {
		
	@Autowired
		StaticResourceMapperService mapperService;
	
	private List<String> successfulList;
	private Map<String, String> failedList;
		
	public SavingFileAssistant() throws NoSuchAlgorithmException {
		super();
		this.successfulList = new ArrayList<String>();
		this.failedList = new HashMap<String, String>();
	}

//	@Async("TaskExecutor_In_serviceConfig")
		public List<StaticResourceMapper> saveFile(List<MultipartFile> files,
								String path, String contentTypeToCheck) throws IOException {
			
			MessageDigest salt = null;
			try {
				salt = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			
			List<String> names = files.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList());
			
			out.println("HERE: SAVEFILE GOT CALLED: " + names);
			
			List<StaticResourceMapper> key_FileName = new LinkedList<StaticResourceMapper>();
						
				for (MultipartFile file : files) {
				
						salt.update(UUID.randomUUID().toString().getBytes());
						String fileKey = ByteToHex.bytesToHex(salt.digest());
					
						String originalName = file.getOriginalFilename();
						String contentType = file.getContentType();
						
				// If the file's content type is not "image", wont save it, add it to failed list
						if(!contentType.substring(0, contentType.indexOf("/")).equals(contentTypeToCheck)) {
								this.failedList.put(originalName, "Invalid file type");
								continue;
						}
						
						int indexOfExtension = originalName.lastIndexOf(".")+1;
						
				// If the file's name does not have an extension, wont save it, add it to failed list		
						if(indexOfExtension < 0) {
								this.failedList.put(originalName, "File name does not contain extension");
								continue;
						}
						
						String fileExtension = originalName.substring(indexOfExtension);
						String newFileName = UUID.randomUUID().toString() + "."+fileExtension;
						
						File savingPath = new File(path + newFileName);
						
						try {
								file.transferTo(savingPath);
								successfulList.add(originalName);
								StaticResourceMapper mapper = new StaticResourceMapper();
								mapper.setFileKey(fileKey);
								mapper.setFileName(newFileName);
								mapperService.save(mapper);
								key_FileName.add(mapper);
						} catch (IllegalStateException e) {
								e.printStackTrace();
								throw e;
						} catch (IOException e) {
								e.printStackTrace();
								this.failedList.put(originalName, "An error occurred while saving this file");
								continue;
						}			
				}
			
				return key_FileName;
		}
	
	@Async("TaskExecutor_In_serviceConfig")
	public CompletableFuture<List<StaticResourceMapper>> saveFiles(List<MultipartHolder> files,
							String path, String contentTypeToCheck) throws IOException {
		
		MessageDigest salt = null;
		try {
			salt = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		List<String> names = files.stream().map(file -> file.getFileName()).collect(Collectors.toList());
		
		out.println("HERE: SAVEFILE GOT CALLED: " + names);
		
		List<StaticResourceMapper> key_FileName = new ArrayList<StaticResourceMapper>();
					
			for (MultipartHolder file : files) {
					
					salt.update(UUID.randomUUID().toString().getBytes());
					String fileKey = ByteToHex.bytesToHex(salt.digest());
				
					String originalName = file.getFileName();
					String contentType = file.getFileContentType();
					
			// If the file's content type is not "image", wont save it, add it to failed list
					if(!contentType.substring(0, contentType.indexOf("/")).equals(contentTypeToCheck)) {
							this.failedList.put(originalName, "Invalid file type");
							continue;
					}
					
					int indexOfExtension = originalName.lastIndexOf(".")+1;
					
			// If the file's name does not have an extension, wont save it, add it to failed list		
					if(indexOfExtension < 0) {
							this.failedList.put(originalName, "File name does not contain extension");
							continue;
					}
					
					String fileExtension = originalName.substring(indexOfExtension);
					String newFileName = UUID.randomUUID().toString() + "."+fileExtension;
					
					File savingPath = new File(path + newFileName);
					
					try {
							Files.write(savingPath.toPath(), file.getFileData());
							successfulList.add(originalName);
							StaticResourceMapper mapper = new StaticResourceMapper();
							mapper.setFileKey(fileKey);
							mapper.setFileName(newFileName);
							mapperService.save(mapper);
							key_FileName.add(mapper);
					} catch (IllegalStateException e) {
							e.printStackTrace();
							throw e;
					} catch (IOException e) {
							e.printStackTrace();
							this.failedList.put(originalName, "An error occurred while saving this file");
							continue;
					}			
			}
		
			return CompletableFuture.completedFuture(key_FileName);
	}
	

	public Map<String, String> getFailedList() {
		return failedList;
	}


	public void setFailedList(Map<String, String> failedList) {
		this.failedList = failedList;
	}

}
