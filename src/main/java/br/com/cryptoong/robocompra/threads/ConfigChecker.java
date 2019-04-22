package br.com.cryptoong.robocompra.threads;

import br.com.cryptoong.robocompra.dao.CompraDao;
import br.com.cryptoong.robocompra.singleton.Setup;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class ConfigChecker implements Runnable{
    private String numero;

    public ConfigChecker(String numero){
        this.numero = numero;
    }
    @Override
    public void run() {
        Setup setup = Setup.getInstance();
        while(true){
            synchronized (setup.getLock()){
                CompraDao dao = new CompraDao();
                setup.setBean(dao.getSetUp(numero));
                dao.close();
            }
            try {
                Thread.sleep(2 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
