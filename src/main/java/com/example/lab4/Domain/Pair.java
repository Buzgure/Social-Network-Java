package com.example.lab4.Domain;

import java.util.Objects;

public class Pair <FE, SE>{
    private FE first;
    private SE second;

    public Pair(FE fE, SE sE) {
        this.first = fE;
        this.second = sE;
    }

    public FE getFirst() {
        return first;
    }

    public void setFirst(FE first) {
        this.first = first;
    }

    public SE getSecond() {
        return second;
    }

    public void setSecond(SE second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        return this.first.equals(((Pair) obj).first) && this.second.equals(((Pair) obj).second);
    }

    @Override
    public String toString() {
        return first + ", " + second + " ";
    }
}
