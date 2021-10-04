package com.company;
import com.raylib.Jaylib;
import static com.raylib.Jaylib.*;
import static com.raylib.Raylib.DrawRectangle;
import java.lang.Math;

public class Game {
    //variables
    private int block_size=60;
    private int score=0;
    private int jewels=0;
    private int[][] bord=new int[6][14];
    private int[] nextPice=new int[3];
    private CurrentPice currentPice;
    private int dir=-1;
    private int level=0;
    private Texture2D background;
    private Texture2D[] piceTextures=new Texture2D[5];
    private int counter = 0;
    private boolean waiting=false;
    private boolean[][] bordToRemove=new boolean[6][14];
    private float timeBuffer=0.0f;
    private float waitTime=1.25f;
    private float flikerTimeBuffer=0;
    private float flikerTime=.175f;
    private Texture2D GameOverScreen;
    private Texture2D StartUpScreen;
    private boolean gameover=false;
    private boolean StartingUp=true;
    private boolean[] level_buffer=new boolean[6];
    private int ScoreBuffer=0;
    //reset function
    private void reset(){
        this.score=0;
        this.level=0;
        this.jewels=0;
        for(int i=0;i<6;i++) {
            this.level_buffer[i]=false;
        }
        this.timeBuffer=0.0f;
        this.gameover=false;
        this.flikerTimeBuffer=0;
        for(int i=0;i<6;i++){
            for(int j=0;j<14;j++){
                bord[i][j]=-1;
                bordToRemove[i][j]=false;
            }
        }
        this.currentPice=new CurrentPice(generateNextPice(),piceTextures,bord);
        this.nextPice=generateNextPice();
    }
    //constructor
    Game(){
        this.score=0;
        this.level=0;
        this.jewels=0;
        for(int i=0;i<6;i++) {
            this.level_buffer[i]=false;
        }
        this.timeBuffer=0.0f;
        this.flikerTimeBuffer=0;
        this.gameover=false;
        this.background = Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\background.png");
        for(int i=0;i<6;i++){
            for(int j=0;j<14;j++){
                bord[i][j]=-1;
                bordToRemove[i][j]=false;
            }
        }
        this.currentPice=new CurrentPice(generateNextPice(),piceTextures,bord);
        this.nextPice=generateNextPice();
        this.piceTextures[0]=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\red_pice.png");
        this.piceTextures[1]=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\blue_pice.png");
        this.piceTextures[2]=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\yellow_pice.png");
        this.piceTextures[3]=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\orange_pice.png");
        this.piceTextures[4]=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\purple_pice.png");
        this.StartUpScreen=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\startup_img.png");
        this.GameOverScreen=Jaylib.LoadTexture("C:\\Users\\dlcdl\\IdeaProjects\\Collmuns_JAVA\\src\\com\\company\\game_over_screen.png");
    }
    //drawing functions
    public void draw(){
        if(!StartingUp ||timeBuffer>1.8){
            if(!gameover){
                drawBackgroud();
                currentPice.draw();
                DrawNextPice();
                drawBord();
                drawGrid();
                DrawValues();
            }else{
                DrawTexture(GameOverScreen,0,0,WHITE);
                Jaylib.Color color=YELLOW;
                double value=1.4;
                color.r((byte)(92*value));
                color.g((byte)(78*value));
                color.b((byte)(22*value));
                DrawText("Score: "+String.valueOf(score),10,10,45,color);
                DrawText("Levels: "+String.valueOf(level),10,60,45,color);
                DrawText("Jewels: "+String.valueOf(jewels),10,110,45,color);
            }
        }else{
            DrawTexture(StartUpScreen,0,0,WHITE);
        }
    }
    //draw grid
    private void drawGrid(){
        //x grid
        for(int x=1;x<6;x++){
            DrawRectangle(417+(x*62),35,2,835,DARKGRAY);
        }
        //y grid
        for(int y=1;y<14;y++){
            DrawRectangle(417,(int)(35+(59.5*y)),372,2,DARKGRAY);
        }
        //next pice grid
        for(int i=1;i<3;i++){
            DrawRectangle(314,(int)(35+(60*i)),60,2,DARKGRAY);
        }
    }
    //draw nextpice
    private void DrawNextPice(){
        int startX=316;
        int startY=33;
        for(int i=0;i<3;i++){
            DrawTexture(piceTextures[nextPice[i]],startX,startY+(60*i)+5,WHITE);
        }
    }
    //draw values
    private void DrawValues(){
        Jaylib.Color color=YELLOW;
        double value=1.4;
        color.r((byte)(92*value));
        color.g((byte)(78*value));
        color.b((byte)(22*value));
        DrawText(String.valueOf(score),130,545,35,color);
        DrawText(String.valueOf(level),290,710,35,color);
        DrawText(String.valueOf(jewels),260,840,35,color);
    }
    //draw bg
    private void drawBackgroud(){
        DrawTexture(background,0,0,WHITE);
    }
    //draw bord
    private void drawBord(){
        int bordStartX=417;
        int bordStartY=35;
        flikerTimeBuffer+=GetFrameTime();
        if(flikerTimeBuffer>this.flikerTime*2){
            flikerTimeBuffer=0;
        }
        for(int i=0;i<6;i++){
            for(int j=0;j<14;j++){
                if(bord[i][j]!=-1){
                    if(!waiting){
                        DrawTexture(piceTextures[bord[i][j]],bordStartX+(i*62),bordStartY+(int)((j)*59.5),WHITE);
                    }else{
                        if(bordToRemove[i][j]){
                            if (flikerTimeBuffer<this.flikerTime){
                                DrawTexture(piceTextures[bord[i][j]],bordStartX+(i*62),bordStartY+(int)((j)*59.5),WHITE);
                            }
                        }else{
                            DrawTexture(piceTextures[bord[i][j]],bordStartX+(i*62),bordStartY+(int)((j)*59.5),WHITE);
                        }
                    }
                }
            }
        }
    }
    //game logic
    public void update(){
        if(!StartingUp){
            if(!gameover){
                //not waiting
                if(currentPice.needPut(bord) && !waiting){
                    for(int i=0;i<3;i++){
                        int content=currentPice.content[i];
                        try{
                            bord[currentPice.pos.x][currentPice.pos.y+i]=content;
                        }catch (Exception e){
                            timeBuffer=0;
                            gameover=true;
                        }
                    }
                    currentPice=new CurrentPice(nextPice,piceTextures,bord);
                    nextPice=generateNextPice();
                    waiting=generateToBeRemoved();
                }
                if(!waiting){
                    currentPice.moveFromInput(bord);
                    currentPice.update(bord);
                }
                if(IsKeyPressed(KEY_TAB)){
                    nextPice=generateNextPice();
                }
                //waiting
                if(waiting){
                    timeBuffer+=GetFrameTime();
                    if(timeBuffer>waitTime){
                        timeBuffer=0;
                        this.jewels+=countToBeRemoved();
                        processLevelPass();
                        processToBeRemove();
                        processGravity();
                        score +=ScoreBuffer;
                        ScoreBuffer=0;
                        waiting=generateToBeRemoved();
                    }
                }
            }else{
                timeBuffer+=GetFrameTime();
                if(IsKeyPressed(KEY_SPACE) && timeBuffer>1){
                    reset();
                }
            }
        }else{
            timeBuffer+=GetFrameTime();
            if(timeBuffer>2.5){
                timeBuffer=0;
                StartingUp=false;
            }
        }
    }
    private void processToBeRemove(){
        for(int i=0;i<6;i++){
            for(int j=0;j<14;j++){
                if(bordToRemove[i][j]){
                    bord[i][j]=-1;
                }
            }
        }
    }
    private int countToBeRemoved(){
        int count=0;
        for(int x=0;x<6;x++){
            for(int y=0;y<14;y++){
                if(bordToRemove[x][y]){
                    count+=1;
                }
            }
        }
        return count;
    }
    private boolean generateToBeRemoved(){
        boolean return_value=false;
        //reset removes
        for(int i=0;i<6;i++){
            for(int j=0;j<14;j++){
                bordToRemove[i][j]=false;
            }
        }
        //vetical
        for(int x=0;x<6;x++){
            for(int y=0;y<14-2;y++){
                 int buffer=bord[x][y];
                 if(buffer!=-1){
                     boolean b1=bord[x][y]==buffer;
                     boolean b2=bord[x][y+1]==buffer;
                     boolean b3=bord[x][y+2]==buffer;
                     if(b1 && b2 && b3){
                         bordToRemove[x][y]=true;
                         bordToRemove[x][y+1]=true;
                         bordToRemove[x][y+2]=true;
                         level_buffer[x]=true;
                         return_value=true;
                         ScoreBuffer+=3000;
                     }
                 }
            }
        }
        //horizontal
        for(int x=0;x<6-2;x++){
            for(int y=0;y<14;y++){
                int buffer=bord[x][y];
                if(buffer!=-1){
                    boolean b1=bord[x][y]==buffer;
                    boolean b2=bord[x+1][y]==buffer;
                    boolean b3=bord[x+2][y]==buffer;
                    if(b1 && b2 && b3){
                        bordToRemove[x][y]=true;
                        bordToRemove[x+1][y]=true;
                        bordToRemove[x+2][y]=true;
                        level_buffer[x]=true;
                        level_buffer[x+2]=true;
                        level_buffer[x+1]=true;
                        return_value=true;
                        ScoreBuffer+=3000;
                    }
                }
            }
        }
        //d1
        for(int x=0;x<6-2;x++){
            for(int y=0;y<14-2;y++){
                int buffer=bord[x][y];
                if(buffer!=-1){
                    boolean b1=bord[x][y]==buffer;
                    boolean b2=bord[x+1][y+1]==buffer;
                    boolean b3=bord[x+2][y+2]==buffer;
                    level_buffer[x]=true;
                    if(b1 && b2 && b3){
                        bordToRemove[x][y]=true;
                        bordToRemove[x+1][y+1]=true;
                        bordToRemove[x+2][y+2]=true;
                        level_buffer[x]=true;
                        level_buffer[x+2]=true;
                        level_buffer[x+1]=true;
                        return_value=true;
                        ScoreBuffer+=3000;
                    }
                }
            }
        }
        //d2
        for(int x=0;x<6-2;x++){
            for(int y=2;y<14;y++){
                int buffer=bord[x][y];
                if(buffer!=-1){
                    boolean b1=bord[x][y]==buffer;
                    boolean b2=bord[x+1][y-1]==buffer;
                    boolean b3=bord[x+2][y-2]==buffer;
                    if(b1 && b2 && b3){
                        bordToRemove[x][y]=true;
                        bordToRemove[x+1][y-1]=true;
                        bordToRemove[x+2][y-2]=true;
                        level_buffer[x]=true;
                        level_buffer[x+2]=true;
                        level_buffer[x+1]=true;
                        return_value=true;
                        ScoreBuffer+=3000;
                    }
                }
            }
        }
        return return_value;
    }
    private void processLevelPass(){
        int count=0;
        for(int i=0;i<6;i++){
            if(level_buffer[i]==true){
                count+=1;
            }
        }
        if(count==6){
            level+=1;
            for(int i=0;i<6;i++){
                level_buffer[i]=false;
            }
        }
    }
    private void processGravity(){
        for(int i=0;i<500;i++){
            for(int x=0;x<6;x++){
                for(int y=0;y<13;y++){
                    if(bord[x][y]!=-1 && bord[x][y+1]==-1){
                        switchBord(x,y,x,y+1);
                    }
                }
            }
        }
    }
    private void switchBord(int x1,int y1,int x2,int y2){
        int buffer=bord[x1][y1];
        bord[x1][y1]=bord[x2][y2];
        bord[x2][y2]=buffer;
    }
    private int[] generateNextPice(){
        int[] value=new int[3];
        for(int i=0;i<3;i++){
            value[i]=(int)(Math.random()*5);
        }
        return value;
    }
}
