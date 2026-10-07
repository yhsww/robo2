package models;

public class Bomba extends Obstaculo {
    public Bomba(int id) { super(id); }

    @Override
    public void bater(Robo robo) {
        robo.setExplodiu(true);
        System.out.println("BOOM! O robô " + robo.getCor().getTipoCor() + " explodiu!");
    }
}
