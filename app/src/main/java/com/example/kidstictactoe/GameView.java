package com.example.kidstictactoe;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

public class GameView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();
    private final String[] board = new String[9];
    private int current = 0; // 0 = Player 1, 1 = Player 2
    private boolean gameOver = false;
    private int winner = -1;
    private float density;
    private int theme = 0; // 0 = Sun/Moon, 1 = Cross/Zero
    private SoundManager soundManager;

    private final int BG = Color.rgb(255, 250, 235);
    private final int CARD = Color.rgb(255, 239, 190);
    private final int ORANGE = Color.rgb(255, 123, 84);
    private final int BLUE = Color.rgb(79, 164, 214);
    private final int GREEN = Color.rgb(107, 190, 108);
    private final int PURPLE = Color.rgb(145, 111, 196);
    private final int DARK = Color.rgb(86, 67, 60);
    private final int RED = Color.rgb(220, 53, 69);

    public GameView(Context c, int selectedTheme) {
        super(c);
        density = getResources().getDisplayMetrics().density;
        p.setStrokeCap(Paint.Cap.ROUND);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.theme = selectedTheme;
        this.soundManager = new SoundManager(c);
    }

    private float dp(float v) { return v * density; }
    private void txt(Canvas c, String s, float x, float y, float size, int color, Paint.Align align, boolean bold) {
        p.setStyle(Paint.Style.FILL); p.setColor(color); p.setTextAlign(align); p.setTextSize(dp(size));
        p.setTypeface(bold ? Typeface.create(Typeface.DEFAULT, Typeface.BOLD) : Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        c.drawText(s, dp(x), dp(y), p);
    }
    private void round(Canvas c, RectF r, float rad, int color) {
        p.setStyle(Paint.Style.FILL); p.setColor(color); p.setShadowLayer(dp(5), 0, dp(3), 0x28000000); c.drawRoundRect(r, dp(rad), dp(rad), p); p.clearShadowLayer();
    }

    @Override protected void onDraw(Canvas c) {
        super.onDraw(c); c.drawColor(BG);
        float w = getWidth()/density, h = getHeight()/density;
        // cheerful background blobs
        p.setStyle(Paint.Style.FILL); p.setColor(0x22FFD34E); c.drawCircle(dp(w-34), dp(62), dp(48), p);
        p.setColor(0x1F6CC5FF); c.drawCircle(dp(30), dp(h-60), dp(56), p);
        p.setColor(0x1F9C6DFF); c.drawCircle(dp(w-30), dp(h-125), dp(30), p);

        txt(c, "TIC-TAC-TOE!", w/2, 56, 30, DARK, Paint.Align.CENTER, true);
        txt(c, "🌈 Tiny game • BIG smiles 🌈", w/2, 84, 15, PURPLE, Paint.Align.CENTER, true);

        round(c, new RectF(dp(22), dp(102), dp(w-22), dp(165)), 22, CARD);
        String player = getPlayerText();
        txt(c, player, w/2, 140, 22, DARK, Paint.Align.CENTER, true);

        float size = Math.min(w - 42, 340);
        float left = (w-size)/2, top = 195, gap = 10;
        float cell = (size - gap*2)/3;
        p.setColor(0x14000000); p.setStyle(Paint.Style.FILL);
        for (int r=0;r<3;r++) for(int col=0;col<3;col++) {
            float x=left+col*(cell+gap), y=top+r*(cell+gap);
            round(c,new RectF(dp(x),dp(y),dp(x+cell),dp(y+cell)),22,Color.WHITE);
            String v=board[r*3+col];
            if (theme == 0) {
                if ("P1".equals(v)) drawSun(c,x+cell/2,y+cell/2,cell*.25f);
                if ("P2".equals(v)) drawMoon(c,x+cell/2,y+cell/2,cell*.25f);
            } else {
                if ("P1".equals(v)) drawCross(c,x+cell/2,y+cell/2,cell*.25f);
                if ("P2".equals(v)) drawZero(c,x+cell/2,y+cell/2,cell*.25f);
            }
        }

        float by = top + size + 28;
        round(c, new RectF(dp(50),dp(by),dp(w-50),dp(by+58)), 26, GREEN);
        txt(c, gameOver ? "🎉 PLAY AGAIN!" : "✨ TAP A SQUARE ✨", w/2, by+38, 20, Color.WHITE, Paint.Align.CENTER, true);
        txt(c, getThemeText(), w/2, h-30, 14, DARK, Paint.Align.CENTER, true);
    }

    private String getPlayerText() {
        if (gameOver) {
            if (winner == -1) return "🤝 SUPER DRAW!";
            if (theme == 0) return winner == 0 ? "🏆 SUN WINS!" : "🏆 MOON WINS!";
            return winner == 0 ? "🏆 CROSS WINS!" : "🏆 ZERO WINS!";
        }
        if (theme == 0) return current == 0 ? "☀️ Sun's turn!" : "🌙 Moon's turn!";
        return current == 0 ? "✕ Cross's turn!" : "⭕ Zero's turn!";
    }

    private String getThemeText() {
        if (theme == 0) return "Sun ☀️  vs  Moon 🌙";
        return "Cross ✕  vs  Zero ⭕";
    }

    private void drawSun(Canvas c,float cx,float cy,float rr) {
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(dp(5)); p.setColor(ORANGE);
        for(int i=0;i<8;i++){ double a=i*Math.PI/4; c.drawLine(dp(cx+(float)Math.cos(a)*(rr+7)),dp(cy+(float)Math.sin(a)*(rr+7)),dp(cx+(float)Math.cos(a)*(rr+17)),dp(cy+(float)Math.sin(a)*(rr+17)), p); }
        p.setStyle(Paint.Style.FILL); p.setColor(Color.rgb(255,205,62)); c.drawCircle(dp(cx),dp(cy),dp(rr),p);
        p.setColor(DARK); c.drawCircle(dp(cx-rr*.35f),dp(cy-rr*.10f),dp(3),p); c.drawCircle(dp(cx+rr*.35f),dp(cy-rr*.10f),dp(3),p);
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(dp(2)); c.drawArc(new RectF(dp(cx-rr*.38f),dp(cy-rr*.1f),dp(cx+rr*.38f),dp(cy+rr*.55f)),15,150,false,p);
    }
    private void drawMoon(Canvas c,float cx,float cy,float rr) {
        p.setStyle(Paint.Style.FILL); p.setColor(BLUE); c.drawCircle(dp(cx),dp(cy),dp(rr),p);
        p.setColor(Color.WHITE); c.drawCircle(dp(cx+rr*.35f),dp(cy-rr*.3f),dp(rr),p);
        p.setColor(DARK); c.drawCircle(dp(cx-rr*.15f),dp(cy-rr*.10f),dp(3),p); c.drawCircle(dp(cx+rr*.30f),dp(cy-rr*.10f),dp(3),p);
        p.setColor(0x55344F80); c.drawCircle(dp(cx-rr*.1f),dp(cy+rr*.3f),dp(4),p);
    }

    private void drawCross(Canvas c, float cx, float cy, float rr) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(dp(5));
        p.setColor(ORANGE);
        float offset = rr * 0.7f;
        c.drawLine(dp(cx - offset), dp(cy - offset), dp(cx + offset), dp(cy + offset), p);
        c.drawLine(dp(cx + offset), dp(cy - offset), dp(cx - offset), dp(cy + offset), p);
    }

    private void drawZero(Canvas c, float cx, float cy, float rr) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(dp(5));
        p.setColor(BLUE);
        c.drawCircle(dp(cx), dp(cy), dp(rr * 0.7f), p);
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction()!=MotionEvent.ACTION_UP) return true;
        float d=density, w=getWidth()/d; float size=Math.min(w-42,340), left=(w-size)/2, top=195, gap=10, cell=(size-gap*2)/3;
        float x=e.getX()/d, y=e.getY()/d;
        if(gameOver){ 
            float by=top+size+28; 
            if(x>=50&&x<=w-50&&y>=by&&y<=by+70){ 
                soundManager.playTapSound();
                reset(); 
            } 
            return true; 
        }
        if(x<left||x>left+size||y<top||y>top+size) return true;
        int col=(int)((x-left)/(cell+gap)), row=(int)((y-top)/(cell+gap));
        if(col>2||row>2) return true; 
        int idx=row*3+col;
        if(board[idx]!=null) return true;
        
        soundManager.playTapSound();
        board[idx]=current==0?"P1":"P2"; 
        haptic();
        
        if(checkWin(board[idx])){
            gameOver=true;
            winner=current;
            soundManager.playGameOverSound();
        }
        else { 
            boolean full=true; 
            for(String b:board) if(b==null) full=false; 
            if(full){
                gameOver=true;
                winner=-1;
                soundManager.playGameOverSound();
            } else current=1-current; 
        }
        invalidate(); 
        return true;
    }
    
    private boolean checkWin(String who){
        int[][] wins={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}}; 
        for(int[] a:wins) if(who.equals(board[a[0]])&&who.equals(board[a[1]])&&who.equals(board[a[2]])) return true; 
        return false;
    }
    
    private void reset(){
        for(int i=0;i<9;i++)board[i]=null;
        current=random.nextBoolean()?0:1;
        gameOver=false;
        winner=-1;
        invalidate();
    }
    
    private void haptic(){
        try{
            ((Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(30);
        }catch(Exception ignored){}
    }
}
