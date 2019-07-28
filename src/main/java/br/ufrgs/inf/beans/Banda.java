package br.ufrgs.inf.beans;

import java.io.Serializable;

public class Banda implements Serializable {

    private int larguraBanda;

    public Banda(int larguraBanda) {
        this.larguraBanda = larguraBanda;
    }

    public int getLarguraBanda() {

        return larguraBanda;
    }

}
