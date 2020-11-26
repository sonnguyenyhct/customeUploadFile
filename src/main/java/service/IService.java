package service;

import exception.NotFoundException;

public interface IService<T> {

    Iterable<T> selectAll();
    T findById(Long id) throws NotFoundException;
    void save(T t);
    void update(T t);
    void remote(Long id);
}
