package frame;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.MsgType;
import sender.Sender;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;

public class LoginFrame extends JFrame implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Username;
	private JPasswordField Password;
	private  int first_x;
	private int first_y;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// k客户端运行时分配一个Port
				try {
					Sender.chatSoc = new DatagramSocket();
				} catch (SocketException e2) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Client Port has been binded");
					System.exit(1);
				}
				Sender.chatPort = Sender.chatSoc.getPort();

				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setUndecorated(true);
		setType(Type.POPUP);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					Login();
			}
		});
		setResizable(false);
		setTitle("\u767B\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 408);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {

				ImageIcon icon = new ImageIcon(getClass().getResource("/backGround/login.png"));
				Image img = icon.getImage();
				g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Username = new JTextField();
		Username.setBorder(new EmptyBorder(0, 0, 0, 0));
		Username.setAlignmentY(Component.TOP_ALIGNMENT);
		Username.setAlignmentX(Component.LEFT_ALIGNMENT);
		Username.setToolTipText("\u7528\u6237\u540D");
		Username.setBounds(170, 248, 203, 29);
		contentPane.add(Username);
		Username.setColumns(10);

		JButton button = new JButton("");
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.setForeground(new Color(9,163,220));
		button.setBackground(new Color(9,163,220));
		button.setBorder(new EmptyBorder(0, 0, 0, 0));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login();
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 17));
		button.setBounds(170, 361, 236, 34);
		contentPane.add(button);

		JButton button_1 = new JButton("");
		button_1.setBorder(new EmptyBorder(0, 0, 0, 0));
		button_1.setOpaque(false);
		button_1.setBackground(new Color(235,242,249));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Register();
			}
		});
		button_1.setFont(new Font("宋体", Font.PLAIN, 17));
		button_1.setBounds(421, 248, 75, 29);
		contentPane.add(button_1);

		Password = new JPasswordField();
		Password.setBorder(new EmptyBorder(0, 0, 0, 0));
		Password.setAlignmentY(Component.TOP_ALIGNMENT);
		Password.setAlignmentX(Component.LEFT_ALIGNMENT);
		Password.setToolTipText("\u5BC6\u7801");
		Password.setBounds(170, 284, 203, 29);
		contentPane.add(Password);
		
		JButton button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		button_2.setBorder(new EmptyBorder(0, 0, 0, 0));
		button_2.setBackground(new Color(0,91,149));
		button_2.setOpaque(false);
		button_2.setBounds(493, 0, 44, 34);
		contentPane.add(button_2);
		
		JButton min_btn = new JButton("");
		min_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
		});
		min_btn.setOpaque(false);
		min_btn.setBorder(new EmptyBorder(0, 0, 0, 0));
		min_btn.setBackground(new Color(0, 91, 149));
		min_btn.setBounds(452, 0, 44, 34);
		contentPane.add(min_btn);
	}

	private void Login() {
		String username = Username.getText();
		String password = new String(Password.getPassword());
		if (username.equals("")) {
			JOptionPane.showMessageDialog(null, "请输入用户名！");
			return;
		}
		if (password.equals("")) {
			JOptionPane.showMessageDialog(null, "请输入密码！");
			return;
		}

		Sender.uname = username;
		if (Sender.sendUDPMsg(MsgType.ONLINE, Sender.uname, Sender.serverIP, Sender.ServerPort,
				username + ":" + password, "server")) {

			byte[] bytes = new byte[1024 * 128];
			DatagramPacket p = new DatagramPacket(bytes, bytes.length);

			try {
				Sender.chatSoc.setSoTimeout(50000);

				try {
					Sender.chatSoc.receive(p);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "连接服务器超时！请检查服务器是否开启。");
			}

			try {
				String recStr = new String(p.getData(), 0, p.getLength(), "UTF-8");
				String[] strs = recStr.split("[*]");
				int msgType = Integer.parseInt(strs[0]);
				if (msgType == MsgType.ONLINE_ACK) {
					Sender.chatSoc.setSoTimeout(0);
					Sender.uname = username;
					JOptionPane.showMessageDialog(null, "登陆成功");
					setVisible(false);
					new ChatFrame().setVisible(true);
				} else if (msgType == MsgType.ONLINE_ERR) {
					JOptionPane.showMessageDialog(null, "用户名或密码错误");
				} else if (msgType == MsgType.ONLINE_ALREADY) {
					JOptionPane.showMessageDialog(null, "您已登陆过，请退出后尝试！");
				}

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	private void Register() {
		String username = Username.getText();
		String password = new String(Password.getPassword());
		if (username.equals("")) {
			JOptionPane.showMessageDialog(null, "请输入用户名！");
			return;
		}
		if (password.equals("")) {
			JOptionPane.showMessageDialog(null, "请输入密码！");
			return;
		}

		Sender.uname = username;
		if (Sender.sendUDPMsg(MsgType.REGISTER, Sender.uname, Sender.serverIP, Sender.ServerPort,
				username + ":" + password, "server")) {

			byte[] bytes = new byte[1024 * 128];
			DatagramPacket p = new DatagramPacket(bytes, bytes.length);

			try {
				Sender.chatSoc.setSoTimeout(40000);

				try {
					Sender.chatSoc.receive(p);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "连接服务器超时！请检查服务器是否开启。");
			}

			try {
				String recStr = new String(p.getData(), 0, p.getLength(), "UTF-8");
				String[] strs = recStr.split("[*]");
				int msgType = Integer.parseInt(strs[0]);
				if (msgType == MsgType.REGISTER_ACK) {
					Sender.chatSoc.setSoTimeout(0);
					Sender.uname = username;
					JOptionPane.showMessageDialog(null, "注册成功！");
					setVisible(false);
					new ChatFrame().setVisible(true);
				} else if (msgType == MsgType.REGISTER_ERR) {
					JOptionPane.showMessageDialog(null, "用户名已存在！");
				}

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		first_x=e.getX();
		first_y=e.getY(); //记录下位移的初点
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		int x=e.getX()-first_x;

		int y=e.getY()-first_y;  //取得位移(x,y)

		setBounds(getX()+x,getY()+y, getWidth(),getHeight());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
