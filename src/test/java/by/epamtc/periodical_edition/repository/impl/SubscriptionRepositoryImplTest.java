package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Subscription;
import by.epamtc.periodical_edition.enums.PaymentStatus;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionRepositoryImplTest extends BaseRepositoryTest {

    private final SubscriptionRepositoryImpl subscriptionRepository;
    private final List<Subscription> subscriptions;
    private final ContentRepositoryImpl contentRepository;

    public SubscriptionRepositoryImplTest() {
        subscriptions = new ArrayList<>();
        subscriptionRepository = new SubscriptionRepositoryImpl(getConnectionPool());
        subscriptions.add(new Subscription(1L, 28, 1L, PaymentStatus.PAID));
        subscriptions.add(new Subscription(2L, 42, 1L, PaymentStatus.UNPAID));
        subscriptions.add(new Subscription(3L, 41, 3L, PaymentStatus.PAID));
        subscriptions.add(new Subscription(4L, 32, 4L, PaymentStatus.UNPAID));
        subscriptions.add(new Subscription(5L, 42, 1L, PaymentStatus.PAID));
        subscriptions.add(new Subscription(6L, 52, 2L, PaymentStatus.UNPAID));
        subscriptions.add(new Subscription(7L, 72, 3L, PaymentStatus.PAID));
        subscriptions.add(new Subscription(8L, 92, 4L, PaymentStatus.UNPAID));
        subscriptions.add(new Subscription(9L, 2, 1L, PaymentStatus.PAID));
        subscriptions.add(new Subscription(10L, 62, 2L, PaymentStatus.UNPAID));
        subscriptions.add(new Subscription(11L, 48, 3L, PaymentStatus.PAID));
        contentRepository = new ContentRepositoryImpl(getConnectionPool());
    }

    @Test
    public void findById_validData_shouldReturnUser() {
        //given && when
        Subscription expected = subscriptions.get(0);

        //then
        Subscription actual = subscriptionRepository.findById(1L);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnUsers() {
        //given && when
        List<Subscription> actual = subscriptionRepository.findAll();

        //then
        Assert.assertEquals(subscriptions, actual);

    }

    @Test
    public void add_validData_shouldAddNewSubscription() {
        //given
        Subscription expected = new Subscription(12L, 30, 1L, PaymentStatus.PAID);
        Subscription actual = new Subscription(null, 30, 1L, PaymentStatus.PAID);

        // when
        boolean isAdded = subscriptionRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, subscriptionRepository.findById(actual.getId()));


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
        Assert.assertEquals(expected, subscriptionRepository.findById(actual.getId()));


    }

    @Test
    public void delete_validData_shouldDeleteSubscription() {
        //given
        Subscription expected = subscriptions.get(0);
        Subscription actual = subscriptionRepository.findById(1L);

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, contentRepository.findContentBySubscriptionId(actual.getId()).size());

        //when
        boolean isDeleted = subscriptionRepository.delete(actual.getId());
        Assert.assertTrue(isDeleted);
        Assert.assertNull(subscriptionRepository.findById(1L));
        Assert.assertEquals(0, contentRepository.findContentBySubscriptionId(actual.getId()).size());


    }

    @Test
    public void findSubscriptionsByUserId_validData_shouldReturnAllSubscriptionsOfCertainUser() {
        //given && when
        List<Subscription> expected = subscriptions.stream()
                .filter(subscription -> subscription.getUserId() == 1L)
                .collect(Collectors.toList());

        //then
        List<Subscription> actual = subscriptionRepository.findSubscriptionsByUserId(1L);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findSubscriptionsThatIncludePeriodicalEditionById(){
        //given && when
        int expected = 2;

        //then
        List<Subscription> actual = subscriptionRepository.findSubscriptionsThatIncludePeriodicalEditionById(1L);
        Assert.assertEquals(expected, actual.size());
    }
}