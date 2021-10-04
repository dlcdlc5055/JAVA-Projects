package com.company;


import static com.raylib.Jaylib.*;

public class CurrentPice {
    public int[] content=new int[3];
    public Vect pos=new Vect(3,-3);
    private Texture2D[] piceTextures;
    private float TimeBuffer=0;
    private float TimeMoveDown=.40f;

    CurrentPice(int[] content,Texture2D[] piceTextures,int[][] bord){
        if(bord[2][0]!=-1){
            while (true){
                int count=0;
                for(int i=0;i<6;i++){
                    if(bord[i][0]!=-1){
                        count+=1;
                    }
                }
                if(count==6){
                    break;
                }
                int buffer= (int)(Math.random() * 6);
                if(bord[buffer][0]==-1){
                    pos.x=buffer;
                    break;
                }
            }
        }
        this.content=content;
        this.piceTextures=piceTextures;
    }

    public void moveFromInput(int[][] bord){
        if(IsKeyPressed(KEY_A)){
            if(pos.x-1>-1 && pos.y+2<14 && pos.y+2>-1 && bord[pos.x-1][pos.y+2]==-1){
                pos.x=pos.x-1;
            }
        }else if(IsKeyPressed(KEY_D)){
            if(pos.x+1<6 && pos.y+2<14 && pos.y+2>-1 && bord[pos.x+1][pos.y+2]==-1){
                pos.x=pos.x+1;
            }
        }else if(IsKeyPressed(KEY_SPACE)){
            for(int i=0;i<14;i++){
                if(bord[pos.x][i]!=-1){
                    pos.y=i-3;
                    return;
                }else if(i==13){
                    pos.y=i-2;
                }
            }
        }else if(IsKeyPressed(KEY_W)){
            shuffle(true);
        }else if(IsKeyPressed(KEY_S)){
            shuffle(false);
        }
    }

    public void draw(){
        int bordStartX=417;
        int bordStartY=35;
        for(int i=0;i<3;i++){
            if(pos.y+i>=0){
                DrawTexture(piceTextures[content[i]],bordStartX+(pos.x*62),bordStartY+(int)((pos.y+i)*59.5),WHITE);
            }
        }
    }

    private void shuffle(boolean dir){
        int[] buffer=new int[3];
        for(int i=0;i<3;i++){
            buffer[i]=content[i];
        }
        if(dir){
            content[0]=buffer[1];
            content[1]=buffer[2];
            content[2]=buffer[0];
        }else{
            content[0]=buffer[2];
            content[1]=buffer[0];
            content[2]=buffer[1];
        }
    }

    public void update(int[][] bord){
        TimeBuffer+=GetFrameTime();
        if(TimeBuffer>TimeMoveDown){
            TimeBuffer=0;
            if(pos.y+3<14&&bord[pos.x][pos.y+3]==-1){
                pos.y+=1;
            }
        }
    }

    private boolean checkIfPosibleMove(int dir,int[][] bord){
        return false;
    }

    public boolean needPut(int[][] bord){
        boolean value = false;
        if(pos.y+3==14){
            value=true;
        }else if(bord[pos.x][pos.y+3]!=-1){
            value=true;
        }
        return value;
    }
}
