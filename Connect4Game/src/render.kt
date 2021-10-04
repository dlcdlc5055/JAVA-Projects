import com.raylib.Jaylib
import com.raylib.Raylib

fun render(gd:GameData){
    renderBg()
    renderPlayer(gd)
    renderBord(gd)
    if (gd.state==1){
        renderDraw()
    }else if(gd.state==2){
        renderWin(gd.turn)
    }
}

fun renderPlayer(gd: GameData){
    var color=Jaylib.RED
    if(gd.turn==false){
        color=Jaylib.YELLOW
    }
    var y=128/2
    var x = gd.PlayerPosX
    Raylib.DrawCircle(x,y,50.toFloat(),color)
}

fun renderBord(gd:GameData){
    var data=gd.data
    for(i in 0..6){
        for(j in 0..5){
            if (data[i][j]==0){
                Raylib.DrawCircle(i*128+65,128+65+j*128,50.toFloat(),Jaylib.BLACK)
            }else if(data[i][j]==1){
                Raylib.DrawCircle(i*128+65,128+65+j*128,50.toFloat(),Jaylib.YELLOW)
            }else if(data[i][j]==2){
                Raylib.DrawCircle(i*128+65,128+65+j*128,50.toFloat(),Jaylib.RED)
            }
        }
    }
}

fun renderBg(){
    Raylib.ClearBackground(Jaylib.RAYWHITE)
    Raylib.DrawRectangle(0,0,900,128,Jaylib.BLACK)
    Raylib.DrawRectangle(0,128,900,900,Jaylib.BLUE)
}

fun renderDraw(){
    Raylib.DrawRectangle(0,0,900,900,Jaylib.BLACK)
    Raylib.DrawText("Draw!! ,Press Space to reset",150,900/2-40,40,Jaylib.RAYWHITE)
}

fun renderWin(side:Boolean){
    Raylib.DrawRectangle(0,0,900,900,Jaylib.BLACK)
    var value=0
    if(side==true){
        value=1
    }else{
        value=2
    }
    Raylib.DrawText("Player "+value.toString()+" Win!! ,Press Space to reset",77,900/2-40,40,Jaylib.RAYWHITE)
}