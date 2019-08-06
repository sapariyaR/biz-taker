package com.bt.biztaker.dao;


import java.util.List;

import javax.persistence.Query;

import org.hibernate.SessionFactory;

import com.bt.biztaker.entity.User;

import io.dropwizard.hibernate.AbstractDAO;

@SuppressWarnings("unchecked")
public class UserDao extends AbstractDAO<User> {

	public UserDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public User saveOrUpdateUser(User user) {
		return persist(user);
	}
	
	public User getUserByEmailId(String emailId) {
		String queryString = "SELECT u.id, u.email, u.password, u.role, u.isEnable, u.createdDate, u.updatedDate FROM User u WHERE u.email = :email";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("email", emailId);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object[] eachResult : resultList) {
				return fillUser(eachResult);
			}
		}
		return null;
	}

	private User fillUser(Object[] eachResult) {
		User user = new User();
		if(eachResult[0] != null) {
			user.setId(Long.parseLong(eachResult[0].toString()));
		}
		user.setEmail(eachResult[1].toString());
		user.setPassword(eachResult[2].toString());
		user.setRole(eachResult[3].toString());
		user.setIsEnable(Boolean.parseBoolean(eachResult[4].toString()));
		user.setCreatedDate(Long.parseLong(eachResult[5].toString()));
		user.setUpdatedDate(Long.parseLong(eachResult[6].toString()));
		return user;
	}

}
