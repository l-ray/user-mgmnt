package de.lray.service.admin.user.it;

import de.lray.service.admin.ScimTestMessageFactory;
import de.lray.service.admin.user.UserAlreadyExistsException;
import de.lray.service.admin.user.persistence.UserRepository;
import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserPatch;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResultItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Default
public class MockedUserRepository implements UserRepository {

    public MockedUserRepository() {}

    @Override
    public List<UserResultItem> getUsers(UserSearchCriteria searchCriteria) {
        return Collections.emptyList();
    }

    @Override
    public UserResource getUser(String id) {
        return ScimTestMessageFactory.createUserAdd();
    }

    @Override
    public UserResource addUser(UserAdd payload) {
        throw new UserAlreadyExistsException("Test - entity already exists.");
    }

    @Override
    public UserResource updateUser(String id, UserAdd payload) {
        return ScimTestMessageFactory.createUserAdd();
    }

    @Override
    public UserResource patchUser(String userId, UserPatch payload) {
        throw new UserUnknownException("Test - not found ");
    }
}
