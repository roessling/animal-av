/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package animal.main.lookandfeel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

import animal.gui.AnimalMainWindow;
import animal.main.AnimationWindow;
//import sun.swing.DefaultLookup;


/**
 * A Basic L&amp;F implementation of SliderUI.
 *
 * @author Tom Santos
 */
public class AnimalStepBasicSliderUI extends BasicSliderUI{

    /////////////////////////////////////////////////////////////////////////////
    // ComponentUI Interface Implementation methods
    /////////////////////////////////////////////////////////////////////////////
    public static ComponentUI createUI(JComponent b)    {
        return new AnimalStepBasicSliderUI((JSlider)b);
    }

    public AnimalStepBasicSliderUI(JSlider b)   {
      super(b);
    }
    

    public void paint( Graphics g, JComponent c )   {
        recalculateIfInsetsChanged();
        recalculateIfOrientationChanged();
        Rectangle clip = g.getClipBounds();
        
        if ( !clip.intersects(trackRect) && slider.getPaintTrack())
            calculateGeometry();

        if ( slider.getPaintTrack() && clip.intersects( trackRect ) ) {
            paintTrack( g );
        }
        if ( slider.getPaintTicks() && clip.intersects( tickRect ) ) {
            paintTicks( g );
        }
        if ( slider.getPaintLabels() && clip.intersects( labelRect ) ) {
            paintLabels( g );
        }
//        if ( slider.hasFocus() && clip.intersects( focusRect ) ) {
//            paintFocus( g );
//        }
        if ( clip.intersects( thumbRect ) ) {
            paintThumb( g );
        }
    }
    
    public void paintTrack(Graphics g)  {

      Rectangle trackBounds = trackRect;

      if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
          int cy = (trackBounds.height / 2) - 2;
          int cw = trackBounds.width;

          g.translate(trackBounds.x, trackBounds.y + cy);
          
            g.setColor(new Color(160, 160, 160));
            g.fillRect(0, 0, cw, cy);
            

            AnimationWindow aw = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false);
            int maxStep = slider.getMaximum()-1;
            int curStep = aw.getAnimationState().getStep();
            g.setColor(new Color(202, 112, 10));
            g.fillRect(0, 0, (int)(((double)cw)/maxStep*(curStep-1)), cy);
            
