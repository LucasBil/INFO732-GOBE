package polytech.idu.views;

import polytech.idu.controllers.UserController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserController controller;

    public LoginView() {
        this.controller = new UserController();
        initUI();
    }

    private void initUI() {
        setTitle("Login / Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; usernameField = new JTextField(15); panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; passwordField = new JPasswordField(15); panel.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttons.add(loginButton);
        buttons.add(registerButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(buttons, gbc);

        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> performRegister());

        add(panel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (controller.login(username, password)) {
            dispose(); // Close login window
            new MainWindow().setVisible(true); // Open main app
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (controller.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
