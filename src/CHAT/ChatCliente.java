package CHAT;

import java.io.*;
import java.net.*;

public class ChatCliente {

    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 5000;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void conectar(String nombre) throws IOException {

        socket = new Socket(HOST, PUERTO);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF(nombre);
    }

    public void enviar(String mensaje) throws IOException {
        out.writeUTF(mensaje);
    }

    public String recibir() throws IOException {
        return in.readUTF();
    }

    public void cerrar() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}