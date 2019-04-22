package br.com.cryptoong.robocompra.bean;

import java.math.BigDecimal;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class CompraBean {
    private Long id;
    private String paylaod;
    private BigDecimal preco;
    private BigDecimal amount;
    private BigDecimal valorInicial;
    private String clientId;
    private Long timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaylaod() {
        return paylaod;
    }

    public void setPaylaod(String paylaod) {
        this.paylaod = paylaod;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
