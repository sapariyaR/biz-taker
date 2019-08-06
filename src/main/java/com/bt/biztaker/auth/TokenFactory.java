package com.bt.biztaker.auth;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenFactory {

	private final byte[] tokenSecret;
	private Long ACCESS_EXP_TIME_MIN = 30l;
	private Long REFREDH_EXP_TIME_DAYS = 20l;
	
	public TokenFactory(byte[] tokenSecret) {
		 this.tokenSecret = tokenSecret;
	}
	
	public String createAccessJwtToken(JwtPrincipal jwtPrincipal) {
		JwtBuilder builder = generateToken(jwtPrincipal);
		long expMillis = Calendar.getInstance().getTimeInMillis() + (ACCESS_EXP_TIME_MIN * 60000); 
	    Date exp = new Date(expMillis); 
	    builder.setExpiration(exp);
		return builder.compact();
	}
	
	private JwtBuilder generateToken(JwtPrincipal jwtPrincipal) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; 
		Key signingKey = new SecretKeySpec(tokenSecret, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder().setClaims(jwtPrincipal.getClaims())
		.setIssuedAt(Calendar.getInstance().getTime()) 
		.setSubject(jwtPrincipal.getName())
	    .setIssuer("BIZ-TAKER") 
	    .signWith(signatureAlgorithm, signingKey);
		return builder;
	}
	
	public String createRefreshToken(JwtPrincipal jwtPrincipal) {
		JwtBuilder builder = generateToken(jwtPrincipal);
		long expMillis = Calendar.getInstance().getTimeInMillis() + (REFREDH_EXP_TIME_DAYS * 1440 * 60000); 
	    Date exp = new Date(expMillis); 
	    builder.setExpiration(exp);
		return builder.compact();
	}
}
