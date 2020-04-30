
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NguyenKhanh
 */
public class RopeLabel extends JLabel {

    private static final int LINE_MIN_ANGEL = 10;// goc ban dau cua moc 
    private static final int LINE_MAX_ANGEL = 170;// goc toi da cua moc
    private static final int LINE_MIN = 30; // do dai soi day cua moc
    private static final int LINE_MAX = 800; // do dai toi da cua soi day
    private static final int LINE_THICKNESS = 3; // toc do chay cua moc
    private static final int LINE_STEP = 10; // toc do tha xuong cua moc
    private static final Color LINE_COLOR = new Color(150, 80, 0, 230); // mau cua soi day
    Point start, end;
    ArrayList<Gold> list;
    GoldMiner parent;

    int line_angel = LINE_MIN_ANGEL; // goc bat dau cua soi day
    int line_d_angel = 1; // toc do dung dua cua soi day
    int line_lenght; // do dai cua soi day
    int line_d_lenght = 1; // do dai cong them cua soi day
    int line_step;
    int goldIndex;
    boolean scan = true;
    int F_WIDTH, F_WIDTH_2;
    int F_HEIGHT;
    BufferedImage achor;
    int achorWidth_2;
    int achorHeight;

    //creater hook and set locate
    public RopeLabel(GoldMiner parent, int F_WIDTH, int F_HEIGHT, ArrayList<Gold> list) {
        try {
            this.list = list;
            this.parent = parent;
            this.F_WIDTH = F_WIDTH;
            this.F_HEIGHT = F_HEIGHT;
            this.F_WIDTH_2 = F_WIDTH / 2;
            this.setBounds(0, 0, F_WIDTH, F_HEIGHT);
            //lay anh cua cai moc
            achor = ImageIO.read(this.getClass().getResource("/image/hook-sheet0.png"));
            //lay toa do cua moc hien tai
            achorWidth_2 = achor.getWidth() / 2;
            achorHeight = achor.getHeight();
            //reset lai soi day va goc 
            reset();
            //ve lai
            repaint();

        } catch (Exception e) {
            System.out.println("Error" + e);
        }

    }
    //reset lai do dai va goc quay cua moc 
    //reset location of variables
    public void reset() {
        start = new Point(F_WIDTH / 2, 75);
        end = new Point(F_WIDTH / 2, 75 + 300);
        line_lenght = LINE_MIN;
        line_angel = LINE_MIN_ANGEL;
        line_d_angel = 3;
        line_d_lenght = 1;
        goldIndex = -1;
        line_step = LINE_STEP;
    }

    //kiem tra trang thai cua moc
    //check status of hook
    public void shoot() {
        scan = false;
        repaint();
    }

    //kiem tra trang thai cua moc va lam moc di chuyen theo chu ki
    //motion state of hook
    private void updateLineAngel() {
        int index;
        //trang thai moc di chuyen qua lai
        if (scan) { // Move back and forth
            line_angel += line_d_angel;
            if (line_angel <= LINE_MIN_ANGEL || line_angel >= LINE_MAX_ANGEL) { // di chuyen nguoc lai
                line_d_angel = -line_d_angel;
            }
        // trang thai moc di chuyen len xuong 
        } else { // Move up and down
            line_lenght += line_d_lenght * line_step; // do dai cua soi day tang len
            if (line_lenght < LINE_MIN) { // neu do dai cua soi day nho hon do dai nho nhat thi dung lai
                line_d_lenght = 1;
                line_lenght = LINE_MIN;
                //cap nhat lai trang thai cua moc la di chuyen qua lai
                scan = true;
                //neu cham duoc vat
                if (goldIndex >= 0) {
                    //lay vat len 
                    Gold gt = list.get(goldIndex);
                    //dem vat den vi tri 0 - 0
                    gt.setLocation(0, 0);
                    // lam vat trong xuot
                    gt.setVisible(false);
                    //xoa vat ra khoi mang hinh
                    list.remove(goldIndex);
                    //cap nhat lai diem 
                    parent.updateScore(Gold.VALUE[gt.type]);
                    //hien thi lai diem
                    parent.showAddScore(Gold.VALUE[gt.type]);
                }
                //neu khong cham duoc gi
                goldIndex = -1;
                line_step = LINE_STEP;
            } else {
                index = isGetGold();
                // truong hop neu do dai soi day = voi do dai toi da cua soi day hoac cham den vi tri max hoac min cua Wildth
                // hoac cham den vi tri toi da cua Height hoac cham vao vat
                if (line_lenght >= LINE_MAX || end.getY() >= F_HEIGHT || end.getX() <= 0 || end.getX() >= F_WIDTH || index != -1) {
                    line_d_lenght = -1;
                    //neu cham vao vat
                    if (index >= 0) {
                        goldIndex = index;
                        // lay vat
                        Gold gt = list.get(goldIndex);
                        //keo vat len theo toc do da dat truoc
                        line_step = Gold.SPEED[gt.type];
                    }
                    // khi khong keo duoc gi
                    else{
                        goldIndex = index;
                        line_step = 50;
                    }
                }
            }
        }
        // di chuyen cua vat theo cai moc
        //Move thing along the rope
        if (goldIndex >= 0) {
            Gold gt = list.get(goldIndex);
            gt.setLocation((int) end.getX() + 40 - gt.r, (int) end.getY()  - gt.r);
        }
        updateRope();
    }
    //chuyen dong cua cai moc duoc tinh theo cong thuc phia duoi
    //motion cycle of hook
    private void updateRope() {
        end.setLocation(
                start.getX() + Math.cos(Math.toRadians(line_angel)) * line_lenght,
                start.getY() + Math.sin(Math.toRadians(line_angel)) * line_lenght);
    }
    // lay toa do va ban kinh cua vat de xem co cham vao vat hay khong
    //Move thing along the rope
    public int isGetGold() {  
        for (int i = 0; i < list.size(); i++) {
            if (Gold.distance((int) end.getX() + 40, (int) end.getY() + achorHeight, list.get(i)) <= list.get(i).r) {
                return i; //cham vao vat
            }
        }
        return -1;
    }
    
    
    //ve lai moc va soi day di chuyen
    //draw image hook and rope moving
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        updateLineAngel();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(LINE_THICKNESS));
        g2d.setColor(LINE_COLOR);
        //goc toa do cua soi day
        g2d.drawLine((int) start.getX() + 40, (int) start.getY(), (int) end.getX() + 40, (int) end.getY() );
        // ve lai cai moc khi khong gap duoc gi
        if (goldIndex == -1) {
            g.drawImage(achor, (int) end.getX() + 40 - achorWidth_2, (int) end.getY() , this);
        }
        // kiem tra Round
        parent.checkRound();
        // kiem tra de hien bang Win game
        parent.showResult_Win();
        // kiem tra de hien bang game over
        parent.showOverGame();
        //dieu kien de ve
        if(!parent.checkGolds()&&parent.time !=0){
          try {
            Thread.sleep(30);
            repaint();             
        } catch (InterruptedException e) {
            e.getMessage();
        }  
          
        }
        
        
                
        
        
    }

}
