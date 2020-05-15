package com.cos.insta.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String apply;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="imageId")
	@JsonBackReference
	private Image image;
	
	@ManyToOne
	@JoinColumn(name="fromUserId")
	@JsonIgnoreProperties({"images"})
	private User fromUser;
	
	@ManyToOne
	@JoinColumn(name="toUserId")
	@JsonIgnoreProperties({"images"})
	private User toUser;
	
	@CreationTimestamp
	private Timestamp createDate;
}
