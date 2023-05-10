package edu.kh.comm.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.board.model.dao.ReplyDAO;
import edu.kh.comm.board.model.vo.Reply;
import edu.kh.comm.common.Util;

@Service
public class RepleyServiceimpl implements ReplyService{

	@Autowired
	private ReplyDAO dao;

	// 댓글 목록 조회 서비스
	@Override
	public List<Reply> selectReplyList(int boardNo) {
		
		return dao.selectReplyList(boardNo);
	}

	@Override
	public int insertReply(Reply reply) {
		
		// xss, 개행문자 처리 필요
		reply.setReplyContent( Util.XSSHandling( reply.getReplyContent()));
		reply.setReplyContent( Util.newLineHandling( reply.getReplyContent()));
		
		
		return dao.insertReply(reply);
	}

	
}
