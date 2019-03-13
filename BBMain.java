import javax.swing.JFrame;
public class BBMain
{
    public static final int WIDTH =640, HEIGHT = 480;
    public static void main(String args[])
    {
        JFrame theFrame = new JFrame("Brick Breaker");
        
        GamePanel thePanel = new GamePanel();
        
        //Thread theThread = new Thread(thePanel);
        
        theFrame.setLocationRelativeTo(null);
        theFrame.setResizable(false);
        theFrame.setSize(WIDTH,HEIGHT);
        
        theFrame.add(thePanel);
        
        //theThread.start();
        
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setVisible(true);
        
        thePanel.playGame();
    }
}
