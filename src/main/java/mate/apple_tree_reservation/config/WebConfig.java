package mate.apple_tree_reservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://port-0-apple-tree-v1-1mrfs721lwuqd2yb.sel5.cloudtype.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Custom-Header")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
