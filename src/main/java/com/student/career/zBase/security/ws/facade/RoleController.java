package com.student.career.zBase.security.ws.facade;


import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.dao.criteria.RoleCriteria;
import com.student.career.zBase.security.service.facade.RoleService;
import com.student.career.zBase.security.ws.dto.RoleDto;
import com.student.career.zBase.security.ws.transformer.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {


    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleTransformer roleTransformer;

    @GetMapping("/")
    public ResponseEntity<List<RoleDto>> findAll() {
        return ResponseEntity.ok(roleTransformer.toDto(roleService.findAll()));
    }


    @PostMapping("/")
    public ResponseEntity<RoleDto> save(@RequestBody RoleDto roleDto) {
        Role role = roleTransformer.toEntity(roleDto);
        role = roleService.save(role);
        return new ResponseEntity<>(roleTransformer.toDto(role), HttpStatus.CREATED);
    }


    @PutMapping("/")
    public ResponseEntity<RoleDto> update(@RequestBody RoleDto roleDto) {
        Role role = roleTransformer.toEntity(roleDto);
        role = roleService.update(role);
        return new ResponseEntity<>(roleTransformer.toDto(role), HttpStatus.OK);
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        roleService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(roleTransformer.toDto(roleService.findById(id)));
    }

    @PostMapping("/find-by-criteria")
    public ResponseEntity<List<RoleDto>> findByCriteria(@RequestBody RoleCriteria roleCriteria) {
        return ResponseEntity.ok(roleTransformer.toDto(roleService.findByCriteria(roleCriteria)));
    }

}
