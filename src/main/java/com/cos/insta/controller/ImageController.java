package com.cos.insta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageController {
	
	@GetMapping("/image/feed")
	public String authFeed() {
		return "image/feed";
	}
}
