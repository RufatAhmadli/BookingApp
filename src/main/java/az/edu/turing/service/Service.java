package az.edu.turing.service;

import java.util.List;

public interface Service<T> {
    T create(T t);
    T update(Long id,T t);
    boolean deleteById(Long id);
    List<T> list();
    T findById(Long id);
}
