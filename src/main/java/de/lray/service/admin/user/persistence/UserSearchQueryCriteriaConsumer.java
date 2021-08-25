package de.lray.service.admin.user.persistence;

import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Metamodel;

import java.util.function.Consumer;

public class UserSearchQueryCriteriaConsumer implements Consumer<UserSearchCriteria> {

    private Predicate predicate;

    private final CriteriaBuilder builder;
    private final Root r;
    private final Metamodel model;

    public UserSearchQueryCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<User> r, Metamodel model) {
        super();
        this.predicate = predicate;
        this.builder = builder;
        this.r = r;
        this.model = model;
    }

    @Override
    public void accept(UserSearchCriteria param) {

        if (param.getUserName() != null) {
            var join = r.join(model.entity(User.class).getSingularAttribute("credentials"));
            predicate = builder.and(predicate, builder.equal(
                    join.get(model.entity(Credentials.class).getSingularAttribute("username")),
                    param.getUserName()
                    )
            );
        }

        if (param.getLastModifiedAfter() != null) {
            predicate = builder.and(predicate, builder
                    .greaterThanOrEqualTo(r.get("updateTime"), param.getLastModifiedAfter()));
        }

    }

    public Predicate getPredicate() {
        return predicate;
    }

}
