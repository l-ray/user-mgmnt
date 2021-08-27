package de.lray.service.admin.user.endpoint;

import de.lray.service.admin.common.dto.Error;
import de.lray.service.admin.user.exception.UserCreationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserCreationExceptionMapper implements ExceptionMapper<UserCreationException> {
    @Override
    public Response toResponse(UserCreationException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Error.invalidArgument(e.getMessage()))
                .build();
    }
}
