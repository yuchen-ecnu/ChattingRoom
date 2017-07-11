package qqdefaultface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import frame.ChatFrame;

public class PicsJWindow extends JWindow {
	private static final long serialVersionUID = 1L;
	GridLayout gridLayout1 = new GridLayout(7, 15);//设置为7*15的网格布局
	JLabel[] ico = new JLabel[105]; //存储所有表情的数组
	int i;
	ChatFrame owner;
	String[] intro = new String[105];

	public PicsJWindow(ChatFrame owner) {
		super(owner);
		this.owner = owner;
		try {
			init();
			this.setAlwaysOnTop(true);//永远置顶
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void init() throws Exception {
		this.setPreferredSize(new Dimension(28 * 15, 28 * 7));//每个表情为28*28，横排15个，竖排7个，共105个表情
		JPanel p = new JPanel();
		p.setOpaque(true);		//设置显示表情的控件不透明
		this.setContentPane(p);
		p.setLayout(gridLayout1);
		p.setBackground(SystemColor.text);
		String fileName = "";
		for (i = 0; i < ico.length; i++) {
			fileName = "qqdefaultface/" + i + ".gif";//读取表情路径 

			//
			ico[i] = new JLabel(new ChatPic(PicsJWindow.class.getResource(fileName), i), SwingConstants.CENTER);//表情初始化居中
			ico[i].setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));	//设置边框线
			ico[i].setToolTipText("/"+i);//设置表情提示语
			ico[i].addMouseListener(new MouseAdapter() {//为每个表情设置点击监听
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 1) {
						JLabel cubl = (JLabel) (e.getSource());
						ChatPic cupic = (ChatPic) (cubl.getIcon());//获取点击的表情（ChatPic）
						owner.insertSendPic(cupic);//向聊天窗口插入表情
						getObj().dispose();//隐藏表情窗口
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {//鼠标选中时将边框置为蓝色
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}

				@Override
				public void mouseExited(MouseEvent e) {//鼠标移开时置为原样
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
				}

			});
			p.add(ico[i]);//将按钮放入表情框
		}
		//鼠标离开表情框时隐藏框体
		p.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				getObj().dispose();
			}

		});
	}

	@Override
	public void setVisible(boolean show) {
		if (show) {
			determineAndSetLocation();//调整表情框的位置，使其置于聊天信息框的上方
		}
		super.setVisible(show);
	}

	private void determineAndSetLocation() {
		Point loc = owner.getPicBtn().getLocationOnScreen();//获取插入图片按钮相对于屏幕的位置
		setBounds(loc.x - getPreferredSize().width / 3, loc.y - getPreferredSize().height, getPreferredSize().width,
				getPreferredSize().height);//调整位置
	}

	//获取当前窗体
	private JWindow getObj() {
		return this;
	}
}
