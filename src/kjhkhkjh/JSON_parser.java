package kjhkhkjh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JSON_parser {
	
public static boolean first=true;
public static boolean nesto =false;

public static char col='A';
	public static Table load_table(File json_file)
	{
		Table ret=new Table();
		
		try {
			Scanner scanner=new Scanner(json_file);
			while (scanner.hasNextLine())
			{
				read_line(ret,scanner.nextLine());
			}
			
			
			
		} catch (FileNotFoundException e) {e.printStackTrace();}
			
		
		return ret;
	}
	public static void read_line(Table t,String line)
	{
		String format_string="";
		String data="";
		Scanner line_scanner =new Scanner(line);
		
		int f=0;
		//citamo line by line
		while (line_scanner.hasNext())
		{
			String temp_read=line_scanner.next();
			if (temp_read.equals(""))
			{
				col='A';
				return;
			}
			if (temp_read.equals("{"))
			{
				nesto=true;
				return;
			}
			else if(temp_read.equals("}")|| temp_read.equals("},"))
			{
				col='A';
				nesto=false;
				first=false;
				return;
			}
			else if (temp_read.equals("[") || temp_read.equals("]"))
			{
				return;
			}
			else//u telu strukture, svaka linija tacno 1 whitespace
			{
				String temp="";
				int i=0;
				for (i=0;i<temp_read.length();i++)
				{
					if (temp_read.charAt(i)!='"' && temp_read.charAt(i)!=',')
					{
						if (temp_read.charAt(i)==':')
						{
							i++;
							break;
						}
						temp+=temp_read.charAt(i);
					}
				}
				
				//procitali format;
				format_string=temp;
				temp="";
				int j=i;
				for (i=j;i<temp_read.length();i++)
				{
					if (temp_read.charAt(i)!='"' && temp_read.charAt(i)!=',')
					{
						temp+=temp_read.charAt(i);
					}
				}
				data=temp;
		
				
				
			}
				
		}
		if (format_string.equals("TEXT"))f=0;
		else if (format_string.equals("NUM"))f=1;
		else if(format_string.equals("DATE"))f=2;
		if (first)
		{
			Column c=new Column();
			Cell cell=new Cell(data);
			cell.set_format(f);
			c.add_cell(new Cell(data));
			t.add_column(c);
		}
		else
		{
			Cell cell=new Cell(data);
			cell.set_format(f);
			t.add_cell(col, cell);
			col++;
		}
		
		
		
	}
	public static File save_file(Table t)
	{
		File file=new File("JSON_savefile.json");
		String format="";
		try {
			FileWriter writer=new FileWriter("JSON_savefile.json");
			writer.write("[\n");
			
			for (int i=0;i<t.get_num_rows();i++)
			{
				writer.write("  {\n");
				for (char c='A';c<=t.get_max_column();c++)
				{
					if (t.get_cell(c, i).get_format()==0)format="TEXT";
					if (t.get_cell(c, i).get_format()==1)format="NUM";
					if (t.get_cell(c, i).get_format()==2)format="DATE";
					
					
					
					writer.write("    \""+format+"\":");
					writer.write("\""+t.get_cell(c,i).getValue()+"\"");
					if (c==t.get_max_column())
					{
						writer.write("\n");
					}
					else
					{
						writer.write(",\n");
					}
				}
				if (i==t.get_num_rows()-1)
				{
					writer.write("  }\n");
				}
				else
				{
					writer.write("  },\n");
				}
				
				
			}
			writer.write("]");
			
			writer.close();
		} catch (IOException e) {e.printStackTrace();}
		
		return file;
	}

	
	
	
	
}
