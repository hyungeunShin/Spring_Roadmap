package hello.upload.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item {
	private Long id;
	private String itemName;
	private UploadFile attachFile;
	private List<UploadFile> imageFiles;
}
