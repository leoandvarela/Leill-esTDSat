package dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {    
    Connection conn;
    PreparedStatement st;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    private String mensagem;
    
    public boolean conectar() {
        return this.conectar(false);
    }
     
    public boolean conectar(boolean mensagemConectadoExibida) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leilao", "root", "senharoot");

            if (mensagemConectadoExibida) {
                mensagem = "Conectado com sucesso";
                JOptionPane.showMessageDialog(null, mensagem);
            }

            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            mensagem = "Erro ao conectar: " + ex.getMessage();
            JOptionPane.showMessageDialog(null, mensagem);
            return false;
        }
    }
    public void desconectar() {
        if (conn != null) {
            try {
                conn.close();
                mensagem = "Conexão encerrada com sucesso.";
                JOptionPane.showMessageDialog(null, mensagem);
            } catch (SQLException ex) {
                mensagem = "Erro ao encerrar a conexão: " + ex.getMessage();
                JOptionPane.showMessageDialog(null, mensagem);
            }
        }
    }
    
    public int salvar(ProdutosDTO produtos) {
        int status;
        try {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Conexão não estabelecida.");
            }

            st = conn.prepareStatement("INSERT INTO produtos (nome, valor) VALUES (?, ?)");
            st.setString(1, produtos.getNome());
            st.setInt(2, produtos.getValor());

            status = st.executeUpdate();
            return status;
        } catch (SQLException ex) {
            mensagem = "Erro ao salvar: " + ex.getMessage();
            JOptionPane.showMessageDialog(null, mensagem);
            return -1;
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        
        return listagem;
    }
        
}

