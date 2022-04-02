package com.nbchand.brs.service;

import com.nbchand.brs.dto.ResponseDto;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
public interface GenericCrudService<T> {
    ResponseDto saveEntity(T t);

    List<T> findAllEntities();

    ResponseDto findEntityById(Integer id);

    ResponseDto deleteEntityById(Integer id);
}
