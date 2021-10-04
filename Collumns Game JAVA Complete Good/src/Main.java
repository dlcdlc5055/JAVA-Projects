package com.company;
import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Main {

    public static void main(String[] args) {
        InitWindow(1201, 901, "Collumns Game Java Edition");
        Game game=new Game();
        SetTargetFPS(60);

        while(!WindowShouldClose()){
            BeginDrawing();
            ClearBackground(RAYWHITE);
            game.update();
            game.draw();
            EndDrawing();
        }
        Raylib.CloseWindow();
    }
}
