package com.company;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;
import java.lang.Math;

public class Player {
    private Vect pos;
    private float speedBase;
    private float speed;
    private int dir;
    private Color color;
    private Rectangle head;
    private int size=18;
    private int UP,DOWN,LEFT,RIGHT;
    public boolean alive=true;
    Player(Vect pos,float speed,Color color){
        this.pos=pos;
        this.dir=(int)(Math.random() * 4);
        this.color=color;
        this.speedBase=speed;
        this.speed=speed;
        head=new Rectangle(pos.x,pos.y, pos.x+size, pos.y+size,this.color);
        UP=0;
        DOWN=1;
        LEFT=2;
        RIGHT=3;
        alive=true;
    }

    boolean [][] imPrintPlayer(boolean[][] playerBord){
        boolean[] b2=new boolean[size];
        boolean[][] buffer=playerBord;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                try{
                    buffer[pos.x+i][pos.y+j]=true;
                }catch (Exception e){}
            }
        }
        for(int i=0;i<size;i++){
            if(dir==LEFT){
                try{
                    b2[i]=playerBord[pos.x-1][pos.y+i];
                }catch (Exception e){
                    b2[i]=false;
                }
            }
            if(dir==UP){
                try{
                    b2[i]=playerBord[pos.x+i][pos.y-1];
                }catch (Exception e){
                    b2[i]=false;
                }
            }
            if(dir==DOWN){
                try{
                    b2[i]=playerBord[pos.x+i][pos.y+size];
                }catch (Exception e){
                    b2[i]=false;
                }
            }
            if(dir==RIGHT){
                try{
                    b2[i]=playerBord[pos.x+size][pos.y+i];
                }catch (Exception e){
                    b2[i]=false;
                }
            }
        }
        for(int i=0;i<size;i++){
            if(b2[i]){
                System.out.println("dir:");
                System.out.println(dir);
                System.out.println("i:");
                System.out.println(i);
                this.alive=false;
            }
        }
        return buffer;
    }
    void processInput(boolean id){
        if(!id){
            if(IsKeyPressed(KEY_W) && dir!=DOWN){
                dir=UP;
            }
            if(IsKeyPressed(KEY_S)&& dir!=UP){
                dir=DOWN;
            }
            if(IsKeyPressed(KEY_A)&& dir!=RIGHT){
                dir=LEFT;
            }
            if(IsKeyPressed(KEY_D)&& dir!=LEFT){
                dir=RIGHT;
            }
        }else{
            if(IsKeyPressed(KEY_UP) && dir!=DOWN){
                dir=UP;
            }
            if(IsKeyPressed(KEY_DOWN)&& dir!=UP){
                dir=DOWN;
            }
            if(IsKeyPressed(KEY_LEFT)&& dir!=RIGHT){
                dir=LEFT;
            }
            if(IsKeyPressed(KEY_RIGHT)&& dir!=LEFT){
                dir=RIGHT;
            }
        }
    }
   void processUpdate(){
        this.speed=this.speedBase*GetFrameTime();
        if(speed>size){
            speed=size;
        }
        if(dir==UP){
            pos.y-=(int)(speed);
            if(pos.y<0){
                pos.y=900+ pos.y;
            }
        }
        if(dir==DOWN){
            pos.y+=(int)(speed);
            pos.y=pos.y%900;
        }
        if(dir==LEFT){
            pos.x-=(int)(speed);
            if(pos.x<0){
                pos.x=1600+ pos.x;
            }
        }
        if(dir==RIGHT){
            pos.x+=(int)(speed);
            pos.x=pos.x%1600;
        }
        head=new Rectangle(pos.x,pos.y, pos.x+size, pos.y+size,this.color);
        head.draw();
    }
    void checkAlive(boolean[][] enemyBord){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                    int x=pos.x+i;
                    int y=pos.y+j;
                    if(x>0){
                        x=x%1600;
                    }
                    if(y>0){
                        y=y%900;
                    }
                    if(x<0){
                        x=1600+x;
                    }
                    if(y<0){
                        y=900+y;
                    }
                    boolean buffer=enemyBord[x][y];
                    if(buffer){
                        this.alive=false;
                    }
            }
        }
    }
}
