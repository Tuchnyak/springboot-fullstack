package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();

        // Then
        Mockito.verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        // Givem
        int id = 1;

        // When
        underTest.selectCustomerById(id);

        // Then
        Mockito.verify(customerRepository)
                .findById(id);
    }

    @Test
    void insertCustomer() {
        // Givem
        Customer customer = new Customer();

        // When
        underTest.insertCustomer(customer);

        // Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Givem
        String email = "test";

        // When
        underTest.existsPersonWithEmail(email);

        // Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        // Givem
        Integer id = 1;

        // When
        underTest.existsPersonWithId(id);

        // Then
        Mockito.verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Givem
        Integer id = 1;

        // When
        underTest.deleteCustomerById(id);

        // Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Givem
        Customer customer = new Customer();

        // When
        underTest.insertCustomer(customer);

        // Then
        Mockito.verify(customerRepository).save(customer);
    }
}