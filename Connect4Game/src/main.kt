import com.raylib.Jaylib
import com.raylib.Raylib

    fun main(args: Array<String>) {
        Raylib.InitWindow(900, 900, "Connect 4")
        Raylib.SetTargetFPS(60)
        var gameData=GameData()
        while (!Raylib.WindowShouldClose()) {
            Raylib.BeginDrawing()
            update(gameData)
            render(gameData)
            Raylib.EndDrawing()
        }
        Raylib.CloseWindow()
    }
