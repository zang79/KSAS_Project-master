package KJcompany.KSAS.service;

import java.util.List;

import KJcompany.KSAS.domain.Item;
import KJcompany.KSAS.domain.Member;
import KJcompany.KSAS.domain.UserItem;

public interface UserItemService {

	public void register(Member member, Item item) throws Exception;

	public UserItem read(Long userItemNo) throws Exception;

	public List<UserItem> list(Long userNo) throws Exception;
	
}
