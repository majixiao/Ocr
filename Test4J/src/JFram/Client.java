package JFram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Content.Main;
import Eorry.Finish;

public class Client extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static Label lable=new Label();
	public String path;
	public String xlspath;
	JFileChooser fc = new JFileChooser();
	JFileChooser fc2 = new JFileChooser();
	JTextField text, text2;
	JButton button, button1, button2;
	BufferedImage img = null;
	ActionListener listener;

	public Client() {
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
	}

	void init() {
		ImageIcon icon = new ImageIcon("a.jpg");
		text = new JTextField();
		text.setBounds(240, 210, 270, 60);  
		add(text);
		text2 = new JTextField();
		text2.setBounds(240, 290, 270, 60);
		add(text2);
		button = new JButton("选择图片文件夹");
		button.setBounds(560, 210, 270, 60);
		//button.setBorder(null
		/*button.setContentAreaFilled(false);  
		button.setOpaque(false);
		button.setBorder(null);*/
		button.setIcon(icon);
	
		
		add(button);
		button2 = new JButton("选择结果的保存位置");
		button2.setBounds(560, 290, 270, 60);
		add(button2);
		button.addActionListener(this);
		button1 = new JButton("开始识别");
		button1.setBounds(365, 361, 313, 65);
		add(button1);
		button1.addActionListener(this);
		button2.addActionListener(this);
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == button) {
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int intRetVal = fc.showOpenDialog(this);
			if (intRetVal == JFileChooser.APPROVE_OPTION/* 获取选中的文件对象 */) {
				path = fc.getSelectedFile().getPath();
				text.setText(path);

			}
		}
		if (e.getSource() == button2) {
			fc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int intRetVal = fc2.showOpenDialog(this);
			if (intRetVal == JFileChooser.APPROVE_OPTION/* 获取选中的文件对象 */) {
				xlspath = fc2.getSelectedFile().getPath();
				text2.setText(xlspath);

			}
		}
		if (e.getSource() == button1) {
			try {
			
				lable.setBounds(470, 500, 100, 30);
				lable.setText("正在识别中...");
				add(lable);
				
				long num;
				Main a = new Main();
			    long begin=	new Date().getTime();
				int successnum = a.Shibie(path, xlspath);
				long over = new Date().getTime();
				num = a.number(path);
				long times = (over-begin)/1000;
				Finish f = new Finish(num, successnum,times);
				f.setBounds(400, 200, 650, 400);
				
				f.setTitle("结果");

			} catch (Exception e1) {

				e1.printStackTrace();
			}
		}
	}

	public void paint(Graphics g) {
		try {
			URL u = Client.class.getResource("1.jpg");
			img = ImageIO.read(u);
			super.paint(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			text.repaint();
			button1.repaint();
			button.repaint();
			button2.repaint();
			text2.repaint();
		
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
