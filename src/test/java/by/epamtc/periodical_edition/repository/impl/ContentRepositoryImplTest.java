package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Content;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContentRepositoryImplTest extends BaseRepositoryTest {
    private final List<Content> contents;
    private final ContentRepositoryImpl contentRepository;

    public ContentRepositoryImplTest() {
        contents = new ArrayList<>();
        contentRepository = new ContentRepositoryImpl(getConnectionPool());
        contents.add(new Content(1L, LocalDate.of(2021, 6, 5), LocalDate.of(2021, 7, 5), 20, 1L, 1L));
        contents.add(new Content(2L, LocalDate.of(2021, 7, 6), LocalDate.of(2021, 8, 6), 30, 2L, 2L));
        contents.add(new Content(3L, LocalDate.of(2021, 8, 7), LocalDate.of(2021, 9, 7), 40, 3L, 1L));
        contents.add(new Content(4L, LocalDate.of(2021, 9, 8), LocalDate.of(2021, 10, 8), 50, 4L, 2L));
    }

    @Test
    public void findById_validData_shouldReturnContent() {
        //given
        Content expected = contents.get(0);

        //when
        Content actual = contentRepository.findById(1L);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnContents() {
        //given && when
        final List<Content> actual = contentRepository.findAll();

        //then
        Assert.assertEquals(contents, actual);
    }

    @Test
    public void add_validData_shouldAddNewContent() {
        // given
        Content expected = new Content(5L, LocalDate.of(2020, 10, 5), LocalDate.of(2020, 11, 5), 20, 1L, 1L);
        Content actual = new Content(null, LocalDate.of(2020, 10, 5), LocalDate.of(2020, 11, 5), 20, 1L, 1L);

        //when
        boolean isAdded = contentRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, contentRepository.findById(actual.getId()));
    }

    @Test
    public void update_validData_shouldUpdateContent() {
        //given
        Content expected = new Content(2L, LocalDate.of(2020, 10, 5), LocalDate.of(2020, 11, 5), 20, 1L, 1L);
        Content actual = contentRepository.findById(2L);

        //when
        actual.setId(2L);
        actual.setStartDate(LocalDate.of(2020, 10, 5));
        actual.setExpirationDate(LocalDate.of(2020, 11, 5));
        actual.setPrice(20);
        actual.setSubscriptionId(1L);
        actual.setPeriodicalEditionId(1L);
        boolean isUpdated = contentRepository.update(actual);

        //then
        Assert.assertTrue(isUpdated);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, contentRepository.findById(actual.getId()));
    }

    @Test
    public void delete_validData_shouldDeleteContent() {
        //given
        Content expected = contents.get(0);
        Content actual = contentRepository.findById(1L);

        Assert.assertEquals(expected, actual);

        //when
        boolean isDeleted = contentRepository.delete(1L);

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertNull(contentRepository.findById(1L));
    }

    @Test
    public void findContentBySubscriptionId_validData_shouldReturnContentOfCertainSubscription() {
        //given
        List<Content> expected = contents.stream()
                .filter(content -> content.getSubscriptionId() == 1L)
                .collect(Collectors.toList());

        //when
        List<Content> actual = contentRepository.findContentBySubscriptionId(1L);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findContentByPeriodicalEditionId_validData_shouldReturnContentOfCertainPeriodicalEdition() {
        //given
        List<Content> expected = contents.stream()
                .filter(content -> content.getPeriodicalEditionId() == 1L)
                .collect(Collectors.toList());

        //when
        List<Content> actual = contentRepository.findContentByPeriodicalEditionId(1L);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deletePeriodicalEditionFromSubscription_validData_shouldDeletePeriodicalEdition() {
        //given
        int expected = 1;
        List<Content> contents = contentRepository.findContentBySubscriptionId(3L);
        Assert.assertEquals(expected, contents.size());

        //when
        boolean isDeleted = contentRepository.deletePeriodicalEditionFromSubscription(3L, 1L);

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertEquals(0, contentRepository.findContentBySubscriptionId(3L).size());
    }
}