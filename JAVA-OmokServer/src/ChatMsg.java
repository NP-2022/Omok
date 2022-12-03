
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;
//000: 1초 시간 흐름
//100:로그인, 400:로그아웃, 200:채팅메시지, 201:게임방 채팅메시지, 300:Image, 500: Mouse Event, 600:방 생성 601:방 삭제
//700:방 입장 701:방 퇴장 702:방 목록 갱신 703:유저 목록 갱신 704:관전입장 800:게임시작 801:게임준비 802:게임종료 803:게임중단 
//900:바둑돌입력 901:복기(바둑돌 하나씩 제거) 902:복기 취소 903: 무르기, 904: 무르기

// msg debug 문장 : System.out.println("Debug ["+ "username:"+ msg.UserName + " code: "+ msg.code + " msg: " + msg.data+"]");
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