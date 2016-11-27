package IansIndustrialInstallation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Daniel Field
 */
public class ExportDialog extends JFrame implements ActionListener{
    
    JLabel lblDir = new JLabel("Export path: ");
    JLabel lblExport = new JLabel("Export the data to: ");
    
    JTextField txtDir = new JTextField(18);
    
    JButton btnExport = new JButton("Export");
    
    JCheckBox chkDAT = new JCheckBox(".DAT"),
              chkRAF = new JCheckBox(".RAF"),
              chkRPT = new JCheckBox(".RPT"),
              chkCSV = new JCheckBox(".CSV");

    String strFile;
    String fileName;
    
    int[][] vals;
    
    int acceptable, concerning, danger;
    
    public ExportDialog(HashMap<Point, Integer> hm, int acceptable, int concerning, int danger, int columnCount, int rowCount, String fileName)
    {       
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(400,90));
        this.setLocation((int)(getRootPane().getLocation().getX()+10),(int)(getRootPane().getLocation().getY()+30));
        this.setResizable(false);
        
        btnExport.addActionListener(ExportDialog.this);
        
        this.fileName = fileName;
        this.acceptable = acceptable;
        this.concerning = concerning;
        this.danger = danger;
        
        txtDir.setText("C:\\");
        
        vals = new int[columnCount][rowCount-3];

        for(int y = 0; y < rowCount-3; y++)
        {
            for(int x = 0; x < columnCount; x++)
            {
                vals[x][y] = hm.get(new Point(x,y));
            }
        }
        
        chkDAT.setSelected(true);
        chkCSV.setSelected(true);
        chkRAF.setSelected(true);
        chkRPT.setSelected(true);
        
        add(lblDir);        
        add(txtDir);
        add(btnExport);
        add(lblExport);
        add(chkDAT);
        add(chkRPT);
        add(chkRAF);
        add(chkCSV);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        if(e.getSource() == btnExport)
        {
            try {
                strFile = txtDir.getText();
                if(!strFile.endsWith("\\"))
                {
                    strFile += "\\";
                }
                strFile += fileName;
                if(chkDAT.isSelected())
                    Export.toDAT(strFile+".dat", vals, acceptable, concerning, danger);
                if(chkCSV.isSelected())
                    Export.toCSV(strFile+".csv", vals, acceptable, concerning, danger);
                if(chkRPT.isSelected())
                    Export.toRPT(strFile+".rpt", vals, acceptable, concerning, danger);
                if(chkRAF.isSelected())
                    Export.toRAF(strFile+".raf", vals, acceptable, concerning, danger);
                
                JOptionPane.showMessageDialog(null,"Exported successfully.");           
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"There was an error. Make sure the directory exists and that this programme has permission to write to the directory.");
            }
        }
    }
}