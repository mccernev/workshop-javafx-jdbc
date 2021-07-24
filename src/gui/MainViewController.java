package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeler;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}


	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");

	}

	@FXML
	public void onMenuItemAboutAction() {
		// Vamos fazer chamar a view da minha tela About):
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	// Função para abrir uma outra tela;
	// Estamos usando absoluteName porque vamos passar o caminho completo
	// (no caso aqui vai ser gui/about.xml).

	// Para garantir que todo esse código seja executado sem ser interrompido
	// porque as aplicações gráficas são multitrading, você pode acrescentar, antes
	// do void,
	// o termo "synchronized"
	// Assim você garante que esse processamento todo não vai ser interrompido
	// durante o
	// multitrading.

	private synchronized void loadView(String absoluteName) {
		try {
			// Para carregar uma tela temos que usar aquele objeto tipo FXMLLoader
			// Vamos instanciar um objeto do tipo FXMLLoader;
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

			// Meu objeto VBox vai carregar essa view:
			VBox newVBox = loader.load();

			// Vamos fazer um código para mostrar essa view dentro da janela principal;
			// Para ter condição de trabalhar com a janela principal vou ter que pegar uma
			// referência da cena.
			// Essa cena em que a janela principal está, foi declarada na classe principal
			// (Main.java).

			// Aqui no controlador vou pegar aquela referẽncia
			// da cena que está na classe Main.java:
			Scene mainScene = Main.getMainScene();
			// Precisamos da referência da cena porque vou ter que pegar uma referẽncia do
			// VBox da janela principal,
			// lá no meu MainView.fxml e eu vou precisar acrescentar nos children desse
			// VBox,
			// os children do VBox da janela About.
			// Ou seja, eu vou precisar acrescentar os filhos da janela About na janela
			// principal
			// Fazer uma espécie de "substituição" dos elementos presentes numa janela pelos
			// elementos
			// que vem da outra janela.
			// Por isso eu preciso de uma referẽncia do VBox dessa janela principal.
			// Este será meu ponto de partida.
			// Para fazer isso, vou criar uma variável do tipo VBox.
			// Vou pegar minha cena principal e chamar a partir dela,
			// o método .getRoot (ele vai pegar o primeiro elemento da minha View
			// que é o ScrollPane. Então vou precisar fazer o casting desse getRoot para
			// ScrollPane.
			// Dentro do ScrollPane eu tenho o elemento "content", para acessa-lo tenho que
			// acrescentar .getContent();
			// Ou seja ele é uma referência para o meu VBox.
			// Então agora vou pegar toda essa expressão e fazer um casting para VBox.
			// O código vai ficar assim:
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			// O que será necessário fazer em relação aos elementos:
			// Tenho que preservar o menuBar, ele sempre estará presente na minnha
			// aplicação.
			// Vou ter que excluir tudo o que estiver nos filhos da página prncipal;
			// Vou ter que incluir esse menuBar, e em seguida, os filhos do VBox da tela
			// About.
			// Para fazer isto:
			// Vou guardar uma referẽncia para o menu:
			// Vai receber o primeiro filho do VBox da janela principal , ou seja o menuBar.
			Node mainMenu = mainVBox.getChildren().get(0);
			// Vou limpar os filhos da minha tela principal:
			mainVBox.getChildren().clear();
			// Agora vou adicionar nesses filhos o mainMenu, e depois os filhos do NewVBox;
			// O código vai ficar assim:
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			// Vamos tratar essa exceção mostrando um Alert na tela;
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	private synchronized void loadView2(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);

			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			// Estou usando o mesmo para acessar também o controller. 
			
			DepartmentListController controller = loader.getController();
			
			// Pegando a referência para essa View eu chamar aquelas operações:
			
			// Primeiro vamos injetar a dependência do service para o controller:			
			controller.setDepartmentService(new DepartmentService());
			
			// Agora que o controller já tem a dependência, 
			// Vamos chamar o: 
			controller.updateTableView();
			
			// Então assim estamos fazendo um processo manual de injetar dependência
			// lá no meu controller e depois de chamar para atualizar os dados
			// na tela da minha tableView.
			
			
			
			
			
			

		} catch (IOException e) {

			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
