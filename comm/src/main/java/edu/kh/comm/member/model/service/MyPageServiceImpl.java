package edu.kh.comm.member.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;

@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageDAO dao;

	/** 회원 정보 수정
	 *
	 */
	@Override
	public Map<String, Object> updateinfo(Map<String, Object> paramMap, Member loginMember) {
		
		return dao.updateinfo(paramMap, loginMember);
	}

}
