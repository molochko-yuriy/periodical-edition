package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Subscription;
import by.epamtc.periodical_edition.entity.User;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import by.epamtc.periodical_edition.repository.ReviewRepository;
import by.epamtc.periodical_edition.repository.SubscriptionRepository;
import by.epamtc.periodical_edition.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryImplTest extends BaseRepositoryTest {
    private UserRepository userRepository;
    private List<User> users;

    public UserRepositoryImplTest(){
        users = new ArrayList<>();
        userRepository = new UserRepositoryImpl(getConnectionPool());
        users.add(new User(1L, "Александр", "Степанов", "stepanow.a@mail.ru", "1111","8684758965", "stepanow.a@mail.ru", 235));
        users.add(new User(2L, "Виктор", "Александров", "alecsandrow.a@mail.ru", "2222",
                "985214","alecsandrow.a@mail.ru", 25));
    }


    @Test
    public void findById_validData_shouldReturnUser() {
        //given
        User expected = users.get(0);

        //when
        User actual = userRepository.findById(1L);

         //then
        assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() {
        //given && when
        final List<User> actual = userRepository.findAll();

        //then
        assertEquals(users, actual);
    }


    @Test
    public void add_validData_shouldAddNewUser() {
        //given
        User expected = new User(3L, "Oleg", "Petrov", "rider", "580236", "+375295899663", "oleg.p@mail.ru", 85);
        User actual = new User(null,"Oleg", "Petrov", "rider", "580236", "+375295899663", "oleg.p@mail.ru", 85);

        // when
        boolean isAdded = userRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(expected, actual);
        assertEquals(expected, userRepository.findById(actual.getId()));
    }

    @Test
    public void update_validData_shouldUpdateUser() {
        //given
        User expected = new User(2L, "Petrov", "Oleg", "rider", "158963","+375291548544", "petrov.a@mail.ru", 85);
        User actual = userRepository.findById(2L);

        Assert.assertEquals(users.get(1), actual);

        //when
        actual.setId(2L);
        actual.setLastName("Petrov");
        actual.setFirstName("Oleg");
        actual.setLogin("rider");
        actual.setPassword("158963");
        actual.setMobilePhone("+375291548544");
        actual.setEmail("petrov.a@mail.ru");
        actual.setBalance(85);
        boolean isUpdated = userRepository.update(actual);

        //then
        assertTrue(isUpdated);
        assertEquals(expected, actual);
        assertEquals(expected, userRepository.findById(actual.getId()));
    }



    @Test
    public void delete_validData_shouldDeleteUser() {
        SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl(getConnectionPool());

        ReviewRepository reviewRepository = new ReviewRepositoryImpl(getConnectionPool());
        //given
        User actual = userRepository.findById(1L);

        Subscription actual1 =subscriptionRepository.findById(1L);

        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByUserId(1L);

        // when
        boolean result = userRepository.delete(1L);
        boolean result2 = subscriptionRepository.delete(1L);

        //then
        Assert.assertEquals(new User(), userRepository.findById(actual.getId()));

    }
}