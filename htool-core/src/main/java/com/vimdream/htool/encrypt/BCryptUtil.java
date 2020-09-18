package com.vimdream.htool.encrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Title: BCryptUtil
 * @Author vimdream
 * @ProjectName htool-core
 * @Date 2020/6/28 9:35
 */
public class BCryptUtil {

    public static String passwordBcryptEncode(String password){

        return new BCryptPasswordEncoder().encode(password);
    }

    public static Boolean passwordConfirm(String plaintextPassword,String encodePassword){
        return new BCryptPasswordEncoder().matches(plaintextPassword,encodePassword);
    }

}
