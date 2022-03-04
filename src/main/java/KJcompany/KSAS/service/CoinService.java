package KJcompany.KSAS.service;

import java.util.List;

import KJcompany.KSAS.domain.ChargeCoin;
import KJcompany.KSAS.domain.PayCoin;

public interface CoinService {

	public void charge(ChargeCoin chargeCoin) throws Exception;

	public List<ChargeCoin> list(Long userNo) throws Exception;
	
	public List<PayCoin> listPayHistory(Long userNo) throws Exception;
	
}
