package chenxinyis;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.math.*;

public class Hist extends Frame implements ActionListener
{	
	int maxn, data[];
	int histogram[] = new int[256];
	int iw,ih,k;
	
	
	int graysum=0;
	int zhongzhi;
	double  biaozhuncha;
	
	public Hist(String str)
	{
		setTitle(str);//"图像直方图"
		
		Panel pdown = new Panel();
		

		//关闭窗口
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});	
		setSize(350, 350);
	 
		setVisible(true);
		
		
	}
	
	
		
	public void actionPerformed(ActionEvent e)
	{
		hide();
	}
	
  @SuppressWarnings("null")
public void getData(int [] data,int iw,int ih, BufferedImage img)
	{
		this.data = data;
		this.iw=iw;
		this.ih=ih;
		int pix;
    	int R,G,B;
		for(int i = 0; i < iw*ih; i++)
		{
			int grey = data[i]&0xff;
			histogram[grey]++;
		}
		
		//找出最大的数,进行标准化.
		maxn = histogram[0];
		for(int i = 1; i < 256; i++)
			if(maxn <= histogram[i])
				maxn = histogram[i];	
			
		for(int i = 0; i < 256; i++)		
			histogram[i] = histogram[i]*200/maxn;			
		
		
		//计算平均灰度,中值灰度,标准差,像素总数//陈欣怡
		 Color color;
		 int kk =0;
		 int gray[] =new int[ih-1];
		 int gray2[] = new int[iw-1];
		 int temp;
        for(int i=1;i<iw;i++){
        	for(int j=1;j<ih;j++){
                pix=img.getRGB(i,j);
                color = new Color(pix); 
                R=color.getRed();
                G=color.getGreen();
                B=color.getBlue();
                
                temp=(int)(0.3 * R + 0.59 * G + 0.11 * B);
               gray[j-1]=temp;
               this.graysum+= temp; 
        	}
        	QuickSort sort = new QuickSort();
        	sort.quickSort(gray, 0, gray.length - 1);
        	gray2[i-1] = gray[(gray.length)/2];
        }
        graysum/=(iw-1)*(ih-1);
        QuickSort sort2 = new QuickSort();
    	sort2.quickSort(gray2, 0, gray2.length - 1);
    	this.zhongzhi = gray2[(gray2.length)/2];
    	
    	for(int i=1;i<iw;i++)
        	for(int j=1;j<ih;j++){
                pix=img.getRGB(i,j);
                color = new Color(pix); 
                R=color.getRed();
                G=color.getGreen();
                B=color.getBlue();
                
                temp=(int)(0.3 * R + 0.59 * G + 0.11 * B);
               gray[j-1]=temp;
               this.biaozhuncha+= (temp-graysum)*(temp-graysum); 
        	}
    	this.biaozhuncha/=(iw-1)*(ih-1);
    	biaozhuncha = Math.sqrt(biaozhuncha);
	}
  
//  public void shuangfenggudi(int [] data,int iw,int ih, BufferedImage img)
//	{
//		this.data = data;
//		this.iw=iw;
//		this.ih=ih;
//		double paixu[];
//  	int R,G,B;
//		for(int i = 0; i < iw*ih; i++)
//		{
//			int grey = data[i]&0xff;
//			histogram[grey]++;
//			paixu[grey]= histogram[grey]*0.01+grey/1000.00;
//		}
//		
//		//找出最大的数,进行标准化.
//		maxn = histogram[0];
//		for(int i = 1; i < 256; i++)
//			if(maxn <= histogram[i])
//				maxn = histogram[i];	
//			
//		for(int i = 0; i < 256; i++)		
//			histogram[i] = histogram[i]*200/maxn;			
//		
//    //陈欣怡
//      QuickSort sort2 = new QuickSort();
//  	sort2.quickSort(paixu, 0, paixu.length - 1);
//  	int no1top = paixu[paixu.length-1];
//  	int no2top = paixu[paixu.length-2];
//  	
//  	
//  	
//	}
	
	public void paint(Graphics g)
	{		
		//画出水平和垂直的轴
		g.drawLine(30, 250, 286, 250);
		g.drawLine(30, 50,  30, 250);
		
		g.drawLine(30, 50, 32, 50);
		g.drawLine(30, 150, 32, 150);
		
		g.drawString("0",   28,  263);
		g.drawString("50",  65, 263);
		g.drawString("100", 123, 263);
		g.drawString("150", 173, 263);
		g.drawString("200", 223, 263);
		g.drawString("250", 273, 263);  
		g.drawString("平均灰度："+graysum,28,280);
		g.drawString("中值灰度："+zhongzhi,28,295);
		g.drawString("标准差："+biaozhuncha,28,310);
		g.drawString("像素总数："+iw*ih,28,325);
		
		g.drawString(""+(maxn/2), 5, 155);
		g.drawString(""+maxn,   5, 55);
		
		//直方图
		for(int i = 0; i < 256; i++)
			g.drawLine(30+i, 250, 30+i, 250-histogram[i]);					
	}
	
	

}
