package az.edu.turing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    T save(T t);
    T create(T t);
    T update(T t);
    List<T> findAll();
    Optional<T> findById(Long id);
    boolean deleteById(Long id);

}
