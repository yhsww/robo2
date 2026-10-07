package models;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    public static final int DIMENSAO_TABULEIRO = 4;
    private final List<Obstaculo> obstaculos = new ArrayList<>();
    private final List<Robo> robos = new ArrayList<>();
    private Fruta fruta = new Fruta();

    public boolean validarPosicao(int x, int y) {
        if (fruta.getPosX() == x && fruta.getPosY() == y) return false;
        for (Robo r : robos) if (r.getPosX() == x && r.getPosY() == y) return false;
        for (Obstaculo o : obstaculos) if (o.getPosX() == x && o.getPosY() == y) return false;
        return x >= 1 && x <= 4 && y >= 1 && y <= 4;
    }

    public boolean adicionarRobo(Robo robo) {
        if (!validarPosicao(robo.getPosX(), robo.getPosY())) return false;
        robos.add(robo);
        return true;
    }

    public boolean adicionarFruta(Fruta fruta) {
        if (!validarPosicao(fruta.getPosX(), fruta.getPosY())) return false;
        this.fruta = fruta;
        return true;
    }

    public boolean adicionarObstaculos(Obstaculo obstaculo) {
        if (!validarPosicao(obstaculo.getPosX(), obstaculo.getPosY())) return false;
        obstaculos.add(obstaculo);
        return true;
    }

    public void criarTabuleiro() {
        for (int x = 1; x <= 4; x++) {
            for (int y = 1; y <= 4; y++) {
                String casa = ".";
                if (fruta.getPosX() == x && fruta.getPosY() == y) casa = "F";
                for (Obstaculo o : obstaculos) {
                    if (o.getPosX() == x && o.getPosY() == y) casa = o instanceof Bomba ? "B" : "R";
                }
                for (Robo r : robos) {
                    if (!r.getExplodiu() && r.getPosX() == x && r.getPosY() == y) casa = r.getCor().getTipoCor().substring(0,1).toUpperCase();
                }
                System.out.print("[" + casa + "] ");
            }
            System.out.println();
        }
    }
}
