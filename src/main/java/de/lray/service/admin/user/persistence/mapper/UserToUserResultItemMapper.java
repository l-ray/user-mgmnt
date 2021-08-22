package de.lray.service.admin.user.persistence.mapper;

import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.persistence.entities.Contact;
import de.lray.service.admin.user.persistence.entities.User;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class UserToUserResultItemMapper {

    static final String REST_DATETIME_FORMAT = "dd-MM-yy hh:mm:ss";
    static final String CONTACT_TYPE_WORK_STRING = "work";

    private UserToUserResultItemMapper() {
    }

    public static UserResultItem map(User item) {
        var dateFormatter = new SimpleDateFormat(REST_DATETIME_FORMAT);
        var result = mapToResultDto(item, new UserResource());
        result.meta.lastModified = dateFormatter.format(item.getUpdateDate());
        result.meta.created = dateFormatter.format(item.getCreationDate());

    /*
    result.displayName = item.DisplayName
    result.preferredLanguage = item.UserLanguage?.Code
    result.locale = item.Locale?.Code
    result.timezone = item.TimeZone?.DisplayName
    */
        return result;
    }

    private static UserResultItem mapToResultDto(User item) {
        return mapToResultDto(item, null);
    }

    private static UserResultItem mapToResultDto(User item, UserResultItem result) {
        Objects.requireNonNull(item.getContact(), "No contact object on user " + item.getId());
        Objects.requireNonNull(item.getCredentials(), "No credential object on user " + item.getId());

        result = result == null ? new UserResultItem() : result;
        result.id = item.getId().toString();

        var credentials = item.getCredentials();

        result.userName = credentials.getUsername();
        result.active = credentials.isActive() && !credentials.isLocked();
        result.name = mapUserName(item.getContact());

        result.emails = mapEmails(item.getContact());
        result.phoneNumbers = mapPhones(item.getContact());

        // result.roles = item.Roles.map(\elt -> elt.Role.DisplayName).toList()
        return result;
    }

    private static List<UserPhone> mapPhones(Contact contact) {
        var item = new UserPhone();
        item.type = CONTACT_TYPE_WORK_STRING;
        item.value = contact.getPhoneNumber();
        return (item.value == null) ? Collections.emptyList() : List.of(item);
    }

    private static List<UserEmail> mapEmails(Contact contact) {
        var item = new UserEmail();
        item.primary = true;
        item.type = CONTACT_TYPE_WORK_STRING;
        item.value = contact.getPrimaryEMail();
        return (item.value == null) ? Collections.emptyList() : List.of(item);
    }

    private static UserName mapUserName(Contact contact) {
        var item = new UserName();
        item.givenName = contact.getFirstName();
        // item.middleName = user.MiddleName;
        item.familyName = contact.getLastName();
        return item;

    }
}