package hello.itemservice.web.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginForm {
	@NotEmpty
	private String loginId;
	
	@NotEmpty
	private String password;
}