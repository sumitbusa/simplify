package com.akshenkadakia.homeutility.home;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;

public class Utils {

	private Context _context;

	// constructor
	public Utils(Context context) {
		this._context = context;
	}

	/*
	 * Reading file paths from SDCard
	 */
	public ArrayList<String> getFilePaths() {
		ArrayList<String> filePaths = new ArrayList<String>();
		try {
			File[] outFile = new File(_context.getExternalFilesDir(null).toString() + "/gallery").listFiles();
			for (File filename : outFile) {
				filePaths.add(filename.getAbsolutePath());
			}
		}catch (NullPointerException e){}
		return filePaths;
	}

	/*
	 * getting screen width
	 */
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
	/*
	    getting screen Height
	 */
    public int getScreenHeight(){
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.y;
        return columnWidth;
    }

}
