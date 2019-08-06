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
@Table(name = "news_subscription")
public class NewsSubscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "person_id", nullable= false)
	private Person person;
	
	@Column(length = 50,name="news_about")
	@JsonProperty
	private String newsAbout;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getNewsAbout() {
		return newsAbout;
	}

	public void setNewsAbout(String newsAbout) {
		this.newsAbout = newsAbout;
	}
	
}
