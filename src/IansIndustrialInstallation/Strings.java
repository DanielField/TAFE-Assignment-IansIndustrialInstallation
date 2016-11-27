package IansIndustrialInstallation;

/**
 *
 * @author Daniel Field
 */
public class Strings 
{
    /*
     * This class is used mainly to get the string for the legend out of the way.
     * Just thought it would be a bit of fun using some HTML to allow me to 
     * use one label instead of using 4 different labels. Though I could have also 
     * used an image to display the legend, which would have probably been the easiest option.
     */
    
    public static final String NO2 = "Ians_W7_NO2";
    public static final String SO2 = "Ians_W7_SO2";
    public static final String CO = "Ians_W7_CO";
    public static final String OBSTRUCT = "Ians_W7_Obstruct";
    
    public static final String NO2_CSV = "Ians_W7_NO2.csv";
    public static final String SO2_CSV = "Ians_W7_SO2.csv";
    public static final String CO_CSV = "Ians_W7_CO.csv";
    public static final String OBSTRUCT_CSV = "Ians_W7_Obstruct.csv"; 

    public static final String TITLE = "Hazard readings";
    
    public static final String LEGEND = 
        "<html>"
            + "Legend:<br>"
            + "<div style=\"border: 1px solid #DDDDDD; background-color:#00FF00; font: 11px Arial;\">"
                + "Acceptable"
            + "</div>"
            + "<div style=\"border: 1px solid #DDDDDD; background-color:#FFFF00; font: 11px Arial;\">"
                + "Concerning"
            + "</div>"
            + "<div style=\"border: 1px solid #DDDDDD; background-color:#FF0000; font: 11px Arial;\">"
                + "Dangerous"
            + "</div>"
      + "</html>";
    
    public static final String ERROR = "Oh no! The following error has occured:\r\n";
}
