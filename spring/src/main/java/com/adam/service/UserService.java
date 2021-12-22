package com.adam.service;

import com.spring.BeanNameAware;
import com.spring.InitializingBean;
import com.spring.anno.Autowired;
import com.spring.anno.Component;
import com.spring.anno.Scope;

/**
 * @author VAIO-adam
 */
@Component("userService")
@Scope("singleton")
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    private String beanName;

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化！！！");
    }

    public void test() {
        System.out.println(orderService);
        System.out.println(beanName);
    }
}
