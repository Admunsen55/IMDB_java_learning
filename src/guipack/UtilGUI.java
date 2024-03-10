package guipack;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class UtilGUI {
    public static Rectangle[][] fitBounds(int width, int height, int nr_rows, int nr_columns) {
        Rectangle[][] rectangles = new Rectangle[nr_rows][nr_columns];
        int x = 0;
        int y = 0;
        int w = width / nr_columns;
        int h = height / nr_rows;
        for (int i = 0; i < nr_rows; i++) {
            for (int j = 0; j < nr_columns; j++) {
                rectangles[i][j] = new Rectangle(x, y, w, h);
                x += w;
            }
            x = 0;
            y += h;
        }
        return rectangles;
    }
    public static Rectangle getFitInCenterBounds(Rectangle old_bounds, Dimension new_dimension) {
        int x = old_bounds.x + (old_bounds.width - new_dimension.width) / 2;
        int y = old_bounds.y + (old_bounds.height - new_dimension.height) / 2;
        return new Rectangle(x, y, new_dimension.width, new_dimension.height);
    }
}
