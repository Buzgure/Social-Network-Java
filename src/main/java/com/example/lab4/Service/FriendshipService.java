package com.example.lab4.Service;

import com.example.lab4.Domain.Friendship;
import com.example.lab4.Repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService {
    private Repository<Long, Friendship> repository;
    private UserService service;

    public FriendshipService(Repository<Long, Friendship> repository) {
        this.repository = repository;
    }

    public Optional<Friendship> addFriendship(Friendship friend){
        List<Friendship> allF = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        try{
            for(Friendship f: allF)
                if(f.equals(friend))
                    throw new IllegalArgumentException("A friendship between selected users already exists!");

            return repository.save(friend);
        } catch (Exception e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    public List<Friendship> getAllFriendships(){
        Iterable<Friendship> friendships = repository.findAll();

        return StreamSupport.stream(friendships.spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Friendship> deleteFriendship(String userName1, String userName2){
        Friendship friend = findFriendship(userName1, userName2);
        return repository.delete(friend.getId());

    }

    public Friendship findFriendship(String userName1, String userName2){
        List<Friendship> friendships = getAllFriendships();
        Friendship friend = null;
        for(Friendship f: friendships)
            if((userName1.equals(f.getFirstUser().getUserName()) && userName2.equals(f.getSecondUser().getUserName()))
                    || (userName2.equals(f.getFirstUser().getUserName()) &&
                    userName1.equals(f.getSecondUser().getUserName())))
                friend = f;
        return friend;
    }

//    public Optional<Friendship> updateFriendship(String userName, String newUser){
//        boolean isFirst = false;
//        List<Friendship> friend = getAllFriendships();
//        for(Friendship f: friend){
//            if(f.getFirstUser().getUserName().equals(userName) || f.getSecondUser().getUserName().equals(userName)){
//                User desiredUser = service.findUser(userName);
//                User userToBeFriendedWith = service.findUser(newUser);
//                LocalDateTime date = LocalDateTime.now();
//                Friendship newF = new Friendship(desiredUser, userToBeFriendedWith, date);
//                return repository.update(newF);
//
//            }
//        }
//        return Optional.empty();
//    }


}
