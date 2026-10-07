package models;

import java.util.ArrayList;
import java.util.List;

public abstract class Jogo {
    protected final Tabuleiro tabuleiro;
    protected final List<Robo> robos;
    protected Fruta fruta;

    public Jogo() {
        tabuleiro = new Tabuleiro();
        robos = new ArrayList<>();
        fruta = new Fruta();
    }

    public abstract void iniciarPartida();
}
