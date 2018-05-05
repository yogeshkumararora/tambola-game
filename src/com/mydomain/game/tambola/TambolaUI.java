package com.mydomain.game.tambola;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

public class TambolaUI extends JPanel {
	// Field members
	private AtomicBoolean paused;
	private JTextArea textArea;
	private JButton button;
	private Thread threadObject;
	private JLabel[] label;
	private JLabel textAreaLabel;

	private final Font BIGGER_FONT = new Font("monspaced", Font.PLAIN, 35);
	private final Font BUTTON_FONT = new Font("tahoma", Font.BOLD, 23);

	/**
	 * Constructor
	 */
	public TambolaUI() {
		paused = new AtomicBoolean(false);
		textArea = new JTextArea(5, 30);
		button = new JButton();

		initComponents();
	}

	/**
	 * Initializes components
	 */
	public void initComponents() {
		label = new JLabel[90];
		setLayout(new GridLayout(10, 9, 10, 20));

		for (int row = 0; row < 9; row++) {
			for (int column = 1; column <= 10; column++) {
				int num = row * 10 + column;
				System.out.println("num = " + num);
				String labelText = "";
				labelText = labelText + num;
				System.out.println("labelText = " + labelText);
				label[num - 1] = new JLabel(labelText, JLabel.CENTER);
				label[num - 1].setFont(BIGGER_FONT);
				label[num - 1].setBorder(BorderFactory
						.createLineBorder(Color.black));

				label[num - 1].setForeground(Color.green);
				add(label[num - 1]);
			}
		}
		// Construct components
		textAreaLabel = new JLabel("Spoken Nos.");
		
		add(textAreaLabel);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		add(new JScrollPane(textArea));
		button.setText("Pause");
		button.setFont(BUTTON_FONT);
		button.setForeground(Color.RED);
		
		button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		button.setBorder(BorderFactory.createLineBorder(Color.black));
		button.addActionListener(new ButtonListener());
		add(button);

		// Runnable that continually writes to text area
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				RandomNumberGenerator rng = new RandomNumberGenerator();
				for (int index = 0; index < 90; index++) {
					if (paused.get()) {
						synchronized (threadObject) {
							// Pause
							try {
								threadObject.wait();
							} catch (InterruptedException e) {
							}
						}
					}

					TextToSpeech tts = new TextToSpeech();
					int num = rng.getNextNumberToSpeak();
					tts.convertTextToSpeech(num);
					textArea.append(num + ", ");
					if (label[num - 1].getText().equals("" + num)) {
						label[num - 1].setForeground(Color.red);
					}

					// Sleep
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		threadObject = new Thread(runnable);
		threadObject.start();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 670);
	}

	/**
	 * Button action listener
	 * 
	 * @author meherts
	 * 
	 */
	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (!paused.get()) {
				button.setText("Start");
				paused.set(true);
				button.setFont(BUTTON_FONT);
				button.setForeground(Color.RED);
			} else {
				button.setText("Pause");
				paused.set(false);

				// Resume
				synchronized (threadObject) {
					threadObject.notify();
				}
			}
		}
	}

	public static void main(final String[] arg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new TambolaUI());
				frame.pack();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				frame.setTitle("Gaming Console - Tambola");
				frame.setResizable(false);
			}
		});
	}

}
