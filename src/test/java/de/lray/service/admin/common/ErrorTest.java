package de.lray.service.admin.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorTest {

    @Test
    public void testNotFoundErrorWithoutDetail() {
        var error = Error.notFound(null);
        assertThat(error.status).isEqualTo(404);
        assertThat(error.detail).contains("404");
    }

    @Test
    public void testNotFoundMessage() {
        var error = Error.notFound("bla");
        assertThat(error.status).isEqualTo(404);
        assertThat(error.detail).contains("bla");
    }

    @Test
    public void testAlreadyExistsErrorWithoutDetail() {
        var error = Error.alreadyExists(null);
        assertThat(error.status).isEqualTo(409);
        assertThat(error.detail).contains("409");
    }

    @Test
    public void testAlreadyExistsMessage() {
        var error = Error.alreadyExists("bla");
        assertThat(error.status).isEqualTo(409);
        assertThat(error.detail).contains("bla");
    }

    @Test
    public void testInvalidArgumentErrorWithoutDetail() {
        var error = Error.invalidArgument(null);
        assertThat(error.status).isEqualTo(400);
        assertThat(error.detail).contains("400");
    }

    @Test
    public void testInvalidArgumentMessage() {
        var error = Error.invalidArgument("bla");
        assertThat(error.status).isEqualTo(400);
        assertThat(error.detail).contains("bla");
    }

}