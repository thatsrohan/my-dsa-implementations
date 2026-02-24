import java.util.*;
public class BSPDngeon{
    static final int MAP_W = 60;
static final int MAP_H = 30;
static final int MIN_SIZE = 8;

static class rect{
    int x,y,w,h;
    rect(int x,int y,int w,int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    static class Node {
Rect area;
Node left,right;
Rect room;
Node(Rect r){ area=r; }
void split(Random rand){
    if(area.w < MIN_SIZE*2 && area.h < MIN_SIZE*2) return;
    boolean vertical = rand.nextBoolean();
    if(area.w > area.h) vertical = true;
    else if(area.h > area.w) vertical = false;
    if(vertical){
        int cut = rand.nextInt(area.w-MIN_SIZE*2)+MIN_SIZE;
        left =new Node(new Rect(area.x,area.y,cut,area.h));
        right = new Node(new Rect(area.x+cut,area.y,area.w-cut,area.h));
    }
    else{
        int cut = rand.nextInt(area.h-MIN_SIZE*2)+MIN_SIZE;
        left =new Node(new Rect(area.x,area.y,area.w,cut));
        right = new Node(new Rect(area.x,area.y+cut,area.w,area.h-cut));
    }
    left.split(rand);
right.split(rand);
}

}
}
}
