package application.service;

import domain.model.Atividade;
import domain.model.Participante;

public class CertificadoService {
    public String emitirCertificado(Participante p, Atividade a) {
        if (!a.confirmouPresenca(p)) {
            throw new IllegalStateException("Certificado não pode ser emitido: presença não confirmada.");
        }
        return "CERTIFICADO DE PARTICIPAÇÃO\n" +
               "Certificamos que " + p.getNome() + " participou da atividade " + a.getTitulo() + ".";
    }
}
