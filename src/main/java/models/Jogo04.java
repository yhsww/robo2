package models;

import java.util.ArrayList;
import java.util.List;

public class Jogo04 extends Jogo {
    private final List<Obstaculo> obstaculos = new ArrayList<>();
    public Jogo04() { super(); }
    public List<Obstaculo> getObstaculos() { return obstaculos; }
    public boolean adicionarObstaculo(Obstaculo obstaculo) {
        if (tabuleiro.adicionarObstaculos(obstaculo)) {
            obstaculos.add(obstaculo);
            return true;
        }
        return false;
    }
    @Override public void iniciarPartida() { System.out.println("Jogo 04 - robôs e obstáculos."); }
}
