package com.example.lab4.Service;

import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Domain.User;
import com.example.lab4.Domain.Validators.ValidationException;
import com.example.lab4.Repository.Repository;
import com.example.lab4.utils.Events.ChangeEvent;
import com.example.lab4.utils.Events.ChangeEventType;
import com.example.lab4.utils.Observer.Observable;
import com.example.lab4.utils.Observer.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Service implements Observable<ChangeEvent>{
    private Repository<Long, User> userRepo;

    private Repository<Long, FriendshipForDB> friendshipRepo;

    private List<Observer<ChangeEvent>> obs = new ArrayList<>();

    public Service(Repository<Long, User> userRepo, Repository<Long, FriendshipForDB> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    public Optional<User> addUser(User user){
        Long ID = getMaxID() + 1;
        user.setId(ID);
        try{
            return userRepo.save(user);
        }
        catch (ValidationException e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }


    }

    public Long getMaxID(){
        List<User> all = getUsersAsList();
        Long maxx= 0L;
        for(User u : all)
            if(u.getId()>maxx)
                maxx=u.getId();

        return maxx;
    }

    public FriendshipForDB findFriendshipByUsers(Long ID1, Long ID2){
        return friendshipRepo.findByUsers(ID1, ID2);
    }

    public User findUserByUserName(String userName, String password){
        return userRepo.findByUsernameAndPassword(userName, password);
    }

    public Optional<User> updateUser(User user){
        try{
            return userRepo.update(user);
        }catch (ValidationException e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }

    }
    public User findUser(Long ID){
        if(userRepo.findOne(ID).isPresent())
            return userRepo.findOne(ID).get();
        return null;
    }
    public Optional<User> deleteUser(Long ID){
        return userRepo.delete(ID);
    }

    public Iterable<User> getAll(){return userRepo.findAll();}
    public LocalDateTime generateFriendsFrom(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.format(formatter);

        return LocalDateTime.parse(dateTimeString, formatter);
    }
    public Optional<FriendshipForDB> addFriendship(Long fID, Long sID){
        LocalDateTime requestFrom = generateFriendsFrom();
        Optional<User> u1 = userRepo.findOne(fID);
        Optional<User> u2 = userRepo.findOne(sID);

        if(u1.isEmpty() || u2.isEmpty()){
            throw new Error("One of the users is not valid!");
        }
        FriendshipForDB friendship = new FriendshipForDB(fID, sID, requestFrom);
        try {

            notifyObservers(new ChangeEvent(ChangeEventType.ADD, friendship));
            return friendshipRepo.save(friendship);
        }   catch (ValidationException e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<FriendshipForDB> deleteFriendship(Long aLong){
        Optional<FriendshipForDB> friendship = friendshipRepo.delete(aLong);
        FriendshipForDB friend = null;
        if(friendship.isPresent())
            friend = friendship.get();
        notifyObservers(new ChangeEvent(ChangeEventType.DELETE, friend));
        return friendshipRepo.delete(aLong);
    }

    public Iterable<FriendshipForDB> getAllFriendships(){return friendshipRepo.findAll();}


    public List<FriendshipForDB> getFriendsAsList(){
        Iterable<FriendshipForDB> allF = friendshipRepo.findAll();
        List<FriendshipForDB> list = new ArrayList<>();
        allF.forEach(list::add);
        return list;
    }

    public List<User> getUsersAsList(){
        Iterable<User> allU = userRepo.findAll();
        List<User> list = new ArrayList<>();
        allU.forEach(list::add);
        return list;
    }

    public boolean sendFriendRequest(Long ID1, Long ID2){
        LocalDateTime requestFrom = generateFriendsFrom();
        boolean existsAlready = false;
//        User u1 = userRepo.findOne(ID1).get();
//        User u2 = userRepo.findOne(ID2).get();
        List<FriendshipForDB> listFriends = friendshipRepo.getAllAsList();
        for(FriendshipForDB f:listFriends)
            if (Objects.equals(f.getID1(), ID1) && Objects.equals(f.getID2(), ID2) || Objects.equals(f.getID1(), ID2) && Objects.equals(f.getID2(), ID1)) {
                existsAlready = true;
                break;
            }
        if(!existsAlready){
            FriendshipForDB friendship = new FriendshipForDB(ID1, ID2, requestFrom);
            friendship.setAccepted(false);

            try {
                friendshipRepo.save(friendship);
                notifyObservers(new ChangeEvent(ChangeEventType.ADD, friendship));
                return true;
            }
            catch (ValidationException e) {
                System.out.println(e.getMessage());

                if(Objects.equals(e.getMessage(), "ID duplicat!!!")) {
                    FriendshipForDB p_aux = new FriendshipForDB(ID1, ID2, null);
                    FriendshipForDB p_dejaExistent = findFriendship(p_aux.getId());
                    if(Objects.equals(p_dejaExistent.getID2(), ID1))
                        updateFriendship(ID1, ID2, true);
                }
        }



        }
        return false;
    }

    public boolean updateFriendship(Long ID1, Long ID2, boolean acceptat){
        FriendshipForDB prietenie =  new FriendshipForDB(ID1, ID2, generateFriendsFrom());
        prietenie.setAccepted(acceptat);
        try {
            friendshipRepo.update(prietenie);
            notifyObservers(new ChangeEvent(ChangeEventType.UPDATE, prietenie));
            return true;
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public FriendshipForDB findFriendship(Long ID){
        return friendshipRepo.findOne(ID).get();
    }


    public List<User> getFriendsOfUserList(User user){
        List<FriendshipForDB> allF = getFriendsAsList();
        List<User> result = new ArrayList<>();

        for(FriendshipForDB f: allF){
            User first = findUser(f.getID1());
            User second = findUser(f.getID2());

            if(Objects.equals(f.getID1(), user.getId()))
                result.add(second);
            else if(Objects.equals(f.getID2(), user.getId()))
                result.add(first);
        }
        return result;
    }

    public List<User> getRequestsOfUserList(User currentUser){
        List<FriendshipForDB> allFriends = getFriendsAsList();
        List<User> rezultat = new ArrayList<>();

        for(FriendshipForDB p : allFriends.stream().filter(x->!(x.isAccepted())).toList())
        {
            User u1 = findUser(p.getID1());
            if(Objects.equals(p.getID2(), currentUser.getId()))
                rezultat.add(u1);
        }
        return rezultat;
    }

    public List<User> getUnacceptedRequestsOfUser(User currentUser){
        List<User> allUsers = getUsersAsList();
        List<User> friendsOfUser = getFriendsOfUserList(currentUser);
        List<User> result = new ArrayList<>();

        for(User u: allUsers){
            if(!friendsOfUser.contains(u) && !result.contains(u) && !u.equals(currentUser))
                result.add(u);
        }
        return result;
    }

    @Override
    public void addObserver(Observer<ChangeEvent> e){
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        obs.remove(e);
    }

    @Override
    public void notifyObservers(ChangeEvent t) {
            obs.forEach(x->x.update(t));
        }
}
