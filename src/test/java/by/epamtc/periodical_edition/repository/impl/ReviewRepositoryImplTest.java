package by.epamtc.periodical_edition.repository.impl;

import by.epamtc.periodical_edition.entity.Review;
import by.epamtc.periodical_edition.repository.BaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReviewRepositoryImplTest extends BaseRepositoryTest {
    private ReviewRepositoryImpl reviewRepository;
    private List<Review> reviews;

    public  ReviewRepositoryImplTest(){
        reviewRepository = new ReviewRepositoryImpl(getConnectionPool());
        reviews = new ArrayList<>();
        reviews.add(new Review(1L,"good",5,1L,1L));
        reviews.add(new Review(2L,"bad", 4,2L,2L));
    }

    @Test
    public void findById_validData_shouldReturnReview() {
        //given
        Review expected = reviews.get(0);

        //when
        Review actual = reviewRepository.findById(1L);

        //then
        assertEquals(actual, expected);
    }

    @Test
    public void findAll_validData_shouldReturnReviews() {
        //given
        final List <Review> actual = reviewRepository.findAll();

        // then
        assertEquals(actual, reviews);
    }

    @Test
    public void add_validData_shouldAddNewReview() {
        //given
        Review expected = new Review(3L,"good",5,1L,1L);
        Review actual = new Review(null,"good",5,1L,1L);

        //when
        boolean isAdded = reviewRepository.add(actual);
        //then
        Assert.assertTrue(isAdded);
        assertEquals(expected, actual);
        assertEquals(expected, reviewRepository.findById(actual.getId()));


    }

    @Test
    public void update_validData_shouldUpdateReview() {
        //given
        Review expected = new Review(2L,"good",5,1L,1L);
        Review actual = reviewRepository.findById(2L);
        assertEquals(reviews.get(1), actual);

        //when
        actual.setUserComment("good");
        actual.setRating(5);
        actual.setUserId(1L);
        actual.setPeriodicalEditionId(1L);
        boolean isUpdated = reviewRepository.update(expected);

        //then
        Assert.assertTrue(isUpdated);
        assertEquals(expected, actual);
        assertEquals(expected, reviewRepository.findById(actual.getId()));

    }

    @Test
    public void delete() {
    }
}