package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.conf.Conf;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

//    @Bean
//    public UserDao userDao() {
//        UserDao userDao = new UserDao();
//        userDao.setDataSource(dataSource());
//        return userDao;
//    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl(Conf.DB_URL);
        dataSource.setUsername(Conf.DB_USER);
        dataSource.setPassword(Conf.DB_PASSWORD);

        return dataSource;
    }
}
