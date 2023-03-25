package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(CustomerRepository repository) {
        return args -> {
            Faker faker = Faker.instance();
            String name = faker.name().firstName();
            SecureRandom random = new SecureRandom();
            int age = random.nextInt(16, 121);

            List<Customer> customers = new ArrayList<>();
            customers.add(new Customer(
                            name,
                            faker.internet().safeEmailAddress(),
                            age
                    )
            );

            repository.saveAll(customers);
        };
    }

}
