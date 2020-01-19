package com.softwarelma.ers_boot;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softwarelma.ers_boot.dto.NaeEchoDto;
import com.softwarelma.ers_boot.dto.NaeListFileDto;
import com.softwarelma.ers_boot.pojo.NaeDocPojo;

/**
 * see https://spring.io/guides/gs/rest-service-cors/
 */
@RestController
public class NaeController {

	private static final String CORS_ORIGIN_EN = "http://localhost:4200";
	private static final String CORS_ORIGIN_IT = "http://localhost:4201";
	private static final String CORS_ORIGIN_ES = "http://localhost:4202";

	public NaeController() {
	}

	@CrossOrigin(origins = { CORS_ORIGIN_EN, CORS_ORIGIN_IT, CORS_ORIGIN_ES })
	@GetMapping(path = "/rest/echo/{text}", produces = "application/json")
	public NaeEchoDto echo(HttpServletResponse httpServletResponse, @PathVariable String text) {
		NaeEchoDto echo = new NaeEchoDto();
		echo.setText(text);
		return echo;
	}

	@CrossOrigin(origins = { CORS_ORIGIN_EN, CORS_ORIGIN_IT, CORS_ORIGIN_ES })
	@PostMapping(path = "/rest/postDocsAndGetTexts", consumes = "application/json", produces = "application/json")
	public NaeListFileDto postDocsAndGetTexts(HttpServletResponse httpServletResponse, @RequestBody NaeListFileDto listFileReq) {
		// this.addHeaders(httpServletResponse);
		NaeDocPojo docPojo = new NaeDocPojo();
		NaeListFileDto listFileResp = docPojo.postDocsAndGetTexts(listFileReq);
		return listFileResp;
	}

	@CrossOrigin(origins = { CORS_ORIGIN_EN, CORS_ORIGIN_IT, CORS_ORIGIN_ES })
	@PostMapping(path = "/rest/postTextsAndGetAnnotations", consumes = "application/json", produces = "application/json")
	public NaeListFileDto postTextsAndGetAnnotations(HttpServletResponse httpServletResponse, @RequestBody NaeListFileDto listFileReq) {
		// this.addHeaders(httpServletResponse);
		NaeDocPojo docPojo = new NaeDocPojo();
		NaeListFileDto listFileResp = docPojo.postTextsAndGetAnnotations(listFileReq);
		return listFileResp;
	}

	@CrossOrigin(origins = { CORS_ORIGIN_EN, CORS_ORIGIN_IT, CORS_ORIGIN_ES })
	@PostMapping(path = "/rest/postAnnotationsAndGetDocs", consumes = "application/json", produces = "application/json")
	public NaeListFileDto postAnnotationsAndGetDocs(HttpServletResponse httpServletResponse, @RequestBody NaeListFileDto listFileReq) {
		// this.addHeaders(httpServletResponse);
		NaeDocPojo docPojo = new NaeDocPojo();
		NaeListFileDto listFileResp = docPojo.postAnnotationsAndGetDocs(listFileReq);
		return listFileResp;
	}

}