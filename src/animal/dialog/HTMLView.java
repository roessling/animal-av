package animal.dialog;
/*
 * @(#)HtmlDemo.java  1.12 05/11/17
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
/**
 * Html Demo
 *
 * @version 1.0 2007-01-17
 * @author Guido Roessling (roessling@acm.org>
 * (heavily) based on Jeff Dinkins' HtmlDemo, accompanying JDK 6
 */
public class HTMLView { //extends DemoModule {

    JEditorPane html;
    JPanel panel = null;
  private JFrame frame;
  private Font   defaultFont = new Font("Dialog", 0, 14);

    /**
     * HtmlDemo Constructor
     */
    public HTMLView() { //SwingSet2 swingset) {
      // Set the title for this demo, and an icon used to represent this
      // demo inside the SwingSet2 app.
      panel = new JPanel();
      panel.setLayout(new BorderLayout());
    }
    
    private void finalizeDisplay() {
    frame = new JFrame("HTML");
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(panel, BorderLayout.CENTER);
      panel.setPreferredSize(new Dimension(640, 480));
      frame.pack();
      frame.setVisible(true);
    }
    
    public void setURL(String path) throws MalformedURLException, IOException {
      URL url = null;
      try {
        url = getClass().getResource(path);
      } catch (Exception e) {
        System.err.println("Failed to open " + path);
        url = null;
      }
      
      if (url != null) {
        html = new JEditorPane(url);
        html.setEditable(false);
        html.addHyperlinkListener(createHyperLinkListener());
        
        JScrollPane scroller = new JScrollPane();
        JViewport vp = scroller.getViewport();
        vp.add(html);
        panel.add(scroller, BorderLayout.CENTER);
        finalizeDisplay();
      }
    }
    
    public HyperlinkListener createHyperLinkListener() {
      return new HyperlinkListener() {
        public void hyperlinkUpdate(HyperlinkEvent e) {
          if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (e instanceof HTMLFrameHyperlinkEvent) {
              ((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent(
                  (HTMLFrameHyperlinkEvent)e);
            } else {
              try {
                html.setPage(e.getURL());
              } catch (IOException ioe) {
                System.out.println("IOE: " + ioe);
              }
            }
          }
        }
      };
    }
    
    void updateDragEnabled(boolean dragEnabled) {
      html.setDragEnabled(dragEnabled);
    }
    
  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Dimension dim = new Dimension(0, 0);
    
    if(frame != null)
      dim = frame.getSize();

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }

    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 200) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

    }



    if (html != null) {
      Font f = new Font(html.getFont().getFontName(), html.getFont().getStyle(),
          defaultFont.getSize());

      html.setFont(f);
      html.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
          Boolean.TRUE);

    }

    if (frame != null)
      frame.setSize(dim);

  }

}