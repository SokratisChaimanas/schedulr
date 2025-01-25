package gr.myprojects.schedulr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SchedulrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulrApplication.class, args);
    }

}
