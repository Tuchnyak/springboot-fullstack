package com.amigoscode.customer;

public class CustomerMapper {
    private final CustomerRegistrationRequest customerRegistrationRequest;

    public CustomerMapper(CustomerRegistrationRequest customerRegistrationRequest) {
        this.customerRegistrationRequest = customerRegistrationRequest;
    }

    public Customer map() {
        return new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
    }
}
