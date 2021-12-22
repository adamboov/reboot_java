package com.spring;

/**
 * @author VAIO-adam
 */
public interface InitializingBean {

    /**
     *
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;

}
