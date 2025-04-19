package az.edu.turing.model.mapper;

import az.edu.turing.model.entity.Passenger;

public interface Mapper<T,K> {
    K toDTO(T t);
    T toEntity(K k);
    T toEntity(T t ,K k);
}
