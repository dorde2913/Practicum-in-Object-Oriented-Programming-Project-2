package kjhkhkjh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CSV_parser {

	public static boolean first=true;
	
	public static Table load_table(File csv_file)
	{
		Table ret=new Table();
		
		try {
			Scanner scanner=new Scanner(csv_file);
			while (scanner.hasNextLine())
			{
				read_line(ret,scanner.nextLine());
			}
			
			
			
		} catch (FileNotFoundException e) {e.printStackTrace();}
			
		return ret;
	}
	public static void read_line(Table t,String line)
	{
		Scanner line_scanner =new Scanner(line);
		line_scanner.useDelimiter(",");
		char col='A';
		while (line_scanner.hasNext())
		{
			if (first)
			{
				Column c =new Column();
				
				c.add_cell(new Cell(line_scanner.next()));
				t.add_column(c);
			}
			else
			{
				t.add_cell(col,line_scanner.next());
				System.out.println(col);
				col++;
			}
		}
		first=false;
	}
	public static File save_file(Table t)
	{
		File file=new File("C:\\Users\\dj133\\eclipse-workspace\\kjhkhkjh\\CSV_savefile.csv");
		
		try {
			FileWriter writer=new FileWriter("C:\\Users\\dj133\\eclipse-workspace\\kjhkhkjh\\CSV_savefile.csv");
			for (int i=0;i<t.get_num_rows();i++)
			{
				for (char c='A';c<=t.get_max_column();c++)
				{
					
					writer.write(t.get_cell(c,i).getValue());
					if (c==t.get_max_column())
					{
						writer.write('\n');
					}
					else
					{
						writer.write(',');
					}
				}
			}
			writer.close();
		} catch (IOException e) {e.printStackTrace();}
		
		return file;
	}
}
