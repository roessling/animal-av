/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animal.main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import translator.Translator;
import animal.gui.AnimalMainWindow;
import animal.main.icons.LoadIcon;

/**
 *
 * @author Wester
 */
public class SlideControlBar extends JPanel {
  
  private AnimationWindow animationWindow;
  private int maxStep;
  private JButton forwards;
  private JButton backwards;
  private JTextField number;
  private JPanel slider;
  private JButton first;
  private JButton last;
  private JProgressBar progress;
  private JLabel l;
  
  private Translator translator;
  private boolean slideMode = true;

    /**
   * 
   */
  private static final long serialVersionUID = 1L;

    public SlideControlBar(AnimationWindow controller) {
      
      if (controller != null)
        animationWindow = controller;
      else
        animationWindow = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true);
      
      this.translator = new Translator("AnimationFrame", Animal.getCurrentLocale());
      
      init();
    }
    
    private void init(){
      
      LoadIcon get = new LoadIcon();
      
      setLayout(new BorderLayout(0, 0));
      slider = new JPanel();
      backwards = new JButton();
      backwards.setIcon(get.createBackIcon());
      backwards.setEnabled(true);
      backwards.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          animationWindow.backwardAnimation();
        }});
      slider.add(backwards);
      number = new JTextField();
      number.setHorizontalAlignment(JTextField.CENTER);
      number.setColumns(2);
      number.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          int s = Integer.parseInt(number.getText());
          if(s == 0){
            s = 1;
          }
          if(s > maxStep){
            s = maxStep;
          }
          animationWindow.setStep(s, true);
        }});
      slider.add(number);
      forwards = new JButton();
      forwards.setIcon(get.createForwardIcon());
      forwards.setEnabled(false);
      forwards.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          animationWindow.forwardAnimation();
        }});
      slider.add(forwards);
      
      JPanel firstPane = new JPanel();
      firstPane.setLayout(new BoxLayout(firstPane, BoxLayout.Y_AXIS));
      
      JPanel spaceFirst = new JPanel();
      //spaceFirst.setSize(new Dimension(10, 10));
      JPanel desFirst = new JPanel();
      
      JLabel f = new JLabel("1");
      f.setHorizontalAlignment(JLabel.CENTER);
      //f.setAlignmentX(CENTER_ALIGNMENT);
      f.setAlignmentY(CENTER_ALIGNMENT);
     // f.setSize(new Dimension(20, 10));
      f.setBackground(Color.blue);
      
      first = new JButton();
      first.setIcon(get.createToFirstIcon());
      first.setEnabled(false);
      first.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          animationWindow.setStep(1, true);
        }});
      
      desFirst.add(spaceFirst);
      desFirst.add(f);
      firstPane.add(f);
      firstPane.add(first);
      
      JPanel lastPane = new JPanel();
      lastPane.setLayout(new BoxLayout(lastPane, BoxLayout.Y_AXIS));
      
      JPanel desLast = new JPanel();
      JPanel spaceLast = new JPanel();
      spaceLast.setSize(new Dimension(40, 20));
      l = new JLabel("1");
      l.setBackground(Color.blue);
      
      last = new JButton();
      last.setIcon(get.createToLastIcon());
      last.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          animationWindow.endOfAnimation();
        }});
      desLast.add(spaceLast);
      desLast.add(l);
      lastPane.add(desLast);
      lastPane.add(last);
      
      progress = new JProgressBar();
      add(BorderLayout.NORTH, progress);
      add(BorderLayout.WEST, firstPane);
      add(BorderLayout.CENTER, slider);
      add(BorderLayout.EAST, lastPane);
       
      number.setToolTipText(translator.translateMessage("number"));
      backwards.setToolTipText(translator.translateMessage("backwards"));
      first.setToolTipText(translator.translateMessage("first"));
      forwards.setToolTipText(translator.translateMessage("forwards"));
      last.setToolTipText(translator.translateMessage("last"));
      
    }

    public void setLength(int w) {
      // TODO Auto-generated method stub
      setSize(new Dimension(w, 20));
    }

    public void setSteps(int n) {
      // TODO Auto-generated method stub
      l.setText(String.valueOf(n));
      maxStep = n;
      animationWindow.startOfAnimation();
      animationWindow.setStep(1, true);
      number.setText("0");
      if(n > 999){
        number.setColumns(4);
      }
      if(n < 999 && n > 99){
        number.setColumns(3);
      }
      progress.setMaximum(n);
      progress.setMinimum(1);
      progress.setStringPainted(true);
      progress.setValue(1);
      slideMode = false;
     
    }

    public void setStep(int step) {
      // TODO Auto-generated method stub
      number.setText(String.valueOf(step));
      progress.setValue(step);
    }

    public void determineButtonState(int step) {
      // TODO Auto-generated method stub
      
      if(!forwards.isEnabled()){
        forwards.setEnabled(true);
      }
      
      if(!last.isEnabled()){
        last.setEnabled(true);
      }
      
      if(!backwards.isEnabled()){
        backwards.setEnabled(true);
      }
      
      if(!first.isEnabled()){
        first.setEnabled(true);
      }
      
      if(step == 1 || step == 0){
        backwards.setEnabled(false);
        first.setEnabled(false);
        slideMode = false;
        
      }else{
        if(step == maxStep){
          forwards.setEnabled(false);
          last.setEnabled(false);
          slideMode = false;
        }else{
          if(slideMode){
            setButtonEnabled();
          }
        }
      }
      
      
    }

    public void slideMode(boolean b) {
      // TODO Auto-generated method stub
      slideMode = b;
      determineButtonState(animationWindow.getStep());
//      setButtonEnabled();
    }
    
    private void setButtonEnabled(){
     // System.out.println("slideSHow" + b);
        if(slideMode){
         // System.out.println("slideSHow" + b);
          
          backwards.setEnabled(false);
          forwards.setEnabled(false);
          last.setEnabled(false);
          first.setEnabled(false);
         // repaint();
        }else{
         
          backwards.setEnabled(true);
          forwards.setEnabled(true);
          last.setEnabled(true);
          first.setEnabled(true);
          //repaint();
        }
    }

    public void changeLocale(Locale targetLocale) {
      // TODO Auto-generated method stub
      translator.setTranslatorLocale(Animal.getCurrentLocale());
      number.setToolTipText(translator.translateMessage("number"));
      backwards.setToolTipText(translator.translateMessage("backwards"));
      first.setToolTipText(translator.translateMessage("first"));
      forwards.setToolTipText(translator.translateMessage("forwards"));
      last.setToolTipText(translator.translateMessage("last"));
    }
    
}
