
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import jdk.nashorn.internal.ir.TryNode;
import sun.applet.Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NguyenKhanh
 */
public class GoldMiner extends javax.swing.JFrame {

    public static int NUM_Gold300 = 4;
    public static int NUM_Gold200 = 2;
    public static int NUM_Gold100 = 2;
    public static int NUM_Rock50 = 1;
    public static int NUM_Rock20 = 2;
    public static int NUM_Diamond500 = 1;
    public static int NUM_Bone5 = 1;
    public static int NUM_Skull10 = 1;
    public static final int F_WIDTH = 1400;
    public static final int F_HEIGHT = 600;
    public static final int T_OVER = 0;
    public static final int T_WIN = 1;
    public int[] numcheckScore = {1000, 2000, 3000, 4000};
    private boolean checkScore = false;
    private int countRound = -1;
    private int Round = 0;
    private int EndGame = 3;
    int score = 0;
    int time = 120;
    int tmpScore;
    ArrayList<Gold> list;
    boolean checkPlay = true;
    boolean reset = true;
    Randomier rand;
    RopeLabel lblRopeAnimation;
    Thread timer, t;
    boolean Start = false;

    /**
     * Creates new form GoldMiner
     */
    public GoldMiner() {
        initComponents();
        //add icon of frame
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/icon.png"));
        setIconImage(icon);
        //kich thuoc cua frame
        setSize(F_WIDTH - 50, F_HEIGHT + 100);
        //dat cac label icon theo toa do
        FrameScore_Time.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/image/guiframe-sheet0.png")).getImage().getScaledInstance(432, 350, Image.SCALE_SMOOTH)));
        FrameScore_Time.setLocation(50, -80);
        FScore.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/image/guitarget-sheet0.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        FScore.setLocation(80, 18);
        FTime.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/image/guitime-sheet0.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        FTime.setLocation(80, 85);
        lbMenu.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/image/buttonmenu-sheet0.png")).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));        
        lbMenu.setLocation(1250, 0);
        lbaddScore.setLocation(330, 30);
        setResizable(false);//lock size
        //chinh mang hinh ra giua
        setLocationRelativeTo(null);
        //chinh toa do cua chiec can cau
        lbPlayer.setLocation(F_WIDTH / 2 - lbPlayer.getWidth() / 2 + 248, 50);
        lbScore.setLocation(180, 30);
        lbTime.setLocation(170, 120);
        //cho cac label thong bao trong suot
        lbOvergame.setVisible(false);
        lbWingame.setVisible(false);
        lbHelpNextRound.setVisible(false);
        lbHelpOver.setVisible(false);
        lbRound.setVisible(false);
        lbaddScore.setVisible(false);
        //reset diem = 0
        updateScore(score);
    }
    
    //ham de load nhung vat co trong game (vang, da, kim cuong, xuong)
    private void generateGolds() { // method load things (Gold and rock)
        pnlGold.setLayout(null);
        pnlGold.removeAll();
        pnlGold.revalidate();
        pnlGold.repaint();
        pnlGold.setSize(F_WIDTH, F_HEIGHT);//dat kich thuoc cua cai pannel load vang
        //khoi tao ham random de random vi tri
        rand = new Randomier();
        //khoi tao danh sach de luu cac vat vao trong array list
        list = new ArrayList<Gold>();
        //random cac vat tu kich thuoc lon nhat den nho nhat
        for (int i = 0; i < NUM_Rock50; i++) {
            showGold(Gold.Rock50);
        }
        for (int i = 0; i < NUM_Diamond500; i++) {
            showGold(Gold.Diamond500);
        }
        for (int i = 0; i < NUM_Gold300; i++) {
            showGold(Gold.Gold300);
        }
        for (int i = 0; i < NUM_Gold200; i++) {
            showGold(Gold.Gold200);
        }
        for (int i = 0; i < NUM_Gold100; i++) {
            showGold(Gold.Gold100);
        }
        for (int i = 0; i < NUM_Rock20; i++) {
            showGold(Gold.Rock20);
        }
        for (int i = 0; i < NUM_Bone5; i++) {
            showGold(Gold.Bone5);
        }
        for (int i = 0; i < NUM_Skull10; i++) {
            showGold(Gold.Skull10);
        }
    }

