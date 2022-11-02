package rub.inf.bi.extension.jena.utils;

import org.apache.commons.geometry.core.Point;
import org.apache.commons.geometry.euclidean.threed.line.Line3D;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class BSPTree3DEncoder{

    // point
    public BSPTree3DEncoder(Point point) {
    }

    // line
    public BSPTree3DEncoder(Line3D[] line) {
    }

    // surface
    public BSPTree3DEncoder(Polygon[] polygon) {
    }

    // body
    public BSPTree3DEncoder(MultiPolygon multiPolygon) {
    }
    
    
}
