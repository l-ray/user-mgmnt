package de.lray.service.admin.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

class MetaTest {
    @Test
    void handles_primitive_issues() {
        Assertions.assertThat(new Meta()).isEqualTo(new Meta());
        Assertions.assertThat(new Meta().equals(null)).isFalse();
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new Meta()))
                .isEqualTo(fillDefaults(new Meta()));
    }

    @Test
    void when_differing_then_unequal() {
        var changes = Map.<String, Function<Meta, Object>>of(
                "differing created", (changed) -> changed.created = "anotherday",
                "differing last modified", (changed) -> changed.lastModified = "anotherday"
        );

        for (Map.Entry<String, Function<Meta, Object>> change : changes.entrySet()) {
            var changed = fillDefaults(new Meta());
            var action = change.getValue();
            action.apply(changed);
            Assertions.assertThat(fillDefaults(new Meta()))
                    .as(change.getKey())
                    .isNotEqualTo(changed);
        }
    }

    private Meta fillDefaults(Meta item) {
        item.created = "test";
        item.lastModified = "test";
        item.resourceType = Meta.ResourceTypeEnum.Group;
        return item;
    }
}