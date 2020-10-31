import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBodyProblem extends JPanel implements ActionListener {
    double G = 6.67408e-11;
    int maxX = 768;
    int maxY = 768;
    double scale;
    List<CelestialBody> nbpL = null;
    Timer tm = new Timer(100,this);

    public void paintComponent(Graphics g) {
        super.paintComponent(g);            //Method from JPanel
        for(int i = 0; i < nbpL.size(); i++) {
            CelestialBody body = nbpL.get(i);
            g.fillOval(body.x, body.y, body.radius*2, body.radius*2);
            g.drawString(Integer.toString(i), body.x + body.radius*2, body.y + body.radius*2);
            g.drawString(Double.toString(body.xVel) + " " + Double.toString(body.yVel), body.x + body.radius*2, body.y + body.radius*2 + 10);
            g.drawString(Double.toString(body.x) + " " + Double.toString(body.y), body.x + body.radius*2, body.y + body.radius*2 + 20);
        }
        tm.start(); //Starts time and action listener
    }

    public double distance(double dx, double dy) {
        return Math.sqrt(dx*dx + dy*dy);
    }

    public double universalGravitation(double mass, double mass2, double dist) {
        return (G * mass * mass2)/(dist*dist);
    }

    public void addForce(CelestialBody body, CelestialBody body2) {
        double dx = body2.x - body.x;
        double dy = body2.y - body.y;
        double dist = distance(dx, dy);
        double force = universalGravitation(body.mass, body2.mass, dist);
        double xForce = force * dx / dist;
        double yForce = force * dy / dist;

        body.xForceComponents.add(xForce);
        body.yForceComponents.add(yForce);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < nbpL.size(); i++) {
            CelestialBody body = nbpL.get(i);
            double xNetForce = 0.0;
            double yNetForce = 0.0;
            for (int j = 0; j < nbpL.size(); j++) {
                if (i != j) {
                    CelestialBody body2 = nbpL.get(j);
                    addForce(body, body2);
                }
            }
            //Total net force exerted by each external body
            for (int k = 0; k < body.xForceComponents.size(); k++) {
                xNetForce += body.xForceComponents.get(k);
                yNetForce += body.yForceComponents.get(k);
            }

            body.xVel += (xNetForce/body.mass/scale);
            body.yVel += (yNetForce/body.mass/scale);

            //System.out.print("Pre Round: " + body.xVel);
            body.xVel = Math.round(body.xVel);
            body.yVel = Math.round(body.yVel);

            body.x += (body.xVel);
            body.y += (body.yVel);
        }
        repaint();
    }

    public static class CelestialBody {
        String name;
        double mass;
        int x;
        int y;
        double xVel;
        double yVel;
        int radius;
        List<Double> xForceComponents;
        List<Double> yForceComponents;

        public CelestialBody(String name, double mass, int x, int y, double xVel, double yVel, int radius) {
            this.name = name;
            this.mass = mass;
            this.x = x;
            this.y = y;
            this.xVel = xVel;
            this.yVel = yVel;
            this.radius = radius;
            xForceComponents = new ArrayList<>(10);
            yForceComponents = new ArrayList<>(10);
        }

        @Override
        public String toString() {
            return "CelestialBody " + name +
                    ", mass=" + mass +
                    ", x=" + x +
                    ", y=" + y +
                    ", xVel=" + xVel +
                    ", yVel=" + yVel +
                    ", radius=" + radius +
                    '}';
        }
    }


    public static void main(String args[]) throws FileNotFoundException {
        NBodyProblem nbp = new NBodyProblem();

        try {
            Scanner scanner = new Scanner(new File("test.csv"));
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.next();
                if (line.equals("ArrayList")) {
                    nbp.nbpL = new ArrayList<CelestialBody>(4);
                }
                else if (line.equals("LinkedList")) {
                    nbp.nbpL = new LinkedList<CelestialBody>();
                }
                else if (lineNum == 1) {
                    nbp.scale = Double.parseDouble(line);
                }
                else {
                    String[] bodyData = line.split(",");
                    String name = bodyData[0];
                    double mass = Double.parseDouble(bodyData[1]);
                    int x = Integer.parseInt(bodyData[2]);
                    int y = Integer.parseInt(bodyData[3]);
                    double xVel = Double.parseDouble(bodyData[4]);
                    double yVel = Double.parseDouble(bodyData[5]);
                    int radius = Integer.parseInt(bodyData[6]);
                    nbp.nbpL.add(new CelestialBody(name, mass, x, y, xVel, yVel, radius));
                }
                lineNum++;
            }
            System.out.println(nbp.nbpL.size());
            System.out.println(nbp.nbpL.toString());
        }
        catch (FileNotFoundException e){
            throw e;
        }

        JFrame jf = new JFrame();
        jf.setTitle("N-Body Problem");
        jf.setSize(nbp.maxX, nbp.maxY);
        jf.add(nbp);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
