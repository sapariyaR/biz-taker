package com.bt.biztaker.resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bt.biztaker.auth.JwtPrincipal;
import com.bt.biztaker.auth.TokenFactory;
import com.bt.biztaker.co.CommanConstant;
import com.bt.biztaker.dao.PersonDao;
import com.bt.biztaker.dao.UserDao;
import com.bt.biztaker.dto.LoginDto;
import com.bt.biztaker.dto.SignInDto;
import com.bt.biztaker.dto.SuccessUserDto;
import com.bt.biztaker.entity.Gender;
import com.bt.biztaker.entity.Person;
import com.bt.biztaker.entity.User;
import com.bt.biztaker.utils.BizTakerUtils;
import com.bt.biztaker.utils.JsonUtils;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

	private static final Logger LOGGER = Logger.getLogger(AuthenticationResource.class.getName());
	private final String LOGIN = "/login/{rememberFlag}";
	private final String REFRESH="/refresh";
	private final String SIGNIN_WITH_GOOGLE = "/signInWithGoogle/{rememberFlag}";
	
	//**************************** INIT VARIABLES START************************************************************
	private UserDao userDao;
	private PersonDao personDao;
	private TokenFactory tokenFactory;
		//**************************** INIT VARIABLES START*************************************************************
		
	public AuthenticationResource(UserDao userDao,PersonDao personDao,TokenFactory tokenFactory) {
		this.userDao = userDao;
		this.personDao = personDao;
		this.tokenFactory = tokenFactory;
	}
	
	@POST
	@UnitOfWork
	@Path(LOGIN)
	public Response getUserAuthentication(@Context ContainerRequestContext requestContext,
			@Valid @NotNull LoginDto loginDto, @PathParam("rememberFlag") boolean rememberFlag) {

		try {
			User user = userDao.getUserByEmailId(loginDto.email);
			if (user != null) {
				if (user.getPassword().equals(BizTakerUtils.getSHA256EncryptedString(loginDto.password))) {
					if (user.getIsEnable()) {
						Person person = personDao.getPersonByUserId(user.getId());
						person.setUser(user);
						return onAuthenticationSuccess(person);
					} else {
						return Response.status(Status.BAD_REQUEST)
								.entity(JsonUtils.getMessageJson("Your account is disable.")).build();
					}
				} else {
					return Response.status(Status.BAD_REQUEST)
							.entity(JsonUtils.getMessageJson("Password you have entered is invalid.")).build();
				}
			} else {
				return Response.status(Status.BAD_REQUEST)
						.entity(JsonUtils.getMessageJson("The user name you have entered is invalid.")).build();
			}
		} catch (Exception e) {
			LOGGER.severe("Unable to login User. " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(JsonUtils.getMessageJson(e.getMessage())).build();
		}
	}
	
	/*@GET
	@Path(LOGOUT)
	public Response logout(@Context ContainerRequestContext requestContext) {
		JwtCookiePrincipal.removeFromContext(requestContext);
		return Response.status(Status.OK).entity(JsonUtils.getMessageJson("Logout Successfully.")).build();
	}*/
	
	@GET
	@Path(REFRESH)
	@UnitOfWork
	@RolesAllowed({ CommanConstant.ROLE_USER })
	public Response refresh(@NotNull @Auth JwtPrincipal auth) {
		String auth_token = tokenFactory.createAccessJwtToken(auth);
		String refresh_token = tokenFactory.createRefreshToken(auth);
		return Response.status(Status.OK).header("auth_token", auth_token).header("refresh_token", refresh_token)
				.entity(JsonUtils.getMessageJson("DONE")).build();
	}
	
	@POST
	@UnitOfWork
	@Path(SIGNIN_WITH_GOOGLE)
	public Response getGoogleUserAuthentication(@Context ContainerRequestContext requestContext,
			@Valid @NotNull SignInDto signInDto, @PathParam("rememberFlag") boolean rememberFlag) {
		try {
			User user = userDao.getUserByEmailId(signInDto.email);
			if (user != null) {
				if (user.getIsEnable()) {
					Person person = personDao.getPersonByUserId(user.getId());
					person.setUser(user);
					return onAuthenticationSuccess(person);
				} else {
					return Response.status(Status.BAD_REQUEST)
							.entity(JsonUtils.getMessageJson("Your account is disable.")).build();
				}
			} else {
				Person person = createNewUser(signInDto);
				return onAuthenticationSuccess(person);
			}
		} catch (Exception e) {
			LOGGER.severe("Unable to login User. " + e.getMessage());
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(JsonUtils.getMessageJson(e.getMessage())).build();
		}
	}

	private Response onAuthenticationSuccess(Person person) {
		User user = person.getUser();
		JwtPrincipal jwtPrincipal = new JwtPrincipal(user.toString());
		List<String> roles = new ArrayList<>();
		roles.add(user.getRole());
		jwtPrincipal.setRoles(roles);
		jwtPrincipal.getClaims().put("userId", user.getId());
		jwtPrincipal.getClaims().put("personId", person.getId());
		jwtPrincipal.getClaims().put("email", user.getEmail());

		SuccessUserDto successUserDto = new SuccessUserDto();
		successUserDto.setUserId(user.getId());
		successUserDto.setEmail(user.getEmail());
		successUserDto.setRole(user.getRole());
		successUserDto.setFirstName(person.getFirstName());
		successUserDto.setLastName(person.getLastName());
		successUserDto.setGender(person.getGender().toString());
		successUserDto.setBirthDate(person.getBirthDate());
		successUserDto.setLatitude(person.getLatitude());
		successUserDto.setLongitude(person.getLongitude());
		successUserDto.setAddress(person.getAddress());
		successUserDto.setDpUrl(person.getDpUrl());
		String auth_token = tokenFactory.createAccessJwtToken(jwtPrincipal);
		String refresh_token = tokenFactory.createRefreshToken(jwtPrincipal);
		return Response.status(Status.OK).header("auth_token", auth_token).header("refresh_token", refresh_token)
				.entity(JsonUtils.getJson(successUserDto).toString()).build();

	}
		
	/*
	 * private SuccessUserDto getSuccessUserDto(Person person,
	 * ContainerRequestContext requestContext, boolean rememberFlag) { User user =
	 * person.getUser();
	 * 
	 * DefaultJwtCookiePrincipal principal = new
	 * DefaultJwtCookiePrincipal(user.toString());
	 * principal.setName(person.getId()+" "+person.getFirstName()+" "+person.
	 * getLastName()); List<String> roles = new ArrayList<>();
	 * roles.add(user.getRole()); principal.setRoles(roles);
	 * principal.getClaims().put("userId", user.getId());
	 * principal.getClaims().put("personId", person.getId());
	 * principal.getClaims().put("email", user.getEmail());
	 * principal.addInContext(requestContext); if (rememberFlag) {
	 * principal.setPersistent(true); } SuccessUserDto successUserDto = new
	 * SuccessUserDto(); successUserDto.setUserId(user.getId());
	 * successUserDto.setEmail(user.getEmail());
	 * successUserDto.setRole(user.getRole());
	 * successUserDto.setFirstName(person.getFirstName());
	 * successUserDto.setLastName(person.getLastName());
	 * successUserDto.setGender(person.getGender().toString());
	 * successUserDto.setBirthDate(person.getBirthDate());
	 * successUserDto.setLatitude(person.getLatitude());
	 * successUserDto.setLongitude(person.getLongitude());
	 * successUserDto.setAddress(person.getAddress());
	 * successUserDto.setDpUrl(person.getDpUrl());
	 * 
	 * return successUserDto; }
	 */
	
	private Person createNewUser(SignInDto signInDto) {

		Person person = new Person();
		person.setFirstName(signInDto.name);
		person.setLastName(signInDto.lastName);

		person.setGender(Gender.MALE);

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));

		person.setUpdatedDate(cal.getTimeInMillis());

		User user = new User();
		user.setEmail(signInDto.email);
		user.setRole("user");
		user.setCreatedDate(cal.getTimeInMillis());
		user.setUpdatedDate(cal.getTimeInMillis());

		user = userDao.saveOrUpdateUser(user);

		person.setUser(user);

		return personDao.saveOrUpdatePerson(person);

	}
}
