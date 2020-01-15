package com.softwarelma.ers_boot;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * see https://spring.io/guides/gs/rest-service-cors/
 */
@RestController
public class NaeController {

	private static final String CORS_ORIGIN = "http://localhost:4200";

	public NaeController() {
	}

	@CrossOrigin(origins = CORS_ORIGIN)
	@GetMapping(path = "/rest/echo/{text}", produces = "application/json")
	public NaeEcho echo(HttpServletResponse httpServletResponse, @PathVariable String text) {
		NaeEcho echo = new NaeEcho();
		echo.setText(text);
		return echo;
	}

	@CrossOrigin(origins = CORS_ORIGIN)
	@PostMapping(path = "/rest/postDocsAndGetTexts", consumes = "application/json", produces = "application/json")
	public NaeListFile postDocsAndGetTexts(HttpServletResponse httpServletResponse, @RequestBody NaeListFile listFileReq) {
		// this.addHeaders(httpServletResponse);
		NaeDocPojo docPojo = new NaeDocPojo();
		NaeListFile listFileResp = docPojo.postDocsAndGetTexts(listFileReq);
		return listFileResp;
	}

	@CrossOrigin(origins = CORS_ORIGIN)
	@PostMapping(path = "/rest/postTextsAndGetAnnotations", consumes = "application/json", produces = "application/json")
	public NaeListFile postTextsAndGetAnnotations(HttpServletResponse httpServletResponse, @RequestBody NaeListFile listFileReq) {
		// this.addHeaders(httpServletResponse);
		NaeDocPojo docPojo = new NaeDocPojo();
		NaeListFile listFileResp = docPojo.postTextsAndGetAnnotations(listFileReq);
		return listFileResp;
	}

	@CrossOrigin(origins = CORS_ORIGIN)
	@PostMapping(path = "/rest/postAnnotationsAndGetDocs", consumes = "application/json", produces = "application/json")
	public NaeListFile postAnnotationsAndGetDocs(HttpServletResponse httpServletResponse, @RequestBody NaeListFile listFileReq) {
		// this.addHeaders(httpServletResponse);
		NaeDocPojo docPojo = new NaeDocPojo();
		NaeListFile listFileResp = docPojo.postAnnotationsAndGetDocs(listFileReq);
		return listFileResp;
	}

}