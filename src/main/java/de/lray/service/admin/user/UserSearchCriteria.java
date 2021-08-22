package de.lray.service.admin.user;

import java.util.Date;
import java.util.Objects;

public class UserSearchCriteria {
  public String userName = null;
  public Integer startIndex = 1;
  public Integer count = Integer.MAX_VALUE;
  public Date lastModifiedAfter = null;

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
