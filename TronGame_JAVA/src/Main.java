package com.company;
import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Main {

    public static void main(String[] args) {
        InitWindow(1600, 900, "TRON GAME JAVA Edition");
        SetTargetFPS(30);
        Game game = new Game();

        while(!WindowShouldClose()){
            BeginDrawing();
            ClearBackground(BLACK);
            game.play();
            if(game.gameover && (IsKeyPressed(KEY_ENTER)||IsKeyPressed(KEY_SPACE))){
                game=new Game();
            }
            EndDrawing();
        }
        Raylib.CloseWindow();
    }
}
