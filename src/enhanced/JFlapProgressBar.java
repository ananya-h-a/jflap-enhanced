package enhanced;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.beans.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
public final class JFlapProgressBar extends JPanel {

    private static final JProgressBar progressBar = new JProgressBar() 
    {
        @Override public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        }
    };

    public JFlapProgressBar() 
    {
        super(new BorderLayout());
        progressBar.setStringPainted(true);
        progressBar.setFont(progressBar.getFont().deriveFont(24f));

        JPanel p = new JPanel(new GridLayout(1, 1));
        p.add(progressBar);
        add(p);
        setPreferredSize(new Dimension(320, 240));
    }

    //Call this static method to setup the bar 
    public static void dispProgressBar(JPanel p)
    {
        createAndShowGUI(p);
        setProgress(0);
    }
    
    //Call this static method to change progress
    public static void setProgress(int v)
    {
    	//Progress shown in red
    	if(v < 35){
        	progressBar.setForeground(new Color(0XFF1919));
        }
    	//Progress shown in blue
        else if(v < 70){
        	progressBar.setForeground(new Color(0X3399FF));
        }
    	//Progress shown in green
        else{
        	progressBar.setForeground(new Color(0X99FF66));
        }
    	progressBar.setValue(v);
    }

    public static void createAndShowGUI(JPanel panel)
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        panel.add(new JFlapProgressBar());
        //panel.setVisible(true);
    }
}

class ProgressCircleUI extends BasicProgressBarUI {
    @Override public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        return d;    }

    @Override public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets();
        int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double degree = 360 * progressBar.getPercentComplete();
        double sz = Math.min(barRectWidth, barRectHeight);
        double cx = b.left + barRectWidth  * .5;
        double cy = b.top  + barRectHeight * .5;
        double or = sz * .5;
        double ir = or * .5; //.8;
        Shape inner  = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
        Shape outer  = new Ellipse2D.Double(cx - or, cy - or, sz, sz);
        Shape sector = new Arc2D.Double(cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);
        Area hole = new Area(inner);

        foreground.subtract(hole);
        background.subtract(hole);

        g2.setPaint(new Color(0xDDDDDD));
        g2.fill(background);

        g2.setPaint(progressBar.getForeground());
        g2.fill(foreground);
        g2.dispose();

        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
        }
    }
    public void changeProgress(int a){
    	progressBar.setValue(10);
    }
}


class ProgressListener implements PropertyChangeListener {
    private final JProgressBar progressBar;
    ProgressListener(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setValue(0);
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        String strPropertyName = evt.getPropertyName();
        if ("progress".equals(strPropertyName)) {
            progressBar.setIndeterminate(false);
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            if(progress < 35){
            	progressBar.setForeground(new Color(0XFF1919));
            }
            else if(progress < 70){
            	progressBar.setForeground(new Color(0X3399FF));
            }
            else{
            	progressBar.setForeground(new Color(0X99FF66));
            }
        }
    }
}
