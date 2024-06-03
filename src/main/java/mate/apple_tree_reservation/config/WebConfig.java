package mate.apple_tree_reservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:8081","http://localhost:3000",
                        "https://port-0-apple-tree-v1-1mrfs72llwuqd2yb.sel5.cloudtype.app") // 허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true) // 쿠키 인증 요청 허용
                .maxAge(3600); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱 (초 단위)
    }
}