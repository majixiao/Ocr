package Content;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import JFram.Client;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Main {
	public static File tempdir;
	static {
		 tempdir=new File("D:\\tesstemp");
		 tempdir.mkdir();	
	 }

	public static File[] sort(File[] s){
	        //�м�ֵ
	        File temp =null;
	        //��ѭ��:����Ϊ��С����,��0~����-1
	        for(int j = 0; j<s.length-1;j++){
	            //��Сֵ:�����һ����������С��
	            String min = s[j].getName();
	            //��¼��С�����±��
	            int minIndex=j;
	            //��ѭ��:������Ϊ����С�����ͺ������һ�������бȽ�
	            for(int k=j+1;k<s.length;k++){
	                //�ҵ���Сֵ
	                if (Integer.parseInt(min.substring(0,min.indexOf(".")))>Integer.parseInt(s[k].getName().substring(0,s[k].getName().indexOf(".")))){
	                    //�޸���С
	                    min=s[k].getName();
	                    minIndex=k;
	                }
	            }
	            //���˳��ڲ�ѭ�����ҵ���ε���Сֵ
	            //����λ��
	            temp = s[j];
	            s[j]=s[minIndex];
	            s[minIndex]=temp;
	        }
	        return s;
}

	public  int Shibie(String path,String xlspath) throws IOException, RowsExceededException, WriteException   {
		int successnum=0;
		int p = 1;
		File file = new File(path);
		File excel = new File(xlspath+"\\��è������Ϣִ��.xls");
	
		try {
			excel.createNewFile();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		if (file.isDirectory()) {
			

				WritableWorkbook workbook = Workbook.createWorkbook(excel);
		
			WritableSheet sheet = workbook.createSheet("sheet1", 0);

			Label one = new Label(0, 0, "  **��ҵ����**");
			
			sheet.addCell(one);
			Label two = new Label(4, 0, " **��ҵע���**");
			sheet.addCell(two);
			File[] fs=sort(file.listFiles());
			for (File f : fs) {
				try{
                String	name  = f.getName();
				System.out.println(name);
				String file2 = path+"\\" + name;
				String outfile1 = "D:\\tesstemp\\" + name;
				String prefix=name.split("\\.")[1];
				System.out.println(outfile1);

				File outfile = new File(outfile1);
				File file1 = new File(file2);

				InputStream source = new FileInputStream(file1);
				Iterator readers = ImageIO.getImageReadersByFormatName(prefix);
				ImageReader reader = (ImageReader) readers.next();
				// ȡ��ͼƬ������

				ImageInputStream iis = ImageIO.createImageInputStream(source);
				reader.setInput(iis, true);
				// ͼƬ����
				ImageReadParam param = reader.getDefaultReadParam();
				Rectangle rect = new Rectangle(0, 0, 1000, 75);
				param.setSourceRegion(rect);
				BufferedImage image = reader.read(0, param);
				Graphics2D g = image.createGraphics();
				g.drawImage(image,0, 0, 1000, 75, Color.WHITE, null);
				g.dispose();
				ImageIO.write(image, prefix, outfile);
				
                iis.close();

				ITesseract instance = new Tesseract(); // JNA Interface Mapping
				instance.setDatapath("./tessdata");// ����tessdataλ��
				instance.setLanguage("chi_sim");// ��������Ҫ��׺����
				CleanImage.bengin(outfile1, "D:\\tesstemp",prefix);
				String result = instance.doOCR(outfile);// ��ʼʶ��
				Client.lable.setText("����ʶ��"+name+"....");
				outfile.delete();
				String[] line = new String[10];
				int num = 0;
				String txt;
				BufferedReader read = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(result.getBytes())));
				try {
					while ((txt = read.readLine()) != null) {					
						line[num] = txt;
						num++;
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
				
				String a[] = line[0].toString().split("��");
				String b[] = line[1].toString().split("��");

				System.out.println(a[1]);
				Label label = new Label(4, p, a[1]);
				sheet.addCell(label);
				String re=b[1].replace("��", "��");
				System.out.println(re);
				Label labe2 = new Label(0, p, re);
				sheet.addCell(labe2);
				p++;
				successnum++;
				System.out.println("*************************************************");}
				catch (Exception e) {
					WritableFont font = new WritableFont(WritableFont.createFont("����"),
		                    10, WritableFont.NO_BOLD);
					WritableCellFormat wcf = new WritableCellFormat(font);
		            wcf.setBackground(Colour.RED);
					Label label = new Label(4, p,"("+f.getName()+")ʶ��ʧ��",wcf);
					sheet.addCell(label);		
				
					Label labe2 = new Label(0, p, "("+f.getName()+")ʶ��ʧ��",wcf);
		         
					sheet.addCell(labe2);
					p++;
					
					System.out.println("��ͼƬʶ��ʧ��");
					continue;
				}
				
			}
			try {
				
				workbook.write();
				workbook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
			Main.tempdir.delete();
			
		}
		Client.lable.setText("ͼƬʶ����ɣ���");
		return successnum;
		
	}
	public  long  number(String path){
		File file = new File(path);
		long l = file.listFiles().length;
		return l;
	}


}
