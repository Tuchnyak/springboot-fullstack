package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotChangedException;
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
        if (!customerDAO.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("There is no customer with ID = %s".formatted(id));
        }
        var customerOptional = customerDAO.selectCustomerById(id);
        boolean isChangeExists = checkChanges(customerOptional.get(), request);
        if (!isChangeExists) {
            throw new ResourceNotChangedException("no data changes found");
        }

        var customer = customerOptional.get();
        if (request.name() != null)
            customer.setName(request.name());
        if (request.email() != null)
            customer.setEmail(request.email());
        if (request.age() != null)
            customer.setAge(request.age());

        customerDAO.insertCustomer(customer);
    }

    private boolean checkChanges(Customer customer, CustomerUpdateRequest request) {
        boolean changes = false;

        if (request.name() != null && !request.name().equals(customer.getName()))
            changes = true;
        if (request.email() != null && !request.email().equals(customer.getEmail()))
            changes = true;
        if (request.age() != null && !request.age().equals(customer.getAge()))
            changes = true;

        return changes;
    }
}
