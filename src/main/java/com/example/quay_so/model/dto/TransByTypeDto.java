package com.example.quay_so.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransByTypeDto {
    String requestType;
    List<TransDetailDto> transDetailDtos;
}
