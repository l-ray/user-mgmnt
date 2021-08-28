package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

class UserAddTest extends AbstractDtoTest<UserAdd> {

    @Override
    protected UserAdd newInstance() {
        return new UserAdd();
    }

    @Test
    void handles_inheritance_issue() {
        Assertions.assertThat(new UserAdd()).isNotEqualTo(new UserResource());
    }

    static Stream<Arguments> potentialChangeActions() {
        return Stream.<Arguments>of(
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "differing password", (changed) -> changed.password = "!UK12345"
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "differing super", (changed) -> changed.preferredLanguage = "uk"
                ))
        );
    }

    public UserAdd fillDefaults(UserAdd item) {
        item.password = "DE0007!";
        item.preferredLanguage = "de";
        return item;
    }
}