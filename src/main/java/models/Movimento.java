package models;

public enum Movimento {
    UP("up", 1), DOWN("down", 2), RIGHT("right", 3), LEFT("left", 4);

    private final String tipoMovimento;
    private final int numTipoMovimento;

    Movimento(String tipoMovimento, int numTipoMovimento) {
        this.tipoMovimento = tipoMovimento;
        this.numTipoMovimento = numTipoMovimento;
    }

    public int getNumTipoMovimento() { return numTipoMovimento; }
    public String getTipoMovimento() { return tipoMovimento; }
}
