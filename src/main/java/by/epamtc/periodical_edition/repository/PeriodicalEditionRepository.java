package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.PeriodicalEdition;

import java.util.List;

public interface PeriodicalEditionRepository extends BaseRepository<PeriodicalEdition> {
    List<PeriodicalEdition> findPeriodicalEditionsBySubscriptionId(Long subscriptionId);
}

