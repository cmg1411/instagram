package com.cos.insta.controller;

import org.slf4j.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.insta.service.MyUserDetails;

@Controller
public class ImageController {
	
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);

	
	@GetMapping({"/","/image/feed"})
	public String authFeed(@AuthenticationPrincipal MyUserDetails userDetail) {
		log.info("username:"+userDetail.getUsername());
		return "image/feed";
	}
}
