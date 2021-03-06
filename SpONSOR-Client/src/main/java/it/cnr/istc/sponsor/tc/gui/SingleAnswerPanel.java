/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tc.gui;

import it.cnr.istc.sponsor.tc.logic.LoginTestManager;
import it.cnr.istc.sponsor.tc.logic.QuestionPointListener;
import it.cnr.istc.sponsor.tc.logic.QuestionPointManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SingleAnswerPanel extends javax.swing.JPanel implements QuestionPointListener {

    private int point = 0;
    private SinglePropensionePanel parentConteiner;
    private String answerCode;
    private int maxPoint;
    private static int qnt = 0;

    /**
     * Creates new form SingleAnswerPanel
     */
    public SingleAnswerPanel() {
        initComponents();
    }

    public SingleAnswerPanel(String answerCode, String answer, SinglePropensionePanel parentConteiner, int maxPoint) {
        initComponents();
        this.jToolBar1.setBorderPainted(false);
        this.parentConteiner = parentConteiner;
        this.parentConteiner.addQuestionPointListener(this);
        QuestionPointManager.getInstance().addQuestionPointListener(this); // QUAQUA
        this.answerCode = answerCode; 
        this.jLabel_code.setText(answerCode + ")");
        this.jLabel_text.setText("<html><div>" + answer + "</div>");
        this.parentConteiner.setPointByAnswer(answerCode, point);
        this.maxPoint = maxPoint;
        if (qnt % 2 == 0) {
            this.setBackground(new Color(240, 251, 255));
        } else {
            this.setBackground(new Color(230, 247, 250));
        }
        qnt++;
    }

    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerCode() {
        return answerCode;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new JPopupMenu();
        jMenuItem1 = new JMenuItem();
        jMenu1 = new JMenu();
        jToolBar1 = new JToolBar();
        jButton_decrease = new JButton();
        jLabel_point = new JLabel();
        jButton_increase = new JButton();
        jLabel_code = new JLabel();
        jLabel_text = new JLabel();

        jMenuItem1.setText("jMenuItem1");
        jPopupMenu1.add(jMenuItem1);

        jMenu1.setText("jMenu1");
        jPopupMenu1.add(jMenu1);

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setOpaque(false);

        jButton_decrease.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tc/icons/arrow_sx.png"))); // NOI18N
        jButton_decrease.setEnabled(false);
        jButton_decrease.setOpaque(false);
        jButton_decrease.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_decreaseActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_decrease);

        jLabel_point.setFont(new Font("Tahoma", 1, 18)); // NOI18N
        jLabel_point.setForeground(new Color(0, 102, 255));
        jLabel_point.setText("0");
        jToolBar1.add(jLabel_point);

        jButton_increase.setIcon(new ImageIcon(getClass().getResource("/it/cnr/istc/sponsor/tc/icons/arrow_dx.png"))); // NOI18N
        jButton_increase.setOpaque(false);
        jButton_increase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_increaseActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_increase);

        jLabel_code.setFont(new Font("Tahoma", 1, 18)); // NOI18N
        jLabel_code.setText("A)");
        jToolBar1.add(jLabel_code);

        jLabel_text.setText("<html><div>daushd asdh aodh aodia odj jdaiosdj asodj daushd asdh aodh aodia odj jdaiosdj asodj daushd asdh aodh aodia odj jdaiosdj asodj daushd asdh aodh aodia odj jdaiosdj asodj daushd asdh aodh aodia odj jdaiosdj asodj daushd asdh aodh aodia odj jdaiosdj asodj daushd asdh aodh aodia odj jdaiosdj asodj </div>");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_text, GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_text)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_increaseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_increaseActionPerformed
        point++;
        this.parentConteiner.setPointByAnswer(answerCode, point);
        if (this.parentConteiner.getUsedPoint() > 0) {
            this.jButton_decrease.setEnabled(true);

        }
        if (this.parentConteiner.getUsedPoint() == maxPoint) {
            this.jButton_increase.setEnabled(false);
        }
        this.jLabel_point.setText("" + point);
        QuestionPointManager.getInstance().pointChanged();
//        this.parentConteiner.pointChanged(); QUAQUA
    }//GEN-LAST:event_jButton_increaseActionPerformed

    private void jButton_decreaseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton_decreaseActionPerformed
        point--;
        this.parentConteiner.setPointByAnswer(answerCode, point);
        if (point == 0) {
            this.jButton_decrease.setEnabled(false);
        }
        if (this.parentConteiner.getUsedPoint() == 0) {
            this.jButton_decrease.setEnabled(false);
        }
        if (this.parentConteiner.getUsedPoint() < maxPoint) {
            this.jButton_increase.setEnabled(true);
        }
        this.jLabel_point.setText("" + point);
        QuestionPointManager.getInstance().pointChanged();
//        this.parentConteiner.pointChanged(); QUAQUA
    }//GEN-LAST:event_jButton_decreaseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton_decrease;
    private JButton jButton_increase;
    private JLabel jLabel_code;
    private JLabel jLabel_point;
    private JLabel jLabel_text;
    private JMenu jMenu1;
    private JMenuItem jMenuItem1;
    private JPopupMenu jPopupMenu1;
    private JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void pointChanged() {

        System.out.println("uhm used point = " + this.parentConteiner.getUsedPoint());
        if (this.parentConteiner.getUsedPoint() < maxPoint) {
            this.jButton_increase.setEnabled(true);
        }
        if (this.parentConteiner.getUsedPoint() > 0) {
            this.jButton_decrease.setEnabled(true);
        }
        if (this.parentConteiner.getUsedPoint() == maxPoint) {
            this.jButton_increase.setEnabled(false);
        }

        if (point == 0) {
            this.jButton_decrease.setEnabled(false);
        } else if (point == 10) {
            this.jButton_increase.setEnabled(false);
        }

        if (this.parentConteiner.getUsedPoint() == 0) {
            this.jButton_decrease.setEnabled(false);
        } else if (this.parentConteiner.getUsedPoint() == maxPoint) {
            this.jButton_increase.setEnabled(false);
        }
        LoginTestManager.getInstance().getAccount().addSingleResult(this.parentConteiner.getQuestionTitle(), answerCode, point);

    }
}
