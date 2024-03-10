package guipack;

import java.util.Scanner;
import usefulpack.ComparableItem;
import userpack.User;

public class GUI {
    private static MainWindow main_window;
    private static LoginWindow login_window;
    public static MainWindow getMainWindow() {
        return main_window;
    }
    public static LoginWindow getLoginWindow() {
        return login_window;
    }
    public GUI() {
        try {
            main_window = new MainWindow();
            login_window = new LoginWindow();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    public void runInterface() {
        try {
            login_window.openLoginWindow();
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
}