            g.translate(-trackBounds.x, -(trackBounds.y + cy));
      }
      else {
          int cx = (trackBounds.width / 2) - 2;
          int ch = trackBounds.height;

          g.translate(trackBounds.x + cx, trackBounds.y);

          g.setColor(getShadowColor());
          g.drawLine(0, 0, 0, ch - 1);
          g.drawLine(1, 0, 2, 0);
          g.setColor(getHighlightColor());
          g.drawLine(3, 0, 3, ch);
          g.drawLine(0, ch, 3, ch);
          g.setColor(Color.black);
          g.drawLine(1, 1, 1, ch-2);

          g.translate(-(trackBounds.x + cx), -trackBounds.y);
      }
  }
    
    public void paintThumb(Graphics g)  {
      Rectangle knobBounds = thumbRect;
      int w = knobBounds.width;
      int h = knobBounds.height;

      g.translate(knobBounds.x, knobBounds.y);

      if ( slider.isEnabled() ) {
          g.setColor(slider.getBackground());
      }
      else {
          g.setColor(slider.getBackground().darker());
      }

      Boolean paintThumbArrowShape =
          (Boolean)slider.getClientProperty("Slider.paintThumbArrowShape");

      if ((!slider.getPaintTicks() && paintThumbArrowShape == null) ||
          paintThumbArrowShape == Boolean.FALSE) {

          // "plain" version
          g.fillRect(0, 0, w, h);

          g.setColor(Color.black);
          g.drawLine(0, h-1, w-1, h-1);
          g.drawLine(w-1, 0, w-1, h-1);

          g.setColor(super.getHighlightColor());
          g.drawLine(0, 0, 0, h-2);
          g.drawLine(1, 0, w-2, 0);

          g.setColor(super.getShadowColor());
          g.drawLine(1, h-2, w-2, h-2);
          g.drawLine(w-2, 1, w-2, h-3);
      }
      else if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
          
          g.setColor(Color.BLACK);
          g.fillRect(1, 1, w-2, h-4);
          Polygon pb = new Polygon();
          pb.addPoint(1, 1+h-4);
          pb.addPoint(1+w-2, 1+h-4);
          pb.addPoint(1+(w-2)/2, 1+h-4+5);
          g.fillPolygon(pb);
          
          Color innerPaintColor = new Color(224, 224, 224);
          try {
            Component c1 = AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().getFocusOwner();
            Component c2 = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationWindowView().getFocusOwner();
            boolean isFocusOnSlider = (c1!= null && c1.equals(slider)) || (c2!= null && c2.equals(slider));
            innerPaintColor = isFocusOnSlider ? Color.CYAN : new Color(224, 224, 224);
          } catch (Exception e) {
            e.printStackTrace();
          }
          g.setColor(innerPaintColor);
          g.fillRect(2, 2, w-4, h-6);
          Polygon pf = new Polygon();
          pf.addPoint(2, 2+h-6);
          pf.addPoint(2+w-4, 2+h-6);
          pf.addPoint(2+(w-4)/2, 2+h-6+4);
          g.fillPolygon(pf);

          g.setColor(Color.BLUE);
//          Polygon p = new Polygon();
//          p.addPoint(1, h-cw);
//          p.addPoint(cw-1, h-1);
//          p.addPoint(w-2, h-1-cw);
//          g.fillPolygon(p);

//          g.setColor(super.getHighlightColor());
//          g.drawLine(0, 0, w-2, 0);
//          g.drawLine(0, 1, 0, h-1-cw);
//          g.drawLine(0, h-cw, cw-1, h-1);
//
//          g.setColor(Color.black);
//          g.drawLine(w-1, 0, w-1, h-2-cw);
//          g.drawLine(w-1, h-1-cw, w-1-cw, h-1);
//
//          g.setColor(super.getShadowColor());
//          g.drawLine(w-2, 1, w-2, h-2-cw);
//          g.drawLine(w-2, h-1-cw, w-1-cw, h-2);
          
//          g.setColor(Color.RED);
//          g.fillOval(30, 0, 20, 20);
      }

      g.translate(-knobBounds.x, -knobBounds.y);
  }
    

    public void paintTicks(Graphics g)  {
        Rectangle tickBounds = tickRect;

        g.setColor(Color.BLACK); //DefaultLookup.getColor(slider, this, "Slider.tickColor", Color.black));

        if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
            g.translate(0, tickBounds.y);

            if (slider.getMinorTickSpacing() > 0) {
                int value = slider.getMinimum();

                while ( value <= slider.getMaximum() ) {
                    int xPos = xPositionForValue(value);
                    paintMinorTickForHorizSlider( g, tickBounds, xPos );

                    // Overflow checking
                    if (Integer.MAX_VALUE - slider.getMinorTickSpacing() < value) {
                        break;
                    }

                    value += slider.getMinorTickSpacing();
                }
            }

            if (slider.getMajorTickSpacing() > 0) {
                int value = slider.getMinimum();

                while ( value <= slider.getMaximum() ) {
                    int xPos = xPositionForValue(value);
                    paintMajorTickForHorizSlider( g, tickBounds, xPos );

                    // Overflow checking
                    if (Integer.MAX_VALUE - slider.getMajorTickSpacing() < value) {
                        break;
                    }

                    value += slider.getMajorTickSpacing();
                }
                paintMajorTickForHorizSlider( g, tickBounds, xPositionForValue(slider.getMaximum()) );
            }

            g.translate( 0, -tickBounds.y);
        }
    }
}
