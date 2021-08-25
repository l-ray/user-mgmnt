package de.lray.service.admin.user;

import java.util.Date;
import java.util.Objects;

public class UserSearchCriteria {

    public static class Builder {
        public UserSearchCriteria build() {
            return new UserSearchCriteria(this);
        }

        private String userName = null;
        private Integer startIndex = 1;
        private Integer count = Integer.MAX_VALUE;
        private Date lastModifiedAfter = null;

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setStartIndex(Integer startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder setCount(Integer count) {
            this.count = count;
            return this;
        }

        public Builder setLastModifiedAfter(Date lastModifiedAfter) {
            this.lastModifiedAfter = lastModifiedAfter;
            return this;
        }

        public Builder setAllNull() {
            setUserName(null);
            setCount(null);
            setStartIndex(null);
            setLastModifiedAfter(null);
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final String userName;
    private final Integer startIndex;
    private final Integer count;
    private final Date lastModifiedAfter;

    private UserSearchCriteria(Builder builder) {
        userName = builder.userName;
        startIndex = builder.startIndex;
        count = builder.count;
        lastModifiedAfter = builder.lastModifiedAfter;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Integer getCount() {
        return count;
    }

    public Date getLastModifiedAfter() {
        return lastModifiedAfter;
    }

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
