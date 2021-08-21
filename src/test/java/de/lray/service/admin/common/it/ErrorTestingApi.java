package de.lray.service.admin.common.it;

import de.lray.service.admin.user.UserAlreadyExistsException;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.Valid;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.mockito.Mockito;

import java.util.*;

@Path("errorTest")
public class ErrorTestingApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/alreadyExist")
    public TestMessage testAlreadyExistException() {
     throw new UserAlreadyExistsException("Test-API call - already exists.");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unknownUser")
    public TestMessage testUnknownUserException() {
        throw new UserUnknownException("Test-API call - unknown user");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/validationError")
    public TestMessage testValidationException() {
        throw new UnexpectedTypeException();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/violationError")
    public TestMessage testConstraintValidationException() {
        throw new ConstraintViolationException(Collections.emptySet());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ping")
    public TestMessage testCorrectCall() {
        return new TestMessage("pong");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/ping")
    public TestMessage testCorrectPostCall(@Valid UserName parameter) {
        Objects.requireNonNull(parameter.familyName);
        return new TestMessage("pong");
    }

}