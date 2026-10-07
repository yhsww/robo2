package models;

import java.util.concurrent.ThreadLocalRandom;

public class Robo {
    protected int posX;
    protected int posY;
    protected int posXAnterior;
    protected int posYAnterior;
    protected Movimento movimento;
    protected boolean explodiu;
    protected int qtdMovimentosValidos;
    protected int qtdMovimentosInvalidos;
    protected Cor cor;

    public Robo(Cor cor) {
        this.cor = cor;
        this.posX = 0;
        this.posY = 0;
        this.posXAnterior = 0;
        this.posYAnterior = 0;
        this.qtdMovimentosValidos = 0;
        this.qtdMovimentosInvalidos = 0;
        this.explodiu = false;
    }

    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public int getQtdMovimentosValidos() { return qtdMovimentosValidos; }
    public int getQtdMovimentosInvalidos() { return qtdMovimentosInvalidos; }
    public boolean getExplodiu() { return explodiu; }
    public Cor getCor() { return cor; }
    public Movimento getMovimento() { return movimento; }

    public void setPosX(int posX) { this.posX = posX; }
    public void setPosY(int posY) { this.posY = posY; }
    public void setPosXAnterior(int posXAnterior) { this.posXAnterior = posXAnterior; }
    public void setPosYAnterior(int posYAnterior) { this.posYAnterior = posYAnterior; }
    public void setQtdMovimentosValidos(int valor) { this.qtdMovimentosValidos = valor; }
    public void setQtdMovimentosInvalidos(int valor) { this.qtdMovimentosInvalidos = valor; }
    public void setExplodiu(boolean explodiu) { this.explodiu = explodiu; }
    public void setCor(Cor cor) { this.cor = cor; }
    public void setMovimento(Movimento movimento) { this.movimento = movimento; }

    public void moverRandomico() {
        mover(ThreadLocalRandom.current().nextInt(1, 5));
    }

    public void mover(String direcao) {
        switch (direcao.trim().toLowerCase()) {
            case "up" -> mover(1);
            case "down" -> mover(2);
            case "right" -> mover(3);
            case "left" -> mover(4);
            default -> throw new MovimentoInvalidoException();
        }
    }

    public void mover(int direcao) {
        posXAnterior = posX;
        posYAnterior = posY;
        int novoX = posX;
        int novoY = posY;

        switch (direcao) {
            case 1 -> { movimento = Movimento.UP; novoX--; }
            case 2 -> { movimento = Movimento.DOWN; novoX++; }
            case 3 -> { movimento = Movimento.RIGHT; novoY++; }
            case 4 -> { movimento = Movimento.LEFT; novoY--; }
            default -> {
                qtdMovimentosInvalidos++;
                throw new MovimentoInvalidoException();
            }
        }

        if (novoX < 1 || novoX > Tabuleiro.DIMENSAO_TABULEIRO ||
            novoY < 1 || novoY > Tabuleiro.DIMENSAO_TABULEIRO) {
            qtdMovimentosInvalidos++;
            throw new MovimentoInvalidoException();
        }

        posX = novoX;
        posY = novoY;
        qtdMovimentosValidos++;
    }

    public void voltarPosicaoAnterior() {
        posX = posXAnterior;
        posY = posYAnterior;
    }

    public boolean encontrouAlimento(Fruta fruta) {
        return fruta != null && fruta.getPosX() == posX && fruta.getPosY() == posY;
    }

    @Override
    public String toString() {
        return "Robô " + cor.getTipoCor() +
                "\nQuantidade de movimentos válidos: " + qtdMovimentosValidos +
                "\nQuantidade de movimentos inválidos: " + qtdMovimentosInvalidos +
                "\nTotal de movimentos: " + (qtdMovimentosValidos + qtdMovimentosInvalidos);
    }
}
