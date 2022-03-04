package KJcompany.KSAS.service;

import java.util.List;

import KJcompany.KSAS.domain.CodeDetail;

public interface CodeDetailService {

	public void register(CodeDetail codeDetail) throws Exception;

	public CodeDetail read(CodeDetail codeDetail) throws Exception;

	public void modify(CodeDetail codeDetail) throws Exception;

	public void remove(CodeDetail codeDetail) throws Exception;

	public List<CodeDetail> list() throws Exception;
	
}
