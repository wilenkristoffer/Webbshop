package Data;

import java.io.IOException;
import java.util.List;

public class Innehåller {
    protected int id;
    protected int antal;
    protected Sko sko;
    protected Beställning beställning;
    protected List<Innehåller> allInnehållerInfo;
    protected final Repository r = new Repository();

    public void getInnehållerInfoFromDB() throws IOException {
        allInnehållerInfo = r.getInnehållerInfo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public List<Innehåller> getAllInnehållerInfo() {
        return allInnehållerInfo;
    }

    public void setAllInnehållerInfo(List<Innehåller> allInnehållerInfo) {
        this.allInnehållerInfo = allInnehållerInfo;
    }

    public Sko getSko() {
        return sko;
    }

    public void setSko(Sko sko) {
        this.sko = sko;
    }

    public Beställning getBeställning() {
        return beställning;
    }

    public void setBeställning(Beställning beställning) {
        this.beställning = beställning;
    }

}
