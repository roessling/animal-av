package generators.graphics;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.graphics.antialias.DDAGenerator;
import generators.graphics.antialias.XiaolinGenerator;
import generators.graphics.raymarching.RayMarchingGenerator;
import generators.graphics.regiongrowing.RegionGrowing;
import generators.graphics.simpleraytracing.SimpleRayTracingGenerator;
import generators.graphics.watershed.Watershed;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new AnnotatedDilation());
    generators.add(new Bresenham());
    generators.add(new FloodFill());
    generators.add(new GrahamScanGenerator());
        
    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new BilinearInterpolationGenerator());
    generators.add(new Bresenham2());
    generators.add(new JarvisMarch());
    generators.add(new MidpointCircleAlgorithm());
    generators.add(new ZBufferGenerator2());
    
    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new EdgeFillAlgo());
    generators.add(new GrahamScan());
    generators.add(new Quickhull());
    generators.add(new RamerDouglasPeucker());
    
    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new BoxFilter());
    generators.add(new GaussFilter());
    generators.add(new RegionGrowing()); // Exzellent.
    generators.add(new SummedAreaTableGenerator()); // Vorbildlich.
    generators.add(new Watershed()); // Exzellent.
    
    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new Delaunay());  // Gut.
    generators.add(new Voronoi());
    generators.add(new SimpleRayTracingGenerator());  // Graphisch hochwertig, äußerst flexibel.
    generators.add(new NonMaximaSuppression());
    generators.add(new SobelFilter());
    generators.add(new DDAGenerator());  // Exzellent.
    generators.add(new XiaolinGenerator());  // v.s.
    generators.add(new RangOrdnungsFilter());
    generators.add(new JitteredGridSampling());
    generators.add(new PoissonDiskSampling());
    
    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new RgbToHsl("generators/graphics/rgbToHSL", Locale.GERMANY)); // sehr gut
    generators.add(new RgbToHsl("generators/graphics/rgbToHSL", Locale.US)); // sehr gut
    generators.add(new CohenSutherland(Locale.US)); // ausgezeichnet
    generators.add(new CohenSutherland(Locale.GERMANY)); // ausgezeichnet

    // Generators from the AlgoAnim course in summer term 2016.
    generators.add(new RegionGrowth());
    generators.add(new Scanline());
    generators.add(new SutherlandHodgman());

    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new Horn());
    generators.add(new LaplaceFilter());
    generators.add(new MedianFilter());

    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new BowyerWatson());
    generators.add(new FloydSteinbergGenerator(Locale.US));
    generators.add(new FloydSteinbergGenerator(Locale.GERMANY));
    generators.add(new Histogrammausgleich());
    generators.add(new ImageGradient(Locale.US));
    generators.add(new ImageGradient(Locale.GERMANY));
    generators.add(new RayCastingGenerator(Locale.GERMANY));
    generators.add(new RayCastingGenerator(Locale.US));
    generators.add(new RgbCmykConverter("resources/RgbCmykConverter", Locale.GERMANY));
    generators.add(new RgbCmykConverter("resources/RgbCmykConverter", Locale.US));
    generators.add(new MarchingSquares());
    generators.add(new RGBImageInverter());
    generators.add(new RittersBoundingSphereGenerator(Locale.GERMANY));
    generators.add(new RittersBoundingSphereGenerator(Locale.US));
    generators.add(new ScharrOperator(Locale.US));
    generators.add(new ScharrOperator(Locale.GERMANY));
    

    // Generators from the AlgoAnim course in summer term 2019
    generators.add(new BinaryTreeBinPacking());
    generators.add(new BoundingSphereGenerator());
    generators.add(new FloodfillEight());
    generators.add(new RayMarchingGenerator());
    generators.add(new RGBImageBlender());
    
    return generators;
  }
}
