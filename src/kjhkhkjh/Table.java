package kjhkhkjh;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Table {

	
	private int num_columns=0;
	private int num_rows=0;
	
	private ArrayList<Column> column_list=new ArrayList<>();
	public ArrayList<Column> get_columns()
	{
		return column_list;
	}
	public char get_max_column() {
		return column_list.get(column_list.size()-1).get_id();
	}
	public Table() {}
	public Table(int columns,int rows)
	{
		num_columns=columns;
		num_rows=rows;
		for (int i=0;i<columns;i++)
		{
			Column col=new Column();
			for(int j=0;j<rows;j++)
			{
				col.add_cell(new Cell());
			}
			add_column(col);
		}
		
	}
	public int get_num_columns() {return num_columns;}
	public int get_num_rows() {return num_rows;}
	public Column get_column(char index)
	{
		for (Column c:column_list)
		{
			if (c.get_id()==index)
			{
				return c;
			}
		}
		return null;
	}
	public Cell get_cell(char c, int r)
	{
		return get_column(c).get_cell(r);
	}
	public void add_column(Column c)
	{
		column_list.add(c);
		this.num_columns++;
		this.num_rows=c.get_list().size();
	}
	public int set_format(int format,char column, int row)
	{
		return get_cell(column,row).set_format(format);
	}
	public int set_format(int format, char column)
	{
		return get_column(column).set_format(format);
	}
	public int set_data(char col, int row,String new_value)
	{
		int ret=check_format(col,row,new_value);
		
		if (ret==0)
		{
			get_cell(col,row).setValue(new_value);
			return 0;
		}
		else
		{
			return -1;
		}
		
	}
	public int check_format(char col, int row, String val)
	{
		int format=get_cell(col,row).get_format();
		boolean dot=false;
		switch(format)
		{
		case 0:
			return 0;
		case 1:
			for (int i=0;i<val.length();i++)
			{
				if (!Character.isDigit(val.charAt(i)))
				{
					if (val.charAt(i)=='.' && dot==false)
					{
						dot=true;
					}
					else
					{
						return -1;//invalid format
					}
				}
			}
			return 0;
			
		case 2:
			if (val.length()!=11 )
			{
				return -2;
			}
			for (int i=0;i<val.length();i++)
			{
				if (!Character.isDigit(val.charAt(i)) && val.charAt(i)!='.')return -1;
				else if (val.charAt(i)=='.' && i!=2 && i!=5 && i!=10) return -1;
			}
			return 0;
		}
		return -1;
	}
	
	public static TableView<Map> generate_gui(Table table)
	{
		
		TableView<Map> ret=new TableView<>();
		TableColumn<Map,String> column;
		//adding first column which represents the row #
		column=new TableColumn<>("#");
		column.setCellValueFactory(new MapValueFactory<>("broj_reda"));
		ret.getColumns().add(column);
		
		
		
		
		
		
		
		for (Column col:table.get_columns())
		{
			column=new TableColumn<>(col.get_name());
			column.setCellValueFactory(new MapValueFactory<>(col.get_name()));
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			
			column.setOnEditCommit(new EventHandler<CellEditEvent<Map,String>>()
			{
				int i=0;

				@Override
				public void handle(CellEditEvent<Map, String> arg0) {
					
					TablePosition poz=arg0.getTablePosition();
					String new_value;
					//KOLONU IMAMO U COL, IZ TABLEPOZITION MOZEMO ROW
					if (arg0.getNewValue().charAt(0)=='=')
					{
						CSV_parser.save_file(table);
						String test_s=col.get_cell(poz.getRow()).getValue();
						System.out.println(test_s);
						String t_file="C:\\Users\\dj133\\eclipse-workspace\\kjhkhkjh\\CSV_savefile.csv";
						double rez=JNE_.calculate_formula(t_file, arg0.getNewValue());
						i++;
						new_value=Double.toString(rez);
					}
					else
					{
						new_value=arg0.getNewValue();
					}
					
					int a=table.set_data(col.get_id(),poz.getRow(),new_value);
					
					if (a==0)
					{
						Map mapa=arg0.getRowValue();
						mapa.put(col.get_name(), new_value);
						ret.refresh();
					}
					else
					{
						ret.refresh();
						System.out.println("Los format");
					}

				}
				
			});
			
			
					
			
			ret.getColumns().add(column);
		}
		
		
		ObservableList<Map<String,Object>> items=
				FXCollections.<Map<String,Object>>observableArrayList();
		
		Map<String,Object> item1;
		
		for (int i=0;i<table.get_num_rows();i++)
		{
			item1=new HashMap<String, Object>();
			item1.put("broj_reda", i+1);
			
			
			for (char j='A';j<=table.get_max_column();j++)
			{
				item1.put(table.get_column(j).get_name(), table.get_cell(j, i).getValue());
			}
			items.add(item1);
			ret.getItems().addAll(items);
			items.removeAll(items);
		}
		
		ret.getSelectionModel().setCellSelectionEnabled(true);
		ret.setEditable(true);
		return ret;
	}
	public void add_cell(char col, String val)
	{
		get_column(col).add_cell(new Cell(val));
		for (Column c:column_list)
		{
			if (c.get_list().size()>num_rows)
			{
				num_rows=c.get_list().size();
			}
		}
	}
	public void add_cell(char col, Cell c1)
	{
		get_column(col).add_cell(c1);
		for (Column c:column_list)
		{
			if (c.get_list().size()>num_rows)
			{
				num_rows=c.get_list().size();
			}
		}
	}
	public void set_format(String input)
	{
		char column;
		int row;
		int format;
		if (input.length()==3)//kolona
		{
			column=input.charAt(0);
			format=Character.getNumericValue(input.charAt(2));
			int ret=set_format(format,column);
			if (ret==0)
			{
				System.out.println("Uspesno promenjen format kolone "+column+" u format "+format);
			}
			else
			{
				System.out.println("Greska");
			}
		}
		else//polje
		{
			column=input.charAt(0);
			int i=1;
			String temp="";
			while (Character.isDigit(input.charAt(i)))
			{
				temp+=input.charAt(i);
				i++;
			}
			row=Integer.parseInt(temp);
			temp="";
			i++;
			format=Character.getNumericValue(input.charAt(i));
			int rez=set_format(format,column,row-1);
			if (rez==0)
			{
				System.out.println("Uspesno promenjen format polja "+column+row+" u format "+format);
			}
			else
			{
				System.out.println("Greska");
			}
		}
		
		
	}
	
}
