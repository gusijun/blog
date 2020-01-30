package com.lrm.service;

import com.lrm.po.User;

public interface UserService {

    User checkUser(String name,String password);
}
