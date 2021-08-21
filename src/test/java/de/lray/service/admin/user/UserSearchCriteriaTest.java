package de.lray.service.admin.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserSearchCriteriaTest {

    @Test
    void emptyObjectsEqual() {
        var first = new UserSearchCriteria();
        var scnd = new UserSearchCriteria();
        assertEquals(first,scnd);
    }

    @Test
    void similarObjectsEqual() {
        assertEquals(create(), create());
    }

    @Test
    void differingObjectsFail() {
        var first = create();
        var scnd = create();
        scnd.userName = "other";
        assertNotEquals(first, scnd);
    }

    private UserSearchCriteria create() {
        var item =  new UserSearchCriteria();
        item.userName = "bla";
        item.startIndex = 1;
        item.count = 2;
        item.lastModifiedAfter = Date.from(Instant.ofEpochSecond(0));
        return item;
    }
}
