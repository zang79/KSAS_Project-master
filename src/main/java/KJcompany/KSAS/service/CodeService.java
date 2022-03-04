package KJcompany.KSAS.service;

import java.util.List;

import KJcompany.KSAS.dto.CodeLabelValue;

public interface CodeService {

	public List<CodeLabelValue> getGroupCodeList() throws Exception;
	
	public List<CodeLabelValue> getCodeList(String groupCode) throws Exception;
	
}
