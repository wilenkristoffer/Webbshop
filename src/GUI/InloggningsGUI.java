package GUI;

import Data.Kund;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class InloggningsGUI extends JFrame implements ActionListener {
    protected JPanel loginPromptPanel = new JPanel();
    protected JLabel welcomeMsg = new JLabel("Välkommen till Webbshoppen");
    protected JLabel loginMsg = new JLabel("Skriv in din email och lösenord för att fortsätta");
    protected JTextField emailTextField = new JTextField("Email...",20);
    protected JTextField passwordTextField = new JTextField("Lösenord...",20);
    protected JButton loginButton = new JButton("Logga in");
    protected JButton cancelButton = new JButton("Avbryt");
    protected JButton salesSupportButton = new JButton("Säljstöd");

    public InloggningsGUI(){

        SwingUtilities.invokeLater(() -> {

            emailTextField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (emailTextField.getText().equals("Email...")) {
                        emailTextField.setText("");
                    }
                }
            });

            passwordTextField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (passwordTextField.getText().equals("Lösenord...")) {
                        passwordTextField.setText("");
                    }
                }
            });

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(300, 200);
        this.add(loginPromptPanel);
        loginPromptPanel.add(welcomeMsg);
        loginPromptPanel.add(loginMsg);
        loginPromptPanel.add(emailTextField);
        loginPromptPanel.add(passwordTextField);
        loginPromptPanel.add(loginButton);
        loginPromptPanel.add(cancelButton);
        loginPromptPanel.add(salesSupportButton);

        loginButton.addActionListener(this);
        cancelButton.addActionListener(this);
        salesSupportButton.addActionListener(this);
        loginButton.requestFocusInWindow();

        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == cancelButton){
            this.dispose();
        }
        else if(e.getSource() == loginButton){
            try {
                final Kund kund = new Kund();
                //Hämtar data från databasen via Repository
                kund.getCustomerInfoFromDB();
                //Hämtar listan med kund-info från Kund
                final List<Kund> listWLoginInfo = kund.getListWCustomerInfo();

                //Stream med lambda för att se om email matchar, ger null om ingen match
                List<String> allEmails = listWLoginInfo.stream().map(Kund::getEmail).toList();
                String emailFromDB = allEmails.stream().filter(email -> email.equals(emailTextField.getText())).findFirst().orElse(null);

                //Stream med lambda för att se om lösenord matchar, ger null om ingen match
                List<String> allPasswords = listWLoginInfo.stream().map(Kund::getLösenord).toList();
                String passwordFromDB = allPasswords.stream().filter(password -> password.equals(passwordTextField.getText())).findFirst().orElse(null);


                if (emailFromDB == null || passwordFromDB == null){
                    JOptionPane.showMessageDialog(null,"Fel inloggnings-uppgifter, försök igen!");
                    System.out.println("Email eller lösenord som användaren skrev in matchade inget data från DB");
                }

               else  {
                  final int kundId = getIdForMatch(listWLoginInfo, emailFromDB, passwordFromDB);
                  final String kundName = getNameForMatch(listWLoginInfo, kundId);
                  WebbshopGUI wui = new WebbshopGUI(kundId, kundName);
                  System.out.println("Inloggning lyckades, kundId: " + kundId);
                  this.dispose();
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        else if (e.getSource() == salesSupportButton){
            try {
                SäljstödGUI sui = new SäljstödGUI();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.dispose();
        }
    }

    //Metod som letar igenom listan med email/pass för att hitta det id som tillhör matchande email/pass
    //Används för att skicka vidare namn och kundid till WebbshopGUI

    //Skulle också kunnat ha en metod i Repository för att hämta data som matchar email/pass
    // "select namn, id from Kund where ?(email), ?(lösenord)"
    private static int getIdForMatch(List<Kund> list, String emailMatch, String passwordMatch) {
        return list.stream().filter(kund ->
                        (emailMatch.equals(kund.getEmail())) || (passwordMatch.equals(kund.getLösenord())))
                .mapToInt((kund) -> kund.getId()).findFirst().getAsInt();
    }

    //Metod för att få fram namnet som matchar till id
    private static String getNameForMatch(List<Kund> list, int kundId) {
        return list.stream().filter(kund -> kundId == kund.getId()).map((kund) -> kund.getNamn()).findFirst().get();    }
}
