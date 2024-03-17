package GUI;

import Data.Beställning;
import Data.Innehåller;
import Data.Kund;
import Data.Sko;


import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SäljstödGUI extends JFrame implements ActionListener{

    protected JPanel topPanel = new JPanel();
    protected JLabel topText = new JLabel("Välj en av rapporterna i listan, eller klicka på Rapport 1");
    protected JPanel listPanel = new JPanel();
    protected JComboBox<String> comboBox = new JComboBox<>();
    protected JPanel buttonPanel = new JPanel();
    protected JButton chooseButton = new JButton("Välj");
    protected JButton cancelButton = new JButton("Avbryt");
    protected JButton rapportButton = new JButton("Rapport 1");
    protected final Beställning beställning = new Beställning();
    protected final Kund kund = new Kund();
    protected final Innehåller innehåller = new Innehåller();
    protected final Sko sko = new Sko();
    SkoSökInterface skoMärkeSearch = (i, s) -> i.getSko().getMärke().equalsIgnoreCase(s);
    SkoSökInterface skoFärgSearch = (i, s) -> i.getSko().getFärg().equalsIgnoreCase(s);
    //Sko-storlek använder int så vi måste parsea specificSearch(s) för att kunna hitta matchande skor
    SkoSökInterface skoStorlekSearch = (i, s) -> {
        int specificSize = Integer.parseInt(s);
        return i.getSko().getStorlek() == specificSize;
    };

    public SäljstödGUI() throws IOException {
        //Kör metoder för att hämta listor från DB
        try {
            beställning.getAllOrdersFromDB();
            kund.getCustomerInfoFromDB();
            innehåller.getInnehållerInfoFromDB();
            sko.loadAllItems();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        SwingUtilities.invokeLater(() -> {

            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.setSize(400, 200);
            this.setLayout(new BorderLayout());

            this.add(topPanel, BorderLayout.NORTH);
            this.add(listPanel, BorderLayout.CENTER);
            this.add(buttonPanel, BorderLayout.SOUTH);

            topPanel.add(topText);
            listPanel.add(comboBox);
            buttonPanel.add(chooseButton);
            buttonPanel.add(cancelButton);
            buttonPanel.add(rapportButton);

            String[] allQ = {"Rapport 2", "Rapport 3"};

            for (String question : allQ) {
                comboBox.addItem(question);
            }

            chooseButton.addActionListener(this);
            cancelButton.addActionListener(this);
            rapportButton.addActionListener(this);

        });

    }
    //Högre Ordningens Funktion(hör ihop med SkoSökInterface)
    public void sökKunder(String specificSearch, SkoSökInterface ssi){

        final List<Innehåller> listaMedInnehållerInfo = innehåller.getAllInnehållerInfo();

        List<String> customerInfo = listaMedInnehållerInfo.stream()
                .filter(i -> ssi.sök(i, specificSearch))
                .map(Innehåller::getBeställning)
                .map(Beställning::getKund)
                .map(k -> k.getNamn() + ", " + k.getAdress())
                .distinct()
                .collect(Collectors.toList());

        System.out.println("Rapport 1");
        customerInfo.forEach(System.out::println);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cancelButton) {
            this.dispose();

        } else if (e.getSource() == chooseButton) {

            String selectedRapport = (String) comboBox.getSelectedItem();

            if (selectedRapport.equals("Rapport 2")) {

                final List<Beställning> listaMedBeställningInfo = beställning.getAllOrdersList();

                System.out.println("Rapport 2");
                Map<String, List<Beställning>> kundBeställningMap = listaMedBeställningInfo.stream()
                        .collect(Collectors.groupingBy(order -> order.getKund().getNamn()));

                kundBeställningMap.forEach((kundNamn, beställningar) ->
                        System.out.println(kundNamn + ", Antal beställningar: " + beställningar.size()));
            } else if (selectedRapport.equals("Rapport 3")) {

                final List<Innehåller> listaMedInnehållerInfo = innehåller.getAllInnehållerInfo();

                System.out.println("Rapport 3");

                Map<String, Double> totalSummaMap = listaMedInnehållerInfo.stream()
                        .collect(Collectors.groupingBy(m -> m.getBeställning().getKund().getNamn(),
                                Collectors.summingDouble(purchase -> purchase.getAntal() * purchase.getSko().getPris())));

                totalSummaMap.forEach((kundNamn, totalAmountSpent) ->
                        System.out.println(kundNamn + ", Totalbelopp: " + totalAmountSpent + " kr"));
            }

        } else if (e.getSource() == rapportButton) {

             final String searchInput = JOptionPane.showInputDialog("Vad vill du söka på? (Märke, storlek eller färg)");
             final String specificSearch = JOptionPane.showInputDialog("Vad vill du söka på i " + searchInput + "?");

           if(searchInput.equalsIgnoreCase("märke")){

                sökKunder(specificSearch, skoMärkeSearch);

           } else if(searchInput.equalsIgnoreCase("storlek")){

               sökKunder(specificSearch, skoStorlekSearch);

           } else if(searchInput.equalsIgnoreCase("färg")){

               sökKunder(specificSearch, skoFärgSearch);

           } else {
               JOptionPane.showMessageDialog(null,"Du måste skriva något av följande: märke, storlek eller färg");
           }


        }
    }
}
