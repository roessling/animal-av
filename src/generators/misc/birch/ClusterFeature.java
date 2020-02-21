package generators.misc.birch;

public class ClusterFeature {
    private int N;
    private Vector LS, SS;
    private String name;

    public ClusterFeature(int N, float LSX, float LSY, float SSX, float SSY, String name) {
        this(N, new Vector(LSX, LSY), new Vector(SSX, SSY), name);
    }

    private ClusterFeature(int N, Vector LS, Vector SS, String name) {
        this.N = N;
        this.LS = LS;
        this.SS = SS;
        this.name = name;
    }

    ClusterFeature(ClusterFeature cf) {
        this(cf.N, cf.LS.getX(), cf.LS.getY(), cf.SS.getX(), cf.SS.getY(), cf.name);
    }

    public String getName(){
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public int getN(){
        return N;
    }

    public Vector getLS(){
        return LS;
    }

    public Vector getSS(){
        return SS;
    }

    public Vector getCentroid() {
        return LS.divComponents(N);
    }

    public float getRadius() {
        Vector v = SS.divComponents(N)
                .sub(LS.divComponents(N).sqrComponents());
        return (float) Math.sqrt(v.getX() + v.getY());
    }

    public float getDistanceTo(ClusterFeature cf) {
        /*Vector LS1 = LS, LS2 = cf.LS;
        Vector SS1 = SS, SS2 = cf.SS;
        int N1 = N, N2 = cf.N;
        return SS2.mulComponents(N1)
                .add(SS1.mulComponents(N2))
                .sub(LS1.mulComponents(2).mulComponents(LS2))
                .divComponents(N1 * N2)
                .sqrtComponents()
                .euclidNorm();*/
        return getCentroid().sub(cf.getCentroid()).euclidNorm();
    }

    public ClusterFeature addClusterFeature(ClusterFeature cf, String name) {
        return new ClusterFeature(N + cf.N, LS.add(cf.LS), SS.add(cf.SS), name);
    }

    @Override
    public String toString() {
        return "N=" + N + ", LS=" + LS + ", SS=" + SS + ", Name=" + name;
    }

    public String toStringWithoutName() {
        return "(" + "N=" + N + ", LS=" + LS + ", SS=" + SS + ")";
    }
}
