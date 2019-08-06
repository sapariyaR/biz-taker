package com.bt.biztaker.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.bt.biztaker.utils.JsonUtils;

import io.dropwizard.jersey.validation.JerseyViolationException;

@Provider
public class JerseyViolationExceptionMapper implements ExceptionMapper<JerseyViolationException> {

	@Override
	public Response toResponse(JerseyViolationException exception) {
		return Response.status(Status.BAD_REQUEST)
				.entity(JsonUtils.getMessageJson(exception.getConstraintViolations().iterator().next().getMessage()))
				.build();
	}
}
