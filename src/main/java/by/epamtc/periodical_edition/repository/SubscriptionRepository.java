package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Subscription;

import java.util.List;

public interface SubscriptionRepository extends BaseRepository<Subscription> {
    List<Subscription> findSubscriptionsByUserId(Long userId);

    List<Subscription> findSubscriptionsThatIncludePeriodicalEditionById(Long periodicalEditionId);
}
