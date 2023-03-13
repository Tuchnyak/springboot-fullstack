package com.amigoscode.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {

    public static final String CUSTOMER_ID_SEQUENCE = "customer_id_sequence";

    @Id
    @SequenceGenerator(
            name = CUSTOMER_ID_SEQUENCE,
            sequenceName = CUSTOMER_ID_SEQUENCE
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = CUSTOMER_ID_SEQUENCE
    )

    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer age;

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
