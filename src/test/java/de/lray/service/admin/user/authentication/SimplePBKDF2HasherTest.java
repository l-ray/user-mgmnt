package de.lray.service.admin.user.authentication;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class SimplePBKDF2HasherTest {

    public static final String DEFAULT_PW = "myPassword";

    @Test
    void hashPasswordDifferingSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Assertions.assertThat(SimplePBKDF2Hasher.of(DEFAULT_PW).getHash())
                .isNotEqualTo(SimplePBKDF2Hasher.of(DEFAULT_PW).getHash());

        Assertions.assertThat(SimplePBKDF2Hasher.of(DEFAULT_PW).getHash())
                .isNotEqualTo(SimplePBKDF2Hasher.of(DEFAULT_PW, "someSalt").getHash());
    }

    @Test
    void hashPasswordSameSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var underTest = SimplePBKDF2Hasher.of(DEFAULT_PW);
        var underTest2 = SimplePBKDF2Hasher.of(DEFAULT_PW, underTest.getSalt());
        Assertions.assertThat(underTest.getHash())
                .isEqualTo(underTest2.getHash());
    }

    @Test
    void hashDifferentPasswordSameSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var underTest = SimplePBKDF2Hasher.of("abcdefg");
        var underTest2 = SimplePBKDF2Hasher.of("1234567", underTest.getSalt());
        Assertions.assertThat(underTest.getHash()).isNotEqualTo(underTest2.getHash());
    }
}