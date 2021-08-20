package de.lray.service.admin.user.endpoint;

import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.common.Error;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserUnknownExceptionMapper implements ExceptionMapper<UserUnknownException> {
    @Override
    public Response toResponse(UserUnknownException e) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(Error.notFound(e.getMessage()))
                .build();
    }
}
