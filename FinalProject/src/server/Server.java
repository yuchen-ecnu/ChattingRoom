package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import common.MsgType;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.DropMode;

public class Server extends JFrame implements BaseService,MouseListener,MouseMotionListener{

	//声明控件
	private JPanel contentPane;
	private JButton restart_btn;
	private JButton poweroff_btn;
	private JButton minsize_btn;
	private JLabel lblNewLabel;
	
	//控件變量
	private  int first_x;
	private int first_y;
	
	//声明自有变量
	public static final int Server_Port = 10000; /* 服务器端口 */
	private static DatagramSocket receiveSocket;// 接受消息的Socket
	private static DatagramPacket recPacket;// 接收到的数据报
	private static HashMap<String, SocketAddress> hashTable;// 存储已经登录的用户名和SocketAddress
	private static ServerThread currentThread;//服務器主線程
	private JScrollPane scrollPane;
	private static JTextArea serverMsg;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//顯示服務器界面
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					printMsg("服务器启动失敗\n");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setUndecorated(true);
		setResizable(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setTitle("\u670D\u52A1\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 907, 540);
		contentPane = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				ImageIcon icon = new ImageIcon(getClass().getResource("/backGround/server.jpg"));
				Image img = icon.getImage();
				g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		restart_btn = new JButton("");
		restart_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printMsg("正在重啟服務器......\n");
				receiveSocket.close();
			     currentThread.stop();
				printMsg("服務器關閉成功！\n");
				currentThread = new ServerThread();
				currentThread.start();
			}
		});
		restart_btn.setBounds(810, 13, 41, 32);
		contentPane.add(restart_btn);
		restart_btn.setContentAreaFilled(false);
		restart_btn.setIcon(new ImageIcon(Server.class.getResource("/icon/restart.png")));
		restart_btn.setToolTipText("\u91CD\u542F\u670D\u52A1\u5668");
		restart_btn.setBorderPainted(false);
		restart_btn.setBackground(SystemColor.menu);
		
		lblNewLabel = new JLabel("\u670D\u52A1\u5668\u65E5\u5FD7\uFF1A");
		lblNewLabel.setBounds(0, 68, 129, 24);
		contentPane.add(lblNewLabel);
		lblNewLabel.setFont(new Font("幼圆", Font.BOLD, 17));
		
		poweroff_btn = new JButton("");
		poweroff_btn.setBounds(852, 13, 41, 32);
		contentPane.add(poweroff_btn);
		poweroff_btn.setContentAreaFilled(false);
		poweroff_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     currentThread.stop ();
				printMsg("服務器關閉成功！\n");
				System.exit(0);
			}
		});
		poweroff_btn.setIcon(new ImageIcon(Server.class.getResource("/icon/poweroff.png")));
		poweroff_btn.setToolTipText("\u5173\u95ED\u670D\u52A1\u5668");
		poweroff_btn.setBorderPainted(false);
		poweroff_btn.setBackground(SystemColor.menu);
		
	    minsize_btn = new JButton("");
		minsize_btn.setBounds(766, 13, 41, 32);
		contentPane.add(minsize_btn);
		minsize_btn.setContentAreaFilled(false);
		minsize_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
		});
		minsize_btn.setIcon(new ImageIcon(Server.class.getResource("/icon/min.png")));
		minsize_btn.setToolTipText("\u6700\u5C0F\u5316");
		minsize_btn.setBorderPainted(false);
		minsize_btn.setBackground(SystemColor.menu);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(14, 60, 879, 6);
		contentPane.add(separator);
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 95, 907, 445);
		scrollPane.getViewport().setOpaque(false); 
		contentPane.add(scrollPane);
		
		serverMsg = new JTextArea();
		serverMsg.setFont(new Font("幼圆", Font.BOLD, 16));
		serverMsg.setEditable(false);
		serverMsg.setOpaque(false);
		scrollPane.setViewportView(serverMsg);
		
		currentThread = new ServerThread();//啟動服務器
		currentThread.start();
	}
	
	//服务器进程
	private static class ServerThread extends Thread{
		
		public ServerThread() {
			hashTable = new HashMap<String, SocketAddress>();
			try {
				receiveSocket = new DatagramSocket(Server_Port);
			} catch (SocketException e) {
				printMsg("服務器端口被占用，服務啟動失敗！請嘗試重啟！\n");
				return;
			}
			
			printMsg("服务器启动成功\n");
			byte[] bytes = new byte[1024 * 128];
			recPacket = new DatagramPacket(bytes, bytes.length);
		}
		
		@Override
		public void run() {
			while (true) {
					try {
						receiveSocket.receive(recPacket);
					} catch (IOException e3) {
						e3.printStackTrace();
					}
					String recStr = null;
					try {
						recStr = new String(recPacket.getData(), 0, recPacket.getLength(), "UTF-8");
					} catch (UnsupportedEncodingException e2) {
						e2.printStackTrace();
					}
					String[] strs = recStr.split("[*]");
					int msgType;
					if (strs.length >= 3) {
						msgType = Integer.parseInt(strs[0]);
					} else {
						continue;
					}
					// 上线逻辑
					if (msgType == MsgType.ONLINE) {

						// 获取参数
						String uname = strs[1];
						String message = strs[2];
						if (strs.length > 3) {
							for (int i = 3; i < strs.length - 1; i++) {
								message = message + "*" + strs[i];
							}
						}
						String username = message.split("[:]")[0];
						String password = message.split("[:]")[1];

						boolean isLogin = false;
						for (String key : hashTable.keySet()) {
							if (key.equals(uname)) {
								byte[] msg = null;
								try {
									msg = (MsgType.ONLINE_ALREADY + "*" + uname + "*" + "您已登陆过，请退出后尝试！").getBytes("UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
										recPacket.getPort());
								try {
									receiveSocket.send(packet);
								} catch (IOException e) {
									e.printStackTrace();
								}
								isLogin = true;
								break;
							}
						}
						if(isLogin)continue;
						if (userService.checkUser(username, password)) {
							byte[] msg = null;
							try {
								msg = (MsgType.ONLINE_ACK + "*" + uname
										+ "*" + "登录成功！").getBytes("UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							hashTable.put(uname, recPacket.getSocketAddress());
						
							for (String key : hashTable.keySet()) {
								DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
										recPacket.getPort());
								SocketAddress sa = hashTable.get(key);
								packet.setSocketAddress(sa);
								try {
									receiveSocket.send(packet);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							refresh();	//刷新好友列表
							printMsg(uname+"   已上线");
						} else {
							byte[] msg = null;
							try {
								msg = (MsgType.ONLINE_ERR + "*" + recPacket.getAddress() + ":" + recPacket.getPort()
										+ "*" + "用户名或密码错误！").getBytes("UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
									recPacket.getPort());
							try {
								receiveSocket.send(packet);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					} else if (msgType == MsgType.REGISTER) {
						// 获取参数
						String uname = strs[1];

						String message = strs[2];
						if (strs.length > 3) {
							for (int i = 3; i < strs.length - 1; i++) {
								message = message + "*" + strs[i];
							}
						}
						String username = message.split("[:]")[0];
						String password = message.split("[:]")[1];

						// 处理逻辑
						if (userService.register(username, password)) {
							byte[] msg = null;
							try {
								msg = (MsgType.REGISTER_ACK + "*" + recPacket.getAddress() + ":" + recPacket.getPort()
										+ "*" + "注册成功！").getBytes("UTF-8");
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
									recPacket.getPort());
							hashTable.put(uname, recPacket.getSocketAddress());
							try {
								receiveSocket.send(packet);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							//刷新好友列表
							refresh();
							printMsg(uname + " 已上线！");
						} else {
							byte[] msg = null;
							try {
								msg = (MsgType.REGISTER_ERR + "*" + recPacket.getAddress() + ":" + recPacket.getPort()
										+ "*" + "用户名重复！").getBytes("UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
									recPacket.getPort());
							try {
								receiveSocket.send(packet);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					// 下线逻辑
					else if (msgType == MsgType.OFFLINE) {
						String uname = strs[1];
						hashTable.remove(uname);
						byte[] msg = null;
						try {
							msg = (MsgType.OFFLINE + "*" + uname + "*" + "下线成功！").getBytes("UTF-8");
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
						for (String key : hashTable.keySet()) {
							SocketAddress sa = hashTable.get(key);
							DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
									recPacket.getPort());
							packet.setSocketAddress(sa);
							try {
								receiveSocket.send(packet);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						//刷新好友列表
						refresh();
						printMsg(uname+"   已下线");
					}

					// 震动处理逻辑
					else if (msgType == MsgType.SHAKE) {
						String uname = strs[1];
						if (strs[3].equals("@all")) {
							// 向所有用 户转发消息
							for (String key : hashTable.keySet()) {
								if (key.equals(uname))
									continue;
								SocketAddress sa = hashTable.get(key);
								recPacket.setSocketAddress(sa);
								// printMsg(recPacket.getPort());
								try {
									receiveSocket.send(recPacket);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} else if (msgType == MsgType.CHAT) {
						String chatTO = strs[strs.length - 1];
						String uname = strs[1];

						if (chatTO.equals("@all")) {
							// 向所有用 户转发消息
							for (String key : hashTable.keySet()) {
								if (key.equals(uname))
									continue;
								SocketAddress sa = hashTable.get(key);
								recPacket.setSocketAddress(sa);
								try {
									receiveSocket.send(recPacket);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							String[] targetArr = chatTO.split("[@]");
							for(int i =0; i<targetArr.length;i++){
								String target = targetArr[i];
								if (hashTable.containsKey(target)) {
									SocketAddress sa = hashTable.get(target);
									recPacket.setSocketAddress(sa);
									try {
										receiveSocket.send(recPacket);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									// 信息发送失败逻辑
								}
							}
						}
					} else if(msgType==MsgType.LIST_REFLEASH){
						String uname = strs[1];
						String msgcontent = "";
						for(String key : hashTable.keySet()){
							msgcontent+=key+"|";
						}
						byte[] msg = null;
						try {
							msg = (MsgType.LIST_REFLEASH + "*" +uname +"*"+msgcontent
							+ "*" + "在线用户列表！").getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DatagramPacket packet = new DatagramPacket(msg, msg.length, recPacket.getAddress(),
								recPacket.getPort());
						try {
							receiveSocket.send(packet);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						printMsg("服务器接收到非法数据!\n");
					}

				}
		}
	}
	
	//向服务器窗口打印日志
	public static void  printMsg(String msg){
		serverMsg.append(msg+"\n");
	}
	
	//向所有用户刷新好友列表
	public static void refresh(){
		String msgcontent = "";
		for(String key : hashTable.keySet()){
			msgcontent+=key+"|";
		}
		
		try {
			byte[] msgonline = (MsgType.LIST_REFLEASH + "*" +"uname" +"*"+msgcontent
			+ "*" + "在线用户列表！").getBytes("UTF-8");
			for (String key : hashTable.keySet()) {
				DatagramPacket packet = new DatagramPacket(msgonline, msgonline.length, recPacket.getAddress(),
				recPacket.getPort());
				SocketAddress sa = hashTable.get(key);
				packet.setSocketAddress(sa);
				receiveSocket.send(packet);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
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

		setBounds(getX()+x, getY()+y, getWidth(), getHeight());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
