package com.bt.biztaker.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.SessionFactory;

import com.bt.biztaker.entity.Card;
import com.bt.biztaker.entity.Company;
import com.bt.biztaker.entity.Person;
import com.bt.biztaker.entity.PersonCardMapping;

import io.dropwizard.hibernate.AbstractDAO;

@SuppressWarnings("unchecked")
public class PersonCardMappingDao extends AbstractDAO<PersonCardMapping> {

	public PersonCardMappingDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public PersonCardMapping saveOrUpdateCardMapping(PersonCardMapping mapping) {
		return persist(mapping);
	}
	
	public List<Long> getMyAccessibleCardIds(Long personId){
		Set<Long> accessibleCards = new HashSet<>();
		String queryString = "SELECT pcm.card.id FROM PersonCardMapping pcm WHERE pcm.destinationOwner.id = :personId";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("personId", personId);
		List<Object> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object eachObject : resultList) {
				accessibleCards.add((Long) eachObject);
			}
		}
		return new ArrayList<>(accessibleCards);
	}
	
	public PersonCardMapping getPersonCardMapping(Long sourcePersonId, Long destinationPersonId, Long cardId) {
		String queryString = "SELECT pcm.id, pcm.sourceOwner.id, pcm.destinationOwner.id, pcm.card.id FROM PersonCardMapping pcm "
				+ "WHERE pcm.sourceOwner.id = :sourceId AND pcm.destinationOwner.id = :destinationId AND pcm.card.id = :cardId";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("sourceId", sourcePersonId);
		query.setParameter("destinationId", destinationPersonId);
		query.setParameter("cardId", cardId);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object[] eachResult : resultList) {
				PersonCardMapping cardMapping = new PersonCardMapping();
				cardMapping.setId(Long.parseLong(eachResult[0].toString()));
				Person sourcePerson = new Person();
				sourcePerson.setId(Long.parseLong(eachResult[1].toString()));
				cardMapping.setSourceOwner(sourcePerson);
				Person destinationPerson = new Person();
				destinationPerson.setId(Long.parseLong(eachResult[2].toString()));
				cardMapping.setDestinationOwner(destinationPerson);
				Card card = new Card();
				card.setId(Long.parseLong(eachResult[3].toString()));
				cardMapping.setCard(card);
				return cardMapping;
			}
		}
		return null;
	}
	
	public List<PersonCardMapping> getCardNetwork(Long cardId){
		List<PersonCardMapping> mappingNetwork = new ArrayList<>();
		String queryString = "SELECT pcm.id, pcm.sourceOwner.id, pcm.sourceOwner.firstName, pcm.sourceOwner.lastName , "
				+ "pcm.destinationOwner.id, pcm.destinationOwner.firstName, pcm.destinationOwner.lastName ,"
				+ "pcm.card.id, pcm.card.cardTitle, pcm.card.company.id, pcm.card.company.companyName , "
				+ "pcm.latitude, pcm.longitude, pcm.createdDate FROM PersonCardMapping pcm WHERE pcm.card.id = :cardId";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("cardId", cardId);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object[] eachResult : resultList) {
				mappingNetwork.add(fillCardNetwork(eachResult));
			}
		}
		return mappingNetwork;
	}
	
	private PersonCardMapping fillCardNetwork(Object[] result) {
		PersonCardMapping cardMapping = new PersonCardMapping();
		cardMapping.setId(Long.parseLong(result[0].toString()));
		if (result[1] != null) {
			Person sourcePerson = new Person();
			sourcePerson.setId(Long.parseLong(result[1].toString()));
			if (result[2] != null) {
				sourcePerson.setFirstName(result[2].toString());
			}
			if (result[3] != null) {
				sourcePerson.setLastName(result[3].toString());
			}
			cardMapping.setSourceOwner(sourcePerson);
		}
		if (result[4] != null) {
			Person destinationPerson = new Person();
			destinationPerson.setId(Long.parseLong(result[4].toString()));
			if (result[5] != null) {
				destinationPerson.setFirstName(result[5].toString());
			}
			if (result[6] != null) {
				destinationPerson.setLastName(result[6].toString());
			}
			cardMapping.setDestinationOwner(destinationPerson);
		}
		if (result[7] != null) {
			Card card = new Card();
			card.setId(Long.parseLong(result[7].toString()));
			if (result[8] != null) {
				card.setCardTitle(result[8].toString());
			}
			if (result[9] != null) {
				Company company = new Company();
				company.setId(Long.parseLong(result[9].toString()));
				if (result[10] != null) {
					company.setCompanyName(result[10].toString());
				}
				card.setCompany(company);
			}
			cardMapping.setCard(card);
		}
		if (result[11] != null) {
			cardMapping.setLatitude(Double.parseDouble(result[11].toString()));
		}
		if (result[12] != null) {
			cardMapping.setLongitude(Double.parseDouble(result[12].toString()));
		}
		if (result[13] != null) {
			cardMapping.setCreatedDate(Long.parseLong(result[13].toString()));
		}
		return cardMapping;
	}

}
