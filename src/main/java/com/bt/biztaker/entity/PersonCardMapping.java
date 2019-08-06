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
@Table(name = "person_card_mapping")
public class PersonCardMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "source_owner_id" ,nullable= false)
	@JsonProperty
	private Person sourceOwner;
	
	@ManyToOne
	@JoinColumn(name = "destination_owner_id")
	@JsonProperty
	private Person destinationOwner;
	
	@ManyToOne
	@JoinColumn(name = "card_id",nullable= false)
	@JsonProperty
	private Card card;
	
	@Column(length = 100,name="latitude")
	@JsonProperty
	private Double latitude;
	
	@Column(length = 100,name="longitude")
	@JsonProperty
	private Double longitude;
	
	@Column(length = 50,name="created_date",nullable=false)
	@JsonProperty
	private Long createdDate;
	
	@Column(length = 100,name="card_nick_name")
	private String cardNickName;
	
	@Column(length = 5,name="sharing_type",nullable=false)
	@JsonProperty
	private SharingType sharingType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getSourceOwner() {
		return sourceOwner;
	}

	public void setSourceOwner(Person sourceOwner) {
		this.sourceOwner = sourceOwner;
	}

	public Person getDestinationOwner() {
		return destinationOwner;
	}

	public void setDestinationOwner(Person destinationOwner) {
		this.destinationOwner = destinationOwner;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getCardNickName() {
		return cardNickName;
	}

	public void setCardNickName(String cardNickName) {
		this.cardNickName = cardNickName;
	}

	public SharingType getSharingType() {
		return sharingType;
	}

	public void setSharingType(SharingType sharingType) {
		this.sharingType = sharingType;
	}
	
}
