package com.company;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import static com.raylib.Jaylib.*;

public class Game {
    private boolean[][] PlayerBord=new boolean[10][10];
    private boolean[][] EnemyBord=new boolean[10][10];
    private boolean[][] PlayerBordFired=new boolean[10][10];
    private boolean[][] EnemyBordFired=new boolean[10][10];
    private boolean GameStage=false;
    private boolean waiting=false;
    private Texture2D mask;
    private Texture2D[] ShipSpritesVertical=new Texture2D[5];
    private Texture2D[] ShipSpritesHortizontal=new Texture2D[5];
    private Texture2D background;
    private Texture2D explosion;
    private Ship[] PlayerShip=new Ship[5];
    private Ship[] EnemyShip=new Ship[5];
    private boolean[][] PossiblePosPlayer=new boolean[10][10];
    private boolean[][] PossiblePosEnemy=new boolean[10][10];
    private int selected=-1;
    private Vect selectedPosBuffer=new Vect(0,0);
    private int waitID=-1;
    boolean gameover=false;
    private boolean turn=false;
    private float timeBuffer=0;
    private float waitTime=1.05f;
    private float filikerTImeBuffer=0.0f;
    private float filikerTIme=.15f;
    private Vect RecentHitPos=new Vect(-1,-1);
    private boolean RecentHitBord=false;
    private  Texture2D GameOverText;
    private String txt="PRESS ENTER TO START!!";

