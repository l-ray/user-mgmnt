package de.lray.service.admin.user.authentication;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

public class SimplePBKDF2Hasher {

    public static final String PBKDF_2_WITH_HMAC_SHA_1 = "PBKDF2WithHmacSHA1";
    final String password;

    final byte[] salt;
    final byte[] hash;

    public static SimplePBKDF2Hasher of(String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        return SimplePBKDF2Hasher.of(password, null);
    }

    public static SimplePBKDF2Hasher of(String password, String saltAsString)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        final var hasher = new SimplePBKDF2Hasher(password, saltAsString);
        return hasher;
    }

    private SimplePBKDF2Hasher(String password, String saltAsString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Objects.requireNonNull(password);
        this.password = password;
        this.salt = saltAsString == null
                ? generateSalt()
                : Base64.getDecoder().decode(saltAsString);
        this.hash = generatePasswordHash();
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] freshSalt = new byte[16];
        random.nextBytes(freshSalt);
        return freshSalt;
    }

    private byte[] generatePasswordHash() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_1);
        return factory.generateSecret(spec).getEncoded();
    }

    public String getSalt() {
        return Base64.getEncoder().encodeToString(salt);
    }

    public String getHash() {
        return Base64.getEncoder().encodeToString(hash);
    }
}
