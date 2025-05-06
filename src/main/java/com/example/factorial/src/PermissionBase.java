package com.example.factorial.src;

public abstract class PermissionBase {


    abstract void add(PermissionBase leaf);

    abstract boolean check(String per);

    abstract void remove(PermissionBase leaf);
}
