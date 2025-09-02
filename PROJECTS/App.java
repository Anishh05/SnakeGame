import javax.swing.*;
public class App {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frm = new JFrame("Snake Game");
        frm.setVisible(true);
        frm.setSize(boardWidth,boardHeight);
        frm.setLocationRelativeTo(null); // itll open the window in the center of the screen
        frm.setResizable(false);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakegame = new SnakeGame(boardWidth, boardHeight);
        frm.add(snakegame);
        frm.pack();
        snakegame.requestFocus();
    }
    
}
