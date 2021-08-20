package de.lray.service.admin.user;

import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserPatch;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResultItem;
import de.lray.service.admin.user.operation.UserPatchFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ConcreteUserRepository implements UserRepository {

  static final String REST_DATETIME_FORMAT = "dd-MM-yy hh:mm:ss";
  static final String CONTACT_TYPE_WORK_STRING = "work";

  UserPatchFactory patchFactory;

  public ConcreteUserRepository() {
    this(new UserPatchFactory());
  }

  ConcreteUserRepository(UserPatchFactory userPatchFactory ) {
    patchFactory = userPatchFactory;
  }

  @Override
  public List<UserResultItem> getUsers(UserSearchCriteria searchCriteria) {
    return Arrays.asList();
  }

  @Override
  public UserResource getUser(String id ) {
    throw new UserUnknownException("To be done");
  }

  @Override public UserResource addUser(UserAdd payload ) {
    throw new UnsupportedOperationException("Not done yet.");
  }

  @Override public UserResource updateUser(String id, UserAdd payload) {
    throw new UnsupportedOperationException("Will also follow soon.");
  }

  @Override public UserResource patchUser(String id, UserPatch payload) {
    throw new UnsupportedOperationException("Will potentially follow soon.");
  }

  static private void findUserByPublicId(String id) {
    throw new UnsupportedOperationException("Will follow soon.");
  }

}
