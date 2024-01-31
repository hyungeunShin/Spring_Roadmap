package hello.http.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
	
	//회원 목록 조회
	@GetMapping("/members")
	public ResponseEntity<String> allMembers() {
		List<Member> members = memberService.findMembers();
		
		return new ResponseEntity<String>("회원 목록 조회 : " + members.toString(), HttpStatus.OK);
	}
	
	//회원 등록
	@PostMapping("/members")
	public ResponseEntity<String> joinMember(@ModelAttribute Member member) throws Exception {
		try {
			Member save = memberService.join(member);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI("/members/" + member.getId()));
			return new ResponseEntity<String>("회원 등록 : " + save.toString(), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//회원 조회
	@GetMapping("/members/{id}")
	public ResponseEntity<String> oneMember(@PathVariable(name = "id") Long id) {
		try {
			Member member = memberService.findOne(id);
			return new ResponseEntity<String>("회원 조회 : " + member.toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	//회원 수정1
	@PutMapping("/members/{id}")
	public ResponseEntity<String> joinOrUpdateMember(@ModelAttribute Member member) {
		Map<String, Object> result = memberService.overwrite(member);
		
		if((Integer) result.get("key") == 1) {
			return new ResponseEntity<String>("회원 등록 : " + result.get("member").toString(), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("회원 수정 : " + result.get("member").toString(), HttpStatus.OK);
		}
	}
	
	//회원 수정2
	@PatchMapping("/members/{id}")
	public ResponseEntity<String> updateMember(@ModelAttribute Member member) {
		try {
			Member update = memberService.update(member);
			return new ResponseEntity<String>("회원 수정2 : " + update.toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	//회원 삭제
	@DeleteMapping("/members/{id}")
	public ResponseEntity<String> deleteMember(@PathVariable(name = "id") Long id) {
		try {
			Member member = memberService.delete(id);
			return new ResponseEntity<String>("삭제된 회원 : " + member.toString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
