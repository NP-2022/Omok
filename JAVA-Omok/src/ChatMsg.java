
// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;

//100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event, 600:�� ���� 601:�� ����
//700:�� ���� 701:�� ���� 800:���ӽ��� 801:�����غ� 802:�������� 900:�ٵϵ��Է� 901:����(�ٵϵ� �ϳ��� ����) 902:���� ���

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code;				//� ������ ��������
	public String UserName;			//����� �̸�
	public String data;				//����
	public ImageIcon img;			//�̹���
	public MouseEvent mouse_e;		//���콺 �̺�Ʈ
	public int pen_size; // pen size
	public int roomMax;				//�� �ִ� �÷��� �ο�
	public String roomName;			//�� �̸�
	public int roomNumber;			//���ȣ
	public String gameMode;			//���� ����
	public int y;
	public int x;
	public int stone;
	
	public ChatMsg(String UserName, String code, String msg) {	//���� �̸�, �ڵ�, �޼��� ����
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
	
}