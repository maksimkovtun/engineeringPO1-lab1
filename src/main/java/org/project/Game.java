package org.project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Game extends JFrame {
    private final Board board;
    private JPanel boardPanel;
    private int selectedRow = -1, selectedCol = -1;
    public Game() {
        board = new Board();
        setTitle("Шашки");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeGUI();
    }
    private void initializeGUI() {
        boardPanel = new JPanel(new GridLayout(8, 8));
        updateBoard();
        add(boardPanel);
    }
    private void updateBoard() {
        boardPanel.removeAll();
        Checker[][] boardArray = board.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                Checker checker = boardArray[row][col];
                Color background = (row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY;
                if (checker != null) {
                    button.setFocusPainted(false);
                    button.setBorderPainted(false);
                    button.setIcon(createCheckerIcon(checker.color(), background));
                }
                button.setBackground(background);
                button.setMargin(new Insets(0, 0, 0, 0));
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> handleClick(finalRow, finalCol));
                boardPanel.add(button);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
        if (board.isGameOver()) {
            String winner = board.hasMoves("white") ? "Белые" : "Черные";
            JOptionPane.showMessageDialog(this, "Игра окончена! Победили " + winner + "!");
            System.exit(0);
        }
    }
    private Icon createCheckerIcon(String color, Color background) {
        int size = 50;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(background);
        g2d.fillRect(0, 0, size, size);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color.equals("black") ? Color.BLACK : Color.WHITE);
        g2d.fillOval(5, 5, size - 10, size - 10);
        g2d.dispose();
        return new ImageIcon(image);
    }
    private void handleClick(int row, int col) {
        if (selectedRow == -1 && selectedCol == -1) {
            if (board.getChecker(row, col) != null && board.getChecker(row, col).color().equals(board.getCurrentPlayer())) {
                if (!board.mustCapture() || board.canCapture(row, col)) {
                    selectedRow = row;
                    selectedCol = col;
                }
            }
        } else {
            boolean moveSuccess = board.moveChecker(selectedRow, selectedCol, row, col);
            if (moveSuccess) {
                if (board.mustCapture()) {
                    selectedRow = row;
                    selectedCol = col;
                } else {
                    selectedRow = -1;
                    selectedCol = -1;
                }
            } else {
                selectedRow = -1;
                selectedCol = -1;
            }
        }
        updateBoard();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }
}
