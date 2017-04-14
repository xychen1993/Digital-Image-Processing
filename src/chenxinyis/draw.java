package chenxinyis;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class draw extends Frame implements ActionListener{
	
	Image om;
	int iww;
	int ihh;
	String str;
	
	public draw(String str,int iw, int ih,Graphics g, Image om)
	{
		setTitle(str);
		
		Panel pdown = new Panel();
		

		//关闭窗口
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});	
		setSize(iw+100,ih+100);
		this.om = om;
		this.iww=iw;
		this.ihh = ih;
		this.str = str;
		
		setVisible(true);
		
		
		
		
	}
	
	public void paint(Graphics g){
		g.clearRect(0, 0, iww, ihh);
		g.drawImage(om,50,50, iww,ihh, null);
   	    g.drawString(str, 20,15);    	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		
	}

}
