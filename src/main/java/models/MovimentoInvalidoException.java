package models;

public class MovimentoInvalidoException extends RuntimeException {
    public MovimentoInvalidoException() {
        super("O movimento requisitado é inválido! O robô não pode sair do tabuleiro.");
    }

    public MovimentoInvalidoException(String movimento) {
        super("O movimento requisitado é inválido! O robô não pode sair do tabuleiro.");
    }
}