    //show things on Interface
    //hien thi cac vat len tren mang hinh
    private void showGold(int type) {
        //them cac vat vao danh sach
        list.add(new Gold(this, getPositon(type), type));
        //them cac vat len tren panel
        pnlGold.add(list.get(list.size() - 1));
    }

    //kiem tra ban kinh cua cac vat trong 1 panel
    //check radius  of all things
    private Point getPositon(int type) {
        //tao bien dem de khong bi treo may khi vat do khong chon duoc vi tri trong
        int count = 0;
        int x, y;
        do {
            //random toa do trong khoang Width va Height ( xem ham randomize de ro hon)
            Point p = rand.randomPoint(F_WIDTH - 100, F_HEIGHT - 80);
            //tim toa do ngau nhien x y de dat vat vao vi tri 
            x = (int) p.getX();
            y = (int) p.getY();
            count++;
            //neu random khong co toa do thich hop van xe them vat vao pannel
            if (count >= 1000) {
                break;
            }
         //kiem tra toa do x,y va ban kinh cua vat xe co nam chong len nhau khong
        } while (check(x, y, Gold.RADIUS[type]) == false);
        //tra ve toa do x y
        return new Point(x, y);
    }
    //kiem tra tam va diem giua cua cac vat xem co nam chong len nhau khong
    //check distance  of all things
    private boolean check(int x, int y, int r) {
        int d;
        //quet het danh sach cua vat xem toa do va tam co nam chong len nhau khong neu co thi return false nguoc lai return true
        for (int i = 0; i < list.size(); i++) {
            d = Gold.distance(x, y, list.get(i)); // kiem tra tam va ban kinh cua tung vat
            if (d <= r + list.get(i).r) { // neu duong thang noi 2 tam cua 2 vat co do dai cong lai lon hon do dai khoang cach cua 
                //diem random thi return true ( d > R1 + R2)
              
                return false;
            }
        }
        return true;
    }
    //hien thi thoi gian chay cua game
    //show time run on interface
    public void updateTime() {
        lbTime.setText(int2time(time));
        timer = new Thread() {
            public void run() {
                try {
                    while (time >= 0) {
                        lbTime.setText(int2time(time));
                        System.out.println(time);
                        sleep(1000);
                        time--;
                    }
                    if (time <= 0) {
                        lbTime.setText("00:00");
                    }
                } catch (Exception e) {
                    System.out.println("Error");
                }

            }

        };
        timer.start();
    }
    //method devide 1 number to time 
    private String int2time(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    //reset program
    public void reset() {
        this.time = 120;
        updateTime();
        updateRound();

    }

    //show image Win 
    public void showResult_Win() {
        //kiem tra dieu kien de hien thi mang hinh win game
        //khi cau duoc het so vang va thoi gian lon hon 0 hoac du so diem yeu cau va het thoi gian 
        if ((checkGolds() && time > 0 && Round <= EndGame && Round > 0) || (checkScore && time <= 0 && Round <= EndGame && Round > 0)) {

            lbaddScore.setVisible(false);
            lbRound.setVisible(false);
            checkPlay = true;
            lbWingame.setVisible(true);
            lbHelpNextRound.setVisible(true);
            timer.stop();
            // khi cau het so vang va thoi gian lon hon 0 nhung da het so round thi xe quay lai round 1
        } else if ((checkGolds() && time > 0 && Round > EndGame) || (checkScore && time <= 0 && Round > EndGame)) {
            lbaddScore.setVisible(false);
            lbRound.setVisible(false);
            checkPlay = true;
            lbWingame.setVisible(true);
            lbHelpOver.setVisible(true);
            timer.stop();
            if (Round > EndGame) {
                Round = 0;
            }
        }
    }
    
    //kiem tra xem so diem co hon duoc so diem yeu cau cua tung round hay chua
    //check score have greater than score of round or not
    public void checkRound() {
        if (score >= numcheckScore[countRound]) {
            checkScore = true;
        }
    }

    //hien thi len mang hinh game over
    //show image Game Over
    public void showOverGame() {
        //check dieu kiem neu het thoi gian ma diem khong du diem qua mang
        if (time <= 0 && !checkScore) {
            lbaddScore.setVisible(false);
            lbRound.setVisible(false);
            checkPlay = true;
            lbOvergame.setVisible(true);
            lbHelpOver.setVisible(true);
            timer.stop();
            Round = 0;
            score = 0;
            //reset diem  = 0;
            updateScore(score);
        }
    }
    // qua mang tang len 1 so thu  lam tang do kho cho game :))
    //upload next round
    public void nextRound() {
        if (Round >= 1 && Round <= EndGame) {
            NUM_Rock20++;
            NUM_Gold300--;
            NUM_Gold100++;
            NUM_Bone5++;
            NUM_Skull10++;
            Round++;
        }

    }

    //reset lai round 1
    //upload round 1
    public void Round1() {
        NUM_Gold300 = 4;
        NUM_Gold200 = 2;
        NUM_Gold100 = 2;
        NUM_Rock50 = 1;
        NUM_Rock20 = 2;
        NUM_Bone5 = 1;
        NUM_Skull10 = 1;
        Round++;
    }

    // hien thi diem cua tuong vat duoc cong them vao
    //show value score of things 
    public void showAddScore(int value) {
        lbaddScore.setVisible(true);
        if (value == Gold.VALUE[0]
                || value == Gold.VALUE[1]
                || value == Gold.VALUE[2]
                || value == Gold.VALUE[3]
                || value == Gold.VALUE[4]
                || value == Gold.VALUE[5]
                || value == Gold.VALUE[6]
                || value == Gold.VALUE[7]) {
            lbaddScore.setText("+ " + value);
        }

    }
    // label hien thi so round hien tai
    //label show planging round
    public void updateRound() {
        lbRound.setVisible(true);
        lbRound.setText("Round " + (Round));
    }
    // upload diem hien co
    //update and show Score
    public void updateScore(int score) {
        this.score += score;
        lbScore.setText(this.score + "");
        tmpScore = this.score;
    }
    //kiem tra so vang trong danh sach neu het so vang trong list thi se return true
    //check number things in list 
    public boolean checkGolds() {
        int n = list.size();
        if (n <= 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbMenu = new javax.swing.JLabel();
        lbaddScore = new javax.swing.JLabel();
        lbHelpNextRound = new javax.swing.JLabel();
        lbHelpOver = new javax.swing.JLabel();
        lbRound = new javax.swing.JLabel();
        lbHelp1 = new javax.swing.JLabel();
        lbOvergame = new javax.swing.JLabel();
        lbWingame = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lbScore = new javax.swing.JLabel();
        lbHelp = new javax.swing.JLabel();
        lbHelp2 = new javax.swing.JLabel();
        pnlGold = new javax.swing.JPanel();
        pnlRope = new javax.swing.JPanel();
        FScore = new javax.swing.JLabel();
        FTime = new javax.swing.JLabel();
        FrameScore_Time = new javax.swing.JLabel();
        lbPlayer = new javax.swing.JLabel();
        lbBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game Gold Miner");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        lbMenu.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        lbMenu.setForeground(new java.awt.Color(189, 106, 5));
        lbMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/buttonmenu-sheet0.png"))); // NOI18N
        lbMenu.setToolTipText("ABOUT MENU");
        lbMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbMenuMouseClicked(evt);
            }
        });
        getContentPane().add(lbMenu);
        lbMenu.setBounds(1220, 20, 120, 120);

