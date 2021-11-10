package by.epamtc.periodical_edition.repository;
import by.epamtc.periodical_edition.entity.PeriodicalEditionImage;
import java.util.List;

public interface ImageRepository {
    PeriodicalEditionImage findById(Long periodicalEditionImageId);
    List<PeriodicalEditionImage> findAll();
    boolean add(PeriodicalEditionImage periodicalEditionImage);
    boolean update(PeriodicalEditionImage periodicalEditionImage);
    boolean delete(Long periodicalEditionImageId);
}
