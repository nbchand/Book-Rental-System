package com.nbchand.brs.service;

import com.nbchand.brs.dto.response.ResponseDto;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
public interface GenericCrudService<T> {
    public ResponseDto saveEntity(T t);

    public List<T> findAllEntities();

    public ResponseDto findEntityById(Integer id);

    public ResponseDto deleteEntityById(Integer id);
}
