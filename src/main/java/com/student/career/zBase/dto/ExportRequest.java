package com.student.career.zBase.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExportRequest<D> {
    private List<D> dtos;
    private List<String> fieldsShow;
}
