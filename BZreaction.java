package PDE;

import javax.swing.*;
import java.awt.*;

public class BZreaction extends JPanel{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final double T=30.0;
    private static final double L=3.0;
    private static final double Du=1e-5;
    private static final double Dv=1e-5;
    private static final double F=0.95;
    private static final double q=0.0075;
    private static final double eps=0.08;
    private static final int N=2000;
    private static final int M=350;
    private static final double dt=T/N;
    private static final double h=L/M;

    private static final double alphau=Du*dt/(h*h);
    private static final double alphav=Dv*dt/(h*h);

    private static final double us=(1-F-q+Math.sqrt(1-2*F+F*F+2*q+6*F*q+q*q))/2;

    private double[][] u = new double[M+1][M+1];
    private double[][] v = new double[M+1][M+1];

    private BZreaction(){
        initialize();
        Timer timer = new Timer(0, e -> {
            simulate();
            repaint();
        });
        timer.start();
    }

    private void initialize(){
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
                if ((x>=M/2-1-50 && x<=M/2+1-50 && y>=M/2-1-50 && y<=M/2+1-50) || (x>=M/2-1+10 && x<=M/2+1+10 && y>=M/2-1 && y<=M/2+1)
                 || (x>=M/2-1+40 && x<=M/2+1+40 && y>=M/2-1-60 && y<=M/2+1-60)) {
                    u[x][y] = 0.1;
                    v[x][y] = 0.1;
                } else {
                    u[x][y] = 0.0;
                    v[x][y] = 0.0;
                }
            }
        }
    }

    /*private void initialize(){
        for (int x = 0; x < M+1; x++) {
            for (int y = 0; y < M+1; y++) {
                if (x<=M/2 && y>=M/2-10 && y<=M/2+10) {
                    u[x][y] = us*1.2;
                } else {
                    u[x][y] = us*0.8;
                }
                if (y>=M/2) {
                    v[x][y] = 1.2*us;
                } else {
                    v[x][y] = 0.5*us;
                }
            }
        }
    }*/

    private void simulate(){
        double[][] new_u=new double[M+1][M+1];
        double[][] new_v=new double[M+1][M+1];

        for (int x = 1; x < M-1; x++) {
            for (int y = 1; y < M-1; y++) {
                double f_uv=(u[x][y]*(1-u[x][y])-F*v[x][y]*(u[x][y]-q)/(u[x][y]+q));
                double g_uv=(u[x][y]-v[x][y]);

                new_u[x][y]=u[x][y]+alphau*(u[x-1][y]+u[x+1][y]+u[x][y-1]+u[x][y+1]-4*u[x][y])/eps+dt*f_uv/eps;
                new_v[x][y]=v[x][y]+alphav*(v[x-1][y]+v[x+1][y]+v[x][y-1]+v[x][y+1]-4*v[x][y])+dt*g_uv;
            }
        }

        //境界条件
        for (int x = 0; x < M-1; x++) {
            u[x][0] = u[x][1];
            u[x][M] = u[x][M - 1];
            v[x][0] = v[x][1];
            v[x][M] = v[x][M - 1];
        }
        for (int y = 0; y < M-1; y++) {
            u[0][y] = u[1][y];
            u[M][y] = u[M - 1][y];
            v[0][y] = v[1][y];
            v[M][y] = v[M - 1][y];
        }
        
        u=new_u;
        v=new_v;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < M; y++) {
                float intensity = (float) Math.max(0, Math.min(1, u[x][y]));
                g.setColor(new Color(1-intensity, (float)0.3, intensity));
                g.fillRect(x * (WIDTH / M), y * (HEIGHT / M), (WIDTH / M), (HEIGHT / M));
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("BZ-Reaction Simulation");
        BZreaction panel = new BZreaction();
        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
