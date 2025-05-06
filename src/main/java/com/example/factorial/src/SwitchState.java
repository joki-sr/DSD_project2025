package com.example.factorial.src;

import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class SwitchState {
    private boolean state=false;
    private ArrayList<Observer> List = new ArrayList<Observer>();

    public void ObjAdd(Observer o){
        List.add(o);
    }
    public void remove(Observer o){
        List.remove(o);
    }
    public boolean GetState(){
        return state;
    }
    public void Switch(){
        this.state= !this.state;
        for(Observer o : List){
            Notify(o);
        }
    }
    public void Notify(Observer b){
        b.SetState();
    }
}
