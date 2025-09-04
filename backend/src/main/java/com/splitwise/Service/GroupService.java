package com.splitwise.Service;

import com.splitwise.DTO.GroupDTO;
import com.splitwise.DTO.GroupMemberDTO;
import com.splitwise.Entities.Group;
import com.splitwise.Entities.GroupMember;
import com.splitwise.Entities.User;
import com.splitwise.Repository.GroupMemberRepository;
import com.splitwise.Repository.GroupRepository;
import com.splitwise.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    //Create new group
    public GroupDTO createGroup(String groupName, Long createdByUserId) {
        User creator = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = new Group();
        group.setGroupName(groupName);
        group.setCreatedBy(creator);

        Group savedGroup = groupRepository.save(group);

        GroupMember gm = new GroupMember();
        gm.setGroup(savedGroup);
        gm.setUser(creator);
        groupMemberRepository.save(gm);

        return new GroupDTO(savedGroup.getId(), groupName, creator.getEmail());
    }

    //Add member to group
    public GroupMemberDTO addMember(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupMember> existing = groupMemberRepository.findByGroupId(groupId);
        boolean alreadyMember = existing.stream().anyMatch(gm -> gm.getUser().getId().equals(userId));
        if (alreadyMember) throw new RuntimeException("User already in group");

        GroupMember gm = new GroupMember();
        gm.setGroup(group);
        gm.setUser(user);
        groupMemberRepository.save(gm);

        return new GroupMemberDTO(groupId, user.getEmail());
    }

    //Get all groups of user
    public List<GroupDTO> getUserGroups(Long userId) {
        List<GroupMember> memberships = groupMemberRepository.findByUserId(userId);
        List<GroupDTO> result = new ArrayList<>();
        for (GroupMember gm : memberships) {
            Group g = gm.getGroup();
            result.add(new GroupDTO(g.getId(), g.getGroupName(), g.getCreatedBy().getEmail()));
        }
        return result;
    }

    //Get all members of a group
    public List<GroupMemberDTO> getGroupMembers(Long groupId) {
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        List<GroupMemberDTO> result = new ArrayList<>();
        for (GroupMember gm : members) {
            result.add(new GroupMemberDTO(groupId, gm.getUser().getEmail()));
        }
        return result;
    }
}
