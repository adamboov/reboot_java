package com.spring;

/**
 * @author VAIO-adam
 */
public interface BeanPostProcessor {

    /**
     * 初始化前
     * @param beanName  beanName
     * @param bean  bean
     * @return
     */
    Object postProcessBeforeInitialization(String beanName, Object bean);

    /**
     * 初始化后
     * @param beanName  beanName
     * @param bean  bean
     * @return
     */
    Object postProcessAfterInitialization(String beanName, Object bean);

}
