package ui;

import db.LoginManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.views.LoginView;

public class Managing_Application extends Application {
    private static Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void start(Stage primaryStage) {
//        SessionFactoryManager sessionFactoryManager = new SessionFactoryManager("admin","admin");
//        sessionFactoryManager.buildSessionFactory();
//        Session session = sessionFactoryManager.getNewSession();
//        session.beginTransaction();
//        UsersEntity usersEntity = new UsersEntity();
//        usersEntity.setAccessLevel(AccessLevel.storekeeper);
//        usersEntity.setUserId("storekeeper");
//        String hashed = BCrypt.hashpw("storekeeper", BCrypt.gensalt());
//        usersEntity.setPassword(hashed);
//        session.save(usersEntity);
//        usersEntity = new UsersEntity();
//        usersEntity.setAccessLevel(AccessLevel.storage_manager);
//        usersEntity.setUserId("storage_manager");
//        hashed = BCrypt.hashpw("storage_manager", BCrypt.gensalt());
//        usersEntity.setPassword(hashed);        session.save(usersEntity);
//        usersEntity = new UsersEntity();
//        usersEntity.setAccessLevel(AccessLevel.manager);
//        usersEntity.setUserId("manager");
//        hashed = BCrypt.hashpw("manager", BCrypt.gensalt());
//        usersEntity.setPassword(hashed);
//        session.save(usersEntity);
//        usersEntity = new UsersEntity();
//        usersEntity.setAccessLevel(AccessLevel.administrator);
//        usersEntity.setUserId("administrator");
//        hashed = BCrypt.hashpw("administrator", BCrypt.gensalt());
//        usersEntity.setPassword(hashed);
//        session.save(usersEntity);
//        session.getTransaction().commit();
//        session.close();
//        sessionFactoryManager.closeFactory();
//
        stage = primaryStage;
        primaryStage.getIcons().add(new Image(ui.Managing_Application.class.getResourceAsStream("../graphics/icon.png")));
        LoginView.initialize();
        stage.show();
        stage.setOnCloseRequest(e -> LoginManager.clean());
        //stage.setOnCloseRequest(e -> System.exit(0)); //TODO: zrobić to po ludzku
    }

    @Override
    public void stop() {
        LoginManager.clean();
    }

    public static Stage getStage() {
        return stage;
    }
}