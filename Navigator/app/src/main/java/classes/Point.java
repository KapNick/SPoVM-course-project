package classes;

public class Point {
    private String name;
    private double lattitude;
    private double longtitude;

    public Point(String name, double lattitude, double longtitude) {
        this.name = name;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
    }

    public Point() {
        this.name = "";
        this.lattitude = 0;
        this.longtitude = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
