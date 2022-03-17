package com.example.group07.project2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Entry point for our API. This is how we will obtain our user data
 */
@Controller
@RequestMapping(path="/userApi")
public class UserApi {

    /**
     * User Autowired annotation knows to connect
     * to a database. It imports dependencies at the time
     * we need them.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * This path will actually return all users without
     * us having to type so much code!
     */
    @GetMapping(path = "/allUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * This PostMapping will allow us to add users to our
     * database entity
     *
     * Literally this is all we need to add stuff to our database
     */
    @GetMapping(path="/addUser")
    public @ResponseBody String addUser (@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userRepository.save(user);

        return "Saved User!";
    }

    /**
     * This path will return just the user from the id in the url,
     * or returns null.
     */
    @GetMapping(path = "/getUserById")
    public @ResponseBody User getUserById(@RequestParam @NonNull Integer id) {
        for(User i:userRepository.findAll()){
            if(i.getUserId().equals(id))
                return i;
        }
        return null;
    }

    @GetMapping(path="/findByName")
    public @ResponseBody
    List<User> findUserByName(@RequestParam (defaultValue = "daisy") String name) {
        return userRepository.findUserByName(name);
    }

    /**
     * deleteUser:
     * deletes user from db table.
     * TODO: add admin restriction/ from that same user
     */
    @GetMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(@RequestParam @NonNull Integer userID) {
        String userName;
        for(User currUser: userRepository.findAll()){
            if(currUser.getUserId().equals(userID)){
                userName = currUser.getUsername();
                userRepository.delete(currUser);
                return "Item: " + userName + " with ID:" + userID + " was found and deleted.";
            }
        }

        return "Item with ID: " + userID + " was not found and could not be deleted.";
    }

    /**
     * UpdateUser:
     * this updates the fields of a User on the system.
     * TODO: add admin restriction/ from that same user
     */
    @GetMapping("/updateUser")
    public @ResponseBody String updateUser(@RequestParam Integer userID,
                                           @RequestParam(required = false) String username,
                                           @RequestParam(required = false) String password) {
        for(User currUser: userRepository.findAll()){
            if(currUser.getUserId().equals(userID)){
                if(!username.isBlank())
                    currUser.setUsername(username);

                if(!password.isBlank())
                    currUser.setPassword(password);

                userRepository.save(currUser);

                return "User: " + currUser.getUsername() + " with ID:" + userID + " was found and updated.";
            }
        }

        return "User with ID: " + userID + " was not found and could not be updated.";
    }
}
