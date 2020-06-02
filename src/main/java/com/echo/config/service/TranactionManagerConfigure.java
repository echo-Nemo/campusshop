//package com.echo.config.service;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.TransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.TransactionManagementConfigurer;
//import javax.sql.DataSource;
//
//@Configuration
////开启事务支持
//@EnableTransactionManagement
//public class TranactionManagerConfigure implements TransactionManagementConfigurer {
//    private DataSource dataSource;
//
//    @Override
//    public TransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}
