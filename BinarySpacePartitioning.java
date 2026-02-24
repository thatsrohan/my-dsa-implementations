import java.util.*;
import javax.swing.*;
import java.awt.*;

public class BSPDungeon extends JPanel {

    static final int MAP_W = 60;
    static final int MAP_H = 30;
    static final int MIN_SIZE = 8;
    static final int SCALE = 15;   // size multiplier for visibility

    static class Rect {
        int x, y, w, h;

        Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    static class Node {
        Rect area;
        Node left, right;
        Rect room;

        Node(Rect r) {
            area = r;
        }

        void split(Random rand) {
            if (area.w < MIN_SIZE * 2 && area.h < MIN_SIZE * 2) return;

            boolean vertical = rand.nextBoolean();
            if (area.w > area.h) vertical = true;
            else if (area.h > area.w) vertical = false;

            if (vertical) {
                int cut = rand.nextInt(area.w - MIN_SIZE * 2) + MIN_SIZE;
                left = new Node(new Rect(area.x, area.y, cut, area.h));
                right = new Node(new Rect(area.x + cut, area.y, area.w - cut, area.h));
            } else {
                int cut = rand.nextInt(area.h - MIN_SIZE * 2) + MIN_SIZE;
                left = new Node(new Rect(area.x, area.y, area.w, cut));
                right = new Node(new Rect(area.x, area.y + cut, area.w, area.h - cut));
            }

            left.split(rand);
            right.split(rand);
        }

        void createRooms(Random rand) {
            if (left == null && right == null) {
                int rw = rand.nextInt(area.w - 4) + 3;
                int rh = rand.nextInt(area.h - 4) + 3;
                int rx = area.x + rand.nextInt(area.w - rw);
                int ry = area.y + rand.nextInt(area.h - rh);
                room = new Rect(rx, ry, rw, rh);
            } else {
                left.createRooms(rand);
                right.createRooms(rand);
            }
        }
    }

    Node root;

    BSPDungeon(Node root) {
        this.root = root;
        setPreferredSize(new Dimension(MAP_W * SCALE, MAP_H * SCALE));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNode(g, root);
    }

    void drawNode(Graphics g, Node n) {

        // Draw partition boundary (optional visualization)
        g.setColor(Color.DARK_GRAY);
        g.drawRect(
                n.area.x * SCALE,
                n.area.y * SCALE,
                n.area.w * SCALE,
                n.area.h * SCALE
        );

        // Draw actual room
        if (n.room != null) {
            g.setColor(Color.GREEN);
            g.fillRect(
                    n.room.x * SCALE,
                    n.room.y * SCALE,
                    n.room.w * SCALE,
                    n.room.h * SCALE
            );
        }

        if (n.left != null) drawNode(g, n.left);
        if (n.right != null) drawNode(g, n.right);
    }

    public static void main(String[] args) {

        Random rand = new Random();

        Node root = new Node(new Rect(1, 1, MAP_W - 2, MAP_H - 2));
        root.split(rand);
        root.createRooms(rand);

        JFrame frame = new JFrame("BSP Dungeon Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BSPDungeon(root));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
