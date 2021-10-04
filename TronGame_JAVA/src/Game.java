package com.company;
import com.raylib.Raylib;

import java.util.Stack;

import static com.raylib.Jaylib.*;

public class Game {
    private boolean[][] bord_p1=new boolean[1600][900];
    private boolean[][] bord_p2=new boolean[1600][900];
    private boolean[][] bord_draw_buffer=new boolean[1600][900];
    private Player[] player=new Player[2];
    private Stack<Rectangle> drawDataP1=new Stack<Rectangle>();
    private Stack<Rectangle> drawDataP2=new Stack<Rectangle>();
    private float speed=400f;
    boolean gameover=false;

    private int diference(int x1,int x2){
        if(x1<x2){
            return x2-x1;
        }
        if(x1>x2){
            return x1-x2;
        }
        return 0;
    }

    void play(){
        if(player[0].alive && player[1].alive) {
            this.update();
        }else{
            gameover=true;
        }
        this.draw();
    }

    Game(){
        for(int i=0;i<1600;i++){
            for(int j=0;j<900;j++){
                bord_p1[i][j]=false;
                bord_p2[i][j]=false;
                bord_draw_buffer[i][j]=false;
            }
        }
        player[0]=new Player(new Vect(1300,450),speed,BLUE);
        player[1]=new Player(new Vect(300,450),speed,GREEN);
    }
    private void clearDrawBuffer(){
        for(int i=0;i<1600;i++){
            for(int j=0;j<900;j++){
                bord_draw_buffer[i][j]=false;
            }
        }
    }
    private void update(){
        player[0].processUpdate();
        player[0].checkAlive(bord_p2);
        player[1].processUpdate();
        player[1].checkAlive(bord_p1);
        player[0].processInput(true);
        player[1].processInput(false);
        bord_p1=player[0].imPrintPlayer(bord_p1);
        bord_p2=player[1].imPrintPlayer(bord_p2);
    }
    private void generateRectForPixel(int x,int y){
        boolean p1=bord_p1[x][y];
        boolean p2=bord_p2[x][y];
        //p1
        if(p1 && !bord_draw_buffer[x][y]){
            int count_x=0;
            int count_y=0;
            while(true){
                count_x+=1;
                count_y+=1;
                try {
                    boolean buffer_p1 = bord_p1[x + count_x][y+count_y];
                    if(!buffer_p1 && !bord_draw_buffer[x+count_x][y+count_y]){
                        break;
                    }
                }catch (Exception e){ break; }
            }
            int[] buffer=new int[2];
            buffer[0]=count_x;
            buffer[1]=count_y;
            count_x=0;
            count_y=0;
            while(true){
                count_x+=1;
                try {
                    boolean buffer_p1 = bord_p1[x + count_x][y];
                    if(!buffer_p1 && !bord_draw_buffer[x+count_x][y]){
                        break;
                    }
                }catch (Exception e){ break; }
            }
            while(true){
                count_y+=1;
                try {
                    boolean buffer_p1 = bord_p1[x][y+count_y];
                    if(!buffer_p1 && !bord_draw_buffer[x][y+count_y]){
                        break;
                    }
                }catch (Exception e){ break; }
            }
            if(count_x<buffer[0]){
                count_x=buffer[0]-diference(count_x,buffer[0]);
            }else{
                count_x=count_x-diference(count_x,buffer[0]);
            }
            if(count_y<buffer[0]){
                count_y=buffer[0]-diference(count_y,buffer[1]);
            }else{
                count_y=count_y-diference(count_y,buffer[1]);
            }
            drawDataP1.push(new Rectangle(x,y,x+count_x,y+count_y,BLUE));
            bord_draw_buffer=drawDataP1.peek().imprint(bord_draw_buffer);
        }
        //p2
        if(p2 && !bord_draw_buffer[x][y]){
            int count_x=0;
            int count_y=0;
            while(true){
                count_x+=1;
                count_y+=1;
                try {
                    boolean buffer_p2 = bord_p2[x + count_x][y+count_y];
                    if(!buffer_p2 && !bord_draw_buffer[x+count_x][y+count_y]){
                        break;
                    }
                }catch (Exception e){ break; }
            }
            int[] buffer=new int[2];
            buffer[0]=count_x;
            buffer[1]=count_y;
            count_x=0;
            count_y=0;
            while(true){
                count_x+=1;
                try {
                    boolean buffer_p2 = bord_p2[x + count_x][y];
                    if(!buffer_p2 && !bord_draw_buffer[x+count_x][y]){
                        break;
                    }
                }catch (Exception e){ break; }
            }
            while(true){
                count_y+=1;
                try {
                    boolean buffer_p2 = bord_p2[x][y+count_y];
                    if(!buffer_p2 && !bord_draw_buffer[x][y+count_y]){
                        break;
                    }
                }catch (Exception e){ break; }
            }
            if(count_x<buffer[0]){
                count_x=buffer[0]-diference(count_x,buffer[0]);
            }else{
                count_x=count_x-diference(count_x,buffer[0]);
            }
            if(count_y<buffer[0]){
                count_y=buffer[0]-diference(count_y,buffer[1]);
            }else{
                count_y=count_y-diference(count_y,buffer[1]);
            }
            drawDataP2.push(new Rectangle(x,y,x+count_x,y+count_y,GREEN));
            bord_draw_buffer=drawDataP2.peek().imprint(bord_draw_buffer);
        }
    }
    private void draw(){
        clearDrawBuffer();
        for(int i=0;i<1600;i++){
            for(int j=0;j<900;j++){
                generateRectForPixel(i,j);
            }
        }
        while (!drawDataP1.empty()) {
            Rectangle buffer = drawDataP1.pop();
            buffer.draw();
        }
        while (!drawDataP2.empty()) {
            Rectangle buffer = drawDataP2.pop();
            buffer.draw();
        }
        if(!player[0].alive &&!player[1].alive){
            DrawText("DRAW!!",10,10,60,RAYWHITE);
        }else if(player[0].alive &&!player[1].alive){
            DrawText("PLAYER 2 WINS!!",10,10,60,RAYWHITE);
        }else if(!player[0].alive && player[1].alive){
            DrawText("PLAYER 1 WINS!!",10,10,60,RAYWHITE);
        }
    }
}