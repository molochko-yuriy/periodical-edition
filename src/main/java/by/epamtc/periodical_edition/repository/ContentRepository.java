package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.Content;

import java.util.List;


public interface ContentRepository extends BaseRepository<Content> {
    List<Content> findContentBySubscriptionId(Long subscriptionId);
    List<Content> findContentByPeriodicalEditionId(Long periodicalEditionId);
    boolean deletePeriodicalEditionFromSubscription(Long subscriptionId, Long periodicalEditionId);
}