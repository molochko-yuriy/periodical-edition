package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.PeriodicalEdition;
import by.epamtc.periodical_edition.enums.PeriodicalEditionType;
import by.epamtc.periodical_edition.enums.Periodicity;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PeriodicalEditionRepositoryImplTest extends BaseRepositoryTest {
    private final PeriodicalEditionRepositoryImpl periodicalEditionRepository;
    private final List<PeriodicalEdition> periodicalEditions;

    public PeriodicalEditionRepositoryImplTest() {
        periodicalEditions = new ArrayList<>();
        periodicalEditionRepository = new PeriodicalEditionRepositoryImpl(getConnectionPool());
        periodicalEditions.add(new PeriodicalEdition(1L, 20, "very good", "The Guardian", PeriodicalEditionType.MAGAZINE, Periodicity.WEEKLY));
        periodicalEditions.add(new PeriodicalEdition(2L, 30, "good", "The NY Times", PeriodicalEditionType.NEWSPAPER, Periodicity.MONTHLY));
    }

    @Test
    public void findById_validData_shouldReturn_periodicalEdition() {
        //given
        PeriodicalEdition expected = periodicalEditions.get(0);

        //when
        PeriodicalEdition actual = periodicalEditionRepository.findById(1L);

        //then
        assertEquals(actual, expected);
    }

    @Test
    public void findAll_validData_shouldReturnPeriodicalEditions() {
        //given
        final List<PeriodicalEdition> actual = periodicalEditionRepository.findAll();

        //then
        assertEquals(actual, periodicalEditions);
    }

    @Test
    public void add_validData_shouldAddNewPeriodicalEdition() {
        //given
        PeriodicalEdition expected = new PeriodicalEdition(3L, 20, "some discription", "The Guardian", PeriodicalEditionType.MAGAZINE, Periodicity.WEEKLY);
        PeriodicalEdition actual = new PeriodicalEdition(null, 20, "some discription", "The Guardian", PeriodicalEditionType.MAGAZINE, Periodicity.WEEKLY);

        //when
        boolean isAdded = periodicalEditionRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(actual, expected);
        assertEquals(expected, periodicalEditionRepository.findById(actual.getId()));
    }

    @Test
    public void update_validData_shouldUpdatePeriodicalEdition() {
        //given
        PeriodicalEdition expected = new PeriodicalEdition(2L, 20, "some discription", "The Guardian", PeriodicalEditionType.MAGAZINE, Periodicity.WEEKLY);
        PeriodicalEdition actual = periodicalEditionRepository.findById(2L);

        //when
        actual.setId(2L);
        actual.setPrice(20);
        actual.setDescription("some discription");
        actual.setTitle("The Guardian");
        actual.setPeriodicalEditionType(PeriodicalEditionType.MAGAZINE);
        actual.setPeriodicity(Periodicity.WEEKLY);
        boolean isUpdated = periodicalEditionRepository.update(actual);

        //then
        assertTrue(isUpdated);
        assertEquals(expected, actual);
        assertEquals(expected, periodicalEditionRepository.findById(actual.getId()));
    }

    @Test
    public void delete() {
    }
}