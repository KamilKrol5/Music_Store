package db;

import model.AccessLevel;
import model.entities.UsersEntity;
import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCrypt;
import sh.LoadAlbums;

import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("Duplicates")
public class LoginManager {
    private static Session currentSession;
    private static SessionFactoryManager currentSessionFactoryManager;
    private static AccessLevel accessLevel;
    private static String activeUsername;

    public static void connectUserCheck() {
        currentSessionFactoryManager = new SessionFactoryManager("login", "login");
        currentSessionFactoryManager.buildSessionFactory();
        currentSession = currentSessionFactoryManager.getNewSession();
//        LoadAlbums.loadAlbums();
    }

    /**
     * Function returns access level for given username and password. If user does not exist returns null.
     * @param username username
     * @param password password
     * @return access level.
     */
    public static boolean checkUser(String username, String password) {
        TypedQuery<UsersEntity> query = currentSession.
                createQuery("from UsersEntity where userId like (:username)", UsersEntity.class)
                .setParameter("username", username);
        List<UsersEntity> usersEntityList = query.getResultList();
        if (usersEntityList.size()>0) {
            if (BCrypt.checkpw(password, usersEntityList.get(0).getPassword())) {
//            if(password.equals(usersEntityList.get(0).getPassword())) {
                accessLevel = usersEntityList.get(0).getAccessLevel();
                activeUsername = username;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Closes the currentSession if exists, and closes the session factory.
     */
    public static void clean() {
        if (currentSession !=null) currentSession.close();
        currentSessionFactoryManager.closeFactory();
    }

    public static AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Connect as current user. Used after correct log in.
     */
    public static void connectCurrentSession() {
        if (currentSession !=null) currentSession.close();
        switch (accessLevel) {
            case storekeeper: {
                currentSessionFactoryManager = new SessionFactoryManager("storagekeeper", "storagekeeper");
                currentSessionFactoryManager.buildSessionFactory();
                break;
            }
            case storage_manager: {
                currentSessionFactoryManager = new SessionFactoryManager("storagemanager", "storagemanager");
                currentSessionFactoryManager.buildSessionFactory();
                break;
            }
            case manager: {
                currentSessionFactoryManager = new SessionFactoryManager("manager", "manager");
                currentSessionFactoryManager.buildSessionFactory();
                break;
            }
            case administrator: {
                currentSessionFactoryManager = new SessionFactoryManager("admin", "admin");
                currentSessionFactoryManager.buildSessionFactory();
            }
        }
        currentSession = currentSessionFactoryManager.getNewSession();
    }

    public static Session getSession() {
        return currentSessionFactoryManager.getNewSession();
    }

    public static String getUsername() {
        return activeUsername;
    }
}
