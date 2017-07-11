package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import common.CoolToolTip;
import common.MsgType;
import entity.FontAndText;
import entity.PicInfo;
import qqdefaultface.ChatPic;
import qqdefaultface.PicsJWindow;
import sender.Sender;

import javax.swing.JScrollPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;

public class ChatFrame extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;
	// 声明控件
	private JPanel contentPane;
	private JPanel tools;
	private JPanel format;
	private JPanel inputControl;
	private JTextPane jpMsg;
	private JTextPane jpChat;
	private JScrollPane jspChat;
	private JScrollPane jspChat_1;
	private JScrollPane jspMsg;
	private JComboBox fontName;
	private JComboBox fontSize;
	private JComboBox fontColor;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JButton b_pic;
	private JButton msgFormat;
	private JButton b_shake;
	private JButton b_remove;
	private JButton btnSend;

	// 声明自有变量
	private StyledDocument docChat;// 聊天字体
	private StyledDocument docMsg;// 消息字体

	private static final Color TIP_COLOR = new Color(255, 255, 225);// 消息提示颜色

	private CoolToolTip error_tip;// 错误信息提示

	private PicsJWindow picWindow;// 表情选择框
	private List<PicInfo> myPicInfo = new LinkedList<PicInfo>();// 存储将要发送消息的图片信息
	private List<PicInfo> receivdPicInfo = new LinkedList<PicInfo>();// 存储接收到的图片信息
	private DefaultListModel<String> online = new DefaultListModel<>();
	
	// 构造函数
	public ChatFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle(Sender.uname);
		setResizable(false);
		setBounds(100, 100, 928, 641);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tools = new JPanel();
		tools.setBounds(0, 374, 698, 39);
		contentPane.add(tools);
		tools.setLayout(null);

		b_pic = new JButton("");
		b_pic.setToolTipText("\u8868\u60C5");
		b_pic.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/pic.png")));
		b_pic.setActionCommand("");
		b_pic.setBackground(new Color(240, 240, 240));
		b_pic.setBorderPainted(false);
		b_pic.addMouseListener(this);

		jspChat = new JScrollPane();
		jspChat.setBounds(0, 0, 686, 315);

		b_pic.setBounds(70, 8, 42, 28);
		tools.add(b_pic);
		contentPane.setLayout(null);

		format = new JPanel();
		format.setBounds(0, 335, 698, 39);
		contentPane.add(format);
		format.setLayout(null);
		format.setVisible(false);
		getLayeredPane().add(format, new Integer(Integer.MAX_VALUE));

		fontName = new JComboBox();
		fontName.setModel(new DefaultComboBoxModel(new String[] { "\u5B8B\u4F53", "\u9ED1\u4F53", "Dialog", "Gulim" }));
		fontName.setBounds(92, 7, 116, 26);
		format.add(fontName);

		label = new JLabel("\u5B57\u4F53\uFF1A");
		label.setBounds(23, 11, 55, 22);
		format.add(label);

		fontSize = new JComboBox();
		fontSize.setModel(new DefaultComboBoxModel(new String[] { "12", "14", "18", "22", "30", "40" }));
		fontSize.setSelectedIndex(2);
		fontSize.setBounds(315, 7, 116, 26);
		format.add(fontSize);

		fontColor = new JComboBox();
		fontColor.setModel(new DefaultComboBoxModel(
				new String[] { "\u9ED1\u8272", "\u7EA2\u8272", "\u84DD\u8272", "\u9EC4\u8272", "\u7EFF\u8272" }));
		fontColor.setBounds(556, 7, 116, 26);
		format.add(fontColor);

		label_1 = new JLabel("\u5B57\u53F7\uFF1A");
		label_1.setBounds(256, 11, 45, 18);
		format.add(label_1);

		label_2 = new JLabel("\u989C\u8272\uFF1A");
		label_2.setBounds(487, 11, 55, 18);
		format.add(label_2);

		b_shake = new JButton("");
		b_shake.setToolTipText("\u9707\u52A8");
		b_shake.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/shake.png")));
		b_shake.setBorderPainted(false);
		b_shake.addMouseListener(this);
		b_shake.setBackground(new Color(240, 240, 240));
		b_shake.setBounds(126, 8, 42, 28);
		tools.add(b_shake);

		msgFormat = new JButton("");
		msgFormat.setToolTipText("\u5B57\u4F53");
		msgFormat.setBounds(14, 8, 42, 28);
		msgFormat.setBorderPainted(false);
		tools.add(msgFormat);
		msgFormat.setBackground(new Color(240, 240, 240));
		msgFormat.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/font2.png")));
		msgFormat.addMouseListener(this);

		inputControl = new JPanel();
		inputControl.setBounds(0, 560, 698, 46);
		contentPane.add(inputControl);
		inputControl.setLayout(null);

		b_remove = new JButton("");
		b_remove.setBorderPainted(false);
		b_remove.setToolTipText("\u6E05\u9664");
		b_remove.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/clear_2.png")));
		b_remove.addMouseListener(this);
		b_remove.setBackground(new Color(240, 240, 240));
		b_remove.setBounds(522, 5, 72, 37);
		inputControl.add(b_remove);

		btnSend = new JButton("");
		btnSend.setBorderPainted(false);
		btnSend.setToolTipText("\u53D1\u9001");
		btnSend.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/send.png")));
		btnSend.addMouseListener(this);
		btnSend.setBounds(595, 5, 77, 37);
		btnSend.setBackground(new Color(240, 240, 240));
		inputControl.add(btnSend);

		setUIFont(new FontUIResource("宋体", Font.PLAIN, 15));// 调整整体字体

		this.addWindowListener(new WindowAdapter() {// 设置关闭窗体监听
			@Override
			public void windowClosing(WindowEvent e) {
				int flag = JOptionPane.showConfirmDialog(null, "确认退出程序？");
				if (flag == 0) {
					Sender.sendUDPMsg(MsgType.OFFLINE, Sender.uname, Sender.serverIP, Sender.ServerPort, "", "server");
					byte[] bytes = new byte[1024 * 128];
					DatagramPacket p = new DatagramPacket(bytes, bytes.length);
					System.exit(0);
				}
			}
		});

		this.addComponentListener(new ComponentAdapter() {// 当移动、resize、隐藏窗体时将表情框隐藏

			@Override
			public void componentResized(ComponentEvent e) {
				ChatFrame.this.picWindow.dispose();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				ChatFrame.this.picWindow.dispose();
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				ChatFrame.this.picWindow.dispose();
			}

		});

		chatContain = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				ImageIcon icon = new ImageIcon(ChatFrame.class.getResource("/backGround/chat2.jpg"));
				Image img = icon.getImage();
				g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());
			}
		};
		chatContain.setBounds(0, 0, 698, 374);
		contentPane.add(chatContain);
		chatContain.setLayout(null);
		jspChat_1 = new JScrollPane(jpChat, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jspChat_1.setOpaque(false);
		jspChat_1.setBounds(0, 0, 698, 374);
		chatContain.add(jspChat_1);
		jspChat_1.getViewport().setOpaque(false);

		jpChat = new JTextPane();
		jpChat.setOpaque(false);
		jpChat.setEditable(false);
		// jpChat.setOpaque(false);
		jspChat_1.setViewportView(jpChat);

		MsgContain = new JPanel();
		MsgContain.setBounds(0, 414, 698, 147);
		contentPane.add(MsgContain);
		MsgContain.setLayout(null);

		jspMsg = new JScrollPane();
		jspMsg.setBounds(0, 0, 698, 147);
		MsgContain.add(jspMsg);

		jpMsg = new JTextPane();
		jpMsg.setFocusTraversalPolicyProvider(true);
		// jpMsg.setOpaque(false);
		jspMsg.setViewportView(jpMsg);
		jpMsg.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					sendMsg();
					e.consume();
				}
			}
		});
		error_tip = new CoolToolTip(this, jspMsg, TIP_COLOR, 3, 10);
		
		panel = new JPanel();
		panel.setBounds(702, 0, 220, 606);
		contentPane.add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 37, 220, 569);
		panel.add(scrollPane);
		
		online_list = new JList();
		online_list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				String user = (String) online_list.getSelectedValue();
				if(user==null)return;
				try {
					if(docMsg.getText(0, docMsg.getLength()).contains("@@" + user + ";")){
						return;
					}
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					docMsg.insertString(0, "@@" + user + ";" , null );
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		scrollPane.setViewportView(online_list);
		
		label_3 = new JLabel("\u5728\u7EBF\u7528\u6237\u5217\u8868\uFF1A");
		label_3.setBounds(0, 6, 114, 28);
		panel.add(label_3);
		
		reflesh_bth = new JButton("");
		reflesh_bth.addMouseListener(this);
		reflesh_bth.setToolTipText("\u5237\u65B0");
		reflesh_bth.setBackground(Color.WHITE);
		reflesh_bth.setBorderPainted(false);
		reflesh_bth.setBorder(new EmptyBorder(0, 0, 0, 0));
		reflesh_bth.setOpaque(false);
		reflesh_bth.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/reflesh.png")));
		reflesh_bth.setBounds(171, 7, 49, 27);
		panel.add(reflesh_bth);
		docMsg = jpMsg.getStyledDocument();

		// 实例化自有变量
		docChat = jpChat.getStyledDocument();
		docChat = jpChat.getStyledDocument();
		picWindow = new PicsJWindow(this);
		myPicInfo = new LinkedList<PicInfo>();
		receivdPicInfo = new LinkedList<PicInfo>();

		// 创建接受信息线程
		new receivMsgThread().start();
		
		//请求在线用户列表
		//Sender.sendUDPMsg(MsgType.LIST_REFLEASH, Sender.uname, Sender.serverIP, Sender.ServerPort, "ask for userlist", "@server");
	}

	public JButton getPicBtn() {
		return b_pic;
	}

	// 发送震动消息
	public void sendShake() {
		if (!Sender.sendUDPMsg(MsgType.SHAKE, Sender.uname, Sender.serverIP, Sender.ServerPort, "shake", "@all")) {
			error_tip.setText("发送失败！");
			error_tip.setVisible(true);
		}
		insert("你发送了一个震动！");
	}

	// 抖动窗口逻辑
	public void shake(String uname) {
		setExtendedState(Frame.NORMAL);
		setVisible(true);
		if(!uname.equals(Sender.uname)) insert(uname + " 给你发了一个震动！");
		// 新建一个线程用于窗口抖动
		new Thread() {
			public void run() {
				int before_x = ChatFrame.this.getX();
				int before_y = ChatFrame.this.getY();
				int x = before_x;
				int y = before_y;
				int dir = 1;
				for (int i = 0; i < 30; i++) {
					if (dir == 0) {
						x = before_x - 5;
						y = before_y + 5;
						dir = 1;
					} else {
						x = before_x + 5;
						y = before_y - 5;
						dir = 0;
					}
					ChatFrame.this.setLocation(x, y);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ChatFrame.this.setLocation(before_x, before_y);
			}
		}.start();
	}

	// 发送消息
	private FontAndText myFont = null;

	public void sendMsg() {
		String message = jpMsg.getText();
		if (message.length() == 0) {
			error_tip.setText("请输入聊天信息！");
			error_tip.setVisible(true);
			return;
		}
		if (message.length() > 100) {
			error_tip.setText("消息最多一百个字符！你要发送的为" + message.length() + "个字符！");
			error_tip.setVisible(true);
			return;
		}
		
		String toSend = resolve(message);
		if(toSend=="")toSend =  "@all";
		myFont = getFontAttrib();
		boolean flag  = Sender.sendUDPMsg(MsgType.CHAT, Sender.uname, Sender.serverIP, Sender.ServerPort, myFont.toString(),
				toSend);
		if (flag) {
			addMeg(Sender.uname);
			this.jpMsg.setText("");
		} else {
			error_tip.setText("发送消息失败！");
			error_tip.setVisible(true);
		}
	}

	// 在消息窗口添加消息
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	FontAndText dateFont = new FontAndText("", "宋体", 17, new Color(139, 139, 172));

	// 添加本地消息
	public void addMeg(String uname) {
		String msg = uname + " " + sf.format(new Date());
		dateFont = new FontAndText("", "宋体", 17, Color.blue);
		dateFont.setText(msg);
		insert(dateFont);// 按照指定格式进行插入（插入时间和用户信息）
		dateFont = new FontAndText("", "宋体", 17, new Color(139, 139, 172));
		pos2 = jpChat.getCaretPosition();
		myFont.setText(jpMsg.getText());
		insert(myFont);// 按照指定格式进行插入（消息内容）
		insertPics(false);// 添加本地表情
		insert("");
		// myFont.setAlign("left");
	}

	// 添加接收消息
	public void addRecMsg(String uname, String message) {
		setExtendedState(Frame.NORMAL);
		String msg = uname + " " + sf.format(new Date());
		dateFont.setText(msg);
		insert(dateFont);// 插入时间和用户信息
		int index = message.lastIndexOf("*");

		// 更新插入聊天信息的位置
		pos1 = jpChat.getCaretPosition();
		if (index > 0 && index < message.length() - 1) { // 存在表情信息
			FontAndText attr = getRecivedFont(message.substring(0, index));
			insert(attr);
			receivedPicInfo(message.substring(index + 1, message.length()));
			insertPics(true);
		} else {
			FontAndText attr = getRecivedFont(message);
			insert(attr);
		}
		insert("");
	}

	// 将收到的消息进行转化（转化为自定义的FontAndText）
	public FontAndText getRecivedFont(String message) {
		String[] msgs = message.split("[|]");
		String fontName = "";
		int fontSize = 0;
		String[] color;
		String text = message;
		Color fontC = new Color(222, 222, 222);
		if (msgs.length >= 4) {
			fontName = msgs[0];
			fontSize = Integer.parseInt(msgs[1]);
			color = msgs[2].split("[-]");
			if (color.length == 3) {
				int r = Integer.parseInt(color[0]);
				int g = Integer.parseInt(color[1]);
				int b = Integer.parseInt(color[2]);
				fontC = new Color(r, g, b);
			}
			text = "";
			for (int i = 3; i < msgs.length; i++) {
				text = text + msgs[i];
			}
		}
		FontAndText attr = new FontAndText();

		attr.setName(fontName);
		attr.setSize(fontSize);
		attr.setColor(fontC);

		attr.setText(text.replace(" ", ""));

		return attr;
	}

	// 向聊天信息窗添加表情
	int pos1;
	int pos2;
	private JPanel chatContain;
	private JPanel MsgContain;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JList online_list;
	private JLabel label_3;
	private JButton reflesh_bth;

	private void insertPics(boolean isFriend) {

		if (isFriend) {
			if (this.receivdPicInfo.size() <= 0) {
				return;
			} else {
				for (int i = 0; i < receivdPicInfo.size(); i++) {
					PicInfo pic = receivdPicInfo.get(i);
					String fileName;
					jpChat.setCaretPosition(pos1 + pic.getPos()); // 设置插入位置
					fileName = "qqdefaultface/" + pic.getVal() + ".gif";// 修改图片路径
					jpChat.insertIcon(new ImageIcon(PicsJWindow.class.getResource(fileName))); // 插入图片
				}
				receivdPicInfo.clear();// 插入完成后清空表情数组
			}
		} else {

			if (myPicInfo.size() <= 0) {
				return;
			} else {
				for (int i = 0; i < myPicInfo.size(); i++) {
					PicInfo pic = myPicInfo.get(i);
					jpChat.setCaretPosition(pos2 + pic.getPos() + i); // 设置插入位置
					String fileName;
					fileName = "qqdefaultface/" + pic.getVal() + ".gif";// 修改图片路径
					jpChat.insertIcon(new ImageIcon(PicsJWindow.class.getResource(fileName))); // 插入图片
				}
				myPicInfo.clear();// 插入完成后清空表情数组
			}
		}
		jpChat.setCaretPosition(docChat.getLength()); // 设置滚动条到最底端
	}

	// 插入文本（含格式设置）
	private void insert(FontAndText attrib) {
		try {
			docChat.insertString(docChat.getLength(), attrib.getText() + "\n", attrib.getAttrSet());
			jpChat.setCaretPosition(docChat.getLength()); // 设置滚动条始终保持在最底端
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 插入文本（未设置属性则按默认属性）
	private void insert(String text) {
		try {
			docChat.insertString(docChat.getLength(), text + "\n", dateFont.getAttrSet());
			jpChat.setCaretPosition(docChat.getLength()); // 设置滚动条始终保持在最底端

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 获取当前文字属性集合
	private FontAndText getFontAttrib() {
		FontAndText att = new FontAndText();
		String buildPicInfo = buildPicInfo();
		if (buildPicInfo.equals("")) {
			att.setText(jpMsg.getText());// 纯文本信息
		} else {
			att.setText(jpMsg.getText() + "*" + buildPicInfo);// 文本和表情信息
		}
		att.setName((String) fontName.getSelectedItem());
		att.setSize(Integer.parseInt((String) fontSize.getSelectedItem()));
		String temp_color = (String) fontColor.getSelectedItem();
		if (temp_color.equals("黑色")) {
			att.setColor(new Color(0, 0, 0));
		} else if (temp_color.equals("红色")) {
			att.setColor(new Color(255, 0, 0));
		} else if (temp_color.equals("蓝色")) {
			att.setColor(new Color(0, 0, 255));
		} else if (temp_color.equals("黄色")) {
			att.setColor(new Color(255, 255, 0));
		} else if (temp_color.equals("绿色")) {
			att.setColor(new Color(0, 255, 0));
		}
		return att;
	}

	// 插入图片
	public void insertSendPic(ImageIcon imgIc) {
		jpMsg.insertIcon(imgIc);
	}

	// 重组收到的表情消息
	public void receivedPicInfo(String picInfos) {
		String[] infos = picInfos.split("[+]");
		for (int i = 0; i < infos.length; i++) {
			String[] tem = infos[i].split("[&]");
			if (tem.length == 2) {
				PicInfo pic = new PicInfo(Integer.parseInt(tem[0]), tem[1]);
				receivdPicInfo.add(pic);
			}
		}
	}

	// 重组表情信息（格式：位置&代号+...）
	private String buildPicInfo() {
		StringBuilder sb = new StringBuilder("");
		int iconNum = 0;
		for (int i = 0; i < this.jpMsg.getText().length(); i++) {
			if (docMsg.getCharacterElement(i).getName().equals("icon")) {
				Icon icon = StyleConstants.getIcon(jpMsg.getStyledDocument().getCharacterElement(i).getAttributes());
				ChatPic cupic = (ChatPic) icon;
				PicInfo picInfo = new PicInfo(i, cupic.getIm() + "");
				myPicInfo.add(picInfo);
				sb.append((i) + "&" + cupic.getIm() + "+");
				iconNum++;
			}
		}
		return sb.toString();
	}

	// 调整整个界面的字体（遍历所有组件）
	public static void setUIFont(FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, f);
		}
	}

	// 接收消息线程
	private class receivMsgThread extends Thread {

		public receivMsgThread() {

		}

		@Override
		public void run() {
			while (true) {
				try {
					byte[] bytes = new byte[1024 * 128];
					DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
					Sender.chatSoc.receive(dp);
					String recStr = new String(bytes, 0, dp.getLength(), "UTF-8");
					String[] strs = recStr.split("[*]");
					int msgType;
					if (strs.length >= 3) {
						msgType = Integer.parseInt(strs[0]);
					} else {
						continue;
					}
					String uname = strs[1];

					String message = strs[2];
					if (strs.length > 3) {
						for (int i = 3; i < strs.length - 1; i++) {
							message = message + "*" + strs[i];
						}
					}
					if (msgType == MsgType.CHAT) {
						ChatFrame.this.addRecMsg(uname, message.replace(" ", ""));

					} else if (msgType == MsgType.SHAKE) {
						ChatFrame.this.shake(uname);
					}else if(msgType == MsgType.ONLINE_ACK){
						if(!uname.equals(Sender.uname)) insert(uname + " 已上线！");
					}else if(msgType == MsgType.OFFLINE){
						if(!uname.equals(Sender.uname)) insert(uname + " 已下线！");
					}else if(msgType == MsgType.LIST_REFLEASH){
						String[] online_user = message.split("[|]");
						online.removeAllElements();
						for(String s : online_user){
							if(s.equals(Sender.uname))continue;
							online.addElement(s);
						}
						online_list.setModel(online);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(ChatFrame.this, "系统运行出错！");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (getY() <= 0) {
			setLocation(getX(), 0);
		}
		if (e.getButton() != 1)// 不是左键不触发
			return;

		JComponent source = (JComponent) e.getSource();

		// 鼠标释放时处于按钮范围内才触发按钮的事件
		if (e.getX() >= 0 && e.getX() <= source.getWidth() && e.getY() >= 0 && e.getY() <= source.getHeight()) {
			if (source == btnSend) {
				sendMsg();
			} else if (source == this.b_shake) {
				picWindow.setVisible(false);
				sendShake();
			} else if (source == this.b_pic) {
				format.setVisible(false);
				if (picWindow.isVisible())
					picWindow.setVisible(false);
				else
					picWindow.setVisible(true);
			} else if (source == this.b_remove) {
				jpMsg.setText("");
				jpMsg.revalidate();
			} else if (source == this.msgFormat) {
				picWindow.setVisible(false);
				if (format.isVisible())
					format.setVisible(false);
				else
					format.setVisible(true);
			}else if(source == this.reflesh_bth){
				//请求在线用户列表
				Sender.sendUDPMsg(MsgType.LIST_REFLEASH, Sender.uname, Sender.serverIP, Sender.ServerPort, "ask for userlist", "@server");
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		error_tip.setVisible(false);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	public String resolve(String msg){
		String toSendStr =  "";
		List<String> toSend = new ArrayList<String>();
		int lastpos=0;
		for(int i=0;i<msg.length()-1;i++){
			if(msg.charAt(i)=='@'&&msg.charAt(i+1)=='@'){
				i++;
				for(int pos = i;pos<msg.length();pos++){
					if(msg.charAt(pos)==';'){
						lastpos = pos+1;
						String user = msg.substring(i, pos);
						System.out.println("要发送给" + user);
						toSend.add(user);
						break;
					}
				}
			}
		}
		for(String s:toSend){
			toSendStr += s;
		}
		String new_jpMsg = jpMsg.getText();
		new_jpMsg = new_jpMsg.substring(lastpos,new_jpMsg.length());
//		System.out.println(new_jpMsg);
//		System.out.println(jpMsg.getText());
//		jpMsg.setText(new_jpMsg);
		return toSendStr;
	}
}
