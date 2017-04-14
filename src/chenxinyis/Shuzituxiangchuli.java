package chenxinyis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import chenxinyis.Digi;
import chenxinyis.Common;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.*;




public class Shuzituxiangchuli extends JFrame implements ActionListener
{
	String bmp;
    Image iImage, oImage,oImage2,oImage3,sImage,background;
    BufferedImage img ;  
    BufferedImage img2;
    ImageAnalyse analyse;
    File file;
    Image bcg ;
   
    int   iw, ih;
    int[] pixels,pix;  
   
   
    
             
    boolean loadflag  = false,
            runflag   = false;    //图像处理执行标志 
           
    Digi digit;
    Common common;
   
    
    public Shuzituxiangchuli()
    {     
    	
    	 this.setBackground(Color.white);
         try {
			bcg = ImageIO.read(new File("images//系统用图//icon.png"));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	 this.setIconImage(bcg);

    	  
    	  
        setTitle("数字图像处理.陈欣怡");
     
              
        //菜单界面
        setMenu();
        
        digit  = new Digi();
        common = new Common();
        
        //关闭窗口
        closeWin();
        setSize(484, 400);
        setLocation(300,200);
        setVisible(true);
    }

    @SuppressWarnings({ "static-access", "static-access" })
	public void actionPerformed(ActionEvent evt)
    {
    	String  name;
    	
    	 Graphics graph = getGraphics();	  
        if (evt.getSource() == openItem) 
        {
        	
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images", 0);		//设置默认访问路径
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                name = chooser.getSelectedFile().getAbsolutePath();
                System.out.println("name:"+name);
                if(runflag)
                {
                	loadflag = false;   
                    runflag  = false; 
                }
                
			    
			    	
					bmp=name.substring(name.length()-3);
				    iImage = common.openImage(name,tracker); 
				    oImage = iImage;
				    file = new File(name);
				    
				    
				     try {
						img = ImageIO.read(new File(name));
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}  
				     
				     
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    common.draw(graph,iImage,"原图");
				    loadflag = true;
  
			    			    			               
            }            
        }
        
        else if (evt.getSource() == junhenghua)
        {        	
        	int numbercolor[]= null;
        	if(loadflag)        	
        	{
        		
        		oImage2 = oImage;
        		img = common.toBufferedImage(oImage);
        		oImage = common.junhenghua(img, iw, ih);
        		
        		double times = 0.7;
        		if(iw<ih)
        		common.drawsuoxiao(graph,oImage, "直方图均衡化",iw, ih,times);
        		else
        		common.draw(graph,oImage,"直方图均衡化");	
        		
        		pixels = common.grabber(oImage2, iw, ih);
				Hist h = new Hist("原图直方图");
				h.getData(pixels,iw,ih,img);
				h.setLocation(300, 200);			
				
				pixels = common.grabber(oImage, iw, ih);
				Hist h2 = new Hist("处理后直方图");
				h2.getData(pixels,iw,ih,img2);
				h2.setLocation(400, 200);	
				sImage = oImage;
        		
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        else if (evt.getSource() == readasfile)
        {        	
        	int numbercolor[]= null;
        	if(loadflag)        	
        	{
        		try {
					numbercolor= common.readasfile(file);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} 
        		 common.draw(graph,numbercolor);
        		 sImage = oImage;
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        else if (evt.getSource() == linearItem)
        {   
           	if(loadflag)
        	{ 
 
           		String inputValue = JOptionPane.showInputDialog("斜率和截距（以空格分隔）:");
           		String[] temp = inputValue.split(" ");
           		double x =Double.parseDouble(temp[0]);
           		double b = Double.parseDouble(temp[1]);
				pixels = common.grabber(oImage, iw, ih);		
				
				//对图像进行进行线性拉伸
				pixels = common.linetrans(pixels, iw, ih, x, b);
				
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "变换结果");
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
				JOptionPane.showMessageDialog(null,"请先打开图像!");	        	        	
        }
    
        
        else if (evt.getSource() == histItem)
        {        	
        	
        	if(loadflag)
        	{        		
        		pixels = common.grabber(iImage, iw, ih);
	            
				//显示图像的直方图
				Hist h = new Hist("直方图");
				
				img = common.toBufferedImage(iImage);
				//传送数据
				h.getData(pixels,iw,ih,img);
				h.setLocation(300, 200);								
			}
        	else JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        else if (evt.getSource() == histItem2)
        {        	
        	
        	if(loadflag)
        	{        		
        		pixels = common.grabber(oImage, iw, ih);	
	           
	           
	           img2 = common.toBufferedImage(oImage);
				//显示图像的直方图
				Hist h = new Hist("处理后直方图");
				//传送数据
				h.getData(pixels,iw,ih,img2);
				h.setLocation(300, 200);								
			}
        	else JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        else if (evt.getSource() == baweitu1)
        {        	
        	if(loadflag)        
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);						
        		byte[] imb = common.baweitu(pixels, iw, ih,1);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第1位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu2)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);							
        		byte[] imb = common.baweitu(pixels, iw, ih, 2);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第2位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu3)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);					
        		byte[] imb = common.baweitu(pixels, iw, ih,4);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第3位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu4)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);						
        		byte[] imb = common.baweitu(pixels, iw, ih,8);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第4位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu5)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);						
        		byte[] imb = common.baweitu(pixels, iw, ih,16);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第5位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu6)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);							
        		byte[] imb = common.baweitu(pixels, iw, ih,32);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第6位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu7)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw, ih);							
        		byte[] imb = common.baweitu(pixels, iw, ih,64);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第7位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == baweitu8)
        {        	
        	if(loadflag)        	
        	{
        		oImage = iImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        		
        		pixels = common.grabber(iImage, iw,ih);						
        		byte[] imb = common.baweitu(pixels, iw, ih,128);
        	    pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "8位二值图:第8位");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == outlineItem2)
        {        	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);						
        		byte[] imb = common.toBinary(pixels, iw, ih);
        		pixels = common.bin2Rgb(imb, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "二值化");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == samplItem128)
        {
        	
        	if(loadflag)        	
        	{
        		int gray = 128; 
        	    pixels = common.grabber(oImage, iw, ih);
				boolean flag = false;			
				
				//转变为灰度图像
				pixels = digit.sample(pixels, iw, ih, gray);				
				showPix(graph, pixels, "采样结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
        	else JOptionPane.showMessageDialog(null, "未打开图像");   
        }
        else if (evt.getSource() == samplItem64)
        {
        	
        	if(loadflag)        	
        	{
        		int gray = 64; 
        	    pixels = common.grabber(oImage, iw, ih);
				boolean flag = false;			
				
				//转变为灰度图像
				pixels = digit.sample(pixels, iw, ih, gray);				
				showPix(graph, pixels, "采样结果");		
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
        	else JOptionPane.showMessageDialog(null, "未打开图像");   
        }
        else if (evt.getSource() == samplItem32)
        {
        	
        	if(loadflag)        	
        	{
        		int gray = 32; 
        	    pixels = common.grabber(oImage, iw, ih);
				boolean flag = false;			
				
				//转变为灰度图像
				pixels = digit.sample(pixels, iw, ih, gray);				
				showPix(graph, pixels, "采样结果");		
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
        	else JOptionPane.showMessageDialog(null, "未打开图像");   
        }
        else if (evt.getSource() == samplItem16)
        {
        	
        	if(loadflag)        	
        	{
        		int gray = 16; 
        	    pixels = common.grabber(oImage, iw, ih);
				boolean flag = false;			
				
				//转变为灰度图像
				pixels = digit.sample(pixels, iw, ih, gray);				
				showPix(graph, pixels, "采样结果");		
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
        	else JOptionPane.showMessageDialog(null, "未打开图像");   
        }
        else if (evt.getSource() == quanlIte128)
        { 
        	int level = 128;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        else if (evt.getSource() == quanlIte64)
        { 
        	int level = 64;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        else if (evt.getSource() == quanlIte32)
        { 
        	int level = 32;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        else if (evt.getSource() == quanlIte16)
        { 
        	int level = 16;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        else if (evt.getSource() == quanlIte8)
        {  
        	int level = 8;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        else if (evt.getSource() == quanlIte4)
        { 
        	int level = 4;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");	
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        else if (evt.getSource() == quanlIte2)
        { 
        	int level = 2;
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);				
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");		
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
  		      sImage = oImage;
			}
			else
			 	JOptionPane.showMessageDialog(null, "未打开图像");             	                     
	    }
        
        else if (evt.getSource() == youzhuan180)
        {        	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);						
        		pixels = common.upsidedown(pixels, iw, ih);
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
    		        oImage = createImage(ip);
    		        double times = 0.7;
    		       if(iw>ih)
    	        common.draw(graph,oImage, "旋转180度");
    		       else 
    		     common.drawsuoxiao(graph,oImage,"旋转180度",iw,ih,times);
    		       sImage = oImage;
    	        runflag = true;		        
        	}
        	else
    		 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == youzhuan90)
        {        	
        	if(loadflag)        	
        	{
        		
        		pixels = common.grabber(oImage, iw, ih);	
        		pixels = common.youzhuan90du(pixels, iw, ih);
        		
        		//对调长宽
        			int k = iw;
        			iw = ih;
        			ih = k;
        		
        						
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
    		    oImage = createImage(ip);
    		     
    		     double times = 0.7;
    		    if(iw<ih)
    	        common.drawsuoxiao(graph,oImage,"旋转90度",iw,ih,times);
    		   
    		    else common.draw(graph,oImage,"旋转90度");
    	        runflag = true;		        
    	        sImage = oImage;
        	}
        	else
    		 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        //缩放 双线性
        else if (evt.getSource() == biliner)
        {        	
        	if(loadflag)        	
        	{
        		String inputValue = JOptionPane.showInputDialog("输入缩放倍数(小于1缩小，大于1放大):");
        		double times =Double.parseDouble(inputValue);;
        		img = Common.toBufferedImage(oImage);
    		    img =common.scale(img, iw, ih, times, 1);
    		    int iw2 = img.getWidth();
    		    int ih2 = img.getHeight();
    		   draw h = new draw("缩放"+times+"倍",iw2,ih2,graph,img);
    		   sImage = img;
    	       runflag = true;	
    	        
        	}
        	else
    		 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        else if (evt.getSource() == neighborhood)
        {        	
        	if(loadflag)        	
        	{
        		String inputValue = JOptionPane.showInputDialog("输入缩放倍数(小于1缩小，大于1放大):");
        		double times =Double.parseDouble(inputValue);;
        		img = Common.toBufferedImage(oImage);
    		    img =common.scale(img, iw, ih, times, 2);
    		    int iw2 = img.getWidth();
    		    int ih2 = img.getHeight();
    		    draw h = new draw("缩放"+times+"倍",iw2,ih2,graph,img);
     		   sImage = img;
    	        runflag = true;		  
    	      
    	        
        	}
        	else
    		 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //复原
        else if (evt.getSource() == recovery) {
        	if(loadflag){
        		
        		oImage = iImage;
        		sImage = oImage;
        		iw = iImage.getWidth(null);
        		ih = iImage.getHeight(null);
        	
        		 common.draw(graph,oImage, "原图");
        		
        	}
        	else JOptionPane.showMessageDialog(null, "未打开图像");
        	}
        
        else if (evt.getSource() == histItem)
        {        	
        	
        	if(loadflag)
        	{        		
        		pixels = common.grabber(iImage, iw, ih);
	            
				//显示图像的直方图
				Hist h = new Hist("直方图");
				
				img = common.toBufferedImage(iImage);
				//传送数据
				h.getData(pixels,iw,ih,img);
				h.setLocation(300, 200);								
			}
        	else JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        
        
        //平滑  3x3近邻
        else if (evt.getSource() == pinghua1)
        {        	
        	
        	if(loadflag)
        	{        		
        		pixels = common.grabber(oImage, iw, ih);	
        		
        		
					try {
						pix= common.pinghua(pixels, iw, ih);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

        		ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage,"平滑");
		        sImage = oImage;
        		
        		
        		
			}
        	else JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        
        //3x3　超限像素平滑
        else if (evt.getSource() == pinghua2)
        {        	
        	
        	if(loadflag)
        	{        		
        		pixels = common.grabber(oImage, iw, ih);	
        		
        		
					try {
						pix= common.pinghua2(pixels, iw, ih);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

        		ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage,"平滑");
		        sImage = oImage;
        		
        		
        		
			}
        	else JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        
        
        
        else if (evt.getSource() == ruihua1)
        {        	
        	if(loadflag)        	
        	{
 
        		pixels = common.grabber(oImage, iw, ih);							
        		   pixels = common.krilpls(pixels, iw, ih,1, 
      		             1, false);
        	   
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "Krisch锐化");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        else if (evt.getSource() == ruihua2)
        {        	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);							
     		    pixels = common.krilpls(pixels, iw, ih,2, 
   		             1, false);
     	   
     		   ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph,oImage, "拉普拉斯锐化");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        //3x3　超限像素平滑
        else if (evt.getSource() == togray)
        {        	
        	
        	if(loadflag)
        	{        		
        		pixels = common.grabber(oImage, iw, ih);	
        		pixels = common.toGray(pixels, iw, ih);

        		ImageProducer ip = new MemoryImageSource(iw, ih,pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage,"平滑");
		        sImage = oImage;
        		
        		
        		
			}
        	else JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        
        else if (evt.getSource() == lplsbianyuan)
        {        	
        	if(loadflag)        	
        	{
        		String inputValue = JOptionPane.showInputDialog("输入阈值:");
           		int thresh = Integer.parseInt(inputValue);
        		pixels = common.grabber(oImage, iw, ih);							
        		pixels = common.krilpls(pixels, iw, ih,2,thresh, true);
        	  
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "拉普拉斯边缘检测");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        
        else if (evt.getSource() == kirsch)
        {        	
        	if(loadflag)        	
        	{
        		String inputValue = JOptionPane.showInputDialog("输入阈值:");
           		int thresh = Integer.parseInt(inputValue);
        		pixels = common.grabber(oImage, iw, ih);							
        		   pixels = common.krilpls(pixels, iw, ih,1, thresh, true);
        	   
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "Kirsch缘检测");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //细化
        else if (evt.getSource() == xihua)
        {        	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(oImage, iw, ih);						
				byte[] imb = common.toBinary(pixels, iw, ih);
				imb = common.xihua(imb, iw, ih);
				
				pixels = common.bin2Rgb(imb, iw, ih);
        	   
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "细化");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //直线检测
        else if (evt.getSource() == zhixianjc)
        {        	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(iImage, iw, ih);						
				byte[] imb = common.toBinary(pixels, iw, ih);
				 imb = common.zhixianjc(imb, iw, ih);				
				pixels = common.bin2Rgb(imb, iw, ih);	      
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "细化");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //圆检测
        else if (evt.getSource() == yuanjc)
        {        	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(iImage, iw, ih);						
				byte[] imb = common.toBinary(pixels, iw, ih);
				 imb = common.yuanjc(imb, iw, ih);				
				pixels = common.bin2Rgb(imb, iw, ih);	      
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "细化");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //霍夫曼
        else if (evt.getSource() == hfm)
        {        	
        	if(loadflag)        	
        	{
        		img = Common.toBufferedImage(oImage);
        		pixels = common.grabber(img, iw, ih);						
				common.Huffman(img, iw, ih, pixels);
		       
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //光栅检测
        else if (evt.getSource() == guangshan)
        {        	
        	if(loadflag)        	
        	{
        		String inputValue = JOptionPane.showInputDialog("检测阈值和跟踪阈值0~255(空格分隔):");
           		String[] temp = inputValue.split(" ");
           		
           		int d = Integer.parseInt(temp[0]); //检测阈值
           		int e = Integer.parseInt(temp[1]); //跟踪阈值
           		
        		pixels = common.grabber(iImage, iw, ih);						
				int[] pixels2 = common.grabber(oImage, iw, ih);
				
				 pixels = common.guangshan(pixels, pixels2, iw, ih, d, e);
				 
        		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
  		        oImage = createImage(ip);
		        common.draw(graph,oImage, "光栅扫描跟踪闭合");
		        sImage = oImage;
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        //保存图片
        else if (evt.getSource() == saveItem) {
        	if(loadflag){
        		int iww = sImage.getWidth(null);
        		int ihh = sImage.getHeight(null);
        		BufferedImage savepic = new BufferedImage(iww,ihh, BufferedImage.TYPE_3BYTE_BGR);
            	JFileChooser chooser = new JFileChooser();
            	FileFilter filter = new FileNameExtensionFilter("JPEG", "jpg");
            	chooser.addChoosableFileFilter(filter);
            	chooser.setFileFilter(filter); 
               
            	
                common.chooseFile(chooser, "./images", 0);		//设置默认访问路径
                chooser.setDialogType(JFileChooser.SAVE_DIALOG); // 保存
                chooser.setApproveButtonText("保存");
                chooser.showSaveDialog(null);  
                String path = chooser.getSelectedFile().getPath();
                System.out.println(path); 

                    BufferedImage bi = new BufferedImage(iww,ihh, BufferedImage.TYPE_3BYTE_BGR);

                    Graphics g = bi.getGraphics(); 
                    try { 
                        g.drawImage(sImage, 0, 0, null); 
                        ImageIO.write(bi,"jpg",new File(path+".jpg")); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
        		
        	}
        	else JOptionPane.showMessageDialog(null, "未打开图像");
        	
            
     
        }
        
                        
    }
    
    
  
    public void paint(Graphics g) 
    {    
    	
        if (iImage != null && (!runflag))
        {        
        	g.clearRect(0,0,700, 500);        	          
            g.drawImage(iImage, 50, 100, null);
           if(bmp.compareTo("bmp")==0){
           	g.drawString("Java不支持BMP格式图片显示", 20, 90);
          	g.drawString("请直接点击   第三章作业->颜色数与颜色值  查看所求图片信息", 20, 110);
            }
            else g.drawString("原图", 20, 90);
        }        
    }
    
    public void showPix(Graphics graph, int[] pixels, String str)
    {    
		//将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		double times = 0.7;
		if(iw>ih)
		common.draw(graph, oImage, str);
		else 
			common.drawsuoxiao(graph,oImage,"旋转180度",iw,ih,times);
		runflag = true;
	}
    
    
	
    public static void main(String[] args) 
    {  
        new Shuzituxiangchuli();        
    } 
    
    private void closeWin()
    {
    	addWindowListener(new WindowAdapter()
        {  
            public void windowClosing(WindowEvent e) 
            {  
                System.exit(0);
            }
        });
    }
    
    //菜单界面
    public void setMenu()
    {    	
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);
       
        fileMenu.addSeparator();  
        saveItem = new MenuItem("保存");
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);  
        
        fileMenu.addSeparator();  
        recovery = new MenuItem("复原");
        recovery.addActionListener(this);
        fileMenu.add(recovery);   
        
        
        
        
        processMenu = new Menu("采样级");   
        samplItem128= new MenuItem("128");
        processMenu.add(samplItem128);
        samplItem128.addActionListener(this);
        processMenu.addSeparator();  
        samplItem64 = new MenuItem("64");
        processMenu.add(samplItem64);
        samplItem64.addActionListener(this);
        processMenu.addSeparator();  
        samplItem32 = new MenuItem("32");
        processMenu.add(samplItem32);
        samplItem32.addActionListener(this);
        processMenu.addSeparator();  
        samplItem16 = new MenuItem("16");
        processMenu.add(samplItem16);
        samplItem16.addActionListener(this);
  
        
        processMenu2 = new Menu("量化"); 
        quanlIte128= new MenuItem("128");
        processMenu2.add(quanlIte128);
        quanlIte128.addActionListener(this);
        processMenu2.addSeparator();  
        quanlIte64= new MenuItem("64");
        processMenu2.add(quanlIte64);
        quanlIte64.addActionListener(this);
        processMenu2.addSeparator();  
        quanlIte32= new MenuItem("32");
        processMenu2.add(quanlIte32);
        quanlIte32.addActionListener(this);
        processMenu2.addSeparator();  
        quanlIte16= new MenuItem("16");
        processMenu2.add(quanlIte16);
        quanlIte16.addActionListener(this);
        processMenu2.addSeparator();  
        quanlIte8= new MenuItem("8");
        processMenu2.add(quanlIte8);
        quanlIte8.addActionListener(this);
        processMenu2.addSeparator();  
        quanlIte4= new MenuItem("4");
        processMenu2.add(quanlIte4);
        quanlIte4.addActionListener(this);
        processMenu2.addSeparator();  
        quanlIte2= new MenuItem("2");
        processMenu2.add(quanlIte2);
        quanlIte2.addActionListener(this);
       
        process = new Menu("2章");
        process.add(processMenu);
        process.addSeparator();
        process.add(processMenu2);
        
        Menu processMenu3 = new Menu("3章"); 
        outlineItem= new Menu("将8位灰度图转为8个位平面表示的二值图");
        processMenu3.add(outlineItem);
        outlineItem.addActionListener(this);
        
        baweitu1 = new MenuItem("1");
        outlineItem.add(baweitu1);
        baweitu1.addActionListener(this);
        outlineItem.addSeparator();  
        baweitu2 = new MenuItem("2");
        outlineItem.add(baweitu2);
        baweitu2.addActionListener(this);
        outlineItem.addSeparator();
        baweitu3 = new MenuItem("3");
        outlineItem.add(baweitu3);
        baweitu3.addActionListener(this);
        outlineItem.addSeparator();
        baweitu4 = new MenuItem("4");
        outlineItem.add(baweitu4);
        baweitu4.addActionListener(this);
        outlineItem.addSeparator();  
        baweitu5 = new MenuItem("5");
        outlineItem.add(baweitu5);
        baweitu5.addActionListener(this);
        outlineItem.addSeparator();
        baweitu6 = new MenuItem("6");
        outlineItem.add(baweitu6);
        baweitu6.addActionListener(this);
        outlineItem.addSeparator();
        baweitu7 = new MenuItem("7");
        outlineItem.add(baweitu7);
        baweitu7.addActionListener(this);
        outlineItem.addSeparator();
        baweitu8 = new MenuItem("8");
        outlineItem.add(baweitu8);
        baweitu8.addActionListener(this);
 
        processMenu3.addSeparator();  
        readasfile= new MenuItem("颜色数与颜色值（仅限BMP图像）");
        processMenu3.add( readasfile);
        readasfile.addActionListener(this);
        
        Menu processMenu4 = new Menu("4章"); 
        histItem = new MenuItem("原图直方图");
        processMenu4.add(histItem);
        histItem.addActionListener(this);
        processMenu4.addSeparator(); 
        histItem2 = new MenuItem("处理后的直方图");
        processMenu4.add(histItem2);
        histItem2.addActionListener(this);
        processMenu4.addSeparator(); 
        outlineItem2 = new MenuItem("二值化");
        processMenu4.add(outlineItem2);
        outlineItem2.addActionListener(this);
        
        Menu processMenu5 = new Menu("5章 "); 
        junhenghua = new MenuItem("直方图均衡化");
        processMenu5.add(junhenghua);
        junhenghua.addActionListener(this);
        processMenu5.addSeparator(); 
        
        linearItem= new MenuItem("线性变化");        
        processMenu5.add(linearItem);
        linearItem.addActionListener(this);
       
        Menu processMenu6 = new Menu("6章");
        Menu  Rotate = new Menu("旋转");
        youzhuan180 = new MenuItem("旋转180度");
        Rotate.add(youzhuan180);
        youzhuan180.addActionListener(this);
        Rotate.addSeparator();
        youzhuan90 = new MenuItem("旋转90度");
        Rotate.add(youzhuan90);
        youzhuan90.addActionListener(this);
       
      

        
        processMenu6.add(Rotate);
        processMenu6.addSeparator();
        zoom = new Menu("缩小/放大");
        biliner = new MenuItem("双线性");
        biliner.addActionListener(this);
        zoom.add(biliner);
        zoom.addSeparator();
        neighborhood = new MenuItem("近邻");
        neighborhood.addActionListener(this);
        zoom.add(neighborhood);
        processMenu6.add(zoom);
      
        Menu processMenu7 = new Menu("9章");
       
        pinghua = new Menu("平滑");
        
        pinghua1 = new MenuItem("3x3 邻域平均法");
        pinghua1.addActionListener(this);
        pinghua.add(pinghua1);
        pinghua.addSeparator();
        pinghua2 = new MenuItem("3x3 超限像素平滑法(T=64)");
        pinghua2.addActionListener(this);
        pinghua.add(pinghua2);
        
        processMenu7.add(pinghua);
        processMenu7 .addSeparator();
       
        ruihua = new Menu("锐化");
        
        ruihua1 = new MenuItem("Krisch锐化");
        ruihua.add(ruihua1);
        ruihua1.addActionListener(this);
        ruihua.addSeparator();
        
        ruihua2= new MenuItem("拉普拉斯锐化");
        ruihua.add(ruihua2);
        ruihua2.addActionListener(this);
        
        Menu processMenu8 = new Menu("11章 ");
        togray = new MenuItem("彩色图像灰度化");
        togray.addActionListener(this);
        processMenu8.add(togray);
        
        Menu processMenu9 = new Menu("12章 ");
        lplsbianyuan = new MenuItem("拉普拉斯算子边缘检测");
        lplsbianyuan.addActionListener(this);
        processMenu9.add(lplsbianyuan);
        processMenu9.addSeparator();
        kirsch = new MenuItem("Kirsch方向算子边缘检测");
        kirsch.addActionListener(this);
        processMenu9.add(kirsch);
        processMenu9.addSeparator();
        guangshan = new MenuItem("光栅扫描跟踪法");
        guangshan.addActionListener(this);
        processMenu9.add(guangshan);
        
        Menu processMenu10 = new Menu("13章 ");
        xihua = new MenuItem("细化");
        xihua.addActionListener(this);
        processMenu10.add(xihua);
        processMenu10.addSeparator();
        huofu = new Menu("霍夫变化");
        zhixianjc = new MenuItem("直线检测");
        zhixianjc.addActionListener(this);
        huofu.add(zhixianjc);
        huofu.addSeparator();
        yuanjc = new MenuItem("圆检测");
        yuanjc.addActionListener(this);
        huofu.add(yuanjc);
        huofu.addActionListener(this);
        processMenu10.add(huofu);
        
        Menu processMenu11 = new Menu("14章 ");
        hfm = new MenuItem("霍夫曼编码");
        hfm.addActionListener(this);
        processMenu11.add(hfm);
        
        
        
        processMenu7 .add(ruihua);
        ruihua.addActionListener(this);
       
        
        MenuBar menuBar = new MenuBar();
        menuBar.setFont(getFont());
        menuBar.add(fileMenu);
        menuBar.add(process);
        menuBar.add(processMenu3); 
        menuBar.add(processMenu4); 
        menuBar.add(processMenu5); 
        menuBar.add(processMenu6);
        menuBar.add(processMenu7);
        menuBar.add(processMenu8);
        menuBar.add(processMenu9);
        menuBar.add(processMenu10);
        menuBar.add(processMenu11);
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem saveItem;
    MenuItem recovery;
    MenuItem samplItem;
    MenuItem quantItem; 
    MenuItem samplItem128;
    MenuItem samplItem64;
    MenuItem samplItem32;
    MenuItem samplItem16;
    MenuItem quanlIte128;
    MenuItem quanlIte64;
    MenuItem quanlIte32;
    MenuItem quanlIte16;
    MenuItem quanlIte8;
    MenuItem quanlIte4;
    MenuItem quanlIte2;
    Menu outlineItem;
    MenuItem outlineItem2;
    MenuItem histItem;
    MenuItem histItem2;
    MenuItem  readasfile;
    MenuItem  baweitu1;
    MenuItem  baweitu2;
    MenuItem  baweitu3;
    MenuItem  baweitu4;
    MenuItem  baweitu5;
    MenuItem  baweitu6;
    MenuItem  baweitu7;
    MenuItem  baweitu8;
    MenuItem junhenghua;
    Menu process;
    Menu processMenu;
    Menu processMenu2;
    MenuItem  linearItem;
    MenuItem  youzhuan180;
    MenuItem  youzhuan90;
    Menu  zoom;
    MenuItem  biliner;
    MenuItem  neighborhood;
    Menu pinghua;
    Menu ruihua;
    MenuItem ruihua1;
    MenuItem ruihua2;
    MenuItem pinghua1;
    MenuItem pinghua2; 
    MenuItem togray;
    MenuItem lplsbianyuan;
    MenuItem kirsch;
    MenuItem xihua;
    Menu huofu;
    MenuItem zhixianjc;
    MenuItem yuanjc;
    MenuItem hfm;
    MenuItem guangshan;
    
    
    
}
