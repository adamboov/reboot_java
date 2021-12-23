package com.adam;

import com.adam.service.IUserService;
import com.spring.AtomApplicationContext;

/**
 * @author VAIO-adam
 */
public class Test {

    public static void main(String[] args) {

        AtomApplicationContext atomApplicationContext = new AtomApplicationContext(AppConfig.class);

        IUserService userService = (IUserService) atomApplicationContext.getBean("userService");
        userService.test();

    }
}
