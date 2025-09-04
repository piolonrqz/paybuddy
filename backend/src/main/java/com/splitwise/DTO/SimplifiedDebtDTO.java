package com.splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimplifiedDebtDTO {
    private String fromUser;
    private String toUser;
    private Double amount;
}
