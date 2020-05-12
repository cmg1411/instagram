package com.cos.insta.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cos.insta.model.Image;
import com.cos.insta.model.User;
import com.cos.insta.repository.FollowRepository;
import com.cos.insta.repository.LikesRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.service.MyUserDetails;

@Controller
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired // 자동으로 알맞은 Bean을 주입해준다.
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserRepository mUserRepository;
	
	@Autowired
	private FollowRepository mFollowRepository;
	
	@Autowired
	private LikesRepository mLikesRepository; 
	
	@Value("${file.path}")
	private String fileRealPath;
	
	@GetMapping("/auth/login")
	public String authLogin() {
		return "auth/login";
	}

	@GetMapping("/auth/join") //get방식 mapping
	public String authJoin() {
		return "auth/join";
	}

	@PostMapping("/auth/joinProc") //post방식 mapping
	public String authJoinProc(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		log.info("rawPassword : " + rawPassword);
		log.info("encPassword : " + encPassword);

		mUserRepository.save(user);

		return "redirect:/auth/login";
	}
	
	@GetMapping("/user/{id}")
	public String profile(@PathVariable int id,
						  @AuthenticationPrincipal MyUserDetails userDetail,
						  Model model) {
		
		//id를 통해 해당 유저를 검색(이미지 + 유저정보)
		/*
		 * 1.imageCount 
		 * 2.followerCount 
		 * 3.followingCount 
		 * 4.User 오브젝트(Image(likeCount)컬렉션)
		 * 5. followCheck 팔로우 유무(1이면 팔로우 1아니면 언팔)
		 */
		
		Optional<User> oToUser = mUserRepository.findById(id);
		User user = oToUser.get();
		
		//1.
		int imageCount = user.getImages().size();
		model.addAttribute("imageCount",imageCount);
		
		//2.
		int followCount = mFollowRepository.countByFromUserId(user.getId());
		model.addAttribute("followCount", followCount);
		
		//3.
		int followerCount = mFollowRepository.countByToUserId(user.getId());
		model.addAttribute("followerCount", followerCount);
		
		//4.likeCount
		for(Image item : user.getImages()) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}
		
		model.addAttribute("user",user);
		
		//5.
		User principal = userDetail.getUser();
		
		int followCheck = mFollowRepository.countByFromUserIdAndToUserId(principal.getId(), id);
		log.info("followCheck : " + followCheck);
		model.addAttribute("followCheck", followCheck);
		
		return "user/profile";
	}
	
	
	//프로필수정
	
	@PostMapping("/user/profileUpload")
	public String userProfileUpload(@RequestParam("profileImage") MultipartFile file,
									@AuthenticationPrincipal MyUserDetails userDetail) throws IOException{
		User principal = userDetail.getUser();
		
		//파일처리
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(fileRealPath+uuidFilename);
		Files.write(filePath, file.getBytes());
		
		//영속화
		Optional<User> oUser = mUserRepository.findById(principal.getId());
		User user = oUser.get();
		
		//값 변경
		user.setProfileImage(uuidFilename);
		
		//다시 영속화 및 저장
		mUserRepository.save(user);
		return "redirect:/user/"+principal.getId();
		
	}
	
	@GetMapping("/user/edit")
	public String userEdit(@AuthenticationPrincipal MyUserDetails userDetail, 
						   Model model) {
		
		Optional<User> oUser = mUserRepository.findById(userDetail.getUser().getId());
		User user = oUser.get();
		model.addAttribute("user",user);
		
		return "user/profile_edit";
	}
	
	//@PutMapping("/user/editProc") 왜안대는지 모르겠음
	@GetMapping("/user/editProc")
	public String userEditProc(
			User requestUser,
			@AuthenticationPrincipal MyUserDetails userDetail) {
		
		// 영속화
		Optional<User> oUser = mUserRepository.findById(userDetail.getUser().getId());
		User user = oUser.get();
		
		// 값 변경
		user.setName(requestUser.getName());
		user.setUsername(requestUser.getUsername());
		user.setWebsite(requestUser.getWebsite());
		user.setBio(requestUser.getBio());
		user.setEmail(requestUser.getEmail());
		user.setPhone(requestUser.getPhone());
		user.setGender(requestUser.getGender());
		
		// 다시 영속화 및 flush
		mUserRepository.save(user);
		
		return "redirect:/user/"+userDetail.getUser().getId();
	}
}
