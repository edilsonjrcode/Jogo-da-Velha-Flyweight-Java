package ComFlyweigth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/*Estado intrínseco: O estado intrínseco representa os dados compartilhados entre os objetos Flyweight. No exemplo do jogo da velha, o estado intrínseco é representado pelas classes concretas XTile e OTile, onde as imagens dos símbolos "X" e "O" são compartilhadas entre várias instâncias. Essas classes contêm apenas o estado intrínseco e implementam a interface Tile.

Classes concretas: As classes concretas são as implementações específicas dos objetos Flyweight. No exemplo, as classes XTile e OTile são as classes concretas que implementam a interface Tile. Cada classe concreta representa um símbolo específico do jogo da velha.

Fábrica Flyweight: A fábrica Flyweight é responsável pela criação e gerenciamento dos objetos Flyweight. No código fornecido, a classe TileFactory atua como a fábrica Flyweight. Ela possui um cache (mapa tileCache) que armazena os objetos Flyweight já criados. O método getTile é responsável por retornar um objeto Flyweight existente ou criar um novo, conforme necessário.

Cliente: O código do cliente é responsável por utilizar os objetos Flyweight. No exemplo, a classe TicTacToeGame atua como o cliente. Ela solicita os objetos Flyweight à fábrica (TileFactory) e utiliza esses objetos para desenhar os símbolos do jogo da velha.

A separação entre estado intrínseco (compartilhado) e estado extrínseco (específico de cada objeto) é uma das principais características do padrão Flyweight. O estado intrínseco é armazenado nos objetos Flyweight compartilhados, enquanto o estado extrínseco é passado externamente quando necessário.*/

interface Tile {
    void draw(Graphics g, int x, int y);
}

class XTile implements Tile {
    private static final ImageIcon image = new ImageIcon("x.png");

    @Override
    public void draw(Graphics g, int x, int y) {
        image.paintIcon(null, g, x, y);
    }
}

class OTile implements Tile {
    private static final ImageIcon image = new ImageIcon("o.png");

    @Override
    public void draw(Graphics g, int x, int y) {
        image.paintIcon(null, g, x, y);
    }
}

class TileFactory {
    private static final Map<String, Tile> tileCache = new HashMap<>();

    public static Tile getTile(String symbol) {
        Tile tile = tileCache.get(symbol);

        if (tile == null) {
            switch (symbol) {
                case "X":
                    tile = new XTile();
                    break;
                case "O":
                    tile = new OTile();
                    break;
            }
            tileCache.put(symbol, tile);
        }

        return tile;
    }
}

class TicTacToeGame extends JPanel {
    private Tile[][] tiles;
    private boolean playerX;
    private int moves;

    public TicTacToeGame() {
        tiles = new Tile[3][3];
        playerX = true;
        moves = 0;

        setLayout(new GridLayout(3, 3));

        initializeTiles();

        setPreferredSize(new Dimension(300, 300));
    }

    private void initializeTiles() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 48));
                button.addActionListener(new ButtonClickListener(row, col));
                add(button);

                tiles[row][col] = TileFactory.getTile("");
            }
        }
    }

    private void makeMove(int row, int col) {
        JButton button = (JButton) getComponent(row * 3 + col);
        if (button.getText().isEmpty()) {
            String symbol = playerX ? "X" : "O";
            Tile tile = TileFactory.getTile(symbol);
            tiles[row][col] = tile;
            button.setText(symbol);
            moves++;
            checkWinner(row, col);
            playerX = !playerX;
        }
    }

    private void checkWinner(int row, int col) {
        String symbol = playerX ? "X" : "O";

        // Check rows
        if (tiles[row][0] == tiles[row][1] && tiles[row][0] == tiles[row][2] && !buttonIsEmpty(row, 0)) {
            showWinnerDialog(symbol);
        }

        // Check columns
        if (tiles[0][col] == tiles[1][col] && tiles[0][col] == tiles[2][col] && !buttonIsEmpty(0, col)) {
            showWinnerDialog(symbol);
        }

        // Check diagonal (top-left to bottom-right)
        if (tiles[0][0] == tiles[1][1] && tiles[0][0] == tiles[2][2] && !buttonIsEmpty(0, 0)) {
            showWinnerDialog(symbol);
        }

        // Check diagonal (top-right to bottom-left)
        if (tiles[0][2] == tiles[1][1] && tiles[0][2] == tiles[2][0] && !buttonIsEmpty(0, 2)) {
            showWinnerDialog(symbol);
        }

        // Check for a draw
        if (moves == 9) {
            showDrawDialog();
        }
    }

    private boolean buttonIsEmpty(int row, int col) {
        JButton button = (JButton) getComponent(row * 3 + col);
        return button.getText().isEmpty();
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
                JButton button = (JButton) getComponent(row * 3 + col);
                button.setText("");
                tiles[row][col] = TileFactory.getTile("");
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
