package de.lray.service.admin.user;


import de.lray.service.admin.common.Meta;
import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.persistence.UserRepository;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.UriInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

class UserAdminResourceTest {


    @ParameterizedTest
    @CsvSource(value = {"|1|1", "|2|1"}, delimiter = '|')
    void returnsUsers(String filter, Integer startIndex, Integer count) throws ParseException {
        // Given
        UserRepository repo = Mockito.mock(UserRepository.class);

        List<UserResultItem> items = IntStream.range(0, count)
                .mapToObj(x -> {
                    UserResultItem item = new UserResultItem();
                    item.id = "12345678-" + x;
                    item.name = new UserName();
                    item.name.givenName = "Firstname " + x;
                    return item;
                }) // or x -> new Object(x).. or any other constructor
                .collect(Collectors.toList());
        when(repo.getUsers(Mockito.any())).thenReturn(items);

        // When
        UserResult result = new UserAdminResource(repo).getUsers(filter, startIndex, count);

        // Then
        Assertions.assertThat(result.schemas)
                .contains("urn:ietf:params:scim:api:messages:2.0:ListResponse");
        Assertions.assertThat(result.itemsPerPage)
                .as(new TextDescription("itemsPerPage")).isEqualTo(count);
        Assertions.assertThat(result.startIndex)
                .as(new TextDescription("startIndex")).isEqualTo(startIndex);
        Assertions.assertThat(result.totalResults)
                .as(new TextDescription("totalResults")).isEqualTo(count);

        Assertions.assertThat(result.Resources).hasSize(count);
    }


    @Test
    void returnsUsersNoParam() throws ParseException {
        var repo = mock(UserRepository.class);
        when(repo.getUsers(Mockito.any())).thenReturn(Arrays.asList(new UserResultItem()));

        // When
        var result = new UserAdminResource(repo).getUsers(null, null, null);

        // Then
        Assertions.assertThat((result).itemsPerPage)
                .as(new TextDescription("itemsPerPage")).isEqualTo(100);
        Assertions.assertThat((result).startIndex)
                .as(new TextDescription("startIndex")).isEqualTo(1);
        Assertions.assertThat((result).totalResults)
                .as(new TextDescription("startIndex")).isEqualTo(1);
        Assertions.assertThat((result).Resources).hasSize(1);
    }

    @Test
    void whenUsernameFilterGiven_returnResult() throws ParseException {
        // Given
        var expectedSearchCriteria = new UserSearchCriteria();
        expectedSearchCriteria.userName = "myemail@example.com";
        expectedSearchCriteria.startIndex = 0;
        expectedSearchCriteria.count = 1;
        expectedSearchCriteria.lastModifiedAfter = null;

        var expectedResult = new UserResultItem();

        var repo = mock(UserRepository.class);
        when(repo.getUsers(expectedSearchCriteria)).thenReturn(
                Arrays.asList(expectedResult)
        );

        // When
        var result = new UserAdminResource(repo).getUsers("userName eq \"myemail@example.com\"",0,1);

        // Then
        Assertions.assertThat(result.Resources).containsOnly(expectedResult);
    }


    @Test
    void whenLastModifiedFilterGiven_returnResult() throws ParseException {
        // Given
        var expectedSearchCriteria = new UserSearchCriteria();
        expectedSearchCriteria.userName = null;
        expectedSearchCriteria.startIndex = 0;
        expectedSearchCriteria.count = 1;
        expectedSearchCriteria.lastModifiedAfter = new GregorianCalendar(2020, Calendar.APRIL, 7, 14, 19, 34).getTime();

        var expectedResult = new UserResultItem();

        var repo = mock(UserRepository.class);
        when(repo.getUsers(expectedSearchCriteria)).thenReturn(Arrays.asList(expectedResult));

        // When
        var result = new UserAdminResource(repo).getUsers("meta.lastModified gt \"2020-04-07T14:19:34Z\"",0,1);

        // Then
        Assertions.assertThat(result.Resources).containsOnly(expectedResult);
    }


