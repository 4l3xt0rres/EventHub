package presentation;

import application.service.CertificadoService;
import domain.model.Atividade;
import domain.model.Participante;
import domain.shared.CPF;
import domain.shared.Email;
import infrastructure.repository.SQLiteRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventoApp extends JFrame {
    private SQLiteRepository repository = new SQLiteRepository();
    private CertificadoService certificadoService = new CertificadoService();
    private Map<String, Atividade> atividadesMap = new HashMap<>();

    
    private final Color DARK_BG = new Color(33, 37, 41);
    private final Color DARKER_BG = new Color(24, 26, 27);
    private final Color ACCENT_BLUE = new Color(13, 110, 253);
    private final Color ACCENT_GREEN = new Color(25, 135, 84);
    private final Color TEXT_COLOR = new Color(248, 249, 250);
    private final Color TEXT_MUTED = new Color(173, 181, 189);
    private final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 14);

    public EventoApp() {
        configurarDarkLookAndFeel();

        setTitle("EventHub");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(DARK_BG);
        setLayout(new BorderLayout());

        carregarDadosIniciais();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(BOLD_FONT);
        tabs.setOpaque(true);
        tabs.setBackground(DARKER_BG);
        tabs.setForeground(TEXT_COLOR);

        tabs.addTab("✨ Atividades", criarPainelAtividades());
        tabs.addTab("📝 Inscrições & Presença", criarPainelInscricoes());
        tabs.addTab("📊 Consulta de Vagas", criarPainelConsulta());
        tabs.addTab("🔍 Consultar Inscritos", criarPainelInscritosPorAtividade());

        add(tabs, BorderLayout.CENTER);
    }

    private void configurarDarkLookAndFeel() {
        try {
            UIManager.put("Panel.background", DARK_BG);
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Label.font", MAIN_FONT);
            UIManager.put("TextField.background", new Color(45, 49, 54));
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TextField.caretForeground", Color.WHITE);
            UIManager.put("Table.background", DARK_BG);
            UIManager.put("Table.foreground", TEXT_COLOR);
            UIManager.put("Table.gridColor", new Color(73, 80, 87));
            UIManager.put("TableHeader.background", DARKER_BG);
            UIManager.put("TableHeader.foreground", TEXT_COLOR);
            UIManager.put("ScrollPane.background", DARK_BG);
            UIManager.put("Viewport.background", DARK_BG);
            UIManager.put("TabbedPane.selected", ACCENT_BLUE);

            
            UIManager.put("OptionPane.background", DARK_BG);
            UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
            UIManager.put("Button.background", DARKER_BG);
            UIManager.put("Button.foreground", TEXT_COLOR);
        } catch (Exception e) {}
    }

    private JPanel criarPainelAtividades() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel lblHeader = new JLabel("EventHub", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblHeader.setForeground(ACCENT_BLUE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0); 
        panel.add(lblHeader, gbc);

        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField txtTitulo = new JTextField(20);
        JTextField txtVagas = new JTextField(20);
        JButton btnSalvar = criarBotaoModerno("CRIAR ATIVIDADE", ACCENT_BLUE);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Título da Atividade:"), gbc);
        gbc.gridx = 1; panel.add(txtTitulo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Vagas Totais:"), gbc);
        gbc.gridx = 1; panel.add(txtVagas, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText();
                int vagas = Integer.parseInt(txtVagas.getText());
                Atividade a = new Atividade(titulo, vagas);
                atividadesMap.put(titulo, a);
                repository.salvarAtividade(a, vagas);
                JOptionPane.showMessageDialog(this, "Atividade criada com sucesso!");
                txtTitulo.setText(""); txtVagas.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel criarPainelInscricoes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtAtividade = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtCPF = new JTextField();
        JTextField txtEmail = new JTextField();
        
        JButton btnInscrever = criarBotaoModerno("INSCREVER", ACCENT_BLUE);
        JButton btnPresenca = criarBotaoModerno("REGISTRAR PRESENÇA", ACCENT_GREEN);
        JButton btnCertificado = criarBotaoModerno("GERAR CERTIFICADO", new Color(111, 66, 193));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Título Atividade:"), gbc);
        gbc.gridx = 1; panel.add(txtAtividade, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nome Aluno:"), gbc);
        gbc.gridx = 1; panel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("CPF (11 dígitos):"), gbc);
        gbc.gridx = 1; panel.add(txtCPF, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(btnInscrever, gbc);
        gbc.gridx = 1; panel.add(btnPresenca, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; panel.add(btnCertificado, gbc);

        btnInscrever.addActionListener(e -> {
            try {
                Atividade a = atividadesMap.get(txtAtividade.getText());
                if (a == null) throw new Exception("Atividade não encontrada!");
                Participante p = new Participante(txtNome.getText(), new Email(txtEmail.getText()), new CPF(txtCPF.getText()));
                a.inscrever(p);
                repository.salvarParticipante(p);
                repository.registrarInscricao(a.getTitulo(), p.getCpf().getNumber());
                JOptionPane.showMessageDialog(this, "Inscrição realizada!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnPresenca.addActionListener(e -> {
            try {
                Atividade a = atividadesMap.get(txtAtividade.getText());
                Participante p = new Participante(txtNome.getText(), new Email(txtEmail.getText()), new CPF(txtCPF.getText()));
                a.registrarPresenca(p);
                repository.registrarPresenca(a.getTitulo(), p.getCpf().getNumber());
                JOptionPane.showMessageDialog(this, "Presença confirmada!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnCertificado.addActionListener(e -> {
            try {
                Atividade a = atividadesMap.get(txtAtividade.getText());
                Participante p = new Participante(txtNome.getText(), new Email(txtEmail.getText()), new CPF(txtCPF.getText()));
                String cert = certificadoService.emitirCertificado(p, a);
                JOptionPane.showMessageDialog(this, cert, "Diploma Acadêmico", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel criarPainelConsulta() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        String[] colunas = {"Atividade", "Vagas Disponíveis"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(MAIN_FONT);
        table.setSelectionBackground(ACCENT_BLUE);
        
        JButton btnAtualizar = criarBotaoModerno("ATUALIZAR LISTA", ACCENT_GREEN);
        btnAtualizar.addActionListener(e -> {
            model.setRowCount(0);
            for (Atividade a : atividadesMap.values()) {
                model.addRow(new Object[]{a.getTitulo(), a.getVagasDisponiveis()});
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnAtualizar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel criarPainelInscritosPorAtividade() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(DARK_BG);
        
        JTextField txtBuscaAtividade = new JTextField(25);
        JButton btnListar = criarBotaoModerno("BUSCAR", ACCENT_BLUE);
        
        JLabel labelBusca = new JLabel("Atividade:");
        labelBusca.setForeground(TEXT_MUTED);
        topPanel.add(labelBusca);
        topPanel.add(txtBuscaAtividade);
        topPanel.add(btnListar);
        
        String[] colunas = {"Nome", "CPF", "Email", "Presença"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        
        btnListar.addActionListener(e -> {
            model.setRowCount(0);
            Atividade a = atividadesMap.get(txtBuscaAtividade.getText());
            if (a != null) {
                for (Participante p : a.getInscritos()) {
                    model.addRow(new Object[]{
                        p.getNome(), p.getCpf().getNumber(), p.getEmail().getAddress(),
                        a.confirmouPresenca(p) ? "✅" : "❌"
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Atividade não encontrada!");
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JButton criarBotaoModerno(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(BOLD_FONT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.darker()),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        return btn;
    }

    private void carregarDadosIniciais() {
        for (Atividade a : repository.carregarTodasAtividades()) {
            atividadesMap.put(a.getTitulo(), a);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventoApp().setVisible(true));
    }
}
