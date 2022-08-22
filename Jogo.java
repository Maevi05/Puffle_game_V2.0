import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Jogo {
    static final int Larg_jogo = 828;
    static final int Alt_Jogo = 467;
    int jogadoresConectados = 0;
    DataOutputStream[] os;
    DataInputStream[] is;
    Player[] players;
    int[] pontos = {0, 0};
    boolean continua = true;
    Logica logica;

    Jogo(int numMaxPlayers) {
        os = new DataOutputStream[numMaxPlayers];
        is = new DataInputStream[numMaxPlayers];
        players = new Player[numMaxPlayers];
    }

    public void addPlayer(SOcket clientSocket) {
        iniciaPlayer(jogadoresConectados, clientSocket);
        enviaDadosIniciais(jogadoresConectados);
        iniciaThreadJogadorRecebe(jogadoresConectados);
        jogadoresConectados++;
    }

    public int numMaxPlayers() {
        return players.length;
    }

    public void iniciaPlayer(int numPlayer, Socket clientSocket) {
        try {
            os[numPlayer] = new DataOutputStream(clientSocket.getOutputStream());
            is[numPlayer] = new DataInputStream(clientSocket.getInputStream());
            if(numPlayer == 0) {
                players[numPlayer] = new Player(50, Alt_Jogo - 15);
            } else {
                players[numPlayer] = new Player(Larg_jogo - 50, Alt_Jogo - 15);
                players[numPlayer].inverte(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            continua = false;
        }
    }

    public void iniciaLogica(Logica logica) {
        this.logica = logica;
    }

    public void inicia() {
        iniciaThreadJogoEnvio();
    }

    public void enviaDadosIniciais(int numPlayerDestino) {
        try {
            os[numPlayerDestino].writeInt(Larg_jogo);
            os[numPlayerDestino].writeInt(Alt_Jogo);
        } catch (IOException e) {
            e.printStackTrace();
            continua = false;
        }
    }

    public void iniciaThreadJogoEnvio() {
        new Thread(new Runnable() {
            public void run() {
                while (continua) {
                    logica.executa();

                    enviaSituacao(0);
                    enviaSituacao(1);
                    enviaPlacar();

                    enviaSituacaoInvertido(1);
                    enviaSituacaoInvertido(0);
                    enviaPlacarInvertido();

                    forcaEnvio();
                    try {
                        Thread.sleep(200);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    void iniciaThreadJogadorRecebe(int numDoPlayer) {
        new Thread(new Runnable() {
            int numPlayerEsperando = numDoPlayer;

            public void run() {
                while (continua) {
                    recebeComandosEDadosDoJogador(numPlayerEsperando);
                }
            }
        }).start();
    }

    public void enviaSituacao(int numDoPlayer) {
        try{
        os[0].writeInt(players[numDoPlayer].x);
        os[0].writeInt(players[numDoPlayer].estado);
        os[0].writeBoolean(players[numDoPlayer].invertido);
    } catch (IOException e) {
        e.printStackTrace();
        continua = false;
    }
}

public void enviaSituacaoInvertido(int numDoPlayer) {
    try {
        os[1].writeInt(Larg_jogo - players[numDoPlayer].x);
        os[1].writeInt(players[numDoPlayer].estado);
        os[1].writeBoolean(!players[numDoPlayer].invertido);
    } catch (IOException e) {
        e.printStackTrace();
        continua = false;
    }
}

public void enviaPlacar() {
    try {
        os[0].writeInt(players[0].pontos);
        os[0].writeInt(players[1].pontos);
    } catch (IOException e) {
        e.printStackTrace();
        continua = false;;
    }
}

public void enviaPlacarInvertido() {
    try {
        os[1].writeInt(players[1].pontos);
        os[1].writeInt(players[0].pontos);
    } catch (IOException e) {
        e.printStackTrace();
        continua = false;
    }
}
    
void forcaEnvio() {
    try {
        os[0].flush();
        os[1].flush();
    } catch (IOException e) {
    }
}

}