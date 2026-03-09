package CHAT;

import javax.swing.*;
import java.awt.*;

public class ChatUI extends JFrame {

    private JTextArea area;
    private JTextField campo;
    private ChatCliente cliente;
    private String miNombre;

    public ChatUI(String nombreUsuario) {

        this.miNombre = nombreUsuario;

        setTitle("Chat Interno");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cliente = new ChatCliente();

        try {
            cliente.conectar(nombreUsuario);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar");
        }

        area = new JTextArea();
        area.setEditable(false);

        campo = new JTextField();
        JButton enviar = new JButton("Enviar");

        setLayout(new BorderLayout());
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(campo, BorderLayout.CENTER);
        panelSur.add(enviar, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        // 🔥 Método centralizado para enviar
        Runnable enviarMensaje = () -> {
            try {
                String mensaje = campo.getText().trim();
                if (!mensaje.isEmpty()) {
                    cliente.enviar(mensaje);
                    campo.setText("");
                }
            } catch (Exception ex) {
                area.append("Error al enviar\n");
            }
        };

        enviar.addActionListener(e -> enviarMensaje.run());
        campo.addActionListener(e -> enviarMensaje.run());

        // 🔥 Hilo receptor mejorado
        new Thread(() -> {
            try {
                String msg;
                while ((msg = cliente.recibir()) != null) {

                    String mensajeProcesado;

                    if (msg.startsWith(miNombre + ":")) {
                        mensajeProcesado = msg.replaceFirst(miNombre + ":", "Yo:");
                    } else {
                        mensajeProcesado = msg;
                    }

                    final String mensajeFinal = mensajeProcesado;

                    SwingUtilities.invokeLater(() -> {
                        area.append(mensajeFinal + "\n");
                    });
                }

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    area.append("Conexión cerrada\n");
                });
            }
        }).start();
    }
}
