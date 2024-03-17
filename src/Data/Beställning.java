package Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Beställning {
    protected int id;
    protected String datum;
    protected List<Beställning> orderIDList = new ArrayList<>();
    protected List<Beställning> allOrdersList = new ArrayList<>();
    protected Kund kund = new Kund();
    protected final Repository r = new Repository();

    public void getOrderIDListFromDB() throws IOException {
        orderIDList = r.getOrderIds();
    }
    public void getAllOrdersFromDB() throws IOException {
        allOrdersList = r.getAllOrders();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public List<Beställning> getOrderIDList() {
        return orderIDList;
    }

    public void setOrderIDList(List<Beställning> orderIDList) {
        this.orderIDList = orderIDList;
    }

    public List<Beställning> getAllOrdersList() {
        return allOrdersList;
    }

    public void setAllOrdersList(List<Beställning> allOrdersList) {
        this.allOrdersList = allOrdersList;
    }
    public void setKund(Kund kund) {
        this.kund = kund;
    }

    public Kund getKund() {
        return kund;
    }
}
