package com.holub.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HtmlExporterTest {
	private Table people;
    private String[] col, row1, row2;

    @BeforeEach
    public void init(){
    	col = new String[]{ "First", "Last"	};
        row1 = new String[]{ "Fred",	"Flintstone" };
        row2 = new String[]{ "Wilma",	"Flintstone" };

        Table people = TableFactory.create( "people", col);
        people.insert( row1 );
        people.insert( row2 );
    }

 
	@Test
	void testHtmlExporter() throws IOException {
        Writer writer = new StringWriter();
        HtmlExporter tableBuilder = new HtmlExporter(writer);
        tableBuilder.startTable();
        tableBuilder.storeMetadata("people", 2, 3, Arrays.stream(col).iterator());
        tableBuilder.storeRow(Arrays.stream(row1).iterator());
        tableBuilder.storeRow(Arrays.stream(row2).iterator());
        tableBuilder.endTable();
        assertEquals("<html>\n<body>\n    <table>\n        <caption>people</caption>\n        <thead>\n"
        		+ "            <tr>\n                <th>First</th>\n                <th>Last</th>\n"
        		+ "            </tr>\n        </thead>\n        <tbody>\n            <tr>\n                <td>Fred</td>\n"
        		+ "                <td>Flintstone</td>\n            </tr>\n            <tr>\n                <td>Wilma</td>\n"
        		+ "                <td>Flintstone</td>\n            </tr>\n        </tbody>\n"
        		+ "    </table>\n</body>\n</html>\n", writer.toString());
        writer.close();
    }

	@Test
	void testStoreRow() throws IOException {
        Writer writer = new StringWriter();
        HtmlExporter tableBuilder = new HtmlExporter(writer);
        tableBuilder.storeRow(Arrays.stream(row1).iterator());
        assertEquals("            <tr>\n                <td>Fred</td>\n"
        		+ "                <td>Flintstone</td>\n            </tr>\n", writer.toString());
        writer.close();
    }

	@Test
	void testStartTable() throws IOException {
		Writer writer = new StringWriter();
        HtmlExporter tableBuilder = new HtmlExporter(writer);
        tableBuilder.startTable();
        assertEquals("<html>\n<body>\n    <table>\n", writer.toString());
        writer.close();
	}

	@Test
	void testEndTable() throws IOException {
        Writer writer = new StringWriter();
        HtmlExporter tableBuilder = new HtmlExporter(writer);
        tableBuilder.endTable();
        assertEquals("        </tbody>\n" + "    </table>\n" + "</body>\n" + "</html>\n", writer.toString());
        writer.close();
    }

}
