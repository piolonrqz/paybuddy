package com.splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMemberDTO {
    private Long groupId;
    private String userEmail;
}