        lbaddScore.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbaddScore.setForeground(new java.awt.Color(204, 102, 0));
        lbaddScore.setText("+0");
        getContentPane().add(lbaddScore);
        lbaddScore.setBounds(340, 20, 80, 50);

        lbHelpNextRound.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbHelpNextRound.setForeground(new java.awt.Color(255, 255, 255));
        lbHelpNextRound.setText("Do you want to level up?. Please press \"Enter\" to next Round");
        getContentPane().add(lbHelpNextRound);
        lbHelpNextRound.setBounds(280, 310, 740, 50);

        lbHelpOver.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbHelpOver.setForeground(new java.awt.Color(255, 255, 255));
        lbHelpOver.setText("Do you want to play againt ?. Please press \"Enter\" to play againt");
        getContentPane().add(lbHelpOver);
        lbHelpOver.setBounds(280, 310, 820, 50);

        lbRound.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        lbRound.setForeground(new java.awt.Color(0, 102, 51));
        lbRound.setText("Round 1");
        getContentPane().add(lbRound);
        lbRound.setBounds(70, 180, 220, 100);

        lbHelp1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbHelp1.setForeground(new java.awt.Color(255, 255, 255));
        lbHelp1.setText("Press \"Space\" key on keyboard to drop anchor");
        getContentPane().add(lbHelp1);
        lbHelp1.setBounds(420, 370, 650, 50);

