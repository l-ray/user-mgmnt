package de.lray.service.admin;

import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.persistence.patch.UserPatchOpAction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

public class ScimTestMessageFactory {

  public static final String EXAMPLE_ID = "d0dd58e43ded4293a61a8760fcba0458";

  private ScimTestMessageFactory() { }

  public static UserAdd createMinimalUserAdd() {
    var result = new UserAddWithPasswordGetter();
    result.userName = "k@rl.io";
    result.active = true;

    result.setPassword("aSecr€t1");
    return result;
  }

  public static UserAdd createUserAdd() {
    var result = new UserAddWithPasswordGetter();
    result.id = EXAMPLE_ID;
    result.userName = "bjensen@example.com";
    result.active = true;

    result.setPassword("aSecr€t1");

    result.name = new UserName();
    result.name.middleName = "the";
    result.name.givenName = "Barbara";
    result.name.familyName = "Jensen";

    var email = new UserEmail();
    email.value = "bjensen@example.com";
    email.primary = true;
    email.type = "work";
    email.display = "bjensen@example.com";

    result.emails = Collections.singletonList(email);

    var extension = new UserExtension();
    extension.birthDate = LocalDate.of(1957, 9,29);

    result.extension = extension;

    var phoneNumber = new UserPhone();
    phoneNumber.value = "+1 555-800-5729";
    phoneNumber.type = "work";
    result.phoneNumbers = Collections.singletonList(phoneNumber);

    result.roles = Collections.singletonList("Tools View");

    result.meta.lastModified = "04-17-2020 00:00:00";
    result.meta.created = "04-17-2020 00:00:00";

    result.displayName = " Steph Curry";
    result.preferredLanguage = "en";
    result.locale = "en-US";
    result.timezone = "America/Los_Angeles";
    return result;
  }

  public static UserPatch createUserPatch() {
    var result = new UserPatch();
    var pwOps = new UserPatchOp();
    var pwOpVal = new UserPatchOpValues();
    pwOpVal.active = false;
    pwOps.op = UserPatchOpAction.replace;
    pwOps.value = pwOpVal;
    result.Operations  = Arrays.asList(pwOps);
    return result;
  }

  public static class UserAddWithPasswordGetter extends UserAdd {
    // this way, produced payloads in tests reflect the password set.
    public String getPassword() {
      return this.password;
    }
  }
}

