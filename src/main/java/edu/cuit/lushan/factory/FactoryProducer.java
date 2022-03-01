package edu.cuit.lushan.factory;

public class FactoryProducer {
    public enum FactoryName{
        USER, DEVICE
    }
    public static AbstractFactory getFactory(FactoryName choice){
        switch (choice){
            case USER:
                return new UserVOFactory();
            case DEVICE:
                return new DeviceVOFactory();
        }
        return null;
    }
}
