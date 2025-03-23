package dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leilao", "root", "root");

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

            st = conn.prepareStatement("INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)");
            st.setString(1, produtos.getNome());
            st.setInt(2, produtos.getValor());
            st.setString(3, "A venda");
            
            status = st.executeUpdate();
            return status;
        } catch (SQLException ex) {
            mensagem = "Erro ao salvar: " + ex.getMessage();
            JOptionPane.showMessageDialog(null, mensagem);
            return -1;
        }
    }
    
    public List<ProdutosDTO> getProdutos(String nome) {
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";
        List<ProdutosDTO> listaProdutos = new ArrayList<>();

        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProdutosDTO produtos = new  ProdutosDTO();
                produtos.setId(rs.getInt("id"));
                produtos.setNome(rs.getString("nome"));
                produtos.setStatus(rs.getString("status"));
                produtos.setValor(rs.getInt("valor"));
                listaProdutos.add(produtos);
            }

            return listaProdutos;
        } catch (Exception e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }

        return listaProdutos;
    }
    
    public ProdutosDTO buscarPorId(int id) {
        ProdutosDTO produtos = null;
        
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produtos = new ProdutosDTO();
                produtos.setId(rs.getInt("id"));
                produtos.setNome(rs.getString("nome"));
                produtos.setStatus(rs.getString("status"));
                produtos.setValor(rs.getInt("valor"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar o produto: " + e.getMessage());
        }
        
        return produtos;
    }
    
    public int vender(int id) {
        String sql = "UPDATE produtos set status = 'Vendido' WHERE id = ? and status = 'A venda'";
        
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro ao vender o produto: " + e.getMessage());
            return 0;
        }
    }
  
    
}