    Game(){
        String currentPath = System.getProperty("user.dir");
        this.selected=-1;
        this.waitID=-1;
        this.GameStage=false;
        for(int i=0;i<10;i++) {
            for (int j = 0; j < 10; j++) {
                this.PlayerBord[i][j] = false;
                this.EnemyBord[i][j] = false;
                PossiblePosPlayer[i][j]=false;
                PossiblePosEnemy[i][j]=false;
            }
        }
        this.GameOverText=LoadTexture(currentPath+"\\assets\\game_over_text.png");
        this.mask=LoadTexture(currentPath+"\\assets\\mask.png");
        this.background=LoadTexture(currentPath+"\\assets\\background.png");
        this.mask=LoadTexture(currentPath+"\\assets\\mask.png");
        this.explosion=LoadTexture(currentPath+"assets\\explosion.png");
        this.ShipSpritesVertical[0]=LoadTexture(currentPath+"\\assets\\carrier.png");
        this.ShipSpritesVertical[1]=LoadTexture(currentPath+"\\assets\\destroyer.png");
        this.ShipSpritesVertical[2]=LoadTexture(currentPath+"\\assets\\submarine.png");
        this.ShipSpritesVertical[3]=LoadTexture(currentPath+"\\assets\\battleship.png");
        this.ShipSpritesVertical[4]=LoadTexture(currentPath+"\\assets\\ptBoat.png");
        this.ShipSpritesHortizontal[0]=LoadTexture(currentPath+"\\assets\\carrier_rotated.png");
        this.ShipSpritesHortizontal[1]=LoadTexture(currentPath+"\\assets\\destroyer_rotated.png");
        this.ShipSpritesHortizontal[2]=LoadTexture(currentPath+"\\assets\\submarine_rotated.png");
        this.ShipSpritesHortizontal[3]=LoadTexture(currentPath+"\\assets\\battleship_rotated.png");
        this.ShipSpritesHortizontal[4]=LoadTexture(currentPath+"\\assets\\ptBoat_rotated.png");
        for(int i=0;i<5;i++){
            PlayerShip[i]=new Ship(this.ShipSpritesVertical[i],this.ShipSpritesHortizontal[i]);
            EnemyShip[i]=new Ship(this.ShipSpritesVertical[i],this.ShipSpritesHortizontal[i]);
        }
        //player
        //carrier
        PlayerShip[0].pos=new Vect(130,40);
        PlayerShip[0].size=5;
        PlayerShip[0].first_rotation=true;
        //destroyer
        PlayerShip[1].pos=new Vect(130+(56*3)+5,40+(56*2));
        PlayerShip[1].size=3;
        PlayerShip[1].first_rotation=true;
        //submarine
        PlayerShip[2].pos=new Vect(130,40+(56*2));
        PlayerShip[2].size=3;
        PlayerShip[2].first_rotation=true;
        //battleship
        PlayerShip[3].pos=new Vect(130,40+56);
        PlayerShip[3].size=4;
        PlayerShip[3].first_rotation=true;
        //ptBoat
        PlayerShip[4].pos=new Vect(417,40);
        PlayerShip[4].size=2;
        PlayerShip[4].rotation=false;
        PlayerShip[4].first_rotation=false;
        //init player
        for(int i=0;i<5;i++){
            PlayerShip[i].setInitPos();
        }
        //enemy
        //carrier
        EnemyShip[0].pos=new Vect(790,40);
        EnemyShip[0].size=5;
        EnemyShip[0].first_rotation=true;
        //destroyer
        EnemyShip[1].pos=new Vect(790+(56*3)+5,40+(56*2));
        EnemyShip[1].size=3;
        EnemyShip[1].first_rotation=true;
        //submarine
        EnemyShip[2].pos=new Vect(790,40+(56*2));
        EnemyShip[2].size=3;
        EnemyShip[2].first_rotation=true;
        //battleship
        EnemyShip[3].pos=new Vect(790,40+56);
        EnemyShip[3].size=4;
        EnemyShip[3].first_rotation=true;
        //ptBoat
        EnemyShip[4].pos=new Vect(790+(418-130),40);
        EnemyShip[4].size=2;
        EnemyShip[4].rotation=false;
        EnemyShip[4].first_rotation=false;
    }
    public Vect getPlayerClickedPoint(){
        Vect data=new Vect(-1,-1);
        Vect mouse_pos=new Vect((int)GetMousePosition().x(),(int)GetMousePosition().y());
        if(mouse_pos.x>678 && mouse_pos.y>320 && mouse_pos.x<678+566 && mouse_pos.y<320+566){
            data.x= (int)((mouse_pos.x-678)/56.6);
            data.y= (int)((mouse_pos.y-320)/56.6);
        }
        return data;
    }
    public void update(){
        generatePossible();
        this.gameover=generateGameOver();
        if(!waiting && !gameover){
            if(GameStage){
                if(!turn){
                    if(IsMouseButtonPressed(MOUSE_LEFT_BUTTON)){
                        Vect clickBuffer=getPlayerClickedPoint();
                        System.out.println(clickBuffer.x);
                        System.out.println(clickBuffer.y);
                        if(clickBuffer.x!=-1 && clickBuffer.y!=-1 && !EnemyBordFired[clickBuffer.x][clickBuffer.y]){
                            EnemyBordFired[clickBuffer.x][clickBuffer.y]=true;
                            timeBuffer=0;
                            waiting=true;
                            RecentHitBord=turn;
                            RecentHitPos=clickBuffer;
                            turn=true;
                        }
                    }
                }else{
                    processAI();
                }
            }else if(!gameover){
                if(selected==-1){
                    Vect mousePos=new Vect((int)GetMousePosition().x(),(int)GetMousePosition().y());
                    for(int i=0;i<5;i++){
                        if(PlayerShip[i].checkIfClicked() && IsMouseButtonPressed(MOUSE_LEFT_BUTTON) && !PlayerShip[i].is_set){
                            selected=i;
                            selectedPosBuffer=PlayerShip[i].pos;
                        }else if(PlayerShip[i].checkIfClicked() && IsMouseButtonPressed(MOUSE_LEFT_BUTTON) && PlayerShip[i].is_set){
                            selected=i;
                            selectedPosBuffer=PlayerShip[i].pos;
                            this.PlayerBord=PlayerShip[i].clearShipPlayer(this.PlayerBord);
                        }
                    }
                }else{
                    if(IsKeyPressed(KEY_TAB)){
                        PlayerShip[selected].rotation=!PlayerShip[selected].rotation;
                    }
                    Vect mousePos=new Vect((int)GetMousePosition().x(),(int)GetMousePosition().y());
                    PlayerShip[selected].pos=new Vect(mousePos.x-(int)(56.6/2),mousePos.y-(int)(56.6/2));
                    if(IsMouseButtonReleased(MOUSE_LEFT_BUTTON) && !PlayerShip[selected].checkIfPossiblePutPlayer(this.PlayerBord,this.PossiblePosPlayer)){
                        new Vect(PlayerShip[selected].pos.x,PlayerShip[selected].pos.y);
                        PlayerShip[selected].rePos();
                        PlayerShip[selected].rotation= PlayerShip[selected].first_rotation;
                        selected=-1;
                    }else if(IsMouseButtonReleased(MOUSE_LEFT_BUTTON) && PlayerShip[selected].checkIfPossiblePutPlayer(this.PlayerBord,this.PossiblePosPlayer)){
                        PlayerShip[selected].pos=new Vect(mousePos.x,mousePos.y);
                        Vect buffer=new Vect((int)((PlayerShip[selected].pos.x-48)/56.6),(int)((PlayerShip[selected].pos.y-320)/56.6));
                        boolean rotate=PlayerShip[selected].rotation;
                        boolean todo=true;
                        for(int i=0;i<PlayerShip[selected].size;i++){
                            try{
                                if(!rotate){
                                    boolean b1=this.PlayerBord[buffer.x][buffer.y+i];
                                }else{
                                    boolean b2=this.PlayerBord[buffer.x+i][buffer.y];
                                }
                            }catch (Exception e){
                                PlayerShip[selected].rePos();
                                PlayerShip[selected].rotation= PlayerShip[selected].first_rotation;
                                selected=-1;
                                return;
                            }
                        }
                        if(todo){
                            PlayerShip[selected].is_set=true;
                            PlayerShip[selected].alignToBordPlayer();
                            for(int i=0;i<PlayerShip[selected].size;i++){
                                if(!rotate){
                                    this.PlayerBord[buffer.x][buffer.y+i]=true;
                                }else{
                                    this.PlayerBord[buffer.x+i][buffer.y]=true;
                                }
                            }
                            selected=-1;
                        }
                    }
                }
                int count=0;
                for(int i=0;i<5;i++){
                    if(PlayerShip[i].is_set){
                        count+=1;
                    }
                }
                if(count==5 && IsKeyPressed(KEY_ENTER)){
                    generateEnemyBord();
                    this.txt="";
                    GameStage=true;
                }
            }
        }else{
            timeBuffer+=GetFrameTime();
            filikerTImeBuffer+=GetFrameTime();
            if(timeBuffer>waitTime){
                timeBuffer=0;
                filikerTImeBuffer=0;
                waiting=false;
                RecentHitPos=new Vect(-1,-1);
            }
        }
    }
    public void draw(){
        DrawBackground();
        DrawText(txt,420,4,28,WHITE);
        if(!GameStage && !gameover) {
            drawPossible();
        }
        drawGrid();
        drawPlayerShips();
        if(gameover || !GameStage){
            drawDebugEnemyBord();
        }
        DrawHits();
        if(gameover){
            Jaylib.Color clr=new Jaylib.Color();
            clr.r((byte)255);
            clr.g((byte)255);
            clr.b((byte)255);
            clr.a((byte)96);
            DrawTexture(mask,0,0,clr);
            if(turn==false){
                DrawTexture(GameOverText,(int)(1274/2-(489/2)),((int)(900/2-(272/2))),WHITE);
                DrawText("PRESS SPACEBAR TO RESET!!",10,10,66,WHITE);
            }else{
                DrawText("PRESS SPACEBAR TO RESET!!",10,10,66,WHITE);
                DrawText("YOU WIN!!",(int)(1274/2-(489/2))-25,((int)(900/2-(272/2)))+25,125,WHITE);
            }
        }
    }
    private void DrawHits(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                Jaylib.Color clr_p=new Jaylib.Color();
                Jaylib.Color clr_e=new Jaylib.Color();
                clr_p.a((byte)255);
                clr_e.a((byte)255);
                if(PlayerBord[i][j]){
                    clr_p.r((byte)255);
                    clr_p.g((byte)128);
                    clr_p.b((byte)128);
                }else{
                    clr_p.r((byte)255);
                    clr_p.g((byte)255);
                    clr_p.b((byte)255);
                }
                if(EnemyBord[i][j]){
                    clr_e.r((byte)255);
                    clr_e.g((byte)128);
                    clr_e.b((byte)128);
                }else{
                    clr_e.r((byte)255);
                    clr_e.g((byte)255);
                    clr_e.b((byte)255);
                }
                if(PlayerBordFired[i][j] && (RecentHitPos==new Vect(-1,-1)||!waiting)){
                    DrawTexture(explosion,(int)(i*56.6)+48+6,(int)(j*56.6)+320+4,clr_p);
                }else if(PlayerBordFired[i][j] && waiting){
                    if(i==RecentHitPos.x && j==RecentHitPos.y  && RecentHitBord==true) {
                        if (this.filikerTImeBuffer < this.filikerTIme) {
                            DrawTexture(explosion, (int) (i * 56.6) + 48+6, (int) (j * 56.6) + 320+4, clr_p);
                        } else if (this.filikerTImeBuffer > this.filikerTIme * 2) {
                            this.filikerTImeBuffer = 0;
                        }
                    }else{
                        DrawTexture(explosion,(int)(i*56.6)+48+6,(int)(j*56.6)+320+4,clr_p);
                    }
                }
                if(EnemyBordFired[i][j] && (RecentHitPos==new Vect(-1,-1) || !waiting)){
                    DrawTexture(explosion,(int)(i*56.6)+678+6,(int)(j*56.6)+320+4,clr_e);
                }else if(EnemyBordFired[i][j] && waiting){
                    if(i==RecentHitPos.x && j==RecentHitPos.y  && RecentHitBord==false) {
                        if (this.filikerTImeBuffer < this.filikerTIme) {
                            DrawTexture(explosion, (int) (i * 56.6) + 678+6, (int) (j * 56.6)+4 + 320, clr_e);
                        } else if (this.filikerTImeBuffer > this.filikerTIme * 2) {
                            this.filikerTImeBuffer = 0;
                        }
                    }else{
                        DrawTexture(explosion,(int)(i*56.6)+678+6,(int)(j*56.6)+4+320,clr_e);
                    }
                }
            }
        }
    }
    private void DrawBackground(){
        DrawTexture(background,0,0,WHITE);
        DrawText("PLAYER",135,222+4,45,RAYWHITE);
        DrawText("ENEMY",650,222+4,45,RAYWHITE);
        DrawText("YOUR FLEET",128,10,25,WHITE);
        DrawText("ENEMY FLEET",871,10,25,WHITE);

        Jaylib.Color white=new Jaylib.Color();
        white.a((byte)255);
        white.r((byte)128);
        white.g((byte)128);
        white.b((byte)128);


        DrawTexture(ShipSpritesHortizontal[0],130,40,white);
        DrawTexture(ShipSpritesVertical[4],417,40,white);
        DrawTexture(ShipSpritesHortizontal[3],130,40+56,white);
        DrawTexture(ShipSpritesHortizontal[2],130,40+(56*2),white);
        DrawTexture(ShipSpritesHortizontal[1],130+(56*3)+5,40+(56*2),white);

        DrawTexture(ShipSpritesHortizontal[0],790,40,white);
        DrawTexture(ShipSpritesVertical[4],(417-130)+790,40,white);
        DrawTexture(ShipSpritesHortizontal[3],790,40+56,white);
        DrawTexture(ShipSpritesHortizontal[2],790,40+(56*2),white);
        DrawTexture(ShipSpritesHortizontal[1],790+(56*3)+5,40+(56*2),white);
        DrawBoatHealth();
    }
    private void drawGrid(){
        for(int i=0;i<11;i++){
            DrawRectangle(48,320+(int)(i*56.6)-2,566,2,DARKGRAY);
            DrawRectangle(48+(int)(i*56.6)-2,320,2,566,DARKGRAY);
            DrawRectangle(682,320+(int)(i*56.6)-2,566,2,DARKGRAY);
            DrawRectangle(682+(int)(i*56.6)-2,320,2,566,DARKGRAY);
        }
    }
    private void drawPossible(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                int x=(int)(i*56.6);
                int y=320+(int)(j*56.6);
                Raylib.Color clr = new Raylib.Color();
                clr.a((byte)255);
                clr.r((byte)32);
                clr.g((byte)58);
                clr.b((byte)71);
                if(!PossiblePosPlayer[i][j]){
                    DrawRectangle(x+48,y,56,56,clr);
                }
                if(!PossiblePosEnemy[i][j]){
                    DrawRectangle(x+678,y,56,56,clr);
                }
            }
        }
    }

    private void drawPlayerShips(){
        for(int i=0;i<5;i++){
            PlayerShip[i].draw();
        }
        if(selected!=-1){
            PlayerShip[selected].draw();
        }
    }

    private void generatePossible(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                PossiblePosPlayer[i][j]=true;
                PossiblePosEnemy[i][j]=true;
            }
        }
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                boolean playerBuffer=PlayerBord[i][j];
                if(playerBuffer){
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i+1,j,false);
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i-1,j,false);
                    PossiblePosPlayer= setBordPoint(this.PossiblePosPlayer,i,j+1,false);
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i,j-1,false);
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i-1,j-1,false);
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i+1,j+1,false);
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i+1,j-1,false);
                    PossiblePosPlayer=setBordPoint(this.PossiblePosPlayer,i-1,j+1,false);
                }
            }
        }
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                boolean enemyBuffer=EnemyBord[i][j];
                if(enemyBuffer){
                    PossiblePosEnemy=setBordPoint(this.PossiblePosEnemy,i+1,j,false);
                    PossiblePosEnemy=setBordPoint(this.PossiblePosEnemy,i-1,j,false);
                    PossiblePosEnemy=setBordPoint(this.PossiblePosEnemy,i,j+1,false);
                    PossiblePosEnemy= setBordPoint(this.PossiblePosEnemy,i,j-1,false);
                    PossiblePosEnemy=setBordPoint(this.PossiblePosEnemy,i-1,j-1,false);
                    PossiblePosEnemy=setBordPoint(this.PossiblePosEnemy,i+1,j+1,false);
                    PossiblePosEnemy= setBordPoint(this.PossiblePosEnemy,i+1,j-1,false);
                    PossiblePosEnemy= setBordPoint(this.PossiblePosEnemy,i-1,j+1,false);
                }
            }
        }
    }
    private void drawDebugEnemyBord(){
        for(int i=0;i<5;i++){
            EnemyShip[i].draw();
        }
    }
    private void generateEnemyBord(){
        //todo
        while (true){
            generatePossible();
            int tryID=(int) (Math.random()*5);
            int tryX=(int) (Math.random()*566)+678;
            int tryY=(int) (Math.random()*566)+320;
            int tryR=(int) (Math.random()*2);
                if(!EnemyShip[tryID].is_set){
                    EnemyShip[tryID].pos=new Vect(tryX,tryY);
                    if(tryR==0){
                        EnemyShip[tryID].rotation=false;
                    }else{
                        EnemyShip[tryID].rotation=true;
                    }
                    if(EnemyShip[tryID].checkIfPossiblePutEnemy(EnemyBord,PossiblePosEnemy)){
                        Vect buffer=new Vect((int)((tryX-678)/56.6),(int)((tryY-320)/56.6));
                        boolean todo=true;
                        for(int i=0;i<EnemyShip[tryID].size;i++){
                            try{
                                if(!EnemyShip[tryID].rotation){
                                    boolean b1=this.EnemyBord[buffer.x][buffer.y+i];
                                }else{
                                    boolean b2=this.EnemyBord[buffer.x+i][buffer.y];
                                }
                            }catch (Exception e){

                            }
                        }
                        if(todo){
                            EnemyShip[tryID].is_set=true;
                            EnemyShip[tryID].pos.x=678+(int)(56.6*buffer.x);
                            EnemyShip[tryID].pos.y=320+(int)(56.6*buffer.y);
                            for(int i=0;i<EnemyShip[tryID].size;i++){
                                if(!EnemyShip[tryID].rotation){
                                    this.EnemyBord[buffer.x][buffer.y+i]=true;
                                }else{
                                    this.EnemyBord[buffer.x+i][buffer.y]=true;
                                }
                            }
                        }
                    }
                }


            int count=0;
            for(int i=0;i<5;i++){
                if(EnemyShip[i].is_set){
                    count+=1;
                }
            }
            if(count==5){
                break;
            }
        }
        System.out.println("enemy bord generated");
    }
    private void DrawBoatHealth(){

    }
    private boolean[][] setBordPoint(boolean[][] bord,int x,int y,boolean value){
        boolean[][] buffer=bord;
        if(x>-1 && y>-1 && x<10 && y<10){
            buffer[x][y]=value;
        }
        return buffer;
    }
    private void processAI(){
        int i=0;
        while (true){
            i+=1;
            if(i>100000){
                break;
            }
            int x=(int)(Math.random()*10);
            int y=(int)(Math.random()*10);
            int buffer=(int)(Math.random()*1000);
            boolean did=false;
            if(buffer<840){
                if(x>-1&&y>-1&&x<10&&y<10&&PlayerBordFired[x][y]==false){
                    PlayerBordFired[x][y]=true;
                    timeBuffer=0;
                    waiting=true;
                    RecentHitBord=turn;
                    RecentHitPos=new Vect(x,y);
                    turn=false;
                    break;
                }
            }else{
                //todo
                while (true){
                    int new_x=(int)(Math.random()*10);
                    int new_y=(int)(Math.random()*10);
                    if(!PlayerBordFired[new_x][new_y] && PlayerBord[new_x][new_y]){
                        PlayerBordFired[new_x][new_y]=true;
                        timeBuffer=0;
                        waiting=true;
                        RecentHitBord=turn;
                        RecentHitPos=new Vect(new_x,new_y);
                        turn=false;
                        break;
                    }
                }
                break;
            }
        }
    }
    private int diference(int v1,int v2){
        if(v1<v2){
            return v2-v1;
        }else if(v2<v1){
            return v1-v2;
        }else{
            return 0;
        }
    }
    private boolean generateGameOver(){
        int val=3+3+5+4+2;
        int count_p=0;
        int count_e=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(PlayerBord[i][j] && PlayerBordFired[i][j]){
                    count_p+=1;
                }
                if(EnemyBord[i][j] && EnemyBordFired[i][j]){
                    count_e+=1;
                }
            }
        }
        if(count_e==val || count_p==val){
            return true;
        }
        return false;
    }
}