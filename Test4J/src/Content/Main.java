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
	        //中间值
	        File temp =null;
	        //外循环:我认为最小的数,从0~长度-1
	        for(int j = 0; j<s.length-1;j++){
	            //最小值:假设第一个数就是最小的
	            String min = s[j].getName();
	            //记录最小数的下标的
	            int minIndex=j;
	            //内循环:拿我认为的最小的数和后面的数一个个进行比较
	            for(int k=j+1;k<s.length;k++){
	                //找到最小值
	                if (Integer.parseInt(min.substring(0,min.indexOf(".")))>Integer.parseInt(s[k].getName().substring(0,s[k].getName().indexOf(".")))){
	                    //修改最小
	                    min=s[k].getName();
	                    minIndex=k;
	                }
	            }
	            //当退出内层循环就找到这次的最小值
	            //交换位置
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
		File excel = new File(xlspath+"\\天猫工商信息执照.xls");
	
		try {
			excel.createNewFile();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		if (file.isDirectory()) {
			

				WritableWorkbook workbook = Workbook.createWorkbook(excel);
		
			WritableSheet sheet = workbook.createSheet("sheet1", 0);

			Label one = new Label(0, 0, "  **企业名称**");
			
			sheet.addCell(one);
			Label two = new Label(4, 0, " **企业注册号**");
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
				// 取得图片读入流

				ImageInputStream iis = ImageIO.createImageInputStream(source);
				reader.setInput(iis, true);
				// 图片参数
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
				instance.setDatapath("./tessdata");// 设置tessdata位置
				instance.setLanguage("chi_sim");// （，不需要后缀名）
				CleanImage.bengin(outfile1, "D:\\tesstemp",prefix);
				String result = instance.doOCR(outfile);// 开始识别
				Client.lable.setText("正在识别"+name+"....");
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
				
				String a[] = line[0].toString().split("二");
				String b[] = line[1].toString().split("二");

				System.out.println(a[1]);
				Label label = new Label(4, p, a[1]);
				sheet.addCell(label);
				String re=b[1].replace("眼", "限");
				System.out.println(re);
				Label labe2 = new Label(0, p, re);
				sheet.addCell(labe2);
				p++;
				successnum++;
				System.out.println("*************************************************");}
				catch (Exception e) {
					WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
		                    10, WritableFont.NO_BOLD);
					WritableCellFormat wcf = new WritableCellFormat(font);
		            wcf.setBackground(Colour.RED);
					Label label = new Label(4, p,"("+f.getName()+")识别失败",wcf);
					sheet.addCell(label);		
				
					Label labe2 = new Label(0, p, "("+f.getName()+")识别失败",wcf);
		         
					sheet.addCell(labe2);
					p++;
					
					System.out.println("此图片识别失败");
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
		Client.lable.setText("图片识别完成！！");
		return successnum;
		
	}
	public  long  number(String path){
		File file = new File(path);
		long l = file.listFiles().length;
		return l;
	}


}
