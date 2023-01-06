package com.example.lab4.Domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User extends Entity<Long>{
    private static long count = 0;
    String lastName;
    String firstName;
    String userName;
    String password;

    LocalDateTime date;

    Map<Long,String> friends;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User(String lastName, String firstName, String userName, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.password = password;
        this.friends = new HashMap<>();
        this.setId(count++);
    }

    public void addFriend(User user) {
        this.friends.put(user.id, user.userName);
    }

    public void deleteFriend(User user){
        this.friends.remove(user.id);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Iterable<String> getFriends() {
        return friends.values();
    }

    @Override
    public String toString() {
        return "User: - FirstName: " +
                firstName + ", " +
                "LastName: " + lastName + ", "
                + "Friends: " + friends + "\n" ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = new User(((User) o).getLastName(), ((User) o).getFirstName(), ((User) o).getUserName(), ((User) o).getPassword());
        user.setId(((User) o).getId());
        return lastName.equals(user.lastName) && firstName.equals(user.firstName) && userName.equals(user.userName) && password.equals(user.password) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLastName(), getFirstName(), getUserName(), getPassword(), getFriends());
    }
}
