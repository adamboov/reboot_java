package com.adam;

import com.adam.service.UserService;
import com.spring.AtomApplicationContext;

/**
 * @author VAIO-adam
 */
public class Test {

    public static void main(String[] args) {

        AtomApplicationContext atomApplicationContext = new AtomApplicationContext(AppConfig.class);

        UserService userService = (UserService) atomApplicationContext.getBean("userService");
        System.out.println(userService);
        userService.test();

    }
}
