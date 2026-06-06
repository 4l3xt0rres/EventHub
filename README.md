# Projeto PBL - Orientação a Objetos
## Tema 6: Gestão de Eventos Acadêmicos

### Estrutura do Projeto
- `src/domain`: Entidades (Evento, Inscrito, Atividade), Value Objects (CPF, Email, Periodo), Agregados.
- `src/application`: Casos de uso (RealizarInscricao, EmitirCertificado).
- `src/infrastructure`: Persistência (opcional bônus).
- `tests`: Testes unitários JUnit 5.

### Como rodar o Projeto
```bash
mvn compile
mvn exec:java -Dexec.mainClass="Main"
```
