package common;

/**
 * 定义所用帧头
 * @author cheny
 *
 */
public final class MsgType {
	public final static int CHAT = 0; // 聊天
	public final static int ONLINE = 1; // 上线
	public final static int OFFLINE = 2; // 下线
	public final static int SHAKE = 3; // 震动
	public final static int ONLINE_ACK = 4; // 登录确认帧
	public final static int ONLINE_ERR = 5; // 登录失败帧
	public final static int ONLINE_ALREADY = 9; // 已登录错误帧
	public final static int REGISTER = 6;	//注册帧
	public final static int REGISTER_ACK = 7;	//注册帧
	public final static int REGISTER_ERR = 8;	//注册失败帧
	public final static int CHAT_SECRET = 9;	//私聊
	public final static int LIST_REFLEASH = 10;	//刷新好友列表
}