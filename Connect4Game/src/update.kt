import com.raylib.Raylib

fun update(gd:GameData):GameData{
    var buffer=gd
    buffer.PlayerPosX=getPlayerPos()
    if(Raylib.IsMouseButtonPressed(Raylib.MOUSE_LEFT_BUTTON) && gd.state==0){
        var mousePos=Raylib.GetMousePosition()
        var xMouse=mousePos.x().toInt()
        var x=xMouse/128
        if (x>-1 && x<7){
            buffer=putPice(x,buffer)
        }
        if(checkForWin(gd,gd.turn)){
            buffer.state=2
        }
        if(checkIfBordFull(buffer )){
            buffer.state=1
        }
    }
    if(Raylib.IsKeyReleased(Raylib.KEY_SPACE)){
        buffer=reset(buffer)
    }
    return buffer
}