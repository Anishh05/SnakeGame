import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // --------------------------
    // Inner class for a tile
    // --------------------------
    private class Tile {
        int x;
        int y;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // --------------------------
    // Game variables
    // --------------------------
    int boardWidth;
    int boardHeight;
    int tileSize = 15;

    Tile snakeHead;             // Snake's head
    ArrayList<Tile> snakeBody;  // Snake's body
    Tile food;                  // Food tile
    Random random;              // For random food placement

    Timer gameLoop;             // Game loop timer
    int velocityX;              // Snake movement in X
    int velocityY;              // Snake movement in Y
    boolean gameOver = false;

    // --------------------------
    // Constructor
    // --------------------------
    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Initialize snake head, body, and food
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        food = new Tile(10, 10);

        random = new Random();
        placeFood(); // Place the first food

        // Initial velocity is 0 (not moving)
        velocityX = 0;
        velocityY = 0;

        // Start game loop
        gameLoop = new Timer(70, this);
        gameLoop.start();
    }

    // --------------------------
    // Drawing the game
    // --------------------------
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Draw grid
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Draw snake head
        g.setColor(Color.GRAY);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Draw snake body
        g.setColor(Color.CYAN);
        for (Tile part : snakeBody) {
            g.fill3DRect(part.x * tileSize, part.y * tileSize, tileSize, tileSize, true);
        }

        // Draw score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("-------- GAME OVER --------- SCORE: " + snakeBody.size(),
                    tileSize - 16, tileSize);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
        }
    }

    // --------------------------
    // Place food randomly
    // --------------------------
    private void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // --------------------------
    // Check collision between two tiles
    // --------------------------
    private boolean collision(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    // --------------------------
    // Move snake
    // --------------------------
    private void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y)); // Add new body part
            placeFood();
        }

        // Move body parts
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile part = snakeBody.get(i);
            if (i == 0) {
                part.x = snakeHead.x;
                part.y = snakeHead.y;
            } else {
                Tile prevPart = snakeBody.get(i - 1);
                part.x = prevPart.x;
                part.y = prevPart.y;
            }
        }

        // Move head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Check collision with body
        for (Tile part : snakeBody) {
            if (collision(snakeHead, part)) {
                gameOver = true;
            }
        }

        // Check collision with walls
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize ||
            snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }

    // --------------------------
    // Timer event (game loop)
    // --------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    // --------------------------
    // Keyboard controls
    // --------------------------
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if ((code == KeyEvent.VK_W || code == KeyEvent.VK_UP) && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if ((code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if ((code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if ((code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    // Unused KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
