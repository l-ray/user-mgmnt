package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import jakarta.validation.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

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
        userPatchOp.value = Map.of();
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
        userPatchOp.op = UserPatchOpAction.replace;
        userPatchOp.value = Map.of(UserPatchOpField.active, false);
        // When
        new UserPatchFactory().apply(readOnlyUser, List.of(userPatchOp));
        // Then
        Mockito.verify(credentials, Mockito.times(1)).setActive(false);
    }

    @Test
    void whenWrongActivePatchOpValueType_thenError() {
        // Given
        var readOnlyUser = Mockito.mock(User.class);
        var credentials = Mockito.mock(Credentials.class);
        Mockito.when(readOnlyUser.getCredentials()).thenReturn(credentials);

        var userPatchOp = new UserPatchOp();
        userPatchOp.op = UserPatchOpAction.replace;
        userPatchOp.value = Map.of(UserPatchOpField.active, "waddehadde");
        var underTest = new UserPatchFactory();
        // When / Then
        var operations = List.of(userPatchOp);
        Assertions.assertThatThrownBy(() -> underTest.apply(readOnlyUser, operations))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("waddehadde")
                .hasMessageContaining("active");
    }
}
