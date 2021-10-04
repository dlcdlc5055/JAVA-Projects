package com.company;
import static com.raylib.Jaylib.*;

import static com.raylib.Jaylib.*;

public class Main {

    public static void main(String[] args) {
        InitWindow(1274, 900, "BATTLESHIP JAVA EDITION");
        SetTargetFPS(60);
        Game game=new Game();

        while(!WindowShouldClose()){
            BeginDrawing();
            ClearBackground(RAYWHITE);
            game.update();
            game.draw();
            if(IsKeyPressed(KEY_SPACE) && game.gameover){
                game=new Game();
            }
            EndDrawing();
        }
        CloseWindow();
    }
}
