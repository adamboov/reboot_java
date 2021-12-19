package com.adam;

import com.spring.AtomApplicationContext;

/**
 * @author VAIO-adam
 */
public class Test {

    public static void main(String[] args) {

        AtomApplicationContext atomApplicationContext = new AtomApplicationContext(AppConfig.class);

        Object userService = atomApplicationContext.getBean("userService");
        System.out.println(userService);

    }
}
