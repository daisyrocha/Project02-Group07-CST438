package com.example.group07.project2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * This is how we will obtain data from our UserList entity
 */
@Controller
@RequestMapping(path = "/userListApi")
public class UserListApi {

    /**
     * User Autowired annotation knows to connect
     * to a database. It imports dependencies at the time
     * we need them.
     */
    @Autowired
    private UserListRepository userListRepository;

    /**
     * This path will actually return all users' lists
     * without us having to type so much code!
     */
    @GetMapping(path = "/allUserLists")
    public @ResponseBody Iterable<UserList> getAllUserLists() {
        return userListRepository.findAll();
    }

    /**
     * This path will return just the user from the id in the url,
     * or returns null.
     */
    @GetMapping(path = "/getUserListById")
    public @ResponseBody String getUserListById(@RequestParam @NonNull Integer id) {
        for(UserList i:userListRepository.findAll()){
            if(i.getUserListId().equals(id))
                return "UserListId:" + i.getUserListId() +
                        "listId: " + i.getUserId() + "," +
                        "list: " + i.getList() + "";
        }
        return "UserList not found.";
    }

    @GetMapping(path = "/getUserListByTitle")
    public @ResponseBody String getUserListByTitle(@RequestParam @NonNull String title) {
        for(UserList i:userListRepository.findAll()){
            if(i.getList().equals(title))
                return "UserListId:" + i.getUserListId() +
                        "listId: " + i.getUserId() + "," +
                        "list: " + i.getList() + "";
        }
        return "UserList not found.";
    }

    /**
     * This PostMapping will allow us to add users to our
     * database entity
     *
     * Literally this is all we need to add stuff to our database
     *
     * Honestly, idk if we need more parameters in the addList method,
     * the id is our primary key and the userId is a foreign key, so I don't think
     * we set it. I'll need to find out about it!!
     */
    @GetMapping(path="/addList")
    public @ResponseBody String addList (@RequestParam @NonNull String list) {
        UserList userList = new UserList();
        userList.setList(list);

        userListRepository.save(userList);

        return "Saved!";
    }

    /**
     * deleteUserList:
     * This detaches a UserList from the user and removes that
     * Userlist from the db.
     * TODO: add admin restriction/ from that same user
     */
    @GetMapping("/deleteUserList")
    public @ResponseBody String deleteUserList(@RequestParam @NonNull Integer userListID, @RequestParam @NonNull Integer userID) {
        String userListName = "";
        for(UserList currList: userListRepository.findAll()){
            if(currList.getUserId().equals(userID) && currList.getUserId().equals(userID)) {
                userListRepository.delete(currList);
                return "UserList was deleted.";
            }
        }

        return "UserList: " + userListName + " with ID: " + userListID + " was not found and could not be deleted.";
    }

    /**
     * UpdateUserList:
     * this updates the fields of a UpdateUserList on the system.
     * TODO: add admin restriction/ from that same user
     */
    @GetMapping("/updateUserList")
    public @ResponseBody String updateUserList(@RequestParam Integer userListId,
                                               @RequestParam(required = false) String userId,
                                               @RequestParam(required = false) String list) {
        for(UserList currUserList: userListRepository.findAll()){
            if(currUserList.getUserListId().equals(userListId)){
                if(!userId.isBlank())
                    currUserList.setUserId(Integer.getInteger(userId));

                if(!list.isBlank())
                    currUserList.setList(list);

                userListRepository.save(currUserList);
                return "UserList with ID:" + userListId + " was found and updated.";
            }
        }

        return "User with ID: " + userListId + " was not found and could not be updated.";
    }
}
