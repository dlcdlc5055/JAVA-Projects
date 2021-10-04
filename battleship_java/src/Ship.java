package com.company;
import static com.raylib.Jaylib.*;

public class Ship {
    private Texture2D sprite_vertical;
    private Texture2D sprite_horizontal;
    Vect pos;
    private Vect init_pos;
    private Vect last_pos;
    int size;
    boolean rotation;
    boolean[] health;
    boolean is_set;
    boolean first_rotation;
    String ship_name;
    Ship(Texture2D sprite_vertical,Texture2D sprite_horizontal){
        this.ship_name=ship_name;
        this.is_set=false;
        this.rotation=true;
        this.sprite_vertical=sprite_vertical;
        pos=new Vect(0,0);
        health=new boolean[this.size];
        this.sprite_horizontal=sprite_horizontal;
    }
    public void setInitPos(){
        init_pos=pos;
    }
    public void rePos(){
        pos=init_pos;
    }
    public boolean checkIfClicked(){
        Vect mousePos=new Vect((int)GetMousePosition().x(),(int)GetMousePosition().y());
        int lenght=(int)(size*56.6);
        if(!rotation){
            if(pos.x<mousePos.x && mousePos.x<pos.x+56 && pos.y< mousePos.y &&  mousePos.y<pos.y+lenght ){
                return true;
            }
        }else{
            if(pos.y<mousePos.y && mousePos.y<pos.y+56 && pos.x< mousePos.x &&  mousePos.x<pos.x+lenght ){
                return true;
            }
        }
        return false;
    }

    public boolean[][] clearShipPlayer(boolean[][] bord){
        boolean[][] buffer=bord;
        for(int i=0;i<this.size;i++){
            System.out.println((int)((pos.x-48)/56));
            System.out.println(((int)((pos.y-320)/56)));
                if(!rotation){
                    buffer[(int)((pos.x-48)/56)][((int)((pos.y-320)/56))+i]=false;
                }else{
                    buffer[(int)((pos.x-48)/56)+i][((int)((pos.y-320)/56))]=false;
                }
        }
        return buffer;
    }
    public void alignToBordEnemy(){
        Vect mousePos=new Vect(pos.x,pos.y);
        Vect EnemyHoverBordPoint=new Vect((int)((mousePos.x-678)/56),(int)((mousePos.y-320)/56));
        if(!rotation){
            if(EnemyHoverBordPoint.x>=10){
                EnemyHoverBordPoint.x=9;
            }
            if(EnemyHoverBordPoint.y>=10-size){
                EnemyHoverBordPoint.y=9-size;
            }
        }else{
            if(EnemyHoverBordPoint.y>=10){
                EnemyHoverBordPoint.y=9;
            }
            if(EnemyHoverBordPoint.x>=10-size){
                EnemyHoverBordPoint.x=9-size;
            }
        }
        pos.x=678+(int)(EnemyHoverBordPoint.x*56);
        pos.y=320+(int)(EnemyHoverBordPoint.y*56);
    }
    public void alignToBordPlayer(){
        Vect mousePos=new Vect(pos.x,pos.y);
        Vect PlayerHoverBordPoint=new Vect((int)((mousePos.x-48)/56.6),(int)((mousePos.y-320)/56.6));
        pos.x=48+(int)(PlayerHoverBordPoint.x*56.6);
        pos.y=320+(int)(PlayerHoverBordPoint.y*56.6);
    }
    public boolean checkIfPossiblePutEnemy(boolean[][] bord,boolean[][] bordPossibles){
        int posible_count=0;
        Vect mousePos=new Vect((int)pos.x,(int)pos.y);
        Vect EnemtyHoverBordPoint=new Vect((int)((mousePos.x-678)/56.6),(int)((mousePos.y-320)/56.6));
        if(mousePos.x>678 && mousePos.y>320 && mousePos.x<678+(int)(56.6*10) && mousePos.y<320+(int)(56.6*10)){
            for(int i=0;i<size;i++){
                if(!rotation){
                    try{
                        boolean b1 = bord[EnemtyHoverBordPoint.x][EnemtyHoverBordPoint.y + i];
                        boolean b2 = bordPossibles[EnemtyHoverBordPoint.x][EnemtyHoverBordPoint.y + i];
                        if (!b1 && b2) {
                            posible_count+=1;
                        }
                    }catch (Exception e){
                        return false;
                    }
                }else{
                    try{
                        boolean b1=bord[EnemtyHoverBordPoint.x+i][EnemtyHoverBordPoint.y];
                        boolean b2=bordPossibles[EnemtyHoverBordPoint.x+i][EnemtyHoverBordPoint.y];
                        if(!b1 && b2){
                            posible_count+=1;
                        }
                    }catch (Exception e){
                        return false;
                    }

                }
            }
        }
        if(posible_count==size){
            return true;
        }
        return false;
    }
    public boolean checkIfPossiblePutPlayer(boolean[][] bord,boolean[][] bordPossibles){
        int posible_count=0;
        Vect mousePos=new Vect((int)GetMousePosition().x(),(int)GetMousePosition().y());
        Vect PlayerHoverBordPoint=new Vect((int)((mousePos.x-48)/56.6),(int)((mousePos.y-320)/56.6));

        if(mousePos.x>48 && mousePos.y>320 && mousePos.x<48+(int)(56.6*10) && mousePos.y<320+(int)(56.6*10)){
            for(int i=0;i<size;i++){
                if(!rotation){
                    try{
                        boolean b1 = bord[PlayerHoverBordPoint.x][PlayerHoverBordPoint.y + i];
                        boolean b2 = bordPossibles[PlayerHoverBordPoint.x][PlayerHoverBordPoint.y + i];
                        if (!b1 && b2) {
                            posible_count+=1;
                        }
                    }catch (Exception e){
                        return false;
                    }
                }else{
                    try{
                        boolean b1=bord[PlayerHoverBordPoint.x+i][PlayerHoverBordPoint.y];
                        boolean b2=bordPossibles[PlayerHoverBordPoint.x+i][PlayerHoverBordPoint.y];
                        if(!b1 && b2){
                            posible_count+=1;
                        }
                    }catch (Exception e){
                        return false;
                    }

                }
            }
        }
        mousePos=new Vect(pos.x,pos.y);
        PlayerHoverBordPoint=new Vect((int)((mousePos.x-48)/56.6),(int)((mousePos.y-320)/56.6));
        if(posible_count==size){
            last_pos=PlayerHoverBordPoint;
            return true;
        }
        return false;
    }
    public void draw(){
        if(!rotation){
            DrawTexture(sprite_vertical,pos.x,pos.y,WHITE);
        }else{
            DrawTexture(sprite_horizontal,pos.x,pos.y,WHITE);
        }
    }
}