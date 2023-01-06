package com.example.lab4.UserInterface;

import com.example.lab4.Domain.Friendship;
import com.example.lab4.Domain.User;
import com.example.lab4.Service.FriendshipService;
import com.example.lab4.Service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    UserService userService;
    FriendshipService friendService;

    public UserInterface(UserService userService, FriendshipService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    void mainMenu(){
        System.out.println("Choose your option:");
        System.out.println("1.User");
        System.out.println("2.Friend");
        System.out.println("3.Exit");
    }
    void userMenu(){
        System.out.println("Choose your option:");
        System.out.println("1.Add User.");
        System.out.println("2.Update User.");
        System.out.println("3.Delete User.");
        System.out.println("4.Show Users.");
    }

    void friendshipMenu(){
        System.out.println("Choose your option:");
        System.out.println("1.Add Friendship");
        System.out.println("2.Delete Friendship");
        System.out.println("3.Show Friendships");
        System.out.println("4.Back");
    }

    void addUser(){
        System.out.println("Enter last name:");
        Scanner input = new Scanner(System.in);
        String lastName = input.nextLine();
        System.out.println("Enter first name:");
        input = new Scanner(System.in);
        String firstName = input.nextLine();
        System.out.println("Enter username:");
        input = new Scanner(System.in);
        String userName = input.nextLine();
        System.out.println("Enter password:");
        input = new Scanner(System.in);
        String password = input.nextLine();
        User u = new User(lastName, firstName, userName, password);

        userService.addUser(u);
        System.out.println("User " + u.getUserName() + " was successfully added.");
    }

    void updateUser(){
        System.out.println("Enter username:");
        Scanner input = new Scanner(System.in);
        String userName = input.nextLine();
        System.out.println("Enter new last name:");
        input = new Scanner(System.in);
        String lastName = input.nextLine();
        System.out.println("Enter new first name:");
        input = new Scanner(System.in);
        String firstName = input.nextLine();
        System.out.println("Enter new password:");
        input = new Scanner(System.in);
        String password = input.nextLine();
        User u = new User(lastName, firstName, userName, password);
        userService.updateUser(userName,lastName, firstName, password);
        System.out.println("Success.");
    }

    void deleteUser(){
        System.out.println("Enter username:");
        Scanner input = new Scanner(System.in);
        String userName = input.nextLine();

        System.out.println("User " + userName + " was deleted successfully.");
        List<Friendship> allFriend = friendService.getAllFriendships();
        for(Friendship friend: allFriend){
            if(friend.getFirstUser().getUserName().equals(userName))
            {   User u = friend.getSecondUser();
                friendService.deleteFriendship(userName, u.getUserName());


            }
            if(friend.getSecondUser().getUserName().equals(userName))
            {
                User u = friend.getFirstUser();
                friendService.deleteFriendship(u.getUserName(), userName);
            }
        }
        List<User> allUsers = userService.getAllUsers();
        for(User u: allUsers)
        {
            Iterable<String> allUserFriends = u.getFriends();
            for(String s: allUserFriends)
            {
                if(s.equals(userName)){
                    userService.deleteFriend(u,userName);
                }

            }

        }
        userService.deleteUser(userName);


    }

    void showAllUsers(){
        System.out.println(userService.getAllUsers());
    }

    void showAllFriendships(){
        System.out.println(friendService.getAllFriendships());
    }

    void addFriendship(){
        System.out.println("Enter first username:");
        Scanner input = new Scanner(System.in);
        String userName1 = input.nextLine();
        User user1 = userService.findUser(userName1);

        System.out.println("Enter second username:");
        input = new Scanner(System.in);
        String userName2 = input.nextLine();

        User user2 = userService.findUser(userName2);
        LocalDateTime date = LocalDateTime.now();

        Friendship friendship = new Friendship(user1, user2, date);
        friendService.addFriendship(friendship);
        user1.addFriend(user2);
        user2.addFriend(user1);


    }

    void deleteFriendship(){
        System.out.println("Enter first username:");
        Scanner input = new Scanner(System.in);
        String userName1 = input.nextLine();

        System.out.println("Enter second username:");
        input = new Scanner(System.in);
        String userName2 = input.nextLine();

        friendService.deleteFriendship(userName1, userName2);
        User u1 = userService.findUser(userName1);
        User u2 = userService.findUser(userName2);
        userService.deleteFriend(u1, userName2);
        userService.deleteFriend(u2, userName1);

    }

    public void runMenu(){
        int option;
        boolean check = true;
        while(check){
            mainMenu();
            Scanner myObj = new Scanner(System.in);
            option = myObj.nextInt();
            switch (option) {
                case 1 -> runUserMenu();
                case 2 -> runFriendshipMenu();
                case 3 -> check = false;
            }

        }
    }

    void runUserMenu(){
        int option;
//        String userName;
        boolean check = true;
//        Scanner input = new Scanner(System.in);
//        userName = input.nextLine();
//        User u;
//        u = userService.findUser(userName);

        while(check){
            try{ userMenu();
                Scanner myObj = new Scanner(System.in);
                option = myObj.nextInt();
                switch (option) {
                    case 1 -> addUser();
                    case 2 -> updateUser();
                    case 3 -> deleteUser();
                    case 4 -> showAllUsers();
                    case 5 -> check = false;
                }}
            catch (Exception e){
                System.out.println(e);
            }

        }
    }

    void runFriendshipMenu(){
        try{int option;
            boolean check = true;

            while(check){
                friendshipMenu();
                Scanner myObj = new Scanner(System.in);
                option = myObj.nextInt();
                switch (option) {
                    case 1 -> addFriendship();
                    case 2 -> deleteFriendship();
                    case 3 -> showAllFriendships();
                    case 4 -> check = false;
                }}


        }catch(Exception e)
        {
            System.out.println(e);
        }
    }


}
