package Data;

import java.io.IOException;
import java.util.List;

public class Kund {
   protected int id;
   protected String namn;
   protected String adress;
   protected String email;
   protected String telefon;
   protected String ort;
   protected String lösenord;
   protected List<Kund> listWCustomerInfo;
   protected final Repository repo = new Repository();

    public void getCustomerInfoFromDB() throws IOException {
        listWCustomerInfo = repo.getCustomerInfo();
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLösenord() {
        return lösenord;
    }

    public void setLösenord(String lösenord) {
        this.lösenord = lösenord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Kund> getListWCustomerInfo() {
        return listWCustomerInfo;
    }

    public void setListWCustomerInfo(List<Kund> listWCustomerInfo) {
        this.listWCustomerInfo = listWCustomerInfo;
    }
}


