package GUI;

import Data.Beställning;
import Data.Repository;
import Data.Sko;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class WebbshopGUI extends JFrame implements ActionListener {
    protected int kundId;
    protected String kundName;
    protected JPanel topPanel = new JPanel();
    protected JLabel kundNamnText = new JLabel();
    protected JLabel webbshopText = new JLabel("Välj en produkt från listan som du vill lägga till i en beställning");
    protected JPanel itemsPanel = new JPanel();
    protected JComboBox<Sko> itemsComboBox;
    protected JPanel buttonsPanel = new JPanel();
    protected JButton chooseButton = new JButton("Välj");
    protected JButton cancelButton = new JButton("Avbryt");
    protected JLabel orderNumberText = new JLabel();
    protected JLabel orderItemText = new JLabel();
    protected final Repository r = new Repository();
    protected final Sko shoes = new Sko();
    protected final Beställning beställning = new Beställning();

    public WebbshopGUI(int kundId, String kundName) throws IOException {
        this.kundId = kundId;
        this.kundName = kundName;

        SwingUtilities.invokeLater(() -> {

            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.setSize(400, 200);
            this.setLayout(new BorderLayout());
            this.add(topPanel, BorderLayout.NORTH);
            this.add(itemsPanel, BorderLayout.CENTER);
            this.add(buttonsPanel, BorderLayout.SOUTH);

            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            kundNamnText.setAlignmentX(Component.CENTER_ALIGNMENT);
            webbshopText.setAlignmentX(Component.CENTER_ALIGNMENT);

            topPanel.add(kundNamnText);
            kundNamnText.setText("Välkommen " +kundName+ "!");
            topPanel.add(webbshopText);



            //loadAllItems i repo blir anropad, sparar alla skor till en lista i Sko klassen
            try {
                shoes.loadAllItems();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //Hämtar listan med all skor
            final List<Sko> allShoes = shoes.getListWAllShoes();

            //Lägger in en rad för varje sko, text tilldelas mha .toString i Sko klassen.
            DefaultComboBoxModel<Sko> comboBoxModel = new DefaultComboBoxModel<>();
            //forEach stoppar in varje sko i ComboBoxen
            allShoes.stream().forEach((s) -> comboBoxModel.addElement(s));
            itemsComboBox = new JComboBox<>(comboBoxModel);

            itemsPanel.add(itemsComboBox);
            itemsPanel.add(orderNumberText);
            itemsPanel.add(orderItemText);

            buttonsPanel.add(chooseButton);
            buttonsPanel.add(cancelButton);

            chooseButton.addActionListener(this);
            cancelButton.addActionListener(this);

        });


    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == cancelButton){
            this.dispose();
        }
        else if (e.getSource() == chooseButton) {
            try {
                beställning.getOrderIDListFromDB();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //En lista med beställningsID
            final List<Beställning> beställningListID = beställning.getOrderIDList();

            // Använder streams max funktion med comparator för att hitta högsta värdet = sista lagda ordern
            final Beställning lastOrder = beställningListID.stream()
                    .max(Comparator.comparingInt(Beställning::getId))
                    .orElse(null);

            final int lastOrderID;
            if (lastOrder != null) {
                lastOrderID = lastOrder.getId() + 1;
                // Hämtar ut värdet och sparar i en int med +1 som increment
                System.out.println("Sista orderID + 1 = " + lastOrderID);
            } else {
                //default värde om listan var tom
                lastOrderID = 1;
                System.out.println("Listan var tom");
            }

            // Hämtar vald sko
            final Sko selectedSko = (Sko) itemsComboBox.getSelectedItem();
            // Id för vald sko
            final int selectedItemId = selectedSko.getId();

            try {
                // Sist körs addToCart
                r.addToCart(kundId, lastOrderID, selectedItemId);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            orderNumberText.setText("Ny beställning, beställningsID: " + lastOrderID);
            orderItemText.setText("Vara: " + selectedSko);

        }

    }
}
