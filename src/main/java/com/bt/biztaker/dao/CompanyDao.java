package com.bt.biztaker.dao;

import org.hibernate.SessionFactory;

import com.bt.biztaker.entity.Company;

import io.dropwizard.hibernate.AbstractDAO;

public class CompanyDao extends AbstractDAO<Company> {

	public CompanyDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public Company saveOrUpdateCompany(Company company) {
		return persist(company);
	}

}
