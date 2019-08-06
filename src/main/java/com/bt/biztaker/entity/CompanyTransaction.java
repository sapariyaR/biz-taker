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
@Table(name = "company_transaction")
public class CompanyTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@ManyToOne
	@JsonProperty
    @JoinColumn(name = "company_id", nullable= false)
	private Company company;
	
	@ManyToOne
	@JsonProperty
    @JoinColumn(name = "trns_owner_id", nullable= false)
	private Person transactionOwner;
	
	@Column(length = 5,name="transaction_type")
	@JsonProperty
	private TransactionType transactionType;
	
	@Column(length = 100,name="amount",nullable=false)
	@JsonProperty
	private Double amount;
	
	@Column(length = 50,name="created_date",nullable=false)
	@JsonProperty
	private Long createdDate;
	
	@Column(length = 50,name="updated_date",nullable=false)
	@JsonProperty
	private Long updatedDate;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Person getTransactionOwner() {
		return transactionOwner;
	}

	public void setTransactionOwner(Person transactionOwner) {
		this.transactionOwner = transactionOwner;
	}
	
}
