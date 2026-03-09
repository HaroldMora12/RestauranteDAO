package vista;

import Api.Clima;
import CHAT.ChatUI;
import CHAT.Server;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private JPanel panelPrincipal;
    private JLabel lblClima;

    public MainMenu() {

        setTitle("Sistema de Gestión");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔥 INICIAR SERVIDOR AUTOMÁTICAMENTE
        Server.iniciarServidor();

        // ================= PANEL SUPERIOR (CLIMA) =================
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTop.setBackground(Color.WHITE);

        lblClima = new JLabel("Cargando clima...");
        lblClima.setFont(new Font("Arial", Font.BOLD, 14));

        panelTop.add(lblClima);
        add(panelTop, BorderLayout.NORTH);

        // ================= PANEL LATERAL =================
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(7,1,5,5));
        panelMenu.setPreferredSize(new Dimension(200, 0));
        panelMenu.setBackground(new Color(33,37,41));

        JButton btnClientes = new JButton("Clientes");
        JButton btnProductos = new JButton("Productos");
        JButton btnOrdenes = new JButton("Nueva Orden");
        JButton btnOrdenesGuardadas = new JButton("Órdenes Guardadas");
        JButton btnChat = new JButton("Chat Interno");

        estilizarBoton(btnClientes);
        estilizarBoton(btnProductos);
        estilizarBoton(btnOrdenes);
        estilizarBoton(btnOrdenesGuardadas);
        estilizarBoton(btnChat);

        panelMenu.add(btnClientes);
        panelMenu.add(btnProductos);
        panelMenu.add(btnOrdenes);
        panelMenu.add(btnOrdenesGuardadas);
        panelMenu.add(btnChat);

        add(panelMenu, BorderLayout.WEST);

        // ================= PANEL PRINCIPAL =================
        panelPrincipal = new PanelConImagen();
        panelPrincipal.setLayout(new BorderLayout());

        add(panelPrincipal, BorderLayout.CENTER);

        // ================= EVENTOS =================

        btnClientes.addActionListener(e -> {
            panelPrincipal.removeAll();
            panelPrincipal.add(new ClientesPanel(), BorderLayout.CENTER);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
        });

        btnProductos.addActionListener(e -> {
            panelPrincipal.removeAll();
            panelPrincipal.add(new ProductosPanel(), BorderLayout.CENTER);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
        });

        btnOrdenes.addActionListener(e -> {
            panelPrincipal.removeAll();
            panelPrincipal.add(new OrdenesPanel(), BorderLayout.CENTER);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
        });

        btnOrdenesGuardadas.addActionListener(e -> {
            panelPrincipal.removeAll();
            panelPrincipal.add(new OrdenesGuardadasPanel(), BorderLayout.CENTER);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
        });

        // ================= EVENTO CHAT =================
        btnChat.addActionListener(e -> {

            String nombreUsuario = JOptionPane.showInputDialog(
                    this,
                    "Ingresa tu nombre:",
                    "Chat Interno",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
                new ChatUI(nombreUsuario).setVisible(true);
            }
        });

        // ================= CARGAR CLIMA =================
        cargarClima();
    }

    private void cargarClima() {
        new Thread(() -> {
            String clima = Clima.obtenerClima();

            SwingUtilities.invokeLater(() -> {
                lblClima.setText(clima);
            });
        }).start();
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(new Color(0,123,255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}