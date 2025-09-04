package com.splitwise.Controller;

import com.splitwise.DTO.GroupDTO;
import com.splitwise.DTO.GroupMemberDTO;
import com.splitwise.Service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    //Create group
    @PostMapping("/create")
    public GroupDTO createGroup(@RequestParam String groupName, @RequestParam Long userId) {
        return groupService.createGroup(groupName, userId);
    }

    //Add member
    @PostMapping("/{groupId}/addMember")
    public GroupMemberDTO addMember(@PathVariable Long groupId, @RequestParam Long userId) {
        return groupService.addMember(groupId, userId);
    }

    //Get groups of a user
    @GetMapping("/user/{userId}")
    public List<GroupDTO> getUserGroups(@PathVariable Long userId) {
        return groupService.getUserGroups(userId);
    }

    //Get members of a group
    @GetMapping("/{groupId}/members")
    public List<GroupMemberDTO> getGroupMembers(@PathVariable Long groupId) {
        return groupService.getGroupMembers(groupId);
    }
}
