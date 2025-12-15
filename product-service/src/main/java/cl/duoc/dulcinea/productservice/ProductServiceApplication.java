package cl.duoc.dulcinea.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        System.out.println("=========================================");
        System.out.println("🚀 Product Service iniciado correctamente");
        System.out.println("📦 H2 Console: http://localhost:8082/h2-console");
        System.out.println("🔗 JDBC URL: jdbc:h2:mem:productdb");
        System.out.println("👤 Usuario: sa");
        System.out.println("🔑 Contraseña: (vacío)");
        System.out.println("=========================================");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}