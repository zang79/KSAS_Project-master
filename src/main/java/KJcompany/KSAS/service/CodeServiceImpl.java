package KJcompany.KSAS.service;

import java.util.ArrayList;
import java.util.List;

import KJcompany.KSAS.domain.CodeDetail;
import KJcompany.KSAS.domain.CodeGroup;
import KJcompany.KSAS.dto.CodeLabelValue;
import KJcompany.KSAS.repository.CodeDetailRepository;
import KJcompany.KSAS.repository.CodeGroupRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CodeServiceImpl implements CodeService {

	private final CodeGroupRepository repository;

	private final CodeDetailRepository codeDetailRepository;

	@Override
	public List<CodeLabelValue> getGroupCodeList() throws Exception {
		List<CodeGroup> codeGroups = repository.findAll(Sort.by(Direction.ASC, "groupCode"));
		
		List<CodeLabelValue> codeGroupList = new ArrayList<CodeLabelValue>();
		
		for(CodeGroup codeGroup : codeGroups) {
			codeGroupList.add(new CodeLabelValue(codeGroup.getGroupCode(), codeGroup.getGroupName()));
		}
		
		return codeGroupList;
	}
	
	@Override
	public List<CodeLabelValue> getCodeList(String groupCode) throws Exception {
		List<CodeDetail> codeDetails = codeDetailRepository.getCodeList(groupCode);
		
		List<CodeLabelValue> codeList = new ArrayList<CodeLabelValue>();
		
		for(CodeDetail codeDetail : codeDetails) {
			codeList.add(new CodeLabelValue(codeDetail.getCodeValue(), codeDetail.getCodeName()));
		}
		
		return codeList;
	}
	
}
