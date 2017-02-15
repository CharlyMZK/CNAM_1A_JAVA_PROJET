import javax.swing.*;
import java.awt.*;

/**
 * Created by MZK on 03/02/2017.
 */
public class JImagePanel extends JPanel{

    private JPanel panel1;
    private JLabel labeIImg;
    private ImageIcon icon;
    public JImagePanel(){
        labeIImg = new JLabel();
        labeIImg.setSize(50,50);
        this.add(labeIImg);
    }

    public void setImage(String image){
        if(image != null){
            icon = new ImageIcon(image);
            labeIImg.setIcon(icon);
            labeIImg.setText("");
        }else{
            labeIImg.setText("Aucune image");
            labeIImg.setIcon(null);
        }

    }
}
