package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
            List<Customer> customers = new ArrayList<>();
            customers.add(new Customer(
                            1,
                            "Alex",
                            "alex@gmail.com",
                            21
                    )
            );
            customers.add(new Customer(
                            2,
                            "Jamile",
                            "jamile@gmail.com",
                            23
                    )
            );

            repository.saveAll(customers);
        };
    }
}
