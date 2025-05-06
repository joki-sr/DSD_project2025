package com.example.factorial.src;

import java.util.ArrayList;
/*
* 权限的用户实体
* 组合了若干叶节点或用户节点
* 递归查询是否拥有对应权限
* */

public class PermissionNode extends PermissionBase{
    private final ArrayList<PermissionBase> pList= new ArrayList<PermissionBase>() ;
    @Override
    public void add(PermissionBase leaf) {
        pList.add(leaf);
    }

    @Override
    public boolean check(String per) {
        for (PermissionBase permissionBase : pList) {
            if (permissionBase.check(per)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void remove(PermissionBase leaf) {
        pList.remove(leaf);
    }
}
