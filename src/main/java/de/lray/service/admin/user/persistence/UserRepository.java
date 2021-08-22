package de.lray.service.admin.user.persistence;

import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserPatch;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResultItem;

import java.util.List;

public interface UserRepository {

  List<UserResultItem> getUsers(UserSearchCriteria searchCriteria);

  UserResource getUser(String id);

  UserResource addUser(UserAdd payload);

  UserResource updateUser(String id, UserAdd payload);

  UserResource patchUser(String userId, UserPatch payload);
}
