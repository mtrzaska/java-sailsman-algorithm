import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TravelingSalesman extends JFrame {

    private List<Point> vertices;   // Lista wierzchołków grafu
    private List<Point> path;       // Lista reprezentująca trasę rozwiązania problemu
    private double totalDistance;   // Całkowita odległość pokonana przez handlowca podróżującego

    /**
     * Konstruktor klasy TravelingSalesman.
     * Inicjalizuje okno, generuje losowe wierzchołki, rozwiązuje problem handlowca podróżującego
     * i rysuje graficzne przedstawienie rozwiązania.
     */
    public TravelingSalesman() {
        setTitle("Traveling Salesman Problem");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int numberOfVertices = new Random().nextInt(10) + 5;
        vertices = generateRandomVertices(numberOfVertices);
        path = solveTravelingSalesman(new ArrayList<>(vertices));

        // Oblicz i wydrukuj całkowitą odległość
        calculateTotalDistance();
        System.out.println("Total Distance: " + totalDistance);

        setVisible(true);
    }

    /**
     * Metoda rysująca graficzne przedstawienie wierzchołków i trasy.
     * @param g Obiekt Graphics do rysowania elementów graficznych.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Znajdź minimalne i maksymalne współrzędne x i y
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point vertex : vertices) {
            minX = Math.min(minX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxX = Math.max(maxX, vertex.x);
            maxY = Math.max(maxY, vertex.y);
        }

        // Dodaj margines, aby uniknąć obcięcia
        int margin = 20;
        minX -= margin;
        minY -= margin;
        maxX += margin;
        maxY += margin;

        // Oblicz skalę, aby wszystko zmieściło się na ekranie
        double scale = Math.min(780.0 / (maxX - minX), 780.0 / (maxY - minY));

        // Rysuj wierzchołki
        g.setColor(Color.BLUE);
        for (Point vertex : vertices) {
            int scaledX = (int) ((vertex.x - minX) * scale) + margin;
            int scaledY = (int) ((vertex.y - minY) * scale) + margin;
            g.fillOval(scaledX, scaledY, 10, 10);
        }

        // Rysuj trasę
        g.setColor(Color.RED);
        for (int i = 0; i < path.size() - 1; i++) {
            Point current = path.get(i);
            Point next = path.get(i + 1);
            int scaledCurrentX = (int) ((current.x - minX) * scale) + margin + 5;
            int scaledCurrentY = (int) ((current.y - minY) * scale) + margin + 5;
            int scaledNextX = (int) ((next.x - minX) * scale) + margin + 5;
            int scaledNextY = (int) ((next.y - minY) * scale) + margin + 5;
            g.drawLine(scaledCurrentX, scaledCurrentY, scaledNextX, scaledNextY);
        }
    }

    /**
     * Generuje losowe wierzchołki na płaszczyźnie.
     * @param numberOfVertices Liczba wierzchołków do wygenerowania.
     * @return Lista wierzchołków.
     */
    private List<Point> generateRandomVertices(int numberOfVertices) {
        List<Point> vertices = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfVertices; i++) {
            int x = random.nextInt(760); // Losowa współrzędna x w zakresie od 0 do 760
            int y = random.nextInt(760); // Losowa współrzędna y w zakresie od 0 do 760
            vertices.add(new Point(x, y));
        }

        return vertices;
    }

    /**
     * Rozwiązuje problem handlowca podróżującego.
     * @param vertices Lista wierzchołków do odwiedzenia.
     * @return Lista reprezentująca trasę rozwiązania problemu.
     */
    private List<Point> solveTravelingSalesman(List<Point> vertices) {
        List<Point> path = new ArrayList<>();
        Random random = new Random();

        // Wylosuj początkowy wierzchołek
        int startIndex = random.nextInt(vertices.size());
        Point currentVertex = vertices.remove(startIndex);
        path.add(currentVertex);

        while (!vertices.isEmpty()) {
            // Znajdź najbliższy wierzchołek
            int nearestIndex = findNearestVertexIndex(currentVertex, vertices);
            Point nearestVertex = vertices.remove(nearestIndex);

            // Dodaj najbliższy wierzchołek do trasy
            path.add(nearestVertex);

            // Przejdź do najbliższego wierzchołka
            currentVertex = nearestVertex;
        }

        return path;
    }

    /**
     * Oblicza całkowitą odległość pokonaną podczas podróży handlowca.
     */
    private void calculateTotalDistance() {
        totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Point current = path.get(i);
            Point next = path.get(i + 1);
            double distance = current.distance(next);
            totalDistance += distance;
            System.out.println("Distance between vertex " + i + " and " + (i + 1) + ": " + distance);
        }
    }

    /**
     * Znajduje indeks najbliższego wierzchołka do danego wierzchołka.
     * @param currentVertex Aktualny wierzchołek.
     * @param vertices Lista wierzchołków do sprawdzenia.
     * @return Indeks najbliższego wierzchołka.
     */
    private int findNearestVertexIndex(Point currentVertex, List<Point> vertices) {
        int nearestIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < vertices.size(); i++) {
            Point vertex = vertices.get(i);
            double distance = currentVertex.distance(vertex);

            if (distance < minDistance) {
                minDistance = distance;
                nearestIndex = i;
            }
        }

        return nearestIndex;
    }

    /**
     * Metoda główna uruchamiająca program.
     * @param args Argumenty wiersza poleceń (nie są używane).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TravelingSalesman::new);
    }
}
