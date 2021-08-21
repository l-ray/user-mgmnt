package de.lray.service.admin.user;

import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.endpoint.UserAdminApi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@RequestScoped
public class UserAdminResource implements UserAdminApi {

    static final int DEFAULT_COUNT_VALUE = 100;
    static final int MAX_SPLITS_NEEDED = 3;
    static final String FILTER_USER_NAME_EQ = "userName eq ";
    static final String FILTER_LAST_MODIFIED_GT = "meta.lastModified gt ";
    static final String REST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    static final Logger LOGGER = LoggerFactory.getLogger(UserAdminResource.class);

    @Context
    HttpHeaders httpHeaders = null;

    @Context
    ContainerRequestContext containerRequestContext = null;

    @Inject
    UserRepository repository = null;

    public UserAdminResource() { }

    public UserAdminResource(UserRepository repo) {
        this.repository = repo;
    }

    @Override
    public UserResult getUsers(UriInfo info) throws ParseException {
        // e.g. filter=meta.lastModified gt “2020-04-07T14:19:34Z”
        // e.g. filter=userName%20eq%20%22myemail%40example.com%22
        MultivaluedMap<String, String> queryParameters = info.getQueryParameters();
        String filter = queryParameters.getFirst("filter");

        String startIndexString = queryParameters.getFirst("startIndex");
        Integer startIndex = startIndexString != null
                ? Integer.valueOf(startIndexString) : null;

        String countString = queryParameters.getFirst("count");
        Integer count = countString != null
                ? Integer.valueOf(countString) : null;

        LOGGER.info("GetUsers filter {} start index {} count {}",filter, startIndex, count);

        UserSearchCriteria searchCriteria = new UserSearchCriteria();
        searchCriteria.startIndex = startIndex == null ? 1 : startIndex;
        searchCriteria.count = count == null ? DEFAULT_COUNT_VALUE : count;
        searchCriteria.userName = filter != null && filter.contains(FILTER_USER_NAME_EQ)
                ? filter.substring(filter.indexOf(FILTER_USER_NAME_EQ)).split("\"", MAX_SPLITS_NEEDED)[1]
                : null;
        searchCriteria.lastModifiedAfter = filter != null && filter.contains(FILTER_LAST_MODIFIED_GT)
                ? new SimpleDateFormat(REST_DATE_FORMAT).parse(
                filter.substring(filter.indexOf(FILTER_LAST_MODIFIED_GT))
                        .split("\"", MAX_SPLITS_NEEDED)[1]
        )
                : null;

        List<UserResultItem> resources = repository.getUsers(searchCriteria);
        UserResult response = new UserResult();
        response.totalResults = resources.size();
        response.startIndex = startIndex == null ? 1 : startIndex;
        response.itemsPerPage = searchCriteria.count;
        response.Resources = resources;

        return response;
    }

    @Override
    public UserResource getUser(String userId) {
        LOGGER.info("get user by userId {}", userId);
        UserResource response = repository.getUser(userId);
        return response;
    }

    @Override
    public UserResource addUser(UserAdd payload) {
        LOGGER.info("Post new user {}", payload);
        UserResource response = repository.addUser(payload);
        return response;
    }

    @Override
    public UserResource updateUser(String userId, UserAdd payload) {
        LOGGER.info("Put user by userId {}: {}", userId, payload);
        UserResource response = repository.updateUser(userId, payload);
        return response;
    }

    @Override
    @PATCH
    public UserResource patchUser(String userId, UserPatch payload) {
        LOGGER.info("patch user by userId {}:{}", userId, payload);

        UserResource response = repository.patchUser(userId, payload);
        return response;
    }

}