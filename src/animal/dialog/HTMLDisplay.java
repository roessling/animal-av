/*
 * @(#)HtmlDemo.java	1.12 05/11/17
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
package animal.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
public class HTMLDisplay { //extends DemoModule {

    JEditorPane html;
    JPanel panel = null;

    /**
     * HtmlDemo Constructor
     */
    public HTMLDisplay() { //SwingSet2 swingset) {
    	// Set the title for this demo, and an icon used to represent this
    	// demo inside the SwingSet2 app.
     	panel = new JPanel();
     	panel.setLayout(new BorderLayout());
    }
    
    private void finalizeDisplay() {
    	JFrame frame = new JFrame("HTML");
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
    
}