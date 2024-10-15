package com.ktdsuniversity.edu.hello_spring.bbs.web;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.DeleteBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.FileHandler;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.PageNotFoundException;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class BoardController {

	public static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private FileHandler fileHandler;
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/list") // http://localhost:8080/board/list
	public String viewBoardList(Model model) {
		BoardListVO boardListVO = this.boardService.getAllBoard();
		model.addAttribute("boardListVO", boardListVO);
		return "board/boardlist";
	}
	
	@GetMapping("/board/write") // http://localhost:8080/board/write
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}
		
//	@PostMapping("/board/write")
//	public String doSomething(WriteBoardVO writeBoardVO) {
//		System.out.println("제목" + writeBoardVO.getSubject());
//		System.out.println("이메일" + writeBoardVO.getEmail());
//		System.out.println("내용" + writeBoardVO.getContent());
//		return "";
//	}
	
	@PostMapping("/board/write")
	public String doCreatNewBoard(@Valid WriteBoardVO writeBoardVO // @Valid WriteBoardVO의 Validation Check 수행
								, BindingResult bindingResult // @Valid의 실패 결과만 할당
								, Model model
								, @SessionAttribute(value = "_LOGIN_USER_", required = false) MemberVO loginMemberVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		writeBoardVO.setIp( request.getRemoteAddr() );
		
		if(bindingResult.hasErrors()) { // 에러가 존재한다면
			model.addAttribute("writeBoardVO", writeBoardVO);
			return "board/boardwrite";
		}
		
		// MemberVO memberVO = (MemberVO)session.getAttribute("_LOGIN_USER_"); @SessionAttribute로 대체

		if (loginMemberVO == null) {
			return "redirect:/member/login";
		}
		// session 에서 가져온 email
		writeBoardVO.setEmail( loginMemberVO.getEmail() );
		
		boolean isCreate = this.boardService.creatNewBoard(writeBoardVO);
		if (logger.isDebugEnabled()) {
			logger.debug("게시글 등록 결과 : " + isCreate);
		}
		
		return "redirect:/board/list";
	}
	
	@GetMapping("/board/view") // http://localhost:8080/board/view?id=?
	public String viewOneBoard(Model model, @RequestParam int id) {
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		model.addAttribute("boardVO", boardVO);
		return "board/boardview";
	}
	
	@GetMapping("/board/modify/{id}") // http://localhost:8080/board/modify/?
	public String viewBoardModifyPage(Model model
									, @PathVariable int id
									, @SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO) {
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		if (!boardVO.getEmail().equals(loginMemberVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		model.addAttribute("boardVO", boardVO);
		return "board/boardmodify";
	}
	
	@PostMapping("/board/modify/{id}")
	public String doModifyOneBoard(@PathVariable int id 
									, @Valid ModifyBoardVO modifyBoardVO
									, BindingResult bindingResult
									, Model model
									, @SessionAttribute(value = "_LOGIN_USER_", required = false) MemberVO loginMemberVO) {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		modifyBoardVO.setIp(request.getRemoteAddr());
		
		modifyBoardVO.setId(id); // 쿼리에서 아이디를 불러오기 위해 파라미터로 받아온 id를 넣어줘야 함
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
		
		if (loginMemberVO == null) {
			return "redirect:/member/login";
		}
		
		modifyBoardVO.setEmail( loginMemberVO.getEmail() );
		
		boolean isUpdated = this.boardService.updateOneBoard(modifyBoardVO);
		if (isUpdated) {
			return "redirect:/board/view?id=" + id;
		}
		else {
			// 사용자가 작성했던 내용을 JSP에 그대로 보내줌
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
	}
	
	@GetMapping("/board/delete/{id}")
	public String doDeleteOneBoard(@PathVariable int id
								, @SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO) {
		
		DeleteBoardVO deleteBoardVO = new DeleteBoardVO();
		deleteBoardVO.setId(id);
		deleteBoardVO.setEmail(loginMemberVO.getEmail());
		
		boolean isDeleted = this.boardService.deleteOneBoard(deleteBoardVO);
		if (isDeleted) {
			return "redirect:/board/list";
		}
		else {
			// 삭제에 실패했다면 조회페이지로 이동
			return "redirect:/board/view?id=" + id;
		}
	}
	
	@GetMapping("/board/file/download/{id}") // import org.springframework.core.io.Resource;
	public ResponseEntity<Resource> doDownloadFile(@PathVariable int id) {
		// 1. 다운로드 할 파일의 이름을 알기 위해 게시글을 조회
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		return this.fileHandler.downloadFile(boardVO.getFileName(), boardVO.getOriginFileName());
	}
	
	@GetMapping("/board/excel/download")
	public ResponseEntity<Resource> doDownloadExcel() {
		
		// 1. Workbook (엑셀 워크시트) 생성
		// -1은 메모리를 무제한을 쓰겠다는 의미
		Workbook workbook = new SXSSFWorkbook(-1); // .xlsx 포멧의 워크북 생성
		
		// 2. Workbook 에 Sheet 만들기
		// import org.apache.poi.ss.usermodel.Sheet; xlsx 포멧
		// 줄도 없고 칸도 없는 상태
		Sheet sheet = workbook.createSheet("게시글 목록");
		
		// 3. Sheet 에 Row 만들기
		// 한 줄(Row)을 만듬
		Row row = sheet.createRow(0);
		
		// 4. Row 에 Cell 만들기
		// 한 칸을 만들어냄
		Cell cell = row.createCell(0);
		cell.setCellValue("번호");
		cell = row.createCell(1);
		cell.setCellValue("제목");
		cell = row.createCell(2);
		cell.setCellValue("첨부파일명");
		cell = row.createCell(3);
		cell.setCellValue("작성자 이메일");
		cell = row.createCell(4);
		cell.setCellValue("조회수");
		cell = row.createCell(5);
		cell.setCellValue("등록일");
		cell = row.createCell(6);
		cell.setCellValue("수정일");
		
		BoardListVO boardListVO = this.boardService.getAllBoard();
		List<BoardVO> boardList = boardListVO.getBoardList();
		int rowIndex = 1;
		for (BoardVO boardVO : boardList) {
			// Sheet 에 Row 만들기
			row = sheet.createRow(rowIndex);
			// Row 에 Cell 만들기
			cell = row.createCell(0);
			cell.setCellValue("" + boardVO.getId()); // id: 112 ==> 112.0
			cell = row.createCell(1);
			cell.setCellValue(boardVO.getContent());
			cell = row.createCell(2);
			cell.setCellValue(boardVO.getOriginFileName() == null ? "" : boardVO.getOriginFileName());
			cell = row.createCell(3);
			cell.setCellValue(boardVO.getEmail());
			cell = row.createCell(4);
			cell.setCellValue("" + boardVO.getViewCnt());
			cell = row.createCell(5);
			cell.setCellValue(boardVO.getCrtDt());
			cell = row.createCell(6);
			cell.setCellValue(boardVO.getMdfyDt());
			
			rowIndex += 1;
		}
		
		// 5. Workbook 을 File 로 생성
		String excelFileName = this.fileHandler.createXlsxFile(workbook);
		
		// 6. File 다운로드
		
		return this.fileHandler.downloadFile(excelFileName, "게시글 목록.xlsx");
	}
	
}
