package sender;

import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;  
public class Sender {  
    /*本机ip*/  
    public static String localIP = "127.0.0.1";  
    
    //public static String serverIP = "127.0.0.1";
    public static String serverIP = "101.200.75.182";  //服务器默认为本机，公网访问时需要进行地址映射
    /*错误信息*/  
    public static String err_msg = "";  
     /*默认发送端口*/  
    public static int ServerPort = 10000;  
    /*默认聊天端口*/  
    public static int chatPort;
    public static DatagramSocket chatSoc;
    public static String uname;

    /** 
     * @param msgType 消息类别 
     * @param uname 
     * @param serverIP 
     * @param SeverPort 
     * @return 发送是否成功 
     */  
    public static boolean sendUDPMsg(int msgType,String uname,String serverIP,int SeverPort,String messae,String tar_uname){  
        try  
        {  
            /*从命令行得到要发送的内容，使用UTF-8编码将消息转换为字节*/  
            byte[] msg = (msgType+"*"+uname+"*"+messae+"*"+tar_uname).getBytes("UTF-8");  
            /*得到主机的internet地址*/  
            InetAddress address = InetAddress.getByName(serverIP);  
  
            /*用数据和地址初始化一个数据报分组（数据包）*/  
            DatagramPacket packet = new DatagramPacket(msg, msg.length, address,  
            		SeverPort);  
  
            //String recStr = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            /*创建一个默认的套接字，通过此套接字发送数据包*/  
           chatSoc.send(packet);  
  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
            err_msg = "系统运行异常！";  
            return false;  
        }  
        return true;  
    }  
}  