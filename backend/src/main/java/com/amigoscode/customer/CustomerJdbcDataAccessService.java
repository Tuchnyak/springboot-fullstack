package com.amigoscode.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
public class CustomerJdbcDataAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    @Override
    public List<Customer> selectAllCustomers() {
        String sql = """
                SELECT id, name, email, age FROM customer;
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        String sql = """
                SELECT id, name, email, age FROM customer
                WHERE id = ?;
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?);
                """;
        jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        String sql = "SELECT count(email) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getInt(1),
                email
        );

        return count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        String sql = "SELECT count(id) FROM customer WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getInt(1),
                id
        );

        return count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer customerToUpdate) {
        if (customerToUpdate.getName() != null) {
            String sql = """
                    UPDATE customer SET name = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql,
                    customerToUpdate.getName(),
                    customerToUpdate.getId()
            );
        }
        if (customerToUpdate.getEmail() != null) {
            String sql = """
                    UPDATE customer SET email = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql,
                    customerToUpdate.getEmail(),
                    customerToUpdate.getId()
            );
        }
        if (customerToUpdate.getAge() != null) {
            String sql = """
                    UPDATE customer SET age = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql,
                    customerToUpdate.getAge(),
                    customerToUpdate.getId()
            );
        }
    }
}
