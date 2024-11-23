package com.example.quay_so.model.response.transaction;

import com.example.quay_so.model.dto.TransByTypeDto;
import com.example.quay_so.model.response.TotalResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransPendingResponse {
    private List<TransByTypeDto> transDetailResponseList;
    private TotalResponse totalResponse;
}

