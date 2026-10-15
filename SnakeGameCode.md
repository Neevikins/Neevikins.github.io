import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * SnakeMouseTrapGame.java
 * CS Education Game: 2D Array Traversal & Grid Coordinate Learning
 * 
 * Concept:
 * - Controls: WASD / Arrow Keys to move the Snake.
 * - Objective: Trap the AI Mouse using your Snake body and Rock obstacles!
 * - CS Learning: Displays [row][col] indices, 1D Memory Mapping formulas,
 *   and real-time execution variable tracking.
 */
public class SnakeMouseTrapGame extends JFrame {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    // Game Elements
    private LinkedList<Point> snake = new LinkedList<>();
    private Point mousePos;
    private Point cheesePos;
    private boolean[][] rocks = new boolean[ROWS][COLS];
    
    private int dx = 1; // Default moving right (col + 1)
    private int dy = 0;
    
    private int score = 0;
    private boolean isGameOver = false;
    private boolean isGameWon = false;
    private Timer gameTimer;
    private Random rand = new Random();

    // UI Components
    private JPanel gridPanel;
    private JLabel[][] cellLabels = new JLabel[ROWS][COLS];
    private JTextArea codeDisplay;
    private JLabel headCoordLabel, mouseCoordLabel, ramIndexLabel, statusLabel;

    public SnakeMouseTrapGame() {
        setTitle("CS Edu: 2D Array Traversal - Snake Mouse Trap");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initGameData();

        // Build UI Panels
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createGridPanel(), BorderLayout.CENTER);
        add(createInspectorPanel(), BorderLayout.EAST);

        // Keyboard Controls
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
        setFocusable(true);

        // Game Loop Timer (Updates every 350ms)
        gameTimer = new Timer(350, e -> gameLoop());
        gameTimer.start();

