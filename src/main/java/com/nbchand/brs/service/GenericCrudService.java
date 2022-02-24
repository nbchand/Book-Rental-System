package com.nbchand.brs.service;

import com.nbchand.brs.dto.author.AuthorDto;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
public interface GenericCrudService<T> {
    public void saveEntity(T t);
    public List<T> findAllEntities();
    public T findEntityById(Integer id);
    public void deleteEntityById(Integer id);
}
