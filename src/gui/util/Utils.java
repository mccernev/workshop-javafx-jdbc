package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	// Esse método vai receber o evento de um controller
	// Ele vai retornar o Stage onde o controller 
	// que recebeu o evento está.
	
	// O nome dela será CurrentStage, ou seja PalcoAtual.
	
	public static Stage currentStage(ActionEvent event) {
	// Como o event é um tipo genérico
	// E vamos precisar de um tipo Node
	// Vamos fazer um downcasting para Node.
	// A partir desse Node eu vou chamar o método getScene.
	// A partir da Scene eu vou usa o método getWindows para pegar a janela
	// Como a Windows é uma superclasse do Stage
	// Eu vou fazer um downcasting do Estage:
			
		
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
}
