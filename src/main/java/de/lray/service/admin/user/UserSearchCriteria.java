package de.lray.service.admin.user;

import java.util.Date;
import java.util.Objects;

public class UserSearchCriteria {
  String userName = null;
  Integer startIndex = 0;
  Integer count = Integer.MAX_VALUE;
  Date lastModifiedAfter = null;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserSearchCriteria that = (UserSearchCriteria) o;
    return Objects.equals(userName, that.userName)
            && Objects.equals(startIndex, that.startIndex)
            && Objects.equals(count, that.count)
            && Objects.equals(lastModifiedAfter, that.lastModifiedAfter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, startIndex, count, lastModifiedAfter);
  }
}
