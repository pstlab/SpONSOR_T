/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.solution;

import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleActivitySolutionPanel extends javax.swing.JPanel {

    private ActivityTurn turn;

    /**
     * Creates new form SingleActivitySolutionPanel
     */
    public SingleActivitySolutionPanel() {
        initComponents();
    }

    public SingleActivitySolutionPanel(ActivityTurn turn) {
        initComponents();
        setTurn(turn);
    }
    
    public ActivityTurn getTurn() {
        return turn;
    }

    public final void setTurn(ActivityTurn turn) {
        this.turn = turn;
        System.out.println("CREAZIONE TURN -> "+turn);
        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        this.jLabel_from.setText(dt.format(turn.getStartTime()));
        this.jLabel_to.setText(dt.format(turn.getEndTime()));
        String list = "<html>";
        System.out.println("<<<<<>>>>>>>>> COMF ZIE: "+turn.getComfirmedTurns().size());
        for (ComfirmedTurn comfirmedTurn : turn.getComfirmedTurns()) {
            Person person = comfirmedTurn.getPerson();
            if(person.getKeywords().isEmpty()){
                list+=("&#8226 "+person.getName()+" "+person.getSurname()+" <br>");
            }else{
                 String kkk = person.getKeywords().stream().map(i -> i.getKeyword()).collect(Collectors.joining(", "));
                 list+=("&#8226 "+person.getName()+" "+person.getSurname()+" <b>("+kkk+")</b><br>");
            }
           
            
        }
//        list+="</ul>";
        this.jLabel_list.setText(list);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_from = new JLabel();
        jLabel_to = new JLabel();
        jLabel_list = new JLabel();

        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        jLabel_from.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel_from.setForeground(new Color(51, 51, 255));
        jLabel_from.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel_from.setText("jLabel1");
        jLabel_from.setVerticalAlignment(SwingConstants.BOTTOM);

        jLabel_to.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel_to.setForeground(new Color(51, 51, 255));
        jLabel_to.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel_to.setText("jLabel2");
        jLabel_to.setVerticalAlignment(SwingConstants.TOP);

        jLabel_list.setBackground(new Color(153, 255, 153));
        jLabel_list.setText("jLabel3");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_from, GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(jLabel_to, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_list, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_list, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel_from, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_to, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel_from;
    private JLabel jLabel_list;
    private JLabel jLabel_to;
    // End of variables declaration//GEN-END:variables
}