        updateUIComponents();
    }

    private void initGameData() {
        snake.clear();
        // Initial Snake position: head at [3][3], tail at [3][2], [3][1]
        snake.add(new Point(3, 3));
        snake.add(new Point(2, 3));
        snake.add(new Point(1, 3));

        dx = 1;
        dy = 0;
        score = 0;
        isGameOver = false;
        isGameWon = false;

        // Reset rocks
        rocks = new boolean[ROWS][COLS];
        // Place default rock obstacles
        rocks[1][1] = true;
        rocks[1][6] = true;
        rocks[6][1] = true;
        rocks[6][6] = true;
        rocks[3][5] = true;

        spawnCheese();
        spawnMouse();
    }

    private void spawnCheese() {
        while (true) {
            int r = rand.nextInt(ROWS);
            int c = rand.nextInt(COLS);
            if (!isOccupied(r, c)) {
                cheesePos = new Point(c, r);
                break;
            }
        }
    }

    private void spawnMouse() {
        while (true) {
            int r = rand.nextInt(ROWS);
            int c = rand.nextInt(COLS);
            if (!isOccupied(r, c) && (cheesePos == null || (cheesePos.x != c || cheesePos.y != r))) {
                mousePos = new Point(c, r);
                break;
            }
        }
    }

    private boolean isOccupied(int r, int c) {
        if (rocks[r][c]) return true;
        for (Point p : snake) {
            if (p.x == c && p.y == r) return true;
        }
        return false;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        header.setBackground(new Color(25, 30, 45));

        JLabel title = new JLabel("2D Array Traversal: Snake vs Mouse");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.CYAN);

        statusLabel = new JLabel("Status: Use WASD/Arrows to Trap the Mouse!");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);

        JButton restartBtn = new JButton("Reset Game");
        restartBtn.addActionListener(e -> {
            initGameData();
            isGameOver = false;
            isGameWon = false;
            gameTimer.start();
            updateUIComponents();
            this.requestFocusInWindow();
        });

        header.add(title);
        header.add(statusLabel);
        header.add(restartBtn);
        return header;
    }

    private JPanel createGridPanel() {
        gridPanel = new JPanel(new GridLayout(ROWS, COLS, 4, 4));
        gridPanel.setBackground(new Color(15, 18, 25));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setOpaque(true);
                cell.setFont(new Font("Monospaced", Font.BOLD, 12));
                cell.setBorder(BorderFactory.createLineBorder(new Color(50, 60, 80)));
                cellLabels[r][c] = cell;
                gridPanel.add(cell);
            }
        }
        return gridPanel;
    }

    private JPanel createInspectorPanel() {
        JPanel inspector = new JPanel();
        inspector.setLayout(new BoxLayout(inspector, BoxLayout.Y_AXIS));
        inspector.setPreferredSize(new Dimension(360, 0));
        inspector.setBackground(new Color(30, 35, 50));
        inspector.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Code Box
        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN), "2D Array Memory Tracker",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13), Color.CYAN));
        codePanel.setBackground(new Color(20, 24, 35));

        codeDisplay = new JTextArea();
        codeDisplay.setEditable(false);
        codeDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        codeDisplay.setBackground(new Color(20, 24, 35));
        codeDisplay.setForeground(new Color(180, 220, 250));
        codeDisplay.setText(
            "// Java Grid Coordinate Lookup\n" +
            "int row = headY;\n" +
            "int col = headX;\n" +
            "grid[row][col] = SNAKE_HEAD;\n\n" +
            "// 1D Row-Major Memory Mapping\n" +
            "int ramAddress = (row * COLS) + col;\n" +
            "// 1D Column-Major Memory Mapping\n" +
            "int colMajorAddr = (col * ROWS) + row;"
        );
        codePanel.add(codeDisplay, BorderLayout.CENTER);

        // Variables Panel
        JPanel varsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        varsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.ORANGE), "Live CS Variables",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13), Color.ORANGE));
        varsPanel.setBackground(new Color(20, 24, 35));

        headCoordLabel = createVarLabel("Snake Head: grid[3][3]");
        mouseCoordLabel = createVarLabel("Mouse Pos: grid[r][c]");
        ramIndexLabel = createVarLabel("Row-Major RAM Index: 27");

        varsPanel.add(headCoordLabel);
        varsPanel.add(mouseCoordLabel);
        varsPanel.add(ramIndexLabel);

        // Instructions Panel
        JPanel instPanel = new JPanel(new BorderLayout());
        instPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GREEN), "CS Learning Objectives",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13), Color.GREEN));
        instPanel.setBackground(new Color(20, 24, 35));

        JTextArea instText = new JTextArea(
            "1. Grid indexing uses grid[row][col].\n" +
            "2. Row corresponds to Y-axis, Col to X-axis.\n" +
            "3. Eat Cheese (🧀) to grow your Snake.\n" +
            "4. Trap the Mouse (🐭) so it has 0 valid moves!\n" +
            "5. Avoid collision with Rocks (🪨) and yourself."
        );
        instText.setEditable(false);
        instText.setWrapStyleWord(true);
        instText.setLineWrap(true);
        instText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instText.setBackground(new Color(20, 24, 35));
        instText.setForeground(Color.LIGHT_GRAY);
        instPanel.add(instText, BorderLayout.CENTER);

        inspector.add(codePanel);
        inspector.add(Box.createRigidArea(new Dimension(0, 10)));
        inspector.add(varsPanel);
        inspector.add(Box.createRigidArea(new Dimension(0, 10)));
        inspector.add(instPanel);

        return inspector;
    }

    private JLabel createVarLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void handleKeyPress(int key) {
        if (isGameOver) return;

        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy != 1) {
            dx = 0; dy = -1;
        } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && dy != -1) {
            dx = 0; dy = 1;
        } else if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && dx != 1) {
            dx = -1; dy = 0;
        } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && dx != -1) {
            dx = 1; dy = 0;
        }
    }

    private void gameLoop() {
        if (isGameOver) return;

        // Move Snake
        Point head = snake.getFirst();
        int newCol = head.x + dx;
        int newRow = head.y + dy;

        // Check Wall Collision
        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
            triggerGameOver("Collision! Snake hit grid boundaries.");
            return;
        }

        // Check Rock Collision
        if (rocks[newRow][newCol]) {
            triggerGameOver("Collision! Snake hit a Rock at grid[" + newRow + "][" + newCol + "].");
            return;
        }

        // Check Self Collision
        for (Point p : snake) {
            if (p.x == newCol && p.y == newRow) {
                triggerGameOver("Collision! Snake ran into its own body.");
                return;
            }
        }

        Point newHead = new Point(newCol, newRow);
        snake.addFirst(newHead);

        // Check Cheese Eating
        if (cheesePos != null && newCol == cheesePos.x && newRow == cheesePos.y) {
            score += 10;
            spawnCheese();
        } else {
            snake.removeLast(); // Keep size unless eating cheese
        }

        // Move Mouse AI
        moveMouseAI();

        // Check if Mouse is Trapped
        if (checkMouseTrapped()) {
            isGameWon = true;
            isGameOver = true;
            gameTimer.stop();
            statusLabel.setText("🎉 VICTORY! You trapped the mouse in the 2D grid!");
            statusLabel.setForeground(Color.GREEN);
        }

        updateUIComponents();
    }

    private void moveMouseAI() {
        int[] dRows = {-1, 1, 0, 0};
        int[] dCols = {0, 0, -1, 1};
        List<Point> validMoves = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int nr = mousePos.y + dRows[i];
            int nc = mousePos.x + dCols[i];

            if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && !isOccupied(nr, nc)) {
                validMoves.add(new Point(nc, nr));
            }
        }

        // If mouse can move, pick move furthest from snake head
        if (!validMoves.isEmpty()) {
            Point bestMove = validMoves.get(0);
            double maxDist = -1;
            Point snakeHead = snake.getFirst();

            for (Point move : validMoves) {
                double dist = move.distance(snakeHead);
                if (dist > maxDist) {
                    maxDist = dist;
                    bestMove = move;
                }
            }
            mousePos = bestMove;
        }
    }

    private boolean checkMouseTrapped() {
        int[] dRows = {-1, 1, 0, 0};
        int[] dCols = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nr = mousePos.y + dRows[i];
            int nc = mousePos.x + dCols[i];

            if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                if (!isOccupied(nr, nc)) {
                    return false; // Mouse still has an open adjacent square
                }
            }
        }
        return true; // Zero valid moves left
    }

    private void triggerGameOver(String reason) {
        isGameOver = true;
        gameTimer.stop();
        statusLabel.setText("GAME OVER: " + reason);
        statusLabel.setForeground(Color.RED);
        updateUIComponents();
    }

    private void updateUIComponents() {
        Point head = snake.getFirst();

        // Update Live Variable Labels
        headCoordLabel.setText("Snake Head: grid[" + head.y + "][" + head.x + "]");
        mouseCoordLabel.setText("Mouse Pos: grid[" + mousePos.y + "][" + mousePos.x + "]");
        
        int ramAddr = (head.y * COLS) + head.x;
        ramIndexLabel.setText("1D RAM Index: " + ramAddr + " (Row
