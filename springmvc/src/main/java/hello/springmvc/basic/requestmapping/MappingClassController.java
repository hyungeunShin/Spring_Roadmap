package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {
	@GetMapping
	public String users() {
		return "get users";
	}

	@PostMapping
	public String addUser() {
		return "post user";
	}

	@GetMapping("/{userId}")
	public String findUser(@PathVariable("userId") String userId) {
		return "get userId = " + userId;
	}

	@PatchMapping("/{userId}")
	public String updateUser(@PathVariable("userId") String userId) {
		return "update userId = " + userId;
	}

	@DeleteMapping("/{userId}")
	public String deleteUser(@PathVariable("userId") String userId) {
		return "delete userId = " + userId;
	}
}
