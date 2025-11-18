package br.com.vinidiefen.pong.services;

import java.util.List;
import java.util.UUID;

import br.com.vinidiefen.pong.components.Ball;
import br.com.vinidiefen.pong.components.Paddle;
import br.com.vinidiefen.pong.components.ScoreManager;
import br.com.vinidiefen.pong.constants.GameConstants;
import br.com.vinidiefen.pong.models.BallModel;
import br.com.vinidiefen.pong.models.MatchModel;
import br.com.vinidiefen.pong.models.PaddleModel;
import br.com.vinidiefen.pong.models.ScoreManagerModel;
import br.com.vinidiefen.pong.repositories.CRUDRepository;

/**
 * Service class responsible for saving and loading game state
 * Centralizes repository management and reduces code duplication
 */
public class GameStateService {
    
    private final CRUDRepository<PaddleModel> paddleRepo;
    private final CRUDRepository<BallModel> ballRepo;
    private final CRUDRepository<ScoreManagerModel> scoreRepo;
    private final CRUDRepository<MatchModel> matchRepo;
    
    public GameStateService() {
        this.paddleRepo = CRUDRepository.of(PaddleModel.class);
        this.ballRepo = CRUDRepository.of(BallModel.class);
        this.scoreRepo = CRUDRepository.of(ScoreManagerModel.class);
        this.matchRepo = CRUDRepository.of(MatchModel.class);
    }
    
    /**
     * Saves the current game state to the database
     */
    public UUID saveGameState(Paddle leftPaddle, Paddle rightPaddle, Ball ball, 
                              ScoreManager scoreManager) {
        // Create model instances using factory methods
        PaddleModel leftPaddleModel = PaddleModel.from(leftPaddle);
        PaddleModel rightPaddleModel = PaddleModel.from(rightPaddle);
        BallModel ballModel = BallModel.from(ball);
        ScoreManagerModel scoreManagerModel = ScoreManagerModel.from(scoreManager, GameConstants.WINNING_SCORE);
        
        // Save entities to database
        paddleRepo.create(leftPaddleModel);
        paddleRepo.create(rightPaddleModel);
        ballRepo.create(ballModel);
        scoreRepo.create(scoreManagerModel);
        
        // Create and save match
        MatchModel matchModel = new MatchModel(leftPaddleModel, rightPaddleModel, 
                                               ballModel, scoreManagerModel);
        matchRepo.create(matchModel);
        
        return matchModel.getId();
    }
    
    /**
     * Loads a specific game state by match ID
     */
    public LoadedGameState loadGameState(UUID matchId) {
        // Load the specific match
        MatchModel match = matchRepo.read(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found: " + matchId);
        }
        
        // Load related entities
        PaddleModel leftPaddleModel = paddleRepo.read(match.getLeftPaddleId());
        PaddleModel rightPaddleModel = paddleRepo.read(match.getRightPaddleId());
        BallModel ballModel = ballRepo.read(match.getBallId());
        ScoreManagerModel scoreManagerModel = scoreRepo.read(match.getScoreManagerId());
        
        return new LoadedGameState(leftPaddleModel, rightPaddleModel, 
                                  ballModel, scoreManagerModel);
    }
    
    /**
     * Gets all saved matches
     */
    public List<MatchModel> getAllMatches() {
        return matchRepo.findAll();
    }
    
    /**
     * Gets score information for a match
     */
    public ScoreManagerModel getScoreForMatch(UUID scoreManagerId) {
        return scoreRepo.read(scoreManagerId);
    }
    
    /**
     * Container class for loaded game state
     */
    public static class LoadedGameState {
        private final PaddleModel leftPaddle;
        private final PaddleModel rightPaddle;
        private final BallModel ball;
        private final ScoreManagerModel scoreManager;
        
        public LoadedGameState(PaddleModel leftPaddle, PaddleModel rightPaddle,
                              BallModel ball, ScoreManagerModel scoreManager) {
            this.leftPaddle = leftPaddle;
            this.rightPaddle = rightPaddle;
            this.ball = ball;
            this.scoreManager = scoreManager;
        }
        
        public PaddleModel getLeftPaddle() { return leftPaddle; }
        public PaddleModel getRightPaddle() { return rightPaddle; }
        public BallModel getBall() { return ball; }
        public ScoreManagerModel getScoreManager() { return scoreManager; }
    }
}
