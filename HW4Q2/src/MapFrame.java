import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MapFrame extends JFrame{
	private static List<Integer> list = new ArrayList<Integer>();
	private JPanel mapJPanel;			//地圖JPanel
	private BloodPanel bloodJPanel;			//血量圖JPanel
	private static int bloodValue=100;	//血量
	private JLabel brickLabel;
	private JLabel heartLabel;
	private JLabel diamondLabel;
	private JLabel roadLabel;
	
	public MapFrame(){	//constructor
		super("電流急急棒");
		setLayout(new BorderLayout());
		
		// 血量圖JPanel //
		bloodJPanel = new BloodPanel();
		bloodJPanel.setPreferredSize(new Dimension(550, 50));
		add(bloodJPanel, BorderLayout.NORTH);
		
		// 地圖JPanel //
		mapJPanel = new JPanel();
		mapJPanel.setLayout(new GridLayout(10,10));
		
		// 匯入map.txt檔，得一個List<int> //
		list = ReadMapFile.ReadFromFile();
		//System.out.print(list);
		
		// 圖片 //
		Icon brick = changeSize("brickwall.png");
		Icon heart = changeSize("heart.png");
		Icon diamond = changeSize("diamond.png");
		diamondLabel = new JLabel(diamond);
		diamondLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent event){
				JOptionPane.showMessageDialog(null, "成功拿到鑽石", "恭喜", JOptionPane.PLAIN_MESSAGE);
				dispose();
			}
		});
		
		// 依照list內容轉換製成地圖 //
		for(int i:list){
			switch(i){
			case 0:
				Random random = new Random();
				if(random.nextInt()%6==0){
					heartLabel = new JLabel(heart);
					heartLabel.addMouseListener(new HeartHandler());
					mapJPanel.add(heartLabel);
				}
				else{
					roadLabel = new JLabel();
					roadLabel.addMouseListener(new RoadHandler());
					mapJPanel.add(roadLabel);
				}
				break;
			case 1:
				brickLabel = new JLabel(brick);
				brickLabel.addMouseListener(new BrickHandler());
				mapJPanel.add(brickLabel);
				break;
			case 2:
				mapJPanel.add(diamondLabel);
				break;
			}
		}
		add(mapJPanel, BorderLayout.CENTER);
	}
	
	public Icon changeSize(String str){	//更改圖片大小
		ImageIcon imgIcon = new ImageIcon(getClass().getResource(str));
		Image img = imgIcon.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		Icon icon = new ImageIcon(img);
		return icon;
	}
	
	private void draw(){		//重畫血量圖
		bloodJPanel.setValue(bloodValue);
		Graphics g = bloodJPanel.getGraphics();
		g.setColor(Color.BLUE);
		g.drawRect(15, 20, 500, 10);
		g.fillRect(15, 20, bloodValue*5, 10);
		repaint();
	}
	
	private class HeartHandler extends MouseAdapter{
		@Override
		public void mouseEntered(MouseEvent event){
			bloodValue += 5;
			if(bloodValue>100)	//若血量家超過100皆視為100
				bloodValue = 100;
			//System.out.printf("get heart %d\n", bloodValue);
			draw();
		}
	}
	
	private class RoadHandler extends MouseAdapter{
		@Override
		public void mouseEntered(MouseEvent event){
			bloodValue -= 5;
			//System.out.printf("road %d\n",bloodValue);
			draw();
			if(noBlood()){
				JOptionPane.showMessageDialog(null, "可惜沒血了QQ", "失敗", JOptionPane.PLAIN_MESSAGE);
				dispose();
			}
		}
	}
	
	private class BrickHandler extends MouseAdapter{
		@Override
		public void mouseEntered(MouseEvent event){
			bloodValue -= 20;
			//System.out.printf("brick %d\n",bloodValue);
			draw();
			if(noBlood()){
				JOptionPane.showMessageDialog(null, "沒血了啦，下次再加油QQ", "失敗", JOptionPane.PLAIN_MESSAGE);
				dispose();	//關閉視窗
			}
		}
	}
	
	private boolean noBlood(){	//確認還有沒有血(是否小於0)
		if(bloodValue<0)
			return true;
		else
			return false;
	}
}
