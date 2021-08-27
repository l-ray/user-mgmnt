package de.lray.service.admin.user.endpoint;

import de.lray.service.admin.providerconfig.ServiceProviderConfigResource;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserPatch;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.text.ParseException;

@Tag(name = "User-Management", description = "SCIM v2 based endpoint to CRUD system users.")
@SecurityScheme(name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@Path("scim/v2/Users")
public interface UserAdminApi {

    @Operation(summary = "List of Users", description = "Returns a list of existing users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(
                    responseCode = "200", description = "List of users.", content = {
                    @Content(
                            mediaType = ServiceProviderConfigResource.SCIM_MEDIA_TYPE,
                            array = @ArraySchema(schema = @Schema(implementation = UserResult.class))
                    )
            }
            )
    })
    @GET
    @Produces(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
    UserResult getUsers(
            @QueryParam("filter")
            @Parameter(required = false, description = "Allows filtering " +
                    "for user-name using ```filter=userName eq \"emustermann\"```  or " +
                    "last modified after date as in ```filter=lastModified gt \"2021-11-11T04:42:34Z\"```.")
                    String filter,
            @QueryParam("startIndex")
            @Parameter(required = false, description = "Used for pagination, index of first result to be included.")
                    Integer startIndex,
            @QueryParam("count")
            @Parameter(required = false, description = "Max number of results.")
                    Integer count
    ) throws ParseException;

    @Operation(summary = "Select single user",
            description = "Returns user with given id, http error elsewise.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(
                    responseCode = "200", description = "The User.", content = {
                    @Content(
                            mediaType = ServiceProviderConfigResource.SCIM_MEDIA_TYPE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = UserResource.class
                            )
                    )
            }
            )
    })
    @GET
    @Produces(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
    @Path("/{userId}")
    UserResource getUser(@PathParam("userId") String userId);

    @Operation(summary = "Create new user",
            description = "In case of valid payload and if successful create the given user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(
                    responseCode = "200", description = "The created user, holding it's unique user-id..", content = {
                    @Content(
                            mediaType = ServiceProviderConfigResource.SCIM_MEDIA_TYPE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = UserResource.class
                            )
                    )
            }
            )
    })
    @POST
    @Consumes({ServiceProviderConfigResource.SCIM_MEDIA_TYPE, MediaType.APPLICATION_JSON})
    @Produces(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
    UserResource addUser(@Valid UserAdd payload);

    @Operation(summary = "Update existing user",
            description = "In case of valid payload and if successful persisted, updates the given user." +
                    "For changing a users password or active/deactivate, the patch endpoint is to" +
                    "be used instead.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(
                    responseCode = "200", description = "The updated user.", content = {
                    @Content(
                            mediaType = ServiceProviderConfigResource.SCIM_MEDIA_TYPE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = UserResource.class
                            )
                    )
            }
            )
    })
    @Produces(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
    @Consumes({ServiceProviderConfigResource.SCIM_MEDIA_TYPE, MediaType.APPLICATION_JSON})
    @Path("/{userId}")
    @PUT
    UserResource updateUser(@PathParam("userId") String userId, @Valid UserAdd payload);

    @Operation(summary = "Change single attribute of existing user",
            description = "This endpoint supports setting a new user password and user (de-)activation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(
                    responseCode = "200", description = "The updated user.", content = {
                    @Content(
                            mediaType = ServiceProviderConfigResource.SCIM_MEDIA_TYPE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = UserResource.class
                            )
                    )
            }
            )
    })
    @Produces(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
    @Consumes({ServiceProviderConfigResource.SCIM_MEDIA_TYPE, MediaType.APPLICATION_JSON})
    @Path("/{userId}")
    @PATCH
    UserResource patchUser(@PathParam("userId") String userId, @Valid UserPatch payload);

}
