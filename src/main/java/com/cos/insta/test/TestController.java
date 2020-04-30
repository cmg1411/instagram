package com.cos.insta.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	@GetMapping("/test/home")
	public String testHome() {
		return "home";
	}
}
