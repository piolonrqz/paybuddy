package com.splitwise.Service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.splitwise.Repository.GroupMemberRepository;
import com.splitwise.Repository.UserRepository;
import com.splitwise.Repository.GroupRepository;
import com.splitwise.Entities.Group;
import com.splitwise.Entities.GroupMember;
import com.splitwise.Entities.User;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    
    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, UserRepository userRepository){
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }    

    //create new group
    public Group createGroup(String groupName, Long createdByUserId){
        User creator = userRepository.findById(createdByUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Group group = new Group();
            group.setGroupName(groupName);
            group.setCreatedBy(creator);

            Group savedGroup = groupRepository.save(group);


            //add creator as first member
            GroupMember gm  = new GroupMember();
            gm.setGroup(savedGroup);
            gm.setUser(creator);
            groupMemberRepository.save(gm);

            return savedGroup;
    }

    //add member to group
    public GroupMember addMember(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupMember> existing = groupMemberRepository.findByGroupId(groupId);
        boolean alreadyMember = existing.stream().anyMatch(gm -> gm.getUser().getId().equals(userId));

        if(alreadyMember) {
            throw new RuntimeException("User Already in  group");
        }

        GroupMember gm = new GroupMember();
        gm.setGroup(group);
        gm.setUser(user);

        return groupMemberRepository.save(gm);
    }


    //get all groups of user
    public List<GroupMember> getUserGroups(Long userId){
        return groupMemberRepository.findByUserId(userId);
    }

    //get all members of a group
    public List<GroupMember> getGroupMembers(Long groudId){
        return groupMemberRepository.findByGroupId(groudId);
    }
}
