package domain.model;

import java.util.ArrayList;
import java.util.List;

public class Atividade {
    private final String titulo;
    private final int vagasTotais;
    private final List<Participante> inscritos;
    private final List<Participante> presentes;

    public Atividade(String titulo, int vagasTotais) {
        this.titulo = titulo;
        this.vagasTotais = vagasTotais;
        this.inscritos = new ArrayList<>();
        this.presentes = new ArrayList<>();
    }

    public void inscrever(Participante p) {
        if (inscritos.size() >= vagasTotais) {
            throw new IllegalStateException("Limite de vagas atingido para a atividade: " + titulo);
        }
        if (inscritos.contains(p)) {
            throw new IllegalStateException("Participante já inscrito nesta atividade.");
        }
        inscritos.add(p);
    }

    public void registrarPresenca(Participante p) {
        if (!inscritos.contains(p)) {
            throw new IllegalStateException("Participante não está inscrito nesta atividade.");
        }
        if (!presentes.contains(p)) {
            presentes.add(p);
        }
    }

    public boolean confirmouPresenca(Participante p) {
        return presentes.contains(p);
    }

    public boolean estaInscrito(Participante p) {
        return inscritos.contains(p);
    }

    public String getTitulo() { return titulo; }
    public int getVagasDisponiveis() { return vagasTotais - inscritos.size(); }

    public java.util.List<Participante> getInscritos() {
        return java.util.Collections.unmodifiableList(inscritos);
    }
}
