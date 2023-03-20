package com.amigoscode.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper underTest = new CustomerRowMapper();
        ResultSet rs = Mockito.mock(ResultSet.class);

        Mockito.when(rs.getInt("id")).thenReturn(1);
        Mockito.when(rs.getString("name")).thenReturn("Alex");
        Mockito.when(rs.getString("email")).thenReturn("hismail");
        Mockito.when(rs.getInt("age")).thenReturn(20);

        // When
        Customer actual = underTest.mapRow(rs, 1);

        // Then
        Customer expected = new Customer(1, "Alex", "hismail", 20);

        assertThat(actual).isEqualTo(expected);
    }

}