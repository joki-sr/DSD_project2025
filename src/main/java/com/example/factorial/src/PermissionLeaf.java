package com.example.factorial.src;

/*
* 权限树的权限实体节点
* 用String描述权限
* Check是否拥有权限
* */
public class PermissionLeaf extends PermissionBase {
    private String pCode;
    public PermissionLeaf(String p){
        pCode = p;
    }

    public String getpCode() {
        return pCode;
    }


    @Override
    void add(PermissionBase leaf) {

    }

    @Override
    public boolean check(String per) {
        return pCode == per;
    }

    @Override
    void remove(PermissionBase leaf) {

    }
}
