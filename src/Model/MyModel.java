package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;


public class MyModel extends Observable implements IModel {

    private int row;
    private int col;
    private int[][] maze;
    private int[][] sol;
    private Position endPosition;
    private Maze originMaze;
    private boolean solved;
    private boolean gameIsOver;

//    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public int[][] generateMaze(int row1, int col1){
        Server serverMazeGenerator;
        serverMazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        serverMazeGenerator.start();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        solved = false;
                        gameIsOver = false;
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeSize = new int[]{row1, col1};
                        toServer.writeObject(mazeSize);
                        toServer.flush();
                        byte[] compressMaze = (byte[]) fromServer.readObject();
                        InputStream inputStream1 = new MyDecompressorInputStream(new ByteArrayInputStream(compressMaze));
                        byte[] decompressedMaze = new byte[12+ mazeSize[0] * mazeSize[1]];
                        inputStream1.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        Position UpdatePos;
                        UpdatePos = maze.getStartPosition();
                        originMaze = maze;
                        row = UpdatePos.getRowIndex();
                        col = UpdatePos.getColumnIndex();
                        endPosition = maze.getGoalPosition();
                        deepCopyMaze(maze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverMazeGenerator.stop();
        setChanged();
        notifyObservers();
        return maze;
    }
    @Override
    public void move(KeyCode movement){
        int r = row;
        int c = col;
        switch (movement) {
            case DIGIT8 :case NUMPAD8:
                if (ifLegalMove(r - 1, c))
                    this.row--;
                break;
            case DIGIT2 :case NUMPAD2:
                if (ifLegalMove(r + 1, c))
                    this.row++;
                break;
            case DIGIT6 :case NUMPAD6:
                if (ifLegalMove(r, c + 1))
                    this.col++;
                break;
            case DIGIT4 :case NUMPAD4:
                if (ifLegalMove(r, c - 1))
                    this.col--;
                break;
            case DIGIT3:
                if (ifLegalMove(r + 1, c + 1))
                    if (ifLegalMove(r, c + 1) || ifLegalMove(r + 1, c)) {
                        this.row++;
                        this.col++;
                    }
                break;
            case DIGIT1 :case NUMPAD1:
                if (ifLegalMove(r + 1, c - 1))
                    if (ifLegalMove(r, c - 1) || ifLegalMove(r + 1, c)) {
                        this.row++;
                        this.col--;
                    }
                break;
            case DIGIT9 :case NUMPAD9:
                if (ifLegalMove(r - 1, c + 1))
                    if (ifLegalMove(r - 1, c) || ifLegalMove(r, c + 1)) {
                        this.row--;
                        this.col++;
                    }
                break;
            case DIGIT7 :case NUMPAD7:
                if (ifLegalMove(r - 1, c - 1))
                    if (ifLegalMove(r, c - 1) || ifLegalMove(r - 1, c)) {
                        this.row--;
                        this.col--;
                    }
                break;
        }
        if (endPosition.getColumnIndex() == getPositionCol() && endPosition.getRowIndex() == getPositionRow())
            gameIsOver = true;
        setChanged();
        notifyObservers();
    }

    public int[][] solve(MyViewModel m, int charRow, int charCol, String x){
        Server serverSolveMaze;
        serverSolveMaze = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        serverSolveMaze.start();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        Maze maze = originMaze;
                        toServer.writeObject(maze);
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject();
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        int sizeOfSolution = mazeSolutionSteps.size();
                        sol = new int[2][sizeOfSolution];
                        if(x.equals("solve")) {
                            for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                                sol[0][i] = ((MazeState)(mazeSolutionSteps.get(i))).getPos().getRowIndex();
                                sol[1][i] = ((MazeState)(mazeSolutionSteps.get(i))).getPos().getColumnIndex();
                            }
                        }
                        else if (x.equals("clue")){
                            int i = 1;
                            sol[0][i] = ((MazeState) (mazeSolutionSteps.get(i))).getPos().getRowIndex();
                            sol[1][i] = ((MazeState) (mazeSolutionSteps.get(i))).getPos().getColumnIndex();
                        }
                        setChanged();
                        notifyObservers();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverSolveMaze.stop();
        return sol;
    }

    public void save(File file){
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            this.solved = false;
            objectOutputStream.writeObject(this.originMaze);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException ignored) {

        }
    }

    public boolean gameOver(){
        return this.gameIsOver;
    }

    public boolean isSolved(){
        return this.solved;
    }
    /**
     * load file and create maze according the file
     */
    public void load(File file){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ObjectInputStream outputStream = new ObjectInputStream(inputStream);
            Maze temp = (Maze)outputStream.readObject();

            setOriginMaze(temp);
            setPositionRow(temp.getStartPosition().getRowIndex());
            setPositionCol(temp.getStartPosition().getColumnIndex());
            setGoalPosition(temp.getGoalPosition());
            this.solved =false;
            setChanged();
            notifyObservers();

            outputStream.close();

        } catch (IOException ignored) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter
     * @return original maze
     */
    public Maze getOriginMaze(){
        return  this.originMaze;
    }
    /**
     * getter
     * @return correct maze
     */
    public int[][] getMaze(){
        return this.maze;
    }
    /**
     * getter
     * @return the final solution maze in array
     */
    public int[][] getSolutionArray(){
        return  this.sol;
    }
    /**
     * getter
     * @return row position of character
     */
    public int getPositionRow(){
        return this.row;
    }
    /**
     * getter
     * @return col position of character
     */
    public int getPositionCol(){
        return  this.col;
    }
    /**
     * getter
     * @return end position of character
     */
    public Position getEndPosition(){
        return this.endPosition;
    }


    /**
     * setter
     */
    public void setOriginMaze(Maze m){
        int row = m.getRows();
        int col = m.getCols();
        this.originMaze = new Maze(row,col,m.getStartPosition(),m.getGoalPosition());
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                originMaze.getMaze()[i][j] = m.getMaze()[i][j];
    }
    /**
     * setter
     */
    public void setMaze(int[][] maze){
        this.maze=maze;
    }
    /**
     * setter
     */
    public void setPositionCol(int col){
        this.col=col;
    }
    /**
     * setter
     */
    public void setPositionRow(int row){
        this.row=row;
    }
    /**
     * setter
     */
    public void setGoalPosition(Position goalPosition){
        this.endPosition=goalPosition;
    }
    /**
     * do deep copy to maze2
     */
    private void deepCopyMaze(Maze m) {
        int row = m.getRows();
        int col = m.getCols();
        maze = new int[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                maze[i][j] = m.getMaze()[i][j];
    }
    private boolean ifLegalMove(int x, int y) {
        return x >= 0 && y >= 0 && x <= maze.length - 1 && y <= maze[0].length - 1 && (this.maze[x][y] != 1);
    }
}