package Content;
import java.awt.Color;  
import java.awt.color.ColorSpace;  
import java.awt.image.BufferedImage;  
import java.awt.image.ColorConvertOp;  
import java.io.File;    
import java.io.IOException;    
import javax.imageio.ImageIO;    
  
public class CleanImage {  
    /** 
     *  
     * @param sfile 
     *            ��Ҫȥ���ͼ�� 
     * @param destDir 
     *            ȥ����ͼ�񱣴��ַ 
     * @throws IOException  
     * 
     */  
    public static void cleanImage(File sfile, String destDir,String type)throws IOException{  
        File destF = new File(destDir);  
        if (!destF.exists()){  
            destF.mkdirs();  
        }  
  
        BufferedImage bufferedImage = ImageIO.read(sfile);  
        int h = bufferedImage.getHeight();  
        int w = bufferedImage.getWidth();  
  
        // �ҶȻ�  
        int[][] gray = new int[w][h];  
        for (int x = 0; x < w; x++){  
            for (int y = 0; y < h; y++){  
                int argb = bufferedImage.getRGB(x, y);  
                // ͼ���������������ʶ���ʷǳ��ߣ�  
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);  
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);  
                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);  
                if (r >= 255){  
                    r = 255;  
                }  
                if (g >= 255){  
                    g = 255;  
                }  
                if (b >= 255){  
                    b = 255;  
                }  
                gray[x][y] = (int) Math.pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)* 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);  
            }  
        }  
  
        // ��ֵ��  
        int threshold = ostu(gray, w, h);  
        BufferedImage binaryBufferedImage = new BufferedImage(w, h,BufferedImage.TYPE_BYTE_BINARY);  
        for (int x = 0; x < w; x++){  
            for (int y = 0; y < h; y++){  
                if (gray[x][y] > threshold){  
                    gray[x][y] |= 0x00FFFF;  
                } else{  
                    gray[x][y] &= 0xFF0000;  
                }  
                binaryBufferedImage.setRGB(x, y, gray[x][y]);  
            }  
        }  
  
        // �����ӡ  
        for (int y = 0; y < h; y++){  
            for (int x = 0; x < w; x++){  
                if (isBlack(binaryBufferedImage.getRGB(x, y))){  
                    System.out.print("*");  
                } else{  
                    System.out.print(" ");  
                }  
            }  
            System.out.println();  
        }  
        ImageIO.write(binaryBufferedImage, type, new File(destDir, sfile.getName()));  
    }  
  
    public static boolean isBlack(int colorInt){  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300){  
            return true;  
        }  
        return false;  
    }  
  
    public static boolean isWhite(int colorInt){  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 300){  
            return true;  
        }  
        return false;  
    }  
  
    public static int isBlackOrWhite(int colorInt){  
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730){  
            return 1;  
        }  
        return 0;  
    }  
  
    public static int getColorBright(int colorInt){  
        Color color = new Color(colorInt);  
        return color.getRed() + color.getGreen() + color.getBlue();  
    }  
  
    public static int ostu(int[][] gray, int w, int h){  
        int[] histData = new int[w * h];  
        // Calculate histogram  
        for (int x = 0; x < w; x++){  
            for (int y = 0; y < h; y++){  
                int red = 0xFF & gray[x][y];  
                histData[red]++;  
            }  
        }  
  
        // Total number of pixels  
        int total = w * h;  
  
        float sum = 0;  
        for (int t = 0; t < 256; t++)  
            sum += t * histData[t];  
  
        float sumB = 0;  
        int wB = 0;  
        int wF = 0;  
  
        float varMax = 0;  
        int threshold = 0;  
  
        for (int t = 0; t < 256; t++){  
            wB += histData[t]; // Weight Background  
            if (wB == 0)  
                continue;  
  
            wF = total - wB; // Weight Foreground  
            if (wF == 0)  
                break;  
  
            sumB += (float) (t * histData[t]);  
  
            float mB = sumB / wB; // Mean Background  
            float mF = (sum - sumB) / wF; // Mean Foreground  
  
            // Calculate Between Class Variance  
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);  
  
            // Check if new maximum found  
            if (varBetween > varMax){  
                varMax = varBetween;  
                threshold = t;  
            }  
        }  
  
        return threshold;  
    }  
    //ͼƬ�Ҷȣ��ڰ�  

    public static void bengin(String name,String dir,String type) throws IOException{  
        /*File testDataDir = new File("testdata"); 
        final String destDir = testDataDir.getAbsolutePath()+"/tmp"; 
        for (File file : testDataDir.listFiles()){ 
            cleanImage(file, destDir); 
        }*/  
       // File testDataDir = new File("C:\\Users\\lin\\Desktop\\run\\19.png");//ȥ��  
    	File testDataDir=new File(name);
    	//String destDir ="C:\\Users\\lin\\Desktop";
    	String destDir=dir;
        cleanImage(testDataDir, destDir,type);  
        //gray("D:\\tmp\\3VG5B.jpg","D:\\tmp\\3VG5B1.jpg");//�ҶȻ�  
    }  
} 