package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

// Vamos implementartambém a interface DataChangeListener:

public class DepartmentListController implements Initializable, DataChangeListener {

	// Vamos criar uma dependência para o nosso serviço:

	private DepartmentService service;

	// nós não vamos colocar aqui um
	// service = new DepartmentService();
	// porque se fizessemos assim estaríamos gerando um acoplamento forte.
	// ao inves disso vamos fazer uma injeção de dependência.

	// Para isso vamos fazer um metodo setDepartment:

	// Vamos criar referências para nossos componentes da tela

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;
	// Vamos precisar carregar os departments nessa obsList
	// Para isto vamos criar o método updateTableView.

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	// Aqui para nossa injeção de dependência;
	// Temos nossa inversão de controle (Princípio Solid).

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	// Nós vamos precisar pegar esse service aqui,
	// carregar os nossos departments,
	// e mostrar dentro do meu tableView.
	// Para isto vamos declarar os nossos atributos usando o Observablelist

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
	// Método responsável por:
	// Acessar o serviço
	// Carregar os departments
	// e jogar os departments na minha observableList.

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
		// Isso vai acrescentar um botão com o texto "edit" em cada linha
		// da minha grid.
		initEditButtons();
		// como este é um método, vamos alterar provisoriamente
		// o loadView para chamá-lo e carrega-lo antes de carregar a tela
		// lá no mainViewController.
	}

	// Esse método vai receber como parâmetro um stage
	// da janelinha que criou a janelinha de diálogo:

	// Por que?
	// Porque quando a gente cria uma janela de diálogo a gente tem que
	// informar para ela quem que é o Stage que criou a janelinha de
	// diálogo.

	// Então já vamos informar aqui:

	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// Vamos fazer uma referencia para o controladro:
			DepartmentFormController controller = loader.getController();

			// Vamos injetar nesse controlador, o departamento:
			controller.setDepartment(obj);

			// Vamos injetar o serviço:
			controller.setDepartmentService(new DepartmentService());

			// Vamos nos inscrever para escutar o evento do onDataChanged:
			controller.subscribeDataChangeListener(this);

			// Vamos chamar o método para carregar os dados do objeto:
			controller.updateFormData();

			// Quando vou carregar uma janelinha modal
			// na frente de outra janela.
			// Eu tenho que instanciar um outro Stage:

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			// Aqui eu defino quem é o
			// Stage pai dessa janela:
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

}
