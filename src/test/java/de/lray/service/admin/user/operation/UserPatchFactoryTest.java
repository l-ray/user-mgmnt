package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.dto.UserPatchOpValues;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class UserPatchFactoryTest {

    @Test
    void should_handle_empty_op_list() {
        // When / Then
        var underTest = new UserPatchFactory();
        Assertions.assertThatNoException().isThrownBy(() ->
                underTest.apply(Mockito.mock(User.class), List.of())
        );
    }

    @Test
    void whenEmptyOp_thenException() {
        // Given
        var readOnlyUser = Mockito.mock(User.class);
        var userPatchOp = new UserPatchOp();
        userPatchOp.value = new UserPatchOpValues();
        // When / Then
        var underTest = new UserPatchFactory();
        var operations = List.of(userPatchOp);
        Assertions.assertThatThrownBy(() -> underTest.apply(readOnlyUser, operations))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    void whenActivePatchOpValue_thenSet() {
        // Given
        var readOnlyUser = Mockito.mock(User.class);
        var credentials = Mockito.mock(Credentials.class);
        Mockito.when(readOnlyUser.getCredentials()).thenReturn(credentials);

        var userPatchOp = new UserPatchOp();
        var patchOpVal = new UserPatchOpValues();
        patchOpVal.active = false;
        userPatchOp.op = UserPatchOpAction.replace;
        userPatchOp.value = patchOpVal;
        // When
        new UserPatchFactory().apply(readOnlyUser, List.of(userPatchOp));
        // Then
        Mockito.verify(credentials, Mockito.times(1)).setActive(false);
    }

    @Test
    void whenPasswordPatchOpValue_thenSet() {
        // Given
        var newPw = "Pa$$w0r|)";
        var readOnlyUser = Mockito.mock(User.class);
        var credentials = Mockito.mock(Credentials.class);
        Mockito.when(credentials.checkPassword(newPw)).thenReturn(false);
        Mockito.when(readOnlyUser.getCredentials()).thenReturn(credentials);

        var userPatchOp = new UserPatchOp();
        var patchOpVal = new UserPatchOpValues();
        patchOpVal.password = newPw;
        userPatchOp.op = UserPatchOpAction.replace;
        userPatchOp.value = patchOpVal;
        // When
        new UserPatchFactory().apply(readOnlyUser, List.of(userPatchOp));
        // Then
        Mockito.verify(credentials, Mockito.times(1)).setPassword(newPw);
        Mockito.verify(credentials, Mockito.times(1)).checkPassword(newPw);
    }

    @Test
    void whenPasswordPatchOpValueEqualExistingPw_thenFail() {
        // Given
        var newPw = "Pa$$w0r|)";
        var readOnlyUser = Mockito.mock(User.class);
        var credentials = Mockito.mock(Credentials.class);
        Mockito.when(credentials.checkPassword(newPw)).thenReturn(true);
        Mockito.when(readOnlyUser.getCredentials()).thenReturn(credentials);

        var userPatchOp = new UserPatchOp();
        var patchOpVal = new UserPatchOpValues();
        patchOpVal.password = newPw;
        userPatchOp.op = UserPatchOpAction.replace;
        userPatchOp.value = patchOpVal;
        var underTest = new UserPatchFactory();
        var operations = List.of(userPatchOp);
        // When / Then
        Assertions.assertThatThrownBy(() -> underTest.apply(readOnlyUser, operations))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
