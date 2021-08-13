package de.lray.service.admin.common.it;

import de.lray.service.admin.user.UserAlreadyExistsException;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserName;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.assertj.core.api.Assertions;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

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
        throw new ConstraintViolationException(Collections.EMPTY_SET);
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
