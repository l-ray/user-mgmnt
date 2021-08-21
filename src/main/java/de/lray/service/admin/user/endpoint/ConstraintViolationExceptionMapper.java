package de.lray.service.admin.user.endpoint;

import de.lray.service.admin.common.Error;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Error.invalidArgument(prepareMessage(exception)))
                .build();
    }

    private String prepareMessage(ConstraintViolationException exception) {
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            msg.append(
                    cv.getPropertyPath())
                    .append(" ")
                    .append(cv.getMessage())
                    .append("\n");
        }
        return msg.toString();
    }
}