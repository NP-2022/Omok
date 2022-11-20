
// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;

//100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event, 600:�� ���� 601:�� ����
//700:�� ���� 701:�� ���� 702:�� ��� ���� 800:���ӽ��� 801:�����غ� 802:�������� 900:�ٵϵ��Է� 901:����(�ٵϵ� �ϳ��� ����) 902:���� undo(�ٵϵ� �ǵ�����)

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; 
	public String UserName;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size; // pen size
	public int roomMax;
	public String roomName;
	public int roomNumber;
	public String gameMode;
	
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
}