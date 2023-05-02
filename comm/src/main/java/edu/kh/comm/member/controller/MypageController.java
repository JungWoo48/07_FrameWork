package edu.kh.comm.member.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.member.model.service.MyPageService;
import edu.kh.comm.member.model.vo.Member;


@Controller
@RequestMapping("/member/myPage")
@SessionAttributes({"loginMember"}) // session scope에서 loginMember를 얻어옴
public class MyPageController {
	private Logger logger = LoggerFactory.getLogger(MyPageController.class);
	
	@Autowired
	private MyPageService service;
	
	// 회원 정보 조회
	@GetMapping("/info")
	public String info() {
		return "member/myPage-info";
	}
	
	// 비밀번호 변경
	@GetMapping("/changePw")
	public String changePw() {
		return "member/myPage-changePw";
	}
	
	// 회원 탈퇴
	@GetMapping("/secession")
	public String secession() {
		return "member/myPage-secession";
	}
	
	// 프로필 변경
	@GetMapping("/profile")
	public String profile() {
		return "member/myPage-profile";
	}
	
	//@PostMapping("/changePw")
	//public int changPw(String memberPw) {
	//	
	//}
	
	//회원정보 수정
	@PostMapping("/info")
	public String updateinfo(@ModelAttribute("loginMember") Member loginMember, 
				 @RequestParam Map<String, Object> paramMap, // 요청시 전달된 파라미터를 구분하지 않고 모두 맵에 담아서 얻어온다
				 String[] updateAdress,
				 RedirectAttributes ra) {
		
		
		logger.info("회원정보 수정 수행");
		
		
		//필요한 값 - 닉네임, 주소, 전화번호
		// 주소는 (String[]로 얻어와서 String.join()을 이영해서 문자열로 변경
		// 회원번호(Session-> 로그인한 회원 정보를 통해서 얻어옴)
		// 	--> @ModelAttribute, @SessionAttributes가 필요
		
		//  @SessionAttributes의 역할 2개
		//	1) Model에 세팅되어 있는 key값을 @SessionAtribute에 작성 하면
		// 	해당 키값과 같은 Model에 세팅된 데이터를 request -> session scope로 이동
		
		//  2) 기존에 session scope로 이동시킨 값을 얻어옴
		//  @ModelAttribute("loginMember") Member loginMember
		// 				   [session key]
		//  --> @SessionAttribute를 통해 session scope에 등록된 값을 얻어와
		//  	오른쪽에 작성된 Member loginMember 변수에 대입
		//		단, @SessionAttribute({"lginMember"})가 클래스 위에 작성 되어 있어야 가능
		
		// *** 매개병수를 이용해서 session, 파라미터 데이터를 동시에 얻어올떄 문제점 ***
		// session에서  객체를 얻어오기 위해 매개변수에 작성한 상태
		// @ModelAttribute("loginMember") Member loginMember
		
		// 파라미터의 name 속성 값(name="memberNickname")이 매개변수에 작성된 객체의 필드(member.vo)와 일치하면 
		// session에서 얻어온 객체의 필드에 덮어쓰기가 된다 -> 이래서는 안된다
		
		// 해결법 : 파라미터의 name속성을 변경해서 얻어온다(필드명을 다르게)
		
		// 파라미터를 저장한 paramMap에 회원번호, 주소를 추가
		String memberAddress = String.join(",,", updateAdress); // 주소 배열 -> 문자열 변환
		
		// 주소 미입력시 
		if(memberAddress.equals(",,,,")) memberAddress = null;
		
		paramMap.put("memberNO", loginMember.getMemberNo());
		paramMap.put("memberAdress", memberAddress);
		
		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(paramMap);
		
		String message = null;
		
		if(result > 0) {
			message = "회원정보 수정됨";
			
			// 바뀐 로그인 정보 동기화
			loginMember.setMemberNickname( (String) paramMap.get("updateNickname"));
			loginMember.setMemberTel((String) paramMap.get("updateTel"));
			loginMember.setMemberAddress((String) paramMap.get("updateAddress"));
			
		} else {
			message = "회원정보 수정실패";
		}
		ra.addFlashAttribute("message", message);
		
		return "/redirect:info";
	}

	// 비밀번호 변경
	
	@PostMapping("/chagePw")
	public String chagePw( @RequestParam Map<String,Object> paramMap,
							@ModelAttribute("loginMember") Member loginMember,
							RedirectAttributes ra 
			) {
		
		// 로그인 된 회원의 번호를 paramMap에 추가
		paramMap.put("memberNO", loginMember.getMemberNo());
		
		// 비밀번호 변경 서비스 호출
		int result = service.changePw(paramMap);
		
		String message = null;
		String path = null;
		
		if(result >0) {
			message = "비밀번호 변경";
			path = "info";
		} else {
			message = "비밀번호 일치하지 않음";
			path = "changePw";
		}
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
	
	// 회원 탈퇴 
	@PostMapping("/secession")
	public String secession( @ModelAttribute("loginMember") Member loginMember, 
							SessionStatus status,
							RedirectAttributes ra,
							HttpServletResponse resp,
							HttpServletRequest req ) {
		
		// 탈퇴 서비스 호출
		int result = service.secession(loginMember);
		
		// 탈퇴 성공시 메인 페이지
		// 세션 없에기
		// 쿠키 비우기
		String message = null;
		String path = null;
		
		if(result >0) {
			message = "탈퇴됨";
			path = "/";
			
			// 세션 없에기
			status.setComplete();		
			
			// 쿠키 비우기
			Cookie cookie = new Cookie("saveid", "");
			cookie.setMaxAge(0);
			cookie.setPath(req.getContextPath());
			resp.addCookie(cookie);
			
			
		} else {
			message = "비밀번호 일치하지 않음";
			path = "secession";
		}
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
		
	
	
	

}
