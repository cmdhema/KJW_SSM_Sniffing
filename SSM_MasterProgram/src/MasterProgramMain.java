import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


public class MasterProgramMain extends JFrame {

	private JPanel contentPane;
	private ButtonGroup radioGroup;
	private JTextArea textArea;
	private JLabel lblNewLabel;
	
	public static String phoneNumber = "";
	public static String result = "";

	public static String photoName = "";
	
	private int state;
	private JPanel panel_1;
	private JTextField textField;
	private JButton button;
	private JRadioButton userButton;
	private JRadioButton recvButton;
	private JRadioButton callButton;
	private JRadioButton photoButton;
	private JComboBox comboBox;
	
	private String photo;
    private String[] array;
	
	private File path = new File(".");
	private String route =  path.getAbsolutePath() + "/";
	private String sa;
	private JRadioButton sendButton;
	
	private String[] userInfoColumnName = {"��ȭ��ȣ", "��Ż�","����","����","��","����","�浵","���� �Ͻ�","GCM Key"};
	private String[] photoColumnName = {"��ȭ��ȣ", "���� �̸�"};
	private String[] smsColumnName = {"��ȭ��ȣ", "���� ��ȣ", "���� �̸�", "����", "�Ͻ�"};
	private String[] callColumnName = {"��ȭ��ȣ","���� ��ȣ", "���� �̸�", "�Ͻ�"};
	
//	// ��Ʈ��ũ ��û�� ������ HashMap
//	public HashMap<Context, ArrayList<NetworkRequest>> mRequestMap = new HashMap<Context, ArrayList<NetworkRequest>>();
//	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MasterProgramMain frame = new MasterProgramMain();
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
	public MasterProgramMain() {
		initComponent();
		createEvents();
	}
	
	private void initComponent(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 644, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
	
		radioGroup = new ButtonGroup();
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
			
		
		
		lblNewLabel = new JLabel("");
		
		panel_1 = new JPanel();
		
		
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.add(panel_1);
		
		textField = new JTextField();
		textField.setColumns(11);
		
		button = new JButton("\uAC80\uC0C9");
		
		
		
		userButton = new JRadioButton("\uC0AC\uC6A9\uC790 \uC815\uBCF4");
		userButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				state = 0;
			}
		});
		
		recvButton = new JRadioButton("\uBA54\uC138\uC9C0 \uC218\uC2E0 \uAE30\uB85D");
		recvButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				state = 1;
			}
		});
		
		
		comboBox = new JComboBox();
		
		sendButton = new JRadioButton("\uBA54\uC138\uC9C0 \uBC1C\uC2E0 \uAE30\uB85D");
		sendButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				state = 2;
			}
		});
		
		callButton = new JRadioButton("\uD1B5\uD654 \uAE30\uB85D");
		callButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				state = 3;
			}
		});
		
		photoButton = new JRadioButton("\uC0AC\uC9C4");
		photoButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				state = 4;
			}
		});
		
		radioGroup = new ButtonGroup();
		radioGroup.add(recvButton);
		radioGroup.add(sendButton);
		radioGroup.add(userButton);
		radioGroup.add(callButton);
		radioGroup.add(photoButton);
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(userButton)
								.addComponent(recvButton)
								.addComponent(sendButton)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(13)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)))
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(callButton)
						.addComponent(button)
						.addComponent(photoButton))
					.addGap(37))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button))
					.addGap(7)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(userButton)
						.addComponent(callButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(recvButton)
						.addComponent(photoButton))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(sendButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		panel.add(lblNewLabel);
		
//		String[][] data = {{"������","pthsoftware_2@hanmail.net"},
//				{"����ȯ","LeeJaeH123@nate.com"},
//				{"������","Sunny61@naver.com"},
//				{"������","dlaelad@hamail.net"},
//				{"�̿���","Leehahaha@google.com"},
//				{"���ر�","KCC1234@daum.net"},
//				{"���־�","JUJU@hotmail.com"},
//				{"���ֿ�","JUJU@hotmail.com"},
//				{"�����","JUJU@hotmail.com"},
//				{"����","JUJU@hotmail.com"},
//				{"��ŰD����","JUJU@hotmail.com"},
//		};
//		// ����Ÿ�� �÷��̸��� ������ �𵨰�ü �����
//		DefaultTableModel model = new DefaultTableModel(data, userInfoColumnName);
//		// �𵨰�ü�� ������ ���̺� �����		
//		JTable table = new JTable(model);
//		
//		//���̺� ��ũ���� �����ϱ� ���� JScrollpane ����
//		JScrollPane scroll = new JScrollPane (table);
//		
//		// �����ӿ� ����
////		add(scroll);	
////		textArea = new JTextArea();
////		scrollPane.setViewportView(textArea);
//		contentPane.add(scroll);
		
		SMSInfo sms = new SMSInfo();
		sms.content = "AA";
		sms.date = "2009-12-12";
		sms.recvNumber = "010-4441-4523";
		sms.sendNumber = "010-1111-2222";
		Vector vector= new Vector();
		vector.addElement("AA");
		vector.addElement("2009-12-12");
		vector.addElement("010-4441-4523");
		vector.addElement("010-1111-2222");
		
		Vector v = new Vector();
		v.add(vector);
		Vector<String> string = new Vector<String>();
		string.addElement("����");
		string.addElement("�Ͻ�");
		string.addElement("�޴� ��� ��ȣ");
		string.addElement("���� ��� ��ȣ");
		
		Vector v2 = new Vector<>();
		v2.add(string);
		
		DefaultTableModel model = new DefaultTableModel(v, v2);
		JTable table = new JTable(model);
		JScrollPane scroll = new JScrollPane (table);
		contentPane.add(scroll);
		
		
	}
	
	private void createEvents(){
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
								
				phoneNumber = textField.getText();	
				
				switch(state){
				case 0:	{//����� ����
//					textArea.setText();
					System.out.println("User CLick");
					result = NetworkConn.getSMSInfo("Info");
					array = result.split("!");
					
					String infoContents = "";
					for(int i=0;i<array.length;i++) {
						infoContents += array[i];
						infoContents += "\n";
					}
					textArea.setText(infoContents);
					break;
				}
				case 1:	{//�޼��� ����
					SMSInfo sms = new SMSInfo();
					sms.content = "AA";
					sms.date = "2009-12-12";
					sms.recvNumber = "010-4441-4523";
					sms.sendNumber = "010-1111-2222";
					Vector<SMSInfo> vector= new Vector<SMSInfo>();
					vector.add(sms);
					
					Vector<String> string = new Vector<String>();
					string.add("����");
					string.add("�Ͻ�");
					string.add("�޴� ��� ��ȣ");
					string.add("���� ��� ��ȣ");
					
					DefaultTableModel model = new DefaultTableModel(vector, string);
					JTable table = new JTable(model);
					JScrollPane scroll = new JScrollPane (table);
					contentPane.add(scroll);
					System.out.println("SMS CLick");
					result = NetworkConn.getSMSInfo("SMSRecv");
					array = result.split("!");
					
					String recvContents = "";
					for(int i=0;i<array.length;i++) {
						recvContents += array[i];
						recvContents += "\n";
					}
					textArea.setText(recvContents);
					break;
				}
				case 2: // �޼��� �߽�
					System.out.println("SMS send CLick");
					result = NetworkConn.getSMSInfo("SMSSend");
					array = result.split("!");
					
					String sendContents = "";
					for(int i=0;i<array.length;i++) {
						sendContents += array[i];
						sendContents += "\n";
					}
					textArea.setText(sendContents);
					break;
				
				case 3:	//��ȭ ���
					System.out.println("Call CLick");
					result = NetworkConn.getSMSInfo("Calling");
					array = result.split("!");
					
					String callingContents = "";
					for(int i=0;i<array.length;i++) {
						callingContents += array[i];
						callingContents += "\n";
					}
					textArea.setText(callingContents);
					break;
				case 4:
					System.out.println("PHOTO CLick");
					result = NetworkConn.getSMSInfo("Photo");
					array = result.split(":");
					
					for(int i =0; i<array.length; i++){
						photoName = array[i];
						//���� ���� �����ϴ� �Լ�
						NetworkConn.getHttp_savePhoto();	
						comboBox.addItem(photoName);
					}					
					break;
				}
				

								
			}
		});
		
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED){
					lblNewLabel.setIcon(new ImageIcon(array[comboBox.getSelectedIndex()]));
					
}
			}
		});
	}
	
	
	String getPhoneNumber(){
		return phoneNumber;
	}
	
	String getPhotoName(){
		return photoName;
	}
	
}


