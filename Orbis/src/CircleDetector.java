import imageProcessors.CannyEdgeDetector;
import imageProcessors.HoughCircle;
import imageProcessors.IPOP;
import imageProcessors.ImagePanel;
import imageProcessors.Vector2i;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.ExcelFile;
import videoProcessors.Video;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
public class CircleDetector extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1790273730764230510L;
	private static JPanel contentPane;
	private JTextField fieldX;
	private JTextField fieldY;
	private JTextField fieldRadius;
	private JTextField fieldColour;
	private JTextField fieldTolerence;
	private JTextField fieldInput;
	private JRadioButton rdbtnReal, rdbtnThreshold, rdbtnEdges;
	private ButtonGroup btnGroup;
	private JButton btnOutput, btnStart;
	private JTextField fieldOutput;
	private JLabel lblMsg;
	@SuppressWarnings("unused")
	private boolean canPreview = false, running = false;
	private static Thread t1, t2, t3;
	private JScrollPane scrollPane;
	private ImagePanel imagePanel;
	private JTextField fieldRadiusError;
	private JProgressBar progressBar;
	private JTextField fieldStartFrame;
	private JTextField fieldEndFrame;
	private JTextField fieldA;
	private JTextField fieldB;
	private JTextField fieldC;
	private JTextField fieldD;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					t1 = new Thread() {
						public void run() {
							CircleDetector frame = new CircleDetector();
							frame.setVisible(true);
						}
					};
					//CircleDetector frame = new CircleDetector();
					t1.start();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private boolean validateStart(){
		boolean xValid = (!fieldX.getText().isEmpty() && Integer.parseInt(fieldX.getText()) >= 0);
		boolean yValid = (!fieldY.getText().isEmpty() && Integer.parseInt(fieldY.getText()) >= 0);
		boolean radiusValid = (!fieldRadius.getText().isEmpty() && Integer.parseInt(fieldRadius.getText()) >= 2);
		boolean inputValid = ((!fieldInput.getText().isEmpty()) && (new File(fieldInput.getText()).exists()));
		boolean outputValid = ((fieldOutput.getText().isEmpty()) || (new File(fieldOutput.getText()).exists()));
		if (xValid && yValid && radiusValid && outputValid && inputValid)
			return true;
		else
			return false;
	}

	/**
	 * Create the frame.
	 */
	public CircleDetector() {
		
		
		
		//adding in components to the content pane
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(panel, BorderLayout.SOUTH);
		
		//The preview area
		
		btnGroup = new ButtonGroup();
		JLabel lblPreview = new JLabel("Preview Frame:");
		panel.add(lblPreview);
		
		lblMsg = new JLabel("Please select your parameters");
		
		rdbtnReal = new JRadioButton("Real");
		panel.add(rdbtnReal);
		btnGroup.add(rdbtnReal);
		
		rdbtnEdges = new JRadioButton("Edges");
		panel.add(rdbtnEdges);
		btnGroup.add(rdbtnEdges);
		
		rdbtnThreshold = new JRadioButton("Threshold");
		panel.add(rdbtnThreshold);
		btnGroup.add(rdbtnThreshold);
		
		btnGroup.setSelected(rdbtnReal.getModel(), true);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(panel_1, BorderLayout.EAST);
		
		JButton button = new JButton("Axis X | Y");
		
		final JButton btnStop = new JButton("Stop");
		
		btnStart = new JButton("Start");
		btnStart.setEnabled(false);
		
		fieldX = new JTextField();
		fieldX.setText("559");
		fieldX.setColumns(10);
		
		fieldY = new JTextField();
		fieldY.setText("435");
		fieldY.setColumns(10);
		
		JButton button_1 = new JButton("Radius");
		
		fieldRadius = new JTextField();
		fieldRadius.setText("8");
		fieldRadius.setColumns(10);
		
		JButton btnColour = new JButton("Colour");
		
		fieldColour = new JTextField();
		fieldColour.setText("FFFFFF");
		fieldColour.setColumns(10);
		
		JButton button_3 = new JButton("Tolerence");
		
		fieldTolerence = new JTextField();
		fieldTolerence.setText("1");
		fieldTolerence.setColumns(10);
		
		JButton button_4 = new JButton("Video File");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				String dir = System.getProperty("user.home") + System.getProperty("file.separator") + "Videos";
				fc.setCurrentDirectory(new File(dir));
				int r = fc.showOpenDialog(contentPane);
				
				if (r == JFileChooser.APPROVE_OPTION){
					fieldInput.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		fieldInput = new JTextField();
		fieldInput.setColumns(10);
		
		btnOutput = new JButton("Output");
		btnOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fc.showOpenDialog(contentPane);
				
				if (r == JFileChooser.APPROVE_OPTION){
					fieldOutput.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		fieldOutput = new JTextField();
		fieldOutput.setColumns(10);
		
		final JCheckBox useTolerence = new JCheckBox("use tolerence");
		final JCheckBox useColour = new JCheckBox("use colour");
		
		//upon changing input value
		fieldInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				String doc = fieldInput.getText();
				File test = new File(doc);
				if (!test.exists() || doc.isEmpty())
				{
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("File input location not valid");
				} else {
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("File input valid");
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String doc = fieldInput.getText();
				File test = new File(doc);
				if (!test.exists() || doc.isEmpty())
				{
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("File input location not valid");
				} else {
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("File input valid");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String doc = fieldInput.getText();
				File test = new File(doc);
				if (!test.exists() || doc.isEmpty())
				{
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("File input location not valid");
				} else {
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("File input valid");
				}
			}
		});
		
		//upon changing output value
				fieldOutput.getDocument().addDocumentListener(new DocumentListener() {
					@Override
					public void changedUpdate(DocumentEvent e) {
						String doc = fieldOutput.getText();
						File test = new File(doc);
						if (!test.exists() || doc.isEmpty())
						{
							if (!validateStart())
								btnStart.setEnabled(false);
							else
								btnStart.setEnabled(true);
							lblMsg.setText("File output location not valid");
						} else {
							if (!validateStart())
								btnStart.setEnabled(false);
							else
								btnStart.setEnabled(true);
							lblMsg.setText("File output valid");
						}
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						String doc = fieldOutput.getText();
						File test = new File(doc);
						if (!test.exists() || doc.isEmpty())
						{
							if (!validateStart())
								btnStart.setEnabled(false);
							else
								btnStart.setEnabled(true);
							lblMsg.setText("File output location not valid");
						} else {
							if (!validateStart())
								btnStart.setEnabled(false);
							else
								btnStart.setEnabled(true);
							lblMsg.setText("File output valid");
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String doc = fieldOutput.getText();
						File test = new File(doc);
						if (!test.exists() || doc.isEmpty())
						{
							if (!validateStart())
								btnStart.setEnabled(false);
							else
								btnStart.setEnabled(true);
							lblMsg.setText("File output location not valid");
						} else {
							if (!validateStart())
								btnStart.setEnabled(false);
							else
								btnStart.setEnabled(true);
							lblMsg.setText("File output valid");
						}
					}
				});
		
		// Upon Start action
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running = true;
				btnStop.setEnabled(true);
				btnStart.setEnabled(false);
				
				// t2 is the odd thread
				t2 = new Thread() {
					public void run() {
						track(Integer.parseInt(fieldA.getText()), Integer.parseInt(fieldB.getText()), Integer.parseInt(fieldC.getText()), Integer.parseInt(fieldD.getText()), false, fieldInput.getText(), fieldOutput.getText(), Integer.decode("0x" + fieldColour.getText()).intValue(), useTolerence.isSelected(), useColour.isSelected(), Integer.parseInt(fieldTolerence.getText()), Integer.parseInt(fieldX.getText()), Integer.parseInt(fieldY.getText()), Integer.parseInt(fieldStartFrame.getText()), Integer.parseInt(fieldEndFrame.getText()), imagePanel);
					}
				};
				// t3 is the even thread
				t3 = new Thread() {
					public void run() {
						//track(true, fieldInput.getText(), fieldOutput.getText(), Integer.decode("0x" + fieldColour.getText()).intValue(), useTolerence.isSelected(), useColour.isSelected(), Integer.parseInt(fieldTolerence.getText()), Integer.parseInt(fieldX.getText()), Integer.parseInt(fieldY.getText()), 35 * 120 + 80, -1, imagePanel);
					}
				};
				t2.start();
				//track(fieldInput.getText(), fieldOutput.getText(), Integer.decode("0x" + fieldColour.getText()).intValue(), Integer.parseInt(fieldTolerence.getText()), Integer.parseInt(fieldX.getText()), Integer.parseInt(fieldY.getText()), imagePanel);
			}
		});
		

		//upon changing radius
		fieldRadius.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				String doc = fieldRadius.getText();
				
				if (doc.isEmpty() || Integer.parseInt(doc) <= 1)
				{
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("Radius value not valid - pick a value > 1");
				} else {
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("Radius value valid");
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String doc = fieldRadius.getText();
				
				if (doc.isEmpty() || Integer.parseInt(doc) <= 1)
				{
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("Radius value not valid - pick a value > 1");
				} else {
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("Radius value valid");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String doc = fieldRadius.getText();
				
				if (doc.isEmpty() || Integer.parseInt(doc) <= 1)
				{
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("Radius value not valid - pick a value > 1");
				} else {
					if (!validateStart())
						btnStart.setEnabled(false);
					else
						btnStart.setEnabled(true);
					lblMsg.setText("Radius value valid");
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("+/-");
		
		fieldRadiusError = new JTextField();
		fieldRadiusError.setText("0");
		fieldRadiusError.setColumns(10);
		
		//upon stop
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running = false;
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
			}
		});
		btnStop.setEnabled(false);
		
		progressBar = new JProgressBar();
		
		fieldStartFrame = new JTextField();
		fieldStartFrame.setColumns(10);
		
		fieldEndFrame = new JTextField();
		fieldEndFrame.setColumns(10);
		
		fieldA = new JTextField();
		fieldA.setColumns(10);
		
		fieldB = new JTextField();
		fieldB.setColumns(10);
		
		fieldC = new JTextField();
		fieldC.setColumns(10);
		
		fieldD = new JTextField();
		fieldD.setColumns(10);
		
		//layout guff
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMsg, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btnColour, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(fieldColour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(useColour))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(fieldTolerence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(useTolerence))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnOutput, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(button_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
								.addComponent(fieldOutput)
								.addComponent(fieldInput, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(fieldRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblNewLabel)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(fieldRadiusError, 0, 0, Short.MAX_VALUE))
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(button, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(fieldX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(6)
								.addComponent(fieldY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(fieldStartFrame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fieldEndFrame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
								.addComponent(fieldA, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(fieldB, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(fieldC, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(fieldD, 0, 0, Short.MAX_VALUE))
							.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
								.addComponent(btnStart)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnStop)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(button)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addComponent(fieldX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addComponent(fieldY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(button_1)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(fieldRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)
								.addComponent(fieldRadiusError, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(btnColour)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(fieldColour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(useColour))))
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(button_3)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(fieldTolerence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(useTolerence))))
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(button_4)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addComponent(fieldInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOutput)
						.addComponent(fieldOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnStart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnStop, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(fieldStartFrame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldEndFrame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(fieldA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
					.addComponent(lblMsg)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		imagePanel = new ImagePanel();
		scrollPane.setViewportView(imagePanel);
	}
	
	public void track(int a, int b, int c, int d, boolean parityEven, String path, String outputPath, int colour, boolean useTol, boolean useColour, int tolerence, int x, int y, int fromFrame, int toFrame, ImagePanel ip){
		//setting up the detection environment
		CannyEdgeDetector canny = new CannyEdgeDetector();
		HoughCircle hough = null;
		
		//setting up the output file
		String excelPath;
		if (!outputPath.isEmpty()){
			excelPath = outputPath;
		} else {
			excelPath = path.substring(0, path.lastIndexOf("\\")) + "\\" + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(path.substring(path.lastIndexOf("."), path.length()))) + ".xlsx";
		}
		File excelFile = new File(excelPath);
		ExcelFile excel = null;
		if (excelFile.exists()){
			excel = new ExcelFile(excelPath);
		}else {
			try {
				ExcelFile.createBlankFile(excelPath);
				excel = new ExcelFile(excelPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		//start the processing
		
		Vector2i[] position = null;
		int[] radius = null;
		
		BufferedImage img1, img2, edges = null;
		Video vid = new Video(path);
		
		position = new Vector2i[vid.getFrames()];
		radius = new int[vid.getFrames()];
		
		int w = vid.getWidth();
		int h = vid.getHeight();
		hough = new HoughCircle(w, h, Integer.parseInt(fieldRadius.getText()) - Integer.parseInt(fieldRadiusError.getText()), Integer.parseInt(fieldRadius.getText()) + Integer.parseInt(fieldRadiusError.getText()));
		vid.start();
		
		Vector2i lastPos = new Vector2i(0,0);
		excel.pull();
		data.put("2", new Object[] {"Frame Number f @" + vid.getFrameRate() + "Hz", "Time t", "screen-space x", "screen-space y", "Radius r of Object", "Radius R of Orbit", "Delta Theta dTheta", "Angular Velocity Omega"});
		
		// -2 WHY? who knows maybe the first/last are being truncated here for some reason
		if (toFrame < 1)
			toFrame = vid.getFrames();
		if (fromFrame < 0 || fromFrame > toFrame)
			fromFrame = 0;
		
		progressBar.setMinimum(fromFrame);
		progressBar.setMaximum(toFrame);
		vid.setFrame(fromFrame);
		for (int i = fromFrame; i < toFrame - 2 && running == true; i++){
			//lblMsg.setText("frame " + i + " out of " + toFrame);
			
			progressBar.setValue(i);
			
			/*
			// if the parity of the thread is odd, and i is even then skip 
			if (!parityEven && i % 2 == 0)
				continue;
			if (parityEven && i % 2 != 0) // if the parity is even, and i is odd then skip
				continue;
			*/
			
			img1 = vid.grab();
			//ip.setImage(img);
			if (useColour){
				if (useTol)
					img2 = IPOP.arrayToBufferedImage(IPOP.opThreshold(img1, colour, tolerence), w, h);
				else
					img2 = IPOP.arrayToBufferedImage(IPOP.opThreshold(img1, colour, 1), w, h);
				canny.setSourceImage(img2);
			}else{
				img2 = null;
				canny.setSourceImage(img1);
			}
			
			canny.setEdgesImage(edges);
			canny.process();
			edges = canny.getEdgesImage();
			
			hough.setImage(edges);
			hough.setBounds(a, b, c, d);
			hough.process();
			position[i] = hough.getCentre();
			radius[i] = hough.getRadius();
			lblMsg.setText("position found " + position[i].getX() + ", " + position[i].getY());
			
			if (i == 0)
				ip.setPreferredSize(new Dimension(img1.getWidth(), img1.getHeight()));
			
			for (Enumeration<AbstractButton> buttons = btnGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();
	            if (button.isSelected() && !parityEven) {
	                if (button.getText() == "Real" || button.getText() == null)
	                	//imagePanel.setImage(img1);
	                	imagePanel.setImage(IPOP.arrayToBufferedImage(IPOP.drawCircle(IPOP.bufferedImageToArray(img1), w, h, position[i].getX(), position[i].getY(), 0, radius[i], 0xFFFFFF, true, 4), w, h));
	                else if (button.getText() == "Threshold" && useTol)
	                	imagePanel.setImage(IPOP.arrayToBufferedImage(IPOP.drawCircle(IPOP.bufferedImageToArray(img2), w, h, position[i].getX(), position[i].getY(), 0, radius[i], 0xFF0000, true, 4), w, h));
	                else if (button.getText() == "Edges" && edges != null)
	                	imagePanel.setImage(IPOP.arrayToBufferedImage(IPOP.drawCircle(IPOP.bufferedImageToArray(edges), w, h, position[i].getX(), position[i].getY(), 0, radius[i], 0xFF0000, true, 4), w, h));
	            }
	        }
			
			float S = (position[i].sub(lastPos)).getLength();
			float R = (float) Math.hypot(640 - position[i].getX(), 360 - position[i].getY());
			//System.out.println("frame: " + (i + 1) + " | time: " + i/vid.getFrameRate() + "s | position: " + position[i].getX() + ", " + position[i].getY() + " | r: " + radius[i] + " | R: " + R + " | S-> " + S + " | dTheta: " + S/R + " | omega: " + vid.getFrameRate()*(S/R));
			data.put((new Integer(i)).toString(), new Object[] {(i), i/vid.getFrameRate(), position[i].getX(), position[i].getY(), radius[i], R, S/R, vid.getFrameRate()*(S/R)} );
			lastPos = position[i];
		}
		excel.setData(data);
		excel.push();
		vid.stop();
		btnStart.setEnabled(true);
	}
}