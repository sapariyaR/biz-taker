package com.bt.biztaker.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.SessionFactory;

import com.bt.biztaker.entity.Gender;
import com.bt.biztaker.entity.Person;
import com.bt.biztaker.entity.User;

import io.dropwizard.hibernate.AbstractDAO;

@SuppressWarnings("unchecked")
public class PersonDao extends AbstractDAO<Person> {

	public PersonDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	String selectedFields = "p.id, p.firstName , p.lastName , p.gender , p.birthDate , p.latitude, p.longitude , "
			+ "p.address , p.user.id , p.token , p.updatedDate , p.dpUrl";
	
	public Person saveOrUpdatePerson(Person person) {
		return persist(person);
	}
	
	public Person getPersonByUserId(Long userId) {
		Query query = currentSession().createQuery("SELECT " +selectedFields +" FROM Person p WHERE p.user.id = :userId");
		query.setParameter("userId", userId);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object[] eachResult : resultList) {
				return fillPersonObject(eachResult);
			}
		}
		return null;
	}
	
	private Person fillPersonObject(Object[] result) {
		Person person = new Person();
		person.setId(Long.parseLong(result[0].toString()));
		if (result[1] != null) {
			person.setFirstName(result[1].toString());
		}
		if (result[2] != null) {
			person.setLastName(result[2].toString());
		}
		if (result[3] != null) {
			person.setGender(Gender.valueOf(result[3].toString()));
		}
		if (result[4] != null) {
			person.setBirthDate(Long.parseLong(result[4].toString()));
		}
		if (result[5] != null) {
			person.setLatitude(Double.parseDouble(result[5].toString()));
		}
		if (result[6] != null) {
			person.setLongitude(Double.parseDouble(result[6].toString()));
		}
		if (result[7] != null) {
			person.setAddress(result[7].toString());
		}
		if (result[8] != null) {
			User user = new User();
			user.setId(Long.parseLong(result[8].toString()));
			person.setUser(user);
		}
		if (result[9] != null) {
			person.setToken(result[9].toString());
		}
		if (result[10] != null) {
			person.setUpdatedDate(Long.parseLong(result[10].toString()));
		}
		if (result[11] != null) {
			person.setDpUrl(result[11].toString());
		}
		return person;
	}

}
