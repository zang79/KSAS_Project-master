package KJcompany.KSAS.common.service;

import java.util.List;
import KJcompany.KSAS.common.domain.PerformanceLog;

public interface PerformanceLogService {

	public void register(PerformanceLog performanceLog) throws Exception;
	
	public List<PerformanceLog> list() throws Exception;

}
