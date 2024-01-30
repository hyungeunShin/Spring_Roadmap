package hello.http.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.http.domain.Member;
import hello.http.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	
	@GetMapping("/members")
	public ResponseEntity<String> allMembers() {
		List<Member> members = memberService.findMembers();
		
		return new ResponseEntity<String>(members.toString(), HttpStatus.OK);
	}
	
	@GetMapping("/members/{id}")
	public ResponseEntity<String> oneMember(@PathVariable(name = "id") Long id) {
		Member member = null;
		try {
			member = memberService.findOne(id);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>(member.toString(), HttpStatus.OK);
	}
	
	@PostMapping("/members")
	public ResponseEntity<String> joinMember(@ModelAttribute Member member) {
		Long id = memberService.join(member);
		
		return new ResponseEntity<String>(id.toString(), HttpStatus.CREATED);
	}
	
	@PutMapping("/members")
	public ResponseEntity<String> updateMember(@ModelAttribute Member member) {
		Member result = memberService.update(member);
		return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
	}
	
	@DeleteMapping("/members/{id}")
	public ResponseEntity<String> deleteMember(@PathVariable(name = "id") Long id) {
		memberService.delete(id);
		return new ResponseEntity<String>("삭제완료", HttpStatus.OK);
	}
}
