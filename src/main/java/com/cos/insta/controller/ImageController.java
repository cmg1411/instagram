package com.cos.insta.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cos.insta.model.Comment;
import com.cos.insta.model.Image;
import com.cos.insta.model.Likes;
import com.cos.insta.model.Tag;
import com.cos.insta.model.User;
import com.cos.insta.model.commentMsg;
import com.cos.insta.repository.CommentRepository;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.LikesRepository;
import com.cos.insta.repository.TagRepository;
import com.cos.insta.service.MyUserDetails;
import com.cos.insta.util.Utils;

@Controller
public class ImageController {
	
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private ImageRepository mImageRepository;
	
	@Autowired
	private TagRepository mTagRepository;
	
	@Autowired
	private LikesRepository mLikesRepository;

	@Autowired
	private CommentRepository mCommentRepository;
	
	@Value("${file.path}")
	private String fileRealPath;

	@GetMapping("/image/explore")
	public String imageExplore(Model model,
			@PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

		// 알고리즘 ( 내 주변에서 좋아요가 가장 많은 순으로 해보는 것 추천)
		Page<Image> pImages = mImageRepository.findAll(pageable);
		List<Image> images = pImages.getContent();

		// 4번 likeCount
		for (Image item : images) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}

		model.addAttribute("images", images);
		return "image/explore";
	}
	
	@PostMapping("/image/like/{id}")
	public @ResponseBody String imageLike(@PathVariable int id,
										  @AuthenticationPrincipal MyUserDetails userDetail) {
		Likes oldLike = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), id);
		
		Optional<Image> oImage = mImageRepository.findById(id);
		Image image = oImage.get();
		
		try {
			if(oldLike == null) { //좋아요 안했음.(추가하기)
				Likes newLike = Likes.builder()
								.image(image)
						        .user(userDetail.getUser())
						        .build();
				
				mLikesRepository.save(newLike);
				
				return "like";
			}else { // 좋아요 한 상태.(삭제하기)
				mLikesRepository.delete(oldLike);
				return "unlike";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "fail";
	}
	
	@GetMapping({"/","/image/feed"})
	public String authFeed(@AuthenticationPrincipal MyUserDetails userDetail,
						   @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		//log.info("username:"+userDetail.getUsername());
		
		//1번 내가 팔로우한 친구들의 사진
		Page<Image> pageImages = mImageRepository.findImage(userDetail.getUser().getId(), pageable);
		
		List<Image> images = pageImages.getContent();
		
		for(Image image : images) {
			Likes like = mLikesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			
			if(like!=null) {
				image.setHeart(true);
			}
		}
		
		// 4번 likeCount
		for (Image item : images) {
			int likeCount = mLikesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);
		}
		
		model.addAttribute("images", images);
		return "image/feed";
	}
	
	@GetMapping("/image/upload")
	public String imageUpload() {
		return "image/image_upload";
	}
	
	@GetMapping("/image/feed/scroll")
	public @ResponseBody List<Image> imageFeedScroll(@AuthenticationPrincipal MyUserDetails userDetail,
		@PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		
		//내가 팔로우 한 친구들의 사진
		Page<Image> pageImages = mImageRepository.findImage(userDetail.getUser().getId(), pageable);
		
		List<Image> images = pageImages.getContent();
		return images;
	}
	
	@PostMapping("/image/uploadProc")
	public String imageUploadProc(
		@AuthenticationPrincipal MyUserDetails userDetail,
		@RequestParam("file") MultipartFile file,
		@RequestParam("caption") String caption,
		@RequestParam("location") String location,
		@RequestParam("tags") String tags
		
	) {
		//이미지 업로드 수행
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid+"_"+file.getOriginalFilename();
		
		Path filePath = Paths.get(fileRealPath+uuidFilename);
		
		try {
			//하드디스크 기록
			Files.write(filePath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		User principal = userDetail.getUser();
		
		Image image = new Image();
		image.setLocation(location);
		image.setCaption(caption);
		image.setUser(principal);
		image.setPostImage(uuidFilename);
		
		//<img src="/upload/파일명" />
		
		mImageRepository.save(image);
		
		//tag 객체 생성 집어넣기
		List<String> tagList = Utils.tagPaser(tags);
		
		for(String tag:tagList) {
			Tag t = new Tag();
			t.setName(tag);
			t.setImage(image);
			mTagRepository.save(t);
			image.getTags().add(t);
		}
			
		return "redirect:/";
	}
	
	@PostMapping("/image/commentProc")
	public @ResponseBody String commentProc(@AuthenticationPrincipal MyUserDetails userDetail,
											@RequestBody commentMsg comm) {
			System.out.println(comm.getMsg());
			System.out.println(comm.getId());

			Optional<Image> img = mImageRepository.findById(comm.getId());
			Image img1 = img.get();
			
			Comment com = new Comment();
			
			com.setApply(comm.getMsg());
			com.setImage(img1);
			com.setFromUser(userDetail.getUser());
			
			mCommentRepository.save(com);
			
		return userDetail.getUsername();
	}
}
