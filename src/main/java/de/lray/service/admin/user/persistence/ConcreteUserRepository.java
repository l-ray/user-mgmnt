package de.lray.service.admin.user.persistence;

import de.lray.service.admin.user.UserAlreadyExistsException;
import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserPatch;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResultItem;
import de.lray.service.admin.user.operation.UserPatchFactory;
import de.lray.service.admin.user.persistence.entities.User;
import de.lray.service.admin.user.persistence.mapper.UserAddToUserMapper;
import de.lray.service.admin.user.persistence.mapper.UserToUserResourceMapper;
import de.lray.service.admin.user.persistence.mapper.UserToUserResultItemMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class ConcreteUserRepository implements UserRepository {

    static final String FIND_USER_BY_PUBLIC_ID_SQL = "SELECT c FROM User c WHERE c.publicId = :publicId";

    private final EntityManager entityManager;

    private final UserTransaction utx;

    private final UserPatchFactory patchFactory;

    private final TypedQuery<User> queryFindUserByPublicId;

    @Inject
    public ConcreteUserRepository(
            EntityManager entityManager,
            UserTransaction utx,
            UserPatchFactory patchFactory
    ) {
        this.patchFactory = patchFactory;
        this.entityManager = entityManager;
        this.utx = utx;

        queryFindUserByPublicId = entityManager.createQuery(
                FIND_USER_BY_PUBLIC_ID_SQL, User.class
        );
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
        finalQuery.setFirstResult(searchCriteria.getStartIndex() - 1);
        finalQuery.setMaxResults(searchCriteria.getCount());

        List<User> result = finalQuery.getResultList();
        return result.stream().map(UserToUserResultItemMapper::map).collect(Collectors.toList());
    }

    @Override
    public UserResource getUser(String id) {
        return UserToUserResourceMapper.map(findUserByPublicId(id));
    }

    @Override
    @Transactional
    public UserResource addUser(UserAdd payload) {
        try {
            var newUser = UserAddToUserMapper.map(payload);
            var mergedUser = entityManager.merge(newUser);
            return UserToUserResourceMapper.map(mergedUser);
        } catch (jakarta.persistence.PersistenceException ex) {
            throw new UserAlreadyExistsException(String.format("User %s already exists in system.", payload.userName));
        }
    }

    @Override
    @Transactional
    public UserResource updateUser(String id, UserAdd payload) {
        var existingUser = findUserByPublicId(id);
        try {
            existingUser = UserAddToUserMapper.map(payload, existingUser);
            var mergedUser = entityManager.merge(existingUser);
            return UserToUserResourceMapper.map(mergedUser);
        } catch (jakarta.persistence.PersistenceException ex) {
            throw new UserAlreadyExistsException(String.format("User %s already exists in system.", payload.userName));
        }
    }

    @Override
    @Transactional
    public UserResource patchUser(String id, UserPatch payload) {
        var user = findUserByPublicId(id);
        user = entityManager.merge(user);
        patchFactory.apply(user, payload.Operations);
        var mergedUser = entityManager.merge(user);
        return UserToUserResourceMapper.map(mergedUser);
    }

    private User findUserByPublicId(String id) {
        try {
            return queryFindUserByPublicId.setParameter("publicId", id).getSingleResult();
        } catch (NoResultException ex) {
            throw new UserUnknownException("public id:".concat(id));
        }
    }
}
