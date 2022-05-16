package dev.bettercode.tasks

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.TransactionManager
import java.util.*
import javax.sql.DataSource


@Import(TasksConfiguration::class)
@Configuration
class TasksTestConfiguration {
    @Bean
    fun getDataSource(): DataSource? {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.h2.Driver")
        dataSourceBuilder.url("jdbc:h2:mem:test;MODE=MYSQL")
        dataSourceBuilder.username("SA")
        dataSourceBuilder.password("")
        val ds = dataSourceBuilder.build()

        val initSchema: Resource = ClassPathResource("schema-h2.sql")
        DatabasePopulatorUtils.execute(ResourceDatabasePopulator(initSchema), ds)

        return ds
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan("dev.bettercode.tasks.infra.adapter.db")
        val vendorAdapter: JpaVendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        em.persistenceUnitName = "entityManager"
        val props = Properties()
        props.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.H2Dialect")
        props.setProperty("hibernate.hbm2ddl.auto", "update")
        em.setJpaProperties(props)
        return em
    }

    @Bean
    fun transactionManager(entityManagerFactoryBean: LocalContainerEntityManagerFactoryBean): TransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactoryBean.getObject()
        return transactionManager
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}