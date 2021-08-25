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
        var result = new User();
        result.setPublicId(payload.id);

        if (hasContactRelevantDate(payload)) {
            var aContact = new Contact();
            aContact.setFirstName(payload.name.givenName);
            aContact.setLastName(payload.name.familyName);
            trySettingEMail(payload, aContact);
            trySettingPhoneNumber(payload, aContact);
            result.setContact(aContact);
        }

        if (hasCredentialRelevantDate(payload)) {
            var creds = new Credentials();
            creds.setUsername(payload.userName);
            creds.setActive(payload.active);
            //creds.setPassword(payload.password);
            creds.setUser(result);
            result.setCredentials(creds);
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
}