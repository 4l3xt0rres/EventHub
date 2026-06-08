import domain.model.Atividade;
import domain.model.Participante;
import domain.shared.CPF;
import domain.shared.Email;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AtividadeTest {

    @Test
    public void deveFalharAoExcederLimiteDeVagas() {
        Atividade palestra = new Atividade("Java Avançado", 1);
        Participante p1 = new Participante("João", new Email("joao@test.com"), new CPF("12345678901"));
        Participante p2 = new Participante("Maria", new Email("maria@test.com"), new CPF("98765432100"));

        palestra.inscrever(p1);
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            palestra.inscrever(p2);
        });

        assertTrue(exception.getMessage().contains("Limite de vagas atingido"));
    }

    @Test
    public void devePermitirInscricaoQuandoHouverVagas() {
        Atividade workshop = new Atividade("TDD na Prática", 10);
        Participante p1 = new Participante("João", new Email("joao@test.com"), new CPF("12345678901"));
        
        assertDoesNotThrow(() -> workshop.inscrever(p1));
        assertEquals(9, workshop.getVagasDisponiveis());
    }
}
