package org.httpcaching.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class SimpleController {

	@GetMapping(value = "/some/resource", produces = "application/octet-stream")
	public InputStreamResource getSomeResource(HttpServletResponse response) throws FileNotFoundException {
		//set the response content type and important headers
		response.setContentType("application/jpeg");
		response.addHeader("Content-Disposition", "attachment; filename=\"java.jpg\"");
		response.addHeader("Cache-Control", CacheControl.maxAge(10, TimeUnit.MINUTES)
				.getHeaderValue());

		return new InputStreamResource(new FileInputStream("./src/main/resources/java.jpg"));
	}
}
