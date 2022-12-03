
// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;
//000: 1�� �ð� �帧
//100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 201:���ӹ� ä�ø޽���, 300:Image, 500: Mouse Event, 600:�� ���� 601:�� ����
//700:�� ���� 701:�� ���� 702:�� ��� ���� 703:���� ��� ���� 704:�������� 800:���ӽ��� 801:�����غ� 802:�������� 803:�����ߴ� 
//900:�ٵϵ��Է� 901:����(�ٵϵ� �ϳ��� ����) 902:���� ��� 903: ������, 904: ������

// msg debug ���� : System.out.println("Debug ["+ "username:"+ msg.UserName + " code: "+ msg.code + " msg: " + msg.data+"]");
class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; 
	public String userName;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size; // pen size
	public int roomMax;
	public String roomName;
	public int roomNumber;
	public String gameMode;
	public int y;
	public int x;
	public int stone;
	
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.userName = UserName;
		this.data = msg;
	}
}