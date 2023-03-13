package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
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

    public void updateCustomer(Integer id, CustomerUpdateRequest request) {
        var customer = getCustomer(id);

        boolean changes = false;

        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDAO.existsPersonWithEmail(request.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }
        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            changes = true;
        }
        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDAO.updateCustomer(customer);
    }

}
