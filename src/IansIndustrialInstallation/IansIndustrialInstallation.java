package IansIndustrialInstallation;

// <editor-fold desc="Imports" defaultstate="collapsed">
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
// </editor-fold>

/**
 *
 * @author Daniel Field
 */
public class IansIndustrialInstallation extends JPanel implements ActionListener, MouseListener {

    // <editor-fold desc="variables">

    JButton btnNO2, btnSO2, btnCO, btnObstruct, btnExport, btnClose, btnImport;

    Rectangle btnSize = new Rectangle(150, 30);

    int maxColumns = 30, maxRows = 30,
            colCount = 0, rowCount = 0,
            cellWidth = 32, cellHeight = 16,
            acceptable = 1, concerning = 2, danger = 3;

    Table table;
    HashMap<Point, Integer> tbl = new HashMap<>();

    JLabel lblGasType,
            lblRecordedLvl,
            lblWarehouse,
            lblDate,
            lblTime,
            lblLegend;

    Point tablePosition = new Point(170, 100);

    // label font
    Font primaryFont = new Font("Arial", Font.BOLD, 12);

    String hazardType = "NO2";
    
    static JFrame frame;

    // </editor-fold>
    
    //<editor-fold desc="init">
    
    public static void main(String[] args) {

        frame = new JFrame();
        frame.add(new IansIndustrialInstallation());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(Strings.TITLE);
        frame.pack();
        frame.setVisible(true);
    }

    public IansIndustrialInstallation() {

        setLayout(null);
        setPreferredSize(new Dimension(870, 490));

        // Buttons
        btnNO2 = drawButton(IansIndustrialInstallation.this, "Nitrogen Dioxide", KeyEvent.VK_N, new Rectangle(10, 100, btnSize.width, btnSize.height));
        btnSO2 = drawButton(IansIndustrialInstallation.this, "Sulphur Dioxide", KeyEvent.VK_S, new Rectangle(10, 140, btnSize.width, btnSize.height));
        btnCO = drawButton(IansIndustrialInstallation.this, "Carbon Monoxide", KeyEvent.VK_M, new Rectangle(10, 180, btnSize.width, btnSize.height));
        btnObstruct = drawButton(IansIndustrialInstallation.this, "Obstructions", KeyEvent.VK_O, new Rectangle(10, 220, btnSize.width, btnSize.height));
        btnExport = drawButton(IansIndustrialInstallation.this, "Export", KeyEvent.VK_E, new Rectangle(10, 409, btnSize.width, btnSize.height));
        btnImport = drawButton(IansIndustrialInstallation.this, "Import", KeyEvent.VK_I, new Rectangle(10, 369, btnSize.width, btnSize.height));
        btnClose = drawButton(IansIndustrialInstallation.this, "Close", KeyEvent.VK_C, new Rectangle(10, 449, btnSize.width, btnSize.height));

        // labels
        drawLabel(Strings.TITLE, new Rectangle(10, 10, 235, 30), new Font("Arial", Font.BOLD, 30), new Color(97, 49, 33), Color.WHITE);
        lblGasType = drawLabel("Displaying NO2 levels (Nitrogen Dioxide)", new Rectangle(10, 50, 300, 20), primaryFont);
        lblWarehouse = drawLabel("", new Rectangle(395, 10, 300, 20), new Font("Arial", Font.BOLD, 20));
        lblDate = drawLabel("", new Rectangle(660, 10, 200, 20), primaryFont);
        lblTime = drawLabel("", new Rectangle(730, 10, 200, 20), primaryFont);
        lblLegend = drawLabel(Strings.LEGEND, new Rectangle(980, 65, 150, 200), primaryFont);

        lblRecordedLvl = drawLabel("Recorded level: ", new Rectangle(170, 480, 375, 30), primaryFont);
        lblRecordedLvl.setVisible(false); // set visibility to false so it only shows the label when a cell is clicked.

        try {
            loadData(Strings.NO2_CSV);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error. Please ensure that your csv file exists.");
        }

        addMouseListener(IansIndustrialInstallation.this);
    }

    //</editor-fold>
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint();

