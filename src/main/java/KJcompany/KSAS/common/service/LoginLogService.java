package KJcompany.KSAS.common.service;

import java.util.List;

import KJcompany.KSAS.common.domain.LoginLog;

public interface LoginLogService {

	public void register(LoginLog loginLog) throws Exception;

	public List<LoginLog> list() throws Exception;

}
