package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	// Vamos criar uma dependência:
	private Department entity;

	// Vamos criar uma dependência:
	private DepartmentService service;

	// Vamos criar uma lista de objetos do tipo dataChangeListeners:
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// Agora o controlador vai permitir outros objetos se inscreverem nessa lista
	// e receberem o evento.

	// Para que eles possam se inscrever, temos que disponibilizar um método para
	// isto:
	// Vamos criar o método subscribeDataChangeListener.

	// Declaração dos componentes da tela:

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// Declaração dos métodos dos componentes:

	// Método para implementar a dependência:
	// Assim nosso controlador vai ter uma instância
	// do departament:

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
		// A partir de agora outros objetos, desde que implementem
		// essa interface, eles podem se inscrever para receber o evento da minha
		// classe.
	}

	// Como não estamos usando um container
	// um framework para fazer a injeção de dependência
	// estamos fazendo a injeção de dependência manual
	// Por isso estamos usando essa programação defensiva no início
	// do código do botão:

	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeLinsteners();
			Utils.currentStage(event).close();
		} 
		catch (ValidationException e){
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeLinsteners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Department getFormData() {
		// Esse método vai ler os dados do formulário
		Department obj = new Department();

		// Para poder fazer as verificações
		// Vamos instanciar uma exceção ValidationException
		// Mas não vamos lançar a exceção
		ValidationException exception = new ValidationException("Validation error");
		// Para cada campo vamos fazer as validações:

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	// Método responsável por pegar os dados do department
	// instanciado e popular as caixas de texto do formulário

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
