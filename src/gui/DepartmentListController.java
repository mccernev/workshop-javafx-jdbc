package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	// Vamos criar referências para nossos componentes da tela

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNew;

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		// TODO Auto-generated method stub

	}

	private void initializeNodes() {
		// Vamos colocar um comando para iniciar adequadamente o
		// comportamento das colunas da minha tabela:
		// Esse é um padrão do JavaFX para iniciar o comportamento das colunas.
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Para que minha TableView acompanhe a altura e a largura da janela:
		// Vou pegar uma referência para o Stage
		// Como? Vou acessar a classe principal, chamar o getMainScene (a cena)
		// Aí vou chamar o get.window (que pega a referência para a janela).
		// Esse Windows é uma superclasse do Stage.
		// Para que eu possa atribuir para o Stage vou precisar fazer um downcasting:

		Stage stage = (Stage) Main.getMainScene().getWindow();

		// Vamos dar um comando para que minha TableView acompanhe a janela;
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
