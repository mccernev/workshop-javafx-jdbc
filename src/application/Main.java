package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

	// Agora vou expor uma referência para a cena
	// Para isso vou criar um atributo para poder
	// guardar a referência à cena nesse atributo: 

	private static Scene mainScene;
	
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
			// Declaração de uma variável da cena:
			// Scene mainScene = new Scene(parent);
			
			// Agora, ao inves de declarar, vou simplesmente 
			// referenciar o atributo criado lá em cima;
			//Scene mainScene = new Scene(scrollPane);
			mainScene = new Scene(scrollPane);

			// A cena foi criada e colocada dentro do primaryStage:
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	// Como o atributo mainScene é um atributo privado
	// Essa é uma boa prática.
	// Vou criar um método para pegar essa referẽncfia:
	// Esse método vai retornar o meu objeto mainScene:
	public static Scene getMainScene(){
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}