        table = new Table(g, Color.GRAY, Color.WHITE, tablePosition, colCount, rowCount - 3, 32, 16);
        for (int y = 0; y < rowCount - 3; y++) {
            for (int x = 0; x < colCount; x++) {
                Point p = new Point(x, y);
                table.drawReading(g, p, tbl.get(p), acceptable, concerning, danger);
            }
        }
        lblLegend.setBounds(tablePosition.x + (cellWidth * colCount) + 10, tablePosition.y / 2 - 10, lblLegend.getWidth(), lblLegend.getHeight());
        lblRecordedLvl.setBounds(tablePosition.x, tablePosition.y + (cellHeight * rowCount - 3) - 40, 500, 30);
    }

    // <editor-fold desc="label and button methods" defaultstate="collapsed">
    private JLabel drawLabel(String text, Rectangle rect, Font font) // No background colour
    {
        JLabel lbl = new JLabel();
        lbl.setBounds(rect.x, rect.y, rect.width, rect.height);
        lbl.setText(text);
        lbl.setFont(font);
        add(lbl);
        return lbl;
    }

    private JLabel drawLabel(String text, Rectangle rect, Font font, Color backColour, Color foreColour) // Custromizable background colour
    {
        JLabel lbl = new JLabel();
        lbl.setBounds(rect.x, rect.y, rect.width, rect.height);
        lbl.setText(text);
        lbl.setOpaque(true);
        lbl.setBackground(backColour);
        lbl.setForeground(foreColour);
        lbl.setFont(font);
        add(lbl);
        return lbl;
    }

    private JButton drawButton(ActionListener actionListener, String text, int MnemonicIndex, Rectangle bounds) {
        JButton btn = new JButton(text);
        btn.setBounds(bounds);
        btn.setMnemonic(MnemonicIndex);
        btn.addActionListener(actionListener);
        btn.setBackground(new Color(230, 230, 230));
        add(btn);
        return btn;
    }
    // </editor-fold>

    // <editor-fold desc="File reading">
    private Dimension getCsvDimensions(String file) throws FileNotFoundException, IOException {
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        Dimension dim = new Dimension(0, 0);

        while ((line = br.readLine()) != null) {
            String[] temp = line.split(",");
            dim.width = temp.length;
            dim.height++;
        }
        return dim;
    }

    private void loadData(String file) throws IOException {
        Dimension csvDim = getCsvDimensions(file);
        colCount = csvDim.width;
        rowCount = csvDim.height;
        String lblText = "";
        switch (file) {
            case Strings.NO2_CSV:
                lblText = "Displaying NO2 levels (Nitrogen Dioxide)";
                hazardType = "NO2";
                acceptable = 1;
                concerning = 10;
                danger = 30;
                break;
            case Strings.SO2_CSV:
                lblText = "Displaying SO2 levels (Sulphur Dioxide)";
                hazardType = "SO2";
                acceptable = 1;
                concerning = 10;
                danger = 30;
                break;
            case Strings.CO_CSV:
                lblText = "Displaying CO levels (Carbon Monoxide)";
                hazardType = "CO";
                acceptable = 1;
                concerning = 8;
                danger = 25;
                break;
            case Strings.OBSTRUCT_CSV:
                lblText = "Displaying obstruction levels";
                hazardType = "Obs";
                acceptable = 1;
                concerning = 2;
                danger = 3;
                break;
        }

        lblGasType.setText(lblText);

        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int y = -3; // line counter

            while ((line = br.readLine()) != null) {
                if (y == -3) {
                    lblWarehouse.setText(line);
                }
                if (y == -2) {
                    lblDate.setText(line);
                }
                if (y == -1) {
                    lblTime.setText(line);
                }

                if (y >= 0) {
                    String[] temp = line.split(",");
                    for (int x = 0; x < temp.length; x++) {
                        int reading = Integer.parseInt(temp[x]);

                        tbl.put(new Point(x, y), reading);
                    }
                }
                y++;
            }
            br.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, Strings.ERROR + e.toString());
        }
    }
    // </editor-fold>

    public static Color checkColour(int value, int acceptable, int concerning, int danger) {
        if (value >= danger) {
            return Color.RED;
        } else if (value >= concerning) {
            return Color.YELLOW;
        } else if (value >= acceptable) {
            return Color.GREEN;
        } else {
            return Color.WHITE;
        }
    }
    
    private void resizeFrame() {
        Dimension dim = new Dimension(tablePosition.x + (cellWidth * colCount) + 95, tablePosition.y + (cellHeight * rowCount - 3) + 40);
        if (dim.height > 490) {
            this.setPreferredSize(dim);
        } else {
            this.setPreferredSize(new Dimension(dim.width, 490));
        }
        ((JFrame) this.getRootPane().getParent()).pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnNO2) {
                loadData(Strings.NO2_CSV);
                resizeFrame();
            }

            if (e.getSource() == btnSO2) {
                loadData(Strings.SO2_CSV);
                resizeFrame();
            }

            if (e.getSource() == btnCO) {
                loadData(Strings.CO_CSV);
                resizeFrame();
            }

            if (e.getSource() == btnObstruct) {
                loadData(Strings.OBSTRUCT_CSV);
                resizeFrame();
            }

            if (e.getSource() == btnExport) {
                String fName = Strings.NO2;

                switch (hazardType) {
                    case "NO2":
                        fName = Strings.NO2;
                        break;
                    case "SO2":
                        fName = Strings.SO2;
                        break;
                    case "CO":
                        fName = Strings.CO;
                        break;
                    case "Obs":
                        fName = Strings.OBSTRUCT;
                        break;
                }

                JFrame window = new ExportDialog(tbl, acceptable, concerning, danger, colCount, rowCount, fName);
                window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                window.pack();
                window.setVisible(true);
            }

            if (e.getSource() == btnImport) {
                FileDialog fd = new FileDialog(frame, "Choose a file to import.", FileDialog.LOAD);
                fd.setFile("*.csv");
                fd.setDirectory("C:\\");
                fd.setVisible(true);

                String filename = fd.getFile();
                if (filename != null) {
                    try {
                        loadData(filename);
                    } catch (IOException ex) {
                    }
                    resizeFrame();
                }
            }
        } catch (IOException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, Strings.ERROR + ex);
        }

        if (e.getSource() == btnClose) {
            System.exit(0);
        }
    }

    // <editor-fold desc="MouseListener Abstract methods">
    @Override
    public void mouseClicked(MouseEvent e) {

        try {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();
            int x = (int) b.getX() - this.getX() - 8;
            int y = (int) b.getY() - this.getY() - 30;

            int[] cellData = table.getCellAndData(tbl, x, y);

            if (cellData[0] != -1) {
                lblRecordedLvl.setVisible(true);
                String selectedCell = String.format("<html>Selected cell: %1$d,%2$d<br>Recorded level: %3$d</html>", cellData[1], cellData[2], cellData[0]);
                lblRecordedLvl.setText(selectedCell);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    // </editor-fold>
}
