package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {

    private static final List<Customer> CUSTOMERS;

    static {
        CUSTOMERS = new ArrayList<>();
        CUSTOMERS.add(new Customer(
                        1,
                        "Alex",
                        "alex@gmail.com",
                        21
                )
        );
        CUSTOMERS.add(new Customer(
                        2,
                        "Jamile",
                        "jamile@gmail.com",
                        23
                )
        );
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return CUSTOMERS;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return CUSTOMERS
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        CUSTOMERS.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return CUSTOMERS.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return CUSTOMERS.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var opt = selectCustomerById(id);
        opt.ifPresent(CUSTOMERS::remove);
    }

    @Override
    public void updateCustomer(Customer customerToUpdate) {
        CUSTOMERS.add(customerToUpdate);
    }
}
