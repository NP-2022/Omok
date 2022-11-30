
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;

//100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event, 600:방 생성 601:방 삭제
//700:방 입장 701:방 퇴장 800:게임시작 801:게임준비 802:게임종료 900:바둑돌입력 901:복기(바둑돌 하나씩 제거) 902:복기 취소

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code;				//어떤 목적의 내용인지
	public String UserName;			//사용자 이름
	public String data;				//내용
	public ImageIcon img;			//이미지
	public MouseEvent mouse_e;		//마우스 이벤트
	public int pen_size; // pen size
	public int roomMax;				//방 최대 플레이 인원
	public String roomName;			//방 이름
	public int roomNumber;			//방번호
	public String gameMode;			//게임 종류
	public int y;
	public int x;
	public int stone;
	
	public ChatMsg(String UserName, String code, String msg) {	//유저 이름, 코드, 메세지 내용
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
	
}