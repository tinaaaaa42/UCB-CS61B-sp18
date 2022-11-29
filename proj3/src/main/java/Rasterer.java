import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private double ullon, ullat, lrlon, lrlat, width;
    private double[] lonDPPOfDepth;
    String[][] renderGrids;
    int bestDepth;

    public Rasterer() {
        lonDPPInitializer();

    }

    /** initialize the lonDPP[] */
    private void lonDPPInitializer() {
        lonDPPOfDepth = new double[8];
        for (int i = 0; i < 8; i += 1) {
            lonDPPOfDepth[i] = lonDPP(MapServer.ROOT_LRLON,
                    MapServer.ROOT_ULLON, MapServer.TILE_SIZE) / Math.pow(2, i);
        }
    }

    /** @return the longitudinal distance per pixel */
    private double lonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        ullon = params.get("ullon");
        ullat = params.get("ullat");
        lrlon = params.get("lrlon");
        lrlat = params.get("lrlat");
        width = params.get("w");

        if (!positionLegalChecker(ullon, ullat, lrlon, lrlat)) {
            results.put("render_grid", null);
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            results.put("depth", 0);
            results.put("query_success", false);
        } else {
            double queryLonDPP = lonDPP(lrlon, ullon, width);
            bestDepth = depthChooser(queryLonDPP);

            double lonPer = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, bestDepth);
            double latPer = (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) / Math.pow(2, bestDepth);
            int ulx = (int) ((ullon - MapServer.ROOT_ULLON) / lonPer);
            int uly = (int) ((ullat - MapServer.ROOT_ULLAT) / latPer);
            int lrx = (int) ((lrlon - MapServer.ROOT_ULLON) / lonPer);
            int lry = (int) ((lrlat - MapServer.ROOT_ULLAT) / latPer);

            renderGrids = new String[lry - uly + 1][lrx - ulx + 1];
            for (int i = 0; i < renderGrids.length; i++) {
                for (int j = 0; j < renderGrids[0].length; j++) {
                    renderGrids[i][j] = "d" + bestDepth
                            + "_x" + (ulx + j) + "_y" + (uly + i) + ".png";
                }
            }

            results.put("render_grid", renderGrids);
            results.put("raster_ul_lon", MapServer.ROOT_ULLON + ulx * lonPer);
            results.put("raster_lr_lon", MapServer.ROOT_ULLON + (lrx + 1) * lonPer);
            results.put("raster_ul_lat", MapServer.ROOT_ULLAT + uly * latPer);
            results.put("raster_lr_lat", MapServer.ROOT_ULLAT + (lry + 1) * latPer);
            results.put("depth", bestDepth);
            results.put("query_success", true);
        }
        return results;
    }

    /** @return if the given position is legal, return true. Else, return false. */
    private boolean positionLegalChecker(double ullon, double ullat, double lrlon, double lrlat) {
        return ullon >= MapServer.ROOT_ULLON && lrlon <= MapServer.ROOT_LRLON
                && ullat <= MapServer.ROOT_ULLAT && lrlat >= MapServer.ROOT_LRLAT
                && ullon < lrlon && ullat > lrlat;
    }

    private int depthChooser(double queryLonDPP) {
        int best = -1;
        for (int i = 0; i < lonDPPOfDepth.length; i += 1) {
            if (lonDPPOfDepth[i] <= queryLonDPP) {
                best = i;
                break;
            }
        }
        if (best < 0) {
            best = 7;
        }
        return best;
    }
}
