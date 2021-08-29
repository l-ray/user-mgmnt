package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Stream;

class UserExtensionTest extends AbstractDtoTest<UserExtension> {

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<UserExtension, Object>>of(
                        "differing birth date", (changed) -> changed.birthDate = null))
        );
    }

    @Override
    protected UserExtension newInstance() {
        return new UserExtension();
    }

    protected UserExtension fillDefaults(UserExtension item) {
        item.birthDate = LocalDate.of(2011,3,11);
        return item;
    }
}
