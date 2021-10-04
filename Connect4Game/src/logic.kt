import com.raylib.Raylib

class GameData{
    var turn = false
    var PlayerPosX = 900/2
    var data = InitBord(this)
    var state=0
    val redPice=1
    val yellowPice=2
    val blocksize=128
}

fun getPlayerPos(): Int {
    var mousePos= Raylib.GetMousePosition()
    var x= mousePos.x().toInt()
    if(x<128/2){
        x=128/2
    }else if (x>900-128/2){
        x=900-128/2
    }
    return x
}

fun InitBord(gd: GameData):Array<IntArray>{
    var data = Array(7) { IntArray(6) }
    for(i in 0..6){
        for(j in 0..5){
            data[i][j]=0
        }
    }
    return data
}

fun putPice(index:Int,gd: GameData): GameData {
    var b1=false
    var buffer=gd
    for (i in 0..5){
        if(buffer.data[index][0]!=0){
            break
        }
        if (i>0 && i!=5){
            if(buffer.data[index][i]!=0){
                if (buffer.turn==false){
                    buffer.data[index][i-1]=1
                    b1=true
                    break
                }
                else if (buffer.turn==true){
                    buffer.data[index][i-1]=2
                    b1=true
                    break
                }
            }
        }else if (i==5){
            if(buffer.data[index][i]==0){
                if (buffer.turn==false){
                    buffer.data[index][i]=1
                    b1=true
                    break
                }
                else if (buffer.turn==true){
                    buffer.data[index][i]=2
                    b1=true
                    break
                }
            }else{
                if (buffer.turn==false){
                    buffer.data[index][i-1]=1
                    b1=true
                    break
                }
                else if (buffer.turn==true){
                    buffer.data[index][i-1]=2
                    b1=true
                    break
                }
            }
        }
    }
    if(b1){
        buffer.turn=!buffer.turn
    }
    return buffer
}

fun checkForWin(gd: GameData,side:Boolean):Boolean{
    var bord=gd.data
    var turn=gd.turn
    var value:Int
    if(side==false){
        value=gd.yellowPice
    }else{
        value=gd.redPice
    }
    //horizontal
    for (j in 0..5){
        var count=0
        for (i in 0..6){
            if(bord[i][j]==value){
                count+=1
            }else{
                count=0
            }
            if(count==4){
                return true
            }
        }
    }
    //vertical
    for (i in 0..6){
        var count=0
        for (j in 0..5){
            if(bord[i][j]==value){
                count+=1
            }else{
                count=0
            }
            if(count==4){
                return true
            }
        }
    }
    //diag 1
    for (i in 0..(6-3)){
        for (j in 0..(5-3)){
            var count=0
            if(bord[i][j]==value){
                count++
            }
            if(bord[i+1][j+1]==value){
                count++
            }
            if(bord[i+2][j+2]==value){
                count++
            }
            if(bord[i+3][j+3]==value){
                count++
            }
            if(count==4) {
                return true
            }
        }
    }
    //diag 2
    for (i in 0..(6-3)){
        for (j in 3..5){
            var count=0
            if(bord[i][j]==value){
                count++
            }
            if(bord[i+1][j-1]==value){
                count++
            }
            if(bord[i+2][j-2]==value){
                count++
            }
            if(bord[i+3][j-3]==value){
                count++
            }
            if(count==4) {
                return true
            }
        }
    }
    return false
}

fun reset(gd: GameData):GameData{
    var buffer=gd
    buffer.data=InitBord(gd)
    buffer.state=0
    return buffer
}

fun checkIfBordFull(gd: GameData):Boolean{
    var count=0
    for(i in 0..6){
        for(j in 0..5){
            if(gd.data[i][j]!=0){
                count+=1
            }
        }
    }
    if(count==6*7){
        return true
    }
    return false
}