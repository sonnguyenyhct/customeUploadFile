package config;

import aspect.MyLogger;
import formatter.ProvinceFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import repository.CustomerRepository;
import service.customer.CustomerService;
import service.customer.ICustomerService;
import service.province.IProvinceService;
import service.province.ProvinceService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("controller")
@PropertySource("classpath:uploadfile.properties")
@EnableJpaRepositories("repository")
@EnableSpringDataWebSupport
@EnableAspectJAutoProxy
public class AppConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        return thymeleafViewResolver;
    }

    @Bean
    public TemplateEngine templateEngine(){
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    //Config FileUpload
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        //Set the maximum allowed size (in bytes) for each individual file.
        resolver.setMaxUploadSizePerFile(-1);//unlimit

        //You may also set other available properties.

        return resolver;
    }

    @Autowired
    Environment env;
    // Cấu hình để sử dụng các file nguồn tĩnh (css, image, js..)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String uploadFile = env.getProperty("file_upload").toString();
        registry.addResourceHandler("/i/**").addResourceLocations("file:" + uploadFile );
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //Cau hinh Hibernate, database

    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    //Tim nhung entity trong model.
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(new String[]{"model"});
        em.setDataSource(dataSource());
        //Khai bao Hibernate
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/demospring");
        dataSource.setUsername( "root" );
        dataSource.setPassword( "sonyhct" );
        return dataSource;
    }

    //De su dung @Transactional
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
    //dialect : Sinh ra khoa ngoai
    Properties additionalProperties(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }

    @Bean
    public ICustomerService iCustomerService(){
        return new CustomerService();
    }

    @Bean
    public IProvinceService iProvinceService(){
        return new ProvinceService();
    }
    /*@Bean
    public CustomerRepository customerRepository(){
        return new CustomerRepositoryImpl();
    }*/

    /*@Bean
    public IProductService iProductService(){
        return new ProductService();
    }

    @Bean
    public ProductRepository productRepository(){
        return new ProductRepositoryImpl();
    }*/
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new ProvinceFormatter(applicationContext.getBean(ProvinceService.class)));
    }

    @Bean
    public MyLogger myLogger(){
        return new MyLogger();
    }

}
