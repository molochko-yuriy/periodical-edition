package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.User;
import by.epamtc.periodical_edition.repository.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImplTest extends BaseRepositoryTest {
    private final UserRepository userRepository;
    private final List<User> users;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;

    public UserRepositoryImplTest() {
        users = new ArrayList<>();
        userRepository = new UserRepositoryImpl(getConnectionPool());
        users.add(new User(1L, "Степанов", "Александр", "stepanow.a@mail.ru", "1111",
                "8684758965", "stepanow.a@mail.ru", 235));
        users.add(new User(2L, "Александров", "Виктор", "alecsandrow.a@mail.ru", "2222",
                "985214", "alecsandrow.a@mail.ru", 25));
        users.add(new User(3L, "Степанов", "Степан", "stepan.a@mail.ru", "3333",
                "9522585", "stepan.a@mail.ru", 235));
        users.add(new User(4L, "Александров", "Андрей", "alecsandr.a@mail.ru", "4444",
                "7892152", "alecsandr.a@mail.ru", 25));
        subscriptionRepository = new SubscriptionRepositoryImpl(getConnectionPool());
        roleRepository = new RoleRepositoryImpl(getConnectionPool());
    }

    @Test
    public void findById_validData_shouldReturnUser() {
        //given
        User expected = users.get(0);

        //when
        User actual = userRepository.findById(1L);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() {
        //given && when
        final List<User> actual = userRepository.findAll();

        //then
        Assert.assertEquals(users, actual);
    }


    @Test
    public void add_validData_shouldAddNewUser() {
        //given
        User expected = new User(5L, "Oleg", "Petrov", "rider", "580236", "+375295899663", "oleg.p@mail.ru", 85);
        User actual = new User(null, "Oleg", "Petrov", "rider", "580236", "+375295899663", "oleg.p@mail.ru", 85);

        // when
        boolean isAdded = userRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, userRepository.findById(actual.getId()));
    }

    @Test
    public void update_validData_shouldUpdateUser() {
        //given
        User expected = new User(2L, "Petrov", "Oleg", "rider", "158963", "+375291548544", "petrov.a@mail.ru", 85);
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
        Assert.assertTrue(isUpdated);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, userRepository.findById(actual.getId()));
    }


    @Test
    public void delete_validData_shouldDeleteUser() {
        //given
        User expected = users.get(0);
        User actual = userRepository.findById(1L);

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(2, roleRepository.findRolesByUserId(actual.getId()).size());
        Assert.assertEquals(4, subscriptionRepository.findSubscriptionsByUserId(actual.getId()).size());

        // when
        boolean isDeleted = userRepository.delete(actual.getId());

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertNull(userRepository.findById(1L));
        Assert.assertEquals(0, roleRepository.findRolesByUserId(actual.getId()).size());
        Assert.assertEquals(0, subscriptionRepository.findSubscriptionsByUserId(actual.getId()).size());
    }
}