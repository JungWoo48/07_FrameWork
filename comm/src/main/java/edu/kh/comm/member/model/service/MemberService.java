package edu.kh.comm.member.model.service;

import java.util.List;

import edu.kh.comm.member.model.vo.Member;

/* Service interface를 사용하는 이유
 * 
 * 1. 프로젝트의 규칙성을 부여하기 위해서
 * 
 * 2. Spring AOP(Aspect Oriented Programming 관점 지향 프로그래밍)를 위해서 필요
 * (흩어진 관심사 : 소스코드상에서 계속 반복해서 사용되는 부분들 -- 유지보수가 어려움
 *  A 클래스와 B클래스에서 공통적으로 사용하는 코드일 경우 하나가 수정되면 다른쪽이 수정되기 어려움)
 *  이러한 공통적인 코드들을 모듈화하여 관리하는 것이 AOP
 *  
 *  3. 클래스간의 결합도를 약화 시키기 위하여 -> 유지보수성 향상
 * 
 * */
/**
 * 
 *
 */
public interface MemberService {
	
	// 모든 메서드가 추상 메서드이다.( 묵시적으로 public abstract)
	// 모든 필드는 상수이다.		 ( 묵시적으로 public static final)
	
	/** 로그인 Service
	 * @param inputMember
	 * @return loginMember
	 */
	public abstract Member login(Member inputMember);

	
	
	
	/** 이메일 중복 검사
	 * @param memberEmail
	 * @return
	 */
	public abstract int emailDupCheck(String memberEmail);




	/** 닉네임 중복 검사
	 * @param memberNickname
	 * @return
	 */
	public abstract int nicknameDupCheck(String memberNickname);




	/** 회원가입 service
	 * @param signUpMember
	 * @return
	 */
	public abstract int signUp(Member signUpMember);



	/** 회원 1명 정보 조회 서비스
	 * @param memberEmail
	 * @return 
	 */
	public abstract Member selectOne(String memberEmail);
	
	
	/** 회원 목록 조회 서비스
	 * @return list
	 */
	public abstract List<Member> selectAll();




}






