package edu.kh.comm.member.model.service;

import java.util.Map;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {

	/** 회원 정보 변경 
	 * @param paramMap
	 * @param loginMember
	 * @return
	 */
	Map<String, Object> updateinfo(Map<String, Object> paramMap, Member loginMember);

	

	
	
	

}
