package com.amigoscode.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "cons_customer_email_unique",
                        columnNames = {"email"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {

    public static final String CUSTOMER_ID_SEQUENCE = "customer_id_seq";

    @Id
    @SequenceGenerator(
            name = CUSTOMER_ID_SEQUENCE,
            sequenceName = CUSTOMER_ID_SEQUENCE,
//            initialValue = 1,
            allocationSize = 1
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
