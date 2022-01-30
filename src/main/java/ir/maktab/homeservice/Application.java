package ir.maktab.homeservice;

import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.entities.users.Expert;
import ir.maktab.homeservice.services.ExpertService;
import ir.maktab.homeservice.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    ExpertService expertService;

    @Autowired
    ServiceService serviceService;

    @Bean
    @Profile("!test")
    public CommandLineRunner initialization() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                Expert expert = new Expert();

                expert.setEmail("ehsan@gmail.com");
                expert.setPassword("12345678a");
                expert.setFirstName("Ali");
                expert.setLastName("ghlich");
                expert.setCredit(13.5);
                Expert saved = expertService.save(expert);

                Service service = new Service();
                service.setTitle("a1");
                Service assistance = serviceService.save(service);
                expertService.addAssistance(saved.getId(), assistance.getId());
            }
        };
    }
}
