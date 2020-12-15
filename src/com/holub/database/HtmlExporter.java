package com.holub.database;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.swing.JTable;

enum Type {
	HEADER("thead", "tr", "th"), 
	BODY("tbody", "tr", "td"),
	FOOTER("tfoot", "tr", "td");

	String wrapper, row, cell;
    
    Type(String wrapper, String row, String cell){
        this.wrapper = wrapper; this.row = row; this.cell = cell;
    }
}

public class HtmlExporter implements Table.Exporter{
    private final Writer out;
    private int width;

    public HtmlExporter( Writer out ) { 
        this.out = out;
    }
    
    @Override
    public void storeMetadata(String tableName, int width, int height, Iterator columnNames) throws IOException {
        this.width = width;
        String tempName = tableName == null ? "" : tableName;
        out.write(String.format("        <caption>%s</caption>\n", tempName)); //caption ¥Ÿ¿Ωø° thead
        out.write("        <" + Type.HEADER.wrapper + ">\n");
        storeRowByType(columnNames, Type.HEADER);
        out.write("        </" + Type.HEADER.wrapper + ">\n");
        out.write("        <" + Type.BODY.wrapper + ">\n");
    }

    public void storeRowByType(Iterator data, Type type) throws IOException{
        StringBuilder dataStr = new StringBuilder();
        dataStr.append("            <").append(type.row).append(">\n");
        while(data.hasNext()){
            Object datum = data.next();
            dataStr.append("                <").append(type.cell).append(">");
            dataStr.append(datum.toString());
            dataStr.append("</").append(type.cell).append(">\n");
        }
        dataStr.append("            </").append(type.row).append(">\n");
        
        out.write(dataStr.toString());
    }

    @Override
    public void storeRow(Iterator data) throws IOException {
        storeRowByType(data, Type.BODY);
    }

    @Override public void startTable() throws IOException {
        out.write("<html>\n" + "<body>\n" + "    <table>\n");
    }
    @Override public void endTable() throws IOException {
        out.write("        </tbody>\n" + "    </table>\n" + "</body>\n" + "</html>\n");
    }

}