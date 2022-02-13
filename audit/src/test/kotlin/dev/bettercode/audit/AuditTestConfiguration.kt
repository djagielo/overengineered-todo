package dev.bettercode.audit

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.TransactionManager
import java.util.*
import javax.sql.DataSource

@Import(AuditConfiguration::class)
@Configuration
class AuditTestConfiguration {
    @Bean
    fun getDataSource(): DataSource? {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.h2.Driver")
        dataSourceBuilder.url("jdbc:h2:mem:test;MODE=MYSQL")
        dataSourceBuilder.username("SA")
        dataSourceBuilder.password("")
        return dataSourceBuilder.build()
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan("dev.bettercode.audit.repository")
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
}