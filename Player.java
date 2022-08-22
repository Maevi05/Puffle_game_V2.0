import java.awt.Rectangle;

class Tamanho {
    int l, h;

    Tamanho(int largura, int altura) {
        l = largura;
        h = altura;
    }
}

public class Player {
    //static final int PRETO = 1;
    static final int PUFFLE = 1;
    static final int COME = 2;

    int x, y; 

    Rectangle colisao;//posicao onde vai ser desenhada a imagem

    boolean invertido = false;
    int estado = PUFFLE;

    Tamanho[] tam = new Tamanho[6];//tamanho da imagem do jogador

    int centroPlayer = 32;//deslocamento até o centro da imagem

    int pontos = 0;

    Player() {
        start();
    }

    Player(int x, int y) {
        start();
        posicao(x, y);
    }

    void start() {
        defineTamanhoPlayer();
        colisao = new Rectangle(0, 0, tam[estado].l, tam[estado].h);
        estado(PUFFLE);
    }

    void defineTamanhoPlayer() {
        tam[PUFFLE] = new tam(50, 100);
        tam[COME] = new tam(25, 50);
    }

    void inverte(boolean invertido) {
        this.invertido = invertido;
        posicao(x);
    }

    void estado(int estado) {
        this.estado = estado;
        colisao.setSize(tam[estado].l, tam[estado].h);
        posicao(x, y);
    }

    void posicao(int x, int y) {
        this.y = y;
        colisao.y = y - colisao.height;
        posicao(x);
    }

    void posicao(int x) {
        this.x = x;
        if (invertido) {
            colisao.setLocation(x - tam[estado].l + centroPlayer, colisao.y);
        } else {
            colisao.setLocation(x - centroPlayer, colisao.y);
        }
    }

    boolean verificaColisao (Player p) {
        return colisao.intersects(p.colisao);
    }
}