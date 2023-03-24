package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)     // instead autocloseable boilerplate code
class CustomerServiceTest {

    private static final Faker FAKER = Faker.instance();

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;
//    private AutoCloseable autoCloseable;   // instead autocloseable boilerplate code

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);   // instead autocloseable boilerplate code
        underTest = new CustomerService(customerDAO);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();   // instead autocloseable boilerplate code
//    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        // Given
        int id = 10;
        Customer customer = new Customer();
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        int id = 10;
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("There is no customer with ID = %s".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                FAKER.name().fullName(),
                email,
                20
        );

        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        // When
        underTest.addCustomer(registrationRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(registrationRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(registrationRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(registrationRequest.age());
    }

    @Test
    void willThrowWhenAddCustomerWithTakenEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress();
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                FAKER.name().fullName(),
                email,
                20
        );

        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addCustomer(registrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;
        Mockito.when(customerDAO.existsPersonWithId(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenDeleteCustomerByIdWithIdNotFound() {
        // Given
        int id = 1;
        Mockito.when(customerDAO.existsPersonWithId(id)).thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("There is no customer with ID = %s".formatted(id));

        verify(customerDAO, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "mail", 19);
        String email = FAKER.internet().safeEmailAddress();
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                FAKER.name().fullName(),
                email,
                20
        );
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer captured = customerArgumentCaptor.getValue();

        assertThat(customer.getId()).isEqualTo(id);
        assertThat(captured.getName()).isEqualTo(updateRequest.name());
        assertThat(captured.getEmail()).isEqualTo(updateRequest.email());
        assertThat(captured.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenUpdateCustomerWithoutChanges() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "mail", 20);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .withFailMessage("no data changes found");

        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerWhenEmailExists() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "mail", 20);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),
                FAKER.internet().safeEmailAddress(),
                customer.getAge()
        );
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        Mockito.when(customerDAO.existsPersonWithEmail(updateRequest.email())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(customerDAO, never()).updateCustomer(any());
    }
}