package br.com.cryptoong.robocompra.dao;

import br.com.cryptoong.biblioteca.bean.ClienteBean;
import br.com.cryptoong.robocompra.bean.CompraBean;
import br.com.cryptoong.robocompra.bean.SetupBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Project Workspace
 * Created by Allan Romanato
 */
public class CompraDao {
    private Connection conn;
    private final String schema = "crypto";
    private final String tabelaSetUp = "robo_compra_conf";
    private final String tabelaCompra = "robo_compra";
    private final String tabelaCliente = "clientes";
    public  CompraDao(){
        conn = new ConnectionFactory().getConnection();
    }

    public SetupBean getSetUp(String numero){
        try{
            String sql = "SELECT * FROM " + schema + "." + tabelaSetUp+" WHERE numero_robo=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, numero);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                SetupBean bean = new SetupBean();
                bean.setAmountInicial(rs.getBigDecimal("amount_inicial"));
                bean.setPrecoInicial(rs.getBigDecimal("preco_inicial"));
                bean.setIncremento(rs.getBigDecimal("incremento"));
                bean.setTotalUsdt(rs.getBigDecimal("max_usdt"));
                return bean;
            }
            
            throw new RuntimeException("Nenhum registro encontrado");
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }

    public void insert(CompraBean bean){
        String sql = "INSERT INTO "+schema+"."+tabelaCompra+
                " (payload_compra, amount_compra, preco_compra, valor_inicial, cliente_id, `timestamp`)" +
                " VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bean.getPaylaod());
            stmt.setBigDecimal(2, bean.getAmount());
            stmt.setBigDecimal(3,bean.getPreco());
            stmt.setBigDecimal(4, bean.getValorInicial());
            stmt.setString(5, bean.getClientId());
            stmt.setLong(6, bean.getTimestamp());

            stmt.execute();

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getUsdtComprado(String cliente){
        String sql = "SELECT * FROM crypto."+tabelaCompra+" WHERE cliente_id=?";
        BigDecimal sum = new BigDecimal("0");
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                sum = sum.add(rs.getBigDecimal("amount_compra").multiply(rs.getBigDecimal("preco_compra"))).setScale(5,RoundingMode.HALF_EVEN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ClienteBean getClient(String codigo) {
        String sql = "SELECT * FROM crypto."+tabelaCliente+" WHERE codigo_cliente=?";
        ClienteBean bean = new ClienteBean();
        try {

            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                bean.setCodigoCliente(rs.getString("codigo_cliente"));
                bean.setNomeCliente(rs.getString("nome_cliente"));
                bean.setApiKey(rs.getString("api_key"));
                bean.setApiSecret(rs.getString("api_secret"));
            }
            rs.close();
            stmt.close();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bean;
    }
}
