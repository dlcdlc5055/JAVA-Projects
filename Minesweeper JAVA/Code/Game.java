package com.company;
import com.raylib.Raylib;
import static com.raylib.Jaylib.*;
import static com.raylib.Raylib.DrawRectangle;
import java.lang.*;
import java.util.Random;

public class Game {
    private boolean state=false;
    private int default_nr_of_bomb=10;
    private int current_nr_of_bomb=0;
    private boolean[][] marked_bord=new boolean[10][10];
    private boolean[][] show_bord=new boolean[10][10];
    private int[][] bord=new int[10][10];
    private int bomb=-1;
    private int time_buffer=0;

    private int RandInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private int getNrOfBombs(){
        int count=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++) {
                if(bord[i][j]==-1){
                    count+=1;
                }
            }
        }
        return count;
    }

    private int getBordPoint(int x,int y){
        if(x>-1 && y>-1 && x<10 && y<10){
            return bord[x][y];
        }else{
            return 0;
        }
    }

    private int getNrOfNaiborBomb(int x,int y){
        int count=0;
        boolean[] values=new boolean[8];
        //vert
        values[0]=getBordPoint(x-1,y)==-1;
        values[1]=getBordPoint(x+1,y)==-1;
        //horz
        values[2]=getBordPoint(x,y-1)==-1;
        values[3]=getBordPoint(x,y+1)==-1;
        //d1
        values[4]=getBordPoint(x-1,y-1)==-1;
        values[5]=getBordPoint(x+1,y+1)==-1;
        //d2
        values[6]=getBordPoint(x+1,y-1)==-1;
        values[7]=getBordPoint(x-1,y+1)==-1;
        for(int i=0;i<8;i++){
            if(values[i]==true){
                count+=1;
            }
        }
        return count;
    }

    private void generateBord(){
        while (true){
            if(getNrOfBombs()==default_nr_of_bomb){
                break;
            }else{
                int x=RandInt(0,9);
                int y=RandInt(0,9);
                bord[x][y]=-1;
            }
        }

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++) {
                if(bord[i][j]!=-1){
                    bord[i][j]=getNrOfNaiborBomb(i,j);
                }
            }
        }
    }

    private void reset(){
        time_buffer=0;
        state=false;
        current_nr_of_bomb=default_nr_of_bomb;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++) {
                marked_bord[i][j]=false;
                show_bord[i][j]=false;
                bord[i][j]=0;
            }
        }
        generateBord();
    }

    private Vect2D getClickedSqere(){
        int x=(int)GetMousePosition().x();
        int y=(int)GetMousePosition().y();
        for(int i=0;i<11;i++){
            for(int j=0;j<10;j++) {
                int base_x=i*90+450-90-5;
                int base_y=j*90+5;
                int size=80;
                if(x<base_x && x< base_x+size && y>base_y && y< base_y+size){
                    Vect2D vector= new Vect2D(i-1,j);
                    return vector;
                }
            }
        }
        return new Vect2D(-1,-1);
    }

    private boolean checkForGameEnd(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(show_bord[i][j]==true && bord[i][j]==-1){
                    return true;
                }
            }
        }
        return false;
    }

    private void floodFillEmpty(int x,int y){
        if(getBordPoint(x,y)==0&& x>-1 && y>-1 && y<10 && x<10&& show_bord[x][y]==false){
            show_bord[x][y]=true;
            floodFillEmpty(x+1,y);
            floodFillEmpty(x-1,y);
            floodFillEmpty(x,y+1);
            floodFillEmpty(x,y-1);
        }else if(getBordPoint(x,y)>0){
            show_bord[x][y]=true;
            return;
        }
    }

    Game(){
        reset();
    }

    void update() {
        Vect2D clicked_sq = getClickedSqere();
        int x = clicked_sq.x;
        int y = clicked_sq.y;

        if(state==false){
            time_buffer+=16;
        }

        if (clicked_sq.x > -1 && clicked_sq.y > -1) {
            int item = bord[x][y];
            state = checkForGameEnd();
            if(state==false){
                if (IsMouseButtonPressed(MOUSE_LEFT_BUTTON)) {
                    if (item == 0) {
                        floodFillEmpty(x,y);
                    } else if (item > 0) {
                        show_bord[x][y] = true;
                    } else if (item == -1) {
                        show_bord[x][y] = true;
                    }
                } else if (IsMouseButtonPressed(MOUSE_RIGHT_BUTTON)) {
                    marked_bord[x][y] = true;
                    show_bord[x][y] = false;
                    current_nr_of_bomb -= 1;
                }
            }else{
                if(IsKeyPressed(KEY_SPACE)){
                    reset();
                }
            }
        }
}

    void draw(){
        if(state==false){
            Integer s=new Integer(current_nr_of_bomb);
            DrawText("BOMBS: "+s.toString(),10,10,50,BLACK);
            DrawRectangle(1600/2-450-5,0,910,910,DARKGRAY);
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++) {
                    if(marked_bord[i][j]==false){
                        DrawRectangle(i*90+450-90-5,j*90+5,80,80,GRAY);
                    }else{
                        DrawRectangle(i*90+450-90-5,j*90+5,80,80,YELLOW);
                    }
                    if(show_bord[i][j]==true){
                        if(bord[i][j]>0){
                            DrawRectangle(i*90+450-90-5,j*90+5,80,80,RAYWHITE);
                            Integer t = new Integer(bord[i][j]);
                            DrawText(t.toString(),i*90+450-90-5+25,j*90+5+15,50,BLACK);
                        }else if(bord[i][j]==0){
                            DrawRectangle(i*90+450-90-5,j*90+5,80,80,RAYWHITE);
                        }else if(bord[i][j]==-1){
                            DrawRectangle(i*90+450-90-5,j*90+5,80,80,RAYWHITE);
                            DrawCircle(i*90+450-90-5+40,j*90+5+40,30,RED);
                        }
                    }
                }
            }
        }else{
            DrawRectangle(1600/2-450-5,0,910,910,DARKGRAY);
            Integer s=new Integer(current_nr_of_bomb);
            DrawText("BOMBS: "+s.toString(),10,10,50,BLACK);
            Integer m_bomb=new Integer(0);
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++) {
                    if(marked_bord[i][j]==false){
                        DrawRectangle(i*90+450-90-5,j*90+5,80,80,GRAY);
                    }else if(marked_bord[i][j]==true && bord[i][j]!=-1){
                        DrawRectangle(i*90+450-90-5,j*90+5,80,80,GRAY);
                    }
                    if(bord[i][j]==-1 && marked_bord[i][j]==false){
                        DrawRectangle(i*90+450-90-5,j*90+5,80,80,ORANGE);
                        DrawCircle(i*90+450-90-5+40,j*90+5+40,30,RED);
                    }else if(bord[i][j]==-1 && marked_bord[i][j]==true){
                        DrawRectangle(i*90+450-90-5,j*90+5,80,80,YELLOW);
                        DrawCircle(i*90+450-90-5+40,j*90+5+40,30,RED);
                        m_bomb+=1;
                    }
                    if(show_bord[i][j]==true){
                        if(bord[i][j]>0) {
                            DrawRectangle(i * 90 + 450 - 90 - 5, j * 90 + 5, 80, 80, RAYWHITE);
                            Integer t = new Integer(bord[i][j]);
                            DrawText(t.toString(), i * 90 + 450 - 90 - 5 + 25, j * 90 + 5 + 15, 50, BLACK);
                        }else if(bord[i][j]==0){
                            DrawRectangle(i*90+450-90-5,j*90+5,80,80,RAYWHITE);
                        }
                    }
                }
            }
            int seconds=time_buffer/1000;
            DrawRectangle(1600-350,0,910,910,DARKBLUE);
            DrawText("Disarmed Bombs: "+m_bomb.toString(),1600-340,55*0+10,23,RAYWHITE);
            DrawText("Bombs Left: "+(new Integer(default_nr_of_bomb-m_bomb)).toString(),1600-340,55*1+10,23,RAYWHITE);
            DrawText("Time Eclipsed: "+(new Integer(seconds)).toString()+" Seconds",1600-340,55*2+10,23,RAYWHITE);
        }
    }
}
