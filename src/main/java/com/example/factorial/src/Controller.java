package com.example.factorial.src;

import com.example.factorial.src.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Controller {
    private User user;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
