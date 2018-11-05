package it.cnr.istc.sponsor.tt.abstracts;

import java.awt.Color;
import java.awt.Paint;

public class PaintSupplier
{
    private Color[] paintSequence;
    static int curIdx;
    
    public PaintSupplier()
    {
        paintSequence = createPaintSequence();
        curIdx = 0;
    }
    
    public static void reset(){
        curIdx = 0;
    }
    
    public Color getNextPaint()
    {
        return paintSequence[curIdx++ % paintSequence.length];
    }
    
    public Color[] createPaintSequence()
    {
        return new Color [] {
                new Color(0xFF, 0x55, 0x55),
                new Color(0x55, 0x55, 0xFF),
                new Color(0x55, 0xFF, 0x55),
                new Color(0xFF, 0xFF, 0x55),
                new Color(0xFF, 0x55, 0xFF),
                new Color(0x55, 0xFF, 0xFF),
                Color.pink,
                Color.gray,
                new Color(0xc0, 0x00, 0x00),
                new Color(0x00, 0x00, 0xC0),
                new Color(0x00, 0x80, 0x00),
                new Color(0xC0, 0xC0, 0x00),
                new Color(0xC0, 0x00, 0xC0),
                new Color(0x00, 0xC0, 0xC0),
                Color.darkGray,
                new Color(0xFF, 0x40, 0x40),
                new Color(0x40, 0x40, 0xFF),
                new Color(0x40, 0xFF, 0x40),
                new Color(0xFF, 0xFF, 0x40),
                new Color(0xFF, 0x40, 0xFF),
                new Color(0x40, 0xFF, 0xFF),
                Color.lightGray,
                new Color(0x80, 0x00, 0x00),
                new Color(0x00, 0x00, 0x80),
                new Color(0x00, 0x80, 0x00),
                new Color(0x80, 0x80, 0x00),
                new Color(0x80, 0x00, 0x80),
                new Color(0x00, 0x80, 0x80),
                new Color(0xFF, 0x80, 0x80),
                new Color(0x80, 0x80, 0xFF),
                new Color(0x80, 0xFF, 0x80),
                new Color(0xFF, 0xFF, 0x80),
                new Color(0xFF, 0x80, 0xFF),
                new Color(0x80, 0xFF, 0xFF)
        };
    }

}
