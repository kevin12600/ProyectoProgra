package Controlador;

import BD.ConexionBD;
    import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class IniciarSesionController implements Initializable {

    private TextField correoTextField;

    private PasswordField contraseniaPasswordField;

    private Button iniciarSesionButton;

    private Connection connection;
    @FXML
    private TextField correoField;
    @FXML
    private TextField contraseniaField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Establecer el evento de clic del botón "Iniciar Sesión"
        iniciarSesionButton.setOnAction(event -> iniciarSesion());
    }

    private void iniciarSesion() {
        String correo = correoTextField.getText();
        String contrasenia = contraseniaPasswordField.getText();

        // Verificar si los campos están vacíos
        if (correo.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Vacíos", "Por favor, ingrese el correo y la contraseña.");
            return;
        }

        // Crear una conexión a la base de datos PostgreSQL
        connection = ConexionBD.getConnection();
        if (connection == null) {
            mostrarAlerta(AlertType.ERROR, "Error de Conexión", "No se pudo establecer la conexión a la base de datos.");
            return;
        }

        // Consultar el usuario en la base de datos
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasenia = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correo);
            statement.setString(2, contrasenia);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Usuario válido, mostrar mensaje de éxito y realizar las acciones necesarias
                mostrarAlerta(AlertType.INFORMATION, "Inicio de Sesión Exitoso", "¡Bienvenido, " + correo + "!");
                // Realizar las acciones necesarias después del inicio de sesión exitoso
            } else {
                // Usuario inválido, mostrar mensaje de error
                mostrarAlerta(AlertType.ERROR, "Inicio de Sesión Fallido", "El correo o la contraseña son incorrectos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error al iniciar sesión.");
        }
    }

    private void mostrarAlerta(AlertType alertType, String titulo, String mensaje) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
