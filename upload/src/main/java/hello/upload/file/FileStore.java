package hello.upload.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import hello.upload.domain.UploadFile;

@Component
public class FileStore {
	@Value("${file.dir}")
	private String fileDir;
	
	public String getFullPath(String fileName) {
		return fileDir + fileName;
	}
	
	public List<UploadFile> storeFiles(List<MultipartFile> files) throws IllegalStateException, IOException {
		List<UploadFile> storeFileResult = new ArrayList<>();
		
		for(MultipartFile file : files) {
			if(!file.isEmpty()) {
				storeFileResult.add(storeFile(file));
			}
		}
		
		return storeFileResult;
	}
	
	public UploadFile storeFile(MultipartFile file) throws IllegalStateException, IOException {
		if(file.isEmpty()) {
			return null;
		}
		
		String originalFilename = file.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);
		
		file.transferTo(new File(getFullPath(storeFileName)));
		
		return new UploadFile(originalFilename, storeFileName);
	}
	
	private String createStoreFileName(String fileName) {
		String uuid = UUID.randomUUID().toString();
		String ext = extractExt(fileName);
		return uuid + "." + ext;
	}
	
	private String extractExt(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos + 1);
	}
}
