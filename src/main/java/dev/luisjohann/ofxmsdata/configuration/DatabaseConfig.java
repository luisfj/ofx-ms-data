package dev.luisjohann.ofxmsdata.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackages = "dev.luisjohann.ofxmsdata.repository.jpa", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = TransactionManagerUtil.JPA_TRANSACTION_MANAGER)
@EnableR2dbcRepositories(basePackages = "dev.luisjohann.ofxmsdata.repository.r2dbc")
@EnableTransactionManagement()
public class DatabaseConfig {

   @Bean
   @ConfigurationProperties(prefix = "spring.datasource.jpa")
   public DataSourceProperties jpaDataSourceProperties() {
      return new DataSourceProperties();
   }

   @Bean
   public DataSource jpaDataSource() {
      return jpaDataSourceProperties().initializeDataSourceBuilder().build();
   }

   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
      return builder
            .dataSource(jpaDataSource())
            .packages("dev.luisjohann.ofxmsdata.model")
            .persistenceUnit("jpa")
            .build();
   }

   @Value("${conf.db.host}")
   String dbHost;
   @Value("${conf.db.port}")
   Integer dbPort;
   @Value("${conf.db.database}")
   String dbBase;
   @Value("${conf.db.user}")
   String dbUser;
   @Value("${conf.db.pass}")
   String dbPass;

   @Bean
   @ConfigurationProperties(prefix = "spring.datasource.r2dbc")
   public ConnectionFactoryOptions r2dbcConnectionFactoryOptions() {
      return ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "postgres")
            .option(ConnectionFactoryOptions.DATABASE, dbBase)
            .option(ConnectionFactoryOptions.HOST, dbHost)
            .option(ConnectionFactoryOptions.PORT, dbPort)
            .option(ConnectionFactoryOptions.USER, dbUser)
            .option(ConnectionFactoryOptions.PASSWORD, dbPass)
            // Configurações do datasource R2DBC
            .build();
   }

   @Bean
   public ConnectionFactory r2dbcConnectionFactory() {
      return ConnectionFactories.get(r2dbcConnectionFactoryOptions());
   }

   @Bean
   public DatabaseClient databaseClient(ConnectionFactory r2dbcConnectionFactory) {
      return DatabaseClient.builder()
            .connectionFactory(r2dbcConnectionFactory)
            .build();
   }

   @Bean(name = TransactionManagerUtil.R2DBC_TRANSACTION_MANAGER)
   public R2dbcTransactionManager r2dbcTransactionManager(ConnectionFactory r2dbcConnectionFactory) {
      return new R2dbcTransactionManager(r2dbcConnectionFactory);
   }

   @Bean(name = TransactionManagerUtil.JPA_TRANSACTION_MANAGER)
   public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
      return new JpaTransactionManager(entityManagerFactory);
   }

   // @Bean(name = "r2dbcTransactionManager")
   // public PlatformTransactionManager r2dbcTransactionManager(ConnectionFactory
   // connectionFactory) {
   // return new R2dbcTransactionManager(connectionFactory);
   // }

}
