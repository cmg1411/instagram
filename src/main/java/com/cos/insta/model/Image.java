package com.cos.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//tag검색 만들지 않음.
@Data
@Entity
public class Image {

	@Id // id를 기본키로 쓰겠다.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성전략을 identity로.
	private int id; // 시퀀스
	private String location; // 사진 찍은 위치
	private String caption; // 사진 설명
	private String postImage; // 사진 파일 이름 + 경로

	@ManyToOne // M:1관계
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties({ "password", "images" })
	private User user;

	// (1) Tag List
	@OneToMany(mappedBy="image")
	@JsonManagedReference
	private List<Tag> tags = new ArrayList<>();
	
	// (2) Like List
	@OneToMany(mappedBy="image")
	private List<Likes> likes = new ArrayList<>();

	@Transient // DB에 영향을 미치지 않는다. //serialize 과정에서 무시
	private int likeCount;

	@Transient
	private boolean heart;
	
	@CreationTimestamp
	private Timestamp createDate;
	@UpdateTimestamp
	private Timestamp updateDate;
}
