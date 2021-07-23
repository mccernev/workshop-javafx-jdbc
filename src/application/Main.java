package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			// Estamos instanciando um objeto do tipo FXMLLoader
			// Na instanciação estamos passando o caminho da Vies.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			// Parent parent = loader.load();
			ScrollPane scrollPane = loader.load();
			// Macete para deixar o scrollPane ajustado à janela:
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			// Scene mainScene = new Scene(parent);
			Scene mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
