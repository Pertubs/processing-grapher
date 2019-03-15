class FileGraph implements TabAPI {

    int cL, cR, cT, cB;     // Content coordinates (left, right, top bottom)
    int xData;
    Graph graph;

    String name;
    String outputfile;
    String[] dataColumns = {};
    Table dataTable;

    boolean labelling;


    /**********************************
     * Constructor
     **********************************/
    FileGraph(String setname, int left, int right, int top, int bottom) {
        name = setname;
        
        cL = left;
        cR = right;
        cT = top;
        cB = bottom;

        xData = -1;     // -1 if no data column contains x-axis data

        graph = new Graph(cL, cR, cT, cB, 0, 100, 0, 10);
        outputfile = "No File Set";

        labelling = false;
    }

    String getName() {
        return name;
    }

    void drawContent() {
        graph.drawGrid();
        plotFileData();
    }

    void drawNewData() {
        // Not being used yet 
    }

    /**********************************
     * Change content area size
     **********************************/
    void changeSize(int newL, int newR, int newT, int newB) {
        cL = newL;
        cR = newR;
        cT = newT;
        cB = newB;

        graph.changeSize(cL, cR, cT, cB);
        //drawContent();
    }


    /**********************************
     * Change output file location
     **********************************/
    void setOutput(String newoutput) {
        outputfile = newoutput;
        if (outputfile != "No File Set") {
            // Check whether file is of type *.csv
            if (outputfile.contains(".csv")) {
                dataTable = loadTable(outputfile, "csv, header");
            } else {
                alertHeading = "Invalid file type; it must be *.csv";
                outputfile = "No File Set";
                redrawAlert = true;
            }
        }
        redrawContent = true;
    }

    void plotFileData() {
        if(outputfile != "No File Set" && outputfile != "" && dataTable.getColumnCount() > 0) {
            xData = -1;

            // Load columns
            while (dataColumns.length > 0) dataColumns = remove(dataColumns, 0);

            for (int i = 0; i < dataTable.getColumnCount(); i++) {
                dataColumns = append(dataColumns, dataTable.getColumnTitle(i));
                if ((i == 0) && (dataTable.getColumnTitle(i).contains("x:"))) {
                    xData = i;
                }
            }

            redrawUI = true;

            // Ensure that some data acutally exists in the table
            if (dataTable.getRowCount() > 0 && !(xData == 0 && dataTable.getColumnCount() == 1)) {
                float minx = 0, maxx = 0;
                float miny = dataTable.getFloat(0, 0), maxy = dataTable.getFloat(0, 0);

                if (xData == -1) {
                    minx = 0;
                    maxx = 0;
                    miny = dataTable.getFloat(0, 0);
                    maxy = dataTable.getFloat(0, 0);
                } else {
                    minx = dataTable.getFloat(0, 0);
                    maxx = dataTable.getFloat(dataTable.getRowCount() - 1, 0);
                    miny = dataTable.getFloat(0, 1);
                    maxy = dataTable.getFloat(0, 1);
                }

                // Calculate Min and Max X and Y axis values
                for (TableRow row : dataTable.rows()) {
                    int i = 0;

                    if (xData != -1) {
                        i = 1;
                        if(minx > row.getFloat(0)) minx = row.getFloat(0);
                        if(maxx < row.getFloat(0)) maxx = row.getFloat(0);
                    } else {
                        maxx += 1 / float(graph.getXrate());
                    }

                    for(   ; i < dataTable.getColumnCount(); i++){
                        if(miny > row.getFloat(i)) miny = row.getFloat(i);
                        if(maxy < row.getFloat(i)) maxy = row.getFloat(i);
                    }
                }

                // Set these min and max values
                graph.setMinMax(minx, 0);
                graph.setMinMax(maxx, 1);
                graph.setMinMax(miny, 2);
                graph.setMinMax(maxy, 3);

                // Draw the axes and grid
                graph.resetGraph();
                graph.drawGrid();

                // Start plotting the data
                for (TableRow row : dataTable.rows()) {
                    if (xData != -1){
                        for (int i = 1; i < dataTable.getColumnCount(); i++) {
                            try {
                                float dataX = row.getFloat(0);
                                float dataPoint = row.getFloat(i);
                                if(Float.isNaN(dataX) || Float.isNaN(dataPoint)) dataPoint = dataX = 99999999;
                                graph.plotData(dataPoint, dataX, i);
                            } catch (Exception e) {
                                println("Error trying to plot file data.");
                                println(e);
                            }
                        }
                    } else {
                        for (int i = 0; i < dataTable.getColumnCount(); i++) {
                            try {
                                float dataPoint = row.getFloat(i);
                                if(Float.isNaN(dataPoint)) dataPoint = 99999999;
                                graph.plotData(dataPoint, -99999999, i);
                            } catch (Exception e) {
                                println("Error trying to plot file data.");
                                println(e);
                            }
                        }
                    }
                }
            }
        }
    }

    String getOutput() {
        return outputfile;
    }

    void saveData() {
        if(outputfile != "No File Set" && outputfile != "") {
            try {
                saveTable(dataTable, outputfile, "csv");
                alertHeading = "File Saved!";
                redrawAlert = true;
            } catch (Exception e){
                alertHeading = "Error - Unable to save file " + e;
                redrawAlert = true;
            }
        }
    }


    /**********************************
     * Draw Side Bar
     **********************************/
    void drawSidebar () {

        // Calculate sizing of sidebar
        // Do this here so commands below are simplified
        int sT = cT;
        int sL = cR;
        int sW = width - cR;
        int sH = height - sT;

        int uH = int(sideItemHeight * uimult);
        int tH = int((sideItemHeight - 8) * uimult);
        int iH = int((sideItemHeight - 5) * uimult);
        int iL = int(sL + (10 * uimult));
        int iW = int(sW - (20 * uimult));

        // Open, close and save files
        drawHeading("Content File", iL, sT + (uH * 0), iW, tH);
        drawButton("Open Data", c_sidebar_button, iL, sT + (uH * 1), iW, iH, tH);
        drawButton("Save Data", c_sidebar_button, iL, sT + (uH * 2), iW, iH, tH);

        // Add labels to data
        drawHeading("Data Labels", iL, sT + (uH * 3.5), iW, tH);
        drawButton("Add Label", c_sidebar_button, iL, sT + (uH * 4.5), iW, iH, tH);
        drawButton("Remove Labels", c_sidebar_button, iL, sT + (uH * 5.5), iW, iH, tH);
        
        // Input Data Columns
        drawHeading("Graph Markings", iL, sT + (uH * 7), iW, tH);
        drawDatabox("x-Axis: " + graph.getXscale(), iL, sT + (uH * 8), iW - (40 * uimult), iH, tH);
        drawDatabox("y-Axis: " + graph.getYscale(), iL, sT + (uH * 9), iW - (40 * uimult), iH, tH);

        // Remove column button
        drawButton("-", c_sidebar_button, iL + iW - (20 * uimult), sT + (uH * 8), 20 * uimult, iH, tH);
        drawButton("-", c_sidebar_button, iL + iW - (20 * uimult), sT + (uH * 9), 20 * uimult, iH, tH);
        drawButton("+", c_sidebar_button, iL + iW - (40 * uimult), sT + (uH * 8), 20 * uimult, iH, tH);
        drawButton("+", c_sidebar_button, iL + iW - (40 * uimult), sT + (uH * 9), 20 * uimult, iH, tH);
        fill(c_grey);
        rect(iL + iW - (20 * uimult), sT + (uH * 8) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
        rect(iL + iW - (20 * uimult), sT + (uH * 9) + (1 * uimult), 1 * uimult, iH - (2 * uimult));

        // Input Data Columns
        drawHeading("Data Format", iL, sT + (uH * 10.5), iW, tH);
        if (xData == 0) drawButton("X-axis: " + dataColumns[0].substring(2, dataColumns[0].length()), c_sidebar_button, iL, sT + (uH * 11.5), iW, iH, tH);
        else drawDatabox("Rate: " + graph.getXrate() + "Hz", iL, sT + (uH * 11.5), iW, iH, tH);
        //drawButton("Add Column", c_sidebar_button, iL, sT + (uH * 12.5), iW, iH, tH);

        float tHnow = 12.5;

        // List of Data Columns
        for(int i = 0; i < dataColumns.length; i++){
            if (!(i == 0 && xData == 0)) {
                // Column name
                drawDatabox(dataColumns[i], iL, sT + (uH * tHnow), iW - (40 * uimult), iH, tH);

                // Remove column button
                drawButton("x", c_sidebar_button, iL + iW - (20 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);
                
                // Hide or Show data series
                color buttonColor = c_colorlist[i-(c_colorlist.length * floor(i / c_colorlist.length))];
                drawButton("", buttonColor, iL + iW - (40 * uimult), sT + (uH * tHnow), 20 * uimult, iH, tH);

                fill(c_grey);
                rect(iL + iW - (20 * uimult), sT + (uH * tHnow) + (1 * uimult), 1 * uimult, iH - (2 * uimult));
                tHnow++;
            }
        }

        textAlign(LEFT, CENTER);
        fill(c_lightgrey);
        text("Input File: " + outputfile, (5 * uimult), height - (bottombarHeight * uimult), width - sW, bottombarHeight - (5 * uimult));
    }

    void keyboardInput(char key) {
        // Not being used yet
    }

    void getContentClick (int xcoord, int ycoord) {
        if (labelling) {
            if(outputfile != "" && outputfile != "No File Set"){

                int xItem = graph.setXlabel(xcoord, ycoord);
                if (xItem != -1) {
                }
            } 
            labelling = false;
            cursor(ARROW);
        } else cursor(ARROW);
    }

    void scrollWheel (float amount) {
        // Not being used yet
    }

    /**********************************
     * Mouse Click on the SideBar
     **********************************/
    void mclickSBar (int xcoord, int ycoord) {

        // Coordinate calculation
        int sT = cT;
        int sL = cR;
        int sW = width - cR;
        int sH = height - sT;

        int uH = int(sideItemHeight * uimult);
        int tH = int((sideItemHeight - 8) * uimult);
        int iH = int((sideItemHeight - 5) * uimult);
        int iL = int(sL + (10 * uimult));
        int iW = int(sW - (20 * uimult));

        // Open data
        if ((mouseY > sT + (uH * 1)) && (mouseY < sT + (uH * 1) + iH)){
            outputfile = "";
            selectInput("Select select a directory and name for output", "fileSelected");
        }

        // Save data
        else if ((mouseY > sT + (uH * 2)) && (mouseY < sT + (uH * 2) + iH)){
            if(outputfile != "" && outputfile != "No File Set"){
                saveData();
            }
        }

        // Add label
        else if ((mouseY > sT + (uH * 4.5)) && (mouseY < sT + (uH * 4.5) + iH)){
            if(outputfile != "" && outputfile != "No File Set"){
                labelling = true;
                contentClick = true;
                cursor(CROSS);
            }
        }
        
        // Remove all labels
        else if ((mouseY > sT + (uH * 5.5)) && (mouseY < sT + (uH * 5.5) + iH)){
            redrawContent = redrawUI = true;
        }

        // Change x-Axis divisions scale
        else if ((mouseY > sT + (uH * 8)) && (mouseY < sT + (uH * 8) + iH)){
            // Decrease x-Axis scale
            if ((mouseX > iL + iW - (20 * uimult)) && (mouseX < iL + iW)) {
                graph.changeGraphDiv(-1, 0);
                redrawContent = redrawUI = true;
            }

            // Increase x-Axis scale
            else if ((mouseX > iL + iW - (40 * uimult)) && (mouseX < iL + iW - (20 * uimult))) {
                graph.changeGraphDiv(1, 0);
                redrawContent = redrawUI = true;
            }
        }

        // Change y-Axis divisions scale
        else if ((mouseY > sT + (uH * 9)) && (mouseY < sT + (uH * 9) + iH)){
            // Decrease y-Axis scale
            if ((mouseX > iL + iW - (20 * uimult)) && (mouseX < iL + iW)) {
                graph.changeGraphDiv(0, -1);
                redrawContent = redrawUI = true;
            }

            // Increase y-Axis scale
            else if ((mouseX > iL + iW - (40 * uimult)) && (mouseX < iL + iW - (20 * uimult))) {
                graph.changeGraphDiv(0, 1);
                redrawContent = redrawUI = true;
            }
        }

        // Change the input data rate
        else if ((mouseY > sT + (uH * 11.5)) && (mouseY < sT + (uH * 11.5) + iH)){
            final String newrate = showInputDialog("Set new data rate:");
            if (newrate != null){
                try {
                    graph.setXrate(Integer.parseInt(newrate));
                    redrawContent = redrawUI = true;
                } catch (Exception e) {}
            }
        }
        
        // Add a data column
        /*
        else if ((mouseY > sT + (uH * 12.5)) && (mouseY < sT + (uH * 12.5) + iH)){
            final String colname = showInputDialog("Column Name:");
            if (colname != null){
                dataColumns = append(dataColumns, colname);
                redrawUI = true;
            }
        }*/
        
        // Edit data column
        else {
            float tHnow = 12.5;

            // List of Data Columns
            for(int i = 0; i < dataColumns.length; i++){

                if ((mouseY > sT + (uH * tHnow)) && (mouseY < sT + (uH * tHnow) + iH)){

                    if ((mouseX > iL + iW - (20 * uimult)) && (mouseX < iL + iW)) {
                        if (xData == 0) {
                            dataColumns = remove(dataColumns, i + 1);
                            dataTable.removeColumn(i + 1);
                        } else {
                            dataColumns = remove(dataColumns, i);
                            dataTable.removeColumn(i);
                        }
                        redrawContent = redrawUI = true;
                    }

                    else if ((mouseX > iL + iW - (40 * uimult)) && (mouseX < iL + iW - (20 * uimult))) {
                        /*
                        if (i - 1 >= 0) {
                            String temp = dataColumns[i - 1];
                            dataColumns[i - 1] = dataColumns[i];
                            dataColumns[i] = temp;
                        }
                        redrawUI = true;*/
                    }
                }
                
                tHnow++;
            }
        }
    }

    void parsePortData(String inputData) {
        // Not using serial comms 
    }
}
