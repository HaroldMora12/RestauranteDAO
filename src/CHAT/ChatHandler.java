package CHAT;


import java.io.*;
import java.net.*;

public class ChatHandler implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nombre;

    public ChatHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            nombre = in.readUTF();

            Server.broadcast(nombre + " se unió al chat");

            String mensaje;

            while ((mensaje = in.readUTF()) != null) {
                Server.broadcast(nombre + ": " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("Cliente desconectado");
        } finally {
            Server.removerCliente(this);
        }
    }

    public void enviarMensaje(String mensaje) {
        try {
            out.writeUTF(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
