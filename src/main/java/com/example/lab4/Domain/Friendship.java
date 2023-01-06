package com.example.lab4.Domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Long>{
    private static long count = 0;
    User firstUser;
    User secondUser;
    LocalDateTime date;

    public Friendship(User firstUser, User secondUser, LocalDateTime date) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.date = date;
        this.setId(count++);
    }

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Friendship friend = (Friendship) obj;
        String a = firstUser.getUserName();
        String b = friend.firstUser.getUserName();
        boolean check =  firstUser.getUserName().equals(friend.firstUser.getUserName()) && secondUser.getUserName().equals(friend.secondUser.getUserName()) ||
                secondUser.getUserName().equals(friend.firstUser.getUserName()) && firstUser.getUserName().equals(friend.secondUser.getUserName());
        return check;
    }

    @Override
    public String toString() {
        return super.getId() + ". " + "user: " + firstUser.getUserName() + ", " + secondUser.getUserName() + "\n";
    }
}
