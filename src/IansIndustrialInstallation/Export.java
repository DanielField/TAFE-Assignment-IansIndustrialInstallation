package IansIndustrialInstallation;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Daniel Field
 */
public class Export {

    public static void toDAT(String file, int[][] values, int acceptable, int concerning, int danger) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        for (int y = 0; y < values[0].length; y++) {
            for (int x = 0; x < values.length; x++) {
                
                
                
                if (values[x][y] >= danger) // dangerous
                {
                    out.append(x + "," + y + "," + "R");
                } else if (values[x][y] >= concerning) // concerning
                {
                    out.append(x + "," + y + "," + "Y");
                } else if (values[x][y] >= acceptable) // acceptable
                {
                    out.append(x + "," + y + "," + "G");
                } else // zero reading
                {
                    out.append(x + "," + y + "," + "W");
                }
                out.newLine();
            }
        }
        out.close();
    }

    public static void toRAF(String file, int[][] values, int acceptable, int concerning, int danger) throws IOException {
        int length = 30,
            spaceCounter = 0,
            readingCounter = 0;

        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        for (int y = 0; y < values[0].length; y++) {
            for (int x = 0; x < values.length; x++) {
                
                Color colour = IansIndustrialInstallation.checkColour(values[x][y],acceptable, concerning, danger);
                
                if (colour == Color.RED) {
                    out.append("R");
                    readingCounter++;
                } else if (colour == Color.YELLOW) {
                    out.append("Y");
                    readingCounter++;
                } else if (colour == Color.GREEN) {
                    out.append("G");
                    readingCounter++;
                } else {
                    out.append("W");
                    readingCounter++;
                }

                if (spaceCounter < length && x == values.length - 1) {
                    for (int i = spaceCounter + readingCounter; i < length; i++) {
                        out.append(" ");
                    }

                    spaceCounter++;
                    readingCounter = 0;
                } else {
                    spaceCounter = 0;

                }
            }
        }
        out.close();
    }

    // Disgusting code here...
    public static void toRPT(String file, int[][] values, int acceptable, int concerning, int danger) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        int counter = 0;
        
        int previousValue = 0;
        int currentValue = 0;

        String line = "";
        
        // Allows me to format the string with a letter representing a colour, followed by the count
        String colourAndCount = "%1$s,%2$d";
        
        String white = "W", green = "G", yellow = "Y", red = "R";

        for (int y = 0; y < values[0].length; y++) {
            for (int x = 0; x < values.length; x++) {
                previousValue = currentValue;
                currentValue = values[x][y];

                Color previousColour = IansIndustrialInstallation.checkColour(previousValue, acceptable, concerning, danger);
                Color currentColour = IansIndustrialInstallation.checkColour(currentValue, acceptable, concerning, danger);

                if (currentColour == Color.RED) {
                    if (previousColour == Color.RED) {
                        counter++;
                    } else if (previousColour == Color.YELLOW){
                        line += String.format(colourAndCount, yellow, counter) + ",";
                        counter = 1;
                    } else if (previousColour == Color.GREEN){
                        line += String.format(colourAndCount, green, counter) + ",";
                        counter = 1;
                    } else {
                        line += String.format(colourAndCount, white, counter) + ",";
                        counter = 1;
                    }
                } else if (currentColour == Color.YELLOW) {
                    if (previousColour == Color.RED) {
                        line += String.format(colourAndCount, red, counter) + ",";
                        counter = 1;
                    } else if (previousColour == Color.YELLOW){
                        counter++;
                    } else if (previousColour == Color.GREEN){
                        line += String.format(colourAndCount, green, counter) + ",";
                        counter = 1;
                    } else {
                        line += "W," + counter + ",";
                        counter = 1;
                    }
                } else if (currentColour == Color.GREEN) {
                    if (previousColour == Color.RED) {
                        line += String.format(colourAndCount, red, counter) + ",";
                        counter = 1;
                    } else if (previousColour == Color.YELLOW){
                        line += String.format(colourAndCount, yellow, counter) + ",";
                        counter = 1;
                    } else if (previousColour == Color.GREEN){
                        counter++;
                    } else {
                        line += String.format(colourAndCount, white, counter) + ",";
                        counter = 1;
                    }
                } else {
                    if (previousColour == Color.RED) {
                        line += String.format(colourAndCount, red, counter) + ",";
                        counter = 1;
                    } else if (previousColour == Color.YELLOW){
                        line += String.format(colourAndCount, yellow, counter) + ",";
                        counter = 1;
                    } else if (previousColour == Color.GREEN){
                        line += String.format(colourAndCount, green, counter) + ",";
                        counter = 1;
                    } else {
                        counter++;
                    }
                }
                
                if(x == values.length-1) {
                    if (currentColour == Color.RED)
                    {
                        line += String.format(colourAndCount, red, counter);
                    } else if (currentColour == Color.YELLOW)
                    {
                        line += String.format(colourAndCount, yellow, counter);
                    } else if (currentColour == Color.GREEN)
                    {
                        line += String.format(colourAndCount, green, counter);
                    } else
                    {
                        line += String.format(colourAndCount, white, counter);
                    }
                }
            }
            out.append(line);
            out.newLine();
            line = "";
            counter = 0;
        }
        out.close();
    }

    public static void toCSV(String file, int[][] values, int acceptable, int concerning, int danger) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        for (int y = 0; y < values[0].length; y++) {
            for (int x = 0; x < values.length; x++) {
                if (x == values.length - 1) {
                    out.append(values[x][y] + "");
                } else {
                    out.append(values[x][y] + ",");
                }
            }
            out.newLine();
        }
        out.close();
    }
}
