package devops

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
public class Application {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate()
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args)
    }

}
