package com.bt.biztaker.resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bt.biztaker.auth.JwtPrincipal;
import com.bt.biztaker.co.CommanConstant;
import com.bt.biztaker.dao.CardDao;
import com.bt.biztaker.dao.CompanyDao;
import com.bt.biztaker.dao.PersonCardMappingDao;
import com.bt.biztaker.dto.CardMappingQRDto;
import com.bt.biztaker.entity.Card;
import com.bt.biztaker.entity.CardAccessibility;
import com.bt.biztaker.entity.Person;
import com.bt.biztaker.entity.PersonCardMapping;
import com.bt.biztaker.entity.SharingType;
import com.bt.biztaker.qrcode.QRConstant;
import com.bt.biztaker.qrcode.QRObject;
import com.bt.biztaker.utils.BizTakerUtils;
import com.bt.biztaker.utils.JsonUtils;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/card")
@Produces(MediaType.APPLICATION_JSON)
public class CardResource {

	private static final Logger LOGGER = Logger.getLogger(CardResource.class.getName());
	
	public final String GET_MY_CARDS_COMPANY = "/getmycardsandcompany";
	private final String GET_PUBLIC_CARDS_UNDER_COMPANY = "/getpubliccardundercompany";
	private final String GET_CARD_NETWORK = "/getcardnetwork";
	private final String GET_QR_CODE_BYTE_BY_CARDID = "/getcardqrcodebycardid";
	private final String MAP_CARD_BASED_ON_QR_STRING = "/mapcardbasedonqrstring";
	
	//**************************** INIT VARIABLES START**********************************************************
	private CardDao cardDao;
	@SuppressWarnings("unused") // temp add to remove unused warning message 
	private CompanyDao companyDao;
	private PersonCardMappingDao cardMappingDao;
	//**************************** INIT VARIABLES END************************************************************
	public CardResource(CardDao cardDao, CompanyDao companyDao,PersonCardMappingDao cardMappingDao) {
		this.cardDao = cardDao;
		this.companyDao = companyDao;
		this.cardMappingDao = cardMappingDao;
	}
	
	@GET
	@Path(GET_MY_CARDS_COMPANY)
	@UnitOfWork
	@RolesAllowed({CommanConstant.ROLE_USER})
	public Response getMyCardsAndCompany(@NotNull @Auth JwtPrincipal auth) {
		LOGGER.info("Get My Card And Company");
		try {
			Long personId = Long.parseLong(auth.getClaims().get("personId").toString()) ;
			Set<Long> accessibleCards = new HashSet<>();
			accessibleCards.addAll(cardDao.getMyAccessibleCardIds(personId));
			accessibleCards.addAll(cardMappingDao.getMyAccessibleCardIds(personId));
			List<Long> cardIds = new ArrayList<>(); cardIds.addAll(accessibleCards);
			cardIds.add(0l);
			List<Card> cards = cardDao.getCardByCardIds_(cardIds);
			if(cards != null && cards.size() > 0) {
				return Response.status(Status.OK).entity(JsonUtils.parseMyCards(cards).toString()).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(JsonUtils.getMessageJson("No data found.")).build();
			}
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			throw new WebApplicationException(e.getMessage(), Status.PRECONDITION_FAILED);
		}
	}
	
	@GET
	@Path(GET_PUBLIC_CARDS_UNDER_COMPANY)
	@UnitOfWork
	@RolesAllowed({CommanConstant.ROLE_USER})
	public Response getPublicCardUnderCompany(@NotNull @Auth JwtPrincipal auth,@NotNull(message="Company is required") @QueryParam("companyId") Long companyId) {
		LOGGER.info("Get getPublicCardUnderCompany");
		try {
			Long personId = Long.parseLong(auth.getClaims().get("personId").toString()) ;
			List<Card> cards = cardDao.getAllPublicCardUnderCompany(companyId, personId, CardAccessibility.PUBLIC);
			if(cards != null && cards.size() > 0) {
				return Response.status(Status.OK).entity(JsonUtils.getJson(cards).toString()).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(JsonUtils.getMessageJson("No data found.")).build();
			}
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			throw new WebApplicationException(e.getMessage(), Status.PRECONDITION_FAILED);
		}
	}
	
