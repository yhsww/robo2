package models;

public abstract class Obstaculo {
    protected int id;
    protected int posX;
    protected int posY;

    public Obstaculo(int id) { this.id = id; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public void setPosX(int posX) { this.posX = posX; }
    public void setPosY(int posY) { this.posY = posY; }
    public abstract void bater(Robo robo);
}
