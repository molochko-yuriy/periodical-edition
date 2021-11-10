package by.epamtc.periodical_edition.repository;
import by.epamtc.periodical_edition.entity.Subscription;

import java.util.List;

public interface SubscriptionRepository {

    Subscription findById(Long subscriptionId);
    List<Subscription> findAll();
    boolean add(Subscription subscription);
    boolean update(Subscription subscription);
    boolean delete(Long subscriptionId);
    List<Subscription> findSubscriptionsByUserId(Long userId);
}