	@GET
	@Path(GET_CARD_NETWORK)
	@UnitOfWork
	@RolesAllowed({CommanConstant.ROLE_USER})
	public Response getCardNetwork(@NotNull(message="Card id is required") @QueryParam("cardId") Long cardId) {
		LOGGER.info("Get Card Network");
		try {
			List<PersonCardMapping> personCardMappingList = cardMappingDao.getCardNetwork(cardId);
			if(personCardMappingList != null && personCardMappingList.size() > 0) {
				return Response.status(Status.OK).entity(JsonUtils.getJson(personCardMappingList).toString()).build();
			}else {
				return Response.status(Status.NO_CONTENT).entity(JsonUtils.getMessageJson("No data found.")).build();
			}
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			throw new WebApplicationException(e.getMessage(), Status.PRECONDITION_FAILED);
		}
	}
	
	@GET
	@Path(GET_QR_CODE_BYTE_BY_CARDID)
	@UnitOfWork
	@RolesAllowed({CommanConstant.ROLE_USER})
	public Response getQRCodeByteByCardId(@NotNull @Auth JwtPrincipal auth,@NotNull(message="Card id is required") @QueryParam("cardId") Long cardId) {
		LOGGER.info("getQRCodeByteByCardId");
		try {
			Long personId = Long.parseLong(auth.getClaims().get("personId").toString()) ;
			Boolean isCardPresent = cardDao.isCardPresent(cardId);
			if(isCardPresent) {
				QRObject object = new QRObject(cardId, QRConstant.QR_CARD,personId);
				byte[]  qrbyteArray = BizTakerUtils.getQRCodeImage(object,350,350);
				return Response.status(Status.OK).entity(JsonUtils.getJson(qrbyteArray).toString()).build();
			}else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtils.getMessageJson("Unable to find relevent card.")).build();
			}
			
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			throw new WebApplicationException(e.getMessage(), Status.PRECONDITION_FAILED);
		}
	}
	
	@POST
	@Path(MAP_CARD_BASED_ON_QR_STRING)
	@UnitOfWork
	@RolesAllowed({CommanConstant.ROLE_USER})
	public Response mapCardBasedOnQRString(@Auth JwtPrincipal auth,
			@Valid @NotNull(message="Input is required") CardMappingQRDto qrDto) {
		try {
			Long destinationPersonId = Long.parseLong(auth.getClaims().get("personId").toString()) ;
			QRObject qrObject = BizTakerUtils.getQRObjectFromQRString(qrDto.qrstring);
			if(qrObject != null && qrObject.entityId != null && qrObject.generatorId != null) {
				if(qrObject.generatorId != destinationPersonId) {
					PersonCardMapping exestingMapping = cardMappingDao.getPersonCardMapping(qrObject.generatorId, destinationPersonId, qrObject.entityId);
					if(exestingMapping == null) {
						PersonCardMapping newMapping = new PersonCardMapping();
						newMapping.setLatitude(qrDto.latitude);
						newMapping.setLongitude(qrDto.longitude);
						Person sourceOwner = new Person();
						sourceOwner.setId(qrObject.generatorId);
						newMapping.setSourceOwner(sourceOwner);
						Person destinationOwner = new Person();
						destinationOwner.setId(destinationPersonId);
						newMapping.setDestinationOwner(destinationOwner);
						Card card = new Card();
						card.setId(qrObject.entityId);
						newMapping.setCard(card);
						newMapping.setCreatedDate(Calendar.getInstance().getTimeInMillis());
						newMapping.setSharingType(SharingType.QR_SCANNER);
						cardMappingDao.saveOrUpdateCardMapping(newMapping);
						return Response.status(Status.OK).entity(JsonUtils.getMessageJson("Card Received successfully.")).build();
					}else {
						return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtils.getMessageJson("You alredy have this card.")).build();
					}
				}else {
					return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtils.getMessageJson("You alredy have this card.")).build();
				}
			}else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtils.getMessageJson("Something went wrong with QR code,Regenerate and scan again.")).build();
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
			throw new WebApplicationException(e.getMessage(), Status.PRECONDITION_FAILED);
		}
	}
	
}
