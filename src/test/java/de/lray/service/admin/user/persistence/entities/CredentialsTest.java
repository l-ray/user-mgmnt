package de.lray.service.admin.user.persistence.entities;

import de.lray.service.admin.user.authentication.SimplePBKDF2Hasher;
import de.lray.service.admin.user.exception.UserCreationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class CredentialsTest {

    @Test
    void whenPasswordSet_thenSaltSet() {
        var underTest = new Credentials();
        underTest.setPassword("12345");
        Assertions.assertTrue(underTest.checkPassword("12345"));
        Assertions.assertFalse(underTest.checkPassword("ABCDEFG"));
    }

    @Test
    void whenCheckNullPw_thenHandle() {
        var underTest = new Credentials();
        underTest.setPassword("12345");
        Assertions.assertThrows(NullPointerException.class, () -> underTest.checkPassword(null));
    }

    @Test
    void whenHasherThrowsException_thenWrap() {
        var underTest = new Credentials() {
            @Override
            SimplePBKDF2Hasher createHasher(String aPW, String salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
                throw new InvalidKeySpecException();
            }
        };
        Assertions.assertThrows(UserCreationException.class, () -> underTest.checkPassword(null));
        Assertions.assertThrows(UserCreationException.class, () -> underTest.setPassword("bla"));
    }

    @Test
    void whenHasherThrowsAlgorithmException_thenWrap() {
        var underTest = new Credentials() {
            @Override
            SimplePBKDF2Hasher createHasher(String aPW, String salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
                throw new NoSuchAlgorithmException();
            }
        };
        Assertions.assertThrows(UserCreationException.class, () -> underTest.checkPassword(null));
        Assertions.assertThrows(UserCreationException.class, () -> underTest.setPassword("bla"));
    }

}