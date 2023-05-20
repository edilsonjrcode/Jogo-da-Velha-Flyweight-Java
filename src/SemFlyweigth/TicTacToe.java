package SemFlyweigth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TicTacToeGame extends JPanel {
    private JButton[][] buttons;
    private boolean playerX;
    private int moves;

    public TicTacToeGame() {
        buttons = new JButton[3][3];
        playerX = true;
        moves = 0;

        setLayout(new GridLayout(3, 3));

        initializeButtons();

        setPreferredSize(new Dimension(300, 300));
    }

    private void initializeButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 48));
                button.addActionListener(new ButtonClickListener(row, col));
                buttons[row][col] = button;
                add(button);
            }
        }
    }

    private void makeMove(int row, int col) {
        JButton button = buttons[row][col];
        if (button.getText().isEmpty()) {
            if (playerX) {
                button.setText("X");
            } else {
                button.setText("O");
            }
            moves++;
            checkWinner(row, col);
            playerX = !playerX;
        }
    }

    private void checkWinner(int row, int col) {
        String symbol = playerX ? "X" : "O";

        // Check rows
        if (buttons[row][0].getText().equals(symbol) &&
                buttons[row][1].getText().equals(symbol) &&
                buttons[row][2].getText().equals(symbol)) {
            showWinnerDialog(symbol);
        }

        // Check columns
        if (buttons[0][col].getText().equals(symbol) &&
                buttons[1][col].getText().equals(symbol) &&
                buttons[2][col].getText().equals(symbol)) {
            showWinnerDialog(symbol);
        }

        // Check diagonal (top-left to bottom-right)
        if (buttons[0][0].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][2].getText().equals(symbol)) {
            showWinnerDialog(symbol);
        }

        // Check diagonal (top-right to bottom-left)
        if (buttons[0][2].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][0].getText().equals(symbol)) {
            showWinnerDialog(symbol);
        }

        // Check for a draw
        if (moves == 9) {
            showDrawDialog();
        }
    }

    private void showWinnerDialog(String symbol) {
        JOptionPane.showMessageDialog(this, "Player " + symbol + " wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
        resetGame();
    }

    private void showDrawDialog() {
        JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.PLAIN_MESSAGE);
        resetGame();
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
        playerX = true;
        moves = 0;
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            makeMove(row, col);
        }
    }
}

public class TicTacToe {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Tic Tac Toe");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new TicTacToeGame());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
