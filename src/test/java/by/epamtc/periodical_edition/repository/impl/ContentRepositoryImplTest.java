package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Content;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ContentRepositoryImplTest extends BaseRepositoryTest {
    private List<Content> contents;
    private ContentRepositoryImpl contentRepository;

    public ContentRepositoryImplTest (){
        contents = new ArrayList<>();
        contentRepository = new ContentRepositoryImpl(getConnectionPool());
        contents.add(new Content(1L, LocalDate.of(2021, 10, 5), LocalDate.of(2021, 11, 5), 20, 1L, 1L));
        contents.add(new Content(2L, LocalDate.of(2021,11,6), LocalDate.of(2021,12,6), 30, 2L, 2L));
    }


    @Test
    public void findById_validData_shouldReturnContent() {
        //given
        Content expected = contents.get(0);

        //when
        Content actual = contentRepository.findById(1L);

        //then
        assertEquals(expected, actual);


    }

    @Test
    public void findAll_validData_shouldReturnContents() {
        //given
        final List <Content> actual = contentRepository.findAll();

        //then
        assertEquals(contents, actual);

    }

    @Test
    public void add_validData_shouldAddNewContent() {
        // given
        Content expected = new Content(3L, LocalDate.of(2020, 10, 5), LocalDate.of(2020, 11, 5), 20, 1L, 1L);
        Content actual = new Content(null, LocalDate.of(2020, 10, 5), LocalDate.of(2020, 11, 5), 20, 1L, 1L);

        //when
        boolean isAdded = contentRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(expected, actual);
        assertEquals(expected, contentRepository.findById(actual.getId()));
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
        assertEquals(expected, actual);
        assertEquals(expected, contentRepository.findById(actual.getId()));
    }

    @Test
    public void delete() {
    }
}