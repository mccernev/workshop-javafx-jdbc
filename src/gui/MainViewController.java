package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeler;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});


	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
		// Para isso aqui funcionar no loadView eu vou ter que acrescentar
		// a declaração desse parâmetro lá na função (seja preenchido como aqui)
		// seja vazio como no caso do doadView do método onMeunuItemAboutAction, abaixo.
	}

	@FXML
	public void onMenuItemAboutAction() {
		// Vamos fazer chamar a view da minha tela About):
		loadView("/gui/About.fxml", x -> {
		});
		// Como acrescentamos um parâmetro ao loadView, temos que colocá-lo
		// aqui também, neste caso ele vai ficar vazio.
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

	// Para poder declarar o tipo de parâmetro vamos usar a interface
	// funcional Consumer:

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
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

			// Depois de carregar a janela
			// Eu vou acrescentar um comandinho especial para ativar a função que vocẽ
			// passar lá no initializangAction.
			// O meu objeto loader tem a função .getController().
			// Essa função agora está retornando um Controller do tipo <T> que é o que
			// usamos para parametrizar a nossa função.
			// Então, vamos chamar o get.Controller
			// E o resultado desse getController eu vou atribuir para uma variável do tipo
			// <T>.

			T controller = loader.getController();

			// Então agora, o meu getController vai retornar o controlador do tipo que
			// eu chamar lá em cima do código (neste caso, o "DepartmentListController"
			// Se fosse outro caso, retornaria outro controlador que estivesse especificado.

			// Como que eu faço para executar a função do meu Consumer?
			// Eu tenho que chamar a função acept do meu consumer.

			initializingAction.accept(controller);

			// Ou seja essas duas últimas linhas de código vão executar
			// a função que vocẽ passar como argumento lá em cima!

		} catch (IOException e) {
			// Vamos tratar essa exceção mostrando um Alert na tela;
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
