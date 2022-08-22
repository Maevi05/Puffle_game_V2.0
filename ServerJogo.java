import java.io.IOException;
import jva.net.ServerSocket;
import java.net.Socket;

public class ServerJogo {

    ServerJogo() {
        ServerSocket serverSocket;

        serverSocket = iniciaServer(12345);

        while (true) {
            Jogo jogo = new Jogo(2);
            int numMaxPlayers = jogo.numMaxPlayers();
            for (int i = 0; i < numMaxPlayers; i++) {
                Socket clientSocket = esperaCliente(serverSocket);
                jogo.addPlayer(clientSocket);
            }
            jogo.iniciaLogica(new Logica(jogo));
            jogo.inicia();
        }
    }

    ServerSocket iniciaServer(int porto) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(porto);
        } catch (IOException e) {
            System.out.println("Não pode escutar o porto: " + porto + "\n" + e);
            System.exit(1);
        }
        return serverSocket;
    }

    Socket esperaCliente(ServerSocket) {
        Socket clientSocket = null;
        try {
            System.out.println("Esperando conexao de um jogador");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Accept falhou: " + serverSocket.getLocalPort() + "\n" + e);
            System.exit(1);
        }
        System.out.println("Accept funcionou");
        return clientSocket;
    }

    public static void main(String args[]) {
        new ServerJogo();
    }
}