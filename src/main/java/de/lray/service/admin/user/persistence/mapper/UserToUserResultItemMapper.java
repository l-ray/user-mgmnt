package de.lray.service.admin.user.persistence.mapper;

import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.persistence.entities.Contact;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class UserToUserResultItemMapper {

    static final String REST_DATETIME_FORMAT = "dd-MM-yy hh:mm:ss";
    static final String CONTACT_TYPE_WORK_STRING = "work";

    private UserToUserResultItemMapper() { /* keep it static */ }

    public static UserResultItem map(User item) {
        var dateFormatter = new SimpleDateFormat(REST_DATETIME_FORMAT);
        var result = mapToResultDto(item, new UserResultItem());
        result.meta.lastModified = dateFormatter.format(item.getUpdateDate());
        result.meta.created = dateFormatter.format(item.getCreationDate());

        return result;
    }

    protected static UserResultItem mapToResultDto(User item, UserResultItem result) {
        Objects.requireNonNull(item.getCredentials(), "No credential object on user " + item.getId());

        result = result == null ? new UserResultItem() : result;
        result.id = item.getPublicId();

        mapCredentials(result, item.getCredentials());
        mapBirthDate(result, item.getContact());

        result.name = mapUserName(item.getContact());

        result.emails = mapEmails(item.getContact());
        result.phoneNumbers = mapPhones(item.getContact());

        // result.roles = item.Roles.stream().map((elt) -> elt.Role.DisplayName).toList()
        return result;
    }

    private static void mapBirthDate(UserResultItem result, Contact contact) {
        if (contact != null && contact.getBirthDate() != null) {
            if (result.extension == null) {
                result.extension = new UserExtension();
            }
            result.extension.birthDate = contact.getBirthDate();
        }
    }

    private static void mapCredentials(UserResultItem result, Credentials credentials) {
        result.userName = credentials.getUsername();
        result.active = Objects.requireNonNullElse(credentials.isActive(), true)
                && !Objects.requireNonNullElse(credentials.isLocked(), false);
    }

    private static List<UserPhone> mapPhones(Contact contact) {
        var item = new UserPhone();
        item.type = CONTACT_TYPE_WORK_STRING;
        item.value = contact == null ? null : contact.getPhoneNumber();
        return (item.value == null) ? Collections.emptyList() : List.of(item);
    }

    private static List<UserEmail> mapEmails(Contact contact) {
        var item = new UserEmail();
        item.primary = true;
        item.type = CONTACT_TYPE_WORK_STRING;
        item.value = contact == null ? null : contact.getPrimaryEMail();
        return (item.value == null) ? Collections.emptyList() : List.of(item);
    }

    private static UserName mapUserName(Contact contact) {
        if (contact != null) {
            var item = new UserName();
            item.givenName = contact.getFirstName();
            item.familyName = contact.getLastName();
            return item;
        } else {
            return null;
        }
    }
}
