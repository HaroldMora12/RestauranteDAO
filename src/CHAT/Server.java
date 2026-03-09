package CHAT;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PUERTO = 5000;
    private static Set<ChatHandler> clientes = new HashSet<>();
    private static boolean corriendo = false;

    public static void iniciarServidor() {

        if (corriendo) return; // evita iniciarlo dos veces

        corriendo = true;

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {

                System.out.println("Servidor de chat iniciado...");

                while (corriendo) {
                    Socket socket = serverSocket.accept();
                    ChatHandler handler = new ChatHandler(socket);
                    clientes.add(handler);
                    new Thread(handler).start();
                }

            } catch (IOException e) {
                System.out.println("Error en servidor: " + e.getMessage());
            }
        }).start();
    }

    public static void broadcast(String mensaje) {
        for (ChatHandler cliente : clientes) {
            cliente.enviarMensaje(mensaje);
        }
    }

    public static void removerCliente(ChatHandler handler) {
        clientes.remove(handler);
    }
}

