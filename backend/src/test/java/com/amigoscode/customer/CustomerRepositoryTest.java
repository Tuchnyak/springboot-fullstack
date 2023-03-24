package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository customerRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        customerRepositoryUnderTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        customerRepositoryUnderTest.save(customer);

        // When
        var actual = customerRepositoryUnderTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        var actual = customerRepositoryUnderTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        customerRepositoryUnderTest.save(customer);

        Integer id = customerRepositoryUnderTest.findAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = customerRepositoryUnderTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWhenIdNotPresent() {
        // Given
        Integer id = -1;

        // When
        var actual = customerRepositoryUnderTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
    }

}