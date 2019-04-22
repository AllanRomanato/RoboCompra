package br.com.cryptoong.robocompra.bean;

import java.math.BigDecimal;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class SetupBean {
    private BigDecimal precoInicial;
    private BigDecimal amountInicial;
    private BigDecimal incremento;
    private BigDecimal totalUsdt;

    public BigDecimal getPrecoInicial() {
        return precoInicial;
    }

    public void setPrecoInicial(BigDecimal precoInicial) {
        this.precoInicial = precoInicial;
    }

    public BigDecimal getAmountInicial() {
        return amountInicial;
    }

    public void setAmountInicial(BigDecimal amountInicial) {
        this.amountInicial = amountInicial;
    }

    public BigDecimal getIncremento() {
        return incremento;
    }

    public void setIncremento(BigDecimal incremento) {
        this.incremento = incremento;
    }

    public BigDecimal getTotalUsdt() {
        return totalUsdt;
    }

    public void setTotalUsdt(BigDecimal totalUsdt) {
        this.totalUsdt = totalUsdt;
    }
}

