package infrastructure.repository;

import domain.model.Atividade;
import domain.model.Participante;
import domain.shared.CPF;
import domain.shared.Email;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteRepository {
    private Connection connection;

    public SQLiteRepository() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:eventos.db");
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS atividades (titulo TEXT PRIMARY KEY, vagas_totais INTEGER)");
        stmt.execute("CREATE TABLE IF NOT EXISTS participantes (cpf TEXT PRIMARY KEY, nome TEXT, email TEXT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS inscricoes (atividade_titulo TEXT, participante_cpf TEXT, presente INTEGER)");
    }

    public void salvarAtividade(Atividade a, int vagas) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT OR REPLACE INTO atividades VALUES (?, ?)");
            pstmt.setString(1, a.getTitulo());
            pstmt.setInt(2, vagas);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvarParticipante(Participante p) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT OR REPLACE INTO participantes VALUES (?, ?, ?)");
            pstmt.setString(1, p.getCpf().getNumber());
            pstmt.setString(2, p.getNome());
            pstmt.setString(3, p.getEmail().getAddress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarInscricao(String atividadeTitulo, String cpf) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO inscricoes VALUES (?, ?, 0)");
            pstmt.setString(1, atividadeTitulo);
            pstmt.setString(2, cpf);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarPresenca(String atividadeTitulo, String cpf) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE inscricoes SET presente = 1 WHERE atividade_titulo = ? AND participante_cpf = ?");
            pstmt.setString(1, atividadeTitulo);
            pstmt.setString(2, cpf);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Atividade> carregarTodasAtividades() {
        List<Atividade> lista = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM atividades");
            while (rs.next()) {
                Atividade a = new Atividade(rs.getString("titulo"), rs.getInt("vagas_totais"));
                carregarInscritos(a);
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void carregarInscritos(Atividade a) throws SQLException {
        String sql = "SELECT DISTINCT p.*, i.presente FROM participantes p " +
                     "JOIN inscricoes i ON p.cpf = i.participante_cpf " +
                     "WHERE i.atividade_titulo = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, a.getTitulo());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Participante p = new Participante(
                rs.getString("nome"),
                new Email(rs.getString("email")),
                new CPF(rs.getString("cpf"))
            );
            if (!a.estaInscrito(p)) {
                a.inscrever(p);
            }
            if (rs.getInt("presente") == 1) {
                a.registrarPresenca(p);
            }
        }
    }
}
