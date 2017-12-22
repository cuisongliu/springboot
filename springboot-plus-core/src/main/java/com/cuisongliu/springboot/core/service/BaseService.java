/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 cuisongliu@qq.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cuisongliu.springboot.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.MyMapper;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Service基类，所有Service均继承该类
 */
public abstract class BaseService<T> {

    protected Class<T> clazz;
    protected String DOMAIN_NAME;
    @Autowired
    protected MyMapper<T> myMapper;
    protected Logger logger = null;

    /**
     * 构造方法之前调用
     */
    @PostConstruct
    @SuppressWarnings("unchecked")
    public void postConstruct() {
        try {
            Type type = this.getClass().getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
            DOMAIN_NAME = clazz.getName();
        } catch (Exception e) {
            e.printStackTrace();
            clazz = null;
        }
        logger = LoggerFactory.getLogger(this.getClass());
    }


    /**
     * @param id 主键ID
     * @return
     */
    @Transactional(readOnly = true)
    public T getByID(Serializable id) {
        try {
            return myMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("根据{" + id + "}获取{" + DOMAIN_NAME + "}发生错误：{}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public T selectByID(Serializable id) {
        return getByID(id);
    }

    @Transactional(readOnly = true)
    public T getByEntity(T entity) {
        return myMapper.selectOne(entity);
    }

    @Transactional(readOnly = true)
    public T selectByEntity(T entity) {
        return getByEntity(entity);
    }

    @Transactional(readOnly = true)
    public List<T> list(T entity) {
        return myMapper.select(entity);
    }

    @Transactional(readOnly = true)
    public List<T> select(T entity) {
        return list(entity);
    }

    @Transactional(readOnly = true)
    public List<T> listAll() {
        return myMapper.selectAll();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void save(T entity) {
        try {
            myMapper.insertSelective(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void insert(T entity) {
        save(entity);
    }

    @Transactional(rollbackFor = {Exception.class})
    public int update(T entity) {
        try {
            return myMapper.updateByPrimaryKeySelective(entity);
        } catch (Exception e) {
            logger.error(DOMAIN_NAME + "修改时发生错误：{}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public int modify(T entity) {
        return update(entity);
    }

    @Transactional(rollbackFor = {Exception.class})
    public int updateEntity(T entity) {
        try {
            return myMapper.updateByPrimaryKey(entity);
        } catch (Exception e) {
            logger.error(DOMAIN_NAME + "修改时发生错误：{}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public int modifyEntity(T entity) {
        return updateEntity(entity);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void delete(Serializable id) {
        try {
            myMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error(DOMAIN_NAME + "删除时发生错误：{}", e.getMessage(), e);

            throw e;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteIds(String ids) {
        try {
            myMapper.deleteByIds(ids);
        } catch (Exception e) {
            logger.error(DOMAIN_NAME + "删除时发生错误：{}", e.getMessage(), e);

            throw e;
        }
    }

}
