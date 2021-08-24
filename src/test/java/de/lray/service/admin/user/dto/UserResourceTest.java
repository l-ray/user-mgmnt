package de.lray.service.admin.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

class UserResourceTest {

    @Test
    void handles_primitive_issues() {
        Assertions.assertThat(new UserResource()).isEqualTo(new UserResource());
        Assertions.assertThat(new UserResource()).isNotEqualTo(null);
        Assertions.assertThat(new UserResource()).isNotEqualTo(new UserResultItem());
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new UserResource()))
                .isEqualTo(fillDefaults(new UserResource()));
    }

    @Test
    void when_differing_then_unequal() {
            var changes = Map.<String, Function<UserResource, Object>>of(
                    "differing language", (changed) -> changed.preferredLanguage = "uk",
                    "differing locale", (changed) -> changed.locale = "uk",
                    "differing displayname", (changed) -> changed.displayName = "wha",
                    "differing super", (changed) -> changed.active = false
            );

            for (Map.Entry<String, Function<UserResource, Object>> change : changes.entrySet()) {
                var changed = fillDefaults(new UserResource());
                var action = change.getValue();
                action.apply(changed);
                Assertions.assertThat(fillDefaults(new UserResource()))
                        .as(change.getKey())
                        .isNotEqualTo(changed);
            }
        }

    private UserResource fillDefaults(UserResource item) {
        item.active = true;
        item.displayName = "test";
        item.preferredLanguage = "de";
        item.timezone = "UTC";
        item.locale = "DE_de";
        return item;
    }
}