package Data;

import java.io.IOException;
import java.util.List;

public class Sko {
    protected int id;
    protected int storlek;
    protected String färg;
    protected double pris;
    protected String märke;
    protected double Saldo;
    protected List<Sko> listWAllShoes;
    protected final Repository r = new Repository();

    public void loadAllItems() throws IOException{
        listWAllShoes = r.getAllItems();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStorlek() {
        return storlek;
    }

    public void setStorlek(int storlek) {
        this.storlek = storlek;
    }

    public String getFärg() {
        return färg;
    }

    public void setFärg(String färg) {
        this.färg = färg;
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

    public String getMärke() {
        return märke;
    }

    public void setMärke(String märke) {
        this.märke = märke;
    }

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double saldo) {
        Saldo = saldo;
    }

    public List<Sko> getListWAllShoes() {
        return listWAllShoes;
    }

    public void setListWAllShoes(List<Sko> listWAllShoes) {
        this.listWAllShoes = listWAllShoes;
    }
  @Override
    public String toString() {

        return "Märke: " + märke + " - Storlek: " + storlek + " - Färg: " + färg + " - Pris: " + pris + " kr";
    }
}
