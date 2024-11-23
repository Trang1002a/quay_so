package com.example.quay_so.model.request.rate;

import com.example.quay_so.model.request.transaction.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentsRequest {
    private String bookName;
    private String userName;
    private List<String> status;
    private PageRequest pageRequest;
}
