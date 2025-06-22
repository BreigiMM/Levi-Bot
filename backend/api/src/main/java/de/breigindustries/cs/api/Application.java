package de.breigindustries.cs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.breigindustries.cs.api.services.UserService;
import de.breigindustries.cs.shared.models.User;

@SpringBootApplication(scanBasePackages = "de.breigindustries.cs")
public class Application {
    public static void main(String[] args) {
        // Start spring and get the context
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        // Get user service bean
        UserService userService = context.getBean(UserService.class);

        // Use the service
        User user = new User(1L, "breigi", "Mathis", "url", "url");

        userService.saveUser(user);
        System.out.println(userService.getAllUsers().get(0).getDisplayName());
        
    }
}
