package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Image;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import by.epamtc.periodical_edition.repository.ImageRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageRepositoryImplTest extends BaseRepositoryTest {
    private final List<Image> images;
    private final ImageRepository imageRepository;

    public ImageRepositoryImplTest() {
        images = new ArrayList<>();
        imageRepository = new ImageRepositoryImpl(getConnectionPool());
        images.add(new Image(1L, 1L, "D/im/cont"));
        images.add(new Image(2L, 2L, "D/if/nok"));
        images.add(new Image(3L, 1L, "A/im/cont"));
        images.add(new Image(4L, 2L, "A/if/nok"));
        images.add(new Image(5L, 1L, "B/im/cont"));
        images.add(new Image(6L, 2L, "B/if/nok"));
    }

    @Test
    public void findById_validData_shouldReturnImage() {
        //given
        Image expected = images.get(0);

        //when
        Image actual = imageRepository.findById(1L);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findAll_validData_shouldReturnImages() {
        //given && when
        final List<Image> actual = imageRepository.findAll();

        //then
        Assert.assertEquals(images, actual);
    }

    @Test
    public void add_validData_shouldAddNewImage() {
        //given
        Image expected = new Image(7L, 1L, "D/if/nok/k");
        Image actual = new Image(null, 1L, "D/if/nok/k");

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
        Image expected = new Image(2L, 1L, "D/inf/nok/k");
        Image actual = imageRepository.findById(2L);

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
        Image expected = images.get(0);
        Image actual = imageRepository.findById(1L);
        Assert.assertEquals(expected, actual);

        //when
        boolean isDeleted = imageRepository.delete(1L);

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertNull(imageRepository.findById(1L));
    }

    @Test
    public void findImageByPeriodicalEditionId_validData_shouldReturnImagesOfCertainPeriodicalEdition() {
        //given
        List<Image> expected = images.stream()
                .filter(image -> image.getPeriodicalEditionId() == 1L)
                .collect(Collectors.toList());

        //when
        List<Image> actual = imageRepository.findImageByPeriodicalEditionId(1L);

        //then
        Assert.assertEquals(expected, actual);
    }
}