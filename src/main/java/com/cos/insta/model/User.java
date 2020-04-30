package com.cos.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data // 롬복
@Entity // JPA의 엔티티로 쓰겠다
@Setter
@Getter
public class User {
	
	@Id //id를 기본키로 쓰겠다.
	@GeneratedValue(strategy = GenerationType.IDENTITY)//기본키 생성전략을 identity로.
	private int id; //시퀀스
	private String username; // 사용자아이디
	private String password; // 암호화된 패스워드
	private String name; // 사용자 이름
	private String website; // 홈페이지 주소
	private String bio; // 자기소개
	private String email; // 이메일
	private String phone;
	private String gender;
	private String profileImage; // 프로필 사진 경로 + 이름
	
	//(1)findById() 일떄만 동작
	//(2)findByUserInfo() 제외
	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties({"user", "tags", "likes"})
	private List<Image> images = new ArrayList<>();
	
	@CreationTimestamp //자동으로 현재 시간이 셋팅.
	private Timestamp createDate;
	@UpdateTimestamp
	private Timestamp updateDate;
}
