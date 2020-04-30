
import java.awt.Point;
import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NguyenKhanh
 */
public class Randomier {

    int x, y;
    static Random rand;

    //ham random tu vi tri min den vi tri max
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();

        return r.nextInt((max - min) + 1) + min;
    }

    //random vi tri dia vao chieu dai va chieu cao
    public Point randomPoint(int width, int height) {
        x = width - 1;
        y = height - 1;
        return new Point(getRandomNumberInRange(300, x - 200), getRandomNumberInRange(960 / 3, y));
    //getRandomNumberInRange(960 / 3, y) co nghia la random toa do 1/3 mang hinh
    }

   

}
