package de.lray.service.admin.user.endpoint;

import de.lray.service.admin.common.Error;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper
        implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(final ValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Error.invalidArgument(prepareMessage(exception)))
                .build();
    }

    private String prepareMessage(ValidationException exception) {
        return exception.getMessage();
    }
}