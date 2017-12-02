package com.lq.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.lq.controller.CompareController;
import com.lq.util.FileUtil;
import com.lq.util.PropertiesUtil;
import com.lq.util.RarUtil;

public class CompareFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CompareController compareController = new CompareController();
	private PropertiesUtil properties = PropertiesUtil.getPropertiesUtil("config/compare/compare.properties");

	private JPanel contentPane;
	private JTextField rarPath;
	private JTextField folderPath;
	private JTextField outPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompareFrame frame = new CompareFrame();
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
	public CompareFrame() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(CompareFrame.class.getResource("/com/sun/java/swing/plaf/motif/icons/DesktopIcon.gif")));
		setTitle("文件对比器 v1.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 355);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(0, 312, 503, 14);
		contentPane.add(progressBar);

		JLabel label = new JLabel("数据集:");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		label.setBounds(69, 10, 98, 41);
		contentPane.add(label);

		rarPath = new JTextField();
		rarPath.setText(properties.getProperty("rarPath"));
		rarPath.setColumns(10);
		rarPath.setBounds(132, 16, 304, 29);
		contentPane.add(rarPath);

		JButton rarButton = new JButton("...");
		rarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("请选择rar数据集...");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("rar文件(*.rar;)", "rar");
				fcDlg.setFileFilter(filter);
				String o = properties.getProperty("rarPath");
				if (o == null || o.isEmpty()) {
					fcDlg.setSelectedFile(new File("D:/."));
				} else {
					fcDlg.setSelectedFile(new File(o));
				}

				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fcDlg.getSelectedFile().getPath();
					rarPath.setText(path);
					properties.editProperty("rarPath", path);
				}
			}
		});
		rarButton.setForeground(SystemColor.activeCaption);
		rarButton.setFont(new Font("宋体", Font.PLAIN, 15));
		rarButton.setBackground(new Color(211, 211, 211));
		rarButton.setBounds(448, 16, 35, 29);
		contentPane.add(rarButton);

		JButton folderButton = new JButton("...");
		folderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("请选择文件目录");
				fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				String o = properties.getProperty("folderPath");
				if (o == null || o.isEmpty()) {
					fcDlg.setSelectedFile(new File("D:/."));
				} else {
					fcDlg.setSelectedFile(new File(o));
				}
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fcDlg.getSelectedFile().getPath();
					folderPath.setText(path);
					properties.editProperty("folderPath", path);
				}
			}
		});
		folderButton.setForeground(SystemColor.activeCaption);
		folderButton.setFont(new Font("宋体", Font.PLAIN, 15));
		folderButton.setBackground(new Color(211, 211, 211));
		folderButton.setBounds(448, 64, 35, 29);
		contentPane.add(folderButton);

		folderPath = new JTextField();
		folderPath.setText(properties.getProperty("folderPath"));
		folderPath.setColumns(10);
		folderPath.setBounds(132, 65, 304, 29);
		contentPane.add(folderPath);

		JLabel label_1 = new JLabel("待检索文件夹：");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		label_1.setBounds(28, 64, 111, 29);
		contentPane.add(label_1);

		JButton outButton = new JButton("...");
		outButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("excel文件");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("excel文件(*.xlsx;)", "xlsx");
				fcDlg.setFileFilter(filter);
				String o = properties.getProperty("outPath");
				if (o == null || o.isEmpty()) {
					fcDlg.setSelectedFile(new File("D:/."));
				} else {
					fcDlg.setSelectedFile(new File(o));
				}
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fcDlg.getSelectedFile().getPath();
					if (!path.toLowerCase().endsWith(".xlsx")) {
						// 如果文件是以选定扩展名结束的，则使用原名
						// 否则加上选定的扩展名
						path = path + ".xlsx";
					}
					outPath.setText(path);
					properties.editProperty("outPath", path);
				}
			}
		});
		outButton.setForeground(SystemColor.activeCaption);
		outButton.setFont(new Font("宋体", Font.PLAIN, 15));
		outButton.setBackground(new Color(211, 211, 211));
		outButton.setBounds(448, 120, 35, 29);
		contentPane.add(outButton);

		outPath = new JTextField();
		outPath.setText(properties.getProperty("outPath"));
		outPath.setColumns(10);
		outPath.setBounds(132, 120, 304, 29);
		contentPane.add(outPath);

		JLabel label_2 = new JLabel("填写统计表路径:");
		label_2.setFont(new Font("宋体", Font.PLAIN, 14));
		label_2.setBounds(21, 120, 118, 29);
		contentPane.add(label_2);

		JTextArea infoText = new JTextArea();
		infoText.setText("...");
		infoText.setEditable(false);
		infoText.setEnabled(false);
		infoText.setBounds(132, 159, 304, 74);
		JScrollPane scroller = new JScrollPane(infoText);
		contentPane.add(scroller);

		// 检索逻辑
		JButton compareButton = new JButton("开始检索");
		compareButton.addActionListener(new ActionListener() {
			List<File> files = new ArrayList<File>();

			// 开始解压
			public void actionPerformed(ActionEvent e) {
				infoText.append("开始解压...");
				RarUtil rarUtil = new RarUtil() {
					@Override
					protected void getFiles(File file, int folderSize, int i) {

						progressBar.setValue(i * 100 / folderSize); // 进度值
						if (file.isFile()) {
							files.add(file);
							infoText.append("  " + file.getName());
						}

					}
				};
				File outPath = null;
				try {
					FileUtil.deleteAllFilesOfDir(outPath);
					outPath = File.createTempFile("COMPARE_FF", "OO");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rarUtil.unRarAllFile(rarPath.getText(), outPath.getAbsolutePath());
				FileUtil.deleteAllFilesOfDir(outPath);
				// 解压完成，开始检索...
				infoText.append("    解压完成，开始检索...");
				File[] compareFiles = FileUtil.searchFile(new File(folderPath.getText()));
				// 待扫描的数据集 数据集 比对
				// boot.jar /uu/boot.jar same
				// different（数据集在指定路径有这个文件，但是大小不一样）
				// same（数据集在指定路径有这个文件，且大小一样）
				// nofind（数据集在指定路径没有这个文件）
				List<String[]> comparedData = new ArrayList<String[]>();
				comparedData.add(new String[] { "待扫描的数据集", "数据集", "比对" });
				String compareFilePath = "";
				String rarFilePath = "";
				String result = "";
				String[] outDatas;
				for (File compareFile : compareFiles) {

					List<File> findData = compareController.compare(compareFile, files);
					compareFilePath = compareFile.getAbsolutePath();
					if (findData.size() == 0) {
						rarFilePath = "无";
						result = "nofind";
						outDatas = new String[] { compareFilePath, rarFilePath, result };
						comparedData.add(outDatas);
						infoText.append("..." + Arrays.toString(outDatas));
					} else {
						for (File file : findData) {
							rarFilePath = file.getPath();
							if (file.length() == compareFile.length()) {
								result = "same";
							} else {
								result = "different";
							}
							outDatas = new String[] { compareFilePath, rarFilePath, result };
							comparedData.add(outDatas);
							infoText.append("..." + Arrays.toString(outDatas));

						}
					}

				}
				// 检索完成...
				infoText.append("    检索完成...");

				progressBar.setString("检索完成."); // 提示信息
			}
		});
		compareButton.setForeground(Color.BLACK);
		compareButton.setFont(new Font("宋体", Font.PLAIN, 14));
		compareButton.setBackground(new Color(100, 149, 237));
		compareButton.setBounds(205, 251, 111, 41);
		contentPane.add(compareButton);
	}
}
