package uk.ac.ebi.eva.vcfdump.server.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class VcfDumperWSConfig implements WebMvcConfigurer {

    @Bean
    public ThreadPoolTaskExecutor mvcAsyncThreadPool() {
        // this pool will be used by to handle async requests in the MVC controllers
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        // TODO: override those default values
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(10);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(mvcAsyncThreadPool());
        long milliseconds = 300000L;
        configurer.setDefaultTimeout(milliseconds);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Advertise a relative server URL so Swagger UI calls the API using the scheme and host the page was loaded from.
     */
    @Bean
    public OpenAPI apiConfiguration(@Value("${server.servlet.context-path:/}") String contextPath) {
        return new OpenAPI()
                .servers(List.of(new Server().url(contextPath)))
                .info(new Info()
                        .title("European Variation Archive VCF Dumper REST Web Services API")
                        .version("1.0")
                        .contact(new Contact()
                                .name("the European Variation Archive team")
                                .url("www.ebi.ac.uk/eva")
                                .email("eva-helpdesk@ebi.ac.uk"))
                        .license(new License()
                                .name("Apache License Version 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

}
