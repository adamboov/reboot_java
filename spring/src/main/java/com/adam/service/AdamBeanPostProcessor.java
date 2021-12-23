package com.adam.service;

import com.spring.BeanPostProcessor;
import com.spring.anno.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author VAIO-adam
 */
@Component("adamBeanPostProcessor")
public class AdamBeanPostProcessor implements BeanPostProcessor {

    /**
     * 初始化前
     *
     * @param beanName beanName
     * @param bean     bean
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        System.out.println("初始化前！！！" + "    beanName:"+ beanName + "    bean:" +bean);
        return bean;
    }

    /**
     * 初始化后
     *
     * @param beanName beanName
     * @param bean     bean
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println("初始化后！！！" + "    beanName:" + beanName + "    bean:" + bean);

        if ("userService".equals(beanName)) {
            System.out.println("准备走代理了：" + beanName);
            Object proxyInstance = Proxy.newProxyInstance(AdamBeanPostProcessor.class.getClassLoader(),
                    bean.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("执行代理对象了-------");
                            return method.invoke(bean, args);
                        }
                    });
            return proxyInstance;
        }

        return bean;
    }
}
