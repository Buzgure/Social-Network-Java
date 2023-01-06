package com.example.lab4.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendshipForDB extends Entity<Long>{
    private static long count = 0;

    private final Long ID1, ID2;

    private LocalDateTime date;

    private boolean accepted;


    public FriendshipForDB(Long ID1, Long ID2, LocalDateTime date) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.date = date;
        this.accepted = false;
        setId(count++);
    }

    public Long getID1() {
        return ID1;
    }

    public Long getID2() {
        return ID2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipForDB that = (FriendshipForDB) o;
        return Objects.equals(ID1, that.ID1) && Objects.equals(ID2, that.ID2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ID1, ID2);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "ID1=" + ID1 +
                ", ID2=" + ID2 +
                ", date=" + date +
                '}';
    }

    public boolean isAccepted(){return accepted;}

    public void setAccepted(boolean accepted){ this.accepted = accepted;}
}
