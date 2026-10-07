package models;

public enum Cor {
    RED("red", 1), BLUE("blue", 2), GREEN("green", 3), WHITE("white", 4), DARK("dark", 5), YELLOW("yellow", 6);

    private final String tipoCor;
    private final int numTipoCor;

    Cor(String tipoCor, int numTipoCor) {
        this.tipoCor = tipoCor;
        this.numTipoCor = numTipoCor;
    }

    public String getTipoCor() { return tipoCor; }
    public int getNumTipoCor() { return numTipoCor; }
}
