package kjhkhkjh;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Column {
	private ArrayList<Cell> cell_list=new ArrayList<>();
	private static char index='A';
	private char id=index++;
	private String name;
	public char get_id() {return id;}
	
	public Column()
	{
		this.name=Character.toString(id);
	}
	public Column(String name)
	{
		this.name=name;
		
	}
	public ArrayList<Cell> get_list()
	{
		return cell_list;
	}
	public void set_name(String s)
	{
		this.name=s;
	}
	public String get_name()
	{
		return name;
	}
	public void add_cell(Cell c)
	{
		cell_list.add(c);
	}
	public Cell get_cell(int index)
	{
		return cell_list.get(index);
	}
	public int set_format(int format)
	{
		for (Cell c:cell_list)
		{
			if (c.set_format(format)!=0)
			{
				return -1;
			}
		}
		return 0;
	}
	
	//cell_list.set() moze da se koristi za zamenu
}
