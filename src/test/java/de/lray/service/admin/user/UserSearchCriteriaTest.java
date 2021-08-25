package de.lray.service.admin.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserSearchCriteriaTest {

    @Test
    void emptyObjectsEqual() {
        var first = UserSearchCriteria.builder()
                .setAllNull().build();
        var scnd = UserSearchCriteria.builder().setAllNull().build();
        assertEquals(first,scnd);
    }

    @Test
    void similarObjectsEqual() {
        assertEquals(builder().build(), builder().build());
    }

    @Test
    void differingObjectsFail() {
        var first = builder();
        var scnd = builder();
        scnd.setUserName("other");
        assertNotEquals(first.build(), scnd.build());
    }

    private UserSearchCriteria.Builder builder() {
        return UserSearchCriteria.builder()
        .setUserName("bla")
        .setStartIndex(1)
        .setCount(2)
        .setLastModifiedAfter(Date.from(Instant.ofEpochSecond(0)));
    }
}
