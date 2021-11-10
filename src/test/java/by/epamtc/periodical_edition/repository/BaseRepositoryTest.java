package by.epamtc.periodical_edition.repository;

import by.epamtc.periodical_edition.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;

import static by.epamtc.periodical_edition.property.Properties.*;

public class BaseRepositoryTest {
    private final FlywayService flywayService;
    private  final JdbcConnectionPool connectionPool;

    public BaseRepositoryTest(){
        flywayService = new FlywayService();
        connectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);
    }

    @Before
    public void initDB(){
        flywayService.migrate();
    }
    @After
    public void cleanDB(){
        flywayService.clean();
    }

    public JdbcConnectionPool getConnectionPool() {
        return connectionPool;
    }

}