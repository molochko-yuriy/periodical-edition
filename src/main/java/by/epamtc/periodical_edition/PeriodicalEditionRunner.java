package by.epamtc.periodical_edition;

import by.epamtc.periodical_edition.entity.*;
import by.epamtc.periodical_edition.enums.PeriodicalEditionType;
import by.epamtc.periodical_edition.enums.Periodicity;
import by.epamtc.periodical_edition.repository.*;
import by.epamtc.periodical_edition.repository.impl.*;
import by.epamtc.periodical_edition.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static by.epamtc.periodical_edition.property.Properties.*;

public class PeriodicalEditionRunner {
    public static void main(String[] args) {
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);





        ReviewRepository repository = new ReviewRepositoryImpl(jdbcConnectionPool);

//        Review review = repository.findById(1L);
//        System.out.println(review);
//__________________________________________________________________________
//        List<Review> periodicalEditionImages = repository.findAll();
//        System.out.println(periodicalEditionImages);
    //___________________________________________________________________________
        System.out.println("before adding -> " + repository.findAll());

        Review review = new Review();
        review.setUserComment("781462");
        review.setRating(1);
        review.setUserId(1L);
        review.setPeriodicalEditionId(1L);

        repository.add(review);

        System.out.println(review);
        System.out.println("after adding -> " + repository.findAll());

    }

    public static void image(){
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);

        ImageRepository repository = new PeriodicalEditionImageRepositoryImpl(jdbcConnectionPool);
        Date date = new Date(567);//resultSet.getDate(..).toLocaleDate();
        LocalDate localDate = date.toLocalDate();
        Date date1 = Date.valueOf(localDate);//preparedStatement.setDate(Date.valueOf(localDate))
        // ________________________________________________________________________________
//        PeriodicalEditionImage periodicalEditionImage = repository.findById(1L);
//        System.out.println(periodicalEditionImage);
        //_____________________________________________________________________________
//        List<PeriodicalEditionImage> periodicalEditionImages = repository.findAll();
//        System.out.println(periodicalEditionImages);
        //_____________________________________________________________________________________
//        System.out.println("before adding -> " + repository.findAll());
//
//        PeriodicalEditionImage periodicalEditionImage = new PeriodicalEditionImage();
//        periodicalEditionImage.setImagePath("dd");
//        periodicalEditionImage.setPeriodicalEditionId(2L);
//
//        repository.add(periodicalEditionImage);
//
//        System.out.println(periodicalEditionImage);
//        System.out.println("after adding -> " + repository.findAll());
        //___________________________________________________________________________________
//        PeriodicalEditionImage periodicalEditionImage = new PeriodicalEditionImage();
//        periodicalEditionImage.setImagePath("d4541d");
//        periodicalEditionImage.setPeriodicalEditionId(2L);
//        periodicalEditionImage.setId(2L);
//        System.out.println("before updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
//
//        repository.update(periodicalEditionImage);
//
//        System.out.println("after updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
        //-----------------------------------------------------------------------------------
        PeriodicalEditionImage periodicalEditionImage = new PeriodicalEditionImage();
        periodicalEditionImage.setImagePath("d4541d");
        periodicalEditionImage.setPeriodicalEditionId(2L);
        periodicalEditionImage.setId(2L);
        System.out.println("before deletion-> " + repository.findById(2L));
        System.out.println("-----------------");

        repository.delete(2L);

        System.out.println("after deletion-> " + repository.findById(2L));
        System.out.println("-----------------");
    }

    public static void periodicalEdition(){
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);

        PeriodicalEditionRepository repository = new PeriodicalEditionRepositoryImpl(jdbcConnectionPool);
//        PeriodicalEdition periodicalEdition = repository.findById(1L);
//        System.out.println(periodicalEdition);
        //  ____________________________________________________________
//        List<PeriodicalEdition> periodicalEditions = repository.findAll();
//        System.out.println(periodicalEditions);
        //______________________________________________________________
