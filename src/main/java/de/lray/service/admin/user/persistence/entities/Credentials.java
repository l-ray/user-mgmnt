package de.lray.service.admin.user.persistence.entities;

import de.lray.service.admin.user.authentication.SimplePBKDF2Hasher;
import de.lray.service.admin.user.exception.UserCreationException;
import jakarta.persistence.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Entity
@Table(name = "credentials")
public class Credentials extends Keyable {

    @Column(unique = true)
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(columnDefinition = "boolean default true")
    private Boolean active;

    @Column(columnDefinition = "boolean default false")
    private Boolean locked;

    String password;

    String salt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public void setPassword(String aPW) {
        try {
            SimplePBKDF2Hasher hasher = createHasher(aPW, null);
            this.password = hasher.getHash();
            this.salt = hasher.getSalt();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new UserCreationException(e.getMessage());
        }
    }

    public boolean checkPassword(String aPW) {
        try {
            SimplePBKDF2Hasher hasher = createHasher(aPW, this.salt);
            return hasher.getHash().equals(this.password);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new UserCreationException(e.getMessage());
        }
    }

    SimplePBKDF2Hasher createHasher(String aPW, String salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return new SimplePBKDF2Hasher(aPW, salt);
    }
}