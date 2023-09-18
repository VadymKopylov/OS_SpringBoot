package com.kopylov.springbootonlineshop.dao.jdbc;

import com.kopylov.springbootonlineshop.dao.ProductsDao;
import com.kopylov.springbootonlineshop.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcProductsDao implements ProductsDao {

    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO products (name, price, creation_date) VALUES (?, ?, ?)";

    private static final String UPDATE_PRODUCT_SQL =
            "UPDATE products SET name=?, price=? WHERE id=?";

    private static final String DELETE_PRODUCT_SQL = "DELETE FROM products WHERE id=?";

    private static final String SELECT_ALL_PRODUCTS_SQL = "SELECT id, name, price, creation_date FROM products";

    private static final String SELECT_PRODUCT_BY_ID_SQL =
            "SELECT id, name, price, creation_date FROM products WHERE id=?";

    private static final String SELECT_PRODUCT_BY_NAME_SQL =
            "SELECT id, name, price, creation_date FROM products WHERE name=?";

    private static final BeanPropertyRowMapper<Product> PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(Product.class);

    private final JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PRODUCTS_SQL, PROPERTY_ROW_MAPPER);
    }

    public Optional<Product> findById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_PRODUCT_BY_ID_SQL, PROPERTY_ROW_MAPPER, id));
    }

    public List<Product> findByName(String name) {
        return jdbcTemplate.query(SELECT_PRODUCT_BY_NAME_SQL, PROPERTY_ROW_MAPPER, name);
    }

    public long save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL, new String[]{"id"});
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));
            return preparedStatement;
        }, keyHolder);

        return (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    public void update(Product product) {
        jdbcTemplate.update(UPDATE_PRODUCT_SQL, product.getName(), product.getPrice(), product.getId());
    }

    public void delete(long id) {
        jdbcTemplate.update(DELETE_PRODUCT_SQL, id);
    }
}

