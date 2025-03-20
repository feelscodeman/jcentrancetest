package com.JavaCode.JavaCodeTest.config;

import com.JavaCode.JavaCodeTest.model.Wallet;
import com.JavaCode.JavaCodeTest.repository.WalletRepository;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

@Configuration
@EnableJpaRepositories("com.JavaCode.JavaCodeTest.repository")
public class SpringDataConfiguration {

    @Value("${dataSourceURL}")
    private String datasourceUrl;

    @Value("${dataSourceUser}")
    private String datasourceUser;

    @Value("${dataSourcePwd}")
    private String datasourcePwd;

    @Bean
public DataSource dataSource() {
    HikariDataSource dataSource = new HikariDataSource();
dataSource.setDriverClassName("org.postgresql.Driver");
dataSource.setJdbcUrl(datasourceUrl);
dataSource.setUsername(datasourceUser);
dataSource.setPassword(datasourcePwd);
dataSource.setConnectionTimeout(1000);
return dataSource;
}

    @Bean
public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
}

@Bean
public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
    jpaVendorAdapter.setShowSql(true);
    return jpaVendorAdapter;
}

@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
    lcemfb.setDataSource(dataSource());
    Properties properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", "create");
    properties.put("hibernate.show_sql", "true");
    properties.put("hibernate.format_sql", "true");
    lcemfb.setJpaProperties(properties);
    lcemfb.setJpaVendorAdapter(jpaVendorAdapter());
    lcemfb.setPackagesToScan("com.JavaCode.JavaCodeTest.model");
    return lcemfb;
}

@Bean
    CommandLineRunner initDatabase(WalletRepository repo) {
        return args -> {
            Random randomIntBalances = new Random();
            randomIntBalances.ints(50L, -1000, 10000)
                    .mapToObj(balance -> new Wallet(BigDecimal.valueOf(balance)))
                    .toList()
                    .forEach(repo::save);
        };
}

}
