package generators.graphics.raymarching;
import animal.main.Animal;

public class Start {
    public static void main(String[] arg)
    {
        RayMarchingGenerator generator = new RayMarchingGenerator();
        Animal.startGeneratorWindow(generator);

    }
}
