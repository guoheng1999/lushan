package edu.cuit.lushan.factory;

public abstract class AbstractFactory<T> {
    abstract public T buildEntityByVO(T entity, Object vo);
    abstract public Object buildVOByEntity(T entity, String name);
}