    @Test
    void returnsSpecificUser() {
        // Given
        UserRepository repo = mock(UserRepository.class);
        UserResource expected = new UserResource();
        expected.id = "abf4dd94";
        when(repo.getUser(any())).thenReturn(expected);
        // When
        var result = new UserAdminResource(repo).getUser("abf4dd94");
        // Then
        Assertions.assertThat(result.schemas)
                .contains("urn:ietf:params:scim:schemas:core:2.0:User");
        Assertions.assertThat(result.id)
                .as(new TextDescription("Result holds expected Id"))
                .isEqualTo("abf4dd94");
        Assertions.assertThat(result.meta.resourceType)
                .isEqualTo(Meta.ResourceTypeEnum.User);
    }

    @Test
    void failIfUserDoesNotExist() {
        // Given
        var repo = mock(UserRepository.class);
        when(repo.getUser(any())).thenThrow(UserUnknownException.class);
        var underTest = new UserAdminResource(repo);
        // When / Then
        Assertions.assertThatThrownBy(() -> underTest.getUser("unknown"))
                .isInstanceOf(UserUnknownException.class);
    }

    @Test
    void whenAddedUserExists_thenReturnConflict() {
        // Given
        var repo = mock(UserRepository.class);
        when(repo.addUser(any())).thenThrow(new UserAlreadyExistsException(""));
        var underTest = new UserAdminResource(repo);
        // When / Then
        Assertions.assertThatThrownBy(() -> underTest.addUser(mock(UserAdd.class)))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void whenAddedUserNew_thenReturn() {
        // Given
        var repo = mock(UserRepository.class);
        when(repo.addUser(any())).thenReturn(new UserResource());
        // When
        var result = new UserAdminResource(repo).addUser(mock(UserAdd.class));
        // Then
        Assertions.assertThat(result).isInstanceOf(UserResource.class);
    }

    @Test
    void updateUser() {
        // Given
        var userId = "d0dd58e43ded4293a61a8760fcba0458";
        var responseUser = new UserResource();
        responseUser.id = userId;

        var repo = mock(UserRepository.class);
        when(repo.updateUser(any(), any())).thenReturn(responseUser);

        // When
        var result = new UserAdminResource(repo).updateUser(userId, mock(UserAdd.class));
        // Then
        Assertions.assertThat(result).isInstanceOf(UserResource.class);
        Assertions.assertThat(result)
                .hasFieldOrPropertyWithValue("id", userId);
    }


    @Test
    void whenUpdateUserNotFound_thenRespond() {
        // Given
        var userId = "d0dd58e43ded4293a61a8760fcba0458";
        var repo = mock(UserRepository.class);
        when(repo.updateUser(any(), any())).thenThrow(new UserUnknownException(""));
        var underTest = new UserAdminResource(repo);
        // When / Then
        Assertions.assertThatThrownBy(() -> underTest.updateUser(userId, mock(UserAdd.class)))
                .isInstanceOf(UserUnknownException.class);
    }


    @Test
    void whenPatchUserToActive_thenReturn() {
        // Given
        var repo = mock(UserRepository.class);
        when(repo.patchUser(any(), any())).thenReturn(new UserResource());
        // When
        var result = new UserAdminResource(repo).patchUser("12345", mock(UserPatch.class));
        // Then
        Assertions.assertThat(result).isInstanceOf(UserResource.class);
    }

    @Test
    void whenPatchUserNotFound_thenRespond() {
        // Given
        var userId = "d0dd58e43ded4293a61a8760fcba0458";
        var repo = mock(UserRepository.class);
        when(repo.patchUser(any(), any())).thenThrow(new UserUnknownException(""));
        var underTest = new UserAdminResource(repo);
        // When / Then
        Assertions.assertThatThrownBy(() -> underTest.patchUser(userId, mock(UserPatch.class)))
                .isInstanceOf(UserUnknownException.class);
    }
}