package KJcompany.KSAS.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import KJcompany.KSAS.common.security.domain.CustomUser;
import KJcompany.KSAS.domain.Board;
import KJcompany.KSAS.domain.Member;
import KJcompany.KSAS.dto.CodeLabelValue;
import KJcompany.KSAS.dto.PaginationDTO;
import KJcompany.KSAS.service.BoardService;
import KJcompany.KSAS.service.BoardServiceImpl;
import KJcompany.KSAS.vo.PageRequestVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

	private final BoardService service;

	@GetMapping("/register")
	@PreAuthorize("hasRole('MEMBER')")
	public void registerForm(Model model, Authentication authentication) throws Exception {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Member member = customUser.getMember();

		Board board = new Board();

		board.setWriter(member.getUserId());

		model.addAttribute(board);
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('MEMBER')")
	public String register(@Validated Board board, BindingResult result, RedirectAttributes rttr) throws Exception {
		if(result.hasErrors()) {
			return "board/register";
		}	
		
		service.register(board);

		rttr.addFlashAttribute("msg", "SUCCESS");
		
		return "redirect:/board/list";
	}

	@GetMapping("/list")
	public void list(@ModelAttribute("pgrq") PageRequestVO pageRequestVO, Model model) throws Exception {
		Page<Board> page = service.list(pageRequestVO);
	
		//PaginationDTO<Board> pagination = new PaginationDTO<>(page);
		//pagination.setPageRequest(pageRequestVO);
		
		model.addAttribute("pgntn", new PaginationDTO<>(page));
		
		List<CodeLabelValue> searchTypeCodeValueList = new ArrayList<CodeLabelValue>();
		searchTypeCodeValueList.add(new CodeLabelValue("n", "---"));
		searchTypeCodeValueList.add(new CodeLabelValue("t", "Title"));
		searchTypeCodeValueList.add(new CodeLabelValue("c", "Content"));
		searchTypeCodeValueList.add(new CodeLabelValue("w", "Writer"));
		searchTypeCodeValueList.add(new CodeLabelValue("tc", "Title OR Content"));
		searchTypeCodeValueList.add(new CodeLabelValue("cw", "Content OR Writer"));
		searchTypeCodeValueList.add(new CodeLabelValue("tcw", "Title OR Content OR Writer"));

		model.addAttribute("searchTypeCodeValueList", searchTypeCodeValueList);
	}


	@GetMapping("/read")
	public String read(Long boardNo, Board board, @ModelAttribute("pgrq") PageRequestVO pageRequestVO, Model model,HttpServletRequest req,HttpServletResponse res) throws Exception {
		// 저장된 쿠키 불러오기
		Cookie cookies[] = req.getCookies();
		Map map = new HashMap();
		if(req.getCookies() != null){
			for (int i = 0; i < cookies.length; i++) {
				Cookie obj = cookies[i];
				map.put(obj.getName(),obj.getValue());
			}
		}

		// 저장된 쿠키중에 read_count 만 불러오기
		String readCount = (String) map.get("read_count");
		// 저장될 새로운 쿠키값 생성
		String newReadCount = "|" + boardNo;

		// 저장된 쿠키에 새로운 쿠키값이 존재하는 지 검사
		if ( StringUtils.indexOfIgnoreCase(readCount, newReadCount) == -1 ) {
			// 없을 경우 쿠키 생성
			Cookie cookie = new Cookie("read_count", readCount + newReadCount);

			res.addCookie(cookie);
			model.addAttribute(service.read(boardNo));
			Integer i = service.read(boardNo).getViews()+1;
			Board boardTemp = service.read(boardNo);
			boardTemp.setViews(i);
			service.views(boardTemp);
			return "/board/read";
		}

		model.addAttribute(service.read(boardNo));
		return "/board/read";
	}

	@PostMapping("/remove")
	@PreAuthorize("(hasRole('MEMBER') and principal.username == #writer) or hasRole('ADMIN')")
	public String remove(Long boardNo, PageRequestVO pageRequestVO, RedirectAttributes rttr, String writer) throws Exception {
		service.remove(boardNo);

		rttr.addAttribute("page", pageRequestVO.getPage());
		rttr.addAttribute("sizePerPage", pageRequestVO.getSizePerPage());
		rttr.addAttribute("searchType", pageRequestVO.getSearchType());
		rttr.addAttribute("keyword", pageRequestVO.getKeyword());
	   
		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/list";
	}

	@GetMapping("/modify")
	@PreAuthorize("hasRole('MEMBER')")
	public void modifyForm(Long boardNo, @ModelAttribute("pgrq") PageRequestVO pageRequestVO, Model model) throws Exception {

		model.addAttribute(service.read(boardNo));
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('MEMBER') and principal.username == #board.writer")
	public String modify(@Validated Board board, BindingResult result, @ModelAttribute("pgrq") PageRequestVO pageRequestVO, RedirectAttributes rttr) throws Exception {
		if(result.hasErrors()) {
			return "board/modify";
		}		
		
		service.modify(board);

		rttr.addAttribute("page", pageRequestVO.getPage());
		rttr.addAttribute("sizePerPage", pageRequestVO.getSizePerPage());
		rttr.addAttribute("searchType", pageRequestVO.getSearchType());
		rttr.addAttribute("keyword", pageRequestVO.getKeyword());
	    
		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/list";
	}

}
