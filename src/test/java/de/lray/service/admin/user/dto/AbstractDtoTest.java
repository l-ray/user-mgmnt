package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;

public abstract class AbstractDtoTest<T extends Object> {

    @Test
    @SuppressWarnings("java:S5838")
    void handles_primitive_issues() {
        Assertions.assertThat(newInstance()).isEqualTo(newInstance());
        Assertions.assertThat(newInstance()).isNotEqualTo(null);
    }

    @Test
    @SuppressWarnings({"java:S5838","java:S5863"})
    void handles_object_identity() {
        var newInstance = newInstance();
        Assertions.assertThat(newInstance).isEqualTo(newInstance);
    }

    protected abstract T newInstance();

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(newInstance()))
                .isEqualTo(fillDefaults(newInstance()));
    }

    @ParameterizedTest
    @MethodSource("potentialChangeActions")
    void when_differing_then_unequal(Pair<String, Function<T, Object>> aPair) {
        var action = aPair.getValue();
        var msg = aPair.getKey();
        var changed = fillDefaults(newInstance());
            action.apply(changed);
            Assertions.assertThat(fillDefaults(newInstance()))
                    .as(msg)
                    .isNotEqualTo(changed);
    }

    protected abstract T fillDefaults(T item);
}
