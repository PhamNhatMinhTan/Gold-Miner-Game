
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NguyenKhanh
 */
public class Gold extends JLabel {

    public static final int Gold300 = 0;
    public static final int Gold200 = 1;
    public static final int Gold100 = 2;
    public static final int Rock50 = 3;
    public static final int Rock20 = 4;
    public static final int Diamond500 = 5;
    public static final int Bone5 = 6;
    public static final int Skull10 = 7;
    public static final String[] IMAGE = {"gold300", "gold200", "gold100", "rock50", "rock30", "diamond", "bone", "skull"};//name of image in files
    public static final int[] VALUE = {300, 200, 100, 50, 30, 500, 5, 10};//score of things
    public static final int[] SPEED = {20, 30, 40, 10, 20, 40, 10, 5};//speed of thing when pull up
    //upload size of things
    public static final Dimension[] SIZE = {new Dimension(100, 88), new Dimension(90, 82), new Dimension(53, 57), new Dimension(170, 66), new Dimension(82, 57), new Dimension(101, 82), new Dimension(55, 65), new Dimension(48, 64)};
    public static final int[] RADIUS = {44, 41, 35, 50, 29, 41, 32, 24};//radius of things (width/2 and height/2 Get the smallest of 2 number)
    public int x, y, tx, ty, r;
    int type;
    GoldMiner parent;

    public Gold(GoldMiner parent, Point p, int type) {
        init(parent, (int) p.getX(), (int) p.getY(), type);
    }

    public Gold(GoldMiner parent, int x, int y, int type) {
        init(parent, x, y, type);
    }

    //upload image with each type
    //them anh tung loai
    public void init(GoldMiner parent, int x, int y, int type) {
        this.type = type;
        this.setText("");
        this.setIcon(new ImageIcon(this.getClass().getResource("/image/" + IMAGE[type] + ".png")));
        this.setVerticalAlignment(SwingUtilities.CENTER);
        this.setHorizontalAlignment(SwingUtilities.CENTER);
        updateLocation(x, y);
        this.parent = parent;
        this.x = x;
        this.tx = x + this.getWidth() / 2;
        this.y = y;
        this.ty = y + this.getHeight() / 2;
        this.r = RADIUS[type];
    }
    // them vat vao vi tri x t va kich thuoc cua vat
    //upload image with x y width and height
    public void updateLocation(int x, int y) {
        this.setBounds(x, y, (int) SIZE[type].getWidth(), (int) SIZE[type].getHeight());
    }
    //ham tinh khoang cach tam va diem giua cua 2 vat
    //find distance of things
    public static int distance(int x, int y, Gold gt2) {
        return (int) Math.sqrt(Math.pow(x - gt2.tx, 2) + Math.pow(y - gt2.ty, 2));
    }

}
