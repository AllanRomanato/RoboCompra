package br.com.cryptoong.robocompra.transaction;

import br.com.cryptoong.biblioteca.bean.ClienteBean;
import br.com.cryptoong.biblioteca.bean.CotationBean;
import br.com.cryptoong.binance.BinanceBean;
import br.com.cryptoong.binance.BinanceTransactions;
import br.com.cryptoong.robocompra.bean.CompraBean;
import br.com.cryptoong.robocompra.dao.CompraDao;
import br.com.cryptoong.biblioteca.exceptions.HttpGetInformationException;
import br.com.cryptoong.biblioteca.generic.Informations;
import br.com.cryptoong.robocompra.singleton.Setup;
import br.com.cryptoong.robocompra.threads.ConfigChecker;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static br.com.cryptoong.geral.Constantes.NEWORDERPATHBINANCE;
import static br.com.cryptoong.geral.Constantes.POSTMETHOD;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class Compra {
    private static final Logger logger = Logger.getLogger(Compra.class);

    public void executa(String codigoCliente) throws InterruptedException {


        ClienteBean cliente;
        logger.info("Iniciando Robo de Compra.");
        Thread.sleep(1000);
        Setup setup = Setup.getInstance();
        BigDecimal minValue = setup.getBean().getPrecoInicial();
        BigDecimal amount = setup.getBean().getAmountInicial();
        BigDecimal incremento = setup.getBean().getIncremento();
        BigDecimal maxUsdt = setup.getBean().getTotalUsdt();
        BigDecimal sumUsdt;

        CompraDao dao = new CompraDao();
        cliente = dao.getClient(codigoCliente);
        sumUsdt = dao.getUsdtComprado(codigoCliente);
        dao.close();


        BinanceTransactions bt = new BinanceTransactions();

        while(true){
            try {
                CotationBean cotationBean = Informations.invokeCotation("binUsdtBtc");
                if (sumUsdt.compareTo(maxUsdt)==-1) {
                    if (cotationBean.getPrice().compareTo(setup.getBean().getPrecoInicial()) == -1) {
                        if (cotationBean.getPrice().compareTo(minValue) == -1) {
                            JSONObject json;
                            CompraBean comprBean = new CompraBean();
                            BinanceBean binanceBean = new BinanceBean();
                            logger.info("########################################################################");
                            logger.info("Executando compra");
                            binanceBean.setApiKey(cliente.getApiKey());
                            binanceBean.setApiSecret(cliente.getApiSecret());
                            binanceBean.setType("MARKET");
                            binanceBean.setPath(NEWORDERPATHBINANCE);
                            binanceBean.setRequestMethod(POSTMETHOD);
                            binanceBean.setSymbol(cotationBean.getSymbol());
                            binanceBean.setSide(cotationBean.getSide());
                            binanceBean.setQuantity(amount.toPlainString());

                            binanceBean.setTimestamp(String.valueOf(System.currentTimeMillis()));
                            String newOrder = bt.executeTransaction(binanceBean, false);
                            logger.info(newOrder);
                            json = new JSONObject(newOrder);
                            sumUsdt = sumUsdt.add(new BigDecimal(json.getString("cummulativeQuoteQty")));
                            comprBean.setPaylaod(newOrder);
                            comprBean.setValorInicial(setup.getBean().getPrecoInicial());
                            comprBean.setTimestamp(json.getLong("transactTime"));
                            comprBean.setAmount(amount);
                            comprBean.setPreco(new BigDecimal(json.getString("cummulativeQuoteQty")).divide(new BigDecimal(json.getString("executedQty")),5, RoundingMode.FLOOR));
                            comprBean.setClientId(codigoCliente);
                            amount = amount.add(incremento);
                            minValue = new BigDecimal(json.getString("cummulativeQuoteQty")).divide(new BigDecimal(json.getString("executedQty")),5, RoundingMode.FLOOR);
                            dao = new CompraDao();
                            dao.insert(comprBean);
                            dao.close();

                            logger.info("########################################################################\n");
                        } else {
                            logger.info("########################################################################");
                            logger.info("Valor minimo para compra não atingido");
                            logger.info("Preco em inicial Banco: " + setup.getBean().getPrecoInicial());
                            logger.info("Preco Minimo para Compra (menor que): " + minValue);
                            logger.info("Preco atual: " + cotationBean.getPrice());
                            logger.info("########################################################################\n");
                        }
                    } else {
                        logger.info("########################################################################");
                        logger.info("Mercado acima do valor inicial.");
                        logger.info("########################################################################\n");
                        minValue = setup.getBean().getPrecoInicial();
                        amount = setup.getBean().getAmountInicial();

                    }
                }else{
                    logger.info("########################################################################");
                    logger.info("Valor maximo de USDT utilizado");
                    logger.info("########################################################################\n");
                }
            } catch (HttpGetInformationException e) {
                e.printStackTrace();
            }
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ConfigChecker("A"));
        t1.start();
        new Compra().executa("0002");
    }
}
