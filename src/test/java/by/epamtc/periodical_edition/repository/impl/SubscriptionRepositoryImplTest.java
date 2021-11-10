package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Subscription;
import by.epamtc.periodical_edition.enums.PaymentStatus;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import by.epamtc.periodical_edition.repository.SubscriptionRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SubscriptionRepositoryImplTest extends BaseRepositoryTest {

    private SubscriptionRepositoryImpl subscriptionRepository;
    private List<Subscription> subscriptions;

    public SubscriptionRepositoryImplTest() {
        subscriptions = new ArrayList<>();
        subscriptionRepository = new SubscriptionRepositoryImpl(getConnectionPool());
        subscriptions.add(new Subscription(1L, 28, 1L, PaymentStatus.PAID));
        subscriptions.add(new Subscription(2L, 42, 2L, PaymentStatus.UNPAID));
    }

    @Test
    public void findById_validData_shouldReturnUser() {
        //given && when
        Subscription expected = subscriptions.get(0);

        //then
        Subscription actual = subscriptionRepository.findById(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnUsers() {
        //given && when
        List<Subscription> actual = subscriptionRepository.findAll();

        //then
        assertEquals(subscriptions, actual);

    }

    @Test
    public void add_validData_shouldAddNewSubscription() {
        //given
    Subscription expected = new Subscription(3L, 30, 1L, PaymentStatus.PAID);
    Subscription actual = new Subscription(null, 30, 1L, PaymentStatus.PAID);

        // when
        boolean isAdded = subscriptionRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(expected,actual);
        assertEquals(expected, subscriptionRepository.findById(actual.getId()));


    }

    @Test
    public void update_validData_shouldUpdateSubscription() {
        //given
        Subscription expected = new Subscription(2L, 30, 1L, PaymentStatus.PAID);
        Subscription actual = subscriptionRepository.findById(2L);
        Assert.assertEquals(subscriptions.get(1), actual);

        //when
        actual.setPrice(30);
        actual.setUserId(1L);
        actual.setPaymentStatus(PaymentStatus.PAID);
        boolean isUpdated = subscriptionRepository.update(expected);

        //then
        Assert.assertTrue(isUpdated);
        Assert.assertEquals(expected, actual);
        assertEquals(expected, subscriptionRepository.findById(actual.getId()));



    }

    @Test
    public void delete() {
    }
}