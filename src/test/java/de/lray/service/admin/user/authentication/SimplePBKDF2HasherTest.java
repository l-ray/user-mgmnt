package de.lray.service.admin.user.authentication;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class SimplePBKDF2HasherTest {

    public static final String DEFAULT_PW = "myPassword";

    @Test
    void hashPasswordDifferingSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Assertions.assertThat(new SimplePBKDF2Hasher(DEFAULT_PW).getHash())
                .isNotEqualTo(new SimplePBKDF2Hasher(DEFAULT_PW).getHash());

        Assertions.assertThat(new SimplePBKDF2Hasher(DEFAULT_PW).getHash())
                .isNotEqualTo(new SimplePBKDF2Hasher(DEFAULT_PW, "someSalt").getHash());
    }

    @Test
    void hashPasswordSameSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var underTest = new SimplePBKDF2Hasher(DEFAULT_PW);
        var underTest2 = new SimplePBKDF2Hasher(DEFAULT_PW, underTest.getSalt());
        Assertions.assertThat(underTest.getHash())
                .isEqualTo(underTest2.getHash());
    }

    @Test
    void hashDifferentPasswordSameSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var underTest = new SimplePBKDF2Hasher("abcdefg");
        var underTest2 = new SimplePBKDF2Hasher("1234567", underTest.getSalt());
        Assertions.assertThat(underTest.getHash()).isNotEqualTo(underTest2.getHash());
    }
}