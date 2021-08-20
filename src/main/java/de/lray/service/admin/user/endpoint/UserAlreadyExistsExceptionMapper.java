package de.lray.service.admin.user.endpoint;

import de.lray.service.admin.common.Error;
import de.lray.service.admin.user.UserAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserAlreadyExistsExceptionMapper implements ExceptionMapper<UserAlreadyExistsException> {
    @Override
    public Response toResponse(UserAlreadyExistsException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(Error.alreadyExists(e.getMessage()))
                .build();
    }
}
