package domain.model;

import domain.shared.CPF;
import domain.shared.Email;

public class Participante {
    private final String nome;
    private final Email email;
    private final CPF cpf;

    public Participante(String nome, Email email, CPF cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    public String getNome() { return nome; }
    public Email getEmail() { return email; }
    public CPF getCpf() { return cpf; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participante that = (Participante) o;
        return cpf.equals(that.cpf);
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }
}
