package Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Repository {
   protected final Properties p = new Properties();
    public List<Kund> getCustomerInfo() throws IOException {
        p.load(new FileInputStream("src/webbshop.properties"));
        try (
                Connection c = DriverManager.getConnection(
                        p.getProperty("connectionString"),
                        p.getProperty("name"),
                        p.getProperty("password"));

                PreparedStatement stmt = c.prepareStatement("select email, lösenord, id, namn from Kund" );

        ) {

            ResultSet rs = stmt.executeQuery();

            List<Kund> kund = new ArrayList<>();
            while (rs.next()) {

                Kund kundInfo = new Kund();
                String emailDB = rs.getString("email");
                kundInfo.setEmail(emailDB);
                String passwordDB = rs.getString("lösenord");
                kundInfo.setLösenord(passwordDB);
                int idDB = rs.getInt("id");
                kundInfo.setId(idDB);
                String nameDB = rs.getString("namn");
                kundInfo.setNamn(nameDB);
                kund.add(kundInfo);

            }
            return kund;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Sko> getAllItems() throws IOException {
        p.load(new FileInputStream("src/webbshop.properties"));
        try (
                Connection c = DriverManager.getConnection(
                        p.getProperty("connectionString"),
                        p.getProperty("name"),
                        p.getProperty("password"));

                PreparedStatement stmt = c.prepareStatement("select id, storlek, färg, pris, märke, saldo from Sko" );

        ) {
            ResultSet rs = stmt.executeQuery();

            List<Sko> allShoes = new ArrayList<>();
            while (rs.next()) {
                Sko shoe = new Sko();
                int id = rs.getInt("id");
                shoe.setId(id);
                int size = rs.getInt("storlek");
                shoe.setStorlek(size);
                String color = rs.getString("färg");
                shoe.setFärg(color);
                double price = rs.getDouble("pris");
                shoe.setPris(price);
                String brand = rs.getString("märke");
                shoe.setMärke(brand);
                double saldo = rs.getDouble("saldo");
                shoe.setSaldo(saldo);
                allShoes.add(shoe);


            }
            return allShoes;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCart(int kundensid, int beställningensid, int produktensid) throws IOException {
        p.load(new FileInputStream("src/webbshop.properties"));
        try (
                Connection c = DriverManager.getConnection(
                        p.getProperty("connectionString"),
                        p.getProperty("name"),
                        p.getProperty("password"));

                CallableStatement stmt = c.prepareCall("call addToCart(?,?,?)");
        ) {

            stmt.setInt(1, kundensid);
            stmt.setInt(2, beställningensid);
            stmt.setInt(3, produktensid);

            stmt.executeQuery();

            System.out.println("AddToCart kördes med:\nKundId: " + kundensid + "\nProduktId: " + produktensid);


        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
    }

        public List<Beställning> getOrderIds() throws IOException{
            p.load(new FileInputStream("src/webbshop.properties"));
            try (
                    Connection c = DriverManager.getConnection(
                            p.getProperty("connectionString"),
                            p.getProperty("name"),
                            p.getProperty("password"));

                    PreparedStatement stmt = c.prepareStatement("select id from Beställning" );

            ) {
                ResultSet rs = stmt.executeQuery();

                List<Beställning> beställning = new ArrayList<>();

                while (rs.next()) {
                    Beställning order = new Beställning();
                    int id = rs.getInt("id");
                    order.setId(id);
                    beställning.add(order);

                }
                return beställning;
            } catch (
                    SQLException e) {
                throw new RuntimeException(e);
            }

        }
    public List<Beställning> getAllOrders() throws IOException{
        p.load(new FileInputStream("src/webbshop.properties"));
        try (
                Connection c = DriverManager.getConnection(
                        p.getProperty("connectionString"),
                        p.getProperty("name"),
                        p.getProperty("password"));

                PreparedStatement stmt = c.prepareStatement("SELECT b.id, b.datum, k.id AS kundid, k.namn AS kundnamn FROM Beställning" +
                        " b JOIN Kund k ON b.kundid = k.id");
        ) {
            ResultSet rs = stmt.executeQuery();

            List<Beställning> beställningList = new ArrayList<>();

            while (rs.next()) {
                Beställning order = new Beställning();
                int orderId = rs.getInt("id");
                order.setId(orderId);
                String datum = rs.getString("datum");
                order.setDatum(datum);


                Kund kund = new Kund();
                int kundId = rs.getInt("kundid");
                String kundName = rs.getString("kundnamn");
                kund.setId(kundId);
                kund.setNamn(kundName);

                order.setKund(kund);

                beställningList.add(order);
            }
            return beställningList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Innehåller> getInnehållerInfo() throws IOException {
        p.load(new FileInputStream("src/webbshop.properties"));
        try (
                Connection c = DriverManager.getConnection(
                        p.getProperty("connectionString"),
                        p.getProperty("name"),
                        p.getProperty("password"));

                PreparedStatement stmt = c.prepareStatement("SELECT innehåller.id, innehåller.antal, sko.märke, sko.storlek, sko.färg, sko.pris, kund.id, kund.namn, kund.adress, beställning.id from Innehåller " +
                        "join Sko on Innehåller.skoid = sko.id " +
                        "join Beställning on innehåller.beställningid = beställning.id " +
                        "join Kund on beställning.kundid = kund.id");
        ) {
            ResultSet rs = stmt.executeQuery();

            List<Innehåller> innehållerList = new ArrayList<>();
                while (rs.next()) {
                    Innehåller innehåller = new Innehåller();
                    int id = rs.getInt("innehåller.id");
                    innehåller.setId(id);
                    int antal = rs.getInt("innehåller.antal");
                    innehåller.setAntal(antal);

                    Sko sko = new Sko();
                    String märke = rs.getString("sko.märke");
                    int storlek = rs.getInt("sko.storlek");
                    String färg = rs.getString("sko.färg");
                    int skoPris = rs.getInt("sko.pris");
                    sko.setPris(skoPris);
                    sko.setMärke(märke);
                    sko.setStorlek(storlek);
                    sko.setFärg(färg);

                    Kund kund = new Kund();
                    kund.setId(rs.getInt("kund.id"));
                    kund.setNamn(rs.getString("kund.namn"));
                    kund.setAdress(rs.getString("kund.adress"));

                    Beställning beställning = new Beställning();
                    beställning.setId(rs.getInt("beställning.id"));
                    beställning.setKund(kund);

                    innehåller.setSko(sko);
                    innehåller.setBeställning(beställning);

                    innehållerList.add(innehåller);

            }
            return innehållerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    }

