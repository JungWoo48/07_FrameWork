package edu.kh.comm.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository // 영혹성을 가지는 DB/파일과 연결되는 클래스임을 명시 + bean 등록
public class MemberDAO {
	
	// DAO는 DB랑 연결하기 위한 Connection이 공통적으로 필요하다
	// -> 필드에 선언
	// + Mybatis 영속성 프레임워크를 이영하려면 Connection을 이용해 만들어진 객체
	// SqlSessionTemplate을 사용
	
	@Autowired // root-contenxt.xml 에서 생성된 sqlSessionTemplate bean을 의존성 주입(DI)한것
	private SqlSessionTemplate sqlSession;
	
	private Logger logger = LoggerFactory.getLogger(MemberDAO.class);
	
	public Member login(Member inputMember) {
		
		// 1행 조회(파라미터x) 방법.
		//int count = sqlSession.selectOne("namespace값.id값");
		
		//int count = sqlSession.selectOne("memberMapper.test1");
		//logger.debug(count + "");
		
		// 1행 조회(파라미터O) 방법
		//String memberNickname = sqlSession.selectOne("memberMapper.test2", inputMember.getMemberEmail());
		//logger.debug(memberNickname);
		
		// 1행 조회(파리미터가 VO인 경우)
		//String memberTel = sqlSession.selectOne("memberMapper.test3", inputMember);//inputmember = email, pw
		//logger.debug(memberTel);
		
		// 1행 조회(파라미터와 반환값이 모두 Vo인 경우
		Member loginMember = sqlSession.selectOne("memberMapper.login", inputMember);
		
		
		return loginMember;
	}

	
	
	/** 이메일 중복 검사 DAO
	 * @param memberEmail
	 * @return result
	 */
	public int emailDupCheck(String memberEmail) {
		
		return sqlSession.selectOne("memberMapper.emailDupCheck", memberEmail);
	}



	/** 닉네임 중복 검사
	 * @param memberNickname
	 * @return
	 */
	public int nicknameDupCheck(String memberNickname) {
		
		return sqlSession.selectOne("memberMapper.nicknameDupCheck", memberNickname);
	}
	
	
	/** 회원가입 DAO
	 * @param signUpMember
	 * @return
	 */
	public int singUp(Member signUpMember) {
		
		int result = sqlSession.insert("memberMapper.signUp", signUpMember);
		
		
		return result;
		
		
	}



	public int selectOne(String memberEmail) {
	
		return sqlSession.selectOne("memberMapper.selectOne", memberEmail);
	}
	

}
