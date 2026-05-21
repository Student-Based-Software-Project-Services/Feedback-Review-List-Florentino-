package util;

import java.sql.Connection;
import dao.AdminDAO;
import dao.RespondentDAO;
import dao.SurveyResponseDAO;

public class DAOManager {
    
    private static DAOManager instance;
    
    private final AdminDAO adminDAO;
    private final RespondentDAO respondentDAO;
    private final SurveyResponseDAO surveyResponseDAO;
    
    private DAOManager() {
        Connection conn = DBConnection.getConnection();
        this.adminDAO = new AdminDAO(conn);
        this.respondentDAO = new RespondentDAO(conn);
        this.surveyResponseDAO = new SurveyResponseDAO(conn);
    }
    
    public static DAOManager getInstance() {
        if (instance == null) {
            return instance = new DAOManager();
        }
        return instance;
    }

    public AdminDAO getAdminDAO() {
        return adminDAO;
    }

    public RespondentDAO getRespondentDAO() {
        return respondentDAO;
    }

    public SurveyResponseDAO getSurveyResponseDAO() {
        return surveyResponseDAO;
    }
}
