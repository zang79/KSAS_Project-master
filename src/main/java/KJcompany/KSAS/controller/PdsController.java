package KJcompany.KSAS.controller;

import java.io.File;
import java.util.List;

import KJcompany.KSAS.common.util.UploadFileUtils;
import KJcompany.KSAS.domain.Pds;
import KJcompany.KSAS.prop.ShopProperties;
import KJcompany.KSAS.service.PdsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pds")
public class PdsController {

	private final PdsService pdsService;

	private final ShopProperties shopProperties;

	@GetMapping("/list")
	public void list(Model model) throws Exception {
		List<Pds> itemList = this.pdsService.list();

		model.addAttribute("itemList", itemList);
	}

	@GetMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public String registerForm(Model model) {
		model.addAttribute(new Pds());

		return "pds/register";
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public String register(@Validated Pds pds, BindingResult result, RedirectAttributes rttr) throws Exception {
		if(result.hasErrors()) {
			return "pds/register";
		}
		
		this.pdsService.register(pds);

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/pds/list";
	}

	@GetMapping("/read")
	public String read(Long itemId, Model model) throws Exception {
		Pds pds = this.pdsService.read(itemId);

		model.addAttribute(pds);

		return "pds/read";
	}

	@GetMapping("/modify")
	@PreAuthorize("hasRole('ADMIN')")
	public String modifyForm(Long itemId, Model model) throws Exception {
		Pds pds = this.pdsService.read(itemId);

		model.addAttribute(pds);

		return "pds/modify";
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('ADMIN')")
	public String modify(@Validated Pds pds, BindingResult result, RedirectAttributes rttr) throws Exception {
		if(result.hasErrors()) {
			return "pds/modify";
		}
		
		this.pdsService.modify(pds);

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/pds/list";
	}

	@GetMapping("/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String removeForm(Long itemId, Model model) throws Exception {
		Pds pds = this.pdsService.read(itemId);

		model.addAttribute(pds);

		return "pds/remove";
	}

	@PostMapping("/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String remove(Pds pds, RedirectAttributes rttr) throws Exception {
		this.pdsService.remove(pds.getItemId());

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/pds/list";
	}

	@ResponseBody
	@PostMapping(value = "/uploadAjax", produces = "text/plain;charset=UTF-8")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {
		String savedName = UploadFileUtils.uploadFile(shopProperties.getUploadPath(), file.getOriginalFilename(), file.getBytes());

		return new ResponseEntity<String>(savedName, HttpStatus.CREATED);
	}

	@ResponseBody
	@GetMapping("/downloadFile")
	public ResponseEntity<byte[]> downloadFile(String fullName) throws Exception {
		ResponseEntity<byte[]> entity = null;

		pdsService.updateAttachDownCnt(fullName);

		try {
			HttpHeaders headers = new HttpHeaders();

			File file = new File(shopProperties.getUploadPath() + fullName);

			String fileName = fullName.substring(fullName.indexOf("_") + 1);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

			entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}

	@ResponseBody
	@PostMapping("/deleteFile")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteFile(String fileName) {
		new File(shopProperties.getUploadPath() + fileName.replace('/', File.separatorChar)).delete();

		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/getAttach/{itemId}")
	public List<String> getAttach(@PathVariable("itemId") Long itemId) throws Exception {
		return pdsService.getAttach(itemId);
	}
	
}
