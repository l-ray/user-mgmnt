package de.lray.service.admin.user.persistence;

import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserPatch;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResultItem;
import de.lray.service.admin.user.operation.UserPatchFactory;
import de.lray.service.admin.user.persistence.entities.User;
import de.lray.service.admin.user.persistence.mapper.UserToUserResultItemMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.UserTransaction;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConcreteUserRepository implements UserRepository {


  static final String FIND_USER_BY_PUBLIC_ID_SQL = "SELECT c FROM User c WHERE c.id = :publicId";


  private final EntityManager entityManager;

  private final UserTransaction utx;

  private final UserPatchFactory patchFactory;

  private final TypedQuery<User> FIND_USER_BY_PUBLIC_ID_QUERY;

  @Inject
  public ConcreteUserRepository(
          EntityManager entityManager,
          UserTransaction utx
          ) {
    patchFactory = new UserPatchFactory();
    this.entityManager = entityManager;
    this.utx = utx;

    FIND_USER_BY_PUBLIC_ID_QUERY = entityManager.createQuery(
            FIND_USER_BY_PUBLIC_ID_SQL, User.class);
  }

  @Override
  public List<UserResultItem> getUsers(UserSearchCriteria searchCriteria) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> query = builder.createQuery(User.class);
    Root<User> r = query.from(User.class);

    Predicate predicate = builder.conjunction();

    UserSearchQueryCriteriaConsumer searchConsumer =
            new UserSearchQueryCriteriaConsumer(predicate, builder, r, entityManager.getMetamodel());
    searchConsumer.accept(searchCriteria);
    predicate = searchConsumer.getPredicate();
    query.where(predicate);

    var finalQuery = entityManager.createQuery(query);
    finalQuery.setFirstResult(searchCriteria.startIndex-1);
    finalQuery.setMaxResults(searchCriteria.count);

    List<User> result = finalQuery.getResultList();
    return result.stream().map(UserToUserResultItemMapper::map).collect(Collectors.toList());
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

  private User findUserByPublicId(String id) {
    return FIND_USER_BY_PUBLIC_ID_QUERY.setParameter("publicId", id).getSingleResult();
  }

}
