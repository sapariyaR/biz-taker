package com.bt.biztaker.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.bt.biztaker.entity.Card;
import com.bt.biztaker.entity.CardAccessibility;
import com.bt.biztaker.entity.Company;
import com.bt.biztaker.entity.Person;

import io.dropwizard.hibernate.AbstractDAO;

@SuppressWarnings("unchecked")
public class CardDao extends AbstractDAO<Card> {

	public CardDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public Card saveOrUpdateCard(Card card) {
		return persist(card);
	}
	
	
	
	public List<Card> getAllPublicCardUnderCompany(Long companyId, Long personId,CardAccessibility accessibility){
		List<Card> publicCards = new ArrayList<>();
		String queryString = "SELECT c.id, c.cardTitle , c.cardDescription, c.createdDate , c.mobileNumber, c.phoneNumber, "
				+ "c.cardAccessibility , "
				+ " c.metaDatas FROM Card c WHERE c.company.createdby.id = :companyId AND c.cardAccessibility = :accessibility "
				+ "AND c.createdby.id != :myPersonId";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("companyId", companyId);
		query.setParameter("accessibility", accessibility);
		query.setParameter("myPersonId", personId);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object[] result : resultList) {
				Card card = new Card();
				card.setId(Long.parseLong(result[0].toString()));
				if(result[1] != null) {
					card.setCardTitle(result[1].toString());
				}
				if(result[2] != null) {
					card.setCardDescription(result[2].toString());
				}
				card.setCreatedDate(Long.parseLong(result[3].toString()));
				if(result[4] != null) {
					card.setMobileNumber(Integer.parseInt(result[4].toString()));
				}
				if(result[5] != null) {
					card.setPhoneNumber(Integer.parseInt(result[5].toString()));
				}
				card.setCardAccessibility(CardAccessibility.valueOf(result[6].toString()));
				if(result[7] != null) {
					card.setMetaDatas(result[7].toString());
				}
				publicCards.add(card);
			}
		}
		return publicCards;
	}
	
	public Boolean isCardPresent(Long cardId) {
		String queryString = "SELECT c.id FROM Card c WHERE c.id = :cardId";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("cardId", cardId);
		List<Object> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return true;
		}
		return false;
	}
	
	public List<Long> getMyAccessibleCardIds(Long personId){
		Set<Long> accessibleCards = new HashSet<>();
		String QueryStringCard = "SELECT c.id FROM Card c WHERE c.createdby.id = :ownerId";
		Query queryCard = currentSession().createQuery(QueryStringCard);
		queryCard.setParameter("ownerId", personId);
		List<Object> resultList = queryCard.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object eachObject : resultList) {
				accessibleCards.add((Long) eachObject);
			}
		}
		return new ArrayList<>(accessibleCards);
		
	}
	
	public Card getCardById(Long cardId) {
		String queryString = "SELECT c.id , c.company.id, c.company.companyName, c.company.companyAddress, c.company.latitude, c.company.longitude, "
				+ "c.company.createdby.id, c.company.createdby.firstName , c.company.createdby.lastName ,c.company.metaDatas ,"
				+ " c.cardTitle ,c.cardDescription, c.createdDate , c.mobileNumber, c.phoneNumber, c.cardAccessibility , "
				+ " c.metaDatas FROM Card c WHERE c.id = :cardId";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("cardId", cardId);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return fillMyCardList(resultList.get(0));
		}
		return null;
	}
	
	public List<Card> getCardByCardIds_(List<Long> cardIds){
		Criteria criteria = criteria();
		criteria.add(Restrictions.in("id",cardIds));
		return criteria.list();
	}
	
	public List<Card> getCardsByIds(List<Long> cardIds){
		List<Card> myCardList = new ArrayList<>();
		String queryString = "SELECT c.id , c.company.id, c.company.companyName, c.company.companyAddress, c.company.latitude, c.company.longitude, "
				+ "c.company.createdby.id, c.company.createdby.firstName , c.company.createdby.lastName ,c.company.metaDatas ,"
				+ " c.cardTitle ,c.cardDescription, c.createdDate , c.mobileNumber, c.phoneNumber, c.cardAccessibility , "
				+ " c.metaDatas c.isMine FROM Card c WHERE c.id IN (:cardIds)";
		Query query = currentSession().createQuery(queryString);
		query.setParameter("cardIds", cardIds);
		List<Object[]> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			for(Object[] eachResult : resultList) {
				Card card = fillMyCardList(eachResult);
				myCardList.add(card);
			}
		}
		return myCardList;
	}
	
	/// ************************************************************ PRIVATE METHODS ****************************************
	private Card fillMyCardList(Object[] result) {
		Card card = new Card();
		card.setId(Long.parseLong(result[0].toString()));
		if (result[1] != null) {
			Company company = new Company();
			company.setId(Long.parseLong(result[1].toString()));
			if (result[2] != null) {
				company.setCompanyName(result[3].toString());
			}
			if (result[3] != null) {
				company.setCompanyAddress(result[3].toString());
			}
			if (result[4] != null) {
				company.setLatitude(Double.parseDouble(result[4].toString()));
			}
			if (result[5] != null) {
				company.setLongitude(Double.parseDouble(result[5].toString()));
			}
			if (result[6] != null) {
				Person person = new Person();
				person.setId(Long.parseLong(result[6].toString()));
				if (result[7] != null) {
					person.setFirstName(result[7].toString());
				}
				if (result[8] != null) {
					person.setLastName(result[8].toString());
				}
				company.setCreatedby(person);
			}
			if (result[9] != null) {
				company.setMetaDatas(result[9].toString());
			}
			card.setCompany(company);
		}
		if (result[10] != null) {
			card.setCardTitle(result[10].toString());
		}
		if (result[11] != null) {
			card.setCardDescription(result[11].toString());
		}
		card.setCreatedDate(Long.parseLong(result[12].toString()));
		if (result[13] != null) {
			card.setMobileNumber(Integer.parseInt(result[13].toString()));
		}
		if (result[14] != null) {
			card.setPhoneNumber(Integer.parseInt(result[14].toString()));
		}
		card.setCardAccessibility(CardAccessibility.valueOf(result[15].toString()));
		if (result[16] != null) {
			card.setMetaDatas(result[16].toString());
		}
		if(result[17] != null) {
			card.setIsMine(Boolean.parseBoolean(result[17].toString()));
		}
		return card;
	}
	
}
