package de.lray.service.admin.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class UserResultItemTest {
    @Test
    @SuppressWarnings("java:S5838")
    void handles_primitive_issues() {
        Assertions.assertThat(new UserResultItem()).isEqualTo(new UserResultItem());
        Assertions.assertThat(new UserResultItem()).isNotEqualTo(null);
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new UserResultItem()))
                .isEqualTo(fillDefaults(new UserResultItem()));
    }

    @Test
    void when_differing_id_then_unequal() {
        var changes = Map.<String, Function<UserResultItem, Object>>of(
                "differing id", (changed) -> changed.id = "uk",
                "differing username", (changed) -> changed.userName = "uk",
                "differing name", (changed) -> changed.name = new UserName(),
                "differing email", (changed) -> changed.emails = Collections.emptyList(),
                "differing phone", (changed) -> changed.phoneNumbers = Collections.emptyList(),
                "differing roles", (changed) -> changed.roles = Collections.emptyList()
        );

        for (Map.Entry<String, Function<UserResultItem, Object>> change : changes.entrySet()) {
            var changed = fillDefaults(new UserResultItem());
            var action = change.getValue();
            action.apply(changed);
            Assertions.assertThat(fillDefaults(new UserResultItem()))
                    .as(change.getKey())
                    .isNotEqualTo(changed);
        }
    }

    private UserResultItem fillDefaults(UserResultItem item) {
        item.id = "5";
        item.userName = "bla";
        item.name = new UserName("a", "b", "c");
        item.emails = List.of(new UserEmail());
        item.phoneNumbers = List.of(new UserPhone());
        item.roles = List.of("dev");
        return item;
    }
}
