package io.github.toberocat.gui;

import io.github.toberocat.actions.ActionLog;
import io.github.toberocat.gui.image.ImageBatch;
import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.loop.RenderLoop;
import io.github.toberocat.utils.Utility;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EditorGui extends JFrame implements WindowListener {
    public static EditorGui EDITOR;

    public EditorGui() throws IOException {
        //Create window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 450);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        getContentPane().setBackground(Color.decode("#091114"));
        setIconImage(ImageIO.read(getClass().getResource("/icon.png")));


        // Create canvas
        Canvas canvas = new Canvas();
        ImageRenderer.register(canvas);
        contentPane.add(canvas, BorderLayout.CENTER);

        new EventListener(canvas);

        // Create loop
        RenderLoop loop = new RenderLoop(canvas);
        loop.start();

        EDITOR = this;

        addWindowListener(this);
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        ActionLog.cleanup();
        try {
            File file = new File("last-edit");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(Utility.currentLastFile - ImageBatch.BATCHED - 1));
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

