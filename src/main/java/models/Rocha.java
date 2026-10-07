package models;

public class Rocha extends Obstaculo {
    public Rocha(int id) { super(id); }

    @Override
    public void bater(Robo robo) {
        robo.voltarPosicaoAnterior();
        System.out.println("Ops! O robô encontrou uma rocha.");
    }
}
