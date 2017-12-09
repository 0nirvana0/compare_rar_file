package com.lq.test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class JProgressBarTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JProgressBarTest() {
		super();
		setTitle("表格");
		setBounds(100, 100, 350, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JButton button = new JButton("           ");
		final JButton button2 = new JButton("完成");
		button2.setBounds(111, 10, 57, 23);
		button2.setEnabled(false); // 初始化时不可用
		button.setBounds(2, 10, 99, 23);

		final JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(178, 13, 146, 16);
		progressBar.setStringPainted(true); // 显示提示信息
		progressBar.setIndeterminate(false);
		getContentPane().setLayout(null);
		getContentPane().add(button); // 布局处理
		getContentPane().add(button2); // 布局处理
		getContentPane().add(progressBar); // 布局处理
		new Progress(progressBar, button2).start(); // 自定义类progress
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JProgressBarTest jProgressBarTest = new JProgressBarTest();
		jProgressBarTest.setVisible(true);
	}

}

class Progress extends Thread {// 自定义类progress
	private final int[] progressValue = { 6, 18, 27, 39, 51, 66, 81, 100 };
	private JProgressBar progressBar;
	private JButton button;

	public Progress(JProgressBar progressBar, JButton button) {
		this.progressBar = progressBar;
		this.button = button;
	}

	public void run() {
		for (int i = 0; i < progressValue.length; i++) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			progressBar.setValue(progressValue[i]); // 进度值
		}
		progressBar.setIndeterminate(false); // 采用确定的进度条
		// progressBar.setIndeterminate(true); //不确定进度的进度条
		progressBar.setString("升级完成."); // 提示信息
		button.setEnabled(true); // 按钮可用
	}
}
