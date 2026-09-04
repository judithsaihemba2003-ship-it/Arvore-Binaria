/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arvoreprova;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArvoreProva extends JFrame {

    // ================= NÓ =================
    static class No {
        int valor;
        No esq, dir;
        boolean destaqueTriangular = false;

        No(int v) {
            valor = v;
        }
    }

    // ================= ÁRVORE =================
    static class Arvore {
        No raiz;

        void inserir(int v) {
            raiz = inserirRec(raiz, v);
        }

        private No inserirRec(No no, int v) {
            if (no == null) return new No(v);

            if (v < no.valor)
                no.esq = inserirRec(no.esq, v);
            else
                no.dir = inserirRec(no.dir, v);

            return no;
        }

        // -------- triangulares --------
        boolean triangular(int x) { // Função para verificar se um número é ou não triangular
            int n = 1;
            int t = 1;
            while (t <= x) {
                if (t == x) return true;
                n++;
                t = n * (n + 1) / 2;
            }
            return false;
        }

        void destacarTriangulares() {
            destacarRec(raiz);
        }

        private void destacarRec(No no) {
            if (no == null) return;
            no.destaqueTriangular = triangular(no.valor);
            destacarRec(no.esq);
            destacarRec(no.dir);
        }

        void removerTriangulares() { // Função para remover números triangulares
            raiz = removerTriRec(raiz);
        }

        private No removerTriRec(No no) {
            if (no == null) return null;

            no.esq = removerTriRec(no.esq);
            no.dir = removerTriRec(no.dir);

            if (triangular(no.valor))
                return remover(no, no.valor);

            return no;
        }

        private No remover(No raiz, int v) {
            if (raiz == null) return null;

            if (v < raiz.valor)
                raiz.esq = remover(raiz.esq, v);
            else if (v > raiz.valor)
                raiz.dir = remover(raiz.dir, v);
            else {
                if (raiz.esq == null) return raiz.dir;
                if (raiz.dir == null) return raiz.esq;

                No menor = raiz.dir;
                while (menor.esq != null)
                    menor = menor.esq;

                raiz.valor = menor.valor;
                raiz.dir = remover(raiz.dir, menor.valor);
            }
            return raiz;
        }

        // -------- travessias --------
        void emOrdem(No n, List<Integer> l) {  // Esquerda direita raíz
            if (n == null) return;
            emOrdem(n.esq, l);
            l.add(n.valor);
            emOrdem(n.dir, l);
        }

        void preOrdem(No n, List<Integer> l) { //  raíz esquerda direita
            if (n == null) return;
            l.add(n.valor);
            preOrdem(n.esq, l);
            preOrdem(n.dir, l);
        }

        void posOrdem(No n, List<Integer> l) { //esquerda raíz direita
            if (n == null) return;
            posOrdem(n.esq, l);
            posOrdem(n.dir, l);
            l.add(n.valor);
        }

        // -------- similar --------
        boolean similar(No a, No b) {  // Duas árvores são similares se têm o mesmo número de nós, onde cada nó possui os mesmos números
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;

            return similar(a.esq, b.esq) && similar(a.dir, b.dir);
        }
    }

    // ================= PAINEL DESENHO =================
    class Painel extends JPanel {
        Arvore arvore;
        Color cor;

        Painel(Arvore a, Color c) {
            arvore = a;
            cor = c;
            setBackground(Color.WHITE);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenhar(g, arvore.raiz, getWidth()/2, 40, getWidth()/4);
        }

        private void desenhar(Graphics g, No no, int x, int y, int dist) {
            if (no == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            if (no.esq != null) {
                g2.drawLine(x, y, x-dist, y+70);
                desenhar(g2, no.esq, x-dist, y+70, dist/2);
            }

            if (no.dir != null) {
                g2.drawLine(x, y, x+dist, y+70);
                desenhar(g2, no.dir, x+dist, y+70, dist/2);
            }

            if (no.destaqueTriangular)
                g2.setColor(Color.YELLOW);
            else
                g2.setColor(cor);

            g2.fillOval(x-20, y-20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawOval(x-20, y-20, 40, 40);
            g2.drawString(""+no.valor, x-6, y+5);
        }
    }

    // ================= INTERFACE =================
    Arvore a1 = new Arvore();
    Arvore a2 = new Arvore();

    Painel p1;
    Painel p2;

    JTextArea trav1 = new JTextArea();
    JTextArea trav2 = new JTextArea();

    public ArvoreProva() {
        setTitle("Árvore Binária - Prova");
        setSize(1200,700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField t1 = new JTextField();
        t1.setBounds(20,20,100,30);
        add(t1);

        JButton b1 = new JButton("Inserir A1");
        b1.setBounds(130,20,120,30);
        add(b1);

        JTextField t2 = new JTextField();
        t2.setBounds(20,60,100,30);
        add(t2);

        JButton b2 = new JButton("Inserir A2");
        b2.setBounds(130,60,120,30);
        add(b2);

        JButton destacar = new JButton("Destacar triangulares");
        destacar.setBounds(270,20,200,30);
        add(destacar);

        JButton remover = new JButton("Remover triangulares");
        remover.setBounds(270,60,200,30);
        add(remover);

        JButton comparar = new JButton("Comparar");
        comparar.setBounds(500,40,150,30);
        add(comparar);

        // BOTÃO APAGAR TUDO
        JButton limpar = new JButton("Apagar tudo");
        limpar.setBounds(680,40,150,30);
        add(limpar);

        p1 = new Painel(a1, new Color(70,130,180));
        p1.setBounds(20,120,500,350);
        add(p1);

        p2 = new Painel(a2, new Color(220,80,80));
        p2.setBounds(600,120,500,350);
        add(p2);

        trav1.setBounds(20,500,500,120);
        add(trav1);

        trav2.setBounds(600,500,500,120);
        add(trav2);

        // ===== ações =====
        b1.addActionListener(e -> {
            int v = Integer.parseInt(t1.getText());
            a1.inserir(v);
            atualizarTrav();
            p1.repaint();
        });

        b2.addActionListener(e -> {
            int v = Integer.parseInt(t2.getText());
            a2.inserir(v);
            atualizarTrav();
            p2.repaint();
        });

        destacar.addActionListener(e -> {
            a1.destacarTriangulares();
            a2.destacarTriangulares();
            p1.repaint();
            p2.repaint();
        });

        remover.addActionListener(e -> {
            a1.removerTriangulares();
            a2.removerTriangulares();
            atualizarTrav();
            p1.repaint();
            p2.repaint();
        });

        comparar.addActionListener(e -> {
            boolean s = a1.similar(a1.raiz, a2.raiz);
            JOptionPane.showMessageDialog(this,
                    s ? "Árvores SIMILARES" : "Árvores NÃO similares");
        });

        limpar.addActionListener(e -> {
            a1.raiz = null;
            a2.raiz = null;
            trav1.setText("");
            trav2.setText("");
            p1.repaint();
            p2.repaint();
        });
    }

    void atualizarTrav() {
        List<Integer> l = new ArrayList<>();

        l.clear(); a1.emOrdem(a1.raiz,l);
        String em1 = "Em ordem: "+l;

        l.clear(); a1.preOrdem(a1.raiz,l);
        em1 += "\nPré: "+l;

        l.clear(); a1.posOrdem(a1.raiz,l);
        em1 += "\nPós: "+l;
        trav1.setText(em1);

        l.clear(); a2.emOrdem(a2.raiz,l);
        String em2 = "Em ordem: "+l;

        l.clear(); a2.preOrdem(a2.raiz,l);
        em2 += "\nPré: "+l;

        l.clear(); a2.posOrdem(a2.raiz,l);
        em2 += "\nPós: "+l;
        trav2.setText(em2);
    }

    public static void main(String[] args) {
        new ArvoreProva().setVisible(true);
    }
}
