package com.example.factorial.src;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeftSensor1 extends Observer{
    
    private ArrayList<Double> a;
    private HashMap<String, Double> map;
    
    @Autowired
    public LeftSensor1(SwitchState s){
        super(s);
        this.a = new ArrayList<>();
        this.map = new HashMap<>();
    }

    // 初始化方法
    public void init() {
        a.add(0.0);
        map.put("初始值", 0.0);
    }
    
    // 添加数据方法
    public void addData(Double value) {
        a.add(value);
    }
    
    // 获取数据
    public ArrayList<Double> getData() {
        return a;
    }
    
    // 获取映射
    public HashMap<String, Double> getMap() {
        return map;
    }

    /*public void fun_c(ArrayList<Float> data){
        a.add(fun_v(data));
    }
    public HashMap<String,Double> fun_s(){

        Double a_max = a.max();
        map.put("前屈",a_max);

        return map;
    }*/
}
