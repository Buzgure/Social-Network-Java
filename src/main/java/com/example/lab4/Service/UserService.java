package com.example.lab4.Service;

import com.example.lab4.Domain.User;
import com.example.lab4.Repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserService {
    private Repository<Long, User> repository;

    public UserService(Repository<Long, User> repository) {
        this.repository = repository;
    }

    public Optional<User> addUser(User user) {
        return repository.save(user);
    }

    public List<User> getAllUsers() {
        Iterable<User> users = repository.findAll();
//        List<User> result = new ArrayList<User>();
//        users.forEach(result::add);


        //          OR          //


        return StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
    }

    public Optional<User> deleteUser(String userName) {
        User user = findUser(userName);
        return repository.delete(user.getId());
    }

    public Optional<User> updateUser(String userName, String lastName, String firstName, String password) {
        User user = findUser(userName);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        return repository.update(user);
    }

    public User findUser(String userName) {
        List<User> users = getAllUsers();
        User user = null;
        for(User u: users)
            if(userName.equals(u.getUserName()))
                user = u;
        return user;
    }

    public void addFriend(User user, String userName){
        User u = findUser(userName);
        u.addFriend(user);
    }

    public void deleteFriend(User user,String userName){
        User u = findUser(userName);
        user.deleteFriend(u);
    }

}
