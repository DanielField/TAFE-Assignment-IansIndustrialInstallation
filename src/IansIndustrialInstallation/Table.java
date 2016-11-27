package IansIndustrialInstallation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

/**
 *
 * @author Daniel Field
 */
public final class Table {
    
    Point tablePos;
    
    int columns, rows,
        cellWidth, cellHeight;
    
    public Table(Graphics g, Color lineColour, Color backColour, Point xy, int columns, int rows, int cellWidth, int cellHeight)
    {
        drawTable(g, lineColour, backColour, xy, columns, rows, cellWidth, cellHeight);
    }
    
    // Used an integer array here because it seemed like the easiest way to return the reading, the x, and the y coordinates.
    public int[] getCellAndData(HashMap<Point, Integer> hm, int cursorX, int cursorY)
    {
        for(int y = 0; y < rows; y++)
        {
            for(int x = 0; x < columns; x++)
            {
                Rectangle cell = new Rectangle(tablePos.x+(x*cellWidth)+1,tablePos.y+(y*cellHeight)+1,cellWidth-1,cellHeight-1);
                if(cursorX < cell.x+cell.width // right
                && cursorX > cell.x // left
                && cursorY < cell.y+cell.height // bottom
                && cursorY > cell.y) // top
                {
                    return new int[] {hm.get(new Point(x,y)), x,y};
                }
            }
        }
        return new int[] {-1};
    }
    
    public void drawReading(Graphics g, Point cell, int reading, int acceptable, int concerning, int danger)
    {
        g.setColor(IansIndustrialInstallation.checkColour(reading, acceptable, concerning, danger));
        g.fillRect(tablePos.x+(cell.x*32)+1, tablePos.y+(cell.y*16)+1, cellWidth-1, cellHeight-1);
    }
    
    // Default colours
    public void drawTable(Graphics g, Point xy, int columns, int rows, int cellWidth, int cellHeight)
    {
        drawTable(g, Color.GRAY, Color.WHITE, xy, columns, rows, cellWidth, cellHeight);
    }
    
    // Choice of colours
    public void drawTable(Graphics g, Color lineColour, Color backColour, Point xy, int columns, int rows, int cellWidth, int cellHeight)
    {
        this.tablePos = xy;
        this.columns = columns;
        this.rows = rows;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        
	Rectangle bounds = new Rectangle(xy.x,xy.y,(columns*cellWidth),(rows*cellHeight));
	
	g.setColor(backColour);
	g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
	
	g.setColor(lineColour);
	g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
	
	int x = cellWidth;
	int y = 0;
	for(int i = 0; i < columns; i++) // Columns
	{
            g.drawLine(x+bounds.x, y+bounds.y, x+bounds.x, bounds.height+bounds.y);
            x+=cellWidth;
	}

	y = cellHeight;
	for(int i = 0; i < rows; i++) // Rows
	{
            g.drawLine(bounds.x, y+bounds.y, bounds.width+bounds.x, y+bounds.y);
            y+=cellHeight;
	}
    }
}

