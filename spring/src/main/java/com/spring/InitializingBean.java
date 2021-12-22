package com.spring;

/**
 * @author VAIO-adam
 */
public interface InitializingBean {

    /**
     *  初始化bean
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;

}
