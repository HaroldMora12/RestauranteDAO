package vista;

import javax.swing.*;
import java.awt.*;

public class PanelConImagen extends JPanel {

    private Image imagen;   // 👈 ESTA LÍNEA ES OBLIGATORIA

    public PanelConImagen() {

        java.net.URL url = getClass().getResource("/recursos/fondo.png");
        System.out.println("URL encontrada: " + url);

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            imagen = icon.getImage();
            System.out.println("Imagen cargada correctamente");
        } else {
            System.out.println("No se encontró la imagen");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
