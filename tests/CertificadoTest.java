import application.service.CertificadoService;
import domain.model.Atividade;
import domain.model.Participante;
import domain.shared.CPF;
import domain.shared.Email;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CertificadoTest {

    @Test
    public void naoDeveEmitirCertificadoSemPresenca() {
        Atividade a = new Atividade("DDD", 10);
        Participante p = new Participante("Bob", new Email("bob@test.com"), new CPF("11122233344"));
        a.inscrever(p);

        CertificadoService service = new CertificadoService();
        
        assertThrows(IllegalStateException.class, () -> {
            service.emitirCertificado(p, a);
        });
    }

    @Test
    public void deveEmitirCertificadoComPresencaConfirmada() {
        Atividade a = new Atividade("DDD", 10);
        Participante p = new Participante("Bob", new Email("bob@test.com"), new CPF("11122233344"));
        a.inscrever(p);
        a.registrarPresenca(p);

        CertificadoService service = new CertificadoService();
        String certificado = service.emitirCertificado(p, a);

        assertNotNull(certificado);
        assertTrue(certificado.contains("Bob"));
        assertTrue(certificado.contains("DDD"));
    }
}
