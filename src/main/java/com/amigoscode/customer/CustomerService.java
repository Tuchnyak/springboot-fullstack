package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDAO.selectCustomerById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("There is no customer with ID = %s".formatted(id))
                );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDAO.existsPersonWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Email already taken");
        }
        customerDAO.insertCustomer(new CustomerMapper(customerRegistrationRequest).map());
    }

    public void deleteCustomerById(Integer id) {
        if (!customerDAO.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("There is no customer with ID = %s".formatted(id));
        }
        customerDAO.deleteCustomerById(id);
    }
}
