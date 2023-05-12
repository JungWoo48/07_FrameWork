package edu.kh.comm.board.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import edu.kh.comm.board.model.service.ReplyService;
import edu.kh.comm.board.model.vo.Reply;

@RestController // Representataional state Transfer - 자원을 이름으로 구분하여 자원의 상태를 주고받는것
				// 특정한 이름(주소)로 요청이 오면 값으로 응답
				// 요청에 따른 응답이 모두 데이터(값) 자체인 컨트롤러
				// @Controller + @ResponseBody
				// ajax여야 사용 가능
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	// 댓글 목록 조회
	@GetMapping("/selectReplyList")
	public String selectReplyList( int boardNo) {
		List<Reply> rList = service.selectReplyList(boardNo);
		
		return new Gson().toJson(rList);	
	}
	
	// 댓글 등록
	@PostMapping("/insert")
	public int insertReply(Reply reply) {
		
		return service.insertReply(reply);
		
	}
	
	
	// 댓글 수정
	
	@PostMapping("/update")
	public int updateReply(Reply reply) {
		
		return service.updateReply(reply);
		
	}
	
	// 댓글 삭제
	@GetMapping("/delete")
	public int deleteReply(int replyNo) {
		
		return service.deleteReply(replyNo);
	}

}
