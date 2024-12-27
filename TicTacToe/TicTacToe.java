import java.lang.Math;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.UUID;



public class TicTacToe {
    public static void main(String[] args) {
        System.out.println("Starting Game...");

        User humanUser = new User("John Don ", "abc@gmail.com", UserType.HUMAN);

        GameController gameController = new GameController(3, humanUser);
        gameController.startGame();

        System.out.println("Game End...");
    }
    
}

class GameController {
    static final int NUMBER_OF_USERS = 2;
    private GameBoard gameBoard;
    private User humanPlayer;
    private User computer;
    private User firstPlayer;
    private User secondPlayer;
 
    public GameController(int gameBoardSize, User humanPlayer) {
        gameBoard = new GameBoard(gameBoardSize);
        this.humanPlayer = humanPlayer;
        this.computer = createUser();
    }

    public User createUser() {
        String computerUserDetail = UUID.randomUUID().toString();
        return new User(computerUserDetail, computerUserDetail, UserType.COMPUTER);
    }

    private void toss() {
        System.out.println("Toss Started...");
        int tossResult = getTossResult();
        if (tossResult == 0) {
            firstPlayer = humanPlayer;
            secondPlayer = computer;
        } else {
            firstPlayer = computer;
            secondPlayer = humanPlayer;
        }
        System.out.println("Player 1: " + firstPlayer);
        System.out.println("Player 2: " + secondPlayer);
        System.out.println("Toss Completed...");
    }

    private int getTossResult() {
        double randomNumber = Math.random()*NUMBER_OF_USERS;
        return (int)Math.floor(randomNumber);
    }

    public void startGame() {
        System.out.println("Game Started...");
        toss();
        int counter = 0;
        while(gameBoard.isMoveLeft()) {
            gameBoard.showGameBoard();
            if(counter%2==0) {
                move(firstPlayer);
            } else {
                move(secondPlayer);
            }
            if(gameBoard.checkWinningCondition()) {
                break;
            }
            counter++;
        }
        gameBoard.showGameBoard();
        declareWinner();
        System.out.println("Game Ended...");
    }

    private void move(User user) {
        switch (user.getUserType()) {
            case UserType.HUMAN:
                moveForHuman();
                break;
            case UserType.COMPUTER:
                moveForComputer();
                break;
        }
    }

    private void moveForHuman() {
        System.out.println("Human Turn...");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Provide the row and column where you want to draw.");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            try {
                gameBoard.setGameBoard(row, col, getHumanPlayerSymbol());
                System.out.println("Human Turn Completed...");
                return;
            } catch  (Exception exception){
                System.out.println(exception);
            }
        }
    }

    private void moveForComputer() {
        System.out.println("Computer Turn...");
        int position = getRandomEmptyPositionOnBoard();
        try {
            gameBoard.fillEmptyPositionFromStart(position, getComputerPlayerSymbol());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Computer Turn Completed ...");
    }

    private int getRandomEmptyPositionOnBoard() {
        return (int)Math.floor(Math.random()*gameBoard.getTotalEmptyBlock());
    }

    private Symbol getHumanPlayerSymbol() {
        if(firstPlayer.getUserType().equals(UserType.HUMAN)) {
            return Symbol.CROSS;
        }
        return Symbol.ZERO;
    }

    private Symbol getComputerPlayerSymbol() {
        if(firstPlayer.getUserType().equals(UserType.COMPUTER)) {
            return Symbol.CROSS;
        }
        return Symbol.ZERO;
    }

    private void declareWinner() {
        if(gameBoard.checkWinningCondition()) {
            if (gameBoard.getWinnerCharacter() == 'X') {
                System.out.println(firstPlayer + " Won.");
                return ;
            }
            System.out.println(secondPlayer + " Won.");
            return ;
        }
        System.out.println("----------Match Draw-----------");
    }



}

class User {
    private String name;
    private String id;
    private String email;
    private UserType userType;

    public User (String name, String email, UserType userType) {
        this.name = name;
        this.email = email;
        this.userType = userType;
        id = email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public UserType getUserTyppe() {
        return userType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "name : " + name + " userType: " + userType.name();
    }

}

enum UserType {
    HUMAN, COMPUTER;
}

enum Symbol {
    CROSS, ZERO;
}


class GameBoard {
    private int gameBoardSize;

    List<List<Character> >  gameBoard;

    private Character winnerCharacter;

    private int totalEmptyBlock;

    public GameBoard(int gameBoardSize) {
        this.gameBoardSize = gameBoardSize;
        this.gameBoard = new ArrayList<>();
        fillBoard();
        totalEmptyBlock = gameBoardSize * gameBoardSize;
    }

    private void fillBoard() {
        
        for(int row=0;row<gameBoardSize;row++) {
            List<Character> gameBoardRow = new ArrayList<>();
            for(int col=0;col<gameBoardSize;col++) {
                gameBoardRow.add('-');
            }
            this.gameBoard.add(gameBoardRow);
        }
    }

