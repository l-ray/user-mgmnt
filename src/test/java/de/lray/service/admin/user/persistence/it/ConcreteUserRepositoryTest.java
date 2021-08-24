package de.lray.service.admin.user.persistence.it;

import de.lray.service.admin.Resources;
import de.lray.service.admin.common.Meta;
import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResultItem;
import de.lray.service.admin.user.operation.UserPatchFactory;
import de.lray.service.admin.user.persistence.ConcreteUserRepository;
import de.lray.service.admin.user.persistence.UserRepository;
import de.lray.service.admin.user.persistence.entities.Contact;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import de.lray.service.admin.user.persistence.mapper.UserToUserResourceMapper;
import de.lray.service.admin.user.persistence.mapper.UserToUserResultItemMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import org.assertj.core.api.Assertions;
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

import java.util.*;

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
                .addClasses(UserToUserResultItemMapper.class, UserToUserResourceMapper.class)
                .addPackage(ConcreteUserRepository.class.getPackage())
                .addClass(Resources.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private static final Map<String, String> USER_NAMES = Map.of(
                "Super", "Mario",
                "Katrin", "Arma",
                "Gerhard", "Buehr"
    );

    String existingUserId = null;

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
        underTest = new ConcreteUserRepository(em, utx);
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Credentials").executeUpdate();
        em.createQuery("delete from User").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (Map.Entry<String, String> aUser : USER_NAMES.entrySet()) {
            var creds = new Credentials();
            creds.setUsername(aUser.getKey().substring(0,1).concat(aUser.getValue()).toLowerCase());
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
                existingUserId = user.getPublicId();
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
        utx.commit();
    }

    @Test
    public void should_find_all_users() throws Exception {
        // when
        List<UserResultItem> retrievedGames = underTest.getUsers(new UserSearchCriteria());

        // then
        assertEquals(USER_NAMES.size(), retrievedGames.size());
        final Set<String> retrievedGameTitles = new HashSet<>();
        for (UserResultItem game : retrievedGames) {
            retrievedGameTitles.add(game.name.givenName);
        }
        assertTrue(retrievedGameTitles.containsAll(USER_NAMES.keySet()));
    }

    @Test
    public void whenItemsPerPageBeyondUserCount_returnsLimitedUserSet() {
        // given
        var criteriaLow = new UserSearchCriteria();
        criteriaLow.startIndex = 1;
        criteriaLow.count = 2;

        var criteriaHigh = new UserSearchCriteria();
        criteriaHigh.startIndex = criteriaLow.startIndex + 1;
        criteriaHigh.count = 2;

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
        var criteriaLow = new UserSearchCriteria();
        criteriaLow.startIndex = Integer.MAX_VALUE - 1;
        criteriaLow.count = 1;
        // When
        var results = underTest.getUsers(criteriaLow);
        //Then
        assertTrue(results.isEmpty());
    }

    @Test
    void whenSearchByKnownUserName_thenReturnSingleElementList() {
        // Given
        var criteria = new UserSearchCriteria();
        criteria.userName = "gbuehr";

        var result = underTest.getUsers(criteria);
        assertEquals(1,result.size());
    }

    @Test
    void whenSpecificUserNotExists_thenHandle() {
        assertThrows(UserUnknownException.class,() -> underTest.getUser("unknown"));
    }

    @Test
    void whenSearchByUnknownUserName_thenReturnEmpty() {
        // Given
        var criteria = new UserSearchCriteria();
        criteria.userName = "unknown";

        var result = underTest.getUsers(criteria);
        assertEquals(List.of(),result);
    }

    @Test
    void whenSearchById_returnsSingleResult() {
        // When
        var result = underTest.getUser(existingUserId);

        //Then
        assertEquals(existingUserId, result.id);
        assertEquals(USER_NAMES.keySet().iterator().next(), result.name.givenName);
    }
}