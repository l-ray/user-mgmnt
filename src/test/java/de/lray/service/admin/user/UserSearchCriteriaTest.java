package de.lray.service.admin.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserSearchCriteriaTest {

    @Test
    public void emptyObjectsEqual() {
        var first = new UserSearchCriteria();
        var scnd = new UserSearchCriteria();
        assertTrue(first.equals(scnd));
    }

    @Test
    public void similarObjectsEqual() {
        assertTrue(create().equals(create()));
    }

    @Test
    public void differingObjectsFail() {
        var first = create();
        var scnd = create();
        scnd.userName = "other";
        assertFalse(first.equals(scnd));
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
