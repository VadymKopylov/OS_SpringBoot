package com.kopylov.onlineshoponspringboot.dao.jdbc;

import com.kopylov.onlineshoponspringboot.dao.ProductsDao;
import com.kopylov.onlineshoponspringboot.model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcProductsDao implements ProductsDao {

    private static final String INSERT_PRODUCT_SQL = """
            INSERT INTO products (name, price, creation_date) VALUES (:name, :price, :creation_date);
            """;
    private static final String UPDATE_PRODUCT_SQL = """
            UPDATE products SET name = :name, price = :price, creation_date = :creation_date WHERE id = :id;
            """;
    private static final String DELETE_PRODUCT_SQL = """
            DELETE FROM products WHERE id = ?;
            """;
    private static final String SELECT_ALL_PRODUCTS_SQL = """
            SELECT id, name, price, creation_date FROM products;
            """;

    private static final String SELECT_PRODUCT_BY_ID_SQL = """
            SELECT id, name, price, creation_date FROM products WHERE id=?;
            """;

    private static final String SELECT_PRODUCT_BY_NAME_SQL = """
            SELECT id, name, price, creation_date FROM products WHERE name=?;
            """;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcProductsDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Product> index() {
        return jdbcTemplate.query(SELECT_ALL_PRODUCTS_SQL, new BeanPropertyRowMapper<>(Product.class));
    }

    public Optional<Product> show(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_PRODUCT_BY_ID_SQL,
                new BeanPropertyRowMapper<>(Product.class), id));
    }

    public List<Product> show(String name) {
        return jdbcTemplate.query(SELECT_PRODUCT_BY_NAME_SQL, new Object[]{name}, new BeanPropertyRowMapper<>(Product.class));
    }

    public void save(Product product) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", product.getName());
        mapSqlParameterSource.addValue("price", product.getPrice());
        mapSqlParameterSource.addValue("creation_date", Timestamp.valueOf(product.getCreationDate()));
        namedParameterJdbcTemplate.update(INSERT_PRODUCT_SQL, mapSqlParameterSource);
    }

    public void update(Product product) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", product.getName());
        mapSqlParameterSource.addValue("price", product.getPrice());
        mapSqlParameterSource.addValue("creation_date", Timestamp.valueOf(product.getCreationDate()));
        mapSqlParameterSource.addValue("id", product.getId());
        namedParameterJdbcTemplate.update(UPDATE_PRODUCT_SQL, mapSqlParameterSource);
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE_PRODUCT_SQL, id);
    }
}

