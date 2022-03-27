package renderer;

import primitives.*;

import static primitives.Util.isZero;

/**
 * Camera producing rays through a view plane
 */
public class Camera {

    private  Point p0;          // center of projection
    private  Vector vUp;        // vector pointing upwards : Y axis
    private  Vector vTo;        // vector pointing towards the scene
    private  Vector vRight;     // vector pointing towards the right : X axis

    private double distance;    // cameras distance from ViewPlane
    private double width;       // ViewPlane width
    private double height;      // ViewPlane height

    /**
     *
     * @param p0 origin  point in 3D space
     * @param vUp vechu
     * @param vTo vechulei
     */
    public Camera(Point p0, Vector vTo, Vector vUp) {
        if(!isZero(vUp.dotProduct(vTo))){
            throw  new IllegalArgumentException("vTo and vUp should be orthogonal");
        }

        this.p0 = p0;

        //normalizing the positional vectors
        this.vTo = vTo.normalize();
        this.vUp = vUp.normalize();

        this.vRight = this.vTo.crossProduct(this.vUp);

    }

    // chaining methods

    /**
     * set distance between the camera and it's view plane
     * @param distance the  distance for the view plane
     * @return instance of Camera for chaining
     */
    public Camera setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    /**
     * setting View Plane size
     * @param width     "physical" width
     * @param height    "physical" height
     * @return instance of Camera for chaining
     */
    public Camera setVPSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Constructing a ray through a pixel
     *
     * @param x
     * @param y
     * @param j
     * @param i
     * @return ray form the camera to Pixel[i,j]
     */
    public Ray constructRay(int x, int y, int j, int i) {
        //Image center
        Point Pc = p0.add(vTo.scale(distance));

        //Ratio (pixel width & height)
        double Ry =height/ y;
        double Rx = width/x;

        //Pixel[i,j] center
        Point PCenter = Pc;

        //delta values for going to Pixel[i,j]  from Pc

        double yI =  -(i - (y -1)/2d)* Ry;
        double xJ =  (j - (x -1)/2d)* Rx;

        if (! isZero(xJ) ){
            PCenter = PCenter.add(vRight.scale(xJ));
        }
        if (! isZero(yI)) {
            PCenter = PCenter.add(vUp.scale(yI));
        }

        return new Ray(p0, PCenter.subtract(p0));
    }
}
