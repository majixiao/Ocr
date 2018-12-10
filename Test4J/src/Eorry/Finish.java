package Eorry;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import JFram.Client;



public class Finish extends JFrame{
	
	Label la;
	Label la1;
	Label la2;
	
	private static final long serialVersionUID = 1L;
	private long num;
	public  long times;
	public long getTimes() {
		return times;
	}
	public void setTimes(long times) {
		this.times = times;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}

	public int getSuccessnum() {
		return successnum;
	}
	public void setSuccessnum(int successnum) {
		this.successnum = successnum;
	}
	private int successnum;

	public Finish(long num,int successnum,long times){
		this.num=num;
		this.successnum=successnum;
		this.times = times;
		setLocationRelativeTo(null);
		setLayout(null);	
		setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		set(num, successnum,times);
		
	}
	public void paint(Graphics g){
		
		setBounds(550,300,500, 300);	
		URL u = Client.class.getResource("1.jpg");
		try {
			BufferedImage  img = ImageIO.read(u);
			super.paint(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public void set(long num,int successnum,long times){
		la=new Label("* 共识别"+num+"张图片！");
		la.setBounds(110,80, 250, 30);
		la.setFont(new Font("",1,25));
		add(la);
		la1=new Label("* 成功识别"+successnum+"张图片！");
		la1.setBounds(110,110, 250, 30);	
		la1.setFont(new Font("",1,25));
		add(la1);
		la2=new Label("* 共用了"+times+"秒！");
		la2.setBounds(110,140, 250, 30);		
		la2.setFont(new Font("",1,25));//设置字体大小

		add(la2);
	}
	
}
