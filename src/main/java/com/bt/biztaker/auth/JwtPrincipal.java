package com.bt.biztaker.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookiePrincipal;

/**
 * Default implementation of JwtCookiePrincipal
 */
public class JwtPrincipal implements JwtCookiePrincipal {

    private final static String PERSISTENT = "pst"; // long-term token == rememberme
    private final static String ROLES = "rls";

    protected final Claims claims;
    
    public JwtPrincipal(
            @JsonProperty("name") String name,
            @JsonProperty("persistent") boolean persistent,
            @JsonProperty("roles") Collection<String> roles,
            @JsonProperty("claims") Claims claims) {
        this.claims = Optional.ofNullable(claims).orElseGet(Jwts::claims);
        this.claims.setSubject(name);
        this.claims.put(PERSISTENT, persistent);
        this.claims.put(ROLES, roles);
    }

    
    public JwtPrincipal(String name) {
        this(name, false, Collections.emptyList(), null);
    }

    public JwtPrincipal(Claims claims) {
        this.claims = claims;
    }
    public Claims getClaims() {
        return claims;
    }
   
    @Override
    public boolean isInRole(String role) {
        return getRoles().contains(role);
    }

    @SuppressWarnings("unchecked")
	public Collection<String> getRoles() {
        return Optional.ofNullable(claims.get(ROLES))
                .map(Collection.class::cast)
                .orElse(Collections.emptyList());
    }

    public void setRoles(Collection<String> roles) {
        claims.put(ROLES, roles);
    }

   
    @Override
    public boolean isPersistent() {
        return claims.get(PERSISTENT) == Boolean.TRUE;
    }
   
    public void setPersistent(boolean persistent) {
        claims.put(PERSISTENT, persistent);
    }

    @Override
    public String getName() {
        return (String) claims.getSubject();
    }

  
    public void setName(String name) {
        claims.setSubject(name);
    }

}
