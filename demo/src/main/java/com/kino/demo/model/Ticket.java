package com.kino.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    private long user_id;
    @Id
    private long screening_id;
    @NotNull
    private int count;

    public Ticket() {
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getScreening_id() {
        return this.screening_id;
    }

    public void setScreening_id(long screening_id) {
        this.screening_id = screening_id;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}