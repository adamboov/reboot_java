package com.adam;

import com.spring.anno.ComponentScan;

/**
 * @author VAIO-adam
 */
@ComponentScan("com.adam.service")
public class AppConfig {

    public AppConfig() {
        System.out.println("appConfig初始化了！");
    }

}
