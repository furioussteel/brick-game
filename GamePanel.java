import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.Font;

import javax.swing.JPanel;
public class GamePanel extends JPanel
{
    //Fields
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;
    private MyMouseMotionListener theMouseListener;

    private int mousex;
    
    //entities
    Ball theBall;
    Paddle thePaddle;
    Map theMap;
    HUD theHud;

    //constructor
    public GamePanel()
    {
        init();
    }

    public void init()
    {
        mousex=0;
        theBall =new Ball();
        thePaddle = new Paddle();
        theMap = new Map(6,14);
        theHud = new HUD();
        theMouseListener = new MyMouseMotionListener();
        addMouseMotionListener(theMouseListener);
        running = true;
        image = new BufferedImage(BBMain.WIDTH, BBMain.HEIGHT, BufferedImage.TYPE_INT_RGB);
        g=(Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void playGame()
    {
        while(running)
        {
            //update
            update();

            //render or draw
            draw();

            //display
            repaint();

            try
            {
                Thread.sleep(12);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void checkCollisions()
    {
        Rectangle ballRect = theBall.getRect();
        Rectangle paddleRect = thePaddle.getRect();
        if(ballRect.intersects(paddleRect))
        {
            theBall.setDY(-theBall.getDY());
            if(theBall.getX()<mousex + thePaddle.getWidth()/4)
            {
                theBall.setDX(theBall.getDX()-0.5);
            }
            if(theBall.getX()<mousex+thePaddle.getWidth() && theBall.getX()> mousex+thePaddle.getWidth()/4)
            {
                theBall.setDX(theBall.getDX() + 0.5);
            }
        }
        //int[][] theMapArray = theMap.getMapArray();
        A: for(int row =0; row<theMap.getMapArray().length;row++)
        {
            for(int col=0; col< theMap.getMapArray()[0].length; col++)
            {
                if(theMap.getMapArray()[row][col]>0)
                {
                    int brickx=col*theMap.getBrickWidth()+theMap.HOR_PAD;
                    int bricky=row*theMap.getBrickHeight()+theMap.VERT_PAD;
                    int brickWidth=theMap.getBrickWidth();
                    int brickHeight=theMap.getBrickHeight();

                    Rectangle brickRect = new Rectangle(brickx, bricky, brickWidth, brickHeight);

                    if(ballRect.intersects(brickRect))
                    {
                        theMap.hitBrick(row,col);
                        theBall.setDY(-theBall.getDY());
                        theHud.addScore(50);
                        break A;
                    }
                }

            }
        }
    }

    public void update()
    {
        checkCollisions();
        theBall.update();
    }

    public void draw()
    {
        //draw background
        g.setColor(Color.WHITE);
        g.fillRect(0,0,BBMain.WIDTH, BBMain.HEIGHT);

        theMap.draw(g);
        theBall.draw(g);

        thePaddle.draw(g);
    
        theHud.draw(g);
        
        if(theMap.isThereAWin()==true)
        {
            drawWin();
            running=false;
        }
        
        if(theBall.youLose())
        {
            drawLoser();
            running=false;
        }
    }

    public void drawWin()
    {
        g.setColor(Color.RED);
        g.setFont(new Font("Courier New", Font.BOLD,50));
        g.drawString("YOU WIN !!",200,200);
    }
    
    public void drawLoser()
    {
        g.setColor(Color.RED);
        g.setFont(new Font("Courier New", Font.BOLD,50));
        g.drawString("YOU LOSE !!",200,200);
    }
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(image,0,0,BBMain.WIDTH, BBMain.HEIGHT, null);
        g2.dispose();
    }

    private class MyMouseMotionListener implements MouseMotionListener
    {
        @Override
        public void mouseDragged(MouseEvent e)
        {

        }

        public void mouseMoved(MouseEvent e)
        {
            mousex=e.getX();
            thePaddle.mouseMoved(e.getX());
        }
    }
}
