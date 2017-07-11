package qqdefaultface;

import java.net.URL;

import javax.swing.ImageIcon;

public class ChatPic extends ImageIcon {

	private static final long serialVersionUID = 1L;
	int im;// Í¼Æ¬´úºÅ

	public int getIm() {
		return im;
	}

	public void setIm(int im) {
		this.im = im;
	}

	public ChatPic(URL url, int im) {
		super(url);
		this.im = im;
	}
}
