package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.PeriodicalEditionImage;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PeriodicalEditionImageRepositoryImplTest extends BaseRepositoryTest {
    private List<PeriodicalEditionImage> periodicalEditionImages;
    private PeriodicalEditionImageRepositoryImpl imageRepository;

    public  PeriodicalEditionImageRepositoryImplTest(){
        periodicalEditionImages = new ArrayList<>();
        imageRepository = new PeriodicalEditionImageRepositoryImpl(getConnectionPool());
        periodicalEditionImages.add(new PeriodicalEditionImage(1L, 1L, "D/im/cont"));
        periodicalEditionImages.add(new PeriodicalEditionImage(2L, 2L, "D/if/nok"));
    }

    @Test
    public void findById_validData_shouldReturnImage() {
        //given
        PeriodicalEditionImage expected = periodicalEditionImages.get(0);

        //when
        PeriodicalEditionImage actual = imageRepository.findById(1L);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnImages() {
        //given
        final List <PeriodicalEditionImage> actual = imageRepository.findAll();

        //then
        assertEquals(periodicalEditionImages, actual);
    }

    @Test
    public void add_validData_shouldAddNewImage() {
        //given
        PeriodicalEditionImage expected = new PeriodicalEditionImage(3L, 1L, "D/if/nok/k");
        PeriodicalEditionImage actual = new PeriodicalEditionImage(null, 1L, "D/if/nok/k");

        //when
        boolean isAdded = imageRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        assertEquals(expected, actual);
        assertEquals(expected, imageRepository.findById(actual.getId()));
    }

    @Test
    public void update_validData_ShouldReturnUpdateImage() {
        //given
        PeriodicalEditionImage expected = new PeriodicalEditionImage(2L, 1L, "D/inf/nok/k");
        PeriodicalEditionImage actual = imageRepository.findById(2L);

        //when
        actual.setId(2L);
        actual.setPeriodicalEditionId(1L);
        actual.setImagePath("D/inf/nok/k");
        boolean isUpdated = imageRepository.update(actual);

        //then
        Assert.assertTrue(isUpdated);
        assertEquals(expected, actual);
        assertEquals(expected, imageRepository.findById(actual.getId()));
    }

    @Test
    public void delete() {
    }
}