package models;

import java.util.concurrent.ThreadLocalRandom;

public class RoboInteligente extends Robo {
    private int ultimaDirecaoInvalida = 0;

    public RoboInteligente(Cor cor) { super(cor); }

    public void mover() {
        int direcao;
        do {
            direcao = ThreadLocalRandom.current().nextInt(1, 5);
        } while (direcao == ultimaDirecaoInvalida);

        try {
            super.mover(direcao);
            ultimaDirecaoInvalida = 0;
        } catch (MovimentoInvalidoException e) {
            ultimaDirecaoInvalida = direcao;
            throw e;
        }
    }
}
