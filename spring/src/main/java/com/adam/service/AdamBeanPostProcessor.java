package com.adam.service;

import com.spring.BeanPostProcessor;
import com.spring.anno.Component;

/**
 * @author VAIO-adam
 */
@Component
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
        System.out.println("初始化前！！！" + beanName + bean);
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
        System.out.println("初始化后！！！"+ beanName + bean);
        return bean;
    }
}
