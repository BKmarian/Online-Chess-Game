import java.util.Objects;

public class Piece {
    private char culoare;
    private String nume;
    private int pozX;
    private int pozY;

    public Piece(char culoare, String nume, int pozX, int pozY) {
        this.pozX = pozX;
        this.pozY = pozY;
        this.nume = nume;

        this.culoare = culoare;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Piece piece = (Piece) object;
        return culoare == piece.culoare &&
                pozX == piece.pozX &&
                pozY == piece.pozY &&
                java.util.Objects.equals(nume, piece.nume);
    }

    public int hashCode() {

        return Objects.hash(super.hashCode(), culoare, nume, pozX, pozY);
    }

    public char getCuloare() {
        return culoare;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getPozX() {
        return pozX;
    }

    public void setPozX(int pozX) {
        this.pozX = pozX;
    }

    public int getPozY() {
        return pozY;
    }

    public void setPozY(int pozY) {
        this.pozY = pozY;
    }

    public void setCuloare(char culoare) {
        this.culoare = culoare;
    }

}