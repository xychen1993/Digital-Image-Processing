package chenxinyis;
import java.io.*;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import chenxinyis.HuffmanShow;

import com.sun.image.codec.jpeg.*;

public class Common extends Frame
{
	//关于图像文件和像素--------------------------    
   public int[] grabber(Image im, int iw, int ih)
	{
		int [] pix = new int[iw * ih];
		try
		{
		    PixelGrabber pg = new PixelGrabber(im, 0, 0, iw,  ih, pix, 0, iw);
		    pg.grabPixels();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}	
		return pix;
	}
   public byte BitsNeeded(int n) 
   {
	    byte ret = 1;
	
	    if (n-- == 0)
	        return 0;
	
	    while ((n >>= 1) != 0)
	        ++ret;
		
	    return ret;
   }
   
   //陈欣怡
public int[] readasfile(File file) throws IOException{
		
	   InputStream in = null;
	   int[] colornumber = new int[1000];
	   in = new FileInputStream(file);
       int tempbyte = 0;
      
       for(int i=0;i<29;i++)
           tempbyte = in.read();
       	  colornumber[0]= tempbyte ;
         
           for(int j=0;j<25;j++)
          tempbyte = in.read();
        
           
           int x=1;
           
           for(int i=0;i<colornumber[0];i++)
   		    x*=2;
           
           int k = 1;
          for(int j=0;j<x;j++)
         {  
        	   tempbyte = in.read();
        	   colornumber[k++]=tempbyte;
           	   tempbyte = in.read();
           	   colornumber[k++]=tempbyte;
           	   tempbyte = in.read();
           	   colornumber[k++]=tempbyte;
           	   tempbyte = in.read();
           	 
           }
           		 
       
       return colornumber;
	
}
	//直方图均衡化
public	Image junhenghua(BufferedImage image , int iw , int ih)
	{
		
		int[] pixels=new int[iw*ih];
		
			//创建一个大小为256的数组，存放对应256个值对应的像素比例
			float[] gray_proportion = new float[256];
			int[] new_gray = new int[256];
		
				
			image.getRGB(0, 0, iw, ih, pixels, 0, iw);
			
			
			//统计对应像素值的像素点个数,并暂存在gray_proportion数组中
			for(int i=0; i<ih; i++)
			{
				for(int j=0; j<iw; j++)
				{
					int temp= (pixels[i*iw+j]>>16)&0xff;
					gray_proportion[temp]= gray_proportion[temp]+1;
				}
			}
			
			//计算每个像素值对应像素点个数占全图的比例
			for(int i=0; i<256; i++)
			{
				gray_proportion[i]= gray_proportion[i]/(ih*iw);
			}
			
			//i从1开始，令gray_proportion[i]=gray_proportion[i]+gray_proportion[i-1]
			for(int i=1; i<256;i++)
			{
				gray_proportion[i]= gray_proportion[i]+gray_proportion[i-1];
			}
			
			//用new_gray记录新的灰度索引值
			for(int i=0; i<256; i++)
			{
				new_gray[i]=(int) (gray_proportion[i]*255);
			}
			
			//将新的灰度索引值赋予pixels
			for(int i=0; i<ih; i++)
			{
				for(int j=0; j<iw; j++)
				{
					int alpha = (pixels[i*iw+j]>>24)&0xff;
					int red= (pixels[i*iw+j]>>16)&0xff;
					red= new_gray[red];
					
					pixels[i*iw+j]= alpha<<24|red<<16|red<<8|red;
				}
			}			
		
		MemoryImageSource source = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image image2 = Toolkit.getDefaultToolkit().createImage(source);
		
		 return image2;
	}
	
public int[] linetrans(int[] pix, int iw, int ih, double p, double q)
{
	ColorModel cm = ColorModel.getRGBdefault();
	int r, g, b;
	
	for(int i = 0; i < iw*ih; i++) 
	{
		r = cm.getRed(pix[i]);
		g = cm.getGreen(pix[i]);
		b = cm.getBlue(pix[i]);
		
		//增加图像亮度
		r  = (int)(p * r + q);
		g  = (int)(p * g + q);
		b  = (int)(p * b + q);
		
		if(r >= 255)   r = 255;
		if(g >= 255)   g = 255;
		if(b >= 255)   b = 255;
		
		pix[i] = 255 << 24|r << 16|g << 8|b;
	}
	return pix;
}

	//将Image转为BufferedImage
	public static BufferedImage toBufferedImage(Image image) {
	if (image instanceof BufferedImage) {
	    return (BufferedImage)image;
	 }
	 image = new ImageIcon(image).getImage();


	 BufferedImage bimage = null;
	 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	try {
	  
	    int transparency = Transparency.OPAQUE;

	     GraphicsDevice gs = ge.getDefaultScreenDevice();
	     GraphicsConfiguration gc = gs.getDefaultConfiguration();
	     bimage = gc.createCompatibleImage(
		 image.getWidth(null), image.getHeight(null), transparency);
	 } catch (HeadlessException e) {

	 }

	if (bimage == null) {

	    int type = BufferedImage.TYPE_INT_RGB;

	     bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	 }


	 Graphics g = bimage.createGraphics();


	 g.drawImage(image, 0, 0, null);
	 g.dispose();

	return bimage;
}
	
   public byte[][] readcolornum(String name, int iw, int ih)
   {    	
   	byte[][] pix = new byte[iw][ih];
   	
   	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //磁盘文件读入数据
           for(int j = 0; j < ih; j++)
           {    
               for(int i = 0; i < iw; i++)
               {
                   byte c = in.readByte();
                   pix[i][j] = c;
               }
           }    	  	   
	  	}
	  	catch(IOException e1){}
	  	
