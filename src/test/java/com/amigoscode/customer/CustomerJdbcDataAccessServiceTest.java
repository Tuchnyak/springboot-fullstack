package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJdbcDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper rowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(getJdbcTemplate(), rowMapper);
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> actualCustomers = underTest.selectAllCustomers();

        // Then
        assertThat(actualCustomers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actualCustomer = underTest.selectCustomerById(id);

        // Then
        assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        Integer id = -1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        // When
        underTest.insertCustomer(customer);
        var actual = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        // When
        var actual = underTest.existsPersonWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailFalse() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsPersonWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsPersonWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdFalse() {
        // Given
        Integer id = -1;

        // When
        var actual = underTest.existsPersonWithId(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(id);
        String newValue = "Mama Samba";
        customerToUpdate.setName(newValue);

        underTest.updateCustomer(customerToUpdate);

        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newValue);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(id);
        String newValue = "newmail@mail.so";
        customerToUpdate.setEmail(newValue);

        underTest.updateCustomer(customerToUpdate);

        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newValue);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(id);
        Integer newValue = 100;
        customerToUpdate.setAge(newValue);

        underTest.updateCustomer(customerToUpdate);

        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newValue);
        });
    }

    @Test
    void updateCustomerAllFields() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(id);
        customerToUpdate.setName("Samba");
        customerToUpdate.setEmail("sam@sam.com");
        customerToUpdate.setAge(1000);

        underTest.updateCustomer(customerToUpdate);

        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customerToUpdate.getName());
            assertThat(c.getEmail()).isEqualTo(customerToUpdate.getEmail());
            assertThat(c.getAge()).isEqualTo(customerToUpdate.getAge());
        });
    }

    @Test
    void updateCustomerNoUpdates() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(id);

        underTest.updateCustomer(customerToUpdate);

        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}