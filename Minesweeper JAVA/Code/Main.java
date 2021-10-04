package com.company;
import com.raylib.Raylib;
import static com.raylib.Jaylib.*;


public class Main {

    public static void main(String[] args) {
        InitWindow(1600, 900, "Minesweeper JAVA");
        SetTargetFPS(60);
        Game game = new Game();

        while(!WindowShouldClose()){
            BeginDrawing();
            game.update();
            ClearBackground(RAYWHITE);
            game.draw();
            EndDrawing();
        }
        Raylib.CloseWindow();
    }
}