	  	return pix; 
   }
	//设置默认目录,过滤文件
	public void chooseFile(JFileChooser jfc, String str, int n)
   {
     	final int type = n;    
       //设置默认文件目录
       jfc.setCurrentDirectory(new File(str));
  
   }
   
   //打开图像
	public Image openImage(String is, MediaTracker tracker )
	{
		Image im = null;
				
		//用MediaTracker跟踪图像加载
		im = Toolkit.getDefaultToolkit().getImage(is);
		tracker.addImage(im,0);
	
		//等待图像完成加载
		try	{ tracker.waitForID(0);	}
		catch(InterruptedException e){ e.printStackTrace();}
		return im;
	}
   
   public int[] toPixels(int[][] pix, int iw, int ih)
   {
   	int[] pi = new int[iw*ih];
   	    	 
   	for(int j = 0; j < ih; j++)
   	{
   		for(int i = 0; i < iw; i++)
   		{
   			int c = pix[i][j];
   			if(c < 0) c = c + 256;
   			pi[i+j*iw] = (255<<24)|(c<<16)|(c<<8)|c;                			
   		}            
       } 
       return pi;
   }
   
   
   public int[] toPixels(double[][] inIm, int iw, int ih)
   {
   	int r;
       int[] pix = new int[iw*ih];
       for (int j = 0; j < ih; j++)
       {
           for (int i = 0; i < iw; i++)
           {
               r = (int)inIm[i][j];
               if (r < 0) r = 0;
               else if (r > 255) r = 255;
               pix[i + j * iw] = 255 << 24|r << 16|r << 8|r;                
           }
       }
       return pix;
   }
   
   //一维double数据转变为图像序列
   public int[] toPixels(double[] inIm, int iw, int ih)
   {
   	int r;
       int[] pix = new int[iw*ih];
       for (int j = 0; j < ih; j++)
       {
           for (int i = 0; i < iw; i++)
           {
               r = (int)inIm[i+j*iw];
               if (r < 0) r = 0;
               else if (r > 255) r = 255;
               pix[i + j * iw] = 255 << 24|r << 16|r << 8|r;                
           }
       }
       return pix;
   }
   
   //图像序列pix阈值分割	
	public int[] thSegment(int[] pix, int iw, int ih, int th)
	{						
		int[] im = new int[iw*ih];
		int t;
		for(int i = 0; i < iw*ih; i++)	
		{
			t = pix[i]&0xff;
								
			if(t > th) 
				im[i] = (255<<24)|(255<<16)|(255<<8)|255;//背景色
			else
			    im[i] = (255<<24)|(0<<16)|(0<<8)|0;      //前景色为		
		}
		return im;
	}
	
	//将RGB图像序列pix变为2值图像序列im
	public byte[] rgb2Bin(int[] pix, int iw, int ih)
	{
		byte[] im = new byte[iw*ih];
		
		for(int i = 0; i < iw*ih; i++)			
			if((pix[i]&0xff) > 128) 
			    im[i] = 0;   
			else
			    im[i] = 1;  
		return im;
	}
	
	//将ARGB图像序列pixels变为2值图像序列im
	public byte[] toBinary(int[] pix, int iw, int ih)
	{
		byte[] im = new byte[iw*ih];
		for(int i = 0; i < iw*ih; i++)							
			if((pix[i]&0xff) > 128) 
				im[i] = 0;   //背景色为0
			else
			    im[i] = 1;   //前景色为1			
		return im;
	}
	
	//陈欣怡
	public byte[] baweitu(int[] pix, int iw, int ih,int x)
	{
		byte[] im = new byte[iw*ih];
		
		for(int i = 0; i < iw*ih; i++){
			
			if((pix[i]&x)==x){
				im[i] = 0;
			}
			else im[i] = 1;
		}
			  	
		return im;
	}
	
   
   //将2值图像序列bw变为RGB图像序列pix
	public int[] bin2Rgb(byte[] bw, int iw, int ih)
	{
		int r, g, b;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0; i < iw*ih; i++) 
		{		
			if(bw[i] == 0)	    {r = 255; g= 255;b = 255;}
			else if(bw[i] == 1)	{r = 0;   g = 0; b = 0;}
			else				{r = 255; g = 0; b = 0;}				
			pix[i] = (255<<24)|(r<<16)|(g<<8)|b;			
		}
		return pix;	
	}
	
	//将字节数组pix转化为图像序列pixels
   public int[] byte2int(byte[][] pix, int iw, int ih)
   {
   	int[] p = new int[iw*ih];
   	    	 
   	for(int j = 0; j < ih; j++)
   	{
   		for(int i = 0; i < iw; i++)
   		{
   			int c = pix[i][j];
   			if(c < 0) c = c + 256;
   			p[i+j*iw] = (255<<24)|(c<<16)|(c<<8)|c;                			
   		}            
       } 
       return p;
   }
	
	//将2值图像矩阵image2变为ARGB图像序列pixels
	public int[] toARGB(int[] bw, int iw, int ih)
	{
		int r;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0;i < iw*ih; i++) 
		{						
			if(bw[i] == 0)	r = 255;				
			else			r = 0;								
			pix[i] = (255<<24)|(r<<16)|(r<<8)|r;			
		}
		
		int x = bw[iw*ih+2];
		int y = bw[iw*ih+3];
		
		//用红色小方块标注链码起始点
		if(x!=-1 && y!=-1)
		{		
			for(int i=-2; i<3; i++)
			   for(int j=-2; j<3; j++)
			       pix[(x+i)+(y+j)*iw] = (255<<24)|(255<<16)|(0<<8)|0;
		}
		return pix;	
	}
	
	//将2值图像矩阵image2变为ARGB图像序列pixels
	public int[] toARGB(byte[] bw, int x, int y, int iw, int ih)
	{
		int r;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0;i < iw*ih; i++) 
		{						
			if(bw[i] == 0)	r = 255;				
			else			r = 0;								
			pix[i] = (255<<24)|(r<<16)|(r<<8)|r;			
		}
		
		//用红色小方块标注链码起始点
		if(x!=-1 && y!=-1)
		{		
			for(int i=-2; i<3; i++)
			   for(int j=-2; j<3; j++)
			       pix[(x+i)+(y+j)*iw] = (255<<24)|(255<<16)|(0<<8)|0;
		}
		return pix;	
	}
	
	public int[] byte2ARGB(byte[][] bw, int iw, int ih)
	{
		int[] pix = new int[iw * ih];
		int r, g, b;
		
	    for(int j = 0;j < ih; j++) 
		{
			for(int i = 0; i < iw; i++)
			{	
				if(bw[i][j] == 0)
				{					
					r = 255;
					g = 255;
					b = 255;
				}
				else
				{					
					r = 255;
					g = 0;
					b = 0;
				}				
				pix[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;
			}
		}
		return pix;	
	}
	
	//基本计算------------------------------------
	
	//计算数组d最大值
   public int maximum(int[] d)
   {
   	int m = 0;
   	for(int i = 0;i < 256; i++)
   	    if(d[i]> m) m = d[i];
   	return m;
   }
   
   //数组h规范化为[0,255]
   public int[] normalize(int[] h, int max)
   {
   	for(int i = 0; i < 255; i++)
   	    h[i] = (int)(h[i] * 255 / max);
   	return h;
   } 
   
   public int[] getHist(int[] pix, int iw, int ih)
	{	
		int[] hist = new int[256];		
		for(int i = 0; i < iw*ih; i++) 
		{
			int grey = pix[i]&0xff;
			hist[grey]++;			
		}
		return hist;
	}
	
 
		
   //显示图像和数据-----------------------------------
       
   //显示一幅图和标题
   public void draw(Graphics g, Image om, int x, int y, String str)
   { 	
		g.clearRect(0, 0, 700, 500);
	    g.drawImage(om, x, y, null);	        
	    g.setColor(Color.red);
	    g.setFont(new Font("",Font.BOLD, 12));
	    g.drawString(str, x+100, 320);	    
   }
   
   //显示一幅图和标题与参数(int)
   public void draw(Graphics g, Image om, String str, int n)
   {    	
	    g.clearRect(0,0,530, 350);        	
        g.drawImage(om, 50, 100, null);      
	    g.drawString(str + n, 20, 100); 
   }
   
   //显示一幅图和标题与参数(float)
   public void draw(Graphics g, Image om, String str, float p)
   {    	
		g.clearRect(0, 0, 530, 330);
	    g.drawImage(om, 5, 50, null);	        
	    g.setColor(Color.red);
	    g.drawString(str+"(p = "+ p + ")", 20, 320); 
   }
   


   
   //显示一幅图与数据
   public boolean draw(Graphics g, Image iImage, String ents, double entr, 
                        String avrs, double aver, String meds, double medn, 
                        String sqss, double sqsm) 
   {  	  
       g.clearRect(0, 0, 530, 350);
   	g.setColor(Color.blue);
   	g.setFont(new Font("",Font.BOLD, 18));
   	g.drawString(ents, 270, 80);
   	g.drawString(""+entr, 270, 100); 
   	g.drawString(avrs, 270, 140);
   	g.drawString(""+aver, 270, 160);  
   	g.drawString(meds, 270, 200);
   	g.drawString(""+medn, 270, 220); 
   	g.drawString(sqss, 270, 260);
   	g.drawString(""+sqsm, 270, 280); 
   	g.drawImage(iImage, 5, 50, null);
   	return true;         	
   }
   
   public void draw(Graphics g, float mu, float t)
   {
   	g.clearRect(0, 0, 530, 350);
   	g.drawLine(30,280,230,280);
       g.drawLine(30,280,30,80);
       g.drawLine(30,80 ,230, 80);
       g.drawLine(230,280,230,80);
       double x, y;
       
       double u = 0.0; 
       double v = 0.0;
       
       //double p = mu;
       
       //draw curve y = 2.8 * x * (1-x)
       for(int i = 1; i < 1000; i++)
       {
       	x = i / 1000.0;
       	y = mu * x * (1 - x);
       	
       	u = x; v = y;
       	g.drawLine((int)(200*u+30),(int)(280-200*v),
       	           (int)(30+200*x),(int)(280-200*y));
       }
       
       //draw line y = x
       g.drawLine(30, 280, 230, 80);
       
       //*** TESTING ATRACTOR ***
       u = t;
       v = 0.0;
       x = t;
       
       for(int i = 0; i< 20;i++)
       {
       	y = mu * x * (1 - x);
       	g.drawLine((int)(200*u+30), (int)(280-200*v), 
       	           (int)(30+200*x), (int)(280-200*y));
       	g.drawLine((int)(30+200*x), (int)(280-200*y), 
       	           (int)(200*y+30), (int)(280-200*y));
       	u = y; v = y;
       	x = y;
       }
       g.setColor(Color.red);
       g.drawString("0", 25, 300);
       g.drawString("1", 225, 300);
       g.drawString(""+t, 15+(int)(200*t), 300); 
   }
   
   //常用draw
   public void draw(Graphics g, Image om, String ostr)
   {  
	   int iw = om.getWidth(null);
	   int ih = om.getHeight(null);
	   
	   g.clearRect(0,0,700, 500);
	   
	   if(iw<ih){
		   
		  double  times = 0.7;
		  g.drawImage(om,150,100, (int)(iw*times),(int)(ih*times), null);
		   
	   }
	   else{
		   g.drawImage(om, 50, 100, null);
	   }
	   
	   g.drawString(ostr, 20,90);    	
   }
   
   //陈欣怡
   public void drawset(Graphics g, Image om, String ostr,int iw,int ih,double times)
   {   
	    g.clearRect(0,0,700, 500);
   		g.drawImage(om,(int)(50/(times*3)), (int)(100/(times*3)), iw,ih, null);
   	    g.drawString(ostr, 20,90);    	
   }
   
   public void drawsuoxiao(Graphics g, Image om, String ostr,int iw,int ih,double times)
   {   
	    g.clearRect(0,0,700, 500);
   		g.drawImage(om,150,100, (int)(iw*times),(int)(ih*times), null);
   	    g.drawString(ostr, 20,90);    	
   }
   
   //陈欣怡
   public void draw(Graphics g, int a[])
   {   
	   int x=1;
	   String ss = "此BMP图像位数为："+a[0];
	   for(int i=0;i<a[0];i++)
		    x*=2;
	   g.clearRect(0,0,700, 500);
	   g.drawString(ss,20,90);
	   for(int i =1;i<x*3;i+=3)
	   { String s = "RGB:"+a[i]+","+a[i+1]+","+a[i+2];
   	    g.drawString(s,20,100+i*5); 
   	    
	   }
   }
   
   
   //显示直方图
   public void draw(Graphics g, int[] h, int max)
   {
   	g.clearRect(270, 0, 530, 350);    	
   	g.drawLine(270, 306, 525, 306); //x轴
   	g.drawLine(270, 50,  270, 306); //y轴
   	for(int i = 0; i < 256; i++)
   	    g.drawLine(270+i, 306, 270+i, 306-h[i]);
   	g.drawString(""+max, 275, 60);
      	g.drawString("直方图", 370, 320);
   }  
   
   //显示数据
   public void draw(Graphics g, int x1, int y1, int x2, int y2, String str)
   {
   	g.clearRect(270,50,526,306);
   	g.setColor(Color.red);
   	g.drawLine(270,305,525,305);            //x轴
	    g.drawLine(270,305,270,50);             //y轴
	    g.drawLine(270,305,270+x1,305-y1);      //(0,0)-(x1,y1) 
	    g.drawLine(270+x1,305-y1,270+x2,305-y2);//(x1,y1)-(x2,y2)
	    g.drawLine(270+x2,305-y2,525,50);       //(x2,y2)-(255,255)
	    g.setColor(Color.BLUE);
	    g.drawString("(x1,y1)",255+x1,290-y1);
	    g.drawString("(x2,y2)",255+x2,325-y2);
	    g.drawString(str,340,70);
   }
   
   //陈欣怡 实现旋转（仅提供180度)
	public int[] upsidedown(int[] pix, int iw, int ih)
	{
			int[] im = new int[iw*ih];
			
			int k=0;
			for(int i = iw*ih-1; i>=0; i--){
				
				im[k++] = pix[i];
			}
				  	
			return im;
		}
	
	//陈欣怡 实现右转90度
	public int[] youzhuan90du(int[] pix, int iw, int ih)
	{
			int[][] im = new int[ih][iw];
			int[] res = new int[iw*ih];
			
			int k=0;
			for(int i = 0; i<ih;i++)
			for(int j=0; j<iw;j++){
				
				im[i][j] = pix[k++];
			}
			
			
			k=0;
			for(int j= 0;j<iw;j++)
			for(int i=ih-1;i>=0;i--){
				
				res[k++] = im[i][j];
			}
				  	
			return res;
		}
	
	
	
	  public int[] grayDilate(int[] pix, int w, int h, int type)
	    {
	    	int i, j, k, l, n;
	    	int m0 = 0, m1 = 0, m2 = 0;
	    	int[][] im  = new int[w][h];
	    	int[] out = new int[w*h];
	    	
	    	for(j = 0; j < h; j++)
			    for(i = 0; i < w; i++)		    
					im[i][j] = pix[i+j*w]&0xff;
	  	    
	    	if(type == 1)
	    	{    	
	    	    m0 = 80; m1 = 10; m2 = 50;
	    	}
	    	else if(type == 2){
	    		m0 = 1; m1 = 1; m2 = 1;
	    	}
	    	int[][] b = {{m2, m1, m2},
	    	             {m1, m0, m1},
	    	             {m2, m1, m2}}; 
	    	    	
			for(j = 0; j < h; j++)
			{
				for(i = 0; i < w; i++)
				{
					int max = 0;
					for(k = -1; k < 2; k++)
					{
					    for(l = -1; l < 2; l++)
					    {
					        if(i-k>=0&&j-l>=0&&(i-k)<w&&(j-l)<h)
					        {    				        
					            n = im[i-k][j-l]+b[k+1][l+1];
					            if(n > max) max = n;    				            
					        }    				            	
					    }
					}
					if(max > 255) max = 255;
					out[i+j*w] = 255 << 24|max << 16|max << 8|max;				            
				}
			}    	    	
	    	return out;
	    }    
	  
	  public int[] grayErode(int[] pix, int w, int h, int type)
	    {
	    	int i, j, k, l, n;
	    	int m0 = 0, m1 = 0, m2 = 0;
	    	int[][] im  = new int[w][h];
	    	int[] out = new int[w*h];
	    	
	    	for(j = 0; j < h; j++)
			    for(i = 0; i < w; i++)		    
					im[i][j] = pix[i+j*w]&0xff;
	  	    
	    	
	    	if(type == 1)
	    	{    	
	    	    m0 = 80; m1 = 10; m2 = 50;
	    	}
	    	else if(type == 2){
	    		m0 = 1; m1 = 1; m2 = 1;
	    	}
	    	
	    	int[][] b = {{m2, m1, m2},
	    	             {m1, m0, m1},
	    	             {m2, m1, m2}}; 
	    	
			for(j = 0; j < h; j++)
			{
				for(i = 0; i < w; i++)
				{
					int min = 255;
					for(k = -1; k < 2; k++)
					{
					    for(l = -1; l < 2; l++)
					    {
					        if(i+k>=0&&j+l>=0&&i+k<w&&j+l<h)
					        {    				        
					            n = im[i+k][j+l]-b[k+1][l+1];
					            if(n < min) min = n;    				            
					        }    				            	
					    }
					}
					if(min < 0) min = 0;
					out[i+j*w] = 255 << 24|min << 16|min << 8|min;				            
				}
			}
			   	    	
	    	return out;
	    }
	  
	  
	//锐化
    public int[] ruihua(int[] pix, int iw, int ih, int type)
	{	            
        int r;
		int[] out1 = grayDilate(pix, iw, ih, 2);
		int[] out2 = grayErode(pix,  iw, ih, 2);
		
		for(int i = 0; i < iw*ih; i++)
		{
			r = Math.abs((out1[i]&0xff)-(out2[i]&0xff));	    
		    pix[i] = 255 << 24|r << 16|r << 8|r;
		}
		return pix;
	}
	public int[] grayFilter(int[] pix, int iw, int ih)
	{		
       	int r;
       	int[] out1, out2;
       	        
		out1 = grayErode(pix, iw, ih, 2);
		out1 = grayDilate(out1, iw, ih, 2);
	
		out1 = grayDilate(out1, iw, ih, 2);
		out1 = grayErode(out1, iw, ih, 2);
		
		out2 = grayDilate(pix, iw, ih, 2);
		out2 = grayErode(out2, iw, ih, 2);
		            
		out2 = grayErode(out2, iw, ih, 2);
		out2 = grayDilate(out2, iw, ih, 2);
		for(int i = 0; i < iw*ih; i++)
		{
			r = (int)(((out1[i]&0xff)+  (out2[i]&0xff))/2);				    
		    pix[i] = 255 << 24|r << 16|r << 8|r;
		}
		return pix;
	}
	
	

	//陈欣怡 平滑 3x3 邻域   注意第一个字节的alpha数值!!
	public int[] pinghua(int []data,int iw,int ih) throws Exception{
	    
	    int[][] ress=new int[ih][iw];
	    int[] res = new int[(iw)*(ih)];
	    int temp = 0;
	    
	    int rsum,gsum,bsum,r,g,b;	 
	    int k = 0;
	      for(int i=0;i<ih;i++)
		  for(int j=0;j<iw;j++){
			  ress[i][j] = data[k++];
		  }
	      
	      k=0;
	      for(int i=0;i<ih;i++)
			  for(int j=0;j<iw;j++){
				  if(i==0||j==0||i==ih-1||j==iw-1){
					  
					  temp = ress[i][j]; 
					  res[k++] = temp;
					  continue;
					  
				  }
				
				  rsum=0;
				  gsum=0;
				  bsum=0;
				  
				  int alpha = (ress[i][j] &  0xff000000)>> 24;
				  
				  for(int m=-1;m<=1;m++)
		    			for(int l=-1;l<=1;l++){
		    				
		    				int pix = ress[i+m][j+l];
		    				 r =(pix & 0xff0000 ) >> 16 ; 
		    				 g= (pix & 0xff00 ) >> 8 ; 
		    				 b= (pix & 0xff ); 
		    				 
		    				 rsum+=r;
		    				 gsum+=g;
		    				 bsum+=b;
		    				 
		    				
		    			}
				  
				  rsum/=9; 
				  gsum/=9;
				  bsum/=9;
                  
				  temp=((alpha*256*256+rsum*256)+gsum)*256+bsum;
				  
				  res[k++] = temp ;
				 	  
			 }
	       return res;
	       
	   }
	
	//陈欣怡 平滑 3x3 超限像素   注意第一个字节的alpha数值!!
		public int[] pinghua2(int []data,int iw,int ih) throws Exception{
		    
		    int[][] ress=new int[ih][iw];
		    int[] res = new int[(iw)*(ih)];
		    int temp = 0;
		    
		    int rsum,gsum,bsum,r,g,b;	 
		    int k = 0;
		      for(int i=0;i<ih;i++)
			  for(int j=0;j<iw;j++){
				  ress[i][j] = data[k++];
			  }
		      
		      k=0;
		      for(int i=0;i<ih;i++)
				  for(int j=0;j<iw;j++){
					  if(i==0||j==0||i==ih-1||j==iw-1){
						  
						  temp = ress[i][j]; 
						  res[k++] = temp;
						  continue;
						  
					  }
					
					  rsum=0;
					  gsum=0;
					  bsum=0;
					  
					  int alpha = (ress[i][j] &  0xff000000)>> 24;
					  
					  for(int m=-1;m<=1;m++)
			    			for(int l=-1;l<=1;l++){
			    				
			    				int pix = ress[i+m][j+l];
			    				 r =(pix & 0xff0000 ) >> 16 ; 
			    				 g= (pix & 0xff00 ) >> 8 ; 
			    				 b= (pix & 0xff ); 
			    				 
			    				 rsum+=r;
			    				 gsum+=g;
			    				 bsum+=b;
			    				 
			    				
			    			}
					  
					  rsum/=9; 
					  gsum/=9;
					  bsum/=9;
	                  
					  temp=((alpha*256*256+rsum*256)+gsum)*256+bsum;
					  
					  if((temp - ress[i][j]>64)||(ress[i][j] - temp>64) ){
						  res[k++] = temp ;
					  }
					  
					  else
					  
					  res[k++] =  ress[i][j];
					 	  
				 }
		       return res;
		       
		   }
		
	 //转变为灰度图像
public int[] toGray(int[] pix, int iw, int ih)
	   {    
			ColorModel cm = ColorModel.getRGBdefault();
			int r, g, b, gray;
				
			for(int i = 0; i < iw*ih; i++)	
			{			
				r = cm.getRed(pix[i]);
				g = cm.getGreen(pix[i]);
				b = cm.getBlue(pix[i]);	
				gray =(int)((r + g + b) / 3);
				pix[i] = 255 << 24|gray << 16|gray << 8|gray;
			}		
			return pix;
		}	
	    
	
		//图像缩放
		public BufferedImage scale(BufferedImage in, int iw,int ih,double r,int interpolationType){
			AffineTransform matrix=new AffineTransform(); //仿射变换
			matrix.scale(r,r);
			AffineTransformOp op = null;
			if (interpolationType==1){			//近邻
				op=new AffineTransformOp(matrix, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			}
			else if (interpolationType==2){		//双线性
				op=new AffineTransformOp(matrix, AffineTransformOp.TYPE_BILINEAR);
			}
			else{
				try {
					throw new Exception("input interpolation type from 1-3 !");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int width = (int)(iw * r);
			int height = (int)(ih * r);
			BufferedImage dstImage = new BufferedImage(width, height, in.getType());
			op.filter(in, dstImage);
			return dstImage;
		}
		
		public int[][] edge(int[][] in, byte[][] tmp, int iw, int ih) 
		{
			int[][] ed = new int[iw][ih];
			
			for(int j = 1; j < ih-1; j++)
			{		
				for(int i = 1; i < iw-1; i++)
				{
					ed[i][j] = Math.abs(tmp[0][0]*in[i-1][j-1]+tmp[0][1]*in[i-1][j]+
					                    tmp[0][2]*in[i-1][j+1]+tmp[1][0]*in[i][j-1]+
				                        tmp[1][1]*in[i][j]    +tmp[1][2]*in[i][j+1]+
					                    tmp[2][0]*in[i+1][j-1]+tmp[2][1]*in[i+1][j]+
					                    tmp[2][2]*in[i+1][j+1]);	            						
				}
			}
			return ed;
		}
		
		//锐化/边缘检测   num=1 krisch num=2 拉普拉斯  flag=false锐化 true边缘检测
		 public int[] krilpls(int[] px, int iw, int ih, int num,int thresh, boolean flag)
			{
				int i, j, r, g, b;
			 int[][] inr   = new int[iw][ih];//红色分量矩阵
			 int[][] ing   = new int[iw][ih];//绿色分量矩阵
			 int[][] inb   = new int[iw][ih];//蓝色分量矩阵
			 int[][] gray  = new int[iw][ih];//灰度图像矩阵
			 
			 ColorModel cm = ColorModel.getRGBdefault();
			 
			 for(j = 0; j < ih; j++)
			 {	    
			     for(i = 0; i < iw; i++)
			     {
			     	inr[i][j] = cm.getRed(px[i+j*iw]);
			         ing[i][j] = cm.getGreen(px[i+j*iw]);
			         inb[i][j] = cm.getBlue(px[i+j*iw]);
			         
			         //转变为灰度图像矩阵
			         gray[i][j] = (int)((inr[i][j]+ing[i][j]+inb[i][j])/3.0);
			     }
			 }	           
			 
			 if(num == 1)//Kirsch
			 {
			 	byte[][][] kir ={{{ 5, 5, 5},
			                       {-3, 0,-3},
			                       {-3,-3,-3}},//kir0
			                      
				                     {{-3, 5, 5},
			                       {-3, 0, 5},
			                       {-3,-3,-3}},//kir1
			                      
			                      {{-3,-3, 5},
			                       {-3, 0, 5},
			                       {-3,-3, 5}},//kir2
			                      
				                     {{-3,-3,-3},
			                       {-3, 0, 5},
			                       {-3, 5, 5}},//kir3
			                      
			                      {{-3,-3,-3},
			                       {-3, 0,-3},
			                       { 5, 5, 5}},//kir4
			                      
				                     {{-3,-3,-3},
			                       { 5, 0,-3},
			                       { 5, 5,-3}},//kir5
			                                    
			                      {{ 5,-3,-3},
			                       { 5, 0,-3},
			                       { 5,-3,-3}},//kir6
			                      
				                     {{ 5, 5,-3},
			                       { 5, 0,-3},
			                       {-3,-3,-3}}};//kir7	        
			     
			     if(flag)//边缘检测
			     {
			     	int[][][] edge = new int[8][iw][ih];
			     	for(i = 0; i < 8; i++)
				            edge[i] = edge(gray, kir[i], iw, ih);
				        for(int k = 1; k < 8; k++)
							for(j = 0; j < ih; j++)
						        for(i = 0; i < iw; i++)
						           	if(edge[0][i][j] < edge[k][i][j])
						        	    edge[0][i][j] = edge[k][i][j];
						        	    	
						for(j = 0; j < ih; j++)
					    {
					        for(i = 0; i < iw; i++)
					        {		       			        	
				        		if(edge[0][i][j] > thresh) r = 0;
				        		else r = 255;
				        		px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;			        	
					        }
					    }		        
			     }
			     else   //锐化
			     {
			     	int[][][] edger = new int[8][iw][ih];
			         int[][][] edgeg = new int[8][iw][ih];
			         int[][][] edgeb = new int[8][iw][ih];
			         	        
				        for(i = 0; i < 8; i++)
				        {	                    
				            edger[i] = edge(inr, kir[i], iw, ih);
				            edgeg[i] = edge(ing, kir[i], iw, ih);
				            edgeb[i] = edge(inb, kir[i], iw, ih);
				        }
				        
				        for(int k = 1; k < 8; k++)
						{			
							for(j = 0; j < ih; j++)
						    {
						        for(i = 0; i < iw; i++)
						        {
						        	if(edger[0][i][j] < edger[k][i][j])
						        	   edger[0][i][j] = edger[k][i][j];	
						        	if(edgeg[0][i][j] < edgeg[k][i][j])
						        	   edgeg[0][i][j] = edgeg[k][i][j];		        	
						        	if(edgeb[0][i][j] < edgeb[k][i][j])
						        	   edgeb[0][i][j] = edgeb[k][i][j];	
						        }
						    } 
					    }
					    
					    for(j = 0; j < ih; j++)
					    {
					        for(i = 0; i < iw; i++)
					        {
					           	r = edger[0][i][j];
					        	g = edgeg[0][i][j];
					        	b = edgeb[0][i][j];			        			        	
					        	px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;			        	
					        }
					    }
				    } 		     
			 }
			 else if(num == 2)                       //Laplace
			 {
				    byte[][] lap1 = {{ 1, 1, 1},
			                      { 1,-8, 1},
			                      { 1, 1, 1}};
				    
			     if(flag)//边缘检测
			     {
			     	int[][] edge = edge(gray, lap1, iw, ih);
					    
						for(j = 0; j < ih; j++)
					    {
					        for(i = 0; i < iw; i++)
					        {			        	
					        	if(edge[i][j] > thresh) r = 0;//黑色，边缘点
					        	else r = 255;
					        	px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;
					        	
					        }
					    }
			     }
			     else
			     {	                           
					    int[][] edger = edge(inr, lap1, iw, ih);
					    int[][] edgeg = edge(ing, lap1, iw, ih);
					    int[][] edgeb = edge(inb, lap1, iw, ih);
						
						for(j = 0; j < ih; j++)
					    {
					        for(i = 0; i < iw; i++)
					        {
					           	r = edger[i][j];
					        	g = edgeg[i][j];
					        	b = edgeb[i][j];
					        	px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;			        	
					        }
					    }
				    }
			 }
			 
			    return px;
		}
		 
		 //细化
		 public byte[] xihua(byte bm[], int iw, int ih)
		    {
			    boolean bModified;     //二值图像修改标志
			
				boolean bCondition1;   //细化条件1标志
				boolean bCondition2;   //细化条件2标志
				boolean bCondition3;   //细化条件3标志
				boolean bCondition4;   //细化条件4标志
				
				int nCount;
				
				//5X5像素块
				int neighbour[][] = new int[5][5];
			    
			    byte[][] bw_tem = new byte[iw][ih];
			    
				bModified = true;     //细化修改标志, 用作循环条件
			    
			    //细化循环开始
				while(bModified)
				{
					bModified = false;
			        
			        //初始化临时二值图像bw_tem
			        for(int j = 0; j < ih; j++)
			            for(int i = 0; i < iw; i++)	            
			                bw_tem[i][j] = 0;
			                
			        for(int j = 2; j < ih - 2; j++)
			        {    
					    for(int i = 2; i < iw - 2; i++)
					    {
			                bCondition1 = false;
						    bCondition2 = false;
						    bCondition3 = false;
						    bCondition4 = false;
						    	                
						    if(bm[i+j*iw] == 0)       //若图像的当前点为白色，则跳过
							    continue;					
							    
							//取以当前点为中心的5X5块
							for(int k = 0; k < 5; k++)					    
								for(int l = 0; l < 5; l++)					   
			                        neighbour[k][l] = bm[(i+k-2)+(j+l-2)*iw];					
					    
							//(1)判断条件2<=n(p)<=6
				            nCount = neighbour[1][1]+neighbour[1][2]+neighbour[1][3]+
				                     neighbour[2][1]                +neighbour[2][3]+
				                     neighbour[3][1]+neighbour[3][2]+neighbour[3][3];
				            if(nCount >= 2 && nCount <= 6)
					            bCondition1 = true;
			                else{
			                    bw_tem[i][j] = 1;
								continue;       //跳过, 加快速度
							}
							
							//(2)判断s(p)=1
							nCount = 0;
							if (neighbour[2][3] == 0 && neighbour[1][3] == 1)
								nCount++;
							if (neighbour[1][3] == 0 && neighbour[1][2] == 1)
								nCount++;
							if (neighbour[1][2] == 0 && neighbour[1][1] == 1)
								nCount++;
							if (neighbour[1][1] == 0 && neighbour[2][1] == 1)
								nCount++;
							if (neighbour[2][1] == 0 && neighbour[3][1] == 1)
								nCount++;
							if (neighbour[3][1] == 0 && neighbour[3][2] == 1)
								nCount++;
							if (neighbour[3][2] == 0 && neighbour[3][3] == 1)
								nCount++;
							if (neighbour[3][3] == 0 && neighbour[2][3] == 1)
								nCount++;					
							if (nCount == 1)
								bCondition2 = true;
			                else
			                {
			                    bw_tem[i][j] = 1;
								continue;
							}
							
							//(3)判断p0*p2*p4=0 or s(p2)!=1
							if (neighbour[2][3]*neighbour[1][2]*neighbour[2][1] == 0)
								bCondition3 = true;
							else
							{
								nCount = 0;
								if (neighbour[0][2] == 0 && neighbour[0][1] == 1)
									nCount++;
								if (neighbour[0][1] == 0 && neighbour[1][1] == 1)
									nCount++;
								if (neighbour[1][1] == 0 && neighbour[2][1] == 1)
									nCount++;
								if (neighbour[2][1] == 0 && neighbour[2][2] == 1)
									nCount++;
								if (neighbour[2][2] == 0 && neighbour[2][3] == 1)
									nCount++;
								if (neighbour[2][3] == 0 && neighbour[1][3] == 1)
									nCount++;
								if (neighbour[1][3] == 0 && neighbour[0][3] == 1)
									nCount++;
								if (neighbour[0][3] == 0 && neighbour[0][2] == 1)
									nCount++;
								if (nCount != 1) //s(p2)!=1
									bCondition3 = true;
								else
								{
			                        bw_tem[i][j] = 1;
									continue;
								}
							}
			
							//(4)判断p2*p4*p6=0 or s(p4)!=1
							if (neighbour[1][2]*neighbour[2][1]*neighbour[3][2] == 0)
								bCondition4 = true;
							else
							{
								nCount = 0;
								if (neighbour[1][1] == 0 && neighbour[1][0] == 1)
									nCount++;
								if (neighbour[1][0] == 0 && neighbour[2][0] == 1)
									nCount++;
								if (neighbour[2][0] == 0 && neighbour[3][0] == 1)
									nCount++;
								if (neighbour[3][0] == 0 && neighbour[3][1] == 1)
									nCount++;
								if (neighbour[3][1] == 0 && neighbour[3][2] == 1)
									nCount++;
								if (neighbour[3][2] == 0 && neighbour[2][2] == 1)
									nCount++;
								if (neighbour[2][2] == 0 && neighbour[1][2] == 1)
									nCount++;
								if (neighbour[1][2] == 0 && neighbour[1][1] == 1)
									nCount++;
								if (nCount != 1)//s(p4)!=1
									bCondition4 = true;
							}
							if(bCondition1 && bCondition2 && bCondition3 && bCondition4)
							{
								bw_tem[i][j] = 0;
								bModified = true;
							}
							else
								bw_tem[i][j] = 1;									    
						}
					}
			
					//将细化了的临时图像bw_tem[h][w]copy到bw[h][w],完成一次细化
			        for(int j = 2;j < ih - 2; j++) 
					    for(int i = 2;i < iw - 2; i++)
			                bm[i+j*iw] = bw_tem[i][j];
				}		
			    return bm;				
			}
			
		 //霍夫检测直线
		 public byte[] zhixianjc(byte bm[], int iw, int ih)
		    {	 
		        //计算变换域的尺寸
		        int tMaxDist = (int)Math.sqrt(iw*iw + ih*ih);//最大距离
		        
				int tMaxAngle = 90;                          //角度从0-180，每格2度
		          
			    byte[] obm = new byte[iw*ih];		 
			   	int [][] ta = new int[tMaxDist][tMaxAngle];
					
				//变换域的坐标
				int tDist, tAngle;	
			
				//像素值
				int pixel;			
					
			    //初始化临时图像矩阵		
				for(int i = 0; i < ih; i++)
				    for(int j = 0; j < iw; j++)
				        obm[i+j*iw] = 0;
				        
				//初始化变换域矩阵       
			    for(tDist =0;tDist<tMaxDist;tDist++)
			        for(tAngle=0; tAngle<tMaxAngle; tAngle++)
				    	ta[tDist][tAngle] = 0;
					
				for(int j = 0; j < ih; j++)
				{
					for(int i = 0;i < iw; i++)
					{					
						//取得当前指针处的像素值
						pixel = bm[i+j*iw];
				
						//如果是黑点，则在变换域的对应各点上加1
						if(pixel == 1)
						{
							//注意步长是2度
							for(tAngle=0; tAngle<tMaxAngle; tAngle++)
							{
								tDist = (int)Math.abs(i*Math.cos(tAngle*2*Math.PI/180.0) 
								      + j*Math.sin(tAngle*2*Math.PI/180.0));
							
								ta[tDist][tAngle] = ta[tDist][tAngle] + 1;
							}
						}			
					}
				}
							
				//找到变换域中的两个最大值点
				int maxValue1 = 0;
				int maxDist1  = 0;
				int maxAngle1 = 0;		
				
				int maxValue2 = 0;
				int maxDist2  = 0;
				int maxAngle2 = 0;
						
				//找到第一个最大值点
				for (tDist = 0; tDist < tMaxDist; tDist++)
				{
					for(tAngle = 0; tAngle < tMaxAngle; tAngle++)
					{
						if(ta[tDist][tAngle] > maxValue1)
						{
							maxValue1 = ta[tDist][tAngle];
							maxDist1  = tDist;
							maxAngle1 = tAngle;
						}	
					}
				}
			
				//将第一个最大值点(maxDist1, maxAngle1)为中心的区域清零
				for (tDist = -9; tDist < 10; tDist++)
					for(tAngle = -1; tAngle < 2; tAngle++)
						if(tDist+maxDist1 >= 0 && tDist+maxDist1 < tMaxDist && 
						   tAngle+maxAngle1 >= 0 && tAngle+maxAngle1 <= tMaxAngle)
						    ta[tDist+maxDist1][tAngle+maxAngle1] = 0;	
			
				//找到第二个最大值点
				for (tDist = 0; tDist < tMaxDist; tDist++)
				{
					for(tAngle = 0; tAngle < tMaxAngle; tAngle++)
					{
						if(ta[tDist][tAngle] > maxValue2)
						{
							maxValue2 = ta[tDist][tAngle];
							maxDist2  = tDist;
							maxAngle2 = tAngle;
						}	
					}
				}	
				
				//标注直线
				for(int j = 0; j < ih; j++)
				{
					for(int i = 0; i < iw; i++)
					{
						//第一条直线上
						tDist = (int)Math.abs(i*Math.cos(maxAngle1*2*Math.PI/180.0) 
						      + j*Math.sin(maxAngle1*2*Math.PI/180.0));
						if (tDist == maxDist1)
							obm[i+j*iw] = 1;
										
						//第二条直线上
						tDist = (int)Math.abs(i*Math.cos(maxAngle2*2*Math.PI/180.0) 
						      + j*Math.sin(maxAngle2*2*Math.PI/180.0));
						if (tDist == maxDist2)
						    obm[i+j*iw] = 1;
					}
				}		
			    return obm;				
			} 
			    
		    //霍夫变换检测圆周
		    public byte[] yuanjc(byte bm[], int iw, int ih)
		    {	         
				int i, j, x, yp, ym, t;
				
				int[] num = new int[iw*ih];     //变换域矩阵
			    byte[] obm = new byte[iw*ih];   //临时图像矩阵
			      
				//初始化变换域矩阵num和临时图像矩阵bw_tem
				for(j = 0; j < ih; j++)
				{
					for(i = 0; i < iw; i++)
					{
						num[i+j*iw]    = 0;
						obm[i+j*iw] =0;
					}
				}
			
			    //calculate num[][]
				for(j = 0; j < ih; j++)
				{
					for(i = 0; i < iw; i++)
					{
						if(bm[i+j*iw] == 1)          //黑色
						{        
							for(x = i-20; x <= i+20; x++)
							{
								t = (int)Math.sqrt(20*20-(x-i)*(x-i));
								yp = j+t;
								ym = j-t;
								if(x >= 0&&x < ih&&yp >= 0&&yp < iw) 
									num[x+yp*iw]++;
								if(x >= 0&&x < ih&&ym >= 0&&ym < iw) 
									num[x+ym*iw]++;
							}
						}
					}
			    }	

			    int i_max = 0, j_max = 0,  //寻找的圆心坐标
				    max   = 0;

				for(j = 0; j < ih; j++)
				{
					for(i = 0; i < iw; i++)
					{
			            if(num[i+j*iw] > max)
			            {
			                max = num[i+j*iw];
						    i_max = i;
							j_max = j;
						}
					}
				}
				
				//画出以i_max,j_max为圆心，半径20的圆
				if(max > 20)
				{
				    for(i = i_max-20; i <= i_max+20; i++)
				    {
					    t = (int)Math.sqrt(20*20-(i_max-i)*(i_max-i));
			            obm[i+(j_max+t)*iw] = 1;  
					    obm[i+(j_max-t)*iw] = 1;
					}
				}
				
				//清除点(i_max,j_max)附近num[i][j]的值
			    for(i = i_max-10; i < i_max+10; i++)
			        for(j = j_max-10; j < j_max+10; j++)
					    if((i_max-10 >= 0)&&(i_max+10 < ih)&&
					       (j_max-10 >= 0)&&(j_max+10 < iw))  //防止数组越界
						    num[i+j*iw] = 0;
			  	
			    //检测第2个圆
				max = 0;
				for(i = 0; i < ih; i++)
				{
					for(j = 0; j < iw; j++)
					{
			            if(num[i+j*iw] > max)
			            {
			                max = num[i+j*iw];
							i_max = i;
							j_max = j;
						}
					}
				}
				
				//画出以i_max,j_max为圆心，半径20的第2个圆
				if(max > 20)
				{
					for(i = i_max-20; i <= i_max+20; i++)
					{
						t = (int)Math.sqrt(20*20-(i_max-i)*(i_max-i));
						obm[i+(j_max+t)*iw] = 1;  
						obm[i+(j_max-t)*iw] = 1;
					}
				}			 
			    return obm;        	
			}  	
		
			//霍夫曼编码
			void Huffman(BufferedImage img,int iw,int ih,int[] pixels)
			{
				int ii;
				int ColorNum=256;
				int width;
				int height;
				float []freq=new float[ColorNum];
				float []freqTemp;
				int []map;
				float entropy=0;
				String [] sCode=new String[ColorNum];
				float avgCode=0;
				float efficiency=0;
				
					
				
						//获取图像的宽和高
						width=iw;
						height=ih;
						//取得图片像素值并复制进pixels数组
						img.getRGB(0, 0, width, height, pixels, 0, width);
					
				
					//初始化sCode数组
					for(int i=0;i<ColorNum;i++)
					{
						sCode[i]="";
					}
					
					//初始化freq数组
					for(int i=0;i<ColorNum;i++)
					{
						freq[i]=0;
					}
					
					//各个灰度的计数
					for(int i=0;i<width*height;i++)
					{
						int temp=(pixels[i]>>16)&0xff;
						freq[temp]=freq[temp]+1;
					}
					
					//各个灰度出现的频率
					for(int i=0;i<ColorNum;i++)
					{
						freq[i]=freq[i]/(width*height);
					}
					
					//计算图像熵
					for(int i=0;i<ColorNum;i++)
					{
						if(freq[i]>0)
						{
							entropy-=freq[i]*Math.log(freq[i])/Math.log(2.0);
						}
					}
					
					freqTemp=new float[ColorNum];	
					map=new int[ColorNum];
					
					for(int i=0;i<ColorNum;i++)
					{
						freqTemp[i]=freq[i];
						map[i]=i;
					}
					//冒泡排序
					for(int j=0;j<ColorNum-1;j++)
					{
						for(int i=0;i<ColorNum-j-1;i++)
						{
							if(freqTemp[i]>freqTemp[i+1])
							{
								float temp=freqTemp[i];
								freqTemp[i]=freqTemp[i+1];
								freqTemp[i+1]=temp;
								
								//更新映射关系
								for(int k=0;k<ColorNum;k++)
								{
									if(map[k]==i)
									{
										map[k]=i+1;
									}else if(map[k]==i+1)
									{
										map[k]=i;
									}
								}
							}
						}
					}
					//开始编码,找到第一个概率非0的像素点
					for(ii=0;ii<ColorNum-1;ii++)
					{
						if(freqTemp[ii]>0)
						{
							break;
						}
					}
					
					//进行Huffman编码
					for(;ii<ColorNum-1;ii++)
					{
						for(int k=0;k<ColorNum;k++)
						{
							if(map[k]==ii)
							{ 
								sCode[k]="1"+sCode[k];
							}
							else if(map[k]==ii+1)
							{
								sCode[k]="0"+sCode[k];
							}
						}
						//最小的两个相加
						freqTemp[ii+1]+=freqTemp[ii];
						//改变映射关系
						for(int k=0;k<ColorNum;k++)
						{
							if(map[k]==ii)
							{
								map[k]=ii+1;
							}
						}
						//重新排序
						for(int j=ii+1;j<ColorNum-1;j++)
						{
							if(freqTemp[j]>freqTemp[j+1])
							{
								float temp=freqTemp[j];
								freqTemp[j]=freqTemp[j+1];
								freqTemp[j+1]=temp;
								//更新映射关系
								for(int k=0;k<ColorNum;k++)
								{
									if(map[k]==j)
									{
										map[k]=j+1;
									}else if(map[k]==j+1)
									{
										map[k]=j;
									}
								}
							}
							else 
							{
								break;
							}
						}
						
					}
					//计算编码的平均长度		
					avgCode=0;
					for(int i=0;i<ColorNum;i++)
					{
						avgCode=avgCode+freq[i]*(sCode[i].length());
					}
					//计算编码效率
					efficiency=entropy/avgCode;	
				
				
				HuffmanShow hs=new HuffmanShow(entropy,avgCode,efficiency);
				hs.setData(freq,sCode);
				hs.showTable();
				hs.setVisible(true);
			}
			
			//陈欣怡 光栅扫描跟踪闭合边缘
			public int[] guangshan(int[] pixels1,int[]pixels2,int iw,int ih,int d,int e){
				
				int[] mark = new int[iw*ih];//用于标记是否为跟踪点
				int r,g,b;
				int[][] gray = new int[iw][ih];
		
				 ColorModel cm = ColorModel.getRGBdefault();
				 //转变为灰度图像矩阵
				for(int j = 0; j < ih; j++)
				 {	    
				     for(int i = 0; i < iw; i++)
				     {
				     	r = cm.getRed(pixels1[i+j*iw]);
				        g = cm.getGreen(pixels1[i+j*iw]);
				         b = cm.getBlue(pixels1[i+j*iw]);
				        
				         gray[i][j] = (int)(0.3*r+0.59*g+0.11*b);
				        
				     }
				 }	   
				
				//光栅跟踪
				for(int j=0;j<ih;j++)
					for(int i=0;i<iw;i++)
					{
						int p=j*iw+i;
						int p1 = 0;
						int p2 = 0;
						int p3 = 0;
						if(i>0&&j>0&&j<ih-1&&i<iw-1){
							 p1=(j-1)*iw+i-1;
							 p2=(j-1)*iw+i;
							 p3=(j-1)*iw+i+1;
						}
						if(gray[i][j]>d){
							mark[p]=1;
						}
						else if(i>0&&j>0&&j<ih-1&&i<iw-1){
							if(mark[p1]==1){
								if(gray[i][j]-gray[i-1][j-1]<e)
									mark[p]=1;
							}
							else if(mark[p2]==1){
								if(gray[i][j]-gray[i-1][j]<e)
									mark[p]=1;
							}
							else if(mark[p3]==1){
								if(gray[i][j]-gray[i][j+1]<e)
									mark[p]=1;
							}
								
						}
						else mark[p] =0;
						
					}
			
					for(int i=0;i<iw*ih;i++){
						if(mark[i]==1){
							int x = 0;
			        		pixels2[i] = (255<<24)|(x<<16)|(x<<8)|x;
						}
					}
					
				return pixels2;
			}
			
			//fano编码
		             
		//真彩转256色
		public int[] to256(File file,int[] pix,int iw,int ih) throws IOException{
				
			for(int i=0;i<ih;i++)
				for(int j=0;j<iw;j++){
					
				}
			
			File file2  = file;
			   InputStream in = null;
			   OutputStream out = null;
			   int[] colornumber = new int[1000];
			   in = new FileInputStream(file);
			   out = new FileOutputStream(file2);
		       int tempbyte = 0;
		      
		       for(int i=0;i<29;i++){
		           tempbyte = in.read();
		           out.write((byte)tempbyte);
		       }
		       	  colornumber[0]= tempbyte ;
		       	  
		         
		           for(int j=0;j<25;j++)
		          tempbyte = in.read();
		        
		           
		           int x=1;
		           
		           for(int i=0;i<colornumber[0];i++)
		   		    x*=2;
		           
		           int k = 1;
		          for(int j=0;j<x;j++)
		         {  
		        	   tempbyte = in.read();
		        	   colornumber[k++]=tempbyte;
		           	   tempbyte = in.read();
		           	   colornumber[k++]=tempbyte;
		           	   tempbyte = in.read();
		           	   colornumber[k++]=tempbyte;
		           	   tempbyte = in.read();
		           	 
		           }
		          
		          
		           		 
		       
		       return colornumber;
			
		}
			
}
