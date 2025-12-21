package com.student.career.zBase.service;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface IService<E, D, C> {

    List<E> findAll();

    E findById(String id);

    E save(E entity);

    E update(E entity);

    void delete(E entity);

    List<E> findByCriteria(C criteria);

    void exportToPdf(HttpServletResponse response, List<D> dtos, List<String> fieldsShow);

    void exportToExcel(HttpServletResponse response, List<D> dtos, List<String> fieldsShow);

    void exportToCsv(HttpServletResponse response, List<D> dtos, List<String> fieldsShow);

}
