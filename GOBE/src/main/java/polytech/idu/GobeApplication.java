package polytech.idu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "polytech.idu.repositories")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "polytech.idu.models")
public class GobeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GobeApplication.class, args);
    }
}
