package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Review;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewRepositoryImplTest extends BaseRepositoryTest {
    private final ReviewRepositoryImpl reviewRepository;
    private final List<Review> reviews;

    public ReviewRepositoryImplTest() {
        reviewRepository = new ReviewRepositoryImpl(getConnectionPool());
        reviews = new ArrayList<>();
        reviews.add(new Review(1L, "good", 5, 1L, 1L));
        reviews.add(new Review(2L, "bad", 4, 2L, 2L));
        reviews.add(new Review(3L, "bad", 1, 3L, 1L));
        reviews.add(new Review(4L, "bad", 2, 4L, 2L));
        reviews.add(new Review(5L, "bad", 4, 1L, 1L));
        reviews.add(new Review(6L, "bad", 3, 2L, 2L));
    }

    @Test
    public void findById_validData_shouldReturnReview() {
        //given
        Review expected = reviews.get(0);

        //when
        Review actual = reviewRepository.findById(1L);

        //then
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void findAll_validData_shouldReturnReviews() {
        //given
        final List<Review> actual = reviewRepository.findAll();

        // then
        Assert.assertEquals(actual, reviews);
    }

    @Test
    public void add_validData_shouldAddNewReview() {
        //given
        Review expected = new Review(7L, "good", 5, 1L, 1L);
        Review actual = new Review(null, "good", 5, 1L, 1L);

        //when
        boolean isAdded = reviewRepository.add(actual);
        //then
        Assert.assertTrue(isAdded);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, reviewRepository.findById(actual.getId()));

    }

    @Test
    public void update_validData_shouldUpdateReview() {
        //given
        Review expected = new Review(2L, "good", 5, 1L, 1L);
        Review actual = reviewRepository.findById(2L);
        Assert.assertEquals(reviews.get(1), actual);

        //when
        actual.setUserComment("good");
        actual.setRating(5);
        actual.setUserId(1L);
        actual.setPeriodicalEditionId(1L);
        boolean isUpdated = reviewRepository.update(expected);

        //then
        Assert.assertTrue(isUpdated);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, reviewRepository.findById(actual.getId()));

    }

    @Test
    public void delete_validData_shouldDeleteReview() {
        //given
        Review expected = reviews.get(0);
        Review actual = reviewRepository.findById(1L);
        Assert.assertEquals(expected, actual);

        //when
        boolean isDeleted = reviewRepository.delete(1L);

        //then
        Assert.assertTrue(isDeleted);
        Assert.assertNull(reviewRepository.findById(1L));

    }

    @Test
    public void findReviewByPeriodicalEditionId_validData_shouldReturnReviewsAboutCertainPeriodicalEdition() {
        //given && when
        List<Review> expected = reviews.stream()
                .filter(review -> review.getPeriodicalEditionId() == 1L)
                .collect(Collectors.toList());
        //then
        List<Review> actual = reviewRepository.findReviewByPeriodicalEditionId(1L);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findReviewByUserId_validData_shouldReturnReviewsAboutCertainPeriodicalEditionFromCertainUser() {

        //given && when
        List<Review> expected = reviews.stream()
                .filter(review -> review.getUserId() == 1L)
                .collect(Collectors.toList());

        //then
        List<Review> actual = reviewRepository.findReviewByUserId(1L);

        Assert.assertEquals(expected, actual);
    }

}