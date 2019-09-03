package generators.helpers.binarySpacePartitioning;

/**
 * Polygons clipped by a plane are located either in the plane itself,
 * are clipped or placed in the positive space / front or negative space / back.
 */
public enum HalfSpace {
	hsPositive,
	hsNegative,
	hsClipped,
	hsInsidePlane;
}
