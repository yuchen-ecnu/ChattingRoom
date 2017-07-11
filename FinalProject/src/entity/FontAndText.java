package entity;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class FontAndText {
	public static final int GENERAL = 0; // 常规
	private String msg = "", name = "宋体"; // 要输入的文本和字体名称

	private int size = 0; // 字号

	private Color color = new Color(225, 225, 225); // 文字颜色

	private SimpleAttributeSet attrSet = null; // 属性集

	public FontAndText() {
	}

	// 设置属性值
	public FontAndText(String msg, String fontName, int fontSize, Color color) {
		this.msg = msg;
		this.name = fontName;
		this.size = fontSize;
		this.color = color;
	}
	
	// 获取属性值
	public SimpleAttributeSet getAttrSet() {
		attrSet = new SimpleAttributeSet();
		if (name != null) {
			StyleConstants.setFontFamily(attrSet, name);
		}
		StyleConstants.setFontSize(attrSet, size);
		if (color != null)
			StyleConstants.setForeground(attrSet, color);
		return attrSet;
	}

	// 重写toString方法方便数据传输（格式：字体|字号|R-G-B|消息）
	public String toString() {
		return name + "|" + size + "|" + color.getRed() + "-" + color.getGreen() + "-" + color.getBlue() + "|" + msg;
	}

	// get/set 方法
	public String getText() {
		return msg;
	}

	public void setText(String text) {
		this.msg = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
