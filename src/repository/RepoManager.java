package repository;

import database.DBConnection;
import java.sql.Connection;

public class RepoManager {

    private static RepoManager instance;

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final ActivityLogRepository activityLogRepository;

    public RepoManager() {
        Connection conn = DBConnection.getConnection();
        this.userRepository = new UserRepository(conn);
        this.feedbackRepository = new FeedbackRepository(conn);
        this.activityLogRepository = new ActivityLogRepository(conn);
    }

    public static RepoManager getInstance() {
        if (instance == null) {
            instance = new RepoManager();
        }
        return instance;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public FeedbackRepository getFeedbackRepository() {
        return feedbackRepository;
    }

    public ActivityLogRepository getActivityLogRepository() {
        return activityLogRepository;
    }
}
