package com.run.tictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //それぞれのプレイや―がボードに置く画像
    public static final int[] PLAYER_IMAGES = {R.drawable.ic_close_black_24dp, R.drawable.ic_panorama_fish_eye_black_24dp};

    //ターン数を数える変数で、どっちのプレイやーかも管理する
    public int turn;

    //ゲームの盤面　誰も選択していないときは　－１
    public int[] gameBoard;

    //実際に見えているゲームの盤面
    public ImageButton[] boardButtons;

    //プレイヤーとターンの表示用のtextView
    public TextView playerTextView;

    //勝敗用のteXtView
    public TextView winnerTextView;

    //ボタンにつけたIDの管理
    public int[] buttonIds = {
            R.id.imageButton, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4,
            R.id.imageButton5, R.id.imageButton6, R.id.imageButton7, R.id.imageButton8, R.id.imageButton9,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTextView = findViewById(R.id.player_text);
        winnerTextView= findViewById(R.id.winner_text);

        boardButtons = new ImageButton[9];

        for (int i = 0; i < boardButtons.length; i++){
            boardButtons[i] = (ImageButton) findViewById(buttonIds[i]);
        }

        init();
        setPlayer();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_menu_reset){
            init();
            setPlayer();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public void init(){

        //編集の初期化
        turn = 1;
        //現在のゲームボードの初期化
        gameBoard = new int[boardButtons.length];
        for(int i = 0; i < boardButtons.length; i++){
            //誰もそのマスをとっていないとき、－１を入れる
            gameBoard[i] = -1;
            //ImageButtonで表示している画像を消す
            boardButtons[i].setImageBitmap(null);
        }

        //勝敗の表示用のTextViewは見えないようにする
        winnerTextView.setVisibility(View.GONE);
    }

    public void setPlayer(){
        //ターン数が２で割り切れたら、player2、割り切れなかったら、player1
        if(turn % 2 ==0){
            playerTextView.setText("Player: ×（２）");
        }else{
            playerTextView.setText("Player: 〇（１）");
        }
    }

    public void tapImageButton(View view){
        //勝敗が画面に出ていないときだけ処理を行うようにする
        if(winnerTextView.getVisibility() == View.VISIBLE) return;

        //どのボタンが押されたのかを取得する
        int tappedButtonPosition = 0;
        int viewId = view.getId();

        if(viewId == R.id.imageButton){
            tappedButtonPosition = 0;
        }else if (viewId == R.id.imageButton2){
            tappedButtonPosition = 1;
        }else if (viewId == R.id.imageButton3){
            tappedButtonPosition = 2;
        }else if (viewId == R.id.imageButton4){
            tappedButtonPosition = 3;
        }else if (viewId == R.id.imageButton5){
            tappedButtonPosition = 4;
        }else if (viewId == R.id.imageButton6){
            tappedButtonPosition = 5;
        }else if (viewId == R.id.imageButton7){
            tappedButtonPosition = 6;
        }else if (viewId == R.id.imageButton8){
            tappedButtonPosition = 7;
        }else if (viewId == R.id.imageButton9){
            tappedButtonPosition = 8;
        }

        //誰もそのマスをとっていないことを確認する
        if(gameBoard[tappedButtonPosition] == -1){

            //そのターンのプレイヤーの、画像を押されたマスにセットする
            boardButtons[tappedButtonPosition].setImageResource(PLAYER_IMAGES[turn % 2]);
            gameBoard[tappedButtonPosition] = turn % 2;

            //勝敗がついたかを判定する
            int judge = judgeGame();

            //judge の値がー１だったら、勝敗がついていない
            //judgeの値が１だったら、oのプレイヤーの勝利
            //judgeの値が０だったら、×のプレイやーの勝利
            if ( judge != -1){//勝敗が決まった時
                if(judge == 0){
                    winnerTextView.setText("Game End\nPlayer: ×(2)\nWin");
                    winnerTextView.setTextColor(Color.BLUE);
                }else{// judge == 1 のとき
                    winnerTextView.setText("Game End\nPlayer: 〇(1)\nWin");
                    winnerTextView.setTextColor(Color.RED);

                }
                winnerTextView.setVisibility(View.VISIBLE);

            }else{//全ターン終了して、引き分けた場合
                if(turn >= gameBoard.length){
                    winnerTextView.setText("Game End\nDraw");
                    winnerTextView.setTextColor(Color.YELLOW);
                    winnerTextView.setVisibility(View.VISIBLE);
                }
            }

            turn++; //turn = turn + 1;

            setPlayer();
        }
    }

    public int judgeGame(){
        for(int i= 0; i< 3; i++){
            //横並びの✔
            if(isMarkedHorizontal(i)){
                return gameBoard[i * 3];
            }
            //縦の並びの✔
            if(isMarkeVertical(i)){
                return gameBoard[i];
            }
        }

        //斜めの✔
        if(isMarkedDiagonal()){
            return gameBoard[4];
        }

        return -1;
    }

    //そのマスが誰かに取られている１または２
    //かつ、同じ人がとっている　横並び &&はかつの意味
    public boolean isMarkedHorizontal(int i){
        if(gameBoard[i * 3] != -1 && gameBoard[i * 3] == gameBoard[i * 3 + 1] && gameBoard[i * 3] == gameBoard[i * 3 + 2]){
            return true;
        }else {
            return false;
        }
    }

    public boolean isMarkeVertical(int i){//縦ならび
        if(gameBoard[i] != -1 && gameBoard[i] == gameBoard[i + 3] && gameBoard[i] == gameBoard[i + 6]){
            return true;
        }else {
           return false;
        }
    }

    public boolean isMarkedDiagonal(){
        if(gameBoard[0] != -1 && gameBoard[0] == gameBoard[4] && gameBoard[0] == gameBoard[8]){
            return true;
        }else{
            return false;
        }
    }


}
