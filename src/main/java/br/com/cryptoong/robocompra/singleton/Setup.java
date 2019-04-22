package br.com.cryptoong.robocompra.singleton;

import br.com.cryptoong.robocompra.bean.SetupBean;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class Setup {
    private static Setup instance;
    private SetupBean bean;
    private Object lock;

    private Setup(){
        lock = new Object();
    }

    public static Setup getInstance(){
        if (instance == null){
            instance = new Setup();
        }
        return instance;
    }

    public SetupBean getBean() {
        return bean;
    }

    public void setBean(SetupBean bean) {
        this.bean = bean;
    }

    public Object getLock() {
        return lock;
    }
}
