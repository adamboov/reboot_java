package com.spring;

import com.spring.anno.Autowired;
import com.spring.anno.Component;
import com.spring.anno.ComponentScan;
import com.spring.anno.Scope;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author VAIO-adam
 */
public class AtomApplicationContext {

    public static final String SINGLETON = "singleton";

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public AtomApplicationContext(Class configClass) {

        this.configClass = configClass;

        //  解析配置类
        //  componentScan注解  扫描路径   扫描  beanDefinition  beanDefinitionMap
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {

            String beanName = entry.getKey();

            System.out.println("entry.getKey() 的beanName:" + beanName);

            BeanDefinition beanDefinition = entry.getValue();

            //  如果是单例bean
            if (SINGLETON.equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }

    }

    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            //  实例化对象
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //  依赖注入 对属性进行注入

            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(declaredField.getName());

                    declaredField.setAccessible(true);

                    declaredField.set(instance, bean);
                }
            }

            //  Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            //  初始化前  对bean进行加工
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {

                instance = beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }

            //  初始化
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //  初始化后  对bean进行加工
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {

                instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析扫描
     *
     * @param configClass 配置类
     */
    private void scan(Class configClass) {
        //  解析配置类
        //  注解扫描    扫描路径    扫描
        ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        //  扫描路径
        String path = componentScan.value();
        System.out.println("path--------> " + path);
        //  扫描
        //  bootstrap   jre/lib
        //  ext         jre/lib/ext
        //  app         classpath
        ClassLoader classLoader = AtomApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path.replace(".", "/"));

        File file = new File(resource.getFile());
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            for (File f : files) {

                String fileName = f.getAbsolutePath();
                System.out.println(fileName);

                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(
                            fileName.indexOf(path.replace(".", "\\")),
                            fileName.indexOf(".class")).replace("\\", ".");
                    System.out.println(className);

                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            //  表示当前类是个bean
                            //  解析当前bean是单例还是prototype的bean
                            //  beanDefinition

                            //  isAssignableFrom 和 instanceof的区别
                            //  https://www.cnblogs.com/wangkaihua/p/10339421.html

                            //  clazz 是 beanPostProcessor的子类
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }

                            Component componentAnnotation = clazz.getAnnotation(Component.class);

                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();

                            beanDefinition.setClazz(clazz);
                            if (clazz.isAnnotationPresent(Scope.class)) {

                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope(SINGLETON);
                            }

                            beanDefinitionMap.put(beanName, beanDefinition);

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Object getBean(String beanName) {

        //  包含key
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            if (SINGLETON.equals(beanDefinition.getScope())) {
                return singletonObjects.get(beanName);
            } else {
                //  创建一个bean 放去单例池
                return createBean(beanName, beanDefinition);
            }

        } else {
            //  不存在对应的bean
            throw new NullPointerException();
        }
    }
}
