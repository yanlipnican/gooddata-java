/**
 * Copyright (C) 2004-2016, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.spring;

import com.gooddata.GoodData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

public class GoodDataServicesRegistrar implements BeanDefinitionRegistryPostProcessor {

    private final String goodDataBeanName;

    public GoodDataServicesRegistrar(String goodDataBeanName) {
        this.goodDataBeanName = goodDataBeanName;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            Arrays.stream(GoodData.class.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Bean.class))
                    .forEach(m -> {
                        final AbstractBeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(m.getReturnType()).getRawBeanDefinition();
                        definition.setFactoryBeanName(goodDataBeanName);
                        definition.setFactoryMethodName(m.getName());
                        final String beanId = Arrays.stream(m.getAnnotation(Bean.class).name()).findFirst().orElse(m.getReturnType().getSimpleName());
                        registry.registerBeanDefinition(goodDataBeanName + beanId, definition);
                    });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // don't wanna post process beans
    }
}
