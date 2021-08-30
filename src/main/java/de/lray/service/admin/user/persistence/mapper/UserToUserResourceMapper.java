package de.lray.service.admin.user.persistence.mapper;

import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.persistence.entities.User;

import java.util.Calendar;
import java.util.Locale;

public abstract class UserToUserResourceMapper {

    private UserToUserResourceMapper() { /* keep it static */ }

    public static UserResource map(User item) {
        var result = new UserResource();
        UserToUserResultItemMapper.mapToResultDto(item, result);
        result.displayName = result.name != null
                ? result.name.givenName.concat(" ").concat(result.name.familyName)
                : "unknown - ".concat(result.id);
        result.preferredLanguage = "DE";
        result.locale = Locale.getDefault().toString();
        result.timezone = Calendar.getInstance().getTimeZone().getID();
        return result;
    }
}
