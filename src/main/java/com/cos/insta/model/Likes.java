package com.cos.insta.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes {

	@Id // id를 기본키로 쓰겠다.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성전략을 identity로.
	private int id; // 시퀀스

	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties({"images", "password", "name", "website", "bio", "email", "phone", "gender","createDate", "updateDate"})
	private User user; // id, username, profileImage

	@ManyToOne
	@JoinColumn(name = "imageId")
	@JsonIgnoreProperties({"tags", "user", "likes"})
	private Image image; // 기본 : image_id

	@CreationTimestamp
	private Timestamp createDate;
	@UpdateTimestamp
	private Timestamp updateDate;
}
