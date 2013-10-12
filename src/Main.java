import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import wtc.user.login.Screen.LoginMain;


public class Main {
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			Main main = new Main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main(){
		
		LoginMain login = new LoginMain();
		login.run();
		
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
}
