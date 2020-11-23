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
	private JPanel mapJPanel;			//�a��JPanel
	private BloodPanel bloodJPanel;			//��q��JPanel
	private static int bloodValue=100;	//��q
	private JLabel brickLabel;
	private JLabel heartLabel;
	private JLabel diamondLabel;
	private JLabel roadLabel;
	
	public MapFrame(){	//constructor
		super("�q�y����");
		setLayout(new BorderLayout());
		
		// ��q��JPanel //
		bloodJPanel = new BloodPanel();
		bloodJPanel.setPreferredSize(new Dimension(550, 50));
		add(bloodJPanel, BorderLayout.NORTH);
		
		// �a��JPanel //
		mapJPanel = new JPanel();
		mapJPanel.setLayout(new GridLayout(10,10));
		
		// �פJmap.txt�ɡA�o�@��List<int> //
		list = ReadMapFile.ReadFromFile();
		//System.out.print(list);
		
		// �Ϥ� //
		Icon brick = changeSize("brickwall.png");
		Icon heart = changeSize("heart.png");
		Icon diamond = changeSize("diamond.png");
		diamondLabel = new JLabel(diamond);
		diamondLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent event){
				JOptionPane.showMessageDialog(null, "���\�����p��", "����", JOptionPane.PLAIN_MESSAGE);
				dispose();
			}
		});
		
		// �̷�list���e�ഫ�s���a�� //
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
	
	public Icon changeSize(String str){	//���Ϥ��j�p
		ImageIcon imgIcon = new ImageIcon(getClass().getResource(str));
		Image img = imgIcon.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		Icon icon = new ImageIcon(img);
		return icon;
	}
	
	private void draw(){		//���e��q��
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
			if(bloodValue>100)	//�Y��q�a�W�L100�ҵ���100
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
				JOptionPane.showMessageDialog(null, "�i���S��FQQ", "����", JOptionPane.PLAIN_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "�S��F�աA�U���A�[�oQQ", "����", JOptionPane.PLAIN_MESSAGE);
				dispose();	//��������
			}
		}
	}
	
	private boolean noBlood(){	//�T�{�٦��S����(�O�_�p��0)
		if(bloodValue<0)
			return true;
		else
			return false;
	}
}