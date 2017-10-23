package tetris;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import tetris.model.*;

import java.util.List;

/**
 * Created by thomas on 1/21/14.
 */
public class BoardRenderer implements Renderer {
    public static final int SQUARE_WIDTH = 25;
    public static final int SQUARE_HEIGHT = 25;
    public static final int BORDER_WIDTH = 1;
    public static final int CENTER_BOARD_LEFT = (Tetris.WIDTH - (Board.BOARD_WIDTH * SQUARE_WIDTH )) / 2;
    public static final int CENTER_BOARD_RIGHT = ((Tetris.WIDTH + (Board.BOARD_WIDTH * SQUARE_WIDTH )) / 2) + BORDER_WIDTH;
    public static final int CENTER_BOARD_TOP = ((Tetris.HEIGHT - (Board.BOARD_HEIGHT * SQUARE_HEIGHT)) / 2) + BORDER_WIDTH;
    public static final int CENTER_BOARD_BOTTOM = ((Tetris.HEIGHT + (Board.BOARD_HEIGHT * SQUARE_HEIGHT)) / 2) + BORDER_WIDTH;

    public static final int PREVIEW_WIDTH = SQUARE_WIDTH * 5;
    public static final int PREVIEW_HEIGHT = SQUARE_HEIGHT * 5;

    private Board board;
    private GameContainer gameContainer;
    private boolean isShowingGrid = false;
    private boolean isShowingShadow = false;
    private static final Color GRID_COLOR = new Color(50, 50, 50);

    //todo: move this somewhere else
    public static final Color colors[] = {
            new Color(0, 0, 0),
            new Color(1, 255, 255),  //I
            new Color(0, 0, 255),    //J
            new Color(255, 165, 0),  //L
            new Color(255, 255, 2),  //O
            new Color(128, 255, 1),  //S
            new Color(128, 1, 128),  //T
            new Color(255, 0, 0)     //Z
    };

    public BoardRenderer(Board board, GameContainer gameContainer) {
        this.board = board;
        this.gameContainer = gameContainer;
    }

    @Override
    public void render(Graphics graphics) {
        if (isShowingGrid) {
            drawGrid(graphics);
        }

        drawBoardBorder(graphics);

        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                Tetromino block = board.getBlock(j, Board.BOARD_HEIGHT - i - 1);

                if (block != Tetromino.None) {
                    int x = j * SQUARE_WIDTH + CENTER_BOARD_LEFT + 1;
                    int y = i * SQUARE_HEIGHT + CENTER_BOARD_TOP;
                    drawSquare(x, y, colors[block.ordinal()], graphics);
                }
            }
        }

        Block currentBlock = board.getCurrentBlock();
        if (currentBlock != null && currentBlock.getBlock() != Tetromino.None) {
            for (int i = 0; i < 4; i++) {
                int x = (board.getCurrentX() + currentBlock.x(i)) * SQUARE_WIDTH + CENTER_BOARD_LEFT + 1;
                int y = CENTER_BOARD_TOP + (Board.BOARD_HEIGHT - (board.getCurrentY() - currentBlock.y(i)) - 1) * SQUARE_HEIGHT;
                drawSquare(x, y, colors[currentBlock.getBlock().ordinal()], graphics);

                if (isShowingShadow) {
                    int nextY = currentBlock.y(i) + 1;
                    Tetromino nextBlock = board.getBlock(x, nextY);
                    while (nextBlock != null && nextBlock != Tetromino.None) {
                        drawSquare(x, nextY + SQUARE_HEIGHT, Color.white, graphics);
                        nextBlock = board.getBlock(x, ++nextY);
                    }
                }
            }
        }

        //Preview Next Piece
        gameContainer.getDefaultFont().drawString(CENTER_BOARD_RIGHT + 50, CENTER_BOARD_TOP + 50, Tetris.resources.getString("next"));
        graphics.setColor(Color.white);
        graphics.drawRoundRect(CENTER_BOARD_RIGHT + 50, CENTER_BOARD_TOP + 70, PREVIEW_WIDTH + 10, PREVIEW_HEIGHT + 10, 5);
        graphics.fillRoundRect(CENTER_BOARD_RIGHT + 55, CENTER_BOARD_TOP + 75, PREVIEW_WIDTH, PREVIEW_HEIGHT, 5);

        Block nextBlock = board.getNextPiece();
        if (nextBlock != null && nextBlock.getBlock() != Tetromino.None) {
            for (int i = 0; i < 4; i++) {
                int[] pos = nextBlock.getRelativeCoordsTable(nextBlock.getBlock().ordinal(), i);
                int x = CENTER_BOARD_RIGHT + 125 + pos[0] * SQUARE_WIDTH;
                int y = CENTER_BOARD_TOP + 100 + pos[1] * SQUARE_HEIGHT;

                drawSquare(x, y, colors[nextBlock.getBlock().ordinal()], graphics);
            }
        }

        List<ParticleEmitter> emitters = board.getParticleEmitters();
        if (emitters != null) {
            for (ParticleEmitter particleEmitter : emitters) {
                for (Particle particle : particleEmitter.getParticles()) {
                    if (particle.isAlive()) {
                        drawParticle(particle, graphics);
                    }
                }
            }
        }
    }

    private void drawSquare(int x, int y, Color color, Graphics g) {
        drawSquare(x, y, color, g, true);
    }

    private void drawSquare(int x, int y, Color color, Graphics g, boolean drawHighlight) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, SQUARE_WIDTH - 2, SQUARE_HEIGHT - 2);
        g.setLineWidth(1);
        g.setColor(color.brighter());
        g.drawLine(x, y + SQUARE_HEIGHT - 1, x, y); //left
        g.drawLine(x, y, x + SQUARE_WIDTH - 1, y); //top

        g.setColor(color.darker());
        g.drawLine(x + 1, y + SQUARE_HEIGHT - 1, x + SQUARE_WIDTH - 1, y + SQUARE_HEIGHT - 1); //right
        g.drawLine(x + SQUARE_WIDTH - 1, y + SQUARE_HEIGHT - 1, x + SQUARE_WIDTH - 1, y + 1); //bottom

        if (drawHighlight) {
            //NES Tetris style specular highlight
            g.setColor(Color.white);
            g.fillRect(x + 2, y + 2, 5, 2);
            g.fillRect(x + 2, y + 4, 2, 3);
        }
    }

    private void drawBoardBorder(Graphics g) {
        Color borderColor = new Color(100, 100, 100);
        for (int i = 0; i < Board.BOARD_WIDTH + 2; i++) {
            int x = (i * SQUARE_WIDTH + CENTER_BOARD_LEFT - SQUARE_WIDTH) + 1;
            int y = CENTER_BOARD_TOP - SQUARE_HEIGHT;
            drawSquare(x, y, borderColor, g, false); //top
            y = CENTER_BOARD_BOTTOM;
            drawSquare(x, y, borderColor, g, false); //bottom
        }

        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            int x = (CENTER_BOARD_LEFT - SQUARE_WIDTH) + 1;
            int y = i * SQUARE_HEIGHT + CENTER_BOARD_TOP;
            drawSquare(x, y, borderColor, g, false); //left

            x = CENTER_BOARD_RIGHT;
            drawSquare(x, y, borderColor, g, false); //right
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(GRID_COLOR);
        for (int i = 1; i < Board.BOARD_WIDTH; i++) {
            int x = CENTER_BOARD_LEFT + (i * SQUARE_WIDTH);
            g.drawLine(x, CENTER_BOARD_BOTTOM, x, CENTER_BOARD_TOP);
        }

        for (int i = 1; i < Board.BOARD_HEIGHT; i++) {
            int y = CENTER_BOARD_TOP + (i * SQUARE_HEIGHT);
            g.drawLine(CENTER_BOARD_LEFT, y, CENTER_BOARD_RIGHT, y);
        }
    }

    private void drawParticle(Particle particle, Graphics g) {
        g.setColor(particle.getColor());
        float x = (float)(particle.getX() * SQUARE_WIDTH + CENTER_BOARD_LEFT + 1);
        float y = (float)(particle.getY() * SQUARE_HEIGHT + CENTER_BOARD_TOP);
        g.fillRect(x, y, particle.getWidth(), particle.getHeight());
    }

    public void setShowingGrid(boolean isShowingGrid) {
        this.isShowingGrid = isShowingGrid;
    }

    public boolean isShowingGrid() {
        return isShowingGrid;
    }

    public boolean isShowingShadow() {
        return isShowingShadow;
    }

    public void setShowingShadow(boolean isShowingShadow) {
        if (isShowingShadow) {
            System.out.println("Showing shadow");
        } else {
            System.out.println("Hiding shadow");
        }
        this.isShowingShadow = isShowingShadow;
    }
}
