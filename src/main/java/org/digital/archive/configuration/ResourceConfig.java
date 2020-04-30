package org.digital.archive.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Haytham DAHRI
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    /**
     * Register paths
     *
     * @param registry: Registry object
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }

}
