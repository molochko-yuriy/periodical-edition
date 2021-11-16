package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.PeriodicalEditionImage;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodicalEditionImageRepositoryImplTest extends BaseRepositoryTest {
    private final List<PeriodicalEditionImage> periodicalEditionImages;
    private final PeriodicalEditionImageRepositoryImpl imageRepository;

    public  PeriodicalEditionImageRepositoryImplTest(){
        periodicalEditionImages = new ArrayList<>();
        imageRepository = new PeriodicalEditionImageRepositoryImpl(getConnectionPool());
        periodicalEditionImages.add(new PeriodicalEditionImage(1L, 1L, "D/im/cont"));
        periodicalEditionImages.add(new PeriodicalEditionImage(2L, 2L, "D/if/nok"));
        periodicalEditionImages.add(new PeriodicalEditionImage(3L, 1L, "A/im/cont"));
        periodicalEditionImages.add(new PeriodicalEditionImage(4L, 2L, "A/if/nok"));
        periodicalEditionImages.add(new PeriodicalEditionImage(5L, 1L, "B/im/cont"));
        periodicalEditionImages.add(new PeriodicalEditionImage(6L, 2L, "B/if/nok"));
    }

    @Test
    public void findById_validData_shouldReturnImage() {
        //given
        PeriodicalEditionImage expected = periodicalEditionImages.get(0);

        //when
        PeriodicalEditionImage actual = imageRepository.findById(1L);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnImages() {
        //given
        final List <PeriodicalEditionImage> actual = imageRepository.findAll();

        //then
        Assert.assertEquals(periodicalEditionImages, actual);
    }

    @Test
    public void add_validData_shouldAddNewImage() {
        //given
        PeriodicalEditionImage expected = new PeriodicalEditionImage(7L, 1L, "D/if/nok/k");
        PeriodicalEditionImage actual = new PeriodicalEditionImage(null, 1L, "D/if/nok/k");

        //when
        boolean isAdded = imageRepository.add(actual);

        //then
        Assert.assertTrue(isAdded);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, imageRepository.findById(actual.getId()));
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
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, imageRepository.findById(actual.getId()));
    }

    @Test
    public void delete_validData_shouldDeleteImage() {
        //given
        PeriodicalEditionImage expected = periodicalEditionImages.get(0);
        PeriodicalEditionImage actual = imageRepository.findById(1L);

        Assert.assertEquals(expected, actual);

        //when
        boolean isDeleted = imageRepository.delete(1L);

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertNull(imageRepository.findById(1L));

    }

    @Test
    public void findImageByPeriodicalEditionId_validData_shouldReturnImagesOfCertainPeriodicalEdition() {
        //given && when
        List<PeriodicalEditionImage> expected = periodicalEditionImages.stream()
                .filter(periodicalEditionImage -> periodicalEditionImage.getPeriodicalEditionId() == 1L)
                .collect(Collectors.toList());

        //then
        List<PeriodicalEditionImage> actual = imageRepository.findImageByPeriodicalEditionId(1L);
        Assert.assertEquals(expected, actual);
    }

}