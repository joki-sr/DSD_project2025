package com.example.factorial.src;

public class PermissionEntityManagement {
    private PermissionEntityManagement(){

    }
    static PermissionEntityManagement Instance;
    static public PermissionEntityManagement GetSingleton(){
        if(Instance==null){
            Instance = new PermissionEntityManagement();
        }
        return Instance;
    }
    public PermissionNode GetDoc(){
        return Doctor;
    }
    public PermissionNode GetPat(){
        return patinent;
    }
    public PermissionNode GetAdm(){
        return Admin;
    }
    public PermissionNode GetAll(){
        return all;
    }


    PermissionNode Doctor = new PermissionNode();
    PermissionNode patinent = new PermissionNode();
    PermissionNode Admin = new PermissionNode();
    PermissionNode all = new PermissionNode();
    //通过如下写法完成初始化
    //手动设置权限
    //添加权限树的儿子节点
    PermissionBase p1 = new PermissionLeaf("数据管理");
    PermissionBase p2 = new PermissionLeaf("显示历史数据");
    PermissionBase p3 = new PermissionLeaf("对比");
    PermissionBase p4 = new PermissionLeaf("患者管理");
    PermissionBase p5 = new PermissionLeaf("选择患者");
    PermissionBase p6 = new PermissionLeaf("数据采集");
    public void INTI(){
        patinent.add(p2);
        Admin.add(p1);
        Doctor.add(p3);
        Doctor.add(p4);
        Doctor.add(p5);
        Doctor.add(p6);

        Doctor.add(patinent);
        all.add(Doctor);
        all.add(Admin);
    }

}
