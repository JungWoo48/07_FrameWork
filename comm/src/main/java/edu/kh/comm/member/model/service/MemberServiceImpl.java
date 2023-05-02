package edu.kh.comm.member.model.service;

import org.slf4j.LoggerFactory;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.model.dao.MemberDAO;
import edu.kh.comm.member.model.vo.Member;

@Service // 비즈니스 로직(데이터 가송, DB연결)을 처리하는 클래스임을 명시 + bean 등록
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDAO dao;
	
	/* Connection을 Service에서 얻어왔던 이유
	 * 
	 * -Service의 메서드 하나는 요청을 처리하는 업무 단위
	 * -> 해당 업무가 끝난 후 트랙잭션을 한번에 처리하기 위해서
	 *    어쩡수 없이 Connection을 Service에서 얻어올수 밖에 없엇음
	 * 
	 * */
	
	// 암호화를 위한 bcrypt 의존성 주입(DI)
	@Autowired
	private BCryptPasswordEncoder bcrypt; 
	
	private Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

	// 로그인 서비스 구현
	@Override
	public Member login(Member inputMember) {
		
		// 전달받은 비밀번호를 암호화 하여
		// DB에서 조회한 비밀번호와 비교(db와 비교 x)
		
		// sha 방식 암호화
		// A회원 / 비밀번호 1234 -> 암호화 : abcd
		// b회원 / 비밀번호 1234 -> 암호화 : abcd ( 암호화시 내용이 같다)
		
		// Bcrypt 암호화 : 암호화 하기전에 salt 를 추가 하여 변형된 상태로 암호화를 진행
		// A회원 / 비밀번호 1234 -> abcd
		// b회원 / 비밀번호 1234 -> @bgh
		
		// 매번 암호화 되는 비밀번호가 다르기 떄문에 직접 비교가 불가능 하다
		// 대신 Bcrypt 암호화를 지원하는 객체가
		// 이를 비교하는 기능(메서드) 가지고 있어서 이를 활용
		
		// ** Bcrypt 암호화를 사용하기 위해 이를 지원 하는 Spring-security 모듈 추가
		//logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		
		
		Member loginMember = dao.login(inputMember);
		
		// loginMember == null : 일치하는 이메일이 없다
		
		if(loginMember !=null) { // 일치하는 이메일을 가진 회원정보가 있을경우
			
			if(bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) { //비밀번호가 일치할경우
								// 평문 					암호문
				loginMember.setMemberPw(null); // 비교후  비밀번호를 세션에 올리지 않기 위해 지우기
					
			} else { // 일치하지 않을 경우
				loginMember = null;
			}
			
		}
		
		return loginMember;
		
		// Connection을 얻어오거나/반환하거나
		// 트랜잭션 처리를 하는 구문을 작성하지 않아도
		// Spring에서 제어를 하기 때문에 Servie 구문이 간단해진다
	}
	
	// 이메일 중복 검사
	@Override
	public int emailDupCheck(String memberEmail) {
		
		
		return dao.emailDupCheck(memberEmail);
	}
	
	// 닉네임 중복 검사
	@Override
	public int nicknameDupCheck(String memberNickname) {
		
		return dao.nicknameDupCheck(memberNickname);
	}

	/**
	 *회원가입 서비스 구현
	 */
	@Override
	public int signUp(Member signUpMember) {
		
		// 비밀번호 암호화
		String encPw = bcrypt.encode( signUpMember.getMemberPw());
		
		// 암호화된 비밀번호로 다시 세팅
		signUpMember.setMemberPw(encPw);
		
		int result  = dao.singUp(signUpMember);
		
		return result;
		
	}
	
	// 회원 1명 정보 조회 서비스 구현
		@Override
		public Member selectOne(String memberEmail) {
			return dao.selectOne(memberEmail);
		}


		// 회원 목록 조회 서비스 구현
		@Override
		public List<Member> selectAll() {
			return dao.selectAll();
		}
		
	
	
	
	
}
