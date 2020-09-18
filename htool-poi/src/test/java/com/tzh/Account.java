package com.tzh;

import com.deepoove.poi.el.Name;
import lombok.Data;

/**
 * @Title: Account
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/7/8 16:39
 */
@Data
public class Account {

    @Name("name1")
    private String name;
    @Name("age1")
    private Integer age;
    @Name("t1")
    private String t1;
    @Name("t2")
    private String t2;

    private AccountVo accountVo;


}

@Data
class AccountVo {

    private String gender;
    private String phone;

}