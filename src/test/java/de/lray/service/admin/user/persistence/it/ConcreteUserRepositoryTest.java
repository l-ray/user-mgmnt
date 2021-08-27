package de.lray.service.admin.user.persistence.it;

import de.lray.service.admin.Resources;
import de.lray.service.admin.ScimTestMessageFactory;
import de.lray.service.admin.common.Meta;
import de.lray.service.admin.user.UserAlreadyExistsException;
import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.operation.UserPatchFactory;
import de.lray.service.admin.user.operation.UserPatchOpAction;
import de.lray.service.admin.user.persistence.ConcreteUserRepository;
import de.lray.service.admin.user.persistence.UserRepository;
import de.lray.service.admin.user.persistence.entities.Contact;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import de.lray.service.admin.user.persistence.mapper.UserAddToUserMapper;
import de.lray.service.admin.user.persistence.mapper.UserToUserResourceMapper;
import de.lray.service.admin.user.persistence.mapper.UserToUserResultItemMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ArquillianExtension.class)
class ConcreteUserRepositoryTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(User.class.getPackage())
                .addPackage(UserResource.class.getPackage())
                .addPackage(Meta.class.getPackage())
                .addPackage(UserSearchCriteria.class.getPackage())
                .addPackage(UserPatchFactory.class.getPackage())
                .addClasses(UserToUserResultItemMapper.class, UserToUserResourceMapper.class, UserAddToUserMapper.class)
                .addPackage(ConcreteUserRepository.class.getPackage())
                .addClass(Resources.class)
                .addClass(ScimTestMessageFactory.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private static final Map<String, String> USER_NAMES = Map.of(
            "Super", "Mario",
            "Katrin", "Arma",
            "Gerhard", "Buehr"
    );

    String existingUserPublicId = null;
    String existingUserName = null;

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    UserRepository underTest;

    @BeforeEach
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
        underTest = new ConcreteUserRepository(em, utx, new UserPatchFactory());
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        em.flush();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Credentials").executeUpdate();
        em.createQuery("delete from User").executeUpdate();
        em.createQuery("delete from Contact ").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (Map.Entry<String, String> aUser : USER_NAMES.entrySet()) {
            var creds = new Credentials();
            creds.setUsername(aUser.getKey().substring(0, 1).concat(aUser.getValue()).toLowerCase());
            creds.setActive(true);
            var contact = new Contact();
            contact.setFirstName(aUser.getKey());
            contact.setLastName(aUser.getValue());
            User user = new User();
            creds.setUser(user);
            user.setContact(contact);
            em.persist(contact);
            em.persist(user);
            em.persist(creds);
            if (aUser.getKey().equals(USER_NAMES.keySet().iterator().next())) {
                em.refresh(user);
                existingUserPublicId = user.getPublicId();
                existingUserName = user.getCredentials().getUsername();
            }
        }
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @AfterEach
    public void commitTransaction() throws Exception {
        try {
            utx.commit();
        } catch (RollbackException re) {
            // transaction rollback happened in test case
        }
    }

    @Test
    public void should_find_all_users() throws Exception {
        // when
        List<UserResultItem> retrievedUser = underTest.getUsers(UserSearchCriteria.builder().build());

        // then
        assertEquals(USER_NAMES.size(), retrievedUser.size());
        final Set<String> retrievedNames = new HashSet<>();
        for (UserResultItem game : retrievedUser) {
            retrievedNames.add(game.name.givenName);
        }
        assertTrue(retrievedNames.containsAll(USER_NAMES.keySet()));
    }

    @Test
    public void whenItemsPerPageBeyondUserCount_returnsLimitedUserSet() {
        // given
        var criteriaLow = UserSearchCriteria.builder()
                .setStartIndex(1)
                .setCount(2).build();

        var criteriaHigh = UserSearchCriteria.builder()
                .setStartIndex(criteriaLow.getStartIndex() + 1)
                .setCount(2).build();

        // When
        var results = underTest.getUsers(criteriaLow);
        var resultOverlap = underTest.getUsers(criteriaHigh);

        //Then
        assertEquals(2, results.size());
        assertEquals(2, resultOverlap.size());
        assertEquals(resultOverlap.get(0), results.get(1));
        assertNotEquals(resultOverlap.get(1), results.get(0));
    }

    @Test
    void whenItemsPerPageBeyondUserCount_returnsEmptyUserSet() {
        // Given
        var criteriaLow = UserSearchCriteria.builder()
                .setStartIndex(Integer.MAX_VALUE - 1)
                .setCount(1).build();
        // When
        var results = underTest.getUsers(criteriaLow);
        //Then
        assertTrue(results.isEmpty());
    }

    @Test
    void whenSearchByKnownUserName_thenReturnSingleElementList() {
        // Given
        var criteria = UserSearchCriteria.builder()
                .setUserName(existingUserName).build();

        var result = underTest.getUsers(criteria);
        assertEquals(1, result.size());
    }

    @Test
    void whenSpecificUserNotExists_thenHandle() {
        assertThrows(UserUnknownException.class, () -> underTest.getUser("unknown"));
    }

    @Test
    void whenSearchByUnknownUserName_thenReturnEmpty() {
        // Given
        var criteria = UserSearchCriteria.builder()
                .setUserName("unknown").build();

        var result = underTest.getUsers(criteria);
        assertEquals(List.of(), result);
    }

    @Test
    void whenSearchById_returnsSingleResult() {
        // When
        var result = underTest.getUser(existingUserPublicId);

        //Then
        assertEquals(existingUserPublicId, result.id);
        assertEquals(USER_NAMES.keySet().iterator().next(), result.name.givenName);
    }

    @Test
    void whenSearchByLastModified_thenReturn() {
        //Given
        var referenceDate = Date.from(LocalDate.now().minusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC));
        em.createQuery("update User u set u.updateDate = :futureDate where not u.publicId = :publicId")
                .setParameter("futureDate", referenceDate)
                .setParameter("publicId", existingUserPublicId)
                .executeUpdate();

        // When
        var result = underTest.getUsers(
                UserSearchCriteria.builder()
                        .setLastModifiedAfter(referenceDate)
                        .build()
        );
        // Then
        var resultPublicId = result
                .stream().map(item -> item.id)
                .collect(Collectors.toList());
        assertEquals(List.of(existingUserPublicId), resultPublicId);
    }

    @Test
    void whenCreateValidUser_thenReturn() {
        // Given
        var userName = "myNewUser";
        var user = ScimTestMessageFactory.createUserAdd();
        user.id = null;
        user.userName = userName;
        user.setPassword("12345abcde");
        // When
        var resultingUser = underTest.addUser(user);

        // Then
        assertNotNull(resultingUser);
        assertEquals(userName, resultingUser.userName);
        assertNotNull(resultingUser.id);

        var dbUser = getDbUserByPublicId(resultingUser.id);

        assertNotNull(dbUser);
        assertEquals(user.userName, dbUser.getCredentials().getUsername());
        assertEquals(user.name.givenName, dbUser.getContact().getFirstName());
        assertEquals(user.name.familyName, dbUser.getContact().getLastName());
    }

    @Test
    void whenCreateExistingUser_thenError() throws SystemException, NotSupportedException {
        // Given
        var userName = existingUserName;
        var user = ScimTestMessageFactory.createUserAdd();
        user.id = null;
        user.userName = userName;
        user.setPassword("12345abcde");
        // When / Then
        assertThrows(UserAlreadyExistsException.class, () -> underTest.addUser(user));
    }

    @Test
    void whenUserNameChange_thenDo() {
        // Given
        var userId = existingUserPublicId;
        var newUserName = "newUserName";
        var userDto = ScimTestMessageFactory.createUserAdd();
        userDto.userName = newUserName;

        // When
        var result = underTest.updateUser(userId, userDto);

        // Then
        assertEquals(newUserName, result.userName);
        var dbUser = getDbUserByPublicId(existingUserPublicId);
        assertEquals(newUserName, dbUser.getCredentials().getUsername());
    }

    @Test
    void whenPwOrStatusByUpdate_thenIgnore() {
        // Given
        //var dbUser = getDbUserByPublicId(existingUserPublicId);
        var user = existingUserPublicId;
        //var oldPw = dbUser.getCredentials().getPassword();
        var userDto = ScimTestMessageFactory.createUserAdd();
        userDto.id = existingUserPublicId;
        userDto.active = false;
        userDto.setPassword( "xxx");

        // When
        var result = underTest.updateUser(
                existingUserPublicId,
                userDto
        );

        // Then
        //assertEquals(oldPw, getDbUserByPublicId(existingUserPublicId).getCredentials().getPassword());
        assertTrue(result.active);
        assertTrue(getDbUserByPublicId(existingUserPublicId).getCredentials().isActive());
    }

    @Test
    void whenUserActiveChange_thenDo() {
        // Given
        var userPatch = new UserPatch();
        var userPatchOp = new UserPatchOp();
        var patchOpVal = new UserPatchOpValues();
        patchOpVal.active = false;
        userPatchOp.op = UserPatchOpAction.replace;
        userPatchOp.value = patchOpVal;
        userPatch.Operations = Arrays.asList(userPatchOp);

        // When
        var result = underTest.patchUser(existingUserPublicId, userPatch);

        // Then
        assertFalse(result.active);
        var dbUser = getDbUserByPublicId(existingUserPublicId);
        em.refresh(dbUser);
        em.refresh(dbUser.getCredentials());
        assertTrue(dbUser.getCredentials().isActive());
    }

    private User getDbUserByPublicId(String publicId) {
        return (User) em.createQuery("select u from User u where u.publicId = :publicId")
                .setParameter("publicId", publicId)
                .getSingleResult();
    }
}