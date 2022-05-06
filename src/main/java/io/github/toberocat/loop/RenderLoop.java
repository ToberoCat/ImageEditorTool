package io.github.toberocat.loop;

import io.github.toberocat.async.AsyncTask;
import io.github.toberocat.callbacks.ExceptionCallback;
import io.github.toberocat.gui.image.ImageRenderer;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class RenderLoop implements Runnable {
    private final Canvas canvas;
    private Thread thread;
    private boolean running;

    public static Runnable EXECUTE_ON_LOAD;

    public RenderLoop(Canvas canvas) {
        this.canvas = canvas;
    }

    private void render() {
        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //Draw Here

        ImageRenderer.instance().render(g);

        //End Drawing
        bs.show();
        g.dispose();
    }

    @Override
    public void run() {
        int fps = 60;
        double timePerTick = 1000000000f / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (EXECUTE_ON_LOAD != null) EXECUTE_ON_LOAD.run();

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                render();
                ticks++;
                delta--;
            }

            if (timer >= 1000000000) {
                if (ticks < fps || ticks > fps) System.out.println("Fps: " + ticks);

                ticks = 0;
                timer = 0;
            }
        }

        stop();
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
