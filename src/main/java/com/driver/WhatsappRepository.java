package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {
    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String name, String mobile) throws Exception
    {
        User user = new User();
        if(!userMobile.contains(mobile)){
            user.setName(name);
            user.setMobile(mobile);
            userMobile.add(mobile);
            return "SUCCESS";
        }
        return null;
    }
    public Group createGroup(List<User> users){
        int count = customGroupCount+1;
        List<User> list = users;
        int length = users.size();
        Group group = new Group();
        if(length > 2){
            String groupName = "Group "+count;
            group.setName(groupName);
            group.setNumberOfParticipants(length);
            groupUserMap.put(group,list);
            customGroupCount++;
        }else{
            String groupName = users.get(1).getName();
            group.setName(groupName);
            group.setNumberOfParticipants(length);
            groupUserMap.put(group,list);
        }
        adminMap.put(group,list.get(0));
        return group;
    }

    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        Message message = new Message();
        messageId = messageId + 1;
        message.setId(messageId);
        message.setContent(content);
        Date date = new Date();
        message.setTimestamp(date);
        return messageId;
    }

    public boolean isPresent(Group group){
        if(groupUserMap.containsKey(group)){
            return true;
        }
        return false;
    }

    public List<User> getGroupUsersList(Group group){
        return groupUserMap.get(group);
    }

    public int sendMessage(Message message, User sender, Group group){
        List<Message> list = groupMessageMap.getOrDefault(group,new ArrayList<>());
        list.add(message);
        groupMessageMap.put(group,list);
        senderMap.put(message,sender);
        return list.size();
    }

    public User getAdmin(Group group){
        return groupUserMap.get(group).get(0);
    }

    public String changeAdmin(User approver, User user, Group group){
        //remove admin from the admin map
        //add user in the admin map for the group
        adminMap.remove(group);
        adminMap.put(group,user);
        return "SUCCESS";
    }
}