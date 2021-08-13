package de.lray.service.admin.user.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"java:S1104","java:S116"})
public class UserResult {

  public static final List<String> schemas  = Arrays.asList(
      "urn:ietf:params:scim:api:messages:2.0:ListResponse"
  );
  public Integer totalResults;
  public Integer startIndex;
  public Integer itemsPerPage;
  public List<UserResultItem> Resources = new ArrayList<>();
}
