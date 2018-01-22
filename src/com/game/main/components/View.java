package com.game.main.components;

import java.awt.*;
import java.awt.image.VolatileImage;

/**
 * Created by AND0053 on 12/04/2015.
 */
public //public static final long serialVersionUID = 1L;
class View extends Panel {
    public Rectangle viewRect;
    public static final int SCREEN_OFFSET = 0;
    public Color backgroundColor = Color.LIGHT_GRAY;
    public static final Dimension gameSize = new Dimension(800, 600); //figure out

    private VolatileImage screen;
    private Graphics draw;

    public Graphics getDraw() {
        return draw;
    }

    public View(Dimension size){ //constructor
        setPreferredSize(size);
        viewRect = new Rectangle(0, 0, gameSize.width, gameSize.height);

    }
    public void intVolatileImage(){ //creates the volatile image (must be done after the panel is visible)
        screen = createVolatileImage(gameSize.width, gameSize.height);//////////////////////////
    }

    //Draw procedure commands
    public void getSurfaceGraphics(){ //must be used before drawing
        draw = screen.getGraphics();
    }

    public void fillGraphics(){ //fills the screen with the background color
        setColor(backgroundColor);
        draw.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    }

    public void drawToScreen(){ //draws the image to the panel(updates the screen)
        //this method needs to draw to another temp object, which will then
        draw = getGraphics();
        draw.drawImage(screen, 0, 0, getWidth(), getHeight(), 0, 0, screen.getWidth(), screen.getHeight(), null);
    }

    public void dumpGraphics(){ //dumps the graphics object, freeing memory (should be used after drawing)
        draw.dispose();
    }

    public boolean isInView(int x, int y, int width, int height){ //check if rectangle is in view
        boolean point1 = viewRect.contains(x, y);
        boolean point2 = viewRect.contains(x + width, y + height);
        return point1 || point2;
    }

    public boolean isInView(Rectangle r){ //check if rectangle is in view
        return isInView(r.x, r.y, r.width, r.height);
    }

    public void setColor(Color col){ //sets the draw colour
        draw.setColor(col);
    }

    public void drawScreen(){ //draw a box around the screen size (if the screen fits the frame, it cannot be seen)
        setColor(Color.black); //mainly a debug tool
        draw.drawRect(SCREEN_OFFSET - 1, SCREEN_OFFSET - 1, gameSize.width + 1, gameSize.height + 1);
    }

    public void renderGameRect(int x, int y, int width, int height, boolean fill){ //draws a game rectangle (dosent check if its on the screen)
        if (fill) {
            draw.fillRect(x - viewRect.x + SCREEN_OFFSET, y - viewRect.y + SCREEN_OFFSET, width, height);
        } else {
            draw.drawRect(x - viewRect.x + SCREEN_OFFSET, y - viewRect.y + SCREEN_OFFSET, width, height);
        }
    }

    public void renderLine(int x1, int y1, int x2, int y2) {
        draw.drawLine(x1 - viewRect.x + SCREEN_OFFSET, y1 - viewRect.y + SCREEN_OFFSET, x2 - viewRect.x + SCREEN_OFFSET, y2 - viewRect.y + SCREEN_OFFSET);
    }

    //copy polygon
    public void renderPolygon(Polygon p) {
        p.translate( -viewRect.x + SCREEN_OFFSET, -viewRect.y + SCREEN_OFFSET);
        draw.drawPolygon(p);
    }

    public void drawGUIText(String s, int x, int y){ //draws text onto the screen (not in the game world)
        draw.drawString(s, SCREEN_OFFSET + x, SCREEN_OFFSET + y);
    }

    public int getFontHeight() {
        return draw.getFontMetrics().getHeight();
    }
}