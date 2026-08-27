package com.example.kidstictactoe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ThemeSelectionActivity extends Activity {
    private float density;
    private int selectedTheme = -1; // -1 = none, 0 = Sun/Moon, 1 = Cross/Zero
    private final int ORANGE = Color.rgb(255, 123, 84);
    private final int BLUE = Color.rgb(79, 164, 214);
    private final int GREEN = Color.rgb(107, 190, 108);
    private final int PURPLE = Color.rgb(145, 111, 196);
    private final int DARK = Color.rgb(86, 67, 60);
    private final int BG = Color.rgb(255, 250, 235);
    private final int CARD = Color.rgb(255, 239, 190);
    private SoundManager soundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        soundManager = new SoundManager(this);
        
        setContentView(new ThemeSelectionView(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release();
        }
    }

    private class ThemeSelectionView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        public ThemeSelectionView(Activity context) {
            super(context);
            density = getResources().getDisplayMetrics().density;
            p.setStrokeCap(Paint.Cap.ROUND);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        private float dp(float v) { return v * density; }
        
        private void txt(Canvas c, String s, float x, float y, float size, int color, Paint.Align align, boolean bold) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(color);
            p.setTextAlign(align);
            p.setTextSize(dp(size));
            p.setTypeface(bold ? Typeface.create(Typeface.DEFAULT, Typeface.BOLD) : Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            c.drawText(s, dp(x), dp(y), p);
        }

        private void round(Canvas c, RectF r, float rad, int color) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(color);
            p.setShadowLayer(dp(5), 0, dp(3), 0x28000000);
            c.drawRoundRect(r, dp(rad), dp(rad), p);
            p.clearShadowLayer();
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            c.drawColor(BG);
            
            float w = getWidth() / density;
            float h = getHeight() / density;
            
            // Background decorations
            p.setStyle(Paint.Style.FILL);
            p.setColor(0x22FFD34E);
            c.drawCircle(dp(w - 34), dp(62), dp(48), p);
            p.setColor(0x1F6CC5FF);
            c.drawCircle(dp(30), dp(h - 60), dp(56), p);
            p.setColor(0x1F9C6DFF);
            c.drawCircle(dp(w - 30), dp(h - 125), dp(30), p);
            
            // Title
            txt(c, "SELECT YOUR GAME! 🎮", w / 2, 56, 28, DARK, Paint.Align.CENTER, true);
            txt(c, "Choose your favorite players", w / 2, 90, 14, PURPLE, Paint.Align.CENTER, false);
            
            // Sun/Moon Option
            float card1Y = 130;
            int color1 = selectedTheme == 0 ? Color.rgb(255, 215, 0) : CARD;
            round(c, new RectF(dp(30), dp(card1Y), dp(w - 30), dp(card1Y + 100)), 20, color1);
            
            // Draw sun and moon icons
            drawSunIcon(c, 70, card1Y + 50, 30);
            txt(c, "vs", w / 2, card1Y + 65, 20, DARK, Paint.Align.CENTER, true);
            drawMoonIcon(c, w - 70, card1Y + 50, 30);
            
            txt(c, "☀️ SUN  vs  MOON 🌙", w / 2, card1Y + 95, 18, DARK, Paint.Align.CENTER, true);
            
            // Cross/Zero Option
            float card2Y = 260;
            int color2 = selectedTheme == 1 ? Color.rgb(255, 215, 0) : CARD;
            round(c, new RectF(dp(30), dp(card2Y), dp(w - 30), dp(card2Y + 100)), 20, color2);
            
            // Draw cross and zero icons
            drawCrossIcon(c, 70, card2Y + 50, 30);
            txt(c, "vs", w / 2, card2Y + 65, 20, DARK, Paint.Align.CENTER, true);
            drawZeroIcon(c, w - 70, card2Y + 50, 30);
            
            txt(c, "✕ CROSS  vs  ZERO ⭕", w / 2, card2Y + 95, 18, DARK, Paint.Align.CENTER, true);
            
            // Start Button
            float btnY = h - 100;
            int btnColor = selectedTheme == -1 ? Color.rgb(180, 180, 180) : GREEN;
            round(c, new RectF(dp(50), dp(btnY), dp(w - 50), dp(btnY + 60)), 20, btnColor);
            txt(c, selectedTheme == -1 ? "SELECT A THEME" : "🎉 LET'S PLAY! 🎉", w / 2, btnY + 40, 20, Color.WHITE, Paint.Align.CENTER, true);
        }

        private void drawSunIcon(Canvas c, float x, float y, float size) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(3));
            p.setColor(ORANGE);
            c.drawCircle(dp(x), dp(y), dp(size * 0.6f), p);
            
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.rgb(255, 205, 62));
            c.drawCircle(dp(x), dp(y), dp(size * 0.5f), p);
        }

        private void drawMoonIcon(Canvas c, float x, float y, float size) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(BLUE);
            c.drawCircle(dp(x), dp(y), dp(size * 0.6f), p);
            
            p.setColor(BG);
            c.drawCircle(dp(x + size * 0.3f), dp(y - size * 0.2f), dp(size * 0.6f), p);
        }

        private void drawCrossIcon(Canvas c, float x, float y, float size) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(5));
            p.setColor(ORANGE);
            
            float offset = size * 0.4f;
            c.drawLine(dp(x - offset), dp(y - offset), dp(x + offset), dp(y + offset), p);
            c.drawLine(dp(x + offset), dp(y - offset), dp(x - offset), dp(y + offset), p);
        }

        private void drawZeroIcon(Canvas c, float x, float y, float size) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(5));
            p.setColor(BLUE);
            c.drawCircle(dp(x), dp(y), dp(size * 0.4f), p);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            if (e.getAction() != MotionEvent.ACTION_UP) return true;
            
            float d = density;
            float w = getWidth() / d;
            float h = getHeight() / d;
            float x = e.getX() / d;
            float y = e.getY() / d;
            
            soundManager.playTapSound();
            
            // Check Sun/Moon option
            if (y >= 130 && y <= 230) {
                selectedTheme = 0;
                invalidate();
                return true;
            }
            
            // Check Cross/Zero option
            if (y >= 260 && y <= 360) {
                selectedTheme = 1;
                invalidate();
                return true;
            }
            
            // Check Start button
            float btnY = h - 100;
            if (selectedTheme != -1 && x >= 50 && x <= w - 50 && y >= btnY && y <= btnY + 60) {
                soundManager.playGameOverSound();
                Intent intent = new Intent(ThemeSelectionActivity.this, MainActivity.class);
                intent.putExtra("theme", selectedTheme);
                startActivity(intent);
                finish();
                return true;
            }
            
            return true;
        }
    }
}
