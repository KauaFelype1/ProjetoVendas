package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/view/login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/login.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Kauã");
            stage.centerOnScreen();
            stage.setMaximized(true);
            stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
