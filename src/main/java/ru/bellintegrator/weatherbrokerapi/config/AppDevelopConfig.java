package ru.bellintegrator.weatherbrokerapi.config;

import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jms.AtomikosConnectionFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.jms.Destination;
import javax.jms.XAConnectionFactory;
import javax.transaction.SystemException;
import java.util.Properties;

@Configuration
@Profile("develop")
public class AppDevelopConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    @DependsOn("transactionServiceImp")
    public UserTransactionManager jtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);

        return userTransactionManager;
    }

    @Bean
    @DependsOn("transactionServiceImp")
    public UserTransactionImp jtaUserTransaction() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(300);

        return userTransactionImp;
    }

    @Bean
    @DependsOn("transactionServiceImp")
    public JtaTransactionManager transactionManager() throws SystemException {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager(jtaTransactionManager());
        transactionManager.setUserTransaction(jtaUserTransaction());

        return transactionManager;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactoryBean());
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDefaultDestination((Destination) destinationJndi().getObject());
        jmsTemplate.setSessionTransacted(true);

        return jmsTemplate;
    }

    @Bean
    public JndiObjectFactoryBean destinationJndi() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("java:jboss/exported/jms/topic/messageBoxTopic");
        jndiObjectFactoryBean.setResourceRef(true);

        return jndiObjectFactoryBean;
    }

    @Bean
    public JndiObjectFactoryBean connectionFactoryJndi() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("java:/JmsXA");
        jndiObjectFactoryBean.setResourceRef(true);

        return jndiObjectFactoryBean;
    }

    @Bean
    public DefaultJmsListenerContainerFactory containerFactory() throws SystemException {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactoryBean());
        containerFactory.setConcurrency("1-1");
        containerFactory.setSessionTransacted(true);
        containerFactory.setPubSubDomain(true);
        containerFactory.setTransactionManager(transactionManager());

        return containerFactory;
    }

    @Bean
    @Primary
    public AtomikosConnectionFactoryBean connectionFactoryBean() {
        AtomikosConnectionFactoryBean connectionFactoryBean = new AtomikosConnectionFactoryBean();
        connectionFactoryBean.setUniqueResourceName("atomikosConnectionFactoryCustomBean");
        connectionFactoryBean.setXaConnectionFactory((XAConnectionFactory) connectionFactoryJndi().getObject());
        connectionFactoryBean.setMaxPoolSize(10);

        return connectionFactoryBean;
    }

    @Bean(name = "transactionServiceImp", initMethod = "init", destroyMethod = "shutdownForce")
    UserTransactionServiceImp transactionServiceImp() {
        Properties properties = new Properties();
        properties.setProperty("com.atomikos.icatch.log_base_name", "my_custom_log_file");

        UserTransactionServiceImp transactionServiceImp = new UserTransactionServiceImp(properties);

        return transactionServiceImp;
    }
}
