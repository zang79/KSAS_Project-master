package KJcompany.KSAS.common.service;

import java.util.List;

import KJcompany.KSAS.common.domain.AccessLog;

public interface AccessLogService {

	public void register(AccessLog accessLog) throws Exception;
	
	public List<AccessLog> list() throws Exception;

}