    public void showGameBoard() {
        System.out.printf("----------------------Printing Board---------------------%n");
        for(int row=0;row<gameBoardSize;row++) {
            for(int col=0;col<gameBoardSize;col++) {
                System.out.print(gameBoard.get(row).get(col) + " ");
            }
            System.out.printf("%n");
        }
        System.out.printf("----------------------Print Completed---------------------%n");
    }
    public void setGameBoard(int row, int col, Symbol symbol) throws Exception {
        if(isFilledBlock(row, col)) {
            throw new Exception("Provided row: "+ row +", col: " + col + " is already filled;");
        }
        System.out.println("Filling row: " + row + ", col: " + col);
        this.gameBoard.get(row).set(col, getChar(symbol)); 
        totalEmptyBlock--;
    }

    public Character getChar(Symbol symbol) {
        switch (symbol) {
            case Symbol.CROSS:
                return 'X';
            case Symbol.ZERO:
                return 'O';
            default: // will never occur
                return '-';
        }
    }

    public boolean checkWinningCondition() {
        if(validateWinningRow() || validateWinningCol() || validateWinningDiagonal()) {
            System.out.println("Winning Condition is true with winner character : " + winnerCharacter);
            return true;
        }
        return false;
    }

    private boolean validateWinningRow() {
        for(int row = 0; row<gameBoardSize;row++) {
            boolean isWinningRow = true;
            if(!isFilledBlock(row, 0)) {
                continue;
            }
            winnerCharacter = gameBoard.get(row).get(0);
            for(int col=1; col< gameBoardSize;col++) {
                if(!gameBoard.get(row).get(col-1).equals(gameBoard.get(row).get(col))) {
                    isWinningRow = !isWinningRow;
                    winnerCharacter = null;
                    break;
                }
            }
            if(isWinningRow)
                return isWinningRow;
        }
        return false;
        
    }


    private boolean validateWinningCol() {
        for(int col = 0; col<gameBoardSize;col++) {
            boolean isWinningCol = true;
            if(!isFilledBlock(0, col))
                continue;
            winnerCharacter = gameBoard.get(0).get(col);
            for(int row=1; row< gameBoardSize;row++) {
                if(!gameBoard.get(row-1).get(col).equals(gameBoard.get(row).get(col))) {
                    isWinningCol = !isWinningCol;
                    winnerCharacter = null;
                    break;
                }
            }
            if(isWinningCol) {
                return isWinningCol;
            }
        }

        return false;
    }

    private boolean validateWinningDiagonal() {
        return validateWinningDiagonalStartFromTopLeft() || validateWinningDiagonalStartFromTopRight();
    }

    private boolean validateWinningDiagonalStartFromTopLeft() {
        if(!isFilledBlock(0, 0)) {
            return false;
        }
        boolean isWinningDiagonal = true;
        winnerCharacter = gameBoard.get(0).get(0);
        for(int row=1; row< gameBoardSize;row++) {
            if(!gameBoard.get(row).get(row).equals(winnerCharacter)) {
                winnerCharacter = null;
                return !isWinningDiagonal;
            }
        }
        return isWinningDiagonal;
    }

    private boolean validateWinningDiagonalStartFromTopRight() {
        if(!isFilledBlock(0, gameBoardSize-1)) {
            return false;
        }
        boolean isWinningDiagonal = true;
        winnerCharacter = gameBoard.get(0).get(gameBoardSize-1);
        for(int row=1; row< gameBoardSize;row++) {
            if(!gameBoard.get(row).get(gameBoardSize-row-1).equals(winnerCharacter)) {
                winnerCharacter = null;
                return !isWinningDiagonal;
            }
        }
        return isWinningDiagonal;
    }

    private boolean isFilledBlock(int row, int col) {
        return gameBoard.get(row).get(0)=='X' || gameBoard.get(row).get(0) == 'O';
    }

    public Character getWinnerCharacter() {
        return winnerCharacter;
    }

    public boolean isMoveLeft() {
        return totalEmptyBlock>0;
    }

    public int getTotalEmptyBlock() {
        return totalEmptyBlock;
    }

    public void fillEmptyPositionFromStart(int position, Symbol symbol) throws Exception {
        System.out.println("Filling position :" + position);
        for(int row = 0; row<gameBoardSize;row++) {
            for(int col=0; col< gameBoardSize;col++) {
                if(!isFilledBlock(row, col)) {
                    if(position==0) {
                        try {
                            setGameBoard(row, col, symbol);
                        } catch (Exception e) {
                            e.printStackTrace();// won't occur
                        }
                        return;
                    }
                   position--;
                }
            }
        }
        throw new Exception("Error while fillEmptyPositionFromStart");
    }


}