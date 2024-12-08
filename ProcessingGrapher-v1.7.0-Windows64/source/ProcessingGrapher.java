import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import static javax.swing.JOptionPane.*; 
import processing.serial.*; 
import java.util.concurrent.locks.ReentrantLock; 
import java.awt.event.KeyEvent; 
import java.awt.Toolkit; 
import java.awt.datatransfer.StringSelection; 
import java.awt.datatransfer.Clipboard; 
import java.awt.datatransfer.Transferable; 
import java.awt.datatransfer.DataFlavor; 
import java.awt.datatransfer.UnsupportedFlavorException; 
import java.io.File; 
import javax.swing.JFrame; 
import java.awt.Dimension; 
import processing.awt.PSurfaceAWT.SmoothCanvas; 
import javafx.stage.FileChooser; 
import javafx.stage.Stage; 
import javafx.scene.canvas.Canvas; 
import javafx.application.Platform; 
import java.lang.reflect.*; 
import java.text.DecimalFormat; 
import javafx.scene.control.ButtonType; 
import javafx.scene.control.TextInputDialog; 
import javafx.scene.control.Alert; 
import javafx.scene.control.Label; 
import javafx.scene.control.TextArea; 
import javafx.scene.input.KeyCode; 
import javafx.scene.layout.GridPane; 
import javafx.scene.layout.Priority; 
import javafx.stage.StageStyle; 
import java.awt.*; 
import java.io.PrintWriter; 
import java.io.StringWriter; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Optional; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ProcessingGrapher extends PApplet {

/* * * * * * * * * * * * * * * * * * * * * * *
 * PROCESSING GRAPHER
 *
 * @file      ProcessingGrapher.pde
 * @brief     Serial monitor and real-time graphing program
 * @author    Simon Bluett
 * @website   https://wired.chillibasket.com/processing-grapher/
 *
 * @copyright GNU General Public License v3
 * @date      28th April 2024
 * @version   1.7.0
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2018-2024 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

final String versionNumber = "1.7.0";

// Swing for input popups


// Serial port handling



// Advanced key inputs


// Copy from and paste to clipboard







// File dialogue


// Resizeable windows




// Java FX imports







// -------- UI APPEARANCE SETTINGS ---------------------------------------------
// UI Scaling Options (eg. 0.6 = tiny, 1.0 = normal, 1.4 = huge)
float uimult = 1.0f;

// Fonts
final String programFont = "Lucida Sans";
final String terminalFont = "Inconsolata-SemiBold.ttf";

// Predefined colors
final int c_white = color(255, 255, 255);
final int c_blue = color(96, 200, 220);
final int c_purple = color(147, 111, 212);
final int c_red = color(208, 38, 98);
final int c_yellow = color(215, 196, 96);
final int c_green = color(35, 205, 65);
final int c_orange = color(230, 85, 37);
final int c_lightgrey = color(134, 134, 138);
final int c_grey = color(111, 108, 90);
final int c_darkgrey = color(49, 50, 44);
final int c_black = color(0, 0, 0);

// Select current colour scheme
// 0 = light (Celeste)
// 1 = dark  (One Dark Gravity)
// 2 = dark  (Monokai) - default
int colorScheme = 2;

// Graph colour list
int[] c_colorlist = {c_blue, c_purple, c_red, c_yellow, c_green, c_orange};

// Default Window Size
int lastWidth = 1000;
int lastHeight = 700;

// Size Values
final int tabWidth = 90;
final int tabHeight = 30;
final int sidebarWidth = 150;
final int sideItemHeight = 30;
final int bottombarHeight = 22;
// -----------------------------------------------------------------------------

// Define UI colour variables
int c_background;
int c_tabbar;
int c_tabbar_h;
int c_idletab;
int c_tabbar_text;
int c_idletab_text;
int c_sidebar;
int c_sidebar_h;
int c_sidebar_heading;
int c_sidebar_text;
int c_sidebar_button;
int c_sidebar_divider;
int c_sidebar_accent;
int c_terminal_text;
int c_message_text;
int c_graph_axis;
int c_graph_gridlines;
int c_graph_border;
int c_serial_message_box;
int c_message_box_outline;
int c_alert_message_box;
int c_info_message_box;
int c_status_bar;
int c_highlight_background;

// Serial Port Variables
Serial myPort;
int portNumber = 0;
int baudRate = 9600;
char lineEnding = '\n';
boolean serialConnected = false;
String currentPort = "";
String[] portList;
char serialParity = 'N';
int serialDatabits = 8;
float serialStopbits = 1.0f;
char separator = ',';


/**
 * Class containing all relevant info for a serial port connection
 * @todo - Use this class to replace the variables being used above
 *//*
class SerialPortItem {
	public Serial port;
	public int portNumber;
	public String portName;
	public int baudRate;
	public char lineEnding;
	public boolean connected;

	public SerialPortItem() {
		portNumber = 0;
		portName = "";
		baudRate = 9600;
		lineEnding = '\n';
		connected = false;
	} 
}
ArrayList<SerialPortItem> serialObjects = new ArrayList<SerialPortItem>();
*/

// Drawing Booleans
boolean redrawUI = true;
boolean redrawAlert = false;
boolean redrawContent = true;
boolean drawNewData = false;
boolean drawFPS = false;
boolean preventDrawing = false;
boolean settingsMenuActive = false;
boolean showInstructions = true;
boolean programActive = true;
int state = 0;

// Interaction Booleans
boolean textInput = false;
boolean controlKey = false;
boolean scrollingActive = false;
boolean contentScrolling = false;

// Tab Bar
ArrayList<TabAPI> tabObjects = new ArrayList<TabAPI>();
TabAPI settings;
int currentTab = 0;
int tabTop = round(tabHeight * uimult);

// Fonts
PFont base_font;
PFont mono_font;

// Alert Messages
final int alertWidth = 300;
final int alertHeight = 150;
String alertHeading = "";
boolean alertActive = false;

// Exit handler
DisposeHandler dh;

// JavaFX pop-up dialogues
Stage stage;
FileChooser fileChooser;
File currentDirectory = null;
String userInputString = null;
int startTime;
PGraphics mainCanvas;

// Options are: FX2D (Recommended for Windows and Mac), JAVA2D (Recommended for Linux)
final String activeRenderer = FX2D;



/******************************************************//**
 * @defgroup SetupFunctions
 * @brief    Program Setup & Initialisation Functions
 *
 * @details  Methods used to initialise all parts of the 
 *           GUI and set the values of required variables
 * @{
 *********************************************************/

/**
 * Processing Setup function
 *
 * This sets up the window and rendering engine.
 * All other loading is done later from the draw() 
 * function after the loading screen is drawn.
 */
public void setup() {
	println("'processing.awt.PSurfaceAWT': This warning is a known issue and doesn't affect the program");

	// Set up the window and rendering engine
	
	

	loadColorScheme(colorScheme);
	background(c_background);

	// Set the desired frame rate (FPS)
	frameRate(60);
}


/**
 * Program Setup Function
 *
 * Initialise all screen drawing parameters and 
 * instantiate the classes for each of the tabs. 
 */
public void setupProgram() {
	startTime = millis();

	// Java FX specific setup
	if (activeRenderer == FX2D) {
		stage = (Stage) ((Canvas) surface.getNative()).getScene().getWindow();
		fileChooser = new FileChooser(); 

		// Minimum Window Size
		stage.setMinWidth(600);
    	stage.setMinHeight(350);

    // Java default renderer specific setup
	} else if (activeRenderer == JAVA2D) {
		// Minimum Window Size
		SmoothCanvas sc = (SmoothCanvas) getSurface().getNative();
		JFrame jf = (JFrame) sc.getFrame();
		Dimension d = new Dimension(600, 350);
		jf.setMinimumSize(d);
	}

    // Ensure window close event is called properly
	dh = new DisposeHandler(this);

	// Add window title and icon
	surface.setIcon(loadImage("icon-48.png"));
	surface.setTitle("Processing Grapher");
	mainCanvas = g;

	// All window to be resized
	surface.setResizable(true);

	// Initialise the fonts
	base_font = createFont(programFont, PApplet.parseInt(13*uimult), true);
	mono_font = createFont(terminalFont, PApplet.parseInt(14*uimult), true);
	textFont(base_font);

	// Calculate initial screen size of the tab content area
	int tabWidth2 = round(width - (sidebarWidth * uimult));
	int tabBottom = round(height - (bottombarHeight * uimult));

	// Load the settings menu and read user preferences file
	settings = new Settings("Settings", 0, tabWidth2, tabTop, tabBottom);
	settings.drawContent();

	// Recalculate now that UI scaling preference has been loaded
	tabWidth2 = round(width - (sidebarWidth * uimult));
	tabBottom = round(height - (bottombarHeight * uimult));

	// Define all the tabs here
	tabObjects.add(new SerialMonitor("Serial", 0, tabWidth2, tabTop, tabBottom));
	tabObjects.add(new LiveGraph("Live Graph", 0, tabWidth2, tabTop, tabBottom));
	tabObjects.add(new FileGraph("File Graph", 0, tabWidth2, tabTop, tabBottom));
	tabObjects.get(0).setVisibility(true);

	// Start serial port checking thread
	portList = Serial.list();
	if (portList.length > 0) currentPort = portList[portList.length - 1];

	thread("checkSerialPortList");
}


/**
 * Resize scaling of all UI elements
 *
 * @param  amount The quantity by which to change the scaling multiplier
 */
public void uiResize(float amount) {
	// Resize UI scaler
	uimult += amount;

	uiResize();
}


/**
 * Resize scaling of all UI elements
 */
public void uiResize() {

	// Resize fonts
	base_font = createFont(programFont, PApplet.parseInt(13*uimult), true);
	mono_font = createFont(terminalFont, PApplet.parseInt(14*uimult), true);
	tabTop = round(tabHeight * uimult);

	// Update sizing on all tabs
	for (TabAPI curTab : tabObjects) {
		curTab.changeSize(0, round(width - (sidebarWidth * uimult)), round(tabHeight * uimult), round(height - (bottombarHeight * uimult)));
	}

	// Settings menu
	settings.changeSize(0, round(width - (sidebarWidth * uimult)), round(tabHeight * uimult), round(height - (bottombarHeight * uimult)));

	// Redraw all content
	redrawUI = true;
	redrawContent = true;
}


/**
 * Dispose handler which is called when the "close"
 * window button is pressed. It makes sure that the
 * exit() function properly is called in this case.
 */
public class DisposeHandler {   
	DisposeHandler(PApplet pa) {
		pa.registerMethod("dispose", this);
	}
   
	public void dispose()
	{
		exit();
	}
}

/**
 * Function to properly exit the application
 */
public void exit() {
	for (TabAPI curTab : tabObjects) {
		curTab.performExit();
	}

	/*
	boolean safeToExit = true;
	for (TabAPI curTab : tabObjects) {
		safeToExit &= curTab.checkSafeExit();
		println(safeToExit);
	}

	if (!safeToExit) {
		println("Showing dialogue");
		myShowInputDialog("Are you sure you want to exit?", "There are still recordings/tasks running.","Nope");
		delay(5000);
	}*/

	programActive = false;
	println("Exiting program - exit()");
	exitActual();
}

/** @} End of SetupFunctions */



/******************************************************//**
 * @defgroup WindowDrawing
 * @brief    Window Drawing Functions
 * 
 * @details  Functions used to manage the drawing of all
 *           elements shown in the program window
 * @{
 *********************************************************/

/**
 * Processing Draw Function
 *
 * This function manages all the screen drawing operations, 
 * and is looped at a frequency up to 60Hz (this will drop 
 * to a lower FPS under load). After the program has 
 * finished the setup process, this function will always 
 * call the drawProgram() method
 *
 * @see void drawProgram()
 */
public void draw() {

	switch (state) {
		// Draw loading screen
		case 0: 
			drawLoadingScreen();
			if (millis() > startTime + 2000) state++;
			break;

		// Setup the program
		case 1: 
			setupProgram();
			state++;
			break;

		// Normal drawing operations of the program
		default:
			drawProgram();
			break;
	}
}


/**
 * Main Program Draw Function
 *
 * The function only updates the areas of the program window 
 * which need to be redrawn, as dictated by these boolean variables:
 *
 * @info redrawContent  - Draw the entire tab content area
 * @info drawNewData    - Only redraw parts affected by new serial data
 * @info redrawUI       - Draw the tab-bar, menu area and bottom status bar
 * @info drawFPS        - Draw a frame rate indicator at the top-right
 * @info redrawAlert    - Draw the alert message box
 * @info preventDrawing - Overrides above variables, preventing the screen
 *                        from being redrawn (eg. when window is too small)
 */
public void drawProgram() {
	// Some logic to allow the screen to be updated behind alerts
	if (alertActive && (redrawContent || drawNewData || redrawUI)) {
		redrawContent = true;
		redrawUI = true;
		redrawAlert = true;
	}

	// If the window is resized, redraw all elements at the new size
	if ((lastWidth != width) || (lastHeight != height)){
		if (width < 400 || height < 300) {
			background(c_background);
			textAlign(CENTER, CENTER);
			text("Window Size too small !", width / 2, height / 2);
			lastWidth = width;
			lastHeight = height;
			preventDrawing = true;
		} else {
			redrawUI = true;
			redrawContent = true;
			preventDrawing = false;
			lastWidth = width;
			lastHeight = height;
			if (alertActive) redrawAlert = true;
			for (TabAPI curTab : tabObjects) {
				curTab.changeSize(0, round(width - (sidebarWidth * uimult)), round(tabHeight * uimult), round(height - (bottombarHeight * uimult)));
			}
			settings.changeSize(0, round(width - (sidebarWidth * uimult)), round(tabHeight * uimult), round(height - (bottombarHeight * uimult)));
		}
	}

	if (!preventDrawing) {
		// Update mouse scrolling
		if (scrollingActive) {
			if (settingsMenuActive && !contentScrolling) {
				settings.scrollBarUpdate(mouseX, mouseY);
			} else {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.scrollBarUpdate(mouseX, mouseY);
			}
		}

		// Redraw the content area elements
		if (redrawContent){
			if (tabObjects.size() > currentTab) {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.drawContent();
				redrawContent = false;
			} else currentTab = 0;
		}

		// Draw new data in the content area
		if (drawNewData) {
			if (tabObjects.size() > currentTab) {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.drawNewData();
				drawNewData = false;
			} else currentTab = 0;
		}
		
		// Redraw the UI elements (right and top bars)
		if (redrawUI){
			drawTabs(currentTab);
			drawSidebar();
			drawInfoBar();
			redrawUI = false;
		}

		// Draw an FPS indicator
		if (drawFPS) {
			rectMode(CORNERS);
			noStroke();
			textAlign(CENTER, CENTER);
			String frameRateText = "FPS: " + round(frameRate);
			fill(c_tabbar);
			final int cL = width - round((sidebarWidth + 4 + 175) * uimult + textWidth(frameRateText));
			final int cR = width - round((sidebarWidth + 2 + 175) * uimult);
			rect(cL, height - (bottombarHeight * uimult), cR, height);
			fill(c_idletab_text);
			text(frameRateText, cL, height - (bottombarHeight * uimult), cR, height - round(4*uimult));
			if (alertActive && !redrawAlert) {
				fill(c_white, 80);
				rect(cL, height - (bottombarHeight * uimult), cR, height);
			}
		}

		// Redraw the alert message
		if(redrawAlert){
			drawAlert();
			redrawAlert = false;
		}
	}
}


/**
 * Draw the top Tab navigation bar
 *
 * @param  highlight The current active tab
 */
public void drawTabs (int highlight) {

	// Setup drawing parameters
	rectMode(CORNER);
	noStroke();
	textAlign(CENTER, CENTER);

	// Tab Bar
	fill(c_tabbar);
	rect(0, 0, width, tabHeight * uimult);
	fill(c_tabbar_h);
	rect(0, (tabHeight - 1) * uimult, width, 1 * uimult);

	// Tab Buttons
	int i = 0;
	final int calcWidth = PApplet.parseInt((tabWidth - 1) * uimult);

	for(TabAPI curTab : tabObjects){
		int calcXpos = PApplet.parseInt(i * tabWidth * uimult);

		if(highlight == i){
			fill(c_background);
			rect(calcXpos, 0, calcWidth, tabHeight * uimult);
			fill(c_red);
			rect(calcXpos, 0, calcWidth, 4 * uimult);
			fill(c_tabbar_text);
		} else {
			fill(c_idletab);
			rect(calcXpos, 0, calcWidth, (tabHeight - 1) * uimult);
			fill(c_idletab_text);
		}

		text(curTab.getName(), calcXpos, 0, calcWidth, tabHeight * uimult);
		i++;
	}

	// Settings button
	if (!settingsMenuActive) {
		fill(c_idletab);
		rect(width - PApplet.parseInt(40 * uimult), 0, 40 * uimult, (tabHeight - 1) * uimult);
		fill(c_idletab_text);
		rect(width - PApplet.parseInt(28 * uimult), tabHeight * uimult * 2 / 6 - 1 * uimult, 10 * uimult, 1.5f * uimult);
		circle(width - PApplet.parseInt(14 * uimult), tabHeight * uimult * 2 / 6, 4 * uimult);
		rect(width - PApplet.parseInt(28 * uimult), tabHeight * uimult * 3 / 6 - 1 * uimult, 2 * uimult, 1.5f * uimult);
		circle(width - PApplet.parseInt(22 * uimult), tabHeight * uimult * 3 / 6, 4 * uimult);
		rect(width - PApplet.parseInt(18 * uimult), tabHeight * uimult * 3 / 6 - 1 * uimult, 6 * uimult, 1.5f * uimult);
		rect(width - PApplet.parseInt(28 * uimult), tabHeight * uimult * 4 / 6 - 1 * uimult, 10 * uimult, 1.5f * uimult);
		circle(width - PApplet.parseInt(14 * uimult), tabHeight * uimult * 4 / 6, 4 * uimult);
	} else {
		fill(c_background);
		rect(round(width - (sidebarWidth * uimult) + 1), 0, sidebarWidth * uimult, (tabHeight - 1) * uimult);
		fill(c_tabbar_text);
		//text("X", width - int(20 * uimult), (tabHeight - 1) / 2 * uimult);
		text("Settings", width - ((sidebarWidth + 20) * uimult) / 2, (tabHeight - 1) / 2 * uimult);
		fill(c_background);
		rect(width - PApplet.parseInt(40 * uimult), 0, 40 * uimult, (tabHeight - 1) * uimult);
		fill(c_tabbar_text);
		
		stroke(c_tabbar_text);
		strokeWeight(1.5f * uimult);
		line(width - PApplet.parseInt(25 * uimult), tabHeight * uimult / 3, width - PApplet.parseInt(15 * uimult), tabHeight * uimult * 2 / 3);
		line(width - PApplet.parseInt(15 * uimult), tabHeight * uimult / 3, width - PApplet.parseInt(25 * uimult), tabHeight * uimult * 2 / 3);
	}
}


/**
 * Draw the Side Bar and Bottom Bar
 *
 * This function draws the right-side menu area
 * for the current tab and the bottom status bar
 */
public void drawSidebar () {

	// Setup drawing parameters
	rectMode(CORNER);
	noStroke();
	textAlign(CENTER, CENTER);

	// Calculate sizing of sidebar
	final int sT = round(tabHeight * uimult);
	final int sL = round(width - (sidebarWidth * uimult) + 1);
	final int sW = round(sidebarWidth * uimult);
	final int sH = height - sT;

	// Bottom info area
	fill(c_tabbar);
	rect(0, height - (bottombarHeight * uimult), width - sW + 1, bottombarHeight * uimult);
	fill(c_tabbar_h);
	rect(0, height - (bottombarHeight * uimult), width - sW + 1, 1 * uimult);

	// Sidebar
	fill(c_sidebar);
	rect(sL, sT, sW, sH);
	fill(c_sidebar_h);
	rect(sL - 1, sT, 1 * uimult, sH);
	
	// If settings menu is open, draw it
	if (settingsMenuActive) {
		settings.drawSidebar();
	// Otherwise draw the Tab-specific sidebar elements
	} else {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.drawSidebar();
		} else currentTab = 0;
	}
}


/**
 * Draw the Bottom Bar
 *
 * This function draws the right-side menu area
 * for the current tab and the bottom status bar
 */
public void drawInfoBar () {

	// Setup drawing parameters
	rectMode(CORNER);
	noStroke();
	textAlign(CENTER, CENTER);

	// Calculate sizing of info bar
	final int sW = round(sidebarWidth * uimult);
	final int bW = round(70 * uimult);
	final int pW = round(70 * uimult);
	final int cW = round(bottombarHeight * uimult);
	final int bH = round(bottombarHeight * uimult);

	final int cL = width - sW - pW - cW - bW - round(4*uimult);
	final int cR = width - sW - pW - bW - round(4*uimult);
	final int pL = width - sW - pW - bW - round(2*uimult);
	final int pR = width - sW - bW - round(2*uimult);
	final int bL = width - sW - bW - round(0*uimult);
	final int bR = width - sW - round(0*uimult);

	// Bottom info area
	fill(c_tabbar);
	rect(0, height - bH, width - sW + 1, bH);

	// Connected/Disconnected
	if (serialConnected) {
		fill(c_status_bar);
		rect(cL, height - bH, cW, bH);
		fill(c_idletab);
		circle(cL + (cW / 2) + round(1*uimult), height - (bH / 2) - round(1*uimult), round(6*uimult));
		circle(cL + (cW / 2) - round(1*uimult), height - (bH / 2) + round(1*uimult), round(6*uimult));
		stroke(c_idletab);
		strokeWeight(1 * uimult);
		line(cL + round(5*uimult), height - round(5*uimult), cR - round(5*uimult), height - bH + round(5*uimult));
		stroke(c_status_bar);
		strokeWeight(1 * uimult);
		line(cL + round(1*uimult), height - bH + round(1*uimult), cR - round(1*uimult), height - round(1*uimult));
		noStroke();
	} else {
		fill(c_idletab);
		rect(cL, height - bH, cW, bH);
		fill(c_status_bar);
		circle(cL + (cW / 2) + round(2*uimult), height - (bH / 2) - round(2*uimult), round(6*uimult));
		circle(cL + (cW / 2) - round(2*uimult), height - (bH / 2) + round(2*uimult), round(6*uimult));
		stroke(c_status_bar);
		strokeWeight(1 * uimult);
		line(cL + round(5*uimult), height - round(5*uimult), cR - round(5*uimult), height - bH + round(5*uimult));
		stroke(c_idletab);
		strokeWeight(5 * uimult);
		line(cL + round(2*uimult), height - bH + round(2*uimult), cR - round(2*uimult), height - round(2*uimult));
		stroke(c_status_bar);
		strokeWeight(1 * uimult);
		line(cL + round(7*uimult), height - bH + round(7*uimult), cR - round(7*uimult), height - round(7*uimult));
		noStroke();
	}

	// Serial port
	String[] ports = Serial.list();
	fill(c_idletab);
	rect(pL, height - bH, pW, bH);
	textAlign(CENTER, TOP);
	textFont(base_font);
	fill(c_status_bar);
	String portString = "Invalid";
	if (portNumber < ports.length) {
		portString = ports[portNumber];
	}
	portString = constrainString(portString, pW * 3 / 4);
	text(portString, pL + (pW / 2), height - bH + round(2*uimult));
	
	// Baud rates
	fill(c_idletab);
	rect(bL, height - bH, bW, bH);
	textAlign(CENTER, TOP);
	textFont(base_font);
	fill(c_status_bar);
	text(str(baudRate), bL + (bW / 2), height - bH + round(2*uimult));

	// Bar outline
	fill(c_tabbar_h);
	rect(0, height - bH, width - sW + round(1*uimult), round(1 * uimult));

	if (tabObjects.size() > currentTab) {
		TabAPI curTab = tabObjects.get(currentTab);
		curTab.drawInfoBar();
	} else currentTab = 0;
}


/**
 * Draw the loading screen which is shown during start-up
 */
public void drawLoadingScreen() {
	// Clear the background
	background(c_background);

	rectMode(CENTER);
	noStroke();
	fill(c_tabbar);
	rect(width / 2, height / 2 - PApplet.parseInt(15 * uimult), PApplet.parseInt(400 * uimult), PApplet.parseInt(340 * uimult));
	fill(c_tabbar_h);
	rect(width / 2, height / 2 - PApplet.parseInt(10 * uimult), PApplet.parseInt(400 * uimult), PApplet.parseInt(2 * uimult));

	// Set up text drawing parameters
	textAlign(CENTER, CENTER);
	textSize(PApplet.parseInt(20 * uimult));
	fill(c_tabbar_text);

	// Draw icon
	image(loadImage("icon-48.png"), (width / 2) - 24, (height / 2) - PApplet.parseInt(130 * uimult));

	// Draw text
	text("Processing Grapher", width / 2, (height / 2) - PApplet.parseInt(50 * uimult));
	textSize(PApplet.parseInt(14 * uimult));
	text("Loading v" + versionNumber, width / 2, (height / 2) + PApplet.parseInt(20 * uimult));
	fill(c_terminal_text);
	text("(C) Copyright 2018-2024 - Simon Bluett", width / 2, (height / 2) + PApplet.parseInt(60 * uimult));
	text("Free Software - GNU General Public License v3", width / 2, (height / 2) + PApplet.parseInt(90 * uimult));
}

/** @} End of WindowDrawing */



/******************************************************//**
 * @defgroup SidebarMenu
 * @brief    Sidebar Menu Drawing Functions
 *
 * @details  Functions used to draw the text, buttons and
 *           data-boxes on the sidebar menu of each tab
 * @{
 *********************************************************/

/**
 * Draw a colour hue selection box
 * 
 * @param currentColor The currently selected colour
 * @param  lS        Left X-coordinate
 * @param  tS        Top Y-coordinate
 * @param  iW        Width of colour selector box area
 * @param  tH        Height of colour selector box area
 */
public void drawColorSelector(int currentColor, float lS, float tS, float iW, float iH) {
	if (tS >= tabTop && tS <= height) {
		rectMode(CORNER);
		strokeWeight(1);
		colorMode(HSB, iW, iW, iW);
		int curX = -1, curY = -1;
		for (int i = 0; i < iW; i++) {
			for (int j = 1; j < iH; j++) {
				int pointColor = color(i, j, iW);
				if (currentColor == pointColor) {
					curX = i;
					curY = j;
				}
				stroke(pointColor);
				point(lS + i, tS + j);
				//line(lS + i, tS, lS + i, tS + iH);
			}
		}
		colorMode(RGB, 255, 255, 255);
		if (curX != -1 && curY != -1) {
			stroke(c_black);
			strokeWeight(uimult * 1.25f);
			noFill();
			ellipse(lS + curX, tS + curY, uimult * 10, uimult * 10);
		}
	}
}


/**
 * Draw a colour gradient selection box
 * 
 * @param currentColor The currently selected colour
 * @param color1       Colour at the left of the gradient
 * @param color2       Colour at the right of the gradient
 * @param  lS        Left X-coordinate
 * @param  tS        Top Y-coordinate
 * @param  iW        Width of colour selector box area
 * @param  tH        Height of colour selector box area
 */
public void drawColorBox2D(int currentColor, int color1, int color2, float lS, float tS, float iW, float iH) {
	if (tS >= tabTop && tS <= height) {
		rectMode(CORNER);
		strokeWeight(1);
		int curPoint = -1;
		for (int i = 0; i < iW; i++) {
			int horizontalColor = lerpColor(color1, color2, (float) (i) / iW);
			if (currentColor == horizontalColor) {
				curPoint = i;
			}
			stroke(horizontalColor);
			line(lS + i, tS, lS + i, tS + iH);
		}
		if (curPoint >= 0) { 
			stroke(c_sidebar_accent);
			line(lS + curPoint, tS, lS + curPoint, tS + iH);
			fill(c_sidebar_accent);
			triangle(lS + curPoint, tS + iH, lS + curPoint - (3 * uimult), tS + iH + (6 * uimult), lS + curPoint + (3 * uimult), tS + iH + (6 * uimult));
		}
	}
}


/**
 * Draw sidebar text
 *
 * @param  text      The text to display
 * @param  textcolor The colour of the text
 * @param  lS        Left X-coordinate
 * @param  tS        Top Y-coordinate
 * @param  iW        Width of text box area
 * @param  tH        Height of text box area
 */
public void drawText(String text, int textcolor, float lS, float tS, float iW, float tH) {
	if (tS >= tabTop && tS <= height) {
		textAlign(LEFT, CENTER);
		textFont(base_font);
		fill(textcolor);
		text(text, lS, tS, iW, tH);
	}
}


/**
 * Draw sidebar heading text
 *
 * @param  text The text to display
 * @param  lS   Left X-coordinate
 * @param  tS   Top Y-coordinate
 * @param  iW   Width of text box area
 * @param  tH   Height of text box area
 */
public void drawHeading(String text, float lS, float tS, float iW, float tH){
	if (tS >= tabTop && tS <= height) {
		textAlign(CENTER, CENTER);
		textFont(base_font);
		fill(c_sidebar_heading);
		text(text, lS, tS, iW, tH);
	}
}


/**
 * Draw a sidebar button (overload function)
 *
 * @see drawButton(String, color, color, float, float, float, float, float)
 */
public void drawButton(String text, int boxcolor, float lS, float tS, float iW, float iH, float tH){
	drawButton(text, c_sidebar_text, boxcolor, lS, tS, iW, iH, tH);
}


/**
 * Draw a sidebar button
 *
 * @param  text      The text to display on the button
 * @param  textcolor The colour of the text
 * @param  boxcolor  Background fill colour of the button
 * @param  lS        Top-left X-coordinate of the button
 * @param  tS        Top-left Y-coordinate of the button
 * @param  iW        Width of the button
 * @param  iH        Height of the button
 * @param  tH        Height of the text area on the button
 */
public void drawButton(String text, int textcolor, int boxcolor, float lS, float tS, float iW, float iH, float tH){
	if (tS >= tabTop && tS <= height) {
		rectMode(CORNER);
		noStroke();
		textAlign(CENTER, CENTER);
		textFont(base_font);
		fill(boxcolor);
		rect(lS, tS, iW, iH);
		fill(textcolor);
		text(text, lS, tS, iW, tH);
	}
}


/**
 * Draw a sidebar databox (overload function)
 *
 * @see drawDatabox(String, color, float, float, float, float, float)
 */
public void drawDatabox(String text, float lS, float tS, float iW, float iH, float tH){
	drawDatabox(text, c_sidebar_text, lS, tS, iW, iH, tH);
}


/**
 * Draw a sidebar databox - this is a button with no fill but just an outline
 *
 * @param  text      The text to display on the button
 * @param  textcolor The colour of the text
 * @param  lS        Top-left X-coordinate of the button
 * @param  tS        Top-left Y-coordinate of the button
 * @param  iW        Width of the button
 * @param  iH        Height of the button
 * @param  tH        Height of the text area on the button
 */
public void drawDatabox(String text, int textcolor, float lS, float tS, float iW, float iH, float tH){
	if (tS >= tabTop && tS <= height) {
		rectMode(CORNER);
		noStroke();
		textAlign(CENTER, CENTER);
		textFont(base_font);
		fill(c_sidebar_button);
		rect(lS, tS, iW, iH);
		fill(c_sidebar);
		rect(lS + (1 * uimult), tS + (1 * uimult), iW - (2 * uimult), iH - (2 * uimult));
		fill(textcolor);
		text(constrainString(text, iW - (10 * uimult)), lS, tS, iW, tH);
	}
}


/**
 * Draw a simple rectangle on the sidebar
 *
 * @param  boxcolor Background fill colour of the rectangle
 * @param  lS        Top-left X-coordinate of the button
 * @param  tS        Top-left Y-coordinate of the button
 * @param  iW        Width of the button
 * @param  iH        Height of the button
 */       
public void drawRectangle(int boxcolor, float lS, float tS, float iW, float iH){
	if (tS >= tabTop && tS <= height) {
		rectMode(CORNER);
		noStroke();
		fill(boxcolor);
		rect(lS, tS, iW, iH);
	}
}


/**
 * Draw a simple rectangle on the sidebar
 *
 * @param  boxcolor Background fill colour of the rectangle
 * @param  lS        Top-left X-coordinate of the button
 * @param  tS        Top-left Y-coordinate of the button
 * @param  iW        Width of the button
 * @param  iH        Height of the button
 */       
public void drawTriangle(int itemcolor, float x1, float y1, float x2, float y2, float x3, float y3){
	if (y1 >= tabTop && y1 <= height && y2 >= tabTop && y2 <= height && y3 >= tabTop && y3 <= height) {
		noStroke();
		fill(itemcolor);
		triangle(x1, y1, x2, y2, x3, y3);
	}
}


/**
 * Check in mouse clicked on a sidebar menu item
 *
 * @param  yPos    Mouse Y-coordinate
 * @param  topPos  Top Y-coordinate of menu area
 * @param  unitH   Height of a standard menu item unit
 * @param  itemH   Height of menu item
 * @param  n       Number of units the current item is from the top
 * @return True if mouse Y-coordinate is on the menu item, false otherwise
 */
public boolean menuYclick(int yPos, int topPos, int unitH, int itemH, float n) {
	return ((yPos >= topPos + (unitH * n)) && (yPos <= topPos + (unitH * n) + itemH));
}


/**
 * Check in mouse clicked on a sidebar menu item
 *
 * @param  xPos    Mouse X-coordinate position
 * @param  leftPos Left X-coordinate of item
 * @param  itemW   Width of menu item
 * @return True if mouse Y-coordinate is on the menu item, false otherwise
 */
public boolean menuXclick(int xPos, int leftPos, int itemW) {
	return ((xPos >= leftPos) && (xPos <= leftPos + itemW));
}


/**
 * Check in mouse clicked on a sidebar menu item
 *
 * @oaram  xPos    Mouse X-coordinate position
 * @param  yPos    Mouse Y-coordinate position
 * @param  topPos  Top Y-coordinate of menu area
 * @param  unitH   Height of a standard menu item unit
 * @param  itemH   Height of menu item
 * @param  n       Number of units the current item is from the top
 * @param  leftPos Left X-coordinate of item
 * @param  unitW   Width of menu item
 * @return True if mouse Y-coordinate is on the menu item, false otherwise
 */
public boolean menuXYclick(int xPos, int yPos, int topPos, int unitH, int itemH, float n, int leftPos, int unitW) {
	return ((yPos >= topPos + (unitH * n)) && (yPos <= topPos + (unitH * n) + itemH) && (xPos >= leftPos) && (xPos <= leftPos + unitW));
}


/**
 * Draw a message box on the screen
 *
 * @param  heading  Title text of the message box
 * @param  text     Array of strings to be displayed (each item is a new line)
 * @param  lS       Left X-coordinate of the display area
 * @param  rS       Right X-coordinate of the display area
 * @param  tS       Top Y-coordinate of the display area
 * @param  alert    Whether or not this is an alert message (reduce opacity of background)
 */ 

public void drawMessageArea(String heading, String[] text, float lS, float rS, float tS, boolean alert) {
	// Setup drawing parameters
	rectMode(CORNER);
	noStroke();
	textAlign(CENTER, CENTER);
	textFont(base_font);

	// Get text width
	final int border = PApplet.parseInt(uimult * 15);

	// Approximate how many rows of text are needed
	int boxHeight = PApplet.parseInt(30 * uimult) + 2 * border;
	int boxWidth = PApplet.parseInt(rS - lS);
	int[] itemHeight = new int[text.length];
	int largestWidth = 0;

	for (int i = 0; i < text.length; i++) {
		itemHeight[i] = PApplet.parseInt(22 * uimult);
		boxHeight += PApplet.parseInt(22 * uimult);
		int textW = PApplet.parseInt(textWidth(text[i]));

		if ((textW + 2 * border > largestWidth) && (textW + 2 * border < boxWidth)) largestWidth = PApplet.parseInt(textW + 2 * border + 2 * uimult);
		else if (textW + 2 * border > boxWidth) {
			largestWidth = boxWidth;
			boxHeight += PApplet.parseInt(22 * uimult * (ceil(textW / (boxWidth - 2 * border))));
			itemHeight[i] += PApplet.parseInt(22 * uimult * (ceil(textW / (boxWidth - 2 * border))));
		}
	}

	boxWidth = largestWidth;
	int verticalSum = PApplet.parseInt(tS + border + 25 * uimult);

	// Slightly lighten the background content
	if (alert) {
		fill(c_white, 80);
		rect(0, 0, width, height);
		if (boxWidth < alertWidth * uimult) boxWidth = PApplet.parseInt(alertWidth * uimult);
		if (boxHeight < alertHeight * uimult) {
			verticalSum += PApplet.parseInt(((alertHeight * uimult) - boxHeight) / 2.0f);
			boxHeight = PApplet.parseInt(alertHeight * uimult);
		}
	}

	// Draw the box
	fill(c_message_box_outline);
	rect(PApplet.parseInt((lS + rS) / 2.0f - (boxWidth) / 2.0f - uimult * 2), tS - PApplet.parseInt(uimult * 2), boxWidth + PApplet.parseInt(uimult * 4), boxHeight + PApplet.parseInt(uimult * 4));
	if (alert) fill(c_alert_message_box);
	else fill(c_info_message_box);
	rect((lS + rS) / 2.0f - (boxWidth) / 2.0f, tS, boxWidth, boxHeight);

	// Draw the text
	rectMode(CORNER);
	fill(c_sidebar_heading);
	text(heading, PApplet.parseInt((lS + rS) / 2.0f - boxWidth / 2.0f + border), PApplet.parseInt(tS + border), boxWidth - 2 * border, 20 * uimult);

	fill(c_sidebar_text);

	for (int i = 0; i < text.length; i++) {
		if (alert && i == text.length - 1) fill(c_lightgrey);
		text(text[i],  PApplet.parseInt((lS + rS) / 2.0f - (boxWidth) / 2.0f + border), verticalSum, boxWidth - 2 * border, itemHeight[i]);
		verticalSum += itemHeight[i];
	}
}


/**
 * Draw a message box on the screen (overload function)
 *
 * This is a simplified version of the function call
 * when the message is not an alert
 *
 * @see void drawMessageArea(String, String[], float, float, float, boolean)
 */
public void drawMessageArea(String heading, String[] text, float lS, float rS, float tS) {
	drawMessageArea(heading, text, lS, rS, tS, false);
}


/**
 * Draw an Alert message box
 *
 * This is a special instance of the message box which
 * is drawn when there is an error/notification message
 * which needs to be shown to the user
 *
 * @see void drawMessageArea(String, String[], float, float, float, boolean)
 */
public void drawAlert () {
	alertActive = true;

	String heading = "Info Message";
	String[] messages = split(alertHeading, '\n');

	if (messages.length > 1) {
		heading = messages[0];
		messages = remove(messages, 0);
	}

	messages = append(messages, "");
	messages = append(messages, "[Click to dismiss]");
	
	drawMessageArea(heading, messages, 50 * uimult, width - 50 * uimult, (height / 2.5f) - (alertHeight * uimult / 2), true);
}


/**
 * Set and show a new alert message
 *
 * @param  message The alert message text
 */
public void alertMessage(String message) {
	if (message != null) {
		alertHeading = message;
		redrawAlert = true;
	}
}

/** @} End of SidebarMenu */



/******************************************************//**
 * @defgroup KeyboardMouse
 * @brief    Keyboard and Mouse interaction functions
 *
 * @details  Functions to manage user input and interaction
 *           using the keyboard or mouse
 * @{
 *********************************************************/

/**
 * Mouse click event handler
 *
 * This function figures out which region of the
 * screen was clicked and passes the information 
 * on to the current tab
 */
public void mousePressed(){ 
	if (!alertActive) {

		// If mouse is hovering over the content area
		if ((mouseX > 0) && (mouseX < PApplet.parseInt(width - (sidebarWidth * uimult))) 
			&& (mouseY > PApplet.parseInt(tabHeight * uimult)) && (mouseY < PApplet.parseInt(height - (bottombarHeight * uimult))))
		{
			if (tabObjects.size() > currentTab) {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.contentClick(mouseX, mouseY);
			} else currentTab = 0;
		} else cursor(ARROW);

		// If mouse is hovering over a tab button
		if ((mouseY > 0) && (mouseY < tabHeight*uimult)) {
			
			// Open settings menu
			if (!settingsMenuActive && mouseX > width - PApplet.parseInt(40 * uimult)) {
				settingsMenuActive = true;
				redrawUI = true;
				settings.setVisibility(true);
			}

			// Close settings menu
			else if (settingsMenuActive && mouseX > width - PApplet.parseInt(sidebarWidth * uimult)) {
				settingsMenuActive = false;
				settings.drawNewData();
				settings.setVisibility(false);
				redrawUI = true;
			}

			// Other tab buttons
			else {
				for (int i = 0; i < tabObjects.size(); i++) {
					if ((mouseX > i*tabWidth*uimult) && (mouseX < (i+1)*tabWidth*uimult)) {
						tabObjects.get(currentTab).setVisibility(false);
						tabObjects.get(i).setVisibility(true);
						currentTab = i;
						redrawUI = redrawContent = true;
					}
				}
			}
		}

		// If mouse is over the info bar
		else if ((mouseY > height - (bottombarHeight * uimult)) && (mouseX < width - (sidebarWidth * uimult))) {
			// Calculate sizing of info bar
			final int sW = round(sidebarWidth * uimult);
			final int bW = round(70 * uimult);
			final int pW = round(70 * uimult);
			final int cW = round(bottombarHeight * uimult);

			final int cL = width - sW - pW - cW - bW - round(4*uimult);
			final int cR = width - sW - pW - bW - round(4*uimult);
			final int pL = width - sW - pW - bW - round(2*uimult);
			final int pR = width - sW - bW - round(2*uimult);
			final int bL = width - sW - bW - round(0*uimult);
			final int bR = width - sW - round(0*uimult);

			// Connect/disconnect button
			if ((mouseX >= cL) && (mouseX <= cR)) {
				setupSerial();
				redrawUI = true;
				redrawContent = true;
			// Port selection button
			} else if ((mouseX >= pL) && (mouseX <= pR)) {
				currentTab = 0;
				tabObjects.get(currentTab).setMenuLevel(1);
				settingsMenuActive = false;
				redrawContent = true;
				redrawUI = true;
			// Baud rate selection button
			} else if ((mouseX >= bL) && (mouseX <= bR)) {
				currentTab = 0;
				tabObjects.get(currentTab).setMenuLevel(2);
				settingsMenuActive = false;
				redrawContent = true;
				redrawUI = true;
			}

		}

		// If mouse is hovering over the side bar
		else if ((mouseX > width - (sidebarWidth * uimult)) && (mouseX < width)) {
			thread("menuClickEvent");
		}

	// If an alert is active, any mouse click hides the notification
	} else {
		alertActive = false;
		redrawUI = true;
		redrawContent = true;
	}
}


/**
 * Thread to manages clicks on the menu
 *
 * This thread asynchronously deals with mouse
 * clicks on the right-hand menu. Running this 
 * in a separate thread allows the main functions
 * of the program to continue working even when
 * blocking user-input pop-up dialogs are used.
 */
public void menuClickEvent() {
	if (settingsMenuActive) {
		settings.menuClick(mouseX, mouseY);
	} else {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.menuClick(mouseX, mouseY);
		} else currentTab = 0;
	}
}


/**
 * Mouse release handler
 *
 * This is only used to track and figure out the
 * end of mouse drag and drop operations
 */
public void mouseReleased() {
	if (scrollingActive) {
		scrollingActive = false;
		cursor(ARROW);
		//println("Stopping scroll");
	}
}


/**
 * Mouse Wheel Scroll Handler
 *
 * @param  event Details of the mouse-scroll event
 */
public void mouseWheel(MouseEvent event) {
  int e = event.getCount();
  
  if (abs(e) > 0) {
	
	// If mouse is hovering over the content area
	if ((mouseX > 0) && (mouseX < round(width - (sidebarWidth * uimult))) 
		&& (mouseY > round(tabHeight * uimult)) && (mouseY < round(height - (bottombarHeight * uimult))))
	{
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.scrollWheel(e);
		} else currentTab = 0;
	}

	// If mouse is hovering over the side bar
	if ((mouseX > width - (sidebarWidth * uimult)) && (mouseX < width)) {
		if (settingsMenuActive) {
			settings.scrollWheel(e);
		} else {
			if (tabObjects.size() > currentTab) {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.scrollWheel(e);
			} else currentTab = 0;
		}
	}
  }
}


/**
 * Start scrolling routine
 */
public void startScrolling(boolean content, int cursorType) {
	//println("Starting scroll");
	scrollingActive = true;
	contentScrolling = content;
	if (cursorType == 1) cursor(TEXT);
	else cursor(HAND);
}

public void startScrolling(boolean content) {
	startScrolling(content, 0);
}


/**
 * Class to manage the dragging/movement of scrollbars
 */
class ScrollBar {
	int x1, w, y1, h;
	int totalElements;
	int totalLength;
	int startPosition;
	int mouseOffset;
	float movementScaler;
	boolean orientation;
	boolean inverted;
	boolean active;

	static public final boolean HORIZONTAL = true;
	static public final boolean VERTICAL = false;
	static public final boolean INVERT = true;
	static public final boolean NORMAL = false;

	/**
	 * Constructor with scrollbar orientation defined
	 * 
	 * @param  orientation Specify horizontal (true) or vertical (false) orientation
	 */
	ScrollBar(boolean orientation, boolean inverted) {
		this.orientation = orientation;
		this.inverted = inverted;
		this.mouseOffset = 0;
		this.active = false;
	}

	/**
	 * Check whether the scrollbar is active
	 * 
	 * @return True if active, false otherwise
	 */
	public boolean active() {
		return this.active;
	}

	/**
	 * Manually enable or disable the scrollbar
	 *
	 * @param  setActive True = enable, false = disable scrollbar
	 */
	public void active(boolean setActive) {
		this.active = setActive;
	}

	/**
	 * Update the scrollbar position and dimensions
	 * 
	 * @param  totalElements Total number of elements in the list
	 * @param  totalLength   Maximum length of the scroll area width or height in px
	 * @param  xLeft         Left-most x-axis position of the scrollbar
	 * @param  yTop          Top-most y-axis position of the scrollbar
	 * @param  xWidth        X-axis width of the scrollbar
	 * @param  yHeight       Y-axis height of the scrollbar
	 */
	public void update(int totalElements, int totalLength, int xLeft, int yTop, int xWidth, int yHeight) {
		this.totalElements = totalElements;
		this.totalLength = totalLength;
		if (orientation == VERTICAL) this.mouseOffset += (yTop + yHeight) - (y1 + h);
		else this.mouseOffset += (xLeft + xWidth) - (x1 + w);
		this.x1 = xLeft;
		this.w = xWidth;
		this.y1 = yTop;
		this.h = yHeight;
		this.movementScaler = (totalElements / (float) totalLength);
	}

	/**
	 * Check if mouse has clicked on the scroll bar
	 * 
	 * @param  xcoord Mouse x-axis coordinate
	 * @param  ycoord Mouse y-axis coordinate
	 * @return True if mouse has clicked on scrollbar, false otherwise
	 */
	public boolean click(int xcoord, int ycoord) {
		if ((xcoord >= x1) && (xcoord <= x1 + w) && (ycoord >= y1) && (ycoord <= y1 + h)) {
			if (orientation == VERTICAL) {
				mouseOffset = y1 + h - ycoord; 
				startPosition = ycoord;
			} else {
				mouseOffset = x1 + w - xcoord; 
				startPosition = xcoord;
			}
			active = true;
			return true;
		}
		active = false;
		return false;
	}

	/**
	 * Mouse drag event function
	 * 
	 * @param  xcoord        Mouse x-axis coordinate
	 * @param  ycoord        Mouse y-axis coordinate
	 * @param  currentScroll The current scroll position
	 * @oaram  minScroll     Minimum scroll position
	 * @oaram  maxScroll     Maximum scroll position
	 * @retun  The new scroll position of the scrollbar
	 */
	public int move(int xcoord, int ycoord, int currentScroll, int minScroll, int maxScroll) {

		int mainCoord = ycoord;
		if (orientation == HORIZONTAL) mainCoord = xcoord;

		int currentPosition = mainCoord + mouseOffset;
		int elementsMoved = PApplet.parseInt((currentPosition - (startPosition + mouseOffset)) * movementScaler);

		if (abs(elementsMoved) >= 1) {

			if (inverted) elementsMoved = -elementsMoved;
			//println(elementsMoved, currentPosition, mouseOffset, startPosition, minScroll, maxScroll);

			if (((elementsMoved < 0) && (currentScroll == minScroll)) || ((elementsMoved > 0) && (currentScroll == maxScroll))) {
				if (orientation == VERTICAL) {
					mouseOffset = y1 + h - ycoord;
					startPosition = ycoord;
				} else {
					mouseOffset = x1 + w - xcoord; 
					startPosition = xcoord;
				}
			} else {
				currentScroll += elementsMoved;
				if (currentScroll < minScroll) currentScroll = minScroll;
				else if (currentScroll > maxScroll) currentScroll = maxScroll;

				startPosition = mainCoord;
			}
		}

		return currentScroll;
	}
}


/**
 * Keyboard Button Typed Handler
 * 
 * This function returns non-coded ASCII keys
 * which were typed. It does not track modifier
 * keys such as SHIFT and CONTROL
 */
public void keyTyped() {

	// Ignore special characters
	if (key == ENTER || key == RETURN || key == BACKSPACE || key == DELETE || key == ESC) return;

	// Only process key typed event if the control key is not pressed
	if (!controlKey) {
		if (settingsMenuActive && (mouseX >= width - (sidebarWidth * uimult))) {
			settings.keyboardInput(key, (keyCode == 0)? key: keyCode, false);
		} else {
			if (tabObjects.size() > currentTab) {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.keyboardInput(key, (keyCode == 0)? key: keyCode, false);
			} else currentTab = 0;
		}
	}
}


/**
 * Keyboard Button Pressed Handler
 *
 * This function deals with button presses for
 * all coded keys, which are not handled by the
 * keyTyped() function
 */
public void keyPressed() {

	boolean coded = (key == CODED);

	// Ensure that all special characters used by the program are marked as coded keys
	if (keyCode == KeyEvent.VK_ENTER 
		|| keyCode == KeyEvent.VK_BACK_SPACE 
		|| keyCode == KeyEvent.VK_ESCAPE 
		|| keyCode == KeyEvent.VK_DELETE)
	{
		coded = true;
	}

	// Close alerts 
	if (keyCode == KeyEvent.VK_ESCAPE && alertActive) {
		key = 0;
		alertActive = false;
		redrawUI = true;
		redrawContent = true;
		return;
	}

	// Check for control key
	if (coded && keyCode == CONTROL) {
		controlKey = true;

	// Decrease UI scaling (CTRL and -)
	} else if (controlKey && (key == '-' || key == '_' || keyCode == KeyEvent.VK_MINUS)){
		if (uimult > 0.5f) uiResize(-0.1f);

	// Increase UI scaling (CTRL and +)
	} else if (controlKey && (key == '=' || key == '+' || keyCode == KeyEvent.VK_EQUALS)) {
		if (uimult < 2.0f) uiResize(0.1f);

	// Send a serial message
	} else if (controlKey && (key == 'm' || key == 'M' || keyCode == KeyEvent.VK_M) && serialConnected) {
		thread("serialSendDialog");

	// Save or set output file location
	} else if (controlKey && (key == 's' || key == 'S' || keyCode == KeyEvent.VK_S)) {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.keyboardInput(key, KeyEvent.VK_F4, true);
			controlKey = false;
		} else {
			currentTab = 0;
		}

	// Open a file location
	} else if (controlKey && (key == 'o' || key == 'O' || keyCode == KeyEvent.VK_O)) {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.keyboardInput(key, KeyEvent.VK_F5, true);
			controlKey = false;
		} else {
			currentTab = 0;
		}

	// Start/stop recording
	} else if (controlKey && (key == 'r' || key == 'R' || keyCode == KeyEvent.VK_R)) {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.keyboardInput(key, KeyEvent.VK_F6, true);
		} else {
			currentTab = 0;
		}

	// Connect/disconnect serial port
	} else if (controlKey && (key == 'q' || key == 'Q' || keyCode == KeyEvent.VK_Q)) {
		setupSerial();
		redrawUI = true;
		redrawContent = true;

	// Copy keys
	} else if (controlKey && (key == 'c' || key == 'C' || keyCode == KeyEvent.VK_C)) {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.keyboardInput(key, KeyEvent.VK_COPY, true);
		} else {
			currentTab = 0;
		}

	// Paste keys
	} else if (controlKey && (key == 'v' || key == 'V' || keyCode == KeyEvent.VK_V)) {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.keyboardInput(key, KeyEvent.VK_PASTE, true);
		} else {
			currentTab = 0;
		}

	// Select all keys
	} else if (controlKey && (key == 'a' || key == 'A' || keyCode == KeyEvent.VK_A)) {
		if (tabObjects.size() > currentTab) {
			TabAPI curTab = tabObjects.get(currentTab);
			curTab.keyboardInput(key, KeyEvent.VK_ALL_CANDIDATES, true);
		} else {
			currentTab = 0;
		}

	// Tab key - move to next tab
	} else if (controlKey && (keyCode == KeyEvent.VK_TAB)) {
		currentTab++;
		if (currentTab >= tabObjects.size()) currentTab = 0;
		redrawUI = true;
		redrawContent = true;

	// For all other keys, send them on to the active tab
	} else if (coded) {
		if (settingsMenuActive && (keyCode == KeyEvent.VK_ESCAPE || (mouseX >= width - (sidebarWidth * uimult)))) {
			settings.keyboardInput(key, keyCode, true);
		} else {
			if (tabObjects.size() > currentTab) {
				TabAPI curTab = tabObjects.get(currentTab);
				curTab.keyboardInput(key, keyCode, true);
			} else {
				currentTab = 0;
			}
		}
	}

	// Prevent the escape key from closing the application
	if (keyCode == KeyEvent.VK_ESCAPE) {
		key = 0;
	}
}


/**
 * Keyboard Button Release Handler
 *
 * This is mainly used for tracking the use
 * of key combinations using the Control key
 */
public void keyReleased() {
	if (key == CODED && keyCode == CONTROL) {
		controlKey = false;
	}
}


/**
 * Copy text from clipboard
 * 
 * @return The string contained in the clipboard
 */
public String getStringClipboard() {

	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	Transferable contents = clipboard.getContents(null);
	Object object = null;
	DataFlavor flavor = DataFlavor.stringFlavor;
	String clipboardText = "";

	if (contents != null && contents.isDataFlavorSupported(flavor))
	{
		try
		{
			object = contents.getTransferData(flavor);
			clipboardText = (String) object;
			//println("Clipboard.getFromClipboard() >> Object transferred from clipboard.");
		}

		catch (UnsupportedFlavorException e1) // Unlikely but we must catch it
		{
			println("Clipboard.getFromClipboard() >> Unsupported flavour: " + e1);
			//~  e1.printStackTrace();
		}

		catch (java.io.IOException e2)
		{
			println("Clipboard.getFromClipboard() >> Unavailable data: " + e2);
			//~  e2.printStackTrace();
		}
	}

	return clipboardText;
}

/** @} End of KeyboardMouse */



/******************************************************//**
 * @defgroup SerialComms
 * @brief    Serial Communication Functions
 *
 * @details  Functions to manage the serial communications
 *           with UART devices and micro-controllers
 * @{
 *********************************************************/

/**
 * Setup Serial Communication
 *
 * This function manages the serial device 
 * connection and disconnection process 
 */
public void setupSerial () {

	if (!serialConnected) {

		// Get a list of the available serial ports
		String[] ports = Serial.list();

		// If no ports are available
		if(ports.length == 0) {

			alertMessage("Error\nNo serial ports available");

		// If the port number we want to use is not in the list
		} else if((portNumber < 0) || (ports.length <= portNumber)) {

			alertMessage("Error\nInvalid port number selected");

		// Try to connet to the serial port
		} else {
			try {
				// Connect to the port
				myPort = new Serial(this, Serial.list()[portNumber], baudRate, serialParity, serialDatabits, serialStopbits);
				currentPort = Serial.list()[portNumber];

				// Trigger serial event once a line-ending is reached in the buffer
				if (lineEnding != 0) {
					myPort.bufferUntil(lineEnding);
				// Else if no line-ending is set, trigger after any byte is received
				} else {
					myPort.buffer(1);
				}

				serialConnected = true;

				// Tell all the tabs that a serial device has connected
				for (TabAPI curTab : tabObjects) {
					curTab.connectionEvent(true);
				}

				redrawUI = true;
			} catch (Exception e){
				alertMessage("Error\nUnable to connect to the port:\n" + e);
			}
		}

	// Disconnect from serial port
	} else {
		myPort.clear();
		myPort.stop();
		currentPort = "";
		serialConnected = false;

		// Tell all the tabs that a device has disconnected
		for (TabAPI curTab : tabObjects) {
			curTab.connectionEvent(false);
		}

		redrawUI = true;
	}
}


/**
 * Receive Serial Message Handler
 *
 * @param  myPort The selected serial COMs port
 */
public void serialEvent (Serial myPort) {
	try {
		String inString;
		if (lineEnding != 0) {
			inString = myPort.readStringUntil(lineEnding);
		} else {
			inString = myPort.readString();
		}

		// Deal with null error
		if (inString == null) return;

		// Trim away blank spaces and line endings
		inString = trim(inString);

		// Ignore empty messages
		if (inString.length() == 0) return;

		// Remove line ending characters... is this needed?
		//inString = inString.replace("\n", "");
		//inString = inString.replace("\r", "");

		// Check if data is graphable
		boolean graphable = numberMessage(inString);

		// Send the data over to all the tabs
		for (TabAPI curTab : tabObjects) {
			curTab.parsePortData(inString, graphable);
		}

	} catch (Exception e) {
		alertMessage("Error\nUnable to read data from serial port:\n" + e);
	}
}


/**
 * Send Serial Message
 *
 * @param  message The message to be sent
 */
public void serialSend (String message) {
	if (serialConnected) {
		try {
			myPort.write(message + lineEnding);
		} catch (Exception e) {
			alertMessage("Error\nUnable to write to the serial port:\n" + e);
		}
	}
}


/**
 * Serial Message Pop-up Dialog
 *
 * Pop-up dialog to allow user to send a serial
 * message from the live graph tab
 */
public void serialSendDialog() {
	final String message = myShowInputDialog("Send a Serial Message", "", "");
	if (message != null){
		serialSend(message);
	}
}


/**
 * Serial Port List Update Thread
 *
 * This thread is started in the setup() method
 * and checks the serial list every 1 second
 * to see if there are any changes which need
 * to be taken into account
 */
public void checkSerialPortList() {
	while (programActive) {
		try {
			boolean different = false;
			boolean currentPortExists = false;

			String[] currentList = Serial.list();
			if (currentList.length != portList.length) different = true;

			for (int i = 0; i < currentList.length; i++) {
				if (i < portList.length) {
					if (!currentList[i].equals(portList[i])) different = true;
				}

				if (currentPort.equals(currentList[i])) {
					currentPortExists = true;
					portNumber = i;
				}
			}

			if (serialConnected && !currentPortExists) {
				setupSerial();
				alertMessage("Error\nThe serial port has been disconnected");
			}
			//else if (serialConnected) {
			//	serialSend("1,2,3,4,5");
			//}

			if (different) {
				redrawUI = true;
			}

			portList = currentList;

		} catch (Exception e) {
			println("Error in checkSerialPortList: " + e);
		}

		delay(1000);
	}
}

/** @} End of SerialComms */



/******************************************************//**
 * @defgroup UserInput
 * @brief    User Input and File Selection Functions
 *
 * @details  Functions to deal with the display and callback
 *           of user input and file selection pop-up dialogs
 * @{
 *********************************************************/

/**
 * Show a pop-up user input dialogue
 *
 * @param  heading     The heading text
 * @param  message     The message text
 * @param  defaultText Default value to show in the input area
 * @return The user input data
 */
public String myShowInputDialog(final String heading, final String message, final String defaultText) {
	if (activeRenderer == FX2D) {
		userInputString = "";
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				userInputString = trim(FxDialogs.showTextInput(heading, message, defaultText));
			}
		});

		// Wait for the user response
		while (programActive && userInputString == "" && userInputString != null) {
			delay(200);
		}

		return userInputString;
		
	} else {
		return trim(showInputDialog(heading + "\n" + message, defaultText));
	}
}


/**
 * Get the File Selected in the Input/Output Dialogue
 *
 * @param  selection The selected file path
 */
 public void fileSelected(File selection) {

	// If a file was actually selected
	if (selection != null) {

		// Send it over to the tabs that require it
		for (TabAPI curTab : tabObjects) {
			if (curTab.getOutput() == "") {
				// Get absolute path of file and convert backslashes into normal slashes
				String newFile = join(split(selection.getAbsolutePath(), '\\'), "/");
				curTab.setOutput(newFile);
			}
		}

	} else {
		for (TabAPI curTab : tabObjects) {
			if (curTab.getOutput() == "") {
				curTab.setOutput("No File Set");
			}
		}
	}
	redrawUI = true;
}


/**
 * Override the Processing "selectOutput" function
 * 
 * This function opens the Save File Dialogue, overriding
 * the default behaviour to use the native JavaFX file dialogue
 * when using the FX2D renderer.
 *
 * @param  message        The title of the Save File Dialogue
 * @param  callbackMethod Function to call when dialogue is submitted
 */
public @Override
void selectOutput(final String message, final String callbackMethod) {
	if (activeRenderer == FX2D) {
		fileChooser.setTitle(message);

		if (currentDirectory != null)
			fileChooser.setInitialDirectory(currentDirectory);

		fileChooser.getExtensionFilters().clear();
		if (message.contains("CSV")) {
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma Separated", "*.csv"));
		} else if (message.contains("TXT")) {
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				File file = fileChooser.showSaveDialog(stage); 
				mySelectCallback(file, callbackMethod);
			}
		});
	} else {
		selectOutput(message, callbackMethod, null);
	}
}


/**
 * Override the Processing "selectInput" function
 * 
 * This function opens the Open File Dialogue, overriding
 * the default behaviour to use the native JavaFX file dialogue
 * when using the FX2D renderer.
 *
 * @param  message        The title of the Open File Dialogue
 * @param  callbackMethod Function to call when dialogue is submitted
 */
public @Override
void selectInput(final String message, final String callbackMethod) {
	if (activeRenderer == FX2D) {
		fileChooser.setTitle(message);

		if (currentDirectory != null)
			fileChooser.setInitialDirectory(currentDirectory);
		
		fileChooser.getExtensionFilters().clear();
		if (message.contains("CSV")) {
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma Separated", "*.csv"));
		} else if (message.contains("TXT")) {
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				File file = fileChooser.showOpenDialog(stage); 
				mySelectCallback(file, callbackMethod);
			}
		});
	} else {
		selectInput(message, callbackMethod, currentDirectory);
	}
}


/**
 * Function used to run callback when a file selection dialogue is submitted
 *
 * A copy of the protected method "selectCallback" in the Processing Core
 *
 * @param  selectedFile   The file selected in the dialogue
 * @param  callbackMethod Name of the function to be called
 */ 
public void mySelectCallback(File selectedFile, String callbackMethod) {
    try {
      Class<?> callbackClass = this.getClass();
      Method selectMethod = callbackClass.getMethod(callbackMethod, new Class[] { File.class });
      selectMethod.invoke(this, new Object[] { selectedFile });
      if (selectedFile != null)
      	currentDirectory = selectedFile.getParentFile();

    } catch (IllegalAccessException iae) {
      System.err.println(callbackMethod + "() must be public");

    } catch (InvocationTargetException ite) {
      ite.printStackTrace();

    } catch (NoSuchMethodException nsme) {
      System.err.println(callbackMethod + "() could not be found");
    }
}


/**
 * User input validation class
 * 
 * The functions within this class make it easier to
 * get data from a user and check that the supplied data
 * is valid and within the specified bounds.
 */
public class ValidateInput {
	private String inputString;
	private String errorMessage;
	private int intValue;
	private double doubleValue;

	static public final int NONE = 0;
	static public final int GT = 1;
	static public final int GTE = 2;
	static public final int LT = 3;
	static public final int LTE = 4;

	/**
	 * Constructor - Show the input dialogue box and get the user response
	 * 
	 * @param  heading     Heading of the user input dialogue window
	 * @param  message     Message to display in the input dialogue window
	 * @param  defaultText Default text to appear in the textbox when window is opened
	 */
	public ValidateInput(final String heading, final String message, final String defaultText) {
		inputString = myShowInputDialog(heading, message, defaultText);
	}

	/**
	 * Check if input string is empty
	 * @return True if user input string is empty, false otherwise
	 */
	public boolean isEmpty() {
		if (inputString == null || inputString.isEmpty() || inputString.trim().isEmpty()) return true;
		return false;
	}

	/**
	 * Set which error message is shown if invalid user input is supplied
	 * @param  error  The error message to display
	 */
	public void setErrorMessage(String error) {
		errorMessage = error;
	}

	/**
	 * Check that supplied data can be parsed as a float
	 * @return True if data can be parsed as a float, false otherwise
	 */
	public boolean checkFloat() {
		return checkDouble(NONE, 0, NONE, 0);
	}

	/**
	 * Check that supplied data can be parsed as a float and is within one constraint
	 * @param  operator1  The constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the constraint against
	 * @return True if data can be parsed as a float and is within the constraint
	 */
	public boolean checkFloat(int operator1, float value1) {
		return checkDouble(operator1, value1, NONE, 0);
	}

	/**
	 * Check that supplied data can be parsed as a float and is within two constraints
	 * @param  operator1  The first constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the first constraint against
	 * @param  operator2  The second constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value2     The value to check the second constraint against
	 * @return True if data can be parsed as a float and is within the constraints
	 */
	public boolean checkFloat(int operator1, float value1, int operator2, float value2) {
		return checkDouble(operator1, value1, operator2, value2);
	}

	/**
	 * Check that supplied data can be parsed as a double
	 * @return True if data can be parsed as a double, false otherwise
	 */
	public boolean checkDouble() {
		return checkDouble(NONE, 0, NONE, 0);
	}

	/**
	 * Check that supplied data can be parsed as a double and is within one constraint
	 * @param  operator1  The constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the constraint against
	 * @return True if data can be parsed as a double and is within the constraint
	 */
	public boolean checkDouble(int operator1, double value1) {
		return checkDouble(operator1, value1, NONE, 0);
	}

	/**
	 * Check that supplied data can be parsed as a double and is within two constraints
	 * @param  operator1  The first constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the first constraint against
	 * @param  operator2  The second constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value2     The value to check the second constraint against
	 * @return True if data can be parsed as a double and is within the constraints
	 */
	public boolean checkDouble(int operator1, double value1, int operator2, double value2) {
		if (inputString != null) {
			try {
				doubleValue = Double.parseDouble(inputString);
				if (!doubleConstraint(operator1, value1) || !doubleConstraint(operator2, value2)) {
					alertMessage(errorMessage);
					return false;
				}
				return true;
			} catch (Exception e) {
				alertMessage(errorMessage);
				return false;
			}
		}
		return false;
	}

	/**
	 * Check that supplied data can be parsed as an integer
	 * @return True if data can be parsed as an integer, false otherwise
	 */
	public boolean checkInt() {
		return checkInt(NONE, 0, NONE, 0);
	}

	/**
	 * Check that supplied data can be parsed as an integer and is within one constraint
	 * @param  operator1  The constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the constraint against
	 * @return True if data can be parsed as an integer and is within the constraint
	 */
	public boolean checkInt(int operator1, int value1) {
		return checkInt(operator1, value1, NONE, 0);
	}

	/**
	 * Check that supplied data can be parsed as an integer and is within two constraints
	 * @param  operator1  The first constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the first constraint against
	 * @param  operator2  The second constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value2     The value to check the second constraint against
	 * @return True if data can be parsed as an integer and is within the constraints
	 */
	public boolean checkInt(int operator1, int value1, int operator2, int value2) {
		if (inputString != null) {
			try {
				intValue = Integer.parseInt(inputString);
				if (!intConstraint(operator1, value1) || !intConstraint(operator2, value2)) {
					alertMessage(errorMessage);
					return false;
				}
				return true;
			} catch (Exception e) {
				alertMessage(errorMessage);
				return false;
			}
		}
		return false;
	}


	/**
	 * Test the user supplied double value against a constraint
	 * @param  operators  The constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the constraint against
	 * @return True if the double is within the constraint
	 */
	private boolean doubleConstraint(int operators, double value1) {
		switch (operators) {
			case GT:
				if (doubleValue <= value1) return false;
				break;
			case GTE:
				if (doubleValue < value1) return false;
				break;
			case LT:
				if (doubleValue >= value1) return false;
				break;
			case LTE:
				if (doubleValue > value1) return false;
				break;
		}
		return true;
	}

	/**
	 * Test the user supplied integer value against a constraint
	 * @param  operators  The constraint type: GT (>), GTE (>=), LT (<), LTE (<=)
	 * @param  value1     The value to check the constraint against
	 * @return True if the integer is within the constraint
	 */
	private boolean intConstraint(int operators, int value1) {
		switch (operators) {
			case GT:
				if (intValue <= value1) return false;
				break;
			case GTE:
				if (intValue < value1) return false;
				break;
			case LT:
				if (intValue >= value1) return false;
				break;
			case LTE:
				if (intValue > value1) return false;
				break;
		}
		return true;
	}

	/**
	 * @return The parsed double
	 */
	public double getDouble() {
		return doubleValue;
	}

	/**
	 * @return The parsed float
	 */
	public float getFloat() {
		return (float) doubleValue;
	}

	/**
	 * @return The parsed integer
	 */
	public int getInt() {
		return intValue;
	}
}

/** @} End of UserInput */



/******************************************************//**
 * @defgroup UtilityFunctions
 * @brief    Utility and Data Operation Functions
 *
 * @details  Functions used to perform common data
 *           manipulation operations
 * @{
 *********************************************************/

/**
 * Ceil number up to 'n' significant figure (overload function)
 *
 * @see double ceilToSigFig(double, int)
 */
public float ceilToSigFig(float num, int n) {
	return (float) ceilToSigFig((double) num, n);
}


/**
 * Ceil number up to 'n' significant figure
 *
 * @param  num The number to be rounded
 * @param  n   The number of significant figures to keep
 * @return The number rounded up to 'n' significant figures
 */
public double ceilToSigFig(double num, int n) {
	if(num == 0) {
		return 0;
	}

	final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
	final int power = n - (int) d;

	final double magnitude = Math.pow(10, power);
	final long shifted = (long) Math.ceil(num*magnitude);
	return shifted/magnitude;
}


/**
 * Floor number down to 'n' significant figure (overload function)
 *
 * @see double floorToSigFig(double, int)
 */
public float floorToSigFig(float num, int n) {
	return (float) floorToSigFig((double) num, n);
}


/**
 * Floor number down to 'n' significant figure
 *
 * @param  num The number to be rounded
 * @param  n   The number of significant figures to keep
 * @return The number rounded down to 'n' significant figures
 */
public double floorToSigFig(double num, int n) {
	if(num == 0) {
		return 0;
	}

	final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
	final int power = n - (int) d;

	final double magnitude = Math.pow(10, power);
	final long shifted = (long) Math.floor(num*magnitude);
	return shifted/magnitude;
}


/**
 * Round number to 'n' significant figures (overload function)
 *
 * @see double roundToSigFig(double, int)
 */
public float roundToSigFig(float num, int n) {
	return (float) roundToSigFig((double) num, n);
}


/**
 * Round number up/down to 'n' significant figure
 *
 * @param  num The number to be rounded
 * @param  n   The number of significant figures to keep
 * @return The number rounded up/down to 'n' significant figures
 */
public double roundToSigFig(double num, int n) {
	if(num == 0) {
		return 0;
	}

	final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
	final int power = n - (int) d;

	final double magnitude = Math.pow(10, power);
	final long shifted = Math.round(num*magnitude);
	return shifted/magnitude;
}


/**
 * Remove an element from a string array
 *
 * @param  a     The String array
 * @param  index The index of the String to be removed
 * @return The string with the specified item removed
 */
public String[] remove(String[] a, int index){
	// Move the specified item to the end of the array
	for (int i = index + 1; i < a.length; i++) {
		a[i-1] = a[i];
	}

	// Remove the last item from the array
	return shorten(a);
}


/**
 * Test whether a character is a number/digit
 *
 * @param  c The character to be tested
 * @return True if the character is a number
 */
//boolean charIsNum(char c) {
//	return 48 <= c && c <= 57;
//}


/**
 * Test whether a String follows correct format to be displayed on live graph
 *
 * @param  msg The string to be tested
 * @return True if the string doesn't contain any invalid characters
 */
public boolean numberMessage(String msg) {
	for (int i = 0; i < msg.length() - 1; i++) {
		final char j = msg.charAt(i);
		if (((j < 43 && j != ' ') || j > 57 || j == 47) && (j != separator)) {
			return false;
		}
	}
	return true;
}


/**
 * Constrain text length to fit within a certain width
 *
 * The function removes characters from the front of the string
 * until it fits into the designated width
 *
 * @param  inputText The text to be constrained
 * @param  maxWidth  The maximum width in pixels of the text
 * @return The shortened string
 */
public String constrainString(String inputText, float maxWidth) {
	if (textWidth(inputText) > maxWidth) {
		while (textWidth(".." + inputText) > maxWidth && inputText.length() > 1) {
			inputText = inputText.substring(1, inputText.length());
		}
		inputText = ".." + inputText;
	}
	return inputText;
}

/** @} End of UtilityFunctions */



/**************************************************************************************//**
 * Abstracted TAB API Interface
 *
 * These are functions which each "Tab" in the GUI
 * need to have. This makes it easier to add new tabs
 * later on which use the same interface and serial
 * features available from the core program.
 ******************************************************************************************/
interface TabAPI {
	// Name of the tab
	public String getName();

	// Show or hide a tab
	public void setVisibility(boolean newState);
	
	// Draw functions
	public void drawContent();
	public void drawNewData();
	public void drawSidebar();
	public void drawInfoBar();

	// Mouse clicks
	public void menuClick (int xcoord, int ycoord);
	public void contentClick (int xcoord, int ycoord);
	public void scrollWheel(float e);
	public void scrollBarUpdate(int xcoord, int ycoord);

	// Keyboard input
	public void keyboardInput(char keyChar, int keyCodeInt, boolean codedKey);

	// Change content area size
	public void changeSize(int newL, int newR, int newT, int newB);
	
	// Getting new file paths
	public String getOutput();
	public void setOutput(String newoutput);
	
	// Serial communication
	public void connectionEvent(boolean status);
	public void parsePortData(String inputData, boolean graphable);

	// Set menu settings
	public void setMenuLevel(int newLevel);

	// Exit function
	public boolean checkSafeExit();
	public void performExit();
}
/**
 * Load the UI colour scheme
 *
 * @param  mode Select the colour scheme to load
 */
public void loadColorScheme(int mode) {
	switch (mode) {
		// Light mode - Celeste
		case 0:
			c_background = color(255, 255, 255);
			c_tabbar = color(229, 229, 229);
			c_tabbar_h = color(217, 217, 217);
			c_idletab = color(240, 240, 240);
			c_tabbar_text = color(50, 50, 50);
			c_idletab_text = color(140, 140, 140);
			c_sidebar = color(229, 229, 229);
			c_sidebar_h = color(217, 217, 217);
			c_sidebar_heading = color(34, 142, 195);
			c_sidebar_text = color(50, 50, 50);
			c_sidebar_button = color(255, 255, 255);
			c_sidebar_divider = color(217, 217, 217);
			c_sidebar_accent = color(255, 108, 160);
			c_terminal_text = color(136, 136, 136);
			c_message_text = c_grey;
			c_graph_axis = color(150, 150, 150);
			c_graph_gridlines = color(229, 229, 229);
			c_graph_border = c_graph_gridlines;
			c_serial_message_box = c_idletab;
			c_message_box_outline = c_tabbar_h;
			c_alert_message_box = c_tabbar;
			c_info_message_box = color(229, 229, 229);
			c_status_bar = c_message_text;
			c_highlight_background = c_tabbar;
			break;

		// Dark mode - One Dark Gravity
		case 1:
			c_background = color(40, 44, 52);
			c_tabbar = color(24, 26, 31);
			c_tabbar_h = color(19, 19, 28);
			c_idletab = color(33, 36, 43);
			c_tabbar_text = c_white;
			c_idletab_text = color(152, 152, 152);
			c_sidebar = color(24, 26, 31); //color(55, 56, 60);
			c_sidebar_h = color(55, 56, 60);
			c_sidebar_heading = color(97, 175, 239);
			c_sidebar_text = c_white;
			c_sidebar_button = color(76, 77, 81);
			c_sidebar_divider = c_grey;
			c_sidebar_accent = c_red;
			c_terminal_text = color(171, 178, 191);
			c_message_text = c_white;
			c_graph_axis = c_lightgrey;
			c_graph_gridlines = c_darkgrey;
			c_graph_border = color(60, 64, 73);
			c_serial_message_box = c_idletab;
			c_message_box_outline = c_tabbar_h;
			c_alert_message_box = c_tabbar;
			c_info_message_box = c_tabbar;
			c_status_bar = c_terminal_text;
			c_highlight_background = color(61, 67, 80);//c_tabbar;
			break;

		// Dark mode - Monokai (default)
		case 2:
		default:
			c_background = color(40, 41, 35);
			c_tabbar = color(24, 25, 21);
			c_tabbar_h = color(19, 19, 18);
			c_idletab = color(32, 33, 28);
			c_tabbar_text = c_white;
			c_idletab_text = color(152, 152, 152);
			c_sidebar = c_tabbar;
			c_sidebar_h = c_tabbar_h;
			c_sidebar_heading = color(103, 216, 239);
			c_sidebar_text = c_white;
			c_sidebar_button = color(92, 93, 90);
			c_sidebar_divider = c_grey;
			c_sidebar_accent = c_red;
			c_terminal_text = c_lightgrey;
			c_message_text = c_white;
			c_graph_axis = c_lightgrey;
			c_graph_gridlines = c_darkgrey;
			c_graph_border = c_graph_gridlines;
			c_serial_message_box = c_darkgrey;
			c_message_box_outline = c_tabbar_h;
			c_alert_message_box = c_tabbar;
			c_info_message_box = c_darkgrey;
			c_status_bar = c_lightgrey;
			c_highlight_background = c_tabbar;
			break;
	}

	redrawUI = true;
	redrawContent = true;
} 
/* * * * * * * * * * * * * * * * * * * * * * *
 * CUSTOM-TABLE CLASS
 * extends Processing Table Class
 *
 * @file     CustomTable.pde
 * @brief    Extend Table class with additional saving options
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    CustomTable
 * @see      Table <Processing core/data/Table.java>
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

class CustomTable extends Table {

	protected boolean csvStreamActive = false;
	protected PrintWriter csvWriter;


	/**
	 * Open an output stream where table rows can be saved
	 *
	 * @param  filename : The file name/location where output should be saved
	 * @return True if successful, false if function is unable to initialise output file
	 */

	public boolean openCSVoutput(String filename) {

		try {
			// Figure out location and make sure the target path exists
			File outputFile = saveFile(filename);

			// Open the writer
			csvWriter = PApplet.createWriter(outputFile);

			// Print the header row
			if (hasColumnTitles()) {
				for (int col = 0; col < getColumnCount(); col++) {
					if (col != 0) {
						csvWriter.print(',');
					}
					try {
						if (getColumnTitle(col) != null) {  // col < columnTitles.length &&
							writeEntryCSV(csvWriter, getColumnTitle(col));
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						PApplet.printArray(getColumnTitles());
						PApplet.printArray(columns);
						throw e;
					}
				}
				csvWriter.println();
			}

			csvStreamActive = true;
			return true;

		} catch (Exception e) {
			println("Error opening up CSV output file stream: " + e);
			printStackTrace(e);
			return false;
		}

	}


	/**
	 * Add save new data rows to the current output file
	 *
	 * @param  indexA : Row index where to start saving from (inclusive)
	 * @param  indexB : Row index of last row to be saved (inclusive)
	 * @return Whether save operation was successful
	 */

	public boolean saveCSVentries(int indexA, int indexB) {

		// Entries can only be saved if csv output stream is open
		if (!csvStreamActive) {
			println("Error saving CSV entries; the output stream hasn't been initialised");
			return false;
		}

		// Check that the index values are within bounds
		if (indexA < 0 || indexB >= rowCount || indexA > indexB) {
			println("Error saving CSV entries; the index parameters are out of bounds");
			return false;
		}

		try {
			// Save the specified rows
			for (int row = indexA; row < indexB + 1; row++) {
				for (int col = 0; col < getColumnCount(); col++) {
					if (col != 0) {
						csvWriter.print(',');
					}
					String entry = getString(row, col);
					// just write null entries as blanks, rather than spewing 'null'
					// all over the spreadsheet file.
					if (entry != null) {
						writeEntryCSV(csvWriter, entry);
					}
				}
				// Prints the newline for the row, even if it's missing
				csvWriter.println();
			}

			if (csvWriter.checkError()) return false;
			return true;

		} catch (Exception e) {
			println("Error saving CSV entries " + e);
			printStackTrace(e);
			return false;
		}
	}


	/**
	 * Close the CSV output stream
	 */

	public boolean closeCSVoutput() {
		if (csvStreamActive) {
			csvStreamActive = false;

			if (csvWriter.checkError()) {
				csvWriter.close();
				return false;
			}

			csvWriter.close();
		}

		return true;
	}
	
}
/* * * * * * * * * * * * * * * * * * * * * * *
 * FILE GRAPH PLOTTER CLASS
 * implements TabAPI for Processing Grapher
 *
 * @file     FileGraph.pde
 * @brief    Tab to plot CSV file data on a graph
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    FileGraph
 * @see      TabAPI <ProcessingGrapher.pde>
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

class FileGraph implements TabAPI {

	int cL, cR, cT, cB;     // Content coordinates (left, right, top bottom)
	int xData;
	Graph graph;
	int menuScroll;
	int menuHeight;
	int menuLevel;
	ScrollBar sidebarScroll = new ScrollBar(ScrollBar.VERTICAL, ScrollBar.NORMAL);

	String name;
	String outputfile;
	String currentfile;
	Table dataTable;
	ArrayList<DataSignal> dataSignals = new ArrayList<DataSignal>();

	int previousColor = c_red;
	int hueColor = c_red;
	int newColor = c_red;
	int colorSelector = 0;

	boolean saveFilePath = false;
	boolean changesMade;
	boolean labelling;
	boolean zoomActive;
	boolean workerActive;
	boolean tabIsVisible;
	int setZoomSize;
	int selectedSignal;
	float[] zoomCoordOne = {0, 0, 0, 0};


	/**
	 * Constructor
	 *
	 * @param  setname Name of the tab
	 * @param  left    Tab area left x-coordinate
	 * @param  right   Tab area right x-coordinate
	 * @param  top     Tab area top y-coordinate
	 * @param  bottom  Tab area bottom y-coordinate
	 */
	FileGraph (String setname, int left, int right, int top, int bottom) {
		name = setname;
		
		cL = left;
		cR = right;
		cT = top;
		cB = bottom;

		xData = -1;     // -1 if no data column contains x-axis data

		graph = new Graph(cL, cR, cT, cB, 0, 100, 0, 10, "Graph 1");
		graph.scale(uimult);
		graph.selected(true);
		outputfile = "No File Set";
		currentfile = "No File Set";

		tabIsVisible = false;
		zoomActive = false;
		setZoomSize = -1;
		labelling = false;
		menuScroll = 0;
		menuHeight = cB - cT - 1; 
		menuLevel = 0;
		changesMade = false;
		selectedSignal = 0;
		workerActive = false;
	}


	/**
	 * Get the name of the current tab
	 *
	 * @return Tab name
	 */
	public String getName () {
		return name;
	}


	/**
	 * Set tab as being active or hidden
	 * 
	 * @param  newState True = active, false = hidden
	 */
	public void setVisibility(boolean newState) {
		tabIsVisible = newState;
	}


	/**
	 * Set current side menu level
	 * 
	 * @param  newLevel The new menu level
	 */
	public void setMenuLevel(int newLevel) {
		menuLevel = newLevel;
	}


	/**
	 * Redraw all tab content
	 */
	public void drawContent () {

		if (workerActive) {
			String[] message = {"Loading in progress!"};
			drawMessageArea("Please Standby", message, cL + 60 * uimult, cR - 60 * uimult, cT + 30 * uimult);
		
		// Show message if no serial device is connected
		} else if (currentfile == "No File Set") {
			graph.drawGrid();
			if (showInstructions) {
				String[] message = {"1. Click 'Open CSV File' to open and plot the signals from a *.CSV file",
								    "2. The first row of the file should contain headings for each of the signal",
								    "3. If the heading starts with 'x:', this column will be used as the x-axis"};
				drawMessageArea("Getting Started", message, cL + 60 * uimult, cR - 60 * uimult, cT + 30 * uimult);
			}
		} else {
			//graph.drawGrid();
			plotFileData();
		}
	}


	/**
	 * Draw new tab data
	 */
	public void drawNewData () {
		// Not being used yet 
	}


	/**
	 * Change tab content area dimensions
	 *
	 * @param  newL New left x-coordinate
	 * @param  newR New right x-coordinate
	 * @param  newT New top y-coordinate
	 * @param  newB new bottom y-coordinate
	 */
	public void changeSize (int newL, int newR, int newT, int newB) {
		cL = newL;
		cR = newR;
		cT = newT;
		cB = newB;

		graph.scale(uimult);
		graph.setSize(cL, cR, cT, cB);
		//drawContent();
	}


	/**
	 * Change CSV data file location
	 *
	 * @param  newoutput Absolute path to the new file location
	 */
	public void setOutput (String newoutput) {
		
		if (newoutput != "No File Set") {

			if (saveFilePath) {
				// Ensure file type is *.csv
				int dotPos = newoutput.lastIndexOf(".");
				if (dotPos > 0) newoutput = newoutput.substring(0, dotPos);
				newoutput = newoutput + ".csv";

				// Test whether this file is actually accessible
				if (saveFile(newoutput) == null) {
					alertMessage("Error\nUnable to access the selected output file location; perhaps this location is write-protected?\n" + newoutput);
					newoutput = "No File Set";
				}
				outputfile = newoutput;
				saveData();

			} else {
				// Check whether file is of type *.csv
				if (newoutput.contains(".csv")) {
					//outputfile = newoutput;
					currentfile = newoutput;
					outputfile = "No File Set";
					xData = -1;
					workerActive = true;
					WorkerThread loadingThread = new WorkerThread();
					loadingThread.loadFile();
					zoomActive = false;
					changesMade = false;
				} else {
					alertMessage("Error\nInvalid file type; it must be *.csv");
					outputfile = "No File Set";
					zoomActive = false;
				}
			}
			
		} else {
			outputfile = newoutput;
			zoomActive = false;
		}

		redrawContent = true;
	}


	/**
	 * Plot CSV data from file onto a graph
	 */
	public void plotFileData () {
		if (currentfile != "No File Set" && currentfile != "" && dataTable.getColumnCount() > 0) {

			xData = -1;

			// Check that columns are loaded
			for (int i = 0; i < dataTable.getColumnCount(); i++) {
				
				String columnTitle = dataTable.getColumnTitle(i);
				if (columnTitle.contains("x:") || columnTitle.contains("X:")) {
					xData = i;
				} else if (columnTitle.contains("l:")) {
					columnTitle = split(columnTitle, ':')[1];
				}

				boolean signalCheck = false;
				for (DataSignal curSig : dataSignals) {
					if (curSig.signalText.equals(columnTitle))
						signalCheck = true;
				}
				if (!signalCheck)
					dataSignals.add(new DataSignal(columnTitle, c_colorlist[i % c_colorlist.length]));
			}

			redrawUI = true;
			if (xData == -1) graph.xAxisTitle("Time (s)");
			else {
				try {
					String xAxisName = split(dataTable.getColumnTitle(xData), ':')[1];
					graph.xAxisTitle(xAxisName);
				} catch (Exception e) {
					println("Error when trying to set X-axis name: " + e);
				}
			}

			// Ensure that some data acutally exists in the table
			if (dataTable.getRowCount() > 0 && !(xData == 0 && dataTable.getColumnCount() == 1)) {
				
				double minx, maxx;
				double miny, maxy;

				if (xData == -1) {
					minx = 0;
					maxx = 0;
					miny = dataTable.getDouble(0, 0);
					maxy = dataTable.getDouble(0, 0);
				} else {
					minx = dataTable.getDouble(0, xData);
					maxx = dataTable.getDouble(0, xData);
					if (xData == 0) {
						miny = dataTable.getDouble(0, 1);
						maxy = dataTable.getDouble(0, 1);
					} else {
						miny = dataTable.getDouble(0, 0);
						maxy = dataTable.getDouble(0, 0);
					}
				}

				// Calculate Min and Max X and Y axis values
				for (TableRow row : dataTable.rows()) {

					if (xData != -1) {
						if (minx > row.getDouble(xData)) minx = row.getDouble(xData);
						if (maxx < row.getDouble(xData)) maxx = row.getDouble(xData);
					} else {
						maxx += 1.0f / graph.frequency();
					}

					for (int i = 0; i < dataTable.getColumnCount(); i++){
						if ((i != xData) && (!dataTable.getColumnTitle(i).contains("l:"))) {
							if (miny > row.getDouble(i)) miny = row.getDouble(i);
							if (maxy < row.getDouble(i)) maxy = row.getDouble(i);
						}
					}
				}

				// Only update axis values if zoom isn't active
				if (zoomActive == false) {
					// Set these min and max values
					graph.limits((float) floorToSigFig(minx, 2), (float) ceilToSigFig(maxx, 2), (float) floorToSigFig(miny, 2), (float) ceilToSigFig(maxy, 2));
				}

				// Draw the axes and grid
				graph.reset();
				graph.drawGrid();

				int counter = 0;

				// Start plotting the data
				int percentage = 0;
				for (TableRow row : dataTable.rows()) {

					float value = counter / PApplet.parseFloat(dataTable.getRowCount());
					if (percentage < PApplet.parseInt(value * 100)) {
						percentage = PApplet.parseInt(value * 100);
						//println(percentage);
					}

					if (xData != -1){
						for (int i = 0; i < dataTable.getColumnCount(); i++) {
							if (i != xData) {
								try {
									double dataX = row.getDouble(xData);
									double dataPoint = row.getDouble(i);
									if(Double.isNaN(dataX) || Double.isNaN(dataPoint)) dataPoint = dataX = Float.MAX_VALUE;
									
									// Only plot it if it is within the X-axis data range
									if (dataX >= graph.xMin() && dataX <= graph.xMax()) {
										if (dataTable.getColumnTitle(i).contains("l:")) {
											if (dataPoint == 1) graph.xLabel((float) dataX, dataSignals.get(i).signalColor);
										} else graph.plotData((float) dataX, (float) dataPoint, i, dataSignals.get(i).signalColor);
									}
								} catch (Exception e) {
									println("Error trying to plot file data.");
									println(e);
								}
							}
						}
					} else {
						for (int i = 0; i < dataTable.getColumnCount(); i++) {
							try {

								// Only start plotting when desired X-point has arrived
								float currentX = counter / graph.frequency();
								if (currentX >= graph.xMin() && currentX <= graph.xMax()) {
									double dataPoint = row.getDouble(i);
									if (Double.isNaN(dataPoint)) dataPoint = Float.MAX_VALUE;
									if (dataTable.getColumnTitle(i).contains("l:")) {
										if (dataPoint == 1) graph.xLabel((float) currentX, dataSignals.get(i).signalColor);
									} else graph.plotData((float) currentX, (float) dataPoint, i, dataSignals.get(i).signalColor);
								}
							} catch (Exception e) {
								println("Error trying to plot file data.");
								println(e);
							}
						}
					}
					counter++;
				}
			}
		}
	}


	/**
	 * Get the current CSV data file location
	 *
	 * @return Absolute path to the data file
	 */
	public String getOutput () {
		return outputfile;
	}


	/**
	 * Save any new changes to the current CSV data file
	 */
	public void saveData () {
		if (outputfile != "No File Set" && outputfile != "" && currentfile != "No File Set") {
			try {
				saveTable(dataTable, outputfile, "csv");
				currentfile = outputfile;
				saveFilePath = false;
				redrawUI = true;
				alertMessage("Success!\nThe data has been saved to the file");
			} catch (Exception e){
				alertMessage("Error\nUnable to save file:\n" + e);
			}
			outputfile = "No File Set";
		}
	}


	/**
	 * Draw the sidebar menu for the current tab
	 */
	public void drawSidebar () {

		// Calculate sizing of sidebar
		// Do this here so commands below are simplified
		int sT = cT;
		int sL = cR;
		int sW = width - cR;
		int sH = height - sT;

		int uH = round(sideItemHeight * uimult);
		int tH = round((sideItemHeight - 8) * uimult);
		int iH = round((sideItemHeight - 5) * uimult);
		int iL = round(sL + (10 * uimult));
		int iW = round(sW - (20 * uimult));
		Filters filterClass = new Filters();

		if (menuLevel == 0)	{
			menuHeight = round((15 + dataSignals.size()) * uH);
			if (xData != -1) menuHeight -= uH;
		} else if (menuLevel == 1) {
			menuHeight = round((3 + dataSignals.size()) * uH);
			if (xData != -1) menuHeight -= uH;
		} else if (menuLevel == 2) menuHeight = round((3 + filterClass.filterList.length) * uH);
		else if (menuLevel == 3) menuHeight = round(9 * uH + iW);

		// Figure out if scrolling of the menu is necessary
		if (menuHeight > sH) {
			if (menuScroll == -1) menuScroll = 0;
			else if (menuScroll > menuHeight - sH) menuScroll = menuHeight - sH;

			// Draw left bar
			fill(c_serial_message_box);
			rect(width - round(15 * uimult) / 2, sT, round(15 * uimult) / 2, sH);

			// Figure out size and position of scroll bar indicator
			int scrollbarSize = sH - round(sH * PApplet.parseFloat(menuHeight - sH) / menuHeight);
			if (scrollbarSize < uH) scrollbarSize = uH;
			int scrollbarOffset = round((sH - scrollbarSize) * (menuScroll / PApplet.parseFloat(menuHeight - sH)));
			fill(c_terminal_text);
			rect(width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);
			sidebarScroll.update(menuHeight, sH, width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);

			sT -= menuScroll;
			sL -= round(15 * uimult) / 4;
			iL -= round(15 * uimult) / 4;
		} else {
			menuScroll = -1;
		}

		// Root sidebar menu
		if (menuLevel == 0) {
			// Open, close and save files
			drawHeading("Analyse Data", iL, sT + (uH * 0), iW, tH);
			drawButton("Open CSV File", c_sidebar_button, iL, sT + (uH * 1), iW, iH, tH);
			if (currentfile != ""  && currentfile != "No File Set" && changesMade) {
				drawButton("Save Changes", c_sidebar_button, iL, sT + (uH * 2), iW, iH, tH);
			} else {
				drawDatabox("Save Changes", c_idletab_text, iL, sT + (uH * 2), iW, iH, tH);
			}

			// Add labels to data
			drawHeading("Data Manipulation", iL, sT + (uH * 3.5f), iW, tH);
			if (currentfile != ""  && currentfile != "No File Set") {
				drawButton("Add a Label", c_sidebar_button, iL, sT + (uH * 4.5f), iW, iH, tH);
				drawButton("Apply a Filter", c_sidebar_button, iL, sT + (uH * 5.5f), iW, iH, tH);
			} else {
				drawDatabox("Add a Label", c_idletab_text, iL, sT + (uH * 4.5f), iW, iH, tH);
				drawDatabox("Apply a Filter", c_idletab_text, iL, sT + (uH * 5.5f), iW, iH, tH);
			}
			
			// Graph type
			drawHeading("Graph Options", iL, sT + (uH * 7), iW, tH);
			drawButton("Line", (graph.graphType() == Graph.LINE_CHART)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 8), iW / 3, iH, tH);
			drawButton("Dots", (graph.graphType() == Graph.DOT_CHART)? c_sidebar_accent:c_sidebar_button, iL + (iW / 3), sT + (uH * 8), iW / 3, iH, tH);
			drawButton("Bar", (graph.graphType() == Graph.BAR_CHART)? c_sidebar_accent:c_sidebar_button, iL + (iW * 2 / 3), sT + (uH * 8), iW / 3, iH, tH);
			drawRectangle(c_sidebar_divider, iL + (iW / 3), sT + (uH * 8) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
			drawRectangle(c_sidebar_divider, iL + (iW * 2 / 3), sT + (uH * 8) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

			// Graph scaling / segmentation
			drawDatabox(str(graph.xMin()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), iL, sT + (uH * 9), (iW / 2) - (6 * uimult), iH, tH);
			drawButton("x", c_sidebar_button, iL + (iW / 2) - (6 * uimult), sT + (uH * 9), 12 * uimult, iH, tH);
			drawDatabox(str(graph.xMax()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), iL + (iW / 2) + (6 * uimult), sT + (uH * 9), (iW / 2) - (6 * uimult), iH, tH);
			drawDatabox(str(graph.yMin()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), iL, sT + (uH * 10), (iW / 2) - (6 * uimult), iH, tH);
			drawButton("y", c_sidebar_button, iL + (iW / 2) - (6 * uimult), sT + (uH * 10), 12 * uimult, iH, tH);
			drawDatabox(str(graph.yMax()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), iL + (iW / 2) + (6 * uimult), sT + (uH * 10), (iW / 2) - (6 * uimult), iH, tH);

			// Zoom Options
			if (currentfile != ""  && currentfile != "No File Set") {
				drawButton("Zoom", (setZoomSize >= 0)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 11), iW / 2, iH, tH);
				drawButton("Reset", (zoomActive)? c_sidebar_accent:c_sidebar_button, iL + (iW / 2), sT + (uH * 11), iW / 2, iH, tH);
				drawRectangle(c_sidebar_divider, iL + (iW / 2), sT + (uH * 11) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
			} else {
				drawDatabox("Zoom", c_idletab_text, iL, sT + (uH * 11), iW / 2, iH, tH);
				drawDatabox("Reset", c_idletab_text, iL + (iW / 2), sT + (uH * 11), iW / 2, iH, tH);
			}

			// Input Data Columns
			drawHeading("Data Format", iL, sT + (uH * 12.5f), iW, tH);
			if (xData != -1) drawButton("X: " + split(dataSignals.get(xData).signalText, ':')[1], c_sidebar_button, iL, sT + (uH * 13.5f), iW, iH, tH);
			else drawDatabox("Rate: " + graph.frequency() + "Hz", iL, sT + (uH * 13.5f), iW, iH, tH);
			//drawButton("Add Column", c_sidebar_button, iL, sT + (uH * 12.5), iW, iH, tH);

			float tHnow = 14.5f;

			// List of Data Columns
			for (int i = 0; i < dataSignals.size(); i++) {
				if (i != xData) {
					// Column name
					drawDatabox(dataSignals.get(i).signalText, iL, sT + (uH * tHnow), iW - (40 * uimult), iH, tH);

					// Remove column button
					drawButton("✕", c_sidebar_button, iL + iW - (20 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);
					
					// Hide or Show data series
					int buttonColor = dataSignals.get(i).signalColor;
					drawButton("", buttonColor, iL + iW - (40 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);

					drawRectangle(c_sidebar_divider, iL + iW - (20 * uimult), sT + (uH * tHnow) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
					tHnow++;
				}
			}

		// Signal selection menu (signal to be filtered)
		} else if (menuLevel == 1) {
			drawHeading("Select a Signal", iL, sT + (uH * 0), iW, tH);

			float tHnow = 1;
			if (dataSignals.size() == 0 || (dataSignals.size() == 1 && xData != -1)) {
				drawText("No signals available", c_sidebar_text, iL, sT + (uH * tHnow), iW, iH);
				tHnow += 1;
			} else {
				for (int i = 0; i < dataSignals.size(); i++) {
					if (i != xData && !dataTable.getColumnTitle(i).contains("l:")) {
						drawButton(constrainString(dataSignals.get(i).signalText, iW - (10 * uimult)), c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
						tHnow += 1;
					}
				}
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_accent, iL, sT + (uH * tHnow), iW, iH, tH);

		// Filter selection menu
		} else if (menuLevel == 2) {
			drawHeading("Select a Filter", iL, sT + (uH * 0), iW, tH);

			float tHnow = 1;
			if (filterClass.filterList.length == 0) {
				drawText("No filters available", c_sidebar_text, iL, sT + (uH * tHnow), iW, iH);
				tHnow += 1;
			} else {
				for (int i = 0; i < filterClass.filterList.length; i++) {
					String filterName = filterClass.filterList[i];
					if (filterName.contains("h:")) {
						filterName = split(filterName, ":")[1];
						drawText(filterName, c_idletab_text, iL, sT + (uH * tHnow), iW, iH);
					} else {
						drawButton(constrainString(filterClass.filterList[i], iW - (10 * uimult)), c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
					}
					tHnow += 1;
				}
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_accent, iL, sT + (uH * tHnow), iW, iH, tH);
		
		// Colour picker menu
		} else if (menuLevel == 3) {
			drawHeading("Select a Colour", iL, sT + (uH * 0), iW, tH);
			drawColorSelector(hueColor, iL, sT + (uH * 1), iW, iW); 
			drawHeading("Set Brightness", iL, sT + (uH * 1.5f) + iW, iW, tH);
			drawColorBox2D(newColor, c_white, hueColor, iL, sT + (uH * 2.5f) + iW, iW / 2, iH);
			drawColorBox2D(newColor, hueColor, c_black, iL + (iW / 2), sT + (uH * 2.5f) + iW, iW / 2, iH);
			drawHeading("Colour Preview", iL, sT + (uH * 4) + iW, iW, tH);
			drawText("New", c_idletab_text, iL, sT + (uH * 4.75f) + iW, iW / 2, iH);
			drawText("Old", c_idletab_text, iL + (iW / 2) + (2 * uimult), sT + (uH * 4.75f) + iW, iW / 2, iH);
			drawButton("", newColor, iL, sT + (uH * 5.5f) + iW, (iW / 2) - (3 * uimult), iH, tH);
			drawButton("", previousColor, iL + (iW / 2) + (2 * uimult), sT + (uH * 5.5f) + iW, (iW / 2) - (2 * uimult), iH, tH);
			drawButton("Confirm", c_sidebar_button, iL, sT + (uH * 6.5f) + iW, iW, iH, tH);
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * 7.5f) + iW, iW, iH, tH);
		}
	}


	/**
	 * Draw the btoom information bar
	 */
	public void drawInfoBar() {
		int sW = width - cR;
		textAlign(LEFT, TOP);
		textFont(base_font);
		fill(c_status_bar);
		text("Input: " + constrainString(currentfile, width - sW - round(175 * uimult) - textWidth("Input: ")), 
			round(5 * uimult), height - round(bottombarHeight * uimult) + round(2*uimult));
	}


	/**
	 * Keyboard input handler function
	 *
	 * @param  key The character of the key that was pressed
	 */
	public void keyboardInput (char keyChar, int keyCodeInt, boolean codedKey) {
		if (keyChar == ESC) {
			if (menuLevel != 0) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			} else if (setZoomSize >= 0) {
				setZoomSize = -1;
				cursor(ARROW);
				redrawContent = true;
				redrawUI = true;
			}
		}

		else if (codedKey) {
			switch (keyCodeInt) {
				case UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= (12 * uimult);
						if (menuScroll < 0) menuScroll = 0;
					}
					redrawUI = true;
					break;

				case DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += (12 * uimult);
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
					}
					redrawUI = true;
					break;

				case KeyEvent.VK_PAGE_UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= height - cT;
						if (menuScroll < 0) menuScroll = 0;
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_PAGE_DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += height - cT;
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_END:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_HOME:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = 0;
						redrawUI = true;
					}
					break;


				case KeyEvent.VK_F4:
					// Save file
					if (currentfile != "" && currentfile != "No File Set" && changesMade){
						saveFilePath = true;
						outputfile = "";
						selectOutput("Select where to save the *.CSV file", "fileSelected");
					}
					break;

				case KeyEvent.VK_F5:
					// Open file
					saveFilePath = false;
					outputfile = "";
					selectInput("Select *.CSV data file to open", "fileSelected");
					break;
			}
		}
	}


	/**
	 * Content area mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void contentClick (int xcoord, int ycoord) {

		// Add a new data label
		if (labelling) {
			if (currentfile != "" && currentfile != "No File Set") {
				if (graph.onGraph(xcoord, ycoord)) {

					// Check if a label column already exists in the table
					int labelColumn = -1;
					for (int i = 0; i < dataTable.getColumnCount(); i++) {
						if (dataTable.getColumnTitle(i).contains("l:Labels")) {
							labelColumn = i;
							break;
						}
					}

					// If label column does not exist, add it to the table
					if (labelColumn == -1) {
						dataTable.addColumn("l:Labels");
						labelColumn = dataTable.getColumnCount() - 1;
						dataSignals.add(new DataSignal("Labels", c_colorlist[dataSignals.size() % c_colorlist.length]));
					}

					// Draw the label and get the x-axis position
					float xPosition = graph.xLabel(xcoord, dataSignals.get(labelColumn).signalColor);

					// Set the correct entry in the label column
					if (xData != -1) {
						// Calculate approximately where in the sequence the label should go
						int startPosition = round((xPosition - graph.xMin()) / (graph.xMax() - graph.xMin()) * (dataTable.getRowCount() - 1));
						if (startPosition < 0) startPosition = 0;
						else if (startPosition >= dataTable.getRowCount()) startPosition = dataTable.getRowCount() - 1;

						try {
							if (dataTable.getFloat(startPosition, xData) <= xPosition) {
								for (int i = startPosition; i < dataTable.getRowCount() - 2; i++) {
									if (xPosition < dataTable.getFloat(i + 1, xData)) {
										dataTable.setInt(i, "l:Labels", 1);
										break;
									}
								}
							} else {
								for (int i = startPosition; i > 1; i--) {
									if (xPosition > dataTable.getFloat(i - 1, xData)) {
										dataTable.setInt(i, "l:Labels", 1);
										break;
									}
								}
							}
						} catch (Exception e) {
							println("FileGraph()::labels Unable to calculate correct label position");
							dataTable.setInt(startPosition, "l:Labels", 1);
						}
					} else {
						int startPosition = round(xPosition * graph.frequency());
						if (startPosition < 0) startPosition = 0;
						else if (startPosition >= dataTable.getRowCount()) startPosition = dataTable.getRowCount() - 1;
						dataTable.setInt(startPosition, "l:Labels", 1);
					}

					changesMade = true;
					redrawUI = true;
				}
			} 
			labelling = false;
			cursor(ARROW);
		} 

		else if (setZoomSize == 0) {
			if (graph.onGraph(xcoord, ycoord)) {
				zoomCoordOne[0] = (graph.xGraphPos(xcoord) * (graph.xMax() - graph.xMin())) + graph.xMin();
				zoomCoordOne[1] = ((1 - graph.yGraphPos(ycoord)) * (graph.yMax() - graph.yMin())) + graph.yMin();
				stroke(c_graph_axis);
				strokeWeight(1 * uimult);
				line(xcoord - (5 * uimult), ycoord, xcoord + (5 * uimult), ycoord);
				line(xcoord, ycoord - (5 * uimult), xcoord, ycoord + (5 * uimult));
				setZoomSize = 1;
			}

		} else if (setZoomSize == 1) {
			if (graph.onGraph(xcoord, ycoord)) {
				zoomCoordOne[2] = (graph.xGraphPos(xcoord) * (graph.xMax() - graph.xMin())) + graph.xMin();
				zoomCoordOne[3] = ((1 - graph.yGraphPos(ycoord)) * (graph.yMax() - graph.yMin())) + graph.yMin();
				setZoomSize = -1;
				zoomActive = true;

				if (zoomCoordOne[0] < zoomCoordOne[2]) {
					graph.xMin(floorToSigFig(zoomCoordOne[0], 4));
					graph.xMax(ceilToSigFig(zoomCoordOne[2], 4));
				} else {
					graph.xMax(ceilToSigFig(zoomCoordOne[0], 4));
					graph.xMin(floorToSigFig(zoomCoordOne[2], 4));
				}

				if (zoomCoordOne[1] < zoomCoordOne[3]) {
					graph.yMin(floorToSigFig(zoomCoordOne[1], 4));
					graph.yMax(ceilToSigFig(zoomCoordOne[3], 4));
				} else {
					graph.yMax(ceilToSigFig(zoomCoordOne[1], 4));
					graph.yMin(floorToSigFig(zoomCoordOne[3], 4));
				}

				redrawContent = true;
				redrawUI = true;
				cursor(ARROW);
			}
		}

		else cursor(ARROW);
	}


	/**
	 * Scroll wheel handler function
	 *
	 * @param  amount Multiplier/velocity of the latest mousewheel movement
	 */
	public void scrollWheel (float amount) {
		// Scroll menu bar
		if (mouseX >= cR && menuScroll != -1) {
			menuScroll += (sideItemHeight * amount * uimult);
			if (menuScroll < 0) menuScroll = 0;
			else if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
		}

		redrawUI = true;
	}


	/**
	 * Scroll bar handler function
	 *
	 * @param  xcoord Current mouse x-coordinate position
	 * @param  ycoord Current mouse y-coordinate position
	 */
	public void scrollBarUpdate (int xcoord, int ycoord) {
		if (sidebarScroll.active()) {
			int previousScroll = menuScroll;
			menuScroll = sidebarScroll.move(xcoord, ycoord, menuScroll, 0, menuHeight - (height - cT));
			if (previousScroll != menuScroll) redrawUI = true;
		}
	}


	/**
	 * Sidebar mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void menuClick (int xcoord, int ycoord) {

		// Coordinate calculation
		int sT = cT;
		int sL = cR;
		if (menuScroll > 0) sT -= menuScroll;
		if (menuScroll != -1) sL -= round(15 * uimult) / 4;
		final int sW = width - cR;
		final int sH = height - sT;

		final int uH = round(sideItemHeight * uimult);
		final int tH = round((sideItemHeight - 8) * uimult);
		final int iH = round((sideItemHeight - 5) * uimult);
		final int iL = round(sL + (10 * uimult));
		final int iW = round(sW - (20 * uimult));

		// Click on sidebar menu scroll bar
		if ((menuScroll != -1) && sidebarScroll.click(xcoord, ycoord)) {
			startScrolling(false);
		}

		// Root menu level
		if (menuLevel == 0) {

			// Open data
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, 1, iL, iW)){
				saveFilePath = false;
				outputfile = "";
				selectInput("Select *.CSV data file to open", "fileSelected");
			}

			// Save data
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 2, iL, iW)){
				if (currentfile != "" && currentfile != "No File Set" && changesMade){
					saveFilePath = true;
					outputfile = "";
					selectOutput("Select where to save the *.CSV file", "fileSelected");
				}
			}

			// Add label
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 4.5f, iL, iW)){
				if (currentfile != "" && currentfile != "No File Set"){
					labelling = true;
					cursor(CROSS);
				}
			}
			
			// Open the filters sub-menu
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 5.5f, iL, iW)){
				if (currentfile != "" && currentfile != "No File Set"){
					if (xData == -1 && dataSignals.size() == 1) {
						selectedSignal = 0;
						menuLevel = 2;
					} else if (xData != -1 && dataSignals.size() == 2) {
						if (xData == 0) selectedSignal = 1;
						else selectedSignal = 0;
						menuLevel = 2;
					} else menuLevel = 1;
					menuScroll = 0;
					redrawUI = true;
				}
			}

			// Change graph type
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 8, iL, iW)){

				// Line
				if (menuXclick(xcoord, iL, PApplet.parseInt(iW / 3))) {
					graph.graphType(Graph.LINE_CHART);
					redrawContent = redrawUI = true;
				}

				// Dot
				else if (menuXclick(xcoord, iL + PApplet.parseInt(iW / 3), PApplet.parseInt(iW / 3))) {
					graph.graphType(Graph.DOT_CHART);
					redrawContent = redrawUI = true;
				}

				// Bar
				else if (menuXclick(xcoord, iL + PApplet.parseInt(2 * iW / 3), PApplet.parseInt(iW / 3))) {
					graph.graphType(Graph.BAR_CHART);
					redrawContent = redrawUI = true;
				}
			}

			// Update X axis scaling
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 9, iL, iW)){

				// Change X axis minimum value
				if (menuXclick(xcoord, iL, (iW / 2) - PApplet.parseInt(6 * uimult))) {
					ValidateInput userInput = new ValidateInput("Set the X-axis Minimum Value", "Minimum:", str(graph.xMin()));
					userInput.setErrorMessage("Error\nInvalid x-axis minimum value entered.\nThe number should be smaller the the maximum value.");
					if (userInput.checkFloat(userInput.LT, graph.xMax())) {
						graph.xMin(userInput.getFloat());
						zoomActive = true;
					} 
					redrawContent = redrawUI = true;
				}

				// Change X axis maximum value
				else if (menuXclick(xcoord, iL + (iW / 2) + PApplet.parseInt(6 * uimult), (iW / 2) - PApplet.parseInt(6 * uimult))) {
					ValidateInput userInput = new ValidateInput("Set the X-axis Maximum Value", "Maximum:", str(graph.xMax()));
					userInput.setErrorMessage("Error\nInvalid x-axis maximum value entered.\nThe number should be larger the the minimum value.");
					if (userInput.checkFloat(userInput.GT, graph.xMin())) {
						graph.xMax(userInput.getFloat());
						zoomActive = true;
					} 
					redrawContent = redrawUI = true;
				}
			}

			// Update Y axis scaling
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 10, iL, iW)){

				// Change Y axis minimum value
				if (menuXclick(xcoord, iL, (iW / 2) - PApplet.parseInt(6 * uimult))) {
					ValidateInput userInput = new ValidateInput("Set the Y-axis Minimum Value", "Minimum:", str(graph.yMin()));
					userInput.setErrorMessage("Error\nInvalid y-axis minimum value entered.\nThe number should be smaller the the maximum value.");
					if (userInput.checkFloat(userInput.LT, graph.yMax())) {
						graph.yMin(userInput.getFloat());
						zoomActive = true;
					} 
					redrawContent = redrawUI = true;
				}

				// Change Y axis maximum value
				else if (menuXclick(xcoord, iL + (iW / 2) + PApplet.parseInt(6 * uimult), (iW / 2) - PApplet.parseInt(6 * uimult))) {
					ValidateInput userInput = new ValidateInput("Set the Y-axis Maximum Value", "Maximum:", str(graph.yMax()));
					userInput.setErrorMessage("Error\nInvalid y-axis maximum value entered.\nThe number should be larger the the minimum value.");
					if (userInput.checkFloat(userInput.GT, graph.yMin())) {
						graph.yMax(userInput.getFloat());
						zoomActive = true;
					} 
					redrawContent = redrawUI = true;
				}
			}

			// Zoom Options
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 11, iL, iW)){

				if (currentfile != "" && currentfile != "No File Set") {
					// New zoom
					if (menuXclick(xcoord, iL, iW / 2)) {
						if (setZoomSize >= 0) {
							setZoomSize = -1;
							cursor(ARROW);
							redrawContent = true;
							redrawUI = true;
						} else {
							setZoomSize = 0;
							cursor(CROSS);
							redrawUI = true;
						}
					}

					// Reset zoom
					else if (menuXclick(xcoord, iL + (iW / 2), iW / 2)) {
						zoomActive = false;
						cursor(ARROW);
						redrawContent = redrawUI = true;
					}
				}
			}

			// Change the input data rate
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 13.5f, iL, iW)){
				if (xData == -1) {
					ValidateInput userInput = new ValidateInput("Received Data Update Rate","Frequency (Hz):", str(graph.frequency()));
					userInput.setErrorMessage("Error\nInvalid frequency entered.\nThe rate can only be a number between 0 - 10,000 Hz");
					if (userInput.checkFloat(userInput.GT, 0, userInput.LTE, 10000)) {
						float newXrate = userInput.getFloat();
						graph.frequency(newXrate);
						redrawContent = true;
						redrawUI = true;
					}
				}
			}
			
			// Edit data column
			else {
				float tHnow = 14.5f;

				// List of Data Columns
				for(int i = 0; i < dataSignals.size(); i++){

					if (i != xData) {
						if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)){

							// Remove the signal
							if (menuXclick(xcoord, iL + iW - PApplet.parseInt(20 * uimult), PApplet.parseInt(20 * uimult))) {
								dataSignals.remove(i);
								dataTable.removeColumn(i);
								
								if (dataSignals.size() == 0 || (dataSignals.size() == 1 && xData != -1)) {
									currentfile = "No File Set";
									xData = -1;
									dataSignals.clear();
								}

								changesMade = true;
								redrawContent = true;
								redrawUI = true;
							}

							else if (menuXclick(xcoord, iL + iW - PApplet.parseInt(40 * uimult), PApplet.parseInt(20 * uimult))) {
								previousColor = dataSignals.get(i).signalColor;
								hueColor = previousColor;
								newColor = previousColor;
								colorSelector = i;

								menuLevel = 3;
								menuScroll = 0;
								redrawUI = true;
							}

							// Change name of column
							else {
								final String colname = myShowInputDialog("Set the Data Signal Name", "Name:", dataSignals.get(i).signalText);
								if (colname != null && colname != ""){
									dataSignals.get(i).signalText = colname;
									if (dataTable.getColumnTitle(i).contains("l:")) dataTable.setColumnTitle(i, "l:" + colname);
									else dataTable.setColumnTitle(i, colname);
									redrawUI = true;
								}
							}
						}

						tHnow++;
					}
				}
			}

		// Signal selection sub-menu
		} else if (menuLevel == 1) {
			float tHnow = 1;
			if (dataSignals.size() == 0) tHnow++;
			else {
				for (int i = 0; i < dataSignals.size(); i++) {
					if (i != xData && !dataTable.getColumnTitle(i).contains("l:")) {
						if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
							selectedSignal = i;
							menuLevel = 2;
							menuScroll = 0;
							redrawUI = true;
						}
						tHnow++;
					}
				}
			}

			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Filter selection sub-menu
		} else if (menuLevel == 2) {
			float tHnow = 1;
			Filters filterClass = new Filters();
			if (filterClass.filterList.length == 0) tHnow++;
			else {
				for (int i = 0; i < filterClass.filterList.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (!filterClass.filterList[i].contains("h:")) {
							workerActive = true;
							WorkerThread filterThread = new WorkerThread();
							filterThread.setFilterTask(i, selectedSignal);
							menuLevel = 0;
							menuScroll = 0;
							redrawUI = true;
							redrawContent = true;
						}
					}
					tHnow++;
				}
			}

			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}
		
		// Select a Colour
		} else if (menuLevel == 3) {

			// Colour hue selection
			if (menuXYclick(xcoord, ycoord, sT, uH, iW, 1, iL, iW)) {
				colorMode(HSB, iW, iW, iW);
				hueColor = color(mouseX - iL, mouseY - (sT + uH), iW);
				newColor = hueColor;
				colorMode(RGB, 255, 255, 255);
				redrawUI = true;

			// Colour brightness selection
			} else if (menuXYclick(xcoord, ycoord, sT + iW, uH, iH, 2.5f, iL, iW)) {
				if (mouseX > iL && mouseX < iL + (iW / 2)) {
					newColor = lerpColor(c_white, hueColor, (float) (mouseX - iL) / (iW / 2));
					redrawUI = true;
				} else if (mouseX > iL + (iW / 2) && mouseX < iL + iW) {
					newColor = lerpColor(hueColor, c_black, (float) (mouseX - (iL + iW / 2)) / (iW / 2));
					redrawUI = true;
				}

			// Submit button
			} else if (menuXYclick(xcoord, ycoord, sT + iW, uH, iH, 6.5f, iL, iW)) {
				dataSignals.get(colorSelector).signalColor = newColor;
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
				redrawContent = true;

			// Cancel button
			} else if (menuXYclick(xcoord, ycoord, sT + iW, uH, iH, 7.5f, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}
		}
	}


	/**
	 * Serial port data handler function
	 *
	 * @param  inputData New data received from the serial port
	 * @param  graphable True if data in message can be plotted on a graph
	 */
	public void parsePortData(String inputData, boolean graphable) {
		// Empty as this tab is not using serial comms 
	}


	/**
	 * Function called when a serial device has connected/disconnected
	 *
	 * @param  status True if a device has connected, false if disconnected
	 */
	public void connectionEvent (boolean status) {
		// Empty as this tab is not using serial comms
	}


	/**
	 * Background thread to take care of loading and processing operations
	 * without causing the program to appear to freeze
	 */
	class WorkerThread extends Thread {

		// Tasks: 0=load file, 1=apply filter
		int task = 0;
		int signal = 0;
		int filter = 0;

		/**
		 * Set up the worker to perform filtering
		 */
		public void setFilterTask(int filterType, int signalNumber) {
			workerActive = true;
			task = 1;
			filter = filterType;
			signal = signalNumber;
			super.start();
		}

		/**
		 * Set up the worker to load a file into memory
		 */
		public void loadFile() {
			workerActive = true;
			task = 0;
			super.start();
		}

		/**
		 * Perform the worker task
		 */
		public void run() {
			// Load a file
			if (task == 0) {
				dataSignals.clear();
				dataTable = loadTable(currentfile, "csv, header");
				for (int i = 0; i < dataTable.getColumnCount(); i++) {
					dataTable.setColumnType(i, Table.STRING);
				}
				workerActive = false;
				redrawContent = true;
				redrawUI = true;

			// Run a filter
			} else if (task == 1) {
				Filters filterClass = new Filters();

				double[] signalData = dataTable.getDoubleColumn(signal);
				double[] xAxisData;

				if (xData != -1) xAxisData = dataTable.getDoubleColumn(xData);
				else {
					xAxisData = new double[signalData.length];
					xAxisData[0] = 0;
					for (int i = 1; i < signalData.length; i++) {
						xAxisData[i] = xAxisData[i - 1] + (1.0f / graph.frequency());
					}
				}

				double[] outputData = filterClass.runFilter(filter, signalData, xAxisData, currentfile);

				if (outputData != null) {
					String signalName = filterClass.filterSlug[filter] + "[" + dataTable.getColumnTitle(signal) + "]";

					dataTable.addColumn(signalName, Table.STRING);
					int newColumnIndex = dataTable.getColumnIndex(signalName);

					for (int i = 0; i < dataTable.getRowCount(); i++) {
						dataTable.setDouble(i, newColumnIndex, outputData[i]);
					}
				}

				workerActive = false;
				redrawContent = true;
				redrawUI = true;
			}
		}
	}


	/**
	 * Check whether it is safe to exit the program
	 *
	 * @return True if the are no tasks active, false otherwise
	 */
	public boolean checkSafeExit() {
		return true;
	}


	/**
	 * End any active processes and safely exit the tab
	 */
	public void performExit() {
		// Nothing to do here
	}


	/**
	 * Data structure to store info related to each colour tag
	 */
	class DataSignal {
		public String signalText;
		public int signalColor;

		/**
		 * Constructor
		 * 
		 * @param  setText  The keyword text which is search for in the serial data
		 * @param  setColor The colour which all lines containing that text will be set
		 */
		DataSignal(String setText, int setColor) {
			signalText = setText;
			signalColor = setColor;
		}
	}
}
/* * * * * * * * * * * * * * * * * * * * * * *
 * FILTERS CLASS
 *
 * @file     Filters.pde
 * @brief    Algorithms to filter time-variant signals
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    Filters
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */




class Filters {

	// Filter list to be shown in the sidebar menu.
	// Entries starting with "h:" are headings
	String[] filterList = {"h:Noise Removal", 
	                            "Moving Average", 
	                            "1D Total Variance",
	                            "RC Low Pass",
	                            "RC High Pass",
	                       "h:Mathematical Functions", 
	                            "Absolute Value:  |x|", 
	                            "Squared:  x^2", 
	                            "Derivative:  Δx/dt", 
	                            "Integral:  Σxdt",
	                       "h:Signal Analysis",
	                   	        "Fourier Transform",
	                   	    	"Enclosed Area"};

	String[] filterSlug = {"h", "avg", "tv", "lp", "hp", "h", "abs", "squ", "Δ/dt", "Σdt", "h", "fft", "ea"};

	/**
	 * Default Constructor
	 */
	Filters() {
		// Empty
	}


	/**
	 * Run the specified filter on the provided data
	 */
	public double[] runFilter(int filterType, double[] signalData, double[] xAxisData, String currentFileLocation) {

		double[] outputData = null;

		switch (filterType) {
			// Moving average filter
			case 0:
			case 1: {
				ValidateInput userInput = new ValidateInput("Set Moving Average Filter Amount", "Number of Samples:", str(5));
				userInput.setErrorMessage("Error\nInvalid window size entered.\nThe window size should be an integer greater than 2.");
				if (userInput.checkInt(userInput.GTE, 2)) {
					int windowSize = userInput.getInt();
					MovingAverage avgFilter = new MovingAverage(windowSize);
					outputData = avgFilter.process(signalData);
				}
				break;
			}

			// 1D Total Variance denoiser 
			case 2: {
				ValidateInput userInput = new ValidateInput("Set Denoising Filter Amount", "Lambda Value:", str(1));
				userInput.setErrorMessage("Error\nInvalid filter value entered.\nThe Lambda value should be a number greater than 0.");
				if (userInput.checkDouble(userInput.GT, 0)) {
					double filterValue = userInput.getDouble();
					DenoiseTv1D denoiseFilter = new DenoiseTv1D();
					outputData = denoiseFilter.process(signalData, filterValue);
				}
				break;
			}

			// Low pass filter
			case 3: {
				ValidateInput userInput = new ValidateInput("Set the 3dB Cutoff Frequency", "Filter Frequency (Hz):", str(100));
				userInput.setErrorMessage("Error\nInvalid filter value entered.\nThe cutoff frequency should be a number above 0 Hz.");
				if (userInput.checkDouble(userInput.GT, 0)) {
					double filterValue = userInput.getDouble();
					RcLowPass lowPassFilter = new RcLowPass(filterValue);
					outputData = lowPassFilter.process(signalData, xAxisData);
				}
				break;
			}

			// High pass filter
			case 4: {
				ValidateInput userInput = new ValidateInput("Set the 3dB Cutoff Frequency", "Filter Frequency (Hz):", str(100));
				userInput.setErrorMessage("Error\nInvalid filter value entered.\nThe cutoff frequency should be a number above 0 Hz.");
				if (userInput.checkDouble(userInput.GT, 0)) {
					double filterValue = userInput.getDouble();
					RcHighPass highPassFilter = new RcHighPass(filterValue);
					outputData = highPassFilter.process(signalData, xAxisData);
				}
				break;
			}

			// Absolute value
			case 5:
			case 6: {
				outputData = new double[signalData.length];
				for (int i = 0; i < signalData.length; i++) {
					outputData[i] = Math.abs(signalData[i]);
				}
				break;
			}

			// Squared
			case 7: {
				outputData = new double[signalData.length];
				for (int i = 0; i < signalData.length; i++) {
					outputData[i] = Math.pow(signalData[i], 2);
				}
				break;
			}

			// Numerical Derivative using central difference
			case 8: {
				outputData = new double[signalData.length];
				outputData[0] = 0;
				outputData[signalData.length - 1] = 0;
				for (int i = 1; i < signalData.length  - 1; i++) {
					outputData[i] = (signalData[i + 1] - signalData[i - 1]) / (xAxisData[i + 1] - xAxisData[i - 1]);
				}
				break;
			}

			// Numerical Intgral 
			case 9: {
				outputData = new double[signalData.length];
				outputData[0] = 0;
				for (int i = 1; i < signalData.length; i++) {
					outputData[i] = outputData[i - 1] + (signalData[i] * (xAxisData[i] - xAxisData[i - 1]));
				}
				break;
			}

			// Fast Fourier Transform
			case 10:
			case 11: {
				double[] amplitudeAxis = null;
				double[] frequencyAxis = new double[signalData.length];
				double samplingFrequency = 0;

				// Figure out the sampling frequency
				for (int i = 0; i < xAxisData.length - 1; i++) {
					samplingFrequency += xAxisData[i+1] - xAxisData[i];
				}
				samplingFrequency /= xAxisData.length - 1;
				samplingFrequency = 1 / samplingFrequency;
				//println("freq: " + samplingFrequency);

				// Run the FFT
				FastFourierTransform fft = new FastFourierTransform();
				amplitudeAxis = fft.processForward(signalData, samplingFrequency, frequencyAxis);

				if (frequencyAxis == null || amplitudeAxis == null) {
					alertMessage("Fast Fourier Transform\nError: Unable to calculate the FFT");
				} else {
					double maxAmplitude = 0;
					int maxAmpIndex = 0;

					// Save the result to a file
					String[] lines = new String[amplitudeAxis.length + 1];
					lines[0] = "x:Frequency (Hz),Amplitude";
					for (int i = 0; i < amplitudeAxis.length; i++) {
						lines[i+1] = frequencyAxis[i] + "," + amplitudeAxis[i];

						if (maxAmplitude < amplitudeAxis[i]) {
							maxAmplitude = amplitudeAxis[i];
							maxAmpIndex = i;
						}
					}

					String newFileLocation = currentFileLocation.substring(0,currentFileLocation.length()-4) + "_fft.csv";
					saveStrings(newFileLocation, lines);
					DecimalFormat format = new DecimalFormat("0.#####");
					alertMessage("Fast Fourier Transform\nDominant frequency:   " + 
						format.format(frequencyAxis[maxAmpIndex]) + " Hz\n(Assuming time axis was in seconds)\n" + 
						"\nFull spectrogram is saved at:\n" + newFileLocation);
				}
				break;
			}

			// Enclosed Area Calculation
			case 12: {
				AreaCalculation areaCalc = new AreaCalculation();
				double[] calculatedAreas = areaCalc.processCycles(xAxisData, signalData);

				if (calculatedAreas == null || calculatedAreas.length < 1) {
					alertMessage("Enclosed Area Calculation\nError: No cycles were found in the data\nTherefore there are no enclosed areas");
				} else {
					double averageArea = 0;
					for (int i = 0; i < calculatedAreas.length; i++) averageArea += calculatedAreas[i];
					averageArea /= calculatedAreas.length;
					DecimalFormat format = new DecimalFormat("0.########");
					alertMessage("Enclosed Area Calculation\nCycles Detected:   " + calculatedAreas.length + "\nAverage Area:   " + format.format(averageArea));
				}
				break;
			}
		}

		return outputData;
	}


	/**
	 * Simple Moving Average (Sliding Window) Filter Class
	 */
	public class MovingAverage {
		private int size;
		private double samples[];
		private double total = 0;
		private int index = 0;
		private boolean bufferFilled = false;

		/**
		 * Constructor
		 *
		 * @param  windowSize The size of the sliding window
		 */
		public MovingAverage(int windowSize) {
			size = windowSize;
			samples = new double[size];
		}

		/**
		 * Add a new sample to the moving average
		 *
		 * @param  signalValue The value of the sample to add
		 */
		public void add(double signalValue) {
			total -= samples[index];
			samples[index] = signalValue;
			total += signalValue;
			index++;
			if (index == size) {
				bufferFilled = true;
				index = 0;
			}
		}

		/**
		 * Get the average value
		 *
		 * @return The moving average result
		 */
		public double getAverage() {
			if (bufferFilled) return total / size;
			return total / index;
		}

		/**
		 * Apply the filter to all the provided signal data at once
		 *
		 * @param  signalData Array containing the y-axis data to be filtered
		 * @return Output an array containing the filtered value for each step
		 */
		public double[] process(double[] signalData) {
			double[] outputData = new double[signalData.length];
			int samplesOffset = size / 2;

			// Calculate average, offsetting signal by half the window size to compensate filter delay
			for (int i = 0; i < signalData.length; i++) {
				add(signalData[i]);

				// Main filter portion, offset by half the window size
				if (i >= samplesOffset) {
					outputData[i - samplesOffset] = getAverage();
				}
			}

			// Gradually trail off the end, in same manner as is done to beginning
			reset();
			for (int i = signalData.length - 1; i >= signalData.length - size; i--) {
				add(signalData[i]);
				if (i < signalData.length - samplesOffset) {
					outputData[i + samplesOffset] = getAverage();
				}
			}

			return outputData;
		}

		/**
		 * Reset the filter
		 */
		public void reset() {
			total = 0;
			index = 0;
			bufferFilled = false;
			for (int i = 0; i < size; i++) samples[i] = 0;
		}
	}


	/**
	 * First-order RC Low Pass Filter
	 *
	 * @author    Simon Bluett
	 * @copyright GNU GPL-v3
	 */
	public class RcLowPass {
		private double timeConstant;
		private double lastOutput;
		private double lastXaxis;
		private boolean filterInitialised;

		/**
		 * Constructor
		 *
		 * @param cutoffFrequeuncy The filter 3dB cutoff frequency (Hz)
		 */
		public RcLowPass(double cutoffFrequency) {
			if (cutoffFrequency <= 0) cutoffFrequency = 1;
			timeConstant = 1 / (2 * Math.PI * cutoffFrequency);
			filterInitialised = false;
		}

		/**
		 * Calculate the next filter output, when provided with the realtime input
		 *
		 * @param  signalData The y-axis data to be filtered
		 * @param  xAxisData  The x-axis value for the current step
		 * @return The filtered output for the current step
		 */
		public double processStep(double signalData, double xAxisData) {
			double outputData;
			if (filterInitialised) {
				double alpha = (xAxisData - lastXaxis) / (timeConstant + (xAxisData - lastXaxis));
				outputData = lastOutput + alpha * (signalData - lastOutput);
			} else {
				outputData = signalData;
				filterInitialised = true;
			}
			lastOutput = outputData;
			lastXaxis = xAxisData;
			return outputData;
		}

		/**
		 * Apply the filter to all the provided signal data at once
		 *
		 * @param  signalData Array containing the y-axis data to be filtered
		 * @param  xAxisData  Array containing the x-axis value for each step
		 * @return Output an array containing the filtered value for each step
		 */
		public double[] process(double[] signalData, double[] xAxisData) {
			double[] outputData = new double[signalData.length];				
			for (int i = 0; i < signalData.length; i++) {
				outputData[i] = processStep(signalData[i], xAxisData[i]);
			}
			return outputData;
		}

		/**
		 * Reset the filter
		 */
		public void reset() {
			filterInitialised = false;
		}
	}


	/**
	 * First-order RC High Pass Filter
	 *
	 * @author    Simon Bluett
	 * @copyright GNU GPL-v3
	 */
	public class RcHighPass {
		private double timeConstant;
		private double lastOutput;
		private double lastInput;
		private double lastXaxis;
		private boolean filterInitialised;

		/**
		 * Constructor
		 *
		 * @param cutoffFrequeuncy The filter 3dB cutoff frequency (Hz)
		 */
		public RcHighPass(double cutoffFrequency) {
			if (cutoffFrequency <= 0) cutoffFrequency = 1;
			timeConstant = 1 / (2 * Math.PI * cutoffFrequency);
			filterInitialised = false;
		}

		/**
		 * Calculate the next filter output, when provided with the realtime input
		 *
		 * @param  signalData The y-axis data to be filtered
		 * @param  xAxisData  The x-axis value for the current step
		 * @return The filtered output for the current step
		 */
		public double processStep(double signalData, double xAxisData) {
			double outputData;
			if (filterInitialised) {
				double alpha = timeConstant / (timeConstant + (xAxisData - lastXaxis));
				outputData = alpha * (lastOutput + signalData - lastInput);
			} else {
				outputData = signalData;
				filterInitialised = true;
			}
			lastOutput = outputData;
			lastInput = signalData;
			lastXaxis = xAxisData;
			return outputData;
		}

		/**
		 * Apply the filter to all the provided signal data at once
		 *
		 * @param  signalData Array containing the y-axis data to be filtered
		 * @param  xAxisData  Array containing the x-axis value for each step
		 * @return Output an array containing the filtered value for each step
		 */
		public double[] process(double[] signalData, double[] xAxisData) {
			double[] outputData = new double[signalData.length];				
			for (int i = 0; i < signalData.length; i++) {
				outputData[i] = processStep(signalData[i], xAxisData[i]);
			}
			return outputData;
		}

		/**
		 * Reset the filter
		 */
		public void reset() {
			filterInitialised = false;
		}
	}


	/**
	 * 1D Total Variation (TV) Noise Removal Algorithm
	 *
	 * @author    Laurent Condat
	 * @copyright CeCILL Licence (compatible with GNU GPL v3)
	 * @note      The algorithm is based on the original C code by 
	 *            Laurent Condat with minor adaptations to encapsulate 
	 *            it in a class and make it work in Java
	 * @website   https://lcondat.github.io/software.html
	 */
	public class DenoiseTv1D {

		public double[] process(double[] input, final double lambda) {
	
			int width = input.length;
			int[] indstart_low = new int[width];
			int[] indstart_up = new int[width];
			double[] output = new double[width];

			int j_low = 0, j_up = 0, jseg = 0, indjseg = 0, i=1;
			int indjseg2, ind;
			double output_low_first = input[0] - lambda;
			double output_low_curr = output_low_first;
			double output_up_first = input[0] + lambda;
			double output_up_curr = output_up_first;
			final double twolambda = 2.0f * lambda;
			if (width == 1) {
				output[0] = input[0];
				return output;
			}
			indstart_low[0] = 0;
			indstart_up[0] = 0;
			width--;
			for (; i<width; i++) {
				if (input[i]>=output_low_curr) {
					if (input[i]<=output_up_curr) {
						output_up_curr+=(input[i]-output_up_curr)/(i-indstart_up[j_up]+1);
						output[indjseg]=output_up_first;
						while ((j_up>jseg)&&(output_up_curr<=output[ind=indstart_up[j_up-1]]))
							output_up_curr+=(output[ind]-output_up_curr)*
								((double)(indstart_up[j_up--]-ind)/(i-ind+1));
						if (j_up==jseg) {
							while ((output_up_curr<=output_low_first)&&(jseg<j_low)) {
								indjseg2=indstart_low[++jseg];
								output_up_curr+=(output_up_curr-output_low_first)*
									((double)(indjseg2-indjseg)/(i-indjseg2+1));
								while (indjseg<indjseg2) output[indjseg++]=output_low_first;
								output_low_first=output[indjseg];
							}
							output_up_first=output_up_curr;
							indstart_up[j_up=jseg]=indjseg;
						} else output[indstart_up[j_up]]=output_up_curr;
					} else 
						output_up_curr=output[i]=input[indstart_up[++j_up]=i];
					output_low_curr+=(input[i]-output_low_curr)/(i-indstart_low[j_low]+1);      
					output[indjseg]=output_low_first;
					while ((j_low>jseg)&&(output_low_curr>=output[ind=indstart_low[j_low-1]]))
						output_low_curr+=(output[ind]-output_low_curr)*
								((double)(indstart_low[j_low--]-ind)/(i-ind+1));	        		
					if (j_low==jseg) {
						while ((output_low_curr>=output_up_first)&&(jseg<j_up)) {
							indjseg2=indstart_up[++jseg];
							output_low_curr+=(output_low_curr-output_up_first)*
								((double)(indjseg2-indjseg)/(i-indjseg2+1));
							while (indjseg<indjseg2) output[indjseg++]=output_up_first;
							output_up_first=output[indjseg];
						}
						if ((indstart_low[j_low=jseg]=indjseg)==i) output_low_first=output_up_first-twolambda;
						else output_low_first=output_low_curr; 
					} else output[indstart_low[j_low]]=output_low_curr;
				} else {
					output_up_curr+=((output_low_curr=output[i]=input[indstart_low[++j_low] = i])-
						output_up_curr)/(i-indstart_up[j_up]+1);
					output[indjseg]=output_up_first;
					while ((j_up>jseg)&&(output_up_curr<=output[ind=indstart_up[j_up-1]]))
						output_up_curr+=(output[ind]-output_up_curr)*
								((double)(indstart_up[j_up--]-ind)/(i-ind+1));
					if (j_up==jseg) {
						while ((output_up_curr<=output_low_first)&&(jseg<j_low)) {
							indjseg2=indstart_low[++jseg];
							output_up_curr+=(output_up_curr-output_low_first)*
								((double)(indjseg2-indjseg)/(i-indjseg2+1));
							while (indjseg<indjseg2) output[indjseg++]=output_low_first;
							output_low_first=output[indjseg];
						}
						if ((indstart_up[j_up=jseg]=indjseg)==i) output_up_first=output_low_first+twolambda;
						else output_up_first=output_up_curr;
					} else output[indstart_up[j_up]]=output_up_curr;
				}
			}
			/* here i==width (with value the actual width minus one) */
			if (input[i]+lambda<=output_low_curr) {
				while (jseg<j_low) {
					indjseg2=indstart_low[++jseg];
					while (indjseg<indjseg2) output[indjseg++]=output_low_first;
					output_low_first=output[indjseg];
				}
				while (indjseg<i) output[indjseg++]=output_low_first;
				output[indjseg]=input[i]+lambda;
			} else if (input[i]-lambda>=output_up_curr) {
				while (jseg<j_up) {
					indjseg2=indstart_up[++jseg];
					while (indjseg<indjseg2) output[indjseg++]=output_up_first;
					output_up_first=output[indjseg];
				}
				while (indjseg<i) output[indjseg++]=output_up_first;
				output[indjseg]=input[i]-lambda;
			} else {
				output_low_curr+=(input[i]+lambda-output_low_curr)/(i-indstart_low[j_low]+1);      
				output[indjseg]=output_low_first;
				while ((j_low>jseg)&&(output_low_curr>=output[ind=indstart_low[j_low-1]]))
					output_low_curr+=(output[ind]-output_low_curr)*
								((double)(indstart_low[j_low--]-ind)/(i-ind+1));	        		
				if (j_low==jseg) {
					if (output_up_first>=output_low_curr)
						while (indjseg<=i) output[indjseg++]=output_low_curr;
					else {
						output_up_curr+=(input[i]-lambda-output_up_curr)/(i-indstart_up[j_up]+1);
						output[indjseg]=output_up_first;
						while ((j_up>jseg)&&(output_up_curr<=output[ind=indstart_up[j_up-1]]))
							output_up_curr+=(output[ind]-output_up_curr)*
								((double)(indstart_up[j_up--]-ind)/(i-ind+1));
						while (jseg<j_up) {
							indjseg2=indstart_up[++jseg];
							while (indjseg<indjseg2) output[indjseg++]=output_up_first;
							output_up_first=output[indjseg];
						}
						indjseg=indstart_up[j_up];
						while (indjseg<=i) output[indjseg++]=output_up_curr;
					}
				} else {
					while (jseg<j_low) {
						indjseg2=indstart_low[++jseg];
						while (indjseg<indjseg2) output[indjseg++]=output_low_first;
						output_low_first=output[indjseg];
					}
					indjseg=indstart_low[j_low];
					while (indjseg<=i) output[indjseg++]=output_low_curr;
				}
			}
			return output;
		}
	}


	/**
	 * Fast Fourier Transform (FFT) to analyse frequency response of a signal
	 *
	 * @author    Numerical Recipes chapter on FFTs
	 * @copyright Apache Licence (compatible with GNU GPL v3)
	 */
	public class FastFourierTransform {

		public double[] processForward(double[] signalData, double samplingFrequency, double[] frequencyAxis)
		{
			// Figure out length of FFT (it must be a factor of 2)
			int signalLength = signalData.length;
			int pt = signalLength;
			pt--;
			pt |= pt >> 1;
			pt |= pt >> 2;
			pt |= pt >> 4;
			pt |= pt >> 8;
			pt |= pt >> 16;
			pt++;

			// Generate the complex data array
			double[] complexArray = new double[pt * 2];
			for (int i = 0; i < pt * 2; i++) complexArray[i] = 0;
			for (int i = 0; i < signalLength; i++) complexArray[i * 2] = signalData[i];

			// Calculate the FFT
			fft(complexArray, pt, 1);

			// Get the amplitude of the complex number
			double[] amplitudeArray = new double[signalLength];
			for (int i = 0; i < signalLength; i++) {
				amplitudeArray[i] = java.lang.Math.sqrt((complexArray[i * 2] * complexArray[i * 2]) + (complexArray[i*2 + 1] * complexArray[i*2 + 1]));
			}

			// Generate the frequency axis
			//frequencyAxis = new double[signalLength];
			if (frequencyAxis != null && frequencyAxis.length >= signalData.length) {
				for (int i = 0; i < signalLength; i++) frequencyAxis[i] = i * samplingFrequency / pt;
			}

			return amplitudeArray;
		}
		

		public void fft(double[] data, int noOfSamples, int direction)
		{
			//variables for trigonometric recurrences
			int i, j, n, m, mmax, istep;
			double wr, wpr, wi, wpi, wtemp, tempr, tempi, theta;

			// The complex array is real+complex so the array has a 
			// size n = 2* number of complex samples. The real part 
			// is the data[index] and the complex part is the data[index+1]
			n = noOfSamples * 2;

			// Binary inversion (real = even-indexes, complex = odd-indexes)
			j = 0;

			for (i = 0; i < n / 2; i += 2) {
				if (j > i) {

					// Swap the real part
					tempr = data[j];
					data[j] = data[i];
					data[i] = tempr;

					// Swap the complex part
					tempr = data[j+1];
					data[j+1] = data[i+1];
					data[i+1] = tempr;

					// Checks if the changes occurs in the first half
					// and use the mirrored effect on the second half
					if ((j / 2) < (n / 4)) {
						// Swap the real part
						tempr = data[(n - (i + 2))];
						data[(n - (i + 2))] = data[(n - (j + 2))];
						data[(n - (j + 2))] = tempr;

						// Swap the complex part
						tempr = data[(n - (i + 2)) + 1];
						data[(n - (i + 2)) + 1] = data[(n - (j + 2)) + 1];
						data[(n - (j + 2)) + 1] = tempr;
					}
				}

				m = n / 2;

				while (m >= 2 && j >= m) {
					j -= m;
					m = m / 2;
				}
				j += m;

				//print(i); print(" , "); println(j);
			}

			// Danielson-Lanzcos routine
			mmax = 2;

			// External loop
			while (n > mmax) {
				istep = mmax << 1;
				theta = direction * (2 * PI / mmax);
				wtemp = java.lang.Math.sin(0.5f * theta);
				wpr = -2.0f * wtemp * wtemp;
				wpi = java.lang.Math.sin(theta);
				wr = 1.0f;
				wi = 0.0f;

				// Internal loops
				for (m = 1; m < mmax; m += 2) {
					for (i = m; i <= n; i += istep) {
						j = i + mmax;
						tempr = wr * data[j-1] - wi * data[j];
						tempi = wr * data[j] + wi * data[j-1];
						data[j-1] = data[i-1] - tempr;
						data[j] = data[i] - tempi;
						data[i-1] += tempr;
						data[i] += tempi;
					}
					wr = (wtemp = wr) * wpr - wi * wpi + wr;
					wi = wi * wpr + wtemp * wpi + wi;
				}
				mmax = istep;
			}
		}
	}


	/**
	 * Enclosed Area Calculation
	 *
	 * @author    Simon Bluett
	 * @copyright GNU GPL-v3
	 */
	public class AreaCalculation {

		/**
		 * Calculate the area of enclosed cycles/loops in the data
		 * @param[in]  xData  X-axis data
		 * @param[in]  yData  Y-axis data
		 */
		public double[] processCycles(double[] xData, double[] yData) {

			// Detect cycles in the data, which correspond to enclosed areas
			int[] cycleEnd = detectCycles(xData, yData);
			double[] calculatedArea = null;
			//println("CycleEnd.length = " + cycleEnd.length);

			if ((cycleEnd != null) && (cycleEnd.length > 1)) {
				calculatedArea = new double[0];

				for (int i = 1; i < cycleEnd.length; i++) {

					double currentArea = 0;

					// Apply the discrete version of Green's Theorem (Pick's Theorem)
					// Based on formula in DOI: 10.1117/1.JEI.26.6.063022
					for (int j = cycleEnd[i - 1]; j < cycleEnd[i] - 1; j++) {
						double areaSlice = ((xData[j] * yData[j+1]) - (yData[j] * xData[j + 1])) / 2.0f;
						currentArea += areaSlice;
						//println(areaSlice);
					}

					// Ensure the cycle is closed
					currentArea += ((xData[cycleEnd[i]-1] * yData[cycleEnd[i - 1]]) - (yData[cycleEnd[i] - 1] * xData[cycleEnd[i - 1]])) / 2.0f;
					currentArea = java.lang.Math.abs(currentArea);
					calculatedArea = (double[])append(calculatedArea, currentArea);
					//println(currentArea);
				}
			}

			return calculatedArea;
		}

		/**
		 * Detect whether the data forms a closed loop
		 * @param[in]  xData  X-axis data
		 * @param[in]  yData  Y-axis data
		 * @return     An array of the indices where one cycle ends and a new one starts
		 */
		public int[] detectCycles(double[] xData, double[] yData) {

			if (xData == null || yData == null || (xData.length != yData.length) || xData.length < 3) return null;

			int[] cycleEnd = { 0 };
			double[] startPoint = { xData[0], yData[0] };
			double avgStepDistance = distance(xData[0], yData[0], xData[1], yData[1]);
			double avgXstep = java.lang.Math.abs(xData[1] - xData[0]);
			double avgYstep = java.lang.Math.abs(yData[1] - yData[0]);

			for (int i = 2; i < xData.length; i++) {
				// Update the average distance between points
				avgStepDistance = ((avgStepDistance * i) + distance(xData[i-1], yData[i-1], xData[i], yData[i])) / (double)(i + 1);
				avgXstep = ((avgXstep * i) + java.lang.Math.abs(xData[i] - xData[i-1])) / (double)(i + 1);
				avgYstep = ((avgYstep * i) + java.lang.Math.abs(yData[i] - yData[i-1])) / (double)(i + 1);

				// If the current point is closer to the start point than half the
				// average distance, then a full loop has probably been completed
				double currentDistance = distance(startPoint[0], startPoint[1], xData[i], yData[i]);
				if  (
					  (currentDistance < avgStepDistance * 0.5f) 
					  && (java.lang.Math.abs(xData[i] - startPoint[0]) < avgXstep * 0.5f)
					  && (java.lang.Math.abs(yData[i] - startPoint[1]) < avgYstep * 0.5f)
					) {
					cycleEnd = append(cycleEnd, i);
					startPoint[0] = xData[i];
					startPoint[1] = yData[i];
					//println("Index: " + i + ", Avg Step: " + avgStepDistance + ", Dist: " + currentDistance + ", Avg X: " + avgXstep);

					// Increment twice to prevent the next point from triggering a loop detection
					i++;
				}
			}

			return cycleEnd;
		}

		/**
		 * Calculate the distance between two coordinates
		 * @param[in]  x1  X-value of point A
		 * @param[in]  y1  Y-value of point A
		 * @param[in]  x2  X-value of point B
		 * @param[in]  y2  Y-value of point B
		 * @return     The euclidean distance between two points
		 */
		private double distance(double x1, double y1, double x2, double y2) {
			return java.lang.Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
		}
	}
}
/**
 * Helper Functions for Java FX Popup Dialogues
 *
 * @file  FxDialogs.pde
 */







//import javafx.scene.input.KeyEvent;












/**
 * @class Java FX Pop-up Dialogues
 */
public static class FxDialogs {

    public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Warning");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void showException(String title, String message, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Exception");
        alert.setHeaderText(title);
        alert.setContentText(message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Details:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";

    public static String showConfirm(String title, String message, String... options) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Choose an option");
        alert.setHeaderText(title);
        alert.setContentText(message);

        //To make enter key press the actual focused button, not the first one. Just like pressing "space".
        /*
        alert.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                event.consume();
                try {
                    Robot r = new Robot();
                    r.keyPress(java.awt.event.KeyEvent.VK_SPACE);
                    r.keyRelease(java.awt.event.KeyEvent.VK_SPACE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        if (options == null || options.length == 0) {
            options = new String[]{OK, CANCEL};
        }

        List<ButtonType> buttons = new ArrayList<ButtonType>();
        for (String option : options) {
            buttons.add(new ButtonType(option));
        }

        alert.getButtonTypes().setAll(buttons);

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            return CANCEL;
        } else {
            return result.get().getText();
        }
    }

    public static String showTextInput(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Input");
        dialog.setHeaderText(title);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }

    }

}
/* * * * * * * * * * * * * * * * * * * * * * *
 * GRAPH CLASS
 *
 * @file     Graph.pde
 * @brief    Class to draw graphs in Processing
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    Graph
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

class Graph {

	static public final int LINE_CHART = 0;
	static public final int DOT_CHART = 1;
	static public final int BAR_CHART = 2;

	// Content coordinates (left, right, top bottom)
	int cL, cR, cT, cB;
	int gL, gR, gT, gB;                // Graph area coordinates
	float gridX, gridY;                // Grid spacing
	float offsetLeft, offsetBottom;

	float minX, maxX, minY, maxY;      // Limits of data
	float[] lastX = {0};               // Arrays containing previous x and y values
	float[] lastY = {Float.MIN_VALUE};
	float xStep;

	float scaleFactor;
	float xRate;
	int plotType;
	String plotName;
	String xAxisName;
	//String yAxisName;

	// Ui variables
	int graphMark;
	int border;
	boolean redrawGraph;
	boolean gridLines;
	boolean squareGrid;
	boolean highlighted;

	PGraphics graphics;


	/**
	 * Constructor
	 *
	 * @param  canvas  The graphics canvas drawing object
	 * @param  left    Graph area left x-coordinate
	 * @param  right   Graph area right x-coordinate
	 * @param  top     Graph area top y-coordinate
	 * @param  bottom  Graph area bottom y-coordinate
	 * @param  minx    Minimum X-axis value on graph
	 * @param  maxx    Maximum X-axis value on graph
	 * @param  miny    Minimum Y-axis value on graph
	 * @param  maxy    Maximum Y-axis value on graph
	 * @param  name    Name/title of the graph
	 */
	Graph(PGraphics canvas, int left, int right, int top, int bottom, float minX, float maxX, float minY, float maxY, String name)
	{
		graphics = g;
		plotName = name;

		this.cL = left;
		this.gL = left;
		this.cR = right;
		this.gR = right;
		this.cT = top;
		this.gT = top;
		this.cB = bottom;
		this.gB = bottom;

		gridX = 0;
		gridY = 0;
		offsetLeft = cL;
		offsetBottom = cT;

		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;

		scaleFactor = 1;
		xRate = 100;
		xStep = 1.0f / xRate;

		graphMark = round(8 * scaleFactor);
		border = round(30 * scaleFactor);

		plotType = 0;
		redrawGraph = gridLines = true;
		squareGrid = false;
		highlighted = false;

		xAxisName = "";
		//yAxisName = "";
	}


	Graph(PGraphics canvas, int graphWidth, int graphHeight, float minX, float maxX, float minY, float maxY, String name)
	{
		this(g, 0, graphWidth, 0, graphHeight, minX, maxX, minY, maxY, name);
	}

	Graph(int left, int right, int top, int bottom, float minX, float maxX, float minY, float maxY, String name)
	{
		this(g, left, right, top, bottom, minX, maxX, minY, maxY, name);
	}

	Graph(int graphWidth, int graphHeight, float minX, float maxX, float minY, float maxY, String name)
	{
		this(g, 0, graphWidth, 0, graphHeight, minX, maxX, minY, maxY, name);
	}

	Graph(PGraphics canvas, int left, int right, int top, int bottom)
	{
		this(canvas, left, right, top, bottom, 0, 20, 0, 1, "Graph");
	}

	Graph(int left, int right, int top, int bottom)
	{
		this(g, left, right, top, bottom, 0, 20, 0, 1, "Graph");
	}

	Graph(PGraphics canvas, int graphWidth, int graphHeight)
	{
		this(canvas, 0, graphWidth, 0, graphHeight, 0, 20, 0, 1, "Graph");
	}

	Graph(int graphWidth, int graphHeight)
	{
		this(g, 0, graphWidth, 0, graphHeight, 0, 20, 0, 1, "Graph");
	}


	/**
	 * Change the graphics buffer onto which to draw the graph
	 *
	 * @param  newGraphics The processing graphics object to use
	 */
	public void canvas(PGraphics newGraphics)
	{
		if (newGraphics != null)
		{
			graphics = newGraphics;
		}
	}


	/**
	 * Read which graphics buffer graph is currently being drawn onto
	 * 
	 * @return The current graphics buffer canvas
	 */
	public PGraphics canvas()
	{
		return graphics;
	}
	

	/**
	 * Change graph content area dimensions
	 *
	 * @param  newL New left x-coordinate
	 * @param  newR New right x-coordinate
	 * @param  newT New top y-coordinate
	 * @param  newB new bottom y-coordinate
	 */
	public void setSize(int newL, int newR, int newT, int newB)
	{
		cL = newL;
		cR = newR;
		cT = newT;
		cB = newB;
		graphMark = round(8 * scaleFactor);
		border = round(30 * scaleFactor);

		for(int i = 0; i < lastX.length; i++) lastX[i] = 0;
		redrawGraph = true;
	}


	/**
	 * Set the chart interface scaling factor
	 * 
	 * @param  factor  The scaling multiplication factor
	 */
	public void scale(float factor)
	{
		if (factor > 0)
		{
			this.scaleFactor = factor;
		}
	}


	 /**
	  * Get the current chart interface scaling factor
	  * 
	  * @return  The chart scaling multiplication factor
	  */
	public float scale()
	{
		return scaleFactor;
	}


	/**
	 * Get the data rate relating to Y-axis spacing
	 * of the live serial data graph
	 *
	 * @return Data rate in samples per second
	 */
	public float frequency()
	{
		return xRate;
	}


	/**
	 * Set the data rate relating to Y-axis spacing
	 * of the live serial data graph
	 *
	 * @param  newrate Data rate in samples per second
	 * @return True if update is successful, false it number is invalid
	 */
	public boolean frequency(float newrate)
	{
		if (valid(newrate) && (newrate > 0) && (newrate <= 10000))
		{
			xRate = newrate;
			xStep = 1.0f / xRate;
			return true;
		}
		else
		{
			println("Graph::setFrequency() - Invalid number: " + newrate);
			return false;
		}
	}


	/**
	 * Check if the X- and Y- graph axes are set to be the same scale
	 *
	 * @return  True/false to enable equal graph axes
	 */
	public boolean getSquareGrid()
	{
		return squareGrid;
	}


	/**
	 * Set the X- and Y- graph axes to the same scale
	 *
	 * @param  value True/false to enable equal graph axes
	 */
	public void setSquareGrid(boolean value)
	{
		squareGrid = value;
	}


	/**
	 * Set the title label for the X-axis
	 *
	 * @param  newName The text to be displayed beside the axis
	 */
	public void xAxisTitle(String newName)
	{
		xAxisName = newName;
	}


	/**
	 * Get the current title label for the x-axis
	 * 
	 * @return  The current x-axis title text
	 */
	public String xAxisTitle()
	{
		return xAxisName;
	}


	/**
	 * Set the name label for the Y-axis
	 *
	 * @param  newName The text to be displayed beside the axis
	 */
	//void setYaxisName(String newName) {
	//	yAxisName = newName;
	//}


	/**
	 * Set all the minimum and maximum limits of the graph axes
	 * 
	 * @param  minX  Minimum x-axis value
	 * @param  maxX  Maximum x-axis value
	 * @param  minY  Minimum y-axis value
	 * @param  maxY  Maximum y-axis value
	 */
	public void limits(float minX, float maxX, float minY, float maxY)
	{
		if (valid(minX)) this.minX = minX;
		if (valid(maxX)) this.maxX = maxX;
		if (valid(minY)) this.minY = minY;
		if (valid(maxY)) this.maxY = maxY;
	}

	/**
	 * Set the minimum X-axis value
	 *
	 * @param  newValue The new x-axis minimum value
	 * @return True if update is successful, false if newValue is invalid
	 */
	public boolean xMin(float newValue)
	{
		if (valid(newValue) && newValue < maxX)
		{
			minX = newValue;
			return true;
		}
		return false;
	}


	/**
	 * Set the maximum X-axis value
	 *
	 * @param  newValue The new x-axis maximum value
	 * @return True if update is successful, false if newValue is invalid
	 */
	public boolean xMax(float newValue)
	{
		if (valid(newValue) && newValue > minX)
		{
			maxX = newValue;
			return true;
		}
		return false;
	}


	/**
	 * Set the minimum Y-axis value
	 *
	 * @param  newValue The new y-axis minimum value
	 * @return True if update is successful, false if newValue is invalid
	 */
	public boolean yMin(float newValue)
	{
		if (valid(newValue) && newValue < maxY)
		{
			minY = newValue;
			return true;
		}
		return false;
	}


	/**
	 * Set the maximum Y-axis value
	 *
	 * @param  newValue The new y-axis maximum value
	 * @return True if update is successful, false if newValue is invalid
	 */
	public boolean yMax(float newValue)
	{
		if (valid(newValue) && newValue > minY)
		{
			maxY = newValue;
			return true;
		}
		return false;
	}


	/**
	 * Functions to get the range of the X- or Y- axes
	 *
	 * @return The new minimum or maximum range value
	 */	
	public float xMin() { return minX; }
	public float xMax() { return maxX; }
	public float yMin() { return minY; }
	public float yMax() { return maxY; }


	/**
	 * Reset graph 
	 */
	public void resetGraph(){
		for(int i = 0; i < lastX.length; i++) lastX[i] = -xStep;
		for(int i = 0; i < lastY.length; i++) lastY[i] = Float.MIN_VALUE;
	}


	/**
	 * Reset and remove all saved data
	 */
	public void reset() {
		lastX = new float[0];
		lastY = new float[0];
	}


	/**
	 * Test whether a float number is valid
	 *
	 * @param  newNumber The number to test
	 * @return True if valid, false is number is NaN or Infinity
	 */
	private boolean valid(float newNumber)
	{
		if ((newNumber != newNumber) || (newNumber == Float.POSITIVE_INFINITY) || (newNumber == Float.NEGATIVE_INFINITY))
		{
			return false;
		}
		return true;
	}


	/**
	 * Draw a X-axis label onto the graph
	 * @{
	 * @param  xCoord       The X-coordinate on the screen
	 * @param  signalColor  The colour in which the label should be drawn
	 * @return The X-axis value of the label position on the graph
	 */
	public float xLabel(int xCoord, int signalColor)
	{
		if (xCoord < gL || xCoord > gR) return 0;

		graphics.stroke(signalColor);
		graphics.strokeWeight(1 * scaleFactor);

		graphics.line(xCoord, gT, xCoord, gB);
		return map(xCoord, gL, gR, minX, maxX);
	}


	/**
	 * Draw a X-axis label onto the graph
	 *
	 * @param  dataX        The x-axis position of the label
	 * @param  signalColor  The colour in which the label should be drawn
	 */
	public void xLabel(float dataX, int signalColor)
	{
		if (dataX >= minX && dataX <= maxX)
		{
			graphics.stroke(signalColor);
			graphics.strokeWeight(1 * scaleFactor);

			graphics.line(map(dataX, minX, maxX, gL, gR), gT, map(dataX, minX, maxX, gL, gR), gB);
		}
	}
	/** @} */


	/**
	 * Change the graph display type
	 *
	 * @param  type  The graph type to display
	 */
	public void graphType(int type)
	{
		if (type >= 0 && type <= 2)
		{
			plotType = type;
		}
	}


	/**
	 * Get the type of graph which is currently being displayed
	 *
	 * @return  The graph type being displayed
	 */
	public int graphType()
	{
		return plotType;
	}


	/**
	 * Show that current graph is active
	 *
	 * This function changes the colour of the graph name/title text
	 * to show that it has been selected.
	 *
	 * @param  state  Whether graph is selected or not
	 */
	public void selected(boolean state)
	{
		highlighted = state;
	}


	/**
	 * See if the current graph is selected
	 * 
	 * @return  True if graph is selected, false otherwise
	 */
	public boolean selected()
	{
		return highlighted;
	}


	/**
	 * Check if a window coordinate is in the graph area
	 *
	 * @param  xCoord The window X-coordinate
	 * @param  yCoord The window Y-coordinate
	 * @return True if coordinate is within the content area
	 */
	public boolean onGraph(int xCoord, int yCoord)
	{
		if (xCoord >= cL && xCoord <= cR && yCoord >= cT && yCoord <= cB) return true;
		else return false;
	}


	/**
	 * Convert window coordinate into X-axis graph value
	 *
	 * @param  xCoord The window X-coordinate
	 * @return The graph X-axis value at this X-coordinate
	 */
	public float xGraphPos(int xCoord)
	{
		if (xCoord < gL) xCoord = gL;
		else if (xCoord > gR) xCoord = gR;
		return map(xCoord, gL, gR, 0, 1);
	}


	/**
	 * Convert window coordinate into Y-axis graph value
	 *
	 * @param  yCoord The window Y-coordinate
	 * @return The graph Y-axis value at this Y-coordinate
	 */
	public float yGraphPos(int yCoord)
	{
		if (yCoord < gT) yCoord = gT;
		else if (yCoord > gB) yCoord = gB;
		return map(yCoord, gT, gB, 0, 1);
	}


	/**
	 * Plot a new data point on the graph
	 *
	 * @param  dataY The Y-axis value of the data
	 * @param  dataX The X-axis value of the data
	 * @param  type  The signal ID/number
	 * @param  signalColor The colour which to draw the signal
	 * @{
	 */
	public void plotData(float dataX, float dataY, int type, int signalColor)
	{

		if (valid(dataY) && valid(dataX)) {

			int x1, y1, x2 = gL, y2;

			// Ensure that the element actually exists in data arrays
			while(lastY.length < type + 1) lastY = append(lastY, Float.MIN_VALUE);
			while(lastX.length < type + 1) lastX = append(lastX, 0);
						
			// Redraw grid, if required
			if (redrawGraph) drawGrid();

			// Bound the Y-axis data
			if (dataY > maxY) dataY = maxY;
			if (dataY < minY) dataY = minY;

			// Bound the X-axis
			if (dataX > maxX) dataX = maxX;
			if (dataX < minX) dataX = minX;

			// Set colours
			graphics.fill(signalColor);
			graphics.stroke(signalColor);
			graphics.strokeWeight(1 * scaleFactor);

			switch(plotType)
			{
				case DOT_CHART:
				{
					// Determine x and y coordinates
					x2 = round(map(dataX, minX, maxX, gL, gR));
					y2 = round(map(dataY, minY, maxY, gB, gT));
					
					graphics.ellipse(x2, y2, 1*scaleFactor, 1*scaleFactor);
					break;
				}

				case BAR_CHART:
				{
					if (lastY[type] != Float.MIN_VALUE && lastY[type] != Float.MAX_VALUE) {
						graphics.noStroke();

						// Determine x and y coordinates
						x1 = round(map(lastX[type], minX, maxX, gL, gR));
						x2 = round(map(dataX, minX, maxX, gL, gR));
						y1 = round(map(dataY, minY, maxY, gB, gT));
						if (minY <= 0) y2 = round(map(0, minY, maxY, gB, gT));
						else y2 = round(map(minY, minY, maxY, gB, gT));

						// Figure out how wide the bar should be
						final int oneSegment = ceil((x2 - x1) / PApplet.parseFloat(lastX.length));
						x1 += oneSegment * type;
						if (lastX.length > 1) x2 = x1 + oneSegment;
						else x2 = x1 + ceil(oneSegment / 1.5f);
						
						graphics.rectMode(CORNERS);
						graphics.rect(x1, y1, x2, y2);
					}
					break;
				}

				case LINE_CHART:
				default: 
				{
					// Only draw line if last value is set
					if (lastY[type] != Float.MIN_VALUE) {
						// Determine x and y coordinates
						x1 = round(map(lastX[type], minX, maxX, gL, gR));
						x2 = round(map(dataX, minX, maxX, gL, gR));
						y1 = round(map(lastY[type], minY, maxY, gB, gT));
						y2 = round(map(dataY, minY, maxY, gB, gT));
						graphics.line(x1, y1, x2, y2);
					}
					break;
				}
			}
			
			lastY[type] = dataY;
			lastX[type] = dataX;
		}
	}


	/**
	 * Plot a new data point using default y-increment
	 *
	 * @note   This is an overload function
	 * @see    void plotData(float, float, int)
	 */
	public void plotData(float dataY, int type) {
	
		// Ensure that the element actually exists in data arrays
		while(lastY.length < type + 1) lastY = append(lastY, Float.MIN_VALUE);
		while(lastX.length < type + 1) lastX = append(lastX, -xStep);
		plotData(lastX[type] + xStep, dataY, type);
	}


	/**
	 * Plot a new data point using default y-increment
	 *
	 * @note   This is an overload function
	 * @see    void plotData(float, float, int, color)
	 */
	public void plotData(float dataX, float dataY, int type) {
		int colorIndex = type - (c_colorlist.length * floor(type / c_colorlist.length));
		plotData(dataX, dataY, type, c_colorlist[colorIndex]);
	}
	/** @} */	


	/**
	 * Plot a rectangle on the graph
	 *
	 * @param  dataY1 Y-axis value of top-left point
	 * @param  dataY2 Y-axis value of bottom-right point
	 * @param  dataX1 X-axis value of top-left point
	 * @param  dataX2 X-axis value of bottom-right point
	 * @param  type   The signal ID/number (this determines the colour)
	 */
	public void plotRectangle(float dataY1, float dataY2, float dataX1, float dataX2, int areaColor) {

		// Only plot data if it is within bounds
		if (
			(dataY1 >= minY) && (dataY1 <= maxY) && 
			(dataY2 >= minY) && (dataY2 <= maxY) &&
			(dataX1 >= minX) && (dataX1 <= maxX) && 
			(dataX2 >= minX) && (dataX2 <= maxX)
		   )
		{
			// Get relevant color from list
			graphics.fill(areaColor);
			graphics.stroke(areaColor);
			graphics.strokeWeight(1 * scaleFactor);

			// Determine x and y coordinates
			final int x1 = round(map(dataX1, minX, maxX, gL, gR));
			final int x2 = round(map(dataX2, minX, maxX, gL, gR));
			final int y1 = round(map(dataY1, minY, maxY, gB, gT));
			final int y2 = round(map(dataY2, minY, maxY, gB, gT));
			
			graphics.rectMode(CORNERS);
			graphics.rect(x1, y1, x2, y2);
		}
	}


	/**
	 * Clear the graph area and redraw the grid lines
	 */
	public void clearGraph()
	{
		// Clear the content area
		graphics.rectMode(CORNER);
		graphics.noStroke();
		graphics.fill(c_background);
		graphics.rect(gL, gT - (scaleFactor * 1), gR - gL + (scaleFactor * 1), gB - gT + (scaleFactor * 2));

		// Setup drawing parameters
		graphics.strokeWeight(1 * scaleFactor);
		graphics.stroke(c_graph_gridlines);

		// Draw X-axis grid lines
		if (gridLines && gridX != 0) {
			for (float i = offsetLeft; i < gR; i += gridX) {
				graphics.line(i, gT, i, gB);
			}
		}

		// Draw y-axis grid lines
		if (gridLines && gridY != 0) {
			for (float i = offsetBottom; i > gT; i -= gridY) {
				graphics.line(gL, i, gR, i);
			}
		}

		float yZero = 0;
		float xZero = 0;
		if      (minY > 0) yZero = minY;
		else if (maxY < 0) yZero = maxY;
		if      (minX > 0) xZero = minX;
		else if (maxX < 0) xZero = maxX;

		// Draw the graph axis lines
		graphics.stroke(c_graph_axis);
		graphics.line(map(xZero, minX, maxX, gL, gR), gT, map(xZero, minX, maxX, gL, gR), gB);
		graphics.line(gL, map(yZero, minY, maxY, gB, gT), gR, map(yZero, minY, maxY, gB, gT));

		// Clear all previous data positions
		resetGraph();
	}


	/**
	 * Round a number to the closest suitable axis division size
	 *
	 * The axis divisions should end in the numbers 1, 2, 2.5, or 5
	 * to ensure that the axes are easily interpretable
	 *
	 * @param  num  The approximate division size we are aiming for
	 * @return The closest idealised division size
	 */
	private double roundToIdeal(float num)
	{
		if(num == 0)
		{
			return 0;
		}

		final int n = 2;
		final double d = Math.ceil( Math.log10( (num < 0)? -num : num ) );
		final int power = n - (int)d;

		final double magnitude = Math.pow( 10, power );
		long shifted = Math.round( num * magnitude );

		// Apply rounding to nearest useful divisor
		if      (abs(shifted) > 75) shifted = (num < 0)? -100 : 100;
		else if (abs(shifted) > 30) shifted = (num < 0)? -50 : 50;
		else if (abs(shifted) > 23) shifted = (num < 0)? -25 : 25;
		else if (abs(shifted) > 15) shifted = (num < 0)? -20 : 20;
		else                        shifted = (num < 0)? -10 : 10; 

		return shifted / magnitude;
	}


	/**
	 * Calculate the precision with which the graph axes labels should be drawn
	 *
	 * @param  maxValue  The maximum axis value
	 * @param  minValue  The minimum axis value
	 * @param  segments  The step size with which the graph will be divided
	 * @return The number of significant digits to display
	 */
	private int calculateRequiredPrecision(double maxValue, double minValue, double segments)
	{
		if ((segments == 0) || (maxValue == minValue)) 
		{
			return 1;
		}

		double largeValue = (maxValue < 0)? -maxValue : maxValue;

		if ((maxValue == 0) || (-minValue > largeValue))
		{
			largeValue = (minValue < 0)? -minValue : minValue;
		}

		final double d1 = Math.floor( Math.log10( (segments < 0)? -segments : segments ) );
		final double d2 = Math.floor( Math.log10( largeValue ) );
		final double removeMSN = Math.round( (segments % Math.pow( 10, d1 )) / Math.pow( 10, d1 - 1 ) );

		int value = abs((int) d2 - (int) d1) + 1;

		if (removeMSN > 0 && removeMSN < 10)
		{
			value++;
		}

		//println(maxValue + "max " + minValue + "min (" + d2 + ")\t - \t" + segments + " seg (" + d1 + ")\t - \t" + value + "val, " + removeMSN + "segMSD " + Math.pow( 10, d1 ) + " " + ((segments % Math.pow( 10, d1 )) ));

		return value;
	}


	/**
	 * Determine whether axis label is shown in decimal or scientific format
	 *
	 * In general, the text will be in decimal notation if the number has
	 * less than 5 characters. It will be in scientific notation otherwise.
	 *
	 * @param  labelNumber The number to be displayed on the label
	 * @param  precision   The number of significant digits to display
	 * @return The formatted label text
	 */
	private String formatLabelText(double labelNumber, int precision)
	{
		// Scientific notation
		String labelScientific = String.format("%." + precision + "g", labelNumber);
		if (labelScientific.contains(".")) labelScientific = labelScientific.replaceAll("[0]+$", "").replaceAll("[.]+$", "");

		// Decimal notation
		String labelDecimal = String.format("%." + precision + "f", labelNumber);
		if (labelDecimal.contains(".")) labelDecimal = labelDecimal.replaceAll("[0]+$", "").replaceAll("[.]+$", "");

		// If decimal notation is shorter than 5 characters, use it
		if (labelDecimal.length() < 5 || (labelDecimal.charAt(0) == '-' && labelDecimal.length() < 6)) return labelDecimal;
		return labelScientific;
	}


	/**
	 * Draw the grid and axes of the graph
	 */
	public void drawGrid()
	{
		redrawGraph = false;

		// Clear the content area
		graphics.rectMode(CORNER);
		graphics.noStroke();
		graphics.fill(c_background);
		graphics.rect(cL, cT, cR - cL, cB - cT);

		// Add border and graph title
		graphics.strokeWeight(1 * scaleFactor);
		graphics.stroke(c_graph_border);
		if (cT > round((tabHeight + 1) * scaleFactor)) graphics.line(cL, cT, cR, cT);
		if (cL > 1) graphics.line(cL, cT, cL, cB);
		graphics.line(cL, cB, cR, cB);
		graphics.line(cR, cT, cR, cB);		

		graphics.textAlign(LEFT, TOP);
		graphics.textFont(base_font);
		graphics.fill(c_lightgrey);
		if (highlighted) graphics.fill(c_red);
		graphics.text(plotName, cL + PApplet.parseInt(5 * scaleFactor), cT + PApplet.parseInt(5 * scaleFactor));

		// X and Y axis zero
		float yZero = 0;
		float xZero = 0;
		if      (minY > 0) yZero = minY;
		else if (maxY < 0) yZero = maxY;
		if      (minX > 0) xZero = minX;
		else if (maxX < 0) xZero = maxX;

		// Text width and height
		graphics.textFont(mono_font);
		final int padding = PApplet.parseInt(5 * scaleFactor);
		final int textHeight = PApplet.parseInt(graphics.textAscent() + graphics.textDescent() + padding);
		final float charWidth = graphics.textWidth("0");

		/* -----------------------------------
		 *     Define graph top and bottom
		 * -----------------------------------*/
		gT = cT + border + textHeight / 3;
		gB = cB - border - textHeight - graphMark;

		/* -----------------------------------
		 *     Calculate y-axis parameters 
		 * -----------------------------------*/
		// Figure out how many segments to divide the data into
		double y_segment = Math.abs(roundToIdeal((maxY - minY) * (textHeight * 2) / (gB - gT)));

		// Figure out a base reference for all the segments
		double y_basePosition = yZero;
		if (yZero > 0) y_basePosition = Math.ceil(minY / y_segment) * y_segment;
		else if (yZero < 0) y_basePosition = -Math.ceil(-maxY / y_segment) * y_segment;

		// Figure out how many decimal places need to be shown on the labels
		int y_precision = calculateRequiredPrecision(maxY, minY, y_segment);

		// Figure out where each of the labels should be drawn
		double y_bottomPosition = y_basePosition - (Math.floor((y_basePosition - minY) / y_segment) * y_segment);
		offsetBottom = map((float) y_bottomPosition, minY, maxY, gB, gT);
		gridY = map((float) y_bottomPosition, minY, maxY, gB, gT) - map((float) (y_bottomPosition + y_segment), minY, maxY, gB, gT);;

		// Figure out the width of the largest label so we know how much room to make
		int yTextWidth = 0;
		String lastLabel = "";
		for (double i = y_bottomPosition; i <= maxY; i += y_segment) {
			String label = formatLabelText(i, y_precision);
			if (label.equals(lastLabel)) y_precision++;
			int labelWidth = PApplet.parseInt(label.length() * charWidth);
			if (labelWidth > yTextWidth) yTextWidth = labelWidth;
			lastLabel = label;
		}

		/* -----------------------------------
		 *     Define graph left and right
		 * -----------------------------------*/
		gL = cL + border + yTextWidth + graphMark + padding;
		gR = cR - border;

		/* -----------------------------------
		 *     Calculate x-axis parameters 
		 * -----------------------------------*/
		boolean solved = false;
		double x_segment;
		int x_precision;
		int xTextWidth = PApplet.parseInt(charWidth * 3);
		double x_basePosition;
		double x_leftPosition;

		// Since the solution of the calculations determines the width of the labels,
		// which in turn influences the calculations, set up a loop so that the label
		// widths are increased in length until they all fit in the axis area.
		do {
			// Figure out how many segments to divide the data into
			x_segment = Math.abs(roundToIdeal((maxX - minX) * (xTextWidth * 3) / (gR - gL)));

			// Figure out a base reference for all the segments
			x_basePosition = xZero;
			if (xZero > 0) x_basePosition = Math.ceil(minX / x_segment) * x_segment;
			else if (xZero < 0) x_basePosition = -Math.ceil(-maxX / x_segment) * x_segment;

			// Figure out how many decimal places need to be shown on the labels
			x_precision = calculateRequiredPrecision(maxX, minX, x_segment);

			// Figure out where each of the labels should be drawn
			x_leftPosition = x_basePosition - (Math.floor((x_basePosition - minX) / x_segment) * x_segment);
			offsetLeft = map((float) x_leftPosition, minX, maxX, gL, gR);
			gridX = map((float) (x_leftPosition + x_segment), minX, maxX, gL, gR) - map((float) x_leftPosition, minX, maxX, gL, gR);

			// Figure out the width of the largest label so we know how much room to make
			int newxTextWidth = 0;
			lastLabel = "";
			for (double i = x_leftPosition; i <= maxX; i += x_segment) {
				String label = formatLabelText(i, x_precision);
				if (label.equals(lastLabel)) x_precision++;
				int labelWidth = PApplet.parseInt(label.length() * charWidth);
				if (labelWidth > newxTextWidth) newxTextWidth = labelWidth;
				lastLabel = label;
			}

			if (newxTextWidth <= xTextWidth) solved = true;
			else xTextWidth = newxTextWidth;
		} while (!solved);

		graphics.fill(c_lightgrey);

		//if (yAxisName != "") {
		//	graphics.textAlign(CENTER, CENTER);
		//	graphics.pushMatrix();
		//	graphics.translate(border / 2, (gB + gT) / 2);
		//	graphics.rotate(-HALF_PI);
		//	graphics.text("Some text here",0,0);
		//	graphics.popMatrix();
		//}

		// ---------- Y-AXIS ----------
		graphics.textAlign(RIGHT, CENTER);

		for (double i = y_bottomPosition; i <= maxY; i += y_segment) {
			final float currentYpixel = map((float) i, minY, maxY, gB, gT);

			if (gridLines) {
				graphics.stroke(c_graph_gridlines);
				graphics.line(gL, currentYpixel, gR, currentYpixel);
			}

			String label = formatLabelText(i, y_precision);

			graphics.stroke(c_graph_axis);
			graphics.text(label, gL - graphMark - padding, currentYpixel - (1 * scaleFactor));
			graphics.line(gL - graphMark, currentYpixel, gL - round(1 * scaleFactor), currentYpixel);

			// Minor graph lines
			if (i > y_bottomPosition) {
				final float minorYpixel = map((float) (i - (y_segment / 2.0f)), minY, maxY, gB, gT);
				graphics.line(gL - (graphMark / 2.0f), minorYpixel, gL - round(1 * scaleFactor), minorYpixel);
			}
		}


		// ---------- X-AXIS ----------
		graphics.textAlign(CENTER, TOP);
		if (xAxisName != "") graphics.text(xAxisName, (gL + gR) / 2, cB - border + padding);

		// Move right first
		for (double i = x_leftPosition; i <= maxX; i += x_segment)
		{
			final float currentXpixel = map((float) i, minX, maxX, gL, gR);

			if (gridLines)
			{
				graphics.stroke(c_graph_gridlines);
				graphics.line(currentXpixel, gT, currentXpixel, gB);
			}

			final String label = formatLabelText(i, x_precision);
			graphics.stroke(c_graph_axis);
			graphics.line(currentXpixel, gB, currentXpixel, gB + graphMark);

			if (i != maxX)
			{
				graphics.text(label, currentXpixel, gB + graphMark + padding);
			}

			// Minor graph lines
			if (i > x_leftPosition)
			{
				final float minorXpixel = map((float) (i - (x_segment / 2.0f)), minX, maxX, gL, gR);
				graphics.line(minorXpixel, gB, minorXpixel, gB + (graphMark / 2.0f));
			}
		}

		// The outer grid axes
		graphics.stroke(c_graph_axis);
		graphics.line(map(xZero, minX, maxX, gL, gR), gT, map(xZero, minX, maxX, gL, gR), gB);
		graphics.line(gL, map(yZero, minY, maxY, gB, gT), gR, map(yZero, minY, maxY, gB, gT));
		graphics.textFont(base_font);
	}
}
/* * * * * * * * * * * * * * * * * * * * * * *
 * LIVE GRAPH PLOTTER CLASS
 * implements TabAPI for Processing Grapher
 *
 * @file     LiveGraph.pde
 * @brief    Real-time serial data plotter tab
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    LiveGraph
 * @see      TabAPI <ProcessingGrapher.pde>
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

class LiveGraph implements TabAPI {

	int cL, cR, cT, cB;     // Content coordinates (left, right, top bottom)
	Graph graphA, graphB, graphC, graphD;
	int menuScroll;
	int menuHeight;
	String name;
	ScrollBar sidebarScroll = new ScrollBar(ScrollBar.VERTICAL, ScrollBar.NORMAL);
	ReentrantLock lock = new ReentrantLock();

	String outputfile;

	String[] dataColumns = {};
	int[] graphAssignment = {};
	int graphMode;
	CustomTable dataTable;
	boolean recordData;
	boolean tabIsVisible;
	int recordCounter;
	int fileCounter;
	int maxFileRows = 100000;
	int drawFrom;
	int pausedCount;
	float xRate;
	int selectedGraph;
	int customXaxis;
	int autoAxis;            //! Graph axis scaling: 0 = Manual, 1 = Expand Only, 2 = Auto expand and contract
	boolean autoFrequency;   //! Detect data sampling rate: True = Automatic, False = Manual
	int frequencyCounter;    //! Counter used to detect sampling rate
	int frequencyTimer;      //! Timer used to detect sampling rate
	boolean isPaused;        //! Play/Pause data on live graphs: True = paused, False = playing
	int maxSamples;          //! Maximum number of samples to record and display on the graphs
	int[] sampleWindow = {1000,1000,1000,1000};
	float[] newMinimum = {0,0,0,0};
	float[] newMaximum = {0,0,0,0};
	float newXminimum = 0;
	float newXmaximum = 0;
	int signalListChange;


	/**
	 * Constructor
	 *
	 * @param  setname Name of the tab
	 * @param  left    Tab area left x-coordinate
	 * @param  right   Tab area right x-coordinate
	 * @param  top     Tab area top y-coordinate
	 * @param  bottom  Tab area bottom y-coordinate
	 */
	LiveGraph (String setname, int left, int right, int top, int bottom) {
		name = setname;
		tabIsVisible = false;
		
		cL = left;
		cR = right;
		cT = top;
		cB = bottom;

		graphA = new Graph(cL, cR, cT, cB, 0, 20, 0, 1, "Graph 1");
		graphB = new Graph(cL, cR, (cT + cB) / 2, cB, 0, 20, 0, 1, "Graph 2");
		graphC = new Graph((cL + cR) / 2, cR, cT, (cT + cB) / 2, 0, 20, 0, 1, "Graph 3");
		graphD = new Graph((cL + cR) / 2, cR, (cT + cB) / 2, cB, 0, 20, 0, 1, "Graph 4");
		graphA.scale(uimult);
		graphB.scale(uimult);
		graphC.scale(uimult);
		graphD.scale(uimult);
		graphA.selected(true);
		graphA.xAxisTitle("Time (s)");
		graphB.xAxisTitle("Time (s)");
		graphC.xAxisTitle("Time (s)");
		graphD.xAxisTitle("Time (s)");

		graphMode = 1;
		selectedGraph = 1;

		outputfile = "No File Set";
		recordData = false;
		recordCounter = 0;
		fileCounter = 0;

		xRate = 100;
		customXaxis = -1;
		autoAxis = 1;
		autoFrequency = true;
		frequencyCounter = 0;
		frequencyTimer = 0;
		isPaused = false;
		
		drawFrom = 0;
		pausedCount = 0;
		maxSamples = 10;
		signalListChange = 0;

		dataTable = new CustomTable();
		
		menuScroll = 0;
		menuHeight = cB - cT - 1; 
	}


	/**
	 * Get the name of the current tab
	 *
	 * @return Tab name
	 */
	public String getName () {
		return name;
	}


	/**
	 * Set tab as being active or hidden
	 * 
	 * @param  newState True = active, false = hidden
	 */
	public void setVisibility(boolean newState) {
		tabIsVisible = newState;
	}


	/**
	 * Set current side menu level
	 * 
	 * @param  newLevel The new menu level
	 */
	public void setMenuLevel(int newLevel) {
		// Do nothing
	}


	/**
	 * Redraw all tab content
	 */
	public void drawContent () {
		graphA.drawGrid();
		graphA.resetGraph();
		if (graphMode > 1) {
			graphB.drawGrid();
			graphB.resetGraph();
		}
		if (graphMode > 2) {
			graphC.drawGrid();
			graphC.resetGraph();
		}
		if (graphMode > 3) {
			graphD.drawGrid();
			graphD.resetGraph();
		}

		if (dataTable.getRowCount() > 0) {
			drawNewData();
		}

		// Show message if no serial device is connected
		if (!serialConnected) {
			if (showInstructions) {
				String[] message = {"1. In the 'Serial' tab, use the right-hand menu to connect to a serial device",
								    "2. Each line sent by the device should contain only numbers separated with commas",
								    "3. The signals/numbers can be displayed in real-time on up to 4 separate graphs"};
				drawMessageArea("Getting Started", message, cL + 60 * uimult, cR - 60 * uimult, cT + 30 * uimult);
			} else {
				String messageText = "Serial Port Disconnected";
				rectMode(CENTER);
				textAlign(CENTER, TOP);
				stroke(c_alert_message_box);
				fill(c_alert_message_box);
				rect((cR - cL) / 2, cT + (15 * uimult), textWidth(messageText) + (10 * uimult), 20 * uimult);
				fill(c_sidebar_heading);
				text(messageText, (cR - cL) / 2, cT + PApplet.parseInt(5 * uimult));
			}

		// Show message if device is connected, but graph is paused
		} else if (isPaused) {
			String messageText = "Live Graph is Paused";
			rectMode(CENTER);
			textAlign(CENTER, TOP);
			stroke(c_alert_message_box);
			fill(c_alert_message_box);
			rect((cR - cL) / 2, cT + (15 * uimult), textWidth(messageText) + (10 * uimult), 20 * uimult);
			fill(c_sidebar_heading);
			text(messageText, (cR - cL) / 2, cT + PApplet.parseInt(5 * uimult));
		}

	}


	/**
	 * Draw new tab data
	 */
	public void drawNewData () {
		lock.lock();
		int currentCount = dataTable.getRowCount();
		if (isPaused) {
			if (pausedCount < currentCount) currentCount = pausedCount;
		}

		// If there is content to draw
		if (currentCount > 0) {
			
			int samplesA = currentCount - sampleWindow[0] - 1;
			int samplesB = currentCount - sampleWindow[1] - 1;
			int samplesC = currentCount - sampleWindow[2] - 1;
			int samplesD = currentCount - sampleWindow[3] - 1;

			drawFrom = samplesA;
			graphA.clearGraph();
			if (graphMode >= 2) {
				graphB.clearGraph();
				if (samplesB < drawFrom) drawFrom = samplesB;
			}
			if (graphMode >= 3) {
				graphC.clearGraph();
				if (samplesC < drawFrom) drawFrom = samplesC;
			}
			if (graphMode >= 4) {
				graphD.clearGraph();
				if (samplesD < drawFrom) drawFrom = samplesD;
			}

			maxSamples = currentCount - drawFrom;
			if (drawFrom < 0) drawFrom = 0;
			newMinimum[0] = Float.NaN;
			newMinimum[1] = Float.NaN;
			newMinimum[2] = Float.NaN;
			newMinimum[3] = Float.NaN;
			newXminimum = Float.NaN;

			newMaximum[0] = Float.NaN;
			newMaximum[1] = Float.NaN;
			newMaximum[2] = Float.NaN;
			newMaximum[3] = Float.NaN;
			newXmaximum = Float.NaN;

			for (int j = drawFrom; j < currentCount; j++) {
				for (int i = 0; i < dataTable.getColumnCount(); i++) {
					if (i != customXaxis)
					{
						try {
							float xDataValue = 0;
							if (customXaxis >= 0) xDataValue = (float) dataTable.getDouble(j, customXaxis);
							float dataPoint = (float) dataTable.getDouble(j, i);

							if (dataPoint != dataPoint) dataPoint = Float.MAX_VALUE;
							if (graphAssignment[i] == 2 && graphMode >= 2 && samplesB <= drawFrom) {
								checkGraphSize(dataPoint, xDataValue, 1);
								if (customXaxis >= 0) graphB.plotData(xDataValue, dataPoint, i);
								else graphB.plotData(dataPoint, i);
							} else if (graphAssignment[i] == 3 && graphMode >= 3 && samplesC <= drawFrom) {
								checkGraphSize(dataPoint, xDataValue, 2);
								if (customXaxis >= 0) graphC.plotData(xDataValue, dataPoint, i);
								else graphC.plotData(dataPoint, i);
							} else if (graphAssignment[i] == 4 && graphMode >= 4 && samplesD <= drawFrom) {
								checkGraphSize(dataPoint, xDataValue, 3);
								if (customXaxis >= 0) graphD.plotData(xDataValue, dataPoint, i);
								else graphD.plotData(dataPoint, i);
							} else if (graphAssignment[i] == 1 && samplesA <= drawFrom) {
								checkGraphSize(dataPoint, xDataValue, 0);
								if (customXaxis >= 0) graphA.plotData(xDataValue, dataPoint, i);
								else graphA.plotData(dataPoint, i);
							}
						} catch (Exception e) {
							println("LiveGraph::drawNewData() - drawFrom: " + drawFrom + ", currentCount: " + currentCount + ", Error: " + e);
						}
					}
				}
				drawFrom++;
			}

			updateGraphSize(graphA, 0);
			if (graphMode >= 2) updateGraphSize(graphB, 1);
			if (graphMode >= 3) updateGraphSize(graphC, 2);
			if (graphMode >= 4) updateGraphSize(graphD, 3);
		}
		lock.unlock();
	}
	

	/**
	 * Update minimum and maximum datapoint arrays
	 *
	 * @param  dataPoint    Y-coordinate of new data point
	 * @param  xAxisPoint   X-coordinate of the new data point
	 * @param  currentGrpah Array index of the graph
	 */
	public void checkGraphSize (float dataPoint, float xAxisPoint, int currentGraph) {
		
		// If data exceeds graph size, resize the graph
		if (autoAxis != 0 && dataPoint != Float.MAX_VALUE) {

			// Find minimum point
			if (dataPoint < newMinimum[currentGraph] || Float.isNaN(newMinimum[currentGraph])) {
				newMinimum[currentGraph] = dataPoint;
			}
			// Find maximum point
			else if (dataPoint > newMaximum[currentGraph] || Float.isNaN(newMaximum[currentGraph])) {
				newMaximum[currentGraph] = dataPoint;
			}

			if (customXaxis >= 0)
			{
				// Find minimum point
				if (xAxisPoint < newXminimum || Float.isNaN(newXminimum)) {
					newXminimum = xAxisPoint;
				}
				// Find maximum point
				else if (xAxisPoint > newXmaximum || Float.isNaN(newXmaximum)) {
					newXmaximum = xAxisPoint;
				}
			}
		}
	}


	/**
	 * Resize graph y-axis if data point is out of bounds
	 *
	 * @param  currentGraph Which if the 4 graphs to check
	 * @param  graphIndx    Array index of the graph
	 */
	public void updateGraphSize(Graph currentGraph, int graphIndex) {

		boolean redrawGrid = false;

		if (autoAxis != 0 && !Float.isNaN(newMinimum[graphIndex]) && !Float.isNaN(newMaximum[graphIndex])) {
			newMinimum[graphIndex] = floorToSigFig(newMinimum[graphIndex], 1);
			newMaximum[graphIndex] = ceilToSigFig(newMaximum[graphIndex], 1);

			if (autoAxis == 1) {
				if (currentGraph.yMin() > newMinimum[graphIndex]) {
					currentGraph.yMin(newMinimum[graphIndex]);
					redrawGrid = true;
				}
				if (currentGraph.yMax() < newMaximum[graphIndex]) {
					currentGraph.yMax(newMaximum[graphIndex]);
					redrawGrid = true;
				}
			} else if (autoAxis == 2) {
				if (currentGraph.yMin() != newMinimum[graphIndex]) {
					currentGraph.yMin(newMinimum[graphIndex]);
					redrawGrid = true;
				}
				if (currentGraph.yMax() != newMaximum[graphIndex]) {
					currentGraph.yMax(newMaximum[graphIndex]);
					redrawGrid = true;
				}
			}
		}

		if (autoAxis != 0 && customXaxis >= 0 && !Float.isNaN(newXminimum) && !Float.isNaN(newXmaximum)) {
			newXminimum = floorToSigFig(newXminimum, 1);
			newXmaximum = ceilToSigFig(newXmaximum, 1);

			if (autoAxis == 1) {
				if (currentGraph.xMin() > newXminimum) {
					currentGraph.xMin(newXminimum);
					redrawGrid = true;
				}
				if (currentGraph.xMax() < newXmaximum) {
					currentGraph.xMax(newXmaximum);
					redrawGrid = true;
				}
			} else if (autoAxis == 2) {
				if (currentGraph.xMin() != newXminimum) {
					currentGraph.xMin(newXminimum);
					redrawGrid = true;
				}
				if (currentGraph.xMax() != newXmaximum) {
					currentGraph.xMax(newXmaximum);
					redrawGrid = true;
				}
			}
		}

		if (redrawGrid)
		{
			currentGraph.drawGrid();
			redrawContent = true;
			redrawUI = true;
		}
	}


	/**
	 * Change tab content area dimensions
	 *
	 * @param  newL New left x-coordinate
	 * @param  newR New right x-coordinate
	 * @param  newT New top y-coordinate
	 * @param  newB new bottom y-coordinate
	 */
	public void changeSize (int newL, int newR, int newT, int newB) {
		cL = newL;
		cR = newR;
		cT = newT;
		cB = newB;
		graphA.scale(uimult);
		graphB.scale(uimult);
		graphC.scale(uimult);
		graphD.scale(uimult);

		if (graphMode == 2) {
			graphA.setSize(cL, cR, cT, (cT + cB) / 2);
			graphB.setSize(cL, cR, (cT + cB) / 2, cB);
		} else if (graphMode == 3) {
			graphA.setSize(cL, (cL + cR) / 2, cT, (cT + cB) / 2);
			graphB.setSize(cL, cR, (cT + cB) / 2, cB);
			graphC.setSize((cL + cR) / 2, cR, cT, (cT + cB) / 2);
		} else if (graphMode == 4) {
			graphA.setSize(cL, (cL + cR) / 2, cT, (cT + cB) / 2);
			graphB.setSize(cL, (cL + cR) / 2, (cT + cB) / 2, cB);
			graphC.setSize((cL + cR) / 2, cR, cT, (cT + cB) / 2);
			graphD.setSize((cL + cR) / 2, cR, (cT + cB) / 2, cB);
		} else {
			graphA.setSize(cL, cR, cT, cB);
		}
		//drawContent();
	}


	/**
	 * Change CSV data file location
	 *
	 * @param  newoutput Absolute path to the new file location
	 */
	public void setOutput (String newoutput) {
		if (newoutput != "No File Set") {
			// Ensure file type is *.csv
			int dotPos = newoutput.lastIndexOf(".");
			if (dotPos > 0) newoutput = newoutput.substring(0, dotPos);
			newoutput = newoutput + ".csv";

			// Test whether this file is actually accessible
			if (saveFile(newoutput) == null) {
				alertMessage("Error\nUnable to access the selected output file location; perhaps this location is write-protected?\n" + newoutput);
				newoutput = "No File Set";
			}
		}
		outputfile = newoutput;
	}


	/**
	 * Get the current CSV data file location
	 *
	 * @return Absolute path to the data file
	 */
	public String getOutput () {
		return outputfile;
	}


	/** 
	 * Start recording new serial data points to file
	 */
	public void startRecording () {
		// Ensure table is empty
		dataTable = new CustomTable();
		drawFrom = 0;
		pausedCount = 0;
		isPaused = false;
		redrawContent = true;

		// Add columns to the table
		while (dataTable.getColumnCount() < dataColumns.length) {
			if (customXaxis >= 0 && customXaxis == dataTable.getColumnCount()) {
				dataTable.addColumn("x:" + dataColumns[dataTable.getColumnCount()]);
			} else {
				dataTable.addColumn(dataColumns[dataTable.getColumnCount()]);
			}
		}

		// Open up the CSV output stream
		if (!dataTable.openCSVoutput(outputfile)) {
			alertMessage("Error\nUnable to create the output file; perhaps the location no longer exists?\n" + outputfile);
		} else {
			recordCounter = 0;
			fileCounter = 0;
			recordData = true;
			redrawUI = true;
		}
	}


	/**
	 * Stop recording data points to file
	 */
	public void stopRecording(){
		recordData = false;
		if (dataTable.closeCSVoutput()) {
			alertMessage("Success\nRecorded " + ((fileCounter * 10000) + recordCounter) + " samples to " + (fileCounter + 1) + " CSV file(s)");
		} else {
			emergencyOutputSave(false);
		}
		outputfile = "No File Set";
		if (tabIsVisible) redrawUI = true;
	}


	/**
	 * Recover from an rrror when recording data to file
	 *
	 * @param  continueRecording If we want to continue recording after dealing with the error
	 */
	public void emergencyOutputSave(boolean continueRecording) {
		dataTable.closeCSVoutput();

		// Figure out name for new backup file
		String[] tempSplit = split(outputfile, '/');
		int dotPos = tempSplit[tempSplit.length - 1].lastIndexOf(".");
		String nextoutputfile = tempSplit[tempSplit.length - 1].substring(0, dotPos);
		outputfile = nextoutputfile + "-backup.csv";

		String emergencysavefile = nextoutputfile + "-backup-" + (fileCounter + 1) + ".csv";

		try {
			// Backup the existing data
			saveTable(dataTable, emergencysavefile);

			// If we want to continue recording, try setting up a new output file
			if (continueRecording) {
				fileCounter++;
				nextoutputfile = nextoutputfile + "-backup-" + (fileCounter + 1) + ".csv";

				// If new output file was successfully opened, only show a Warning message
				if (dataTable.openCSVoutput(nextoutputfile)) {
					alertMessage("Warning\nAn issue occurred when trying to save new data to the ouput file.\n1. A backup of all the data has been created\n2. Data is still being recorded (to a new file)\n3. The files are in the same directory as ProcessingGrapher.exe");
				
				// If not, show an error message that the recording has stopped
				} else {
					recordData = false;
					redrawUI = true;
					alertMessage("Error - Recording Stopped\nAn issue occurred when trying to save new data to the ouput file.\n1. A backup of all the data has been created\n2. The files are in the same directory as ProcessingGrapher.exe");
				}

			// If we don't want to continue, show a simple error message
			} else {
				recordData = false;
				alertMessage("Error\nAn issue occurred when trying to save new data to the ouput file.\n1. Data recording has been stopped\n2. A backup of all the data has been created\n3. The backup is in the same directory as ProcessingGrapher.exe");
			}

		// If something went wrong in the error recovery process, show a critical error message
		} catch (Exception e) {
			dataTable.closeCSVoutput();
			recordData = false;
			alertMessage("Critical Error\nAn issue occurred when trying to save new data to the ouput file.\nData backup was also unsuccessful, so some data may have been lost...\n" + e);
		}
	}


	/**
	 * Function called when a serial device has connected/disconnected
	 *
	 * @param  status True if a device has connected, false if disconnected
	 */
	public void connectionEvent (boolean status) {

		// If port has disconnected
		if (!status) {
			// Stop recording any data
			if (recordData) stopRecording();
			frequencyCounter = 0;
			frequencyTimer = 0;

			// Reset the signal list
			/*
			dataTable.clearRows();
			
			while (dataColumns.length > 0) {
				dataColumns = shorten(dataColumns);
				graphAssignment = shorten(graphAssignment);
			}
			while (dataTable.getColumnCount() > 0) dataTable.removeColumn(0);
			drawFrom = 0;
			frequencyCounter = 0;
			frequencyTimer = 0;
			if (tabIsVisible) redrawContent = true;
			*/
		}
	}


	/**
	 * Parse new data points received from serial port
	 *
	 * @param  inputData String containing data points separated by commas
	 * @param  graphable True if data in message can be plotted on a graph
	 */
	public void parsePortData (String inputData, boolean graphable) {

		// Check that the starts with a number
		if (graphable) {
			// Get data
			String[] dataArray = trim(split(inputData, separator));
			
			// Check the frequency
			if (autoFrequency && (frequencyCounter != -1)) {
				if (frequencyCounter == 0) frequencyTimer = millis();
				frequencyCounter++;
				if ((frequencyCounter > 20) && (millis() - frequencyTimer >= 10000)) {
					float newXrate = 1000.0f * (frequencyCounter) / PApplet.parseFloat(millis() - frequencyTimer);
					if (abs(newXrate - xRate) > 2) {
						xRate = roundToSigFig(newXrate, 2);
						graphA.frequency(xRate);
						graphB.frequency(xRate);
						graphC.frequency(xRate);
						graphD.frequency(xRate);
						sampleWindow[0] = PApplet.parseInt(xRate * abs(graphA.xMax() - graphA.xMin()));
						sampleWindow[1] = PApplet.parseInt(xRate * abs(graphB.xMax() - graphB.xMin()));
						sampleWindow[2] = PApplet.parseInt(xRate * abs(graphC.xMax() - graphC.xMin()));
						sampleWindow[3] = PApplet.parseInt(xRate * abs(graphD.xMax() - graphD.xMin()));
						redrawContent = true;
						redrawUI = true;
					}
					frequencyCounter = 0;
				}
			}

			// If data column does not exist, add it to the list
			while(dataColumns.length < dataArray.length){
				dataColumns = append(dataColumns, "Signal-" + (dataColumns.length + 1));
				graphAssignment = append(graphAssignment, 1);
				dataTable.addColumn("Signal-" + (dataColumns.length + 1), CustomTable.STRING);
				redrawUI = true;
			}

			// Only remove extra columns if not recording and
			// the last 10 input data samples didn't contain the signal
			if (dataColumns.length > dataArray.length) {
				signalListChange++;
				if (signalListChange >= 10 && !recordData && !isPaused) {
					dataColumns = shorten(dataColumns);
					graphAssignment = shorten(graphAssignment);
					dataTable.removeColumn(dataColumns.length);
					signalListChange = 0;
					redrawUI = true;
				}
			}
	
			// --- Data Recording ---
			lock.lock();
			TableRow newRow = dataTable.addRow();
			//float[] newData = new float[dataArray.length];

			// Go through each data column, and try to parse and add to file
			for(int i = 0; i < dataArray.length; i++){
				try {
					double dataPoint = Double.parseDouble(dataArray[i]);
					newRow.setDouble(i, dataPoint);
					//newData[i] = dataPoint;
					//checkGraphSize(dataPoint, 0);
				} catch (Exception e) {
					print(e);
					println(" - When parsing live graph data");
				}
			}
			lock.unlock();

			//graphA.bufferNewData(newData);

			// Record data to file
			if (recordData) {
				recordCounter++;
				if (!dataTable.saveCSVentries(dataTable.lastRowIndex(), dataTable.lastRowIndex())) {
					emergencyOutputSave(true);
				}

				// Separate data into files once the max number of rows has been reached
				if (recordCounter >= maxFileRows) {
					dataTable.closeCSVoutput();
					fileCounter++;
					recordCounter = 0;

					int dotPos = outputfile.lastIndexOf(".");
					String nextoutputfile = outputfile.substring(0, dotPos);
					nextoutputfile = nextoutputfile + "-" + (fileCounter + 1) + ".csv";

					// Ensure table is empty
					dataTable.clearRows();
					drawFrom = 0;

					if (!dataTable.openCSVoutput(nextoutputfile)) {
						//emergencyOutputSave(true);
						println("Failed to start recording to a new output file");
					}
				}
			} else if (!isPaused && !lock.isLocked()) {
				// Remove rows from table which don't need to be shown on the graphs anymore
				while (dataTable.getRowCount() > maxSamples) {
					lock.lock();
					dataTable.removeRow(0);
					drawFrom--;
					if (drawFrom < 0) drawFrom = 0;
					lock.unlock();
				}
			}
	
			if (tabIsVisible) drawNewData = true;
		}
	}


	/**
	 * Draw the sidebar menu for the current tab
	 */
	public void drawSidebar () {
		
		// Calculate sizing of sidebar
		// Do this here so commands below are simplified
		int sT = cT;
		int sL = cR;
		int sW = width - cR;
		int sH = height - sT;

		int uH = round(sideItemHeight * uimult);
		int tH = round((sideItemHeight - 8) * uimult);
		int iH = round((sideItemHeight - 5) * uimult);
		int iL = round(sL + (10 * uimult));
		int iW = round(sW - (20 * uimult));
		menuHeight = round((13.5f + dataColumns.length + ((graphMode + 1) * 0.75f)) * uH);

		// Figure out if scrolling of the menu is necessary
		if (menuHeight > sH) {
			if (menuScroll == -1) menuScroll = 0;
			else if (menuScroll > menuHeight - sH) menuScroll = menuHeight - sH;

			// Draw left bar
			fill(c_serial_message_box);
			rect(width - round(15 * uimult) / 2, sT, round(15 * uimult) / 2, sH);

			// Figure out size and position of scroll bar indicator
			int scrollbarSize = sH - round(sH * PApplet.parseFloat(menuHeight - sH) / menuHeight);
			if (scrollbarSize < uH) scrollbarSize = uH;
			int scrollbarOffset = round((sH - scrollbarSize) * (menuScroll / PApplet.parseFloat(menuHeight - sH)));
			fill(c_terminal_text);
			rect(width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);
			sidebarScroll.update(menuHeight, sH, width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);

			sT -= menuScroll;
			sL -= round(15 * uimult) / 4;
			iL -= round(15 * uimult) / 4;
		} else {
			menuScroll = -1;
		}

		// Save to File
		drawHeading("Record Graph Data", iL, sT + (uH * 0), iW, tH);
		if (outputfile == "No File Set" || outputfile == "") {
			drawButton("Set Output File", c_sidebar_button, iL, sT + (uH * 1), iW, iH, tH);
			drawDatabox("Start Recording", c_idletab_text, iL, sT + (uH * 2), iW, iH, tH);
		} else {
			String[] fileParts = split(outputfile, '/');
			String fileName = fileParts[fileParts.length - 1];

			if (recordData) {
				drawDatabox(fileName, c_idletab_text, iL, sT + (uH * 1), iW, iH, tH);
				drawButton("Stop Recording", c_sidebar_accent, iL, sT + (uH * 2), iW, iH, tH);
			} else {
				drawDatabox(fileName, c_sidebar_text, iL, sT + (uH * 1), iW, iH, tH);
				drawButton("Start Recording", c_sidebar_button, iL, sT + (uH * 2), iW, iH, tH);
			}
		}

		// Graph options
		Graph currentGraph;
		if (selectedGraph == 2) currentGraph = graphB;
		else if (selectedGraph == 3) currentGraph = graphC;
		else if (selectedGraph == 4) currentGraph = graphD;
		else currentGraph = graphA;

		drawHeading("Graph " + selectedGraph + " - Options",                                     iL,                sT + (uH * 3.5f),         iW, tH);
		drawButton("Line", (currentGraph.graphType() == Graph.LINE_CHART)? c_sidebar_accent:c_sidebar_button, iL,                sT + (uH * 4.5f), iW / 3, iH, tH);
		drawButton("Dots", (currentGraph.graphType() == Graph.DOT_CHART)? c_sidebar_accent:c_sidebar_button,  iL + (iW / 3),     sT + (uH * 4.5f), iW / 3, iH, tH);
		drawButton("Bar", (currentGraph.graphType() == Graph.BAR_CHART)? c_sidebar_accent:c_sidebar_button,   iL + (iW * 2 / 3), sT + (uH * 4.5f), iW / 3, iH, tH);
		drawRectangle(c_sidebar_divider, iL + (iW / 3),     sT + (uH * 4.5f) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
		drawRectangle(c_sidebar_divider, iL + (iW * 2 / 3), sT + (uH * 4.5f) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

		drawDatabox(str(currentGraph.xMin()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), ((autoAxis != 2) && (customXaxis >= 0))? c_sidebar_text:c_idletab_text, iL, sT + (uH * 5.5f), (iW / 2) - (6 * uimult), iH, tH);
		drawButton("x", c_sidebar_button, iL + (iW / 2) - (6 * uimult), sT + (uH * 5.5f), 12 * uimult,             iH, tH);
		drawDatabox(str(currentGraph.xMax()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), ((autoAxis != 2) || (customXaxis < 0))? c_sidebar_text:c_idletab_text, iL + (iW / 2) + (6 * uimult), sT + (uH * 5.5f), (iW / 2) - (6 * uimult), iH, tH);
		drawDatabox(str(currentGraph.yMin()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), (autoAxis != 2)? c_sidebar_text:c_idletab_text, iL,                           sT + (uH * 6.5f), (iW / 2) - (6 * uimult), iH, tH);
		drawButton("y", c_sidebar_button, iL + (iW / 2) - (6 * uimult), sT + (uH * 6.5f), 12 * uimult,             iH, tH);
		drawDatabox(str(currentGraph.yMax()).replaceAll("[0]+$", "").replaceAll("[.]+$", ""), (autoAxis != 2)? c_sidebar_text:c_idletab_text, iL + (iW / 2) + (6 * uimult), sT + (uH * 6.5f), (iW / 2) - (6 * uimult), iH, tH);
		if (autoAxis == 2) drawButton("Scale: Automatic", c_sidebar_button, iL, sT + (uH * 7.5f), iW, iH, tH);
		else if (autoAxis == 1) drawButton("Scale: Expand Only", c_sidebar_button, iL, sT + (uH * 7.5f), iW, iH, tH);
		else drawButton("Scale: Manual", c_sidebar_button, iL, sT + (uH * 7.5f), iW, iH, tH);

		// Input Data Columns
		drawHeading("Data Format", iL, sT + (uH * 9), iW, tH);
		//drawButton((isPaused)? "Resume Data":"Pause Data", (isPaused)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 11), iW, iH, tH);
		drawButton("", (!isPaused)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 10), iW / 4, iH, tH);
		drawTriangle(c_sidebar_text, iL + (12 * uimult), sT + (uH * 10) + (8 * uimult), iL + (12 * uimult), sT + (uH * 10) + iH - (8 * uimult), iL + (iW / 4) - (12 * uimult), sT + (uH * 10) + (tH / 2) + 1);
		drawButton("", (isPaused)? c_sidebar_accent:c_sidebar_button, iL + (iW / 4), sT + (uH * 10), iW / 4 + 1, iH, tH);
		drawButton("Clear", c_sidebar_button, iL + (iW / 2), sT + (uH * 10), iW / 2, iH, tH);
		drawRectangle(c_sidebar_text, iL + (iW / 4) + (12 * uimult), sT + (uH * 10) + (8 * uimult), 3 * uimult, iH - (16 * uimult));
		drawRectangle(c_sidebar_text, iL + (iW / 2) - (12 * uimult), sT + (uH * 10) + (8 * uimult), -3 * uimult, iH - (16 * uimult));
		drawRectangle(c_sidebar_divider, iL + (iW / 4), sT + (uH * 10) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
		drawRectangle(c_sidebar_divider, iL + (iW / 2), sT + (uH * 10) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

		//drawButton("Add Column", c_sidebar_button, iL, sT + (uH * 13.5), iW, iH, tH);
		drawDatabox("Split", c_idletab_text, iL, sT + (uH * 11), iW - (80 * uimult), iH, tH);
		drawButton("1", (graphMode == 1)? c_sidebar_accent:c_sidebar_button, iL + iW - (80 * uimult), sT + (uH * 11), 20 * uimult, iH, tH);
		drawButton("2", (graphMode == 2)? c_sidebar_accent:c_sidebar_button, iL + iW - (60 * uimult), sT + (uH * 11), 20 * uimult, iH, tH);
		drawButton("3", (graphMode == 3)? c_sidebar_accent:c_sidebar_button, iL + iW - (40 * uimult), sT + (uH * 11), 20 * uimult, iH, tH);
		drawButton("4", (graphMode == 4)? c_sidebar_accent:c_sidebar_button, iL + iW - (20 * uimult), sT + (uH * 11), 20 * uimult, iH, tH);
		drawRectangle(c_sidebar_divider, iL + iW - (60 * uimult), sT + (uH * 11) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
		drawRectangle(c_sidebar_divider, iL + iW - (40 * uimult), sT + (uH * 11) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
		drawRectangle(c_sidebar_divider, iL + iW - (20 * uimult), sT + (uH * 11) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

		if (customXaxis >= 0) drawDatabox("X: " + dataColumns[customXaxis], iL, sT + (uH * 12), iW, iH, tH);
		else drawDatabox(((autoFrequency)? "Auto: ":"Rate: ") + xRate + "Hz", iL, sT + (uH * 12), iW, iH, tH);

		float tHnow = 13;

		for (int j = 0; j < graphMode + 1; j++) {
			if (j < graphMode) drawText("Graph " + (j + 1), c_idletab_text, iL, sT + (uH * tHnow), iW, iH * 3 / 4);
			else drawText("Hidden", c_idletab_text, iL, sT + (uH * tHnow), iW, iH * 3 / 4);
			tHnow += 0.75f;
			int itemCount = 0;

			// List of Data Columns
			for(int i = 0; i < dataColumns.length; i++) {

				if (graphAssignment[i] == j + 1) {
					// Column name
					drawDatabox(dataColumns[i], iL, sT + (uH * tHnow), iW - (40 * uimult), iH, tH);

					// Up button
					int buttonColor = c_colorlist[i-(c_colorlist.length * floor(i / c_colorlist.length))];
					drawButton("▲", c_sidebar, buttonColor, iL + iW - (40 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);

					// Down button
					drawButton((graphAssignment[i] < graphMode + 1)? "▼":"", c_sidebar, buttonColor, iL + iW - (20 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);

					drawRectangle(c_sidebar_divider, iL + iW - (20 * uimult), sT + (uH * tHnow) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
					tHnow++;
					itemCount++;
				}
			}

			if (itemCount == 0) drawText("Empty", c_idletab_text, iL + iW / 2, sT + (uH * (tHnow - itemCount - 0.75f)), iW / 2, iH * 3 / 4);
		}
	}


	/**
	 * Draw the btoom information bar
	 */
	public void drawInfoBar() {
		int sW = width - cR;
		textAlign(LEFT, TOP);
		textFont(base_font);
		fill(c_status_bar);
		text("Output: " + constrainString(outputfile, width - sW - round(175 * uimult) - textWidth("Output: ")), 
			round(5 * uimult), height - round(bottombarHeight * uimult) + round(2*uimult));
	}


	/**
	 * Keyboard input handler function
	 *
	 * @param  key The character of the key that was pressed
	 */
	public void keyboardInput (char keyChar, int keyCodeInt, boolean codedKey) {
		if (codedKey) {
			switch (keyCodeInt) {
				case UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= (12 * uimult);
						if (menuScroll < 0) menuScroll = 0;
					}
					redrawUI = true;
					break;

				case DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += (12 * uimult);
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
					}
					redrawUI = true;
					break;

				case KeyEvent.VK_PAGE_UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= height - cT;
						if (menuScroll < 0) menuScroll = 0;
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_PAGE_DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += height - cT;
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_END:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_HOME:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = 0;
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_F4:
					// Set output file
					if (!recordData) {
						outputfile = "";
						selectOutput("Select a location and name for the output *.CSV file", "fileSelected");
					}
					break;

				case KeyEvent.VK_F6:
					// Start/stop recording
					if(recordData){
						stopRecording();
					} else if(outputfile != "" && outputfile != "No File Set"){
						startRecording();
					}
					break;
			}
		}
	}


	/**
	 * Content area mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void contentClick (int xcoord, int ycoord) {
		if ((graphMode == 1 || ycoord <= (cT + cB) / 2) && (graphMode < 3 || xcoord <= (cL + cR) / 2)) {
			selectedGraph = 1;
			graphA.selected(true);
			graphB.selected(false);
			graphC.selected(false);
			graphD.selected(false);
			redrawUI = true;
			redrawContent = true;
		} else if ((ycoord > (cT + cB) / 2 && graphMode > 1) && (xcoord <= (cL + cR) / 2 || graphMode < 4)) {
			selectedGraph = 2;
			graphA.selected(false);
			graphB.selected(true);
			graphC.selected(false);
			graphD.selected(false);
			redrawUI = true;
			redrawContent = true;
		} else if ((ycoord <= (cT + cB) / 2 && graphMode > 2) && (xcoord > (cL + cR) / 2)) {
			selectedGraph = 3;
			graphA.selected(false);
			graphB.selected(false);
			graphC.selected(true);
			graphD.selected(false);
			redrawUI = true;
			redrawContent = true;
		} else if ((ycoord > (cT + cB) / 2 && graphMode > 3) && (xcoord > (cL + cR) / 2)) {
			selectedGraph = 4;
			graphA.selected(false);
			graphB.selected(false);
			graphC.selected(false);
			graphD.selected(true);
			redrawUI = true;
			redrawContent = true;
		}
	}


	/**
	 * Scroll wheel handler function
	 *
	 * @param  amount Multiplier/velocity of the latest mousewheel movement
	 */
	public void scrollWheel (float amount) {
		// Scroll menu bar
		if (mouseX >= cR && menuScroll != -1) {
			menuScroll += (sideItemHeight * amount * uimult);
			if (menuScroll < 0) menuScroll = 0;
			else if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
		}

		redrawUI = true;
	}


	/**
	 * Scroll bar handler function
	 *
	 * @param  xcoord Current mouse x-coordinate position
	 * @param  ycoord Current mouse y-coordinate position
	 */
	public void scrollBarUpdate (int xcoord, int ycoord) {
		if (sidebarScroll.active()) {
			int previousScroll = menuScroll;
			menuScroll = sidebarScroll.move(xcoord, ycoord, menuScroll, 0, menuHeight - (height - cT));
			if (previousScroll != menuScroll) redrawUI = true;
		}
	}


	/**
	 * Sidebar mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void menuClick (int xcoord, int ycoord) {

		// Coordinate calculation
		int sT = cT;
		int sL = cR;
		if (menuScroll > 0) sT -= menuScroll;
		if (menuScroll != -1) sL -= round(15 * uimult) / 4;
		final int sW = width - cR;
		final int sH = height - sT;

		final int uH = round(sideItemHeight * uimult);
		final int tH = round((sideItemHeight - 8) * uimult);
		final int iH = round((sideItemHeight - 5) * uimult);
		final int iL = round(sL + (10 * uimult));
		final int iW = PApplet.parseInt(sW - (20 * uimult));

		// Click on sidebar menu scroll bar
		if ((menuScroll != -1) && sidebarScroll.click(xcoord, ycoord)) {
			startScrolling(false);
		}

		// Select output file name and directory
		if (menuXYclick(xcoord, ycoord, sT, uH, iH, 1, iL, iW)){
			if (!recordData) {
				outputfile = "";
				selectOutput("Select a location and name for the output *.CSV file", "fileSelected");
			}
		}
		
		// Start recording data and saving it to a file
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 2, iL, iW)){
			if(recordData){
				stopRecording();
			} else if(outputfile != "" && outputfile != "No File Set"){
				startRecording();
			}
			//else {
			//	alertMessage("Error\nPlease set an output file path.");
			//}
		}

		// Change graph type
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 4.5f, iL, iW)){
			Graph currentGraph;
			if (selectedGraph == 2) currentGraph = graphB;
			else if (selectedGraph == 3) currentGraph = graphC;
			else if (selectedGraph == 4) currentGraph = graphD;
			else currentGraph = graphA;

			// Line
			if (menuXclick(xcoord, iL, iW / 3)) {
				currentGraph.graphType(Graph.LINE_CHART);
				redrawContent = redrawUI = true;
			}

			// Dot
			else if (menuXclick(xcoord, iL + (iW / 3), iW / 3)) {
				currentGraph.graphType(Graph.DOT_CHART);
				redrawContent = redrawUI = true;
			}

			// Bar
			else if (menuXclick(xcoord, iL + (iW * 2 / 3), iW / 3)) {
				currentGraph.graphType(Graph.BAR_CHART);
				redrawContent = redrawUI = true;
			}
		}

		// Update X axis scaling
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 5.5f, iL, iW)) {
			Graph currentGraph;
			if (selectedGraph == 2) currentGraph = graphB;
			else if (selectedGraph == 3) currentGraph = graphC;
			else if (selectedGraph == 4) currentGraph = graphD;
			else currentGraph = graphA;

			// Change X axis minimum value
			if ((autoAxis != 2) && (customXaxis >= 0) && (mouseX > iL) && (mouseX < iL + (iW / 2) - (6 * uimult))) {
				ValidateInput userInput = new ValidateInput("Set the X-axis Minimum Value", "Minimum:", str(currentGraph.xMin()));
				userInput.setErrorMessage("Error\nInvalid x-axis minimum value entered.\nPlease input a number less than the maximum x-axis value.");
				if (userInput.checkFloat(userInput.LT, currentGraph.xMax())) {
					graphA.xMin(userInput.getFloat());
					graphB.xMin(userInput.getFloat());
					graphC.xMin(userInput.getFloat());
					graphD.xMin(userInput.getFloat());
				} 
				redrawContent = redrawUI = true;
			}

			// Change X axis maximum value
			else if (((autoAxis != 2) || (customXaxis < 0)) && menuXclick(xcoord, iL + (iW / 2) + PApplet.parseInt(6 * uimult), (iW / 2) - PApplet.parseInt(6 * uimult))) {
				ValidateInput userInput = new ValidateInput("Set the X-axis Maximum Value", "Maximum:", str(currentGraph.xMax()));
				userInput.setErrorMessage("Error\nInvalid x-axis maximum value entered.\nPlease input a number greater than 0.");
				if (userInput.checkFloat(userInput.GT, 0)) {
					if (customXaxis >= 0) {
						graphA.xMax(userInput.getFloat());
						graphB.xMax(userInput.getFloat());
						graphC.xMax(userInput.getFloat());
						graphD.xMax(userInput.getFloat());
					} else {
						currentGraph.xMax(userInput.getFloat());
						sampleWindow[selectedGraph - 1] = PApplet.parseInt(xRate * abs(currentGraph.xMax() - currentGraph.xMin()));
					}
				} 
				redrawContent = redrawUI = true;
			}
		}

		// Update Y axis scaling
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 6.5f, iL, iW) && (autoAxis != 2)) {
			Graph currentGraph;
			if (selectedGraph == 2) currentGraph = graphB;
			else if (selectedGraph == 3) currentGraph = graphC;
			else if (selectedGraph == 4) currentGraph = graphD;
			else currentGraph = graphA;

			// Change Y axis minimum value
			if (menuXclick(xcoord, iL, (iW / 2) - PApplet.parseInt(6 * uimult))) {
				ValidateInput userInput = new ValidateInput("Set the Y-axis Minimum Value", "Minimum:", str(currentGraph.yMin()));
				userInput.setErrorMessage("Error\nInvalid y-axis minimum value entered.\nThe number should be smaller the the maximum value.");
				if (userInput.checkFloat(userInput.LT, currentGraph.yMax())) {
					currentGraph.yMin(userInput.getFloat());
				} 
				redrawContent = redrawUI = true;
			}

			// Change Y axis maximum value
			else if (menuXclick(xcoord, iL + (iW / 2) + PApplet.parseInt(6 * uimult), (iW / 2) - PApplet.parseInt(6 * uimult))) {
				ValidateInput userInput = new ValidateInput("Set the Y-axis Maximum Value", "Maximum:", str(currentGraph.yMax()));
				userInput.setErrorMessage("Error\nInvalid y-axis maximum value entered.\nThe number should be larger the the minimum value.");
				if (userInput.checkFloat(userInput.GT, currentGraph.yMin())) {
					currentGraph.yMax(userInput.getFloat());
				} 
				redrawContent = redrawUI = true;
			}
		}

		// Turn auto-scaling on/off
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 7.5f, iL, iW)) {
			autoAxis++;
			if (autoAxis > 2) autoAxis = 0;
			redrawUI = true;
			redrawContent = true;
		}

		// Play/pause and reset
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 10, iL, iW)) {

			// Play
			if (menuXclick(xcoord, iL, iW / 4)) {
				if (isPaused) {
					pausedCount = dataTable.getRowCount();
					isPaused = false;
					redrawUI = true;
					redrawContent = true;
				}

			// Pause
			} else if (menuXclick(xcoord, iL + (iW / 4) + 1, iW / 4)) {
				if (!isPaused) {
					pausedCount = dataTable.getRowCount();
					isPaused = true;
					redrawUI = true;
					redrawContent = true;
				}
			
			// Clear graphs
			} else if (menuXclick(xcoord, iL + (iW / 2) + 1, iW / 2)) {
				// Reset the signal list
				dataTable.clearRows();
				drawFrom = 0;
				redrawUI = true;
				redrawContent = true;
			}
		}

		// Add a new input data column
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 11, iL, iW)){
			
			// Graph mode 1
			if (menuXclick(xcoord, iL + iW - PApplet.parseInt(80 * uimult), PApplet.parseInt(20 * uimult))) {
				graphMode = 1;
				graphA.setSize(cL, cR, cT, cB);
				redrawUI = true;
				redrawContent = true;
				if (selectedGraph > 1) {
					selectedGraph = 1;
					graphA.selected(true);
					graphB.selected(false);
					graphC.selected(false);
					graphD.selected(false);
				}
				for (int i = 0; i < graphAssignment.length; i++) {
					if (graphAssignment[i] > graphMode + 1) graphAssignment[i] = graphMode + 1;
				}
			
			// Graph mode 2
			} else if (menuXclick(xcoord, iL + iW - PApplet.parseInt(60 * uimult), PApplet.parseInt(20 * uimult))) {
				graphMode = 2;
				redrawUI = true;
				redrawContent = true;
				graphA.setSize(cL, cR, cT, (cT + cB) / 2);
				graphB.setSize(cL, cR, (cT + cB) / 2, cB);
				if (selectedGraph > 2) {
					selectedGraph = 2;
					graphA.selected(false);
					graphB.selected(true);
					graphC.selected(false);
					graphD.selected(false);
				}
				for (int i = 0; i < graphAssignment.length; i++) {
					if (graphAssignment[i] > graphMode + 1) graphAssignment[i] = graphMode + 1;
				}

			// Graph mode 3
			} else if (menuXclick(xcoord, iL + iW - PApplet.parseInt(40 * uimult), PApplet.parseInt(20 * uimult))) {
				graphMode = 3;
				redrawUI = true;
				redrawContent = true;
				graphA.setSize(cL, (cL + cR) / 2, cT, (cT + cB) / 2);
				graphB.setSize(cL, cR, (cT + cB) / 2, cB);
				graphC.setSize((cL + cR) / 2, cR, cT, (cT + cB) / 2);
				if (selectedGraph > 3) {
					selectedGraph = 3;
					graphA.selected(false);
					graphB.selected(false);
					graphC.selected(true);
					graphD.selected(false);
				}
				for (int i = 0; i < graphAssignment.length; i++) {
					if (graphAssignment[i] > graphMode + 1) graphAssignment[i] = graphMode + 1;
				}

			// Graph mode 4
			} else if (menuXclick(xcoord, iL + iW - PApplet.parseInt(20 * uimult), PApplet.parseInt(20 * uimult))) {
				graphMode = 4;
				redrawUI = true;
				redrawContent = true;
				graphA.setSize(cL, (cL + cR) / 2, cT, (cT + cB) / 2);
				graphB.setSize(cL, (cL + cR) / 2, (cT + cB) / 2, cB);
				graphC.setSize((cL + cR) / 2, cR, cT, (cT + cB) / 2);
				graphD.setSize((cL + cR) / 2, cR, (cT + cB) / 2, cB);
				for (int i = 0; i < graphAssignment.length; i++) {
					if (graphAssignment[i] > graphMode + 1) graphAssignment[i] = graphMode + 1;
				}
			}

			//final String colname = showInputDialog("Column Name:");
			//if (colname != null){
			//    dataColumns = append(dataColumns, colname);
			//    dataTable.addColumn("Untitled-" + dataColumns.length);
			//    graphAssignment = append(graphAssignment, 1);
			//    redrawUI = true;
			//}
		}

		// Change the input data rate
		else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 12, iL, iW)){
			ValidateInput userInput = new ValidateInput("Received Data Update Rate","Frequency (Hz):\n(Leave blank for automatic)", str(graphA.frequency()));
			userInput.setErrorMessage("Error\nInvalid frequency entered.\nThe rate can only be a number between 0 - 10,000 Hz");
			if (userInput.isEmpty()) {
				autoFrequency = true;
				frequencyCounter = 0;
				frequencyTimer = 0;
				redrawUI = true;

			} else if (userInput.checkFloat(userInput.GT, 0, userInput.LTE, 10000)) {
				autoFrequency = false;
				xRate = userInput.getFloat();
				graphA.frequency(xRate);
				graphB.frequency(xRate);
				graphC.frequency(xRate);
				graphD.frequency(xRate);
				sampleWindow[0] = PApplet.parseInt(xRate * abs(graphA.xMax() - graphA.xMin()));
				sampleWindow[1] = PApplet.parseInt(xRate * abs(graphB.xMax() - graphB.xMin()));
				sampleWindow[2] = PApplet.parseInt(xRate * abs(graphC.xMax() - graphC.xMin()));
				sampleWindow[3] = PApplet.parseInt(xRate * abs(graphD.xMax() - graphD.xMin()));

				redrawContent = true;
				redrawUI = true;
			}

			if (customXaxis != -1) {
				graphAssignment[customXaxis] = 1;
				customXaxis = -1;
				autoAxis = 2;
				graphA.xMin(0);
				graphB.xMin(0);
				graphC.xMin(0);
				graphD.xMin(0);
				graphA.xMax(30);
				graphB.xMax(30);
				graphC.xMax(30);
				graphD.xMax(30);
				sampleWindow[0] = PApplet.parseInt(xRate * abs(graphA.xMax() - graphA.xMin()));
				sampleWindow[1] = PApplet.parseInt(xRate * abs(graphB.xMax() - graphB.xMin()));
				sampleWindow[2] = PApplet.parseInt(xRate * abs(graphC.xMax() - graphC.xMin()));
				sampleWindow[3] = PApplet.parseInt(xRate * abs(graphD.xMax() - graphD.xMin()));
				graphA.xAxisTitle("Time (s)");
				graphB.xAxisTitle("Time (s)");
				graphC.xAxisTitle("Time (s)");
				graphD.xAxisTitle("Time (s)");
				redrawContent = true;
				redrawUI = true;
			}
		}
		
		else {
			float tHnow = 13;

			for (int j = 0; j < graphMode + 1; j++) {
				tHnow += 0.75f;

				// List of Data Columns
				for(int i = 0; i < dataColumns.length; i++){

					if (graphAssignment[i] == j + 1) {

						if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)){

							// Down arrow
							if (menuXclick(xcoord, iL + iW - PApplet.parseInt(20 * uimult), PApplet.parseInt(20 * uimult))) {
								graphAssignment[i]++;
								if (graphAssignment[i] > graphMode + 1) graphAssignment[i] = graphMode + 1;
								redrawUI = true;
								redrawContent = true;
							}

							// Up arrow
							else if (menuXclick(xcoord, iL + iW - PApplet.parseInt(40 * uimult), PApplet.parseInt(20 * uimult))) {
								graphAssignment[i]--;
								if (graphAssignment[i] < 1) {
									if (customXaxis >= 0) graphAssignment[customXaxis] = 1;
									autoAxis = 2;
									customXaxis = i;
									sampleWindow[0] = 1000;
									sampleWindow[1] = 1000;
									sampleWindow[2] = 1000;
									sampleWindow[3] = 1000;
									graphA.xAxisTitle(dataColumns[i]);
									graphB.xAxisTitle(dataColumns[i]);
									graphC.xAxisTitle(dataColumns[i]);
									graphD.xAxisTitle(dataColumns[i]);
									graphAssignment[i] = -1;
									drawNewData();
								}
								redrawUI = true;
								redrawContent = true;
							}

							// Change name of column
							else {
								final String colname = myShowInputDialog("Set the Data Signal Name", "Name:", dataColumns[i]);
								if (colname != null && colname.length() > 0) {
									dataColumns[i] = colname;
									redrawUI = true;
								}
							}
						}
					
						tHnow++;
					}
				}
			}
		}
	}


	/**
	 * Check whether it is safe to exit the program
	 *
	 * @return True if the are no tasks active, false otherwise
	 */
	public boolean checkSafeExit() {
		if (recordData) return false;
		return true;
	}


	/**
	 * End any active processes and safely exit the tab
	 */
	public void performExit() {
		if (recordData) stopRecording();
	}
}
/* * * * * * * * * * * * * * * * * * * * * * *
 * SERIAL MONITOR CLASS
 * implements TabAPI for Processing Grapher
 *
 * @file     SerialMonitor.pde
 * @brief    A serial monitor tab for UART comms
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    SerialMonitor
 * @see      TabAPI <ProcessingGrapher.pde>
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */


class SerialMonitor implements TabAPI {

	int cL, cR, cT, cB;
	int border, padding, yTextHeight;
	int msgB;

	PrintWriter dataWriter;

	int msgBorder;
	int msgSize;
	int menuScroll;
	int menuHeight;
	int menuLevel;

	String name;
	String outputfile;

	boolean recordData;
	int recordCounter;
	int fileCounter;
	final int maxFileRows = 50000;
	ArrayList<SerialTag> serialTags = new ArrayList<SerialTag>();
	
	int displayRows;
	final int maxBuffer = 50000;
	int scrollUp;
	ScrollBar serialScroll = new ScrollBar(ScrollBar.VERTICAL, ScrollBar.INVERT);
	ScrollBar sidebarScroll = new ScrollBar(ScrollBar.VERTICAL, ScrollBar.NORMAL);

	String msgText= "";
	int cursorPosition;
	int[] msgTextBounds = {0,0};
	boolean autoScroll;
	boolean tabIsVisible;
	boolean infoTextVisible = false;
	int msgBtnSize = 0;

	int previousColor = c_red;
	int hueColor = c_red;
	int newColor = c_red;
	int colorSelector = 0;

	final int[] baudRateList = {300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 74880, 115200, 230400, 250000};;
	SerialMessages serialBuffer;                              //! Ring buffer used to store serial messages
	//PGraphics serialGraphics;

	TextSelection inputTextSelection = new TextSelection();   //! Selection/highlighting of serial input text area
	TextSelection serialTextSelection = new TextSelection();  //! Selection/highlighting of serial monitor text area
	int activeArea = 0;


	/**
	 * Constructor
	 *
	 * @param  setname Name of the tab
	 * @param  left    Tab area left x-coordinate
	 * @param  right   Tab area right x-coordinate
	 * @param  top     Tab area top y-coordinate
	 * @param  bottom  Tab area bottom y-coordinate
	 */
	SerialMonitor(String setname, int left, int right, int top, int bottom) {
		name = setname;
		tabIsVisible = false;
		
		cL = left;
		cR = right;
		cT = top;
		cB = bottom;

		msgBorder = round(15 * uimult);
		msgSize = round(2*(msgBorder) + (30 * uimult));
		border = round(15 * uimult);
		padding = round(5 * uimult);
		yTextHeight = round(12 * uimult) + padding;

		msgB = cT + msgSize;
		outputfile = "No File Set";
		recordData = false;
		recordCounter = 0;
		fileCounter = 0;
		scrollUp = 0;
		displayRows = 0;
		cursorPosition = 0;
		menuScroll = 0;
		menuHeight = cB - cT - 1; 
		menuLevel = 0;
		autoScroll = true;

		serialTags.add(new SerialTag("SENT:", c_colorlist[0]));
		serialTags.add(new SerialTag("[Info]", c_colorlist[1]));

		serialBuffer = new SerialMessages(maxBuffer);
		//serialBuffer = new StringList();
		serialBuffer.append("--- PROCESSING SERIAL MONITOR ---");
		if (showInstructions) {
			infoTextVisible = true;
			serialBuffer.append("");
			serialBuffer.append("[Info] Connecting to a Serial Device");
			serialBuffer.append("1. In the right sidebar, select the COM port");
			serialBuffer.append("2. Set the correct baud rate for the communication");
			serialBuffer.append("3. Click the 'Connect' button to begin communication");
			serialBuffer.append("");
			serialBuffer.append("[Info] Using the Serial Monitor");
			serialBuffer.append("1. To send a message, start typing; press the enter key to send");
			serialBuffer.append("2. Scroll using the scroll wheel, up/down arrow and page up/down keys");
			serialBuffer.append("3. Press 'Clear Terminal' to remove all serial monitor messages");
			serialBuffer.append("4. Press CTRL+ or CTRL- to increase or decrease interface size");
			serialBuffer.append("");
			serialBuffer.append("[Info] Recording Serial Communication");
			serialBuffer.append("1. Click 'Set Output File' to set the save file location");
			serialBuffer.append("2. Press 'Start Recording' button to initiate the recording");
			serialBuffer.append("3. Press 'Stop Recording' to stop and save the recording");
			serialBuffer.append("");
			serialBuffer.append("[Info] Adding Visual Colour Tags");
			serialBuffer.append("Tags can be used to highlight lines containing specific text");
			serialBuffer.append("1. Click 'Add New Tag' and type the text to be detected");
			serialBuffer.append("2. Now any line containing this text will change colour");
			serialBuffer.append("3. In the right sidebar, Tags can be deleted and modified");
			serialBuffer.append("");
		}
	}


	/**
	 * Get the name of the current tab
	 *
	 * @return Tab name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Set tab as being active or hidden
	 * 
	 * @param  newState True = active, false = hidden
	 */
	public void setVisibility(boolean newState) {
		tabIsVisible = newState;
	}


	/**
	 * Set current side menu level
	 * 
	 * @param  newLevel The new menu level
	 */
	public void setMenuLevel(int newLevel) {
		menuLevel = newLevel;
	}


	/**
	 * Redraw all tab content
	 */
	public void drawContent() {
		
		// Draw the message box
		rectMode(CORNERS);
		noStroke();
		fill(c_background);
		rect(cL, cT, cR, cT + msgB);
		fill(c_serial_message_box);
		rect(cL + msgBorder, cT + msgBorder, cR - msgBorder, msgB - msgBorder);

		textFont(base_font);
		textAlign(LEFT, TOP);

		// Message text button
		String msgBtnText = "Send:";
		msgBtnSize = PApplet.parseInt(textWidth(msgBtnText)); 

		fill(c_terminal_text);
		text(msgBtnText, cL + 2*msgBorder, cT + msgBorder + 6.5f * uimult);

		fill(c_background);
		stroke(c_background);
		strokeWeight(1 * uimult);
		line(cL + 3*msgBorder + msgBtnSize, cT + msgBorder, cL + 3*msgBorder + msgBtnSize, msgB - msgBorder);

		// Ensure cursor is within bounds
		if (cursorPosition < 0) cursorPosition = 0;
		else if (cursorPosition > msgText.length()) cursorPosition = msgText.length();

		// Figure out where the cursor is and how much of the message to show
		textFont(mono_font);

		final float charWidth = textWidth("a");
		final int maxChars = floor((cR -cL - 6*msgBorder - msgBtnSize) / charWidth);

		if (cursorPosition > msgTextBounds[1]) {
			msgTextBounds[0] += cursorPosition - msgTextBounds[1];
			msgTextBounds[1] = cursorPosition;

		} else if (cursorPosition < msgTextBounds[0]) {
			msgTextBounds[1] -= msgTextBounds[0] - cursorPosition;
			msgTextBounds[0] = cursorPosition;
		}

		if (msgTextBounds[1] - msgTextBounds[0] < maxChars || msgTextBounds[1] > msgText.length()) {
			msgTextBounds[1] = msgText.length();
			msgTextBounds[0] = msgText.length() - maxChars;
		}

		// Validate the bounds
		if (msgTextBounds[0] < 0) msgTextBounds[0] = 0;
		if (msgTextBounds[1] < 0) msgTextBounds[1] = 0; 

		// If text is highlighted, draw the background
		if (inputTextSelection.valid && (inputTextSelection.startChar <= msgTextBounds[1]) && (inputTextSelection.endChar >= msgTextBounds[0])) {
			fill(c_highlight_background);
			noStroke();
			rectMode(CORNERS);
			float leftHighlight = cL + 4*msgBorder + msgBtnSize;
			float rightHighlight = leftHighlight + (msgTextBounds[1] - msgTextBounds[0]) * charWidth;
			if (inputTextSelection.startChar > msgTextBounds[0]) leftHighlight += (inputTextSelection.startChar - msgTextBounds[0]) * charWidth;
			if (inputTextSelection.endChar < msgTextBounds[1]) rightHighlight -= (msgTextBounds[1] - inputTextSelection.endChar) * charWidth;
			rect(leftHighlight, cT + msgBorder + round(9 * uimult), rightHighlight, cT + msgBorder + round(9 * uimult) + yTextHeight);
		}

		// Draw cursor
		if (!inputTextSelection.valid && activeArea == 0) {
			fill(c_terminal_text);
			stroke(c_terminal_text);
			rectMode(CORNER);
			rect(cL + 4*msgBorder + msgBtnSize + (cursorPosition - msgTextBounds[0]) * charWidth + round(1*uimult), cT + msgBorder + round(9 * uimult), round(2*uimult), round(13 * uimult));
		}

		// Message text
		rectMode(CORNERS);
		fill(c_message_text);
		text(msgText.substring(msgTextBounds[0], msgTextBounds[1]), cL + 4*msgBorder + msgBtnSize, cT + msgBorder + round(9 * uimult));//, cR - 2*msgBorder, msgB - msgBorder);

		// Draw arrows to indicate if there is any hidden text
		if (msgTextBounds[0] > 0) {
			final int halfWay = cT + msgBorder + round(15 * uimult);
			final int frontPos = cL + round(3.25f*msgBorder) + msgBtnSize;
			final int dist4 = round(4 * uimult);
			final int dist2 = round(2 * uimult);

			fill(c_terminal_text);
			stroke(c_terminal_text);
			triangle(frontPos, halfWay, frontPos + dist4, halfWay + dist2, frontPos + dist4, halfWay - dist2);
		}

		if (msgTextBounds[1] < msgText.length()) {
			final int halfWay = cT + msgBorder + round(15 * uimult);
			final int backPos = cR - round(1.25f*msgBorder);
			final int dist4 = round(4 * uimult);
			final int dist2 = round(2 * uimult);

			fill(c_terminal_text);
			stroke(c_terminal_text);
			triangle(backPos, halfWay, backPos - dist4, halfWay + dist2, backPos - dist4, halfWay - dist2);
		}

		// Draw the terminal
		drawNewData();
	}


	/**
	 * Draw new tab data
	 */
	public void drawNewData() {

		// Clear the content area
		rectMode(CORNER);
		noStroke();
		fill(c_background);
		rect(cL, msgB, cR - cL, cB - msgB);
		
		// Figure out how many rows of text can be displayed
		displayRows = PApplet.parseInt((cB - msgB - border) / yTextHeight);
		while (displayRows > serialBuffer.size() - scrollUp && scrollUp > 0) {
			scrollUp--;
			displayRows = serialBuffer.size() - scrollUp;
		}
		if (displayRows > serialBuffer.size() - scrollUp) displayRows = serialBuffer.size() - scrollUp;
		int totalHeight = displayRows * yTextHeight;

		// Draw left bar
		fill(c_serial_message_box);
		rect(cL, msgB, border/2, totalHeight);
		textAlign(LEFT, TOP);
		textFont(mono_font);

		// Figure out size and position of vertical scroll bar indicator
		if (serialBuffer.size() > 0) {
			int scrollbarSize = totalHeight * displayRows / serialBuffer.size();
			if (scrollbarSize < yTextHeight) scrollbarSize = yTextHeight;
			int scrollbarOffset = PApplet.parseInt((totalHeight - scrollbarSize) * (1 - (scrollUp / PApplet.parseFloat(serialBuffer.size() - displayRows))));
			fill(c_terminal_text);
			rect(cL, msgB + scrollbarOffset, border/2, scrollbarSize);
			serialScroll.update(serialBuffer.size(), totalHeight, cL, msgB + scrollbarOffset, border / 2, scrollbarSize);

			totalHeight -= yTextHeight;

			final float charWidth = textWidth("a");
			final int maxChars = floor((cR - 2*cL - 5*border) / charWidth);

			// Now print the text
			for (int i = 0; i < displayRows; i++) {

				int textColor = c_terminal_text;
				int textIndex = serialBuffer.size() - 1 - i - scrollUp;
				if (textIndex < 0) textIndex = 0;
				String textRow = serialBuffer.get(textIndex);

				// Figure out the text colour
				for (SerialTag curTag : serialTags) {
					if (textRow.contains(curTag.tagText)) {
						textColor = curTag.tagColor;
					}
				}

				// Check wheter text length exceeds width of the window
				if (textRow.length() > maxChars) {
					textRow = textRow.substring(0, maxChars - 1);

					fill(c_terminal_text);
					text(">>", cR - 2*border, msgB + totalHeight);
				}

				// If text is highlighted, draw the background
				if (serialTextSelection.valid) {
					fill(c_highlight_background);
					if (serialTextSelection.startLine < textIndex && serialTextSelection.endLine > textIndex) {
						rect(cL + 2*border, msgB + totalHeight, textRow.length() * charWidth, yTextHeight);
					} else if (serialTextSelection.startLine == textIndex && serialTextSelection.endLine == textIndex) {
						rect(cL + 2*border + charWidth * serialTextSelection.startChar, msgB + totalHeight, (serialTextSelection.endChar - serialTextSelection.startChar) * charWidth, yTextHeight);
					} else if (serialTextSelection.startLine == textIndex && serialTextSelection.endLine > textIndex) {
						rect(cL + 2*border + charWidth * serialTextSelection.startChar, msgB + totalHeight, (textRow.length() - serialTextSelection.startChar) * charWidth, yTextHeight);
					} else if (serialTextSelection.startLine < textIndex && serialTextSelection.endLine == textIndex) {
						rect(cL + 2*border, msgB + totalHeight, serialTextSelection.endChar * charWidth, yTextHeight);
					}
				}

				// Print the text
				fill(textColor);
				text(textRow, cL + 2*border, msgB + totalHeight);//, cR - cL - 3*border, yTextHeight);

				totalHeight -= yTextHeight;
			}

			// If scrolled up, draw a return to bottom button
			if (scrollUp > 0) {
				fill(c_sidebar);
				rect(cR - (40 * uimult), cB - (40 * uimult), (30 * uimult), (30 * uimult));
				fill(c_background);
				strokeWeight(1 * uimult);
				stroke(c_terminal_text);
				triangle(cR - (30*uimult), cB - (29*uimult), cR - (20*uimult), cB - (29*uimult), cR - (25*uimult), cB - (20*uimult));
			}
		} else {
			if (recordData) {
				fill(c_terminal_text);
				text("Serial monitor has been cleared\n(A new save file has just been opened)", cL + 2*border, msgB);
			} else {
				serialBuffer.append("--- PROCESSING SERIAL MONITOR ---");
				drawNewData = true;
			}
		}

		textFont(base_font);
	}
	

	/**
	 * Change tab content area dimensions
	 *
	 * @param  newL New left x-coordinate
	 * @param  newR New right x-coordinate
	 * @param  newT New top y-coordinate
	 * @param  newB new bottom y-coordinate
	 */
	public void changeSize(int newL, int newR, int newT, int newB) {
		cL = newL;
		cR = newR;
		cT = newT;
		cB = newB;

		msgBorder = round(15 * uimult);
		msgSize = round(2*(msgBorder) + (30 * uimult));
		msgB = cT + msgSize;
		border = round(15 * uimult);
		padding = round(5 * uimult);
		yTextHeight = round(12 * uimult) + padding;
		//drawContent();
	}


	/**
	 * Change CSV data file location
	 *
	 * @param  newoutput Absolute path to the new file location
	 */
	public void setOutput(String newoutput) {
		if (newoutput != "No File Set") {
			// Ensure file type is *.csv
			final int dotPos = newoutput.lastIndexOf(".");
			if (dotPos > 0) newoutput = newoutput.substring(0, dotPos);
			newoutput = newoutput + ".txt";

			// Test whether this file is actually accessible
			if (saveFile(newoutput) == null) {
				alertMessage("Error\nUnable to access the selected output file location; is this actually a writable location?\n" + newoutput);
				newoutput = "No File Set";
			}
		}
		outputfile = newoutput;
	}


	/**
	 * Get the current CSV data file location
	 *
	 * @return Absolute path to the data file
	 */
	public String getOutput(){
		return outputfile;
	}


	/** 
	 * Start recording new serial data points to file
	 */
	public void startRecording() {
		try {
			// Open the writer
			File filePath = saveFile(outputfile);
			dataWriter = createWriter(filePath);
			serialBuffer.clear();
			scrollUp = 0;
			recordCounter = 0;
			fileCounter = 0;
			recordData = true;
			redrawUI = true;
			drawNewData = true;
		} catch (Exception e) {
			println(e);
			alertMessage("Error\nUnable to create the output file:\n" + e);
		}
	}


	/**
	 * Stop recording data points to file
	 */
	public void stopRecording(){
		recordData = false;

		try {
			dataWriter.flush();
			dataWriter.close();
			alertMessage("Success\nRecorded " + ((fileCounter * 10000) + recordCounter) + " entries to " + (fileCounter + 1) + " TXT file(s)");
		} catch (Exception e) {
			println(e);
			alertMessage("Error\nUnable to save the output file:\n" + e);
		}

		outputfile = "No File Set";
		if (tabIsVisible) redrawUI = true;
	}


	/**
	 * Function called when a serial device has connected/disconnected
	 *
	 * @param  status True if a device has connected, false if disconnected
	 */
	public void connectionEvent (boolean status) {
		if (status && infoTextVisible) {
			infoTextVisible = false;
			if (!recordData) {
				serialBuffer.clear();
				serialBuffer.append("--- PROCESSING SERIAL MONITOR ---");
				scrollUp = 0;
				serialTextSelection.setVisibility(false);
				drawNewData = true;
			}
		}

		// On disconnect
		if (!status) {
			// Stop recording any data
			if (recordData) stopRecording();
		}
	}

	/**
	 * Parse new data points received from serial port
	 *
	 * @param  inputData String containing data points separated by commas
	 * @param  graphable True if data in message can be plotted on a graph
	 */
	public void parsePortData(String inputData, boolean graphable) {

		serialBuffer.append(inputData, graphable);
		if (!autoScroll && scrollUp < maxBuffer) scrollUp++;

		// --- Data Recording ---
		if(recordData) {
			recordCounter++;

			try {
				dataWriter.println(inputData);
				if (dataWriter.checkError()) {
					emergencyOutputSave(true);
				}

				// Separate data into files once the max number of rows has been reached
				if (recordCounter >= maxFileRows) {
					dataWriter.close();
					fileCounter++;
					recordCounter = 0;

					final int dotPos = outputfile.lastIndexOf(".");
					final String nextoutputfile = outputfile.substring(0, dotPos) + "-" + (fileCounter + 1) + ".txt";
					File filePath = saveFile(nextoutputfile);
					dataWriter = createWriter(filePath);

					serialBuffer.clear();
					scrollUp = 0;
					redrawUI = true;
				}
			} catch (Exception e) {
				emergencyOutputSave(true);
			}
		} else {
			// --- Data Buffer ---
			//if (serialBuffer.size() >= maxBuffer) {
			//	serialBuffer.remove(0);
			//}
		}

		if (!serialBuffer.getVisibility() && graphable) {
			return;
		} else if (tabIsVisible) {
			drawNewData = true;
		}
	}


	/**
	 * Recover from an error when recording data to file
	 *
	 * @param  continueRecording If we want to continue recording after dealing with the error
	 */
	public void emergencyOutputSave(boolean continueRecording) {
		dataWriter.close();

		// Figure out name for new backup file
		String[] tempSplit = split(outputfile, '/');
		int dotPos = tempSplit[tempSplit.length - 1].lastIndexOf(".");
		String nextoutputfile = tempSplit[tempSplit.length - 1].substring(0, dotPos);
		outputfile = nextoutputfile + "-backup.txt";

		String emergencysavefile = nextoutputfile + "-backup-" + (fileCounter + 1) + ".txt";

		try {
			// Backup the existing data
			File filePath = saveFile(emergencysavefile);
			dataWriter = createWriter(filePath);
			for (int i = 0; i < serialBuffer.size(); i++) {
				dataWriter.println(serialBuffer.get(i));
			}
			if (dataWriter.checkError()) continueRecording = false;
			dataWriter.close();

			// If we want to continue recording, try setting up a new output file
			if (continueRecording) {
				fileCounter++;
				nextoutputfile = nextoutputfile + "-backup-" + (fileCounter + 1) + ".txt";

				filePath = saveFile(nextoutputfile);

				// If new output file was successfully opened, only show a Warning message
				if (filePath != null) {
					alertMessage("Warning\nAn issue occurred when trying to save new data to the ouput file.\n1. A backup of all the data has been created\n2. Data is still being recorded (to a new file)\n3. The files are in the same directory as ProcessingGrapher.exe");
					dataWriter = createWriter(filePath);
					serialBuffer.clear();
					scrollUp = 0;

				// If not, show an error message that the recording has stopped
				} else {
					recordData = false;
					alertMessage("Error - Recording Stopped\nAn issue occurred when trying to save new data to the ouput file.\n1. A backup of all the data has been created\n2. The files are in the same directory as ProcessingGrapher.exe");
				}

			// If we don't want to continue, show a simple error message
			} else {
				recordData = false;
				alertMessage("Error\nAn issue occurred when trying to save new data to the ouput file.\n1. Data recording has been stopped\n2. A backup of all the data has been created\n3. The backup is in the same directory as ProcessingGrapher.exe");
			}

			redrawUI = true;
			drawNewData = true;

		// If something went wrong in the error recovery process, show a critical error message
		} catch (Exception e) {
			dataWriter.close();
			recordData = false;
			redrawUI = true;
			alertMessage("Critical Error\nAn issue occurred when trying to save new data to the ouput file.\nData backup was also unsuccessful, so some data may have been lost...\n" + e);
		}
	}


	/**
	 * Draw the sidebar menu for the current tab
	 */
	public void drawSidebar () {

		// Calculate sizing of sidebar
		// Do this here so commands below are simplified
		int sT = cT;
		int sL = cR;
		final int sW = width - cR;
		final int sH = height - cT;

		final int uH = round(sideItemHeight * uimult);
		final int tH = round((sideItemHeight - 8) * uimult);
		final int iH = round((sideItemHeight - 5) * uimult);
		int iL = round(sL + (10 * uimult));
		final int iW = round(sW - (20 * uimult));

		String[] ports = Serial.list();

		if (menuLevel == 0)	menuHeight = round((15 + serialTags.size()) * uH);
		else if (menuLevel == 1) menuHeight = round((3 + ports.length) * uH);
		else if (menuLevel == 2) menuHeight = round((4 + baudRateList.length) * uH);
		else if (menuLevel == 3) menuHeight = round(9 * uH + iW);

		// Figure out if scrolling of the menu is necessary
		if (menuHeight > sH) {
			if (menuScroll == -1) menuScroll = 0;
			else if (menuScroll > menuHeight - sH) menuScroll = menuHeight - sH;

			// Draw left bar
			fill(c_serial_message_box);
			rect(width - round(15 * uimult) / 2, sT, round(15 * uimult) / 2, sH);

			// Figure out size and position of scroll bar indicator
			int scrollbarSize = sH - round(sH * PApplet.parseFloat(menuHeight - sH) / menuHeight);
			if (scrollbarSize < uH) scrollbarSize = uH;
			int scrollbarOffset = round((sH - scrollbarSize) * (menuScroll / PApplet.parseFloat(menuHeight - sH)));
			fill(c_terminal_text);
			rect(width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);
			sidebarScroll.update(menuHeight, sH, width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);

			sT -= menuScroll;
			sL -= round(15 * uimult) / 4;
			iL -= round(15 * uimult) / 4;
		} else {
			menuScroll = -1;
		}

		// Root sidebar menu
		if (menuLevel == 0) {
			// Connect or Disconnect to COM Port
			drawHeading("Serial Port", iL, sT + (uH * 0), iW, tH);
			if (ports.length == 0) drawDatabox("Port: None", iL, sT + (uH * 1), iW, iH, tH);
			else if (ports.length <= portNumber) drawDatabox("Port: Invalid", iL, sT + (uH * 1), iW, iH, tH);
			else drawDatabox("Port: " + constrainString(ports[portNumber], iW - textWidth("Port: ") - (15 * uimult)), iL, sT + (uH * 1), iW, iH, tH);
			drawDatabox("Baud: " + baudRate, iL, sT + (uH * 2), iW, iH, tH);
			drawButton((serialConnected)? "Disconnect":"Connect", (serialConnected)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 3), iW, iH, tH);

			// Save to File
			drawHeading("Record Messages", iL, sT + (uH * 4.5f), iW, tH);
			if (outputfile == "No File Set" || outputfile == "") {
				drawButton("Set Output File", c_sidebar_button, iL, sT + (uH * 5.5f), iW, iH, tH);
				drawDatabox("Start Recording", c_idletab_text, iL, sT + (uH * 6.5f), iW, iH, tH);
			} else {
				String[] fileParts = split(outputfile, '/');
				String fileName = fileParts[fileParts.length - 1];

				if (recordData) {
					drawDatabox(fileName, c_idletab_text, iL, sT + (uH * 5.5f), iW, iH, tH);
					drawButton("Stop Recording", c_sidebar_accent, iL, sT + (uH * 6.5f), iW, iH, tH);
				} else {
					drawDatabox(fileName, c_sidebar_text, iL, sT + (uH * 5.5f), iW, iH, tH);
					drawButton("Start Recording", c_sidebar_button, iL, sT + (uH * 6.5f), iW, iH, tH);
				}
			}

			// Input Data Columns
			drawHeading("Terminal Options", iL, sT + (uH * 8), iW, tH);
			if (recordData) drawDatabox("Clear Terminal", c_idletab_text, iL, sT + (uH * 9), iW, iH, tH);
			else drawButton("Clear Terminal", c_sidebar_button, iL, sT + (uH * 9), iW, iH, tH);
			drawButton((autoScroll)? "Autoscroll: On":"Autoscroll: Off", (autoScroll)? c_sidebar_button:c_sidebar_accent, iL, sT + (uH * 10), iW, iH, tH);
			drawButton((serialBuffer.getVisibility())? "Graph Data: Shown":"Graph Data: Hidden", (serialBuffer.getVisibility())? c_sidebar_button:c_sidebar_accent, iL, sT + (uH * 11), iW, iH, tH);

			// Input Data Columns
			drawHeading("Colour Tags", iL, sT + (uH * 12.5f), iW, tH);
			drawButton("Add New Tag", c_sidebar_button, iL, sT + (uH * 13.5f), iW, iH, tH);

			float tHnow = 14.5f;

			// List of Data Columns
			for (SerialTag curTag : serialTags) {
				// Column name
				drawDatabox(curTag.tagText, iL, sT + (uH * tHnow), iW - (40 * uimult), iH, tH);

				// Remove column button
				drawButton("✕", c_sidebar_button, iL + iW - (20 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);

				// Swap column with one being listed above button
				int buttonColor = curTag.tagColor;
				drawButton("", c_sidebar, buttonColor, iL + iW - (40 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);

				drawRectangle(c_sidebar_divider, iL + iW - (20 * uimult), sT + (uH * tHnow) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
				tHnow++;
			}

		// Serial port select menu
		} else if (menuLevel == 1) {
			drawHeading("Select a Port", iL, sT + (uH * 0), iW, tH);

			float tHnow = 1;
			if (ports.length == 0) {
				drawText("No devices detected", c_sidebar_text, iL, sT + (uH * tHnow), iW, iH);
				tHnow += 1;
			} else {
				for (int i = 0; i < ports.length; i++) {
					drawButton(constrainString(ports[i], iW - (10 * uimult)), c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
					tHnow += 1;
				}
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_accent, iL, sT + (uH * tHnow), iW, iH, tH);

		// Baud rate selection menu
		} else if (menuLevel == 2) {
			drawHeading("Select Baud Rate", iL, sT + (uH * 0), iW, tH);

			float tHnow = 1;
			for (int i = 0; i < baudRateList.length; i++) {
				drawButton(str(baudRateList[i]), c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("More Options", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
			drawButton("Cancel", c_sidebar_accent, iL, sT + (uH * (tHnow + 1)), iW, iH, tH);
		
		// Colour picker menu
		} else if (menuLevel == 3) {
			drawHeading("Select a Colour", iL, sT + (uH * 0), iW, tH);
			drawColorSelector(hueColor, iL, sT + (uH * 1), iW, iW); 
			drawHeading("Set Brightness", iL, sT + (uH * 1.5f) + iW, iW, tH);
			drawColorBox2D(newColor, c_white, hueColor, iL, sT + (uH * 2.5f) + iW, iW / 2, iH);
			drawColorBox2D(newColor, hueColor, c_black, iL + (iW / 2), sT + (uH * 2.5f) + iW, iW / 2, iH);
			drawHeading("Colour Preview", iL, sT + (uH * 4) + iW, iW, tH);
			drawText("New", c_idletab_text, iL, sT + (uH * 4.75f) + iW, iW / 2, iH);
			drawText("Old", c_idletab_text, iL + (iW / 2) + (2 * uimult), sT + (uH * 4.75f) + iW, iW / 2, iH);
			drawButton("", newColor, iL, sT + (uH * 5.5f) + iW, (iW / 2) - (3 * uimult), iH, tH);
			drawButton("", previousColor, iL + (iW / 2) + (2 * uimult), sT + (uH * 5.5f) + iW, (iW / 2) - (2 * uimult), iH, tH);
			drawButton("Confirm", c_sidebar_button, iL, sT + (uH * 6.5f) + iW, iW, iH, tH);
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * 7.5f) + iW, iW, iH, tH);
		}
	}


	/**
	 * Draw the btoom information bar
	 */
	public void drawInfoBar() {
		int sW = width - cR;
		textAlign(LEFT, TOP);
		textFont(base_font);
		fill(c_status_bar);
		text("Output: " + constrainString(outputfile, width - sW - round(175 * uimult) - textWidth("Output: ")), 
			round(5 * uimult), height - round(bottombarHeight * uimult) + round(2*uimult));
	}


	/**
	 * Keyboard input handler function
	 *
	 * @param  key The character of the key that was pressed
	 */
	public void keyboardInput(char keyChar, int keyCodeInt, boolean codedKey) {

		// For standard characters, simply type them into the message box
		if (!codedKey && 32 <= keyChar && keyChar <= 126) {
			
			// If text is selected, overwrite it
			serialTextSelection.setVisibility(false);
			if (inputTextSelection.valid) {
				String msg = msgText.substring(0,inputTextSelection.startChar) + msgText.substring(inputTextSelection.endChar,msgText.length());
				msgText = msg;
				cursorPosition = inputTextSelection.startChar;
				inputTextSelection.setVisibility(false);
			}

			// Add the new text to the message string
			if (cursorPosition < msgText.length()) {
				if (cursorPosition == 0) {
					msgText = keyChar + msgText;
				} else {
					String msg = msgText.substring(0,cursorPosition) + keyChar;
					msg = msg + msgText.substring(cursorPosition,msgText.length());
					msgText = msg;
				}
			} else {
				msgText += keyChar;
			}
			cursorPosition++;
			activeArea = 0;
			redrawContent = true;

		// Test for all other keys in a slightly slower switch statement			
		} else {

			switch (keyCodeInt) {
				case ESC:
					if (menuLevel != 0) {
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					} else if (serialTextSelection.valid) {
						serialTextSelection.setVisibility(false);
						redrawContent = true;
					} else if (inputTextSelection.valid) {
						inputTextSelection.setVisibility(false);
						redrawContent = true;
					}
					break;
				case ENTER:
				case RETURN:
					if (msgText != ""){
						if (serialConnected) {
							serialSend(msgText);
						}
						msgText = "SENT: " + msgText;
						//serialBuffer = append(serialBuffer, msgText);
						serialBuffer.append(msgText);
						inputTextSelection.setVisibility(false);
						msgText = "";
						cursorPosition = 0;
						if (!autoScroll) scrollUp++;
						redrawContent = true;
					}
					break;

				case BACKSPACE:
					if (msgText != "") {
						if (inputTextSelection.valid) {
							String msg = msgText.substring(0,inputTextSelection.startChar) + msgText.substring(inputTextSelection.endChar,msgText.length());
							msgText = msg;
							cursorPosition = inputTextSelection.startChar;
							inputTextSelection.setVisibility(false);
						} else {
							if (cursorPosition < msgText.length() && cursorPosition > 0) {
								String msg = msgText.substring(0,cursorPosition-1) + msgText.substring(cursorPosition,msgText.length());
								msgText = msg;
								cursorPosition--;
							} else if (cursorPosition >= msgText.length() && msgText.length() > 1) {
								msgText = msgText.substring(0, msgText.length()-1);
								cursorPosition--;
								if (cursorPosition < 0) cursorPosition = 0;
							} else if (cursorPosition >= msgText.length() && msgText.length() <= 1) {
								msgText = "";
								cursorPosition = 0;
							}
						}
						activeArea = 0;
						redrawContent = true;
					}
					break;

				case DELETE:
					if (msgText != "") {
						if (inputTextSelection.valid) {
							String msg = msgText.substring(0,inputTextSelection.startChar) + msgText.substring(inputTextSelection.endChar,msgText.length());
							msgText = msg;
							cursorPosition = inputTextSelection.startChar;
							inputTextSelection.setVisibility(false);
						} else {
							if (cursorPosition + 1 < msgText.length() && cursorPosition > 0) {
								String msg = msgText.substring(0,cursorPosition) + msgText.substring(cursorPosition + 1,msgText.length());
								msgText = msg;
							} else if (cursorPosition + 1 == msgText.length() && msgText.length() > 1) {
								msgText = msgText.substring(0, msgText.length()-1);
							} else if (cursorPosition==0 && msgText.length() > 1) {
								msgText = msgText.substring(1, msgText.length());
							} else if (cursorPosition==0 && msgText.length() <= 1) {
								msgText = "";
								cursorPosition = 0;
							}
						}
						activeArea = 0;
						redrawContent = true;
					}
					break;

				case RIGHT:
					if (cursorPosition < msgText.length()) cursorPosition++;
					else cursorPosition = msgText.length();
					serialTextSelection.setVisibility(false);
					activeArea = 0;
					redrawContent = true;
					break;

				case LEFT:
					if (cursorPosition > 0) cursorPosition--;
					else cursorPosition = 0;
					serialTextSelection.setVisibility(false);
					activeArea = 0;
					redrawContent = true;
					break;

				case UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= (12 * uimult);
						if (menuScroll < 0) menuScroll = 0;
						redrawUI = true;
					// Scroll serial monitor
					} else {
						int previousScroll = scrollUp;
						if (scrollUp < serialBuffer.size() - displayRows) scrollUp++;
						else scrollUp = serialBuffer.size() - displayRows;
						drawNewData = true;
						if (previousScroll == 0 && scrollUp > 0) {
							autoScroll = false;
							redrawUI = true;
						}
					}
					break;

				case DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += (12 * uimult);
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					// Scroll serial monitor
					} else {
						int previousScroll = scrollUp;
						if (scrollUp > 0) scrollUp--;
						else scrollUp = 0;
						drawNewData = true;
						if (previousScroll > 0 && scrollUp == 0) {
							autoScroll = true;
							redrawUI = true;
						}
					}
					break;

				case KeyEvent.VK_PAGE_UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= height - cT;
						if (menuScroll < 0) menuScroll = 0;
						redrawUI = true;
					// Scroll serial monitor
					} else {
						int previousScroll = scrollUp;
						if (scrollUp < serialBuffer.size() - displayRows) scrollUp += displayRows;
						if (scrollUp > serialBuffer.size() - displayRows) scrollUp = serialBuffer.size() - displayRows;
						if (previousScroll == 0 && scrollUp > 0) {
							autoScroll = false;
							redrawUI = true;
						}
						drawNewData = true;
					}
					break;

				case KeyEvent.VK_PAGE_DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += height - cT;
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					// Scroll serial monitor
					} else {
						int previousScroll = scrollUp;
						if (scrollUp > 0) scrollUp -= displayRows;
						if (scrollUp < 0) scrollUp = 0;
						drawNewData = true;
						if (previousScroll > 0 && scrollUp == 0) {
							autoScroll = true;
							redrawUI = true;
						}
					}
					break;

				case KeyEvent.VK_END:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					// Scroll serial monitor
					} else {
						scrollUp = 0;
						autoScroll = true;
						drawNewData = true;
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_HOME:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = 0;
						redrawUI = true;
					// Scroll serial monitor
					} else {
						int previousScroll = scrollUp;
						scrollUp = serialBuffer.size() - displayRows;
						drawNewData = true;
						autoScroll = false;
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_ALL_CANDIDATES: {
					// Select all - text
					if (activeArea == 0) {
						serialTextSelection.setVisibility(false);
						inputTextSelection.startChar = 0;
						inputTextSelection.endChar = msgText.length();
						cursorPosition = msgText.length();
						inputTextSelection.setVisibility(true);
					} else if (activeArea == 1) {
						inputTextSelection.setVisibility(false);
						serialTextSelection.startLine = 0;
						serialTextSelection.startChar = 0;
						serialTextSelection.endLine = serialBuffer.size() - 1;
						serialTextSelection.endChar = serialBuffer.get(serialTextSelection.endLine).length();
						serialTextSelection.setVisibility(true);
					}
					redrawContent = true;
					break;
				}

				case KeyEvent.VK_COPY: {
					
					if (serialTextSelection.valid) {
						String copyText = "";

						for (int i = serialTextSelection.startLine; i <= serialTextSelection.endLine; i++) {
							String tempString = serialBuffer.get(i);
							if (serialTextSelection.startLine == serialTextSelection.endLine) {
								copyText = tempString.substring(serialTextSelection.startChar, serialTextSelection.endChar);
							} else if (i == serialTextSelection.startLine) {
								if (serialTextSelection.startChar > 0) {
									copyText += tempString.substring(serialTextSelection.startChar) + '\n';
								}
							} else if (i == serialTextSelection.endLine) {
								copyText += tempString.substring(0, serialTextSelection.endChar);
							} else {
								copyText += tempString + '\n';
							}
						}

						//println("Copying: " + copyText);
						StringSelection selection = new StringSelection(copyText);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}

					else if (inputTextSelection.valid) {
						String copyText = msgText.substring(inputTextSelection.startChar, inputTextSelection.endChar);
						//println("Copying: " + copyText);
						StringSelection selection = new StringSelection(copyText);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
					
					break;
				}

				case KeyEvent.VK_PASTE: {
					String clipboardText = getStringClipboard();
					if (clipboardText != null && clipboardText.length() > 0) {
						String msgEnd = "";
						if (cursorPosition == 0) {
							msgEnd = msgText;
							msgText = "";
						} else if (cursorPosition < msgText.length()) {
							msgEnd = msgText.substring(cursorPosition, msgText.length());
							msgText = msgText.substring(0, cursorPosition);
						}
						//println("Pasting: " + clipboardText);
						String clipboardLines[] = clipboardText.split("\\r?\\n");
						for (int i = 0; i < clipboardLines.length - 1; i++) {
							if (serialConnected) {
								serialSend(msgText + clipboardLines[i]);
							}
							msgText = "SENT: " + msgText + clipboardLines[i];
							serialBuffer.append(msgText);
							msgText = "";
						}
						msgText += clipboardLines[clipboardLines.length - 1] + msgEnd;
						cursorPosition = msgText.length() - msgEnd.length();

						inputTextSelection.setVisibility(false);
						redrawContent = true;
					}
					break;
				}

				case KeyEvent.VK_F4:
					// Set output file
					if (!recordData) {
						outputfile = "";
						selectOutput("Select a location and name for the output *.TXT file", "fileSelected");
					}
					break;

				case KeyEvent.VK_F6:
					// Start/stop recording
					if(recordData) {
						stopRecording();
					} else if(outputfile != "" && outputfile != "No File Set") {
						startRecording();
					}
					break;

				default:
					//print("Unknown character: ");
					//print(keyChar);
					//print(" ");
					//println(keyCodeInt);
					break;
			}
		}
	}


	/**
	 * Content area mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void contentClick (int xcoord, int ycoord) {

		// Scroll down to bottom of serial message button
		if (scrollUp > 0) {
			if ((xcoord > cR - (40*uimult)) && (ycoord > cB - (40*uimult)) && (xcoord < cR - (10*uimult)) && (ycoord < cB - (10*uimult))) {
				scrollUp = 0;
				autoScroll = true;
				redrawUI = true;
				redrawContent = true;
				return;
			}
		}

		// Click on serial monitor scrollbar
		if ((scrollUp != -1) && serialScroll.click(xcoord, ycoord)) {
			sidebarScroll.active(false);
			serialTextSelection.active = false;
			inputTextSelection.active = false;
			startScrolling(true, 0);
			return;
		}

		// Text selection in message area
		if ((ycoord > msgB) && (ycoord < cB - border*1.5f)) {
			sidebarScroll.active(false);
			serialScroll.active(false);
			inputTextSelection.setVisibility(false);
			activeArea = 1;
			serialTextSelectionCalculation(xcoord, ycoord, true);
			startScrolling(true, 1);
			redrawContent = true;
		}

		// Text selection in input area
		if ((ycoord > cT) && (ycoord < msgB)) {
			sidebarScroll.active(false);
			serialScroll.active(false);
			serialTextSelection.setVisibility(false);
			activeArea = 0;
			inputTextSelectionCalculation(xcoord, ycoord, true);
			startScrolling(true, 1);
			redrawContent = true;
		}
	}


	/**
	 * Sidebar mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void menuClick (int xcoord, int ycoord) {

		// Coordinate calculation
		int sT = cT;
		int sL = cR;
		if (menuScroll > 0) sT -= menuScroll;
		if (menuScroll != -1) sL -= round(15 * uimult) / 4;
		final int sW = width - cR;
		final int sH = height - sT;

		final int uH = round(sideItemHeight * uimult);
		final int tH = round((sideItemHeight - 8) * uimult);
		final int iH = round((sideItemHeight - 5) * uimult);
		final int iL = round(sL + (10 * uimult));
		final int iW = round(sW - (20 * uimult));

		String[] ports = Serial.list();

		// Click on sidebar menu scroll bar
		if ((menuScroll != -1) && sidebarScroll.click(xcoord, ycoord)) {
			serialScroll.active(false);
			serialTextSelection.active = false;
			inputTextSelection.active = false;
			startScrolling(false);
		}

		// Root menu level
		if (menuLevel == 0) {

			// COM Port Number
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, 1, iL, iW)) {
				menuLevel = 1;
				menuScroll = 0;
				redrawUI = true;
			}

			// COM Port Baud Rate
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 2, iL, iW)){
				menuLevel = 2;
				menuScroll = 0;
				redrawUI = true;
			}

			// Connect to COM port
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 3, iL, iW)){
				setupSerial();
			}

			// Select output file name and directory
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 5.5f, iL, iW)){
				if (!recordData) {
					outputfile = "";
					selectOutput("Select a location and name for the output *.TXT file", "fileSelected");
				}
			}
			
			// Start recording data and saving it to a file
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 6.5f, iL, iW)) {
				if(recordData) {
					stopRecording();
				} else if(outputfile != "" && outputfile != "No File Set") {
					startRecording();
				}
			}

			// Clear the terminal buffer
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 9, iL, iW)) {
				if (!recordData) {
					serialBuffer.clear();
					serialBuffer.append("--- PROCESSING SERIAL MONITOR ---");
					scrollUp = 0;
					autoScroll = true;
					serialTextSelection.setVisibility(false);
					drawNewData = true;
					redrawUI = true;
				}
			}

			// Turn autoscrolling on/off
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 10, iL, iW)) {
				autoScroll = !autoScroll;
				redrawUI = true;
			}

			// Turn graphable numbers on/off
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 11, iL, iW)) {
				serialBuffer.setVisibility(!serialBuffer.getVisibility());
				redrawContent = true;
				redrawUI = true;
			}

			// Add a new colour tag column
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 13.5f, iL, iW)) {
				final String colname = myShowInputDialog("Add a new Colour Tag","Keyword Text:","");
				if (colname != null && colname.length() > 0){
					serialTags.add(new SerialTag(colname, c_colorlist[serialTags.size() % c_colorlist.length]));
					redrawUI = true;
					drawNewData = true;
				}
			}
			
			else {
				float tHnow = 14.5f;

				// List of Data Columns
				for(int i = 0; i < serialTags.size(); i++) {

					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {

						// Remove column
						if (menuXclick(xcoord, iL + iW - PApplet.parseInt(20 * uimult), PApplet.parseInt(20 * uimult))) {
							serialTags.remove(i);
							redrawUI = true;
							drawNewData = true;
						}

						// Change colour of entry
						else if (menuXclick(xcoord, iL + iW - PApplet.parseInt(40 * uimult), PApplet.parseInt(40 * uimult))) {
							previousColor = serialTags.get(i).tagColor;
							hueColor = previousColor;
							newColor = previousColor;
							colorSelector = i;

							menuLevel = 3;
							menuScroll = 0;
							redrawUI = true;
						}

						// Change name of column
						else {
							final String colname = myShowInputDialog("Update the Colour Tag Keyword", "Keyword Text:", serialTags.get(i).tagText);
							if (colname != null && colname.length() > 0){
								serialTags.get(i).tagText = colname;
								redrawUI = true;
								drawNewData = true;
							}
						}
					}
					
					tHnow++;
				}
			}

		// Select COM port
		} else if (menuLevel == 1) {
			float tHnow = 1;
			if (ports.length == 0) tHnow++;
			else {
				for (int i = 0; i < ports.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {

						// If the serial port is already connected to a different port, disconnect it
						if (serialConnected && portNumber != i) setupSerial();

						portNumber = i;
						currentPort = portList[portNumber];
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					}
					tHnow++;
				}
			}

			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Select a baud rate
		} else if (menuLevel == 2) {
			float tHnow = 1;
			for (int i = 0; i < baudRateList.length; i++) {
				if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
					baudRate = baudRateList[i];
					menuLevel = 0;
					menuScroll = 0;

					// If serial is already connected, disconnect and reconnect it at the new rate
					if (serialConnected) {
						setupSerial();
						setupSerial();
					}
					redrawUI = true;
				}
				tHnow++;
			}

			// More options button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				settingsMenuActive = true;
				settings.setMenuLevel(1);
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

			// Cancel button
			tHnow += 1;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Select a Colour
		} else if (menuLevel == 3) {

			// Colour hue selection
			if (menuXYclick(xcoord, ycoord, sT, uH, iW, 1, iL, iW)) {
				colorMode(HSB, iW, iW, iW);
				hueColor = color(mouseX - iL, mouseY - (sT + uH), iW);
				newColor = hueColor;
				colorMode(RGB, 255, 255, 255);
				redrawUI = true;

			// Colour brightness selection
			} else if (menuXYclick(xcoord, ycoord, sT + iW, uH, iH, 2.5f, iL, iW)) {
				if (mouseX > iL && mouseX < iL + (iW / 2)) {
					newColor = lerpColor(c_white, hueColor, (float) (mouseX - iL) / (iW / 2));
					redrawUI = true;
				} else if (mouseX > iL + (iW / 2) && mouseX < iL + iW) {
					newColor = lerpColor(hueColor, c_black, (float) (mouseX - (iL + iW / 2)) / (iW / 2));
					redrawUI = true;
				}

			// Submit button
			} else if (menuXYclick(xcoord, ycoord, sT + iW, uH, iH, 6.5f, iL, iW)) {
				serialTags.get(colorSelector).tagColor = newColor;
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
				redrawContent = true;

			// Cancel button
			} else if (menuXYclick(xcoord, ycoord, sT + iW, uH, iH, 7.5f, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}
		}
	}


	/**
	 * Scroll wheel handler function
	 *
	 * @param  amount Multiplier/velocity of the latest mousewheel movement
	 */
	public void scrollWheel (float amount) {
		// Scroll menu bar
		if (mouseX >= cR && menuScroll != -1) {
			menuScroll += (sideItemHeight * amount * uimult);
			if (menuScroll < 0) menuScroll = 0;
			else if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);

		// Scroll serial monitor
		} else {
			int previousScroll = scrollUp;
			scrollUp -= round(2 * amount);
			if (scrollUp < 0) scrollUp = 0;
			else if (scrollUp > serialBuffer.size() - displayRows) scrollUp = serialBuffer.size() - displayRows;
			drawNewData = true;
			if (previousScroll == 0 && scrollUp > 0) {
				autoScroll = false;
				redrawUI = true;
			} else if (previousScroll > 0 && scrollUp == 0) {
				autoScroll = true;
				redrawUI = true;
			}
		}

		redrawUI = true;
	}


	/**
	 * Scroll bar handler function
	 *
	 * @param  xcoord Current mouse x-coordinate position
	 * @param  ycoord Current mouse y-coordinate position
	 */
	public void scrollBarUpdate(int xcoord, int ycoord) {
		if (serialScroll.active()) {
			int previousScroll = scrollUp;
			scrollUp = serialScroll.move(xcoord, ycoord, scrollUp, 0, serialBuffer.size() - displayRows);
			if (previousScroll != scrollUp) redrawContent = true;
			if (previousScroll == 0 && scrollUp > 0) {
				autoScroll = false;
				redrawUI = true;
			} else if (previousScroll > 0 && scrollUp == 0) {
				autoScroll = true;
				redrawUI = true;
			}
		}

		else if (sidebarScroll.active()) {
			int previousScroll = menuScroll;
			menuScroll = sidebarScroll.move(xcoord, ycoord, menuScroll, 0, menuHeight - (height - cT));
			if (previousScroll != menuScroll) redrawUI = true;
		}

		else if (serialTextSelection.active) {
			if (serialTextSelectionCalculation(xcoord, ycoord, false)) {
				drawNewData = true;
			}
		}

		else if (inputTextSelection.active) {
			if (inputTextSelectionCalculation(xcoord, ycoord, false)) {
				redrawContent = true;
			}
		}
 	}


	/**
	 * Check whether it is safe to exit the program
	 *
	 * @return True if the are no tasks active, false otherwise
	 */
	public boolean checkSafeExit() {
		if (recordData) return false;
		return true;
	}


	/**
	 * End any active processes and safely exit the tab
	 */
	public void performExit() {
		if (recordData) stopRecording();
		if (serialConnected) setupSerial();
	}


	/**
	 * New serial monitor text selection calculations
	 */
	public boolean serialTextSelectionCalculation(int xcoord, int ycoord, boolean selectionStart) {
		
		// Figure out where in the serial messages was clicked
		int selectedLine = displayRows - ((ycoord - msgB) / yTextHeight);

		// Apply limits and perform automatic scrolling of text is mouse exceeds bounds
		if (selectedLine > displayRows) {
			if (!selectionStart && (scrollUp < serialBuffer.size() - displayRows)) {
				scrollUp++;
				selectedLine = displayRows + 1;
			} else {
				selectedLine = displayRows;
			}
		} else if (selectedLine < 0) {
			if (!selectionStart && (scrollUp > 0)) {
				scrollUp--;
				selectedLine = 0;
			} else {
				selectedLine = 0;
			}
		}

		// Get the actual index of the serial message text
		int textIndex = (serialBuffer.size() - selectedLine - scrollUp);
		if (textIndex < 0) textIndex = 0;
		else if (textIndex >= serialBuffer.size()) textIndex = serialBuffer.size() - 1;

		// Retreive the text in the selected row
		final String textRow = serialBuffer.get(textIndex);

		// Calculate the width of a single character, and the maximum row width (note: assumes mono-spaced font)
		textFont(mono_font);
		final float charWidth = textWidth("a");
		final int maxChars = floor((cR - 2*cL - 5*border) / charWidth);
		textFont(base_font);

		// Figure out which character was selection
		int selectedChar = PApplet.parseInt((xcoord - (cL + 2*border)) / charWidth) + 1;
		if (selectionStart) selectedChar--;
		if (selectedChar < 0) selectedChar = 0;

		// Ensure character is within bounds
		else if (selectedChar >= textRow.length()) selectedChar = textRow.length();
		else if (selectedChar > maxChars) selectedChar = maxChars;

		return serialTextSelection.setNewSelection(selectionStart, textIndex, selectedChar);
	}


	/**
	 * New serial monitor text selection calculations
	 */
	public boolean inputTextSelectionCalculation(int xcoord, int ycoord, boolean selectionStart) {

		// Calculate the width of a single character, and the maximum row width (note: assumes mono-spaced font)
		textFont(mono_font);
		final float charWidth = textWidth("a");
		final int maxChars = floor((cR - 2*cL - 5*border) / charWidth);
		textFont(base_font);

		// Figure out which character was selection
		int selectedChar = msgTextBounds[0] + PApplet.parseInt((xcoord - (cL + 4*msgBorder + msgBtnSize)) / charWidth);

		// Apply limits and perform automatic scaling
		if (selectedChar < msgTextBounds[0]) {
			if (msgTextBounds[0] > 0) {
				msgTextBounds[0]--;
				msgTextBounds[1]--;
				if (cursorPosition > msgTextBounds[1]) cursorPosition--;
			}
			selectedChar = msgTextBounds[0];
		} else if (selectedChar > msgTextBounds[1]) {
			if (msgTextBounds[1] < msgText.length()) {
				msgTextBounds[0]++;
				msgTextBounds[1]++;
				if (cursorPosition < msgTextBounds[0]) cursorPosition++;
			}
			selectedChar = msgTextBounds[1];
		}

		if (selectionStart) {
			cursorPosition = selectedChar;
		}

		return inputTextSelection.setNewSelection(selectionStart, 0, selectedChar);
	}


	/**
	 * Data structure to store info related to each colour tag
	 */
	class SerialTag {
		public String tagText;
		public int tagColor;

		/**
		 * Constructor
		 * 
		 * @param  setText  The keyword text which is search for in the serial data
		 * @param  setColor The colour which all lines containing that text will be set
		 */
		SerialTag(String setText, int setColor) {
			tagText = setText;
			tagColor = setColor;
		}
	}


	/**
	 * Data structure to store info related to selected/highlighted text
	 */
	class TextSelection {
		public int startLine = 0;
		public int startChar = 0;
		public int endLine = 0;
		public int endChar = 0;
		public boolean active = false;
		public boolean valid = false;
		private boolean inverted = false;


		/**
		 * Enable or disable the text selection
		 */ 
		public void setVisibility(boolean selectionState) {
			if (selectionState) {
				valid = true;
			} else {
				valid = false;
				active = false;
				inverted = false;
			}
		}

		/**
		 * Set a new starting or end position for the selection
		 * @param  selectionStart  True = start position of selection, False = new end position
		 * @param  newLine         Line index of the new selection position
		 * @param  newChar         Character index of the new selection position
		 */
		public boolean setNewSelection(boolean selectionStart, int newLine, int newChar) {

			if (this.inverted && newLine == this.startLine && newChar == this.startChar) return false;
			else if (!this.inverted && newLine == this.endLine && newChar == this.endChar) return false;

			// If the supplied position related to the start position of the selection
			if (selectionStart) {
				this.startLine = newLine;
				this.startChar = newChar;
				this.endLine = newLine;
				this.endChar = newChar;
				this.valid = false;

			// If the end position of the selection needs to be updated
			} else {

				// Figure out if the selection direction needs to be switched
				if (!this.inverted && (newLine < this.startLine || (newLine == this.startLine && newChar < this.startChar))) {
					this.inverted = true;
					this.endLine = this.startLine;
					this.endChar = this.startChar;
				} else if (this.inverted && (newLine > this.endLine || (newLine == this.endLine && newChar > this.endChar))) {
					this.inverted = false;
					this.startLine = this.endLine;
					this.startChar = this.endChar;
				}

				// Save the new position in the variables
				if (this.inverted) {
					this.startLine = newLine;
					this.startChar = newChar;
				} else {
					this.endLine = newLine;
					this.endChar = newChar;
				}

				if (this.endLine > this.startLine || this.endChar > this.startChar) {
					this.valid = true;
				}
			}

			this.active = true;
			return true;
		}
	}


	/**
	 * Data structure to store serial messages and other related info
	 */
	class SerialMessages {
		private int totalMessagesLength;
		private int lookupTableLength;
		private int maximumLength;

		private boolean showAllMessages;

		private int bufferEndIdx;
		private int tableStartIdx;
		private int tableEndIdx;

		private StringList serialMessagesBuffer; //!< Buffer which contains all received serial messages
		private IntList textLookupTable;         //!< Table containing indices to all non-graphable serial messages

		/**
		 * Constructor
		 * @param  maxLength Maximum number of entries in the serial buffer
		 */
		SerialMessages(int maxLength) {
			this.bufferEndIdx = 0;
			this.tableStartIdx = 0;
			this.tableEndIdx = 0;
			this.totalMessagesLength = 0;
			this.lookupTableLength = 0;
			this.showAllMessages = true;
			this.maximumLength = maxLength;
			int initialLength = 1000;
			if (initialLength > maxLength) initialLength = maxLength;
			this.serialMessagesBuffer = new StringList(initialLength);
			this.textLookupTable = new IntList(initialLength);
		}

		/**
		 * Get the value at the specific index
		 * @param  index The index at which to retrieve the value
		 * @return The requested serial message
		 */
		public String get(int index) {
			// If reading from the serial messages buffer directly
			if (showAllMessages) {
				// Check that the requested index is within bounds
				if (index < totalMessagesLength) {
					// If buffer is full, ensure values wrap around properly
					if (totalMessagesLength == maximumLength) {
						index += bufferEndIdx;
						if (index >= totalMessagesLength) index -= totalMessagesLength;
					}
					return serialMessagesBuffer.get(index);
				}
			// If only showing non-graphable results, read from the lookup table
			} else {
				// Check that the requested index is within bounds
				if (index < lookupTableLength) {
					index += tableStartIdx;
					if (index >= textLookupTable.size()) index -= textLookupTable.size();
					return serialMessagesBuffer.get(textLookupTable.get(index));
				}
			}
			return null;
		}

		/**
		 * Get the number of items in the list
		 * @return The length of the list (if disabled, graphable entries are excluded)
		 */
		public int size() {
			if (showAllMessages) return totalMessagesLength;
			return lookupTableLength;
		}

		/**
		 * Clear all items from the list
		 */
		public void clear() {
			totalMessagesLength = 0;
			bufferEndIdx = 0;
			tableStartIdx = 0;
			tableEndIdx = 0;
			lookupTableLength = 0;
		}

		/**
		 * Read whether all messages are being shown, or just the non-graphable ones
		 * @return True = all messages shown, false = non-graphable messages shown
		 */
		public boolean getVisibility() {
			return showAllMessages;
		}

		/**
		 * Set whether all messages will be shown, or only the non-graphable ones
		 * @param  setState True = all messages shown, false = only non-graphable messages shown
		 */
		public void setVisibility(boolean setState) {
			showAllMessages = setState;
		}

		/**
		 * Add a new serial message to the list
		 * @param  message   The serial message to add
		 * @param  graphable Whether the message only contains numbers and can be graphed
		 */
		public void append(String message, boolean graphable) {
			// If list hasn't reached its max length, append the new value
			if (totalMessagesLength < maximumLength) {
				if (totalMessagesLength < serialMessagesBuffer.size()) {
					serialMessagesBuffer.set(bufferEndIdx, message);
					if (!graphable) textLookupTable.set(tableEndIdx, bufferEndIdx);
				} else {
					serialMessagesBuffer.append(message);
					textLookupTable.append(0);
					if (!graphable) textLookupTable.set(tableEndIdx, bufferEndIdx);
				}

				totalMessagesLength++;
				bufferEndIdx++;
				if (!graphable) {
					tableEndIdx++;
					lookupTableLength++;
				}

			// Otherwise overwrite oldest item in list in a circular manner
			} else {
				int firstItem = bufferEndIdx;
				if (firstItem >= serialMessagesBuffer.size()) firstItem = 0;
				
				if (textLookupTable.get(tableStartIdx) == firstItem) {
					lookupTableLength--;
					tableStartIdx++;
					if (tableStartIdx >= textLookupTable.size()) tableStartIdx = 0;
				}

				// Add the item to the list
				serialMessagesBuffer.set(firstItem, message);

				if (!graphable) {
					if (tableEndIdx >= textLookupTable.size()) tableEndIdx = 0;
					textLookupTable.set(tableEndIdx++, firstItem);
					lookupTableLength++;
				}

				bufferEndIdx = firstItem + 1;
			}
		}

		/**
		 * Add a new serial message to the list
		 * @note This is an overload function where it is assumed the message cannot be plotted on a graph
		 * @see  void append(String message, boolean graphable)
		 */
		public void append(String message) {
			append(message, false);
		}
	}

	/**
	 * Class to deal with highlighting text in the serial monitor
	 */
	// class TextHighlight {
	// 	private boolean active;
	// 	private int startChar;
	// 	private int endChar;

	// 	/**
	// 	 * Constructor
	// 	 */
	// 	TextHighlight() {
	// 		active = false;
	// 		startChar = 0;
	// 		endChar = 0;
	// 	}

	// 	/**
	// 	 * Check if mouse has clicked on the some text
	// 	 * 
	// 	 * @param  xcoord Mouse x-axis coordinate
	// 	 * @param  ycoord Mouse y-axis coordinate
	// 	 * @return True if mouse has clicked on scrollbar, false otherwise
	// 	 */
	// 	boolean click(int xcoord, int ycoord) {

	// 	}

	// }
}
/* * * * * * * * * * * * * * * * * * * * * * *
 * FILE GRAPH PLOTTER CLASS
 * implements TabAPI for Processing Grapher
 *
 * @file     FileGraph.pde
 * @brief    Tab to plot CSV file data on a graph
 * @author   Simon Bluett
 *
 * @license  GNU General Public License v3
 * @class    FileGraph
 * @see      TabAPI <ProcessingGrapher.pde>
 * * * * * * * * * * * * * * * * * * * * * * */

/*
 * Copyright (C) 2022 - Simon Bluett <hello@chillibasket.com>
 *
 * This file is part of ProcessingGrapher 
 * <https://github.com/chillibasket/processing-grapher>
 * 
 * ProcessingGrapher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

class Settings implements TabAPI {

	int cL, cR, cT, cB;     // Content coordinates (left, right, top bottom)
	int menuScroll;
	int menuHeight;
	int menuLevel;
	ScrollBar sidebarScroll = new ScrollBar(ScrollBar.VERTICAL, ScrollBar.NORMAL);

	String name;
	String outputfile;

	boolean backExit = false;
	boolean unsavedChanges = false;
	boolean tabIsVisible = false;
	final int[] baudRateList = {300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 74880, 115200, 230400, 250000};
	final int[] baudRateListFull = {50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600, 19200, 38400, 57600, 74880, 115200, 230400, 250000, 460800, 500000, 576000, 921600, 1000000, 1152000, 1500000, 2000000, 2500000, 3000000, 3500000, 4000000};
	final String[] lineEndingNames = {"New Line (Default)", "Carriage Return"};
	final char[] lineEndingList = {'\n', '\r'};
	final String[] parityBitsNames = {"None (Default)", "Even", "Odd", "Mark", "Space"};
	final char[] parityBitsList = {'N', 'E', 'O', 'M', 'S'};
	final String[] dataBitsNames = {"5", "6", "7", "8 (Default)"};
	final int[] dataBitsList = {5, 6, 7, 8};
	final String[] stopBitsNames = {"1.0 (Default)", "1.5", "2.0"};
	final float[] stopBitsList = {1.0f, 1.5f, 2.0f};
	final String[] separatorNames = {"Comma (Default)", "Semi-colon [ ; ]", "Tab [ \\t ]", "Colon [ : ]", "Space [   ]", "Underscore [ _ ]", "Vertical Bar [ | ]"};
	final char[] separatorList = {',', ';', '\t', ':', ' ', '_', '|'};

	/**
	 * Constructor
	 *
	 * @param  setname Name of the tab
	 * @param  left    Tab area left x-coordinate
	 * @param  right   Tab area right x-coordinate
	 * @param  top     Tab area top y-coordinate
	 * @param  bottom  Tab area bottom y-coordinate
	 */
	Settings (String setname, int left, int right, int top, int bottom) {
		name = setname;
		
		cL = left;
		cR = right;
		cT = top;
		cB = bottom;

		outputfile = "No File Set";

		menuScroll = 0;
		menuHeight = cB - cT - 1; 
		menuLevel = 0;

		//loadSettings();
	}


	/**
	 * Get the name of the current tab
	 *
	 * @return Tab name
	 */
	public String getName () {
		return name;
	}


	/**
	 * Set tab as being active or hidden
	 * 
	 * @param  newState True = active, false = hidden
	 */
	public void setVisibility(boolean newState) {
		tabIsVisible = newState;
	}


	/**
	 * Set current side menu level
	 * 
	 * @param  newLevel The new menu level
	 */
	public void setMenuLevel(int newLevel) {
		menuLevel = newLevel;
		menuScroll = 0;
		backExit = true;
	}


	/**
	 * Redraw all tab content
	 */
	public void drawContent () {
		loadSettings();
	}


	/**
	 * Draw new tab data
	 */
	public void drawNewData () {
		// Not in use
		menuLevel = 0;
		menuScroll = 0;
	}


	/**
	 * Change tab content area dimensions
	 *
	 * @param  newL New left x-coordinate
	 * @param  newR New right x-coordinate
	 * @param  newT New top y-coordinate
	 * @param  newB new bottom y-coordinate
	 */
	public void changeSize (int newL, int newR, int newT, int newB) {
		cL = newL;
		cR = newR;
		cT = newT;
		cB = newB;
	}


	/**
	 * Change CSV data file location
	 *
	 * @param  newoutput Absolute path to the new file location
	 */
	public void setOutput (String newoutput) {
		// Not in use
	}


	/**
	 * Get the current CSV data file location
	 *
	 * @return Absolute path to the data file
	 */
	public String getOutput () {
		return outputfile;
	}


	/**
	 * Load user prefences from the settings file
	 */
	public void loadSettings() {

		// Check if file exists
		if (dataFile("user-preferences.xml").isFile()) {
			XML xmlFile = loadXML("user-preferences.xml");

			// Interface scale
			XML entry = xmlFile.getChild("interface-scale");
			try {
				int value = entry.getInt("percentage", 100);
				if (value == value && value <= 200 && value >= 50) {
					uimult = value / 100.0f;
					uiResize();
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <interface-scale>:\n" + e);
			}

			// Colour scheme
			entry = xmlFile.getChild("color-scheme");
			try {
				int value = entry.getInt("id", 2);
				if (value == value && value <= 2 && value >= 0) {
					colorScheme = value;
					loadColorScheme(colorScheme);
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <color-scheme>:\n" + e);
			}

			// Toggle FPS indicator on/off
			entry = xmlFile.getChild("fps-indicator");
			try {
				int value = entry.getInt("visible", 0);
				if (value == value && value <= 1 && value >= 0) {
					if (value == 1) drawFPS = true;
					else drawFPS = false;
					redrawUI = true;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <fps-indicator>:\n" + e);
			}

			// Toggle usage instructions on/off
			entry = xmlFile.getChild("usage-instructions");
			try {
				int value = entry.getInt("visible", 1);
				if (value == value && value <= 1 && value >= 0) {
					if (value == 1) showInstructions = true;
					else showInstructions = false;
					redrawUI = true;
					redrawContent = true;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <usage-instructions>\n" + e);
			}

			// Get serial port settings
			entry = xmlFile.getChild("serial-port");
			try {
				int value = entry.getInt("baud-rate", 9600);
				for (int i = 0; i < baudRateListFull.length; i++) {
					if (baudRateListFull[i] == value) baudRate = value;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <serial-port: baud-rate>\n" + e);
			}

			try {
				String value = entry.getString("line-ending", str('\n'));
				char charValue = value.charAt(0);
				for (int i = 0; i < lineEndingList.length; i++) {
					if (lineEndingList[i] == charValue) lineEnding = charValue;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <serial-port: line-ending>\n" + e);
			}

			try {
				String value = entry.getString("parity", str('N'));
				char charValue = value.charAt(0);
				for (int i = 0; i < parityBitsList.length; i++) {
					if (parityBitsList[i] == charValue) serialParity = charValue;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <serial-port: parity>\n" + e);
			}

			try {
				int value = entry.getInt("databits", 8);
				for (int i = 0; i < dataBitsList.length; i++) {
					if (dataBitsList[i] == value) serialDatabits = value;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <serial-port: databits>\n" + e);
			}

			try {
				float value = entry.getFloat("stopbits", 1.0f);
				for (int i = 0; i < stopBitsList.length; i++) {
					if (stopBitsList[i] == value) serialStopbits = value;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <serial-port: stopbits>\n" + e);
			}

			try {
				String value = entry.getString("separator", str(','));
				char charValue = value.charAt(0);
				for (int i = 0; i < separatorList.length; i++) {
					if (separatorList[i] == charValue) separator = charValue;
				}
			} catch (Exception e) {
				println("Unable to parse user settings - <serial-port: separator>\n" + e);
			}
		}
	}


	/**
	 * Save user preferences to the settings file
	 */
	public void saveSettings() {
		XML xmlFile = new XML("user-preferences");
		xmlFile.addChild("interface-scale");
		xmlFile.getChild("interface-scale").setInt("percentage", round(uimult * 100));
		xmlFile.addChild("color-scheme");
		xmlFile.getChild("color-scheme").setInt("id", colorScheme);
		xmlFile.addChild("fps-indicator");
		xmlFile.getChild("fps-indicator").setInt("visible", PApplet.parseInt(drawFPS));
		xmlFile.addChild("usage-instructions");
		xmlFile.getChild("usage-instructions").setInt("visible", PApplet.parseInt(showInstructions));
		xmlFile.addChild("serial-port");
		xmlFile.getChild("serial-port").setInt("baud-rate", baudRate);
		xmlFile.getChild("serial-port").setString("line-ending", str(lineEnding));
		xmlFile.getChild("serial-port").setString("parity", str(serialParity));
		xmlFile.getChild("serial-port").setInt("databits", serialDatabits);
		xmlFile.getChild("serial-port").setFloat("stopbits", serialStopbits);
		xmlFile.getChild("serial-port").setString("separator", str(separator));

		if (saveXML(xmlFile, "data/user-preferences.xml")) {
			alertMessage("Success\nUser preferences saved");
			unsavedChanges = false;
		} else {
			alertMessage("Error\nUnable to save user preferences");
		}
	}


	/**
	 * Draw the sidebar menu for the current tab
	 */
	public void drawSidebar () {

		// Calculate sizing of sidebar
		// Do this here so commands below are simplified
		int sT = cT;
		int sL = cR;
		int sW = width - cR;
		int sH = height - sT;

		int uH = round(sideItemHeight * uimult);
		int tH = round((sideItemHeight - 8) * uimult);
		int iH = round((sideItemHeight - 5) * uimult);
		int iL = round(sL + (10 * uimult));
		int iW = round(sW - (20 * uimult));
		if (menuLevel == 0) menuHeight = round(23 * uH);
		else if (menuLevel == 1) menuHeight = round((3 + baudRateListFull.length) * uH);
		else if (menuLevel == 2) menuHeight = round((3 + lineEndingList.length) * uH);
		else if (menuLevel == 3) menuHeight = round((3 + parityBitsList.length) * uH);
		else if (menuLevel == 4) menuHeight = round((3 + dataBitsList.length) * uH);
		else if (menuLevel == 5) menuHeight = round((3 + stopBitsList.length) * uH);

		// Figure out if scrolling of the menu is necessary
		if (menuHeight > sH) {
			if (menuScroll == -1) menuScroll = 0;
			else if (menuScroll > menuHeight - sH) menuScroll = menuHeight - sH;

			// Draw left bar
			fill(c_serial_message_box);
			rect(width - round(15 * uimult) / 2, sT, round(15 * uimult) / 2, sH);

			// Figure out size and position of scroll bar indicator
			int scrollbarSize = sH - round(sH * PApplet.parseFloat(menuHeight - sH) / menuHeight);
			if (scrollbarSize < uH) scrollbarSize = uH;
			int scrollbarOffset = round((sH - scrollbarSize) * (menuScroll / PApplet.parseFloat(menuHeight - sH)));
			fill(c_terminal_text);
			rect(width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);
			sidebarScroll.update(menuHeight, sH, width - round(15 * uimult) / 2, sT + scrollbarOffset, round(15 * uimult) / 2, scrollbarSize);

			sT -= menuScroll;
			sL -= round(15 * uimult) / 4;
			iL -= round(15 * uimult) / 4;
		} else {
			menuScroll = -1;
		}

		if (menuLevel == 0) {
			// UI Scaling Options
			drawHeading("Interface Size", iL, sT + (uH * 0), iW, tH);
			drawButton("-", c_sidebar_button, iL, sT + (uH * 1), iW / 4, iH, tH);
			drawButton("+", c_sidebar_button, iL + (iW * 3 / 4), sT + (uH * 1), iW / 4, iH, tH);
			drawDatabox(round(uimult*100) + "%", c_idletab_text, iL + (iW / 4), sT + (uH * 1), iW / 2, iH, tH);

			// Change the colour scheme
			drawHeading("Colour Scheme", iL, sT + (uH * 2.5f), iW, tH);
			drawButton("Light - Celeste", (colorScheme == 0)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 3.5f), iW, iH, tH);
			drawButton("Dark - Gravity", (colorScheme == 1)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 4.5f), iW, iH, tH);
			drawButton("Dark - Monokai", (colorScheme == 2)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 5.5f), iW, iH, tH);

			// Turn FPS counter on/off
			drawHeading("FPS Indicator", iL, sT + (uH * 7), iW, tH);
			drawButton("Show", (drawFPS)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 8), iW/2, iH, tH);
			drawButton("Hide", (!drawFPS)? c_sidebar_accent:c_sidebar_button, iL + (iW/2), sT + (uH * 8), iW/2, iH, tH);
			drawRectangle(c_sidebar_divider, iL + (iW / 2), sT + (uH * 8) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

			// Turn useful instructions on/off
			drawHeading("Usage Instructions", iL, sT + (uH * 9.5f), iW, tH);
			drawButton("Show", (showInstructions)? c_sidebar_accent:c_sidebar_button, iL, sT + (uH * 10.5f), iW/2, iH, tH);
			drawButton("Hide", (!showInstructions)? c_sidebar_accent:c_sidebar_button, iL + (iW/2), sT + (uH * 10.5f), iW/2, iH, tH);
			drawRectangle(c_sidebar_divider, iL + (iW / 2), sT + (uH * 10.5f) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

			drawHeading("Serial Port", iL, sT + (uH * 12), iW, tH);
			int c_serial_items = c_sidebar_text;
			if (serialConnected) c_serial_items = c_sidebar_button;
			drawDatabox("Baud: " + baudRate, c_serial_items, iL, sT + (uH * 13), iW, iH, tH);
			drawDatabox("Line Ending: " + ((lineEnding == '\r')? "CR":"NL"), c_serial_items, iL, sT + (uH * 14), iW, iH, tH);
			drawDatabox("Parity: " + serialParity, c_serial_items, iL, sT + (uH * 15), iW, iH, tH);
			drawDatabox("Data Bits: " + serialDatabits, c_serial_items, iL, sT + (uH * 16), iW, iH, tH);
			drawDatabox("Stop Bits: " + serialStopbits, c_serial_items, iL, sT + (uH * 17), iW, iH, tH);
			drawDatabox("Separator: [ " + ((separator == '\t')? "\\t":separator) + " ]", c_sidebar_text, iL, sT + (uH * 18), iW, iH, tH);

			// Save preferences
			drawHeading("User Preferences", iL, sT + (uH * 19.5f), iW, tH);
			if (unsavedChanges) drawButton("Save Settings", c_sidebar_button, iL, sT + (uH * 20.5f), iW, iH, tH);
			else drawDatabox("Save Settings", c_sidebar_button, iL, sT + (uH * 20.5f), iW, iH, tH);

			if (checkDefault()) {
				drawButton("Reset to Default", c_sidebar_button, iL, sT + (uH * 21.5f), iW, iH, tH);
			} else {
				drawDatabox("Reset to Default", c_sidebar_button, iL, sT + (uH * 21.5f), iW, iH, tH);
			}

		// Baud rate selection
		} else if (menuLevel == 1) {
			drawHeading("Select Baud Rate", iL, sT + (uH * 0), iW, tH);
			float tHnow = 1;
			for (int i = 0; i < baudRateListFull.length; i++) {
				drawButton(str(baudRateListFull[i]), (baudRate == baudRateListFull[i])?c_sidebar_accent:c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
		
		// Line ending list
		} else if (menuLevel == 2) {
			drawHeading("Select Line Ending", iL, sT + (uH * 0), iW, tH);
			float tHnow = 1;
			for (int i = 0; i < lineEndingNames.length; i++) {
				drawButton(lineEndingNames[i], (lineEnding == lineEndingList[i])?c_sidebar_accent:c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
		
		// Parity List
		} else if (menuLevel == 3) {
			drawHeading("Select Parity", iL, sT + (uH * 0), iW, tH);
			float tHnow = 1;
			for (int i = 0; i < parityBitsNames.length; i++) {
				drawButton(parityBitsNames[i], (serialParity == parityBitsList[i])?c_sidebar_accent:c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
		
		// Data bits List
		} else if (menuLevel == 4) {
			drawHeading("Select Data Bits", iL, sT + (uH * 0), iW, tH);
			float tHnow = 1;
			for (int i = 0; i < dataBitsNames.length; i++) {
				drawButton(dataBitsNames[i], (serialDatabits == dataBitsList[i])?c_sidebar_accent:c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
		
		// Stop bits list
		} else if (menuLevel == 5) {
			drawHeading("Select Stop Bits", iL, sT + (uH * 0), iW, tH);
			float tHnow = 1;
			for (int i = 0; i < stopBitsNames.length; i++) {
				drawButton(stopBitsNames[i], (serialStopbits == stopBitsList[i])?c_sidebar_accent:c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);

		// Separator list
		} else if (menuLevel == 6) {
			drawHeading("Select Separator", iL, sT + (uH * 0), iW, tH);
			float tHnow = 1;
			for (int i = 0; i < separatorNames.length; i++) {
				drawButton(separatorNames[i], (separator == separatorList[i])?c_sidebar_accent:c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
				tHnow += 1;
			}
			tHnow += 0.5f;
			drawButton("Cancel", c_sidebar_button, iL, sT + (uH * tHnow), iW, iH, tH);
		}
	}


	/**
	 * Draw the btoom information bar
	 */
	public void drawInfoBar() {
		// Empty
	}


	/**
	 * Check whether settings are different than the default
	 *
	 * @return true if different, false if not
	 */
	public boolean checkDefault() {
		if (uimult != 1) return true;
		if (!showInstructions) return true;
		if (drawFPS) return true;
		if (colorScheme != 1) return true;
		if (!serialConnected) {
			if (baudRate != 9600) return true;
			if (lineEnding != '\n') return true;
			if (serialParity != 'N') return true;
			if (serialDatabits != 8) return true;
			if (serialStopbits != 1.0f) return true;
			if (separator != ',') return true;
		}
		return false;
	} 


	/**
	 * Keyboard input handler function
	 *
	 * @param  key The character of the key that was pressed
	 */
	public void keyboardInput (char keyChar, int keyCodeInt, boolean codedKey) {
		if (keyChar == ESC) {
			if (menuLevel != 0) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			} else {
				settingsMenuActive = false;
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}
		}

		else if (codedKey) {
			switch (keyCodeInt) {
				case UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= (12 * uimult);
						if (menuScroll < 0) menuScroll = 0;
					}
					redrawUI = true;
					break;

				case DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += (12 * uimult);
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
					}
					redrawUI = true;
					break;

				case KeyEvent.VK_PAGE_UP:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll -= height - cT;
						if (menuScroll < 0) menuScroll = 0;
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_PAGE_DOWN:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll += height - cT;
						if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_END:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = menuHeight - (height - cT);
						redrawUI = true;
					}
					break;

				case KeyEvent.VK_HOME:
					// Scroll menu bar
					if (mouseX >= cR && menuScroll != -1) {
						menuScroll = 0;
						redrawUI = true;
					}
					break;
			}
		}
	}


	/**
	 * Content area mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void contentClick (int xcoord, int ycoord) {
		// Not in use
	}


	/**
	 * Scroll wheel handler function
	 *
	 * @param  amount Multiplier/velocity of the latest mousewheel movement
	 */
	public void scrollWheel (float amount) {
		// Scroll menu bar
		if (mouseX >= cR && menuScroll != -1) {
			menuScroll += (sideItemHeight * amount * uimult);
			if (menuScroll < 0) menuScroll = 0;
			else if (menuScroll > menuHeight - (height - cT)) menuScroll = menuHeight - (height - cT);
		}

		redrawUI = true;
	}


	/**
	 * Scroll bar handler function
	 *
	 * @param  xcoord Current mouse x-coordinate position
	 * @param  ycoord Current mouse y-coordinate position
	 */
	public void scrollBarUpdate (int xcoord, int ycoord) {
		if (sidebarScroll.active()) {
			int previousScroll = menuScroll;
			menuScroll = sidebarScroll.move(xcoord, ycoord, menuScroll, 0, menuHeight - (height - cT));
			if (previousScroll != menuScroll) redrawUI = true;
		}
	}


	/**
	 * Sidebar mouse click handler function
	 *
	 * @param  xcoord X-coordinate of the mouse click
	 * @param  ycoord Y-coordinate of the mouse click
	 */
	public void menuClick (int xcoord, int ycoord) {

		// Coordinate calculation
		int sT = cT;
		int sL = cR;
		if (menuScroll > 0) sT -= menuScroll;
		if (menuScroll != -1) sL -= round(15 * uimult) / 4;

		int sW = width - cR;
		int sH = height - sT;

		int uH = round(sideItemHeight * uimult);
		int tH = round((sideItemHeight - 8) * uimult);
		int iH = round((sideItemHeight - 5) * uimult);
		int iL = round(sL + (10 * uimult));
		int iW = round(sW - (20 * uimult));

		// Click on sidebar menu scroll bar
		if ((menuScroll != -1) && sidebarScroll.click(xcoord, ycoord)) {
			startScrolling(false);
		}

		// Main Menu
		if (menuLevel == 0) {
			// UI scaling
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, 1, iL, iW)){
				// Decrease
				if (menuXclick(xcoord, iL, iW / 4)) {
					if (uimult > 0.5f) {
						uiResize(-0.1f);
						unsavedChanges = true;
					}
				}

				// Increase
				else if (menuXclick(xcoord, iL + (iW * 3 / 4), iW / 4)) {
					if (uimult < 2.0f) {
						uiResize(0.1f);
						unsavedChanges = true;
					}
				}
			}

			// Color Scheme 0 - Light Celeste
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 3.5f, iL, iW)) {
				if (colorScheme != 0) {
					unsavedChanges = true;
					colorScheme = 0;
					loadColorScheme(colorScheme);
				}
			}

			// Color Scheme 1 - Dark Gravity
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 4.5f, iL, iW)) {
				if (colorScheme != 1) {
					unsavedChanges = true;
					colorScheme = 1;
					loadColorScheme(colorScheme);
				}
			}

			// Color Scheme 2 - Dark Monokai
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 5.5f, iL, iW)) {
				if (colorScheme != 2) {
					unsavedChanges = true;
					colorScheme = 2;
					loadColorScheme(colorScheme);
				}
			}

			// Toggle FPS indicator
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 8, iL, iW)) {
				// Show
				if (menuXclick(xcoord, iL, iW / 2)) {
					if (!drawFPS) {
						drawFPS = true;
						unsavedChanges = true;
						redrawUI = true;
					}
				}

				// Hide
				else if (menuXclick(xcoord, iL + (iW / 2), iW / 2)) {
					if (drawFPS) {
						drawFPS = false;
						unsavedChanges = true;
						redrawUI = true;
					}
				}
			}

			// Toggle usage instructions
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 10.5f, iL, iW)){
				// Show
				if (menuXclick(xcoord, iL, iW / 2)) {
					if (!showInstructions) {
						showInstructions = true;
						unsavedChanges = true;
						redrawUI = true;
						redrawContent = true;
					}
				}

				// Hide
				else if (menuXclick(xcoord, iL + (iW / 2), iW / 2)) {
					if (showInstructions) {
						showInstructions = false;
						unsavedChanges = true;
						redrawUI = true;
						redrawContent = true;
					}
				}
			}

			// Baud rate selection
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 13, iL, iW)) {
				if (!serialConnected) {
					menuLevel = 1;
					menuScroll = 0;
					redrawUI = true;
				}
			}

			// Line ending selection
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 14, iL, iW)) {
				if (!serialConnected) {
					menuLevel = 2;
					menuScroll = 0;
					redrawUI = true;
				}
			}

			// Parity selection
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 15, iL, iW)) {
				if (!serialConnected) {
					menuLevel = 3;
					menuScroll = 0;
					redrawUI = true;
				}
			}

			// Data bits selection
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 16, iL, iW)) {
				if (!serialConnected) {
					menuLevel = 4;
					menuScroll = 0;
					redrawUI = true;
				}
			}

			// Stop bits selection
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 17, iL, iW)) {
				if (!serialConnected) {
					menuLevel = 5;
					menuScroll = 0;
					redrawUI = true;
				}
			}

			// Separator selection
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 18, iL, iW)) {
				menuLevel = 6;
				menuScroll = 0;
				redrawUI = true;
			}

			// Remember preferences
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 20.5f, iL, iW)) {
				if (unsavedChanges) saveSettings();
			}

			// Reset preferences to default
			else if (menuXYclick(xcoord, ycoord, sT, uH, iH, 21.5f, iL, iW)) {
				if (checkDefault()) {
					drawFPS = false;
					showInstructions = true;
					colorScheme = 1;

					if (!serialConnected) {
						baudRate = 9600;
						lineEnding = '\n';
						serialParity = 'N';
						serialDatabits = 8;
						serialStopbits = 1.0f;
						separator = ',';
					}

					unsavedChanges = true;
					loadColorScheme(colorScheme);
					uimult = 1;
					uiResize();
				}
			}

		// Baud rate menu
		} else if (menuLevel == 1) {
			float tHnow = 1;
			if (baudRateListFull.length == 0) tHnow++;
			else {
				for (int i = 0; i < baudRateListFull.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (baudRate != baudRateListFull[i]) unsavedChanges = true;
						baudRate = baudRateListFull[i];
						menuLevel = 0;
						menuScroll = 0;
						if (backExit) {
							backExit = false;
							settingsMenuActive = false;
						}
						redrawUI = true;
					}
					tHnow++;
				}
			}
			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				if (backExit) {
					backExit = false;
					settingsMenuActive = false;
				}
				redrawUI = true;
			}

		// Line ending menu
		} else if (menuLevel == 2) {
			float tHnow = 1;
			if (lineEndingNames.length == 0) tHnow++;
			else {
				for (int i = 0; i < lineEndingNames.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (lineEnding != lineEndingList[i]) unsavedChanges = true;
						lineEnding = lineEndingList[i];
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					}
					tHnow++;
				}
			}
			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Parity menu
		} else if (menuLevel == 3) {
			float tHnow = 1;
			if (parityBitsNames.length == 0) tHnow++;
			else {
				for (int i = 0; i < parityBitsNames.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (serialParity != parityBitsList[i]) unsavedChanges = true;
						serialParity = parityBitsList[i];
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					}
					tHnow++;
				}
			}
			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Data bits menu
		} else if (menuLevel == 4) {
			float tHnow = 1;
			if (dataBitsNames.length == 0) tHnow++;
			else {
				for (int i = 0; i < dataBitsNames.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (serialDatabits != dataBitsList[i]) unsavedChanges = true;
						serialDatabits = dataBitsList[i];
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					}
					tHnow++;
				}
			}
			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Stop bits menu
		} else if (menuLevel == 5) {
			float tHnow = 1;
			if (stopBitsNames.length == 0) tHnow++;
			else {
				for (int i = 0; i < stopBitsNames.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (serialStopbits != stopBitsList[i]) unsavedChanges = true;
						serialStopbits = stopBitsList[i];
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					}
					tHnow++;
				}
			}
			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}

		// Separator menu
		} else if (menuLevel == 6) {
			float tHnow = 1;
			if (separatorNames.length == 0) tHnow++;
			else {
				for (int i = 0; i < separatorNames.length; i++) {
					if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
						if (separator != separatorList[i]) unsavedChanges = true;
						separator = separatorList[i];
						menuLevel = 0;
						menuScroll = 0;
						redrawUI = true;
					}
					tHnow++;
				}
			}
			// Cancel button
			tHnow += 0.5f;
			if (menuXYclick(xcoord, ycoord, sT, uH, iH, tHnow, iL, iW)) {
				menuLevel = 0;
				menuScroll = 0;
				redrawUI = true;
			}
		}
	}


	/**
	 * Serial port data handler function
	 *
	 * @param  inputData New data received from the serial port
	 * @param  graphable True if data in message can be plotted on a graph
	 */
	public void parsePortData(String inputData, boolean graphable) {
		// Not in use
	}


	/**
	 * Function called when a serial device has connected/disconnected
	 *
	 * @param  status True if a device has connected, false if disconnected
	 */
	public void connectionEvent (boolean status) {
		// Not in use
	}


	/**
	 * Check whether it is safe to exit the program
	 *
	 * @return True if the are no tasks active, false otherwise
	 */
	public boolean checkSafeExit() {
		return true;
	}


	/**
	 * End any active processes and safely exit the tab
	 */
	public void performExit() {
		// Nothing to do here
	}
}
  public void settings() { 	size(1000, 700, activeRenderer); 	smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ProcessingGrapher" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
