package domain.shared;

public class CPF {
    private final String number;

    public CPF(String number) {
        if (number == null || !number.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos numéricos.");
        }
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return number.equals(cpf.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}
