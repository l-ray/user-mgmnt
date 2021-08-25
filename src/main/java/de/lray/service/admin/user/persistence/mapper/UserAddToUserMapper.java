package de.lray.service.admin.user.persistence.mapper;

import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.persistence.entities.Contact;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UserAddToUserMapper {

    static final Logger LOGGER = LoggerFactory.getLogger(UserAddToUserMapper.class);

    private UserAddToUserMapper() { /* keep it static */ }

    public static User map(UserAdd payload) {
        var newUser = new User();
        // public id can not be reset once created
        newUser.setPublicId(payload.id);
        // acive/password are only set individually
        if (hasCredentialRelevantDate(payload)) {
            Credentials creds = createCredentialsForUser(newUser);
            creds.setActive(payload.active);
            //creds.setPassword(payload.password);
        }
        return map(payload, newUser);
    }

    public static User map(UserAdd payload, User result) {
        if (hasContactRelevantDate(payload)) {
            var aContact = new Contact();
            aContact.setFirstName(payload.name.givenName);
            aContact.setLastName(payload.name.familyName);
            trySettingEMail(payload, aContact);
            trySettingPhoneNumber(payload, aContact);
            result.setContact(aContact);
        }

        if (hasCredentialRelevantDate(payload)) {
            var creds = createCredentialsForUser(result);
            creds.setUsername(payload.userName);
        }
        // handle roles
        return result;
    }

    private static void trySettingPhoneNumber(UserAdd payload, Contact aContact) {
        try {
            aContact.setPhoneNumber(payload.phoneNumbers.get(0).value);
        } catch (NullPointerException npe) {
            LOGGER.info("registered user without phone number.");
        }
    }

    private static void trySettingEMail(UserAdd payload, Contact aContact) {
        try {
            aContact.setPrimaryEMail(payload.emails.get(0).value);
        } catch (NullPointerException npe) {
            LOGGER.info("registered user without email address.");
        }
    }

    private static boolean hasContactRelevantDate(UserAdd payload) {
        return (payload.name != null && payload.name.familyName != null)
                || (payload.emails != null && !payload.emails.isEmpty() && payload.emails.get(0).value!=null);
    }

    private static boolean hasCredentialRelevantDate(UserAdd payload) {
        return payload.userName != null;
    }

    private static Credentials createCredentialsForUser(User newUser) {
        var creds = newUser.getCredentials();
        if (creds == null) {
            creds = new Credentials();
            newUser.setCredentials(creds);
            creds.setUser(newUser);
        }
        return creds;
    }
}