package KJcompany.KSAS.service;

import KJcompany.KSAS.domain.Board;
import KJcompany.KSAS.repository.BoardRepository;
import KJcompany.KSAS.vo.PageRequestVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardRepository repository;

	@Override
	public void register(Board board) throws Exception {
		repository.save(board);
	}

	@Transactional
	public Board read(Long boardNo) throws Exception {
		return repository.getOne(boardNo);
	}

	@Override
	public void modify(Board board) throws Exception {
		Board boardEntity = repository.getOne(board.getBoardNo());

		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());

		repository.save(boardEntity);
	}

	@Override
	public void remove(Long boardNo) throws Exception {
		repository.deleteById(boardNo);
	}

	@Override
	public Page<Board> list(PageRequestVO pageRequestVO) throws Exception {
		String searchType = pageRequestVO.getSearchType();
		String keyword = pageRequestVO.getKeyword();

		int pageNumber = pageRequestVO.getPage() - 1;
		int sizePerPage = pageRequestVO.getSizePerPage();

		Pageable pageRequest = PageRequest.of(pageNumber, sizePerPage, Sort.Direction.DESC, "boardNo");

		return repository.getSearchPage(searchType, keyword, pageRequest);
	}

	@Override
	public void views(Board board){
		repository.save(board);
	}


}