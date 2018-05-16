package ru.bellintegrator.weatherbrokerapi.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jms.AtomikosConnectionFactoryBean;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.transaction.SystemException;

@Configuration
@Profile("test")
public class AppTestConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager jtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);

        return userTransactionManager;
    }

    @Bean
    public UserTransactionImp jtaUserTransaction() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(300);

        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager transactionManager() throws SystemException {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager(jtaTransactionManager());
        transactionManager.setUserTransaction(jtaUserTransaction());

        return transactionManager;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(queue());
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setPubSubDomain(false);

        return jmsTemplate;
    }

    @Bean
    public ActiveMQQueue queue() {
        return new ActiveMQQueue("testQueue");
    }

    @Bean
    public ActiveMQXAConnectionFactory connectionFactory() {
        ActiveMQXAConnectionFactory connectionFactory = new ActiveMQXAConnectionFactory();
        connectionFactory.setBrokerURL("tcp://127.0.0.1:61616");

        return connectionFactory;
    }

    @Bean
    public JmsListenerContainerFactory containerFactory() throws SystemException {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactoryBean());
        factory.setTransactionManager(transactionManager());
        factory.setConcurrency("1-1");
        factory.setPubSubDomain(false);
        factory.setSessionTransacted(true);

        return factory;
    }

    @Bean
    public AtomikosConnectionFactoryBean connectionFactoryBean() {
        AtomikosConnectionFactoryBean connectionFactoryBean = new AtomikosConnectionFactoryBean();
        connectionFactoryBean.setUniqueResourceName("atomikosConnectionFactoryCustomBean");
        connectionFactoryBean.setXaConnectionFactory(connectionFactory());
        connectionFactoryBean.setMaxPoolSize(10);

        return connectionFactoryBean;
    }
}
