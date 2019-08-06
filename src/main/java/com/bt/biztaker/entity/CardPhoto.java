package com.bt.biztaker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "card_photo")
public class CardPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@Column(length = 5,name="photo_side")
	@JsonProperty
	private PhotoSide photoSide;
	
	@Column(length = 100,name="photo_url")
	@JsonProperty
	private String photoUrl;
	
	@Column(length = 100,name="photo_title")
	@JsonProperty
	private String photoTitle;
	
	@ManyToOne
    @JoinColumn(name = "card_id", nullable= false)
	private Card card;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PhotoSide getPhotoSide() {
		return photoSide;
	}

	public void setPhotoSide(PhotoSide photoSide) {
		this.photoSide = photoSide;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhotoTitle() {
		return photoTitle;
	}

	public void setPhotoTitle(String photoTitle) {
		this.photoTitle = photoTitle;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	
}
