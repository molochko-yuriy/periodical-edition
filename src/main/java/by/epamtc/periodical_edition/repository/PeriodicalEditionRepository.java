package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.entity.PeriodicalEdition;

import java.util.List;

public interface PeriodicalEditionRepository {
    PeriodicalEdition findById(Long periodicalEditionId);
    List<PeriodicalEdition> findAll();
    boolean add(PeriodicalEdition periodicalEdition);
    boolean update(PeriodicalEdition periodicalEdition);
    boolean delete(Long periodicalEditionId);
}

