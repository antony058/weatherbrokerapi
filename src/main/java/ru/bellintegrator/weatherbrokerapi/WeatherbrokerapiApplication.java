package ru.bellintegrator.weatherbrokerapi;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@SpringBootApplication
@EnableTransactionManagement
public class WeatherbrokerapiApplication extends SpringBootServletInitializer {

	@Value("${jdbc.serverName}")
	private String serverName;

	@Value("${jdbc.portNumber}")
	private String portNumber;

	@Value("${jdbc.databaseName}")
	private String databaseName;

	@Value("${jdbc.user}")
	private String user;

	@Value("${jdbc.password}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(WeatherbrokerapiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(applicationClass);
	}

	private static Class<WeatherbrokerapiApplication> applicationClass = WeatherbrokerapiApplication.class;

	@Bean
	public Properties properties() {
		Properties properties = new Properties();
		properties.setProperty("serverName", serverName);
		properties.setProperty("portNumber", portNumber);
		properties.setProperty("databaseName", databaseName);
		properties.setProperty("user", user);
		properties.setProperty("password", password);

		return properties;
	}

	@Bean
	public AtomikosDataSourceBean dataSource() {
		AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
		dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
		dataSource.setXaProperties(properties());
		dataSource.setUniqueResourceName("postgresDatasourceCustomBean");
		dataSource.setPoolSize(1);

		return dataSource;
	}
}
