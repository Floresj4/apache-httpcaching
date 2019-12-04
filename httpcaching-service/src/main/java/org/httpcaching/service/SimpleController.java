package org.httpcaching.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class SimpleController {

	@GetMapping(value = "/some/resource", produces = "application/octet-stream")
	public InputStreamResource getSomeResource(HttpServletResponse response) throws FileNotFoundException {
		response.setContentType("application/jpeg");
		response.setHeader("Content-Disposition", "attachment; filename=\"java.jpg\"");
		
		InputStreamResource resource = new InputStreamResource(
				new FileInputStream("./src/main/resources/java.jpg"));

		return resource;
	}
}
