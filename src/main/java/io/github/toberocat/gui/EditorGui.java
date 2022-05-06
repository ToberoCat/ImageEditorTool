package io.github.toberocat.gui;

import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.loop.RenderLoop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class EditorGui extends JFrame {
    public static EditorGui EDITOR;
    private final RenderLoop loop;

    public EditorGui() throws IOException {
        //Create window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 450);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Create canvas
        Canvas canvas = new Canvas();
        ImageRenderer.register(canvas);
        contentPane.add(canvas, BorderLayout.CENTER);

        new EventListener(canvas);

        // Create loop
        loop = new RenderLoop(canvas);
        loop.start();

        EDITOR = this;
    }
}

