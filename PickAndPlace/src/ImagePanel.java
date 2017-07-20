import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public ImagePanel(String imagePath) {
		ImageIcon image = new ImageIcon(imagePath);
		JLabel label = new JLabel("", image, JLabel.CENTER);
		this.setLayout(new BorderLayout());
		this.add( label, BorderLayout.CENTER );
	}
}
