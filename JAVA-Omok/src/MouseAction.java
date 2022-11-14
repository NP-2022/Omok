import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
public class MouseAction extends MouseAdapter{
	private GameMethod gm; 
	private MapSize m; 
	private ShowMap sm;
	private RunGame g;
	
	public MouseAction(GameMethod gm,MapSize m) {
		this.gm=gm;
		this.m=m;
		this.sm= new ShowMap(m, gm);
		this.g= new RunGame("aaa");
	}
	@Override
	public void mousePressed(MouseEvent me) {
		int x = (int)Math.round(me.getX()/(double) m.getCell())-1;
		int y = (int)Math.round(me.getY()/(double) m.getCell())-2;
		
		if(gm.checkInput(y, x) == false) {
			return;
		}
		
		Stone w = new Stone(y,x,gm.getCun_GamePlayer());
		gm.inputWord(w);
		gm.nextPlayer(gm.getCun_GamePlayer());
		sm.repaint();
		if(gm.endGame(w)==true) {
			String ms;
			if(w.getColor()==1) {
				ms="°Ëµ¹½Â¸®!";
			}
			else if(w.getColor()==2) {
				ms="¹éµ¹½Â¸®!";
			}
			else {
				ms="Àûµ¹½Â¸®!";
			}
			showWin(ms);
			gm.init();
		}
	}
	public void showWin(String msg) {
		JOptionPane.showMessageDialog(g, msg, "",JOptionPane.INFORMATION_MESSAGE);
		
	}
}