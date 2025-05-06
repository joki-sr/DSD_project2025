package com.example.factorial.src;

import org.springframework.beans.factory.annotation.Autowired;

public class Observer {
    private SwitchState obj;
    private boolean State;
    
    protected Observer() {
        // 默认无参构造函数，用于Spring依赖注入
        this.State = false;
    }
    
    public Observer(SwitchState s){
        obj = s;
        State = obj.GetState();
        s.ObjAdd(this);
    }
    
    public void SetState(){
        State = obj.GetState();
    }
    
    public boolean GetState(){
        return State;
    }
    
    //算法组实际承担职能
    //需求沟通后，需要六个Observer类
    //fun()方法支持自命名重构
    public void fun() {
        while(State){
            System.out.println("1");
        }
    }
}
