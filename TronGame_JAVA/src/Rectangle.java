package com.company;
import com.raylib.Jaylib;
import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Rectangle {
    private Vect pos;
    private Vect size;
    private Jaylib.Color clr;
    Rectangle(int x1,int y1,int x2,int y2,Jaylib.Color color){
        this.clr=color;
        pos=new Vect(x1,y1);
        size=new Vect(diference(x1,x2),diference(y1,y2));
    }
    private int diference(int x1,int x2){
        if(x1<x2){
            return x2-x1;
        }
        if(x1>x2){
            return x1-x2;
        }
        return 0;
    }
    public boolean[][] imprint(boolean[][]data){
        boolean[][] buffer=data;
        for(int i=pos.x;i< pos.x+size.x;i++){
            for(int j=pos.y;j< pos.y+size.y;j++){
                if(i>-1&&j>-1 &&i<1600 && j<900){
                    buffer[i][j]=true;
                }
            }
        }
        return buffer;
    }
    public void draw(){
        DrawRectangle(pos.x,pos.y,size.x,size.y,clr);
    }
}