        lbOvergame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/gameover.png"))); // NOI18N
        getContentPane().add(lbOvergame);
        lbOvergame.setBounds(160, 330, 1050, 330);

        lbWingame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/win.png"))); // NOI18N
        getContentPane().add(lbWingame);
        lbWingame.setBounds(150, 340, 1050, 330);

        lbTime.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbTime.setForeground(new java.awt.Color(255, 255, 255));
        lbTime.setText("02:00");
        getContentPane().add(lbTime);
        lbTime.setBounds(160, 120, 80, 50);

        lbScore.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbScore.setForeground(new java.awt.Color(255, 255, 255));
        lbScore.setText("0");
        getContentPane().add(lbScore);
        lbScore.setBounds(190, 0, 80, 50);

        lbHelp.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbHelp.setForeground(new java.awt.Color(255, 255, 255));
        lbHelp.setText("Press the \"Enter\" key on the keyboard to start playing");
        getContentPane().add(lbHelp);
        lbHelp.setBounds(420, 270, 650, 50);

        lbHelp2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbHelp2.setForeground(new java.awt.Color(255, 255, 255));
        lbHelp2.setText("Press \"Esc\" on the keyboard to exit");
        getContentPane().add(lbHelp2);
        lbHelp2.setBounds(420, 320, 650, 50);

        pnlGold.setMaximumSize(new java.awt.Dimension(1400, 800));
        pnlGold.setMinimumSize(new java.awt.Dimension(1400, 480800));
        pnlGold.setOpaque(false);
        pnlGold.setPreferredSize(new java.awt.Dimension(1400, 480));
        pnlGold.setLayout(null);
        getContentPane().add(pnlGold);
        pnlGold.setBounds(0, 0, 1800, 1000);

        pnlRope.setMaximumSize(new java.awt.Dimension(2000, 1400));
        pnlRope.setMinimumSize(new java.awt.Dimension(2000, 1400));
        pnlRope.setOpaque(false);
        pnlRope.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnlRopeKeyPressed(evt);
            }
        });
        pnlRope.setLayout(null);
        getContentPane().add(pnlRope);
        pnlRope.setBounds(0, 0, 1860, 1340);

        FScore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/guitarget-sheet0.png"))); // NOI18N
        getContentPane().add(FScore);
        FScore.setBounds(30, 30, 68, 68);

        FTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/guitime-sheet0.png"))); // NOI18N
        getContentPane().add(FTime);
        FTime.setBounds(20, 140, 100, 110);

        FrameScore_Time.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/guiframe-sheet0.png"))); // NOI18N
        getContentPane().add(FrameScore_Time);
        FrameScore_Time.setBounds(20, 80, 430, 540);

        lbPlayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/excavator-sheet1.png"))); // NOI18N
        getContentPane().add(lbPlayer);
        lbPlayer.setBounds(760, 340, 432, 193);

        lbBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background-sheet0.png"))); // NOI18N
        lbBackground.setMaximumSize(new java.awt.Dimension(1400, 800));
        lbBackground.setMinimumSize(new java.awt.Dimension(1400, 800));
        lbBackground.setPreferredSize(new java.awt.Dimension(1400, 800));
        getContentPane().add(lbBackground);
        lbBackground.setBounds(-50, 0, 1400, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

        int k = evt.getKeyCode();
        // when prees space
        if (k == KeyEvent.VK_SPACE) { // khi bam dau space thi vat se ban day moc xuong 
            lbaddScore.setVisible(false);
            lblRopeAnimation.shoot();
        }
        //when press ESC 
        //bam Esc thi se thoat chuong trinh
        if (k == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        //bam enter de bat dau game
        //when press Enter
        if (k == KeyEvent.VK_ENTER) {
            if (checkPlay) {//lock press enter when playing
                checkScore = false;
                lbHelpNextRound.setVisible(false);
                lbOvergame.setVisible(false);
                lbWingame.setVisible(false);
                lbHelpOver.setVisible(false);

                countRound++;

                if (!checkScore && time >= 0 && Round > 0) {//check next round if round 1 not upload
                    nextRound();
                    generateGolds();
                    reset();
                    pnlRope.setLayout(null);
                    pnlRope.removeAll();
                    pnlRope.revalidate();
                    pnlRope.repaint();
                    pnlRope.setSize(F_WIDTH, F_HEIGHT);
                    lblRopeAnimation = new RopeLabel(this, F_WIDTH, F_HEIGHT, list);
                    pnlRope.add(lblRopeAnimation);
                    lbHelp.setVisible(false);
                    lbHelp1.setVisible(false);
                    lbHelp2.setVisible(false);
                    checkPlay = false;

                } else if (!checkScore && time >= 0 && Round == 0) {//check round 1
                    score = 0;
                    updateScore(score);
                    countRound = 0;
                    Round1();
                    generateGolds();
                    reset();
                    pnlRope.setLayout(null);
                    pnlRope.removeAll();
                    pnlRope.revalidate();
                    pnlRope.repaint();
                    pnlRope.setSize(F_WIDTH, F_HEIGHT);
                    lbHelp.setVisible(false);
                    lbHelp1.setVisible(false);
                    lbHelp2.setVisible(false);
                    lblRopeAnimation = new RopeLabel(this, F_WIDTH, F_HEIGHT, list);
                    pnlRope.add(lblRopeAnimation);
                    checkPlay = false;
                }

            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void pnlRopeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnlRopeKeyPressed

    }//GEN-LAST:event_pnlRopeKeyPressed

    private void lbMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbMenuMouseClicked
        Menu mn = new Menu();
        mn.setVisible(true);
    }//GEN-LAST:event_lbMenuMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GoldMiner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GoldMiner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GoldMiner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GoldMiner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GoldMiner().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FScore;
    private javax.swing.JLabel FTime;
    private javax.swing.JLabel FrameScore_Time;
    private javax.swing.JLabel lbBackground;
    private javax.swing.JLabel lbHelp;
    private javax.swing.JLabel lbHelp1;
    private javax.swing.JLabel lbHelp2;
    private javax.swing.JLabel lbHelpNextRound;
    private javax.swing.JLabel lbHelpOver;
    private javax.swing.JLabel lbMenu;
    private javax.swing.JLabel lbOvergame;
    private javax.swing.JLabel lbPlayer;
    private javax.swing.JLabel lbRound;
    private javax.swing.JLabel lbScore;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbWingame;
    private javax.swing.JLabel lbaddScore;
    private javax.swing.JPanel pnlGold;
    private javax.swing.JPanel pnlRope;
    // End of variables declaration//GEN-END:variables

}
