package de.lray.service.admin.common.dto;

import de.lray.service.admin.user.dto.AbstractDtoTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

class MetaTest extends AbstractDtoTest<Meta> {

    @Override
    protected Meta newInstance() {
        return new Meta();
    }

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<Meta, Object>>of(
                        "differing created", (changed) -> changed.created = "anotherday"
                )),
                Arguments.of(Pair.<String, Function<Meta, Object>>of(
                        "differing last modified", (changed) -> changed.lastModified = "anotherday"
                )),
                Arguments.of(Pair.<String, Function<Meta, Object>>of(
                        "diff last resource type", (changed) -> changed.resourceType = Meta.ResourceTypeEnum.User
                ))
        );
    }

    protected Meta fillDefaults(Meta item) {
        item.created = "test";
        item.lastModified = "test";
        item.resourceType = Meta.ResourceTypeEnum.Group;
        return item;
    }
}