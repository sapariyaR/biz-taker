package com.bt.biztaker;
import java.util.Optional;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;

import com.bt.biztaker.auth.JwtAuthFilter;
import com.bt.biztaker.auth.JwtPrincipal;
import com.bt.biztaker.auth.TokenFactory;
import com.bt.biztaker.dao.CardDao;
import com.bt.biztaker.dao.CompanyDao;
import com.bt.biztaker.dao.PersonCardMappingDao;
import com.bt.biztaker.dao.PersonDao;
import com.bt.biztaker.dao.UserDao;
import com.bt.biztaker.entity.Card;
import com.bt.biztaker.entity.CardPhoto;
import com.bt.biztaker.entity.Company;
import com.bt.biztaker.entity.CompanyTransaction;
import com.bt.biztaker.entity.NewsSubscription;
import com.bt.biztaker.entity.Person;
import com.bt.biztaker.entity.PersonCardMapping;
import com.bt.biztaker.entity.User;
import com.bt.biztaker.exception.JerseyViolationExceptionMapper;
import com.bt.biztaker.health.TemplateHealthCheck;
import com.bt.biztaker.resources.AuthenticationResource;
import com.bt.biztaker.resources.CardResource;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WebApplication extends Application<WebConfiguration> {
	public static void main(String[] args) throws Exception {
		new WebApplication().run(args);
	}

	private final HibernateBundle<WebConfiguration> hibernateBundle = new HibernateBundle<WebConfiguration>(User.class,
			 Person.class,Card.class,PersonCardMapping.class,Company.class,CardPhoto.class,NewsSubscription.class,CompanyTransaction.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(WebConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "BizTaker";
	}

	@Override
	public void initialize(Bootstrap<WebConfiguration> bootstrap) {
		// Enable variable substitution with environment variables

		bootstrap.addBundle(new MigrationsBundle<WebConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(WebConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		bootstrap.addBundle(hibernateBundle);
		//bootstrap.addBundle(JwtCookieAuthBundle.getDefault());
		bootstrap.addBundle(new ConfiguredAssetsBundle("/webapp", "/", "index.html"));
	}

	@Override
	public void run(WebConfiguration configuration, Environment environment) throws Exception {
		final byte[] key = configuration.getJwtTokenSecret();
		
		final JwtConsumer consumer = new JwtConsumerBuilder()
	            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
	            .setRequireExpirationTime() // the JWT must have an expiration time
	            .setRequireSubject() // the JWT must have a subject claim
	            .setVerificationKey(new HmacKey(key)) // verify the signature with the public key
	            .setRelaxVerificationKeyValidation() // relaxes key length requirement
	            .build(); // create the JwtConsumer instance
		
		environment.jersey().register(new AuthDynamicFeature(
	            new JwtAuthFilter.Builder<JwtPrincipal>()
	                .setJwtConsumer(consumer)
	                .setRealm("realm")
	                .setPrefix("Bearer")
	                .setAuthenticator(new ExampleAuthenticator())
	                .buildAuthFilter()));
		TokenFactory tokenFactory = new TokenFactory(key);
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(JwtPrincipal.class));
		
		environment.healthChecks().register("template", new TemplateHealthCheck());
		environment.jersey().register(MultiPartFeature.class);

		// ********************** initialization of DAO Start **********
		UserDao userDao = new UserDao(hibernateBundle.getSessionFactory());
		PersonDao personDao = new PersonDao(hibernateBundle.getSessionFactory());
		CardDao cardDao = new CardDao(hibernateBundle.getSessionFactory());
		CompanyDao companyDao = new CompanyDao(hibernateBundle.getSessionFactory());
		PersonCardMappingDao cardMappingDao = new PersonCardMappingDao(hibernateBundle.getSessionFactory());
		// ********************** initialization of DAO End ************
		
		//*********************** initialization of Service Start ****************
		
		//*********************** initialization of Service End ****************
				
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		
		// ********************** initialization of Resource Start **********
		environment.jersey().register(new AuthenticationResource(userDao,personDao,tokenFactory));
		environment.jersey().register(new CardResource(cardDao, companyDao,cardMappingDao));
		// ********************** initialization of Resource End **********
		
		environment.jersey().register(JerseyViolationExceptionMapper.class);
	}
	 private static class ExampleAuthenticator implements Authenticator<JwtContext, JwtPrincipal> {

	        @Override
	        public Optional<JwtPrincipal> authenticate(JwtContext context) {
	            try {
	                final String issuer = context.getJwtClaims().getIssuer();
	                if ("BIZ-TAKER".equals(issuer)) {
	                	JwtPrincipal jwtPrincipal = new JwtPrincipal(context.getJwtClaims().getSubject());
		                jwtPrincipal.setRoles(context.getJwtClaims().getStringListClaimValue("rls"));
		                jwtPrincipal.getClaims().put("userId",context.getJwtClaims().getClaimValue("userId"));
		                jwtPrincipal.getClaims().put("personId", context.getJwtClaims().getClaimValue("personId"));
						jwtPrincipal.getClaims().put("email", context.getJwtClaims().getClaimValue("email"));
	                	return Optional.of(jwtPrincipal);
	                }
	                return Optional.empty();
	            }
	            catch (MalformedClaimException e) { return Optional.empty(); }
	        }
	    }
}
