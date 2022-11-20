
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;

//100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event, 600:방 생성 601:방 삭제
//700:방 입장 701:방 퇴장 702:방 목록 갱신 800:게임시작 801:게임준비 802:게임종료 900:바둑돌입력 901:복기(바둑돌 하나씩 제거) 902:복기 undo(바둑돌 되돌리기)

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