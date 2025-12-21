package com.student.career.zBase.controller;

import com.student.career.zBase.service.IService;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public abstract class AbstractController<E, D, C, S extends IService<E, D, C>, T extends AbstractTransformer<E, D>> {

    private final S service;
    private final T transformer;

    protected AbstractController(S service, T transformer) {
        this.service = service;
        this.transformer = transformer;
    }

    public ResponseEntity<List<D>> findAll() {
        List<E> result = service.findAll();
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(transformer.toDto(result));
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }

    }

    public ResponseEntity<D> findById(String id) {
        E foundedById = service.findById(id);
        if (foundedById != null) {
            return ResponseEntity.ok(transformer.toDto(foundedById));
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<D> save(D dto) {
        if (dto != null) {
            E e = transformer.toEntity(dto);
            e = service.save(e);
            if (e != null) {
                return new ResponseEntity<>(transformer.toDto(e), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<D> update(D dto) {
        if (dto != null) {
            E e = transformer.toEntity(dto);
            e = service.update(e);
            if (e != null) {
                return new ResponseEntity<>(transformer.toDto(e), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<Void> delete(D dto) {
        if (dto != null) {
            E e = transformer.toEntity(dto);
            service.delete(e);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<List<D>> findByCriteria(C criteria) {
        List<D> dtos = Collections.emptyList();
        if (criteria != null) {
            dtos = transformer.toDto(service.findByCriteria(criteria));
            return ResponseEntity.ok(dtos);
        } else {
            return new ResponseEntity<>(dtos, HttpStatus.NO_CONTENT);
        }
    }
}