//        System.out.println("before adding -> " + repository.findAll());
//
//        PeriodicalEdition periodicalEdition = new PeriodicalEdition();
//        periodicalEdition.setPeriodicalEditionType(PeriodicalEditionType.MAGAZINE);
//        periodicalEdition.setPrice(40);
//        periodicalEdition.setPeriodicity(Periodicity.MONTHLY);
//        periodicalEdition.setDescription("very good");
//        periodicalEdition.setTitle("The Guardian");
//
//        repository.add(periodicalEdition);
//
//        System.out.println(periodicalEdition);
//        System.out.println("after adding -> " + repository.findAll());
        //______________________________________________________________
//        PeriodicalEdition periodicalEdition = new PeriodicalEdition();
//        periodicalEdition.setPeriodicalEditionType(PeriodicalEditionType.MAGAZINE);
//        periodicalEdition.setPrice(40);
//        periodicalEdition.setPeriodicity(Periodicity.MONTHLY);
//        periodicalEdition.setDescription("very good");
//        periodicalEdition.setTitle("The PEOPLE");
//        periodicalEdition.setId(2L);
//
//        System.out.println("before updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
//
//        repository.update(periodicalEdition);
//
//        System.out.println("after updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
        //_________________________________________________________________________
        PeriodicalEdition periodicalEdition = new PeriodicalEdition();
        periodicalEdition.setPeriodicalEditionType(PeriodicalEditionType.MAGAZINE);
        periodicalEdition.setPrice(40);
        periodicalEdition.setPeriodicity(Periodicity.MONTHLY);
        periodicalEdition.setDescription("very good");
        periodicalEdition.setTitle("The PEOPLE");
        periodicalEdition.setId(2L);

        System.out.println("before deletion-> " + repository.findById(2L));
        System.out.println("-----------------");

        repository.delete(2L);

        System.out.println("after deletion-> " + repository.findById(2L));
        System.out.println("-----------------");

    }

    public static void User(){
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);

        UserRepository repository = new UserRepositoryImpl(jdbcConnectionPool);
//      User user = repository.findById(2L);
//      System.out.println(user);

//        List<User> users = repository.findAll();
//        System.out.println(users);
//
        User user = new User();
        user.setId(2L);
        user.setLastName("Petr");
        user.setFirstName("petrov");
        user.setBalance(785);
        user.setEmail("478");
        user.setLogin("7896");
        user.setMobilePhone("789514");
        user.setPassword("45145");

//        System.out.println("-----------------");
//        System.out.println("before updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
//
//        repository.update(user);
//
//        System.out.println("after updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
//
//        System.out.println(user);
//
//        repository.add(user);
//
//        System.out.println(user);
//        List<User> userList = repository.findAll();
//
//        System.out.println(userList);


        System.out.println("-----------------");
        System.out.println("before deletion-> " + repository.findById(2L));
        System.out.println("-----------------");

        repository.delete(2L);

        System.out.println("after deletion-> " + repository.findById(2L));
        System.out.println("-----------------");
    }

    public static void  role() {

        FlywayService flywayService = new FlywayService();
        flywayService.migrate();
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);


        RoleRepository repository = new RoleRepositoryImpl(jdbcConnectionPool);
//        Role role = repository.findById(2L);
//        System.out.println(role);

        List<Role> roles = repository.findAll();
        System.out.println(roles);

//        Role role = new Role();
//        role.setRoleName("cooker");
//
//        System.out.println("-----------------");
//        System.out.println("before adding -> " + role);
//        System.out.println("-----------------");
//
//        repository.add(role);
//
//        System.out.println("after adding -> " + role);
//        System.out.println("-----------------");
//
//        roles = repository.findAll();
//
//        System.out.println(roles);
//
//        Role role = new Role();
//        role.setId(2L);
//        role.setRoleName("cooker");
//
//        System.out.println("-----------------");
//        System.out.println("before updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
//
//        repository.update(role);
//
//
//        System.out.println("after updating -> " + repository.findById(2L));
//        System.out.println("-----------------");
//
//        System.out.println(repository.findAll());

        System.out.println("-----------------");
        System.out.println("before deletion-> " + repository.findById(2L));
        System.out.println("-----------------");

        repository.delete(2L);

        System.out.println("before deletion-> " + repository.findById(2L));
        System.out.println("-----------------");


        System.out.println(repository.findAll());
    }
}
