package com.epam.esm.config;

import com.epam.esm.dao.src.main.java.GiftAndTagDao;
import com.epam.esm.dao.src.main.java.GiftCertificateDao;
import com.epam.esm.dao.src.main.java.TagDao;
import com.epam.esm.mapper.GiftCertificateRowMapper;
import com.epam.esm.mapper.TagRowMapper;
import com.epam.esm.service.src.main.java.GiftCertificateService;
import com.epam.esm.service.src.main.java.TagService;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@Profile("prod")
@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
public class ApplicationConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment environment;

    private static final String MESSAGE_SOURCE = "messageSource";
    private static final String MESSAGES_BASENAME = "languages/language";
    private static final String LOCALE = "locale";

    @Autowired
    public ApplicationConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty("database.driver"));
        dataSource.setUrl(environment.getProperty("database.url"));
        dataSource.setUsername(environment.getProperty("database.username"));
        dataSource.setPassword(environment.getProperty("database.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public GiftCertificateRowMapper giftCertificateRowMapper() {
        return new GiftCertificateRowMapper();
    }

    @Bean
    public TagRowMapper tagRowMapper() {
        return new TagRowMapper();
    }

    @Bean
    public GiftCertificateDao giftCertificateDao() {
        return new GiftCertificateDao(jdbcTemplate(), giftCertificateRowMapper());
    }

    @Bean
    public GiftCertificateService giftCertificateService() {
        return new GiftCertificateService(giftCertificateDao(),
                tagService(),
                giftAndTagDao(),
                giftCertificateValidator());
    }

    @Bean
    public GiftCertificateValidator giftCertificateValidator() {
        return new GiftCertificateValidator();
    }

    @Bean
    public TagDao tagDao() {
        return new TagDao(jdbcTemplate(), tagRowMapper());
    }

    @Bean
    public TagService tagService() {
        return new TagService(tagDao());
    }

    @Bean
    public GiftAndTagDao giftAndTagDao() {
        return new GiftAndTagDao(jdbcTemplate());
    }

    @Bean(MESSAGE_SOURCE)
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MESSAGES_BASENAME);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(LOCALE);
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
