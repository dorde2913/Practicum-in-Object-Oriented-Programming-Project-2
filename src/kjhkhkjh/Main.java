package kjhkhkjh;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application{ 

	
	BorderPane layout;
	Table table;
	TableView<?> table_gui;
	File CSV_save_file;
	File JSON_save_file;
	
	// --module-path "\path\to\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//inicijalizacija menija:
		Menu file_menu=novi_meni("_File",primaryStage);
		MenuBar menu_bar=new MenuBar();
		menu_bar.getMenus().add(file_menu);
		
		
		layout=new BorderPane();
		layout.setTop(menu_bar);
	
		
		
		FlowPane format_pane=new FlowPane();
		
		
		Button format_s=new Button("Submit");
		TextField f_text=new TextField();
		Text t=new Text("Unesi polje/kolonu i format (0-2) (primer: B1 1 ->polje B1 u numericki");
		f_text.setPrefWidth(50);
		format_pane.getChildren().addAll(t,format_s,f_text);
		layout.setBottom(format_pane);
		
		format_s.setOnAction(e->
		{
			String input=f_text.getText();
			if (table!=null)
			{
				table.set_format(input);
				
			}
			else
			{
				System.out.println("greska, tabela je null");
			}
			
		});
		
		
		
		
		
        Scene scene=new Scene(layout,400,400);
        
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
	}
	
	
	
	
	
	private Menu novi_meni(String string,Stage primaryStage) {
		Menu ret=new Menu(string);
		MenuItem new_file =new MenuItem("New...");
		Menu open_file=new Menu("Open File...");
		Menu save_file=new Menu("Save As...");
		MenuItem csv_file=new MenuItem("CSV File...");//submenu for open_file
		MenuItem json_file=new MenuItem("JSON File...");//submenu for open_file
		MenuItem csv_save=new MenuItem("Save as CSV file");
		MenuItem json_save=new MenuItem("Save as JSON file");
		open_file.getItems().add(json_file);
		open_file.getItems().add(csv_file);
		
		save_file.getItems().add(json_save);
		save_file.getItems().add(csv_save);
		
		
		new_file.setOnAction(e->{
			
			int r,c;
			System.out.println("izabrali ste New...");
			//table_gui=create_table();
			Stage popup=new Stage();
			VBox pane=new VBox();
			
			popup.setWidth(300);
			popup.setHeight(150);
			Button btn=new Button("Submit");
			TextField rows=new TextField("Broj redova ovde :");
			TextField columns=new TextField("Broj kolona ovde :");
			pane.getChildren().add(btn);
			pane.getChildren().add(rows);
			pane.getChildren().add(columns);
			
			btn.setOnAction(b->{
				table_gui=create_table(columns.getText(),rows.getText());
				popup.close();
				layout.setCenter(table_gui);
				
				
				primaryStage.show();
			});
			
		
			
			//layout.setCenter(table_gui);
			Scene scene1=new Scene(pane);
			popup.setScene(scene1);
			popup.show();
			
			new_file.setDisable(true);
			open_file.setDisable(true);
			
			
			
		});
		
		csv_file.setOnAction(e->{
			
			
			
			ExtensionFilter ex1=new ExtensionFilter("CSV files","*.csv");
			
			
			FileChooser file_chooser=new FileChooser();
			file_chooser.setTitle("Open CSV file");
			file_chooser.getExtensionFilters().add(ex1);
			File selectedFile=file_chooser.showOpenDialog(primaryStage);
			
			table= CSV_parser.load_table(selectedFile);
			table_gui=Table.generate_gui(table);
			layout.setCenter(table_gui);
			primaryStage.show();
			//sad nam treba neka fora da otvorimo fajl i procitamo
			//iz njega sta nam treba
			
			
			new_file.setDisable(true);
			open_file.setDisable(true);
		});
		
		json_file.setOnAction(e->{
			
			
			ExtensionFilter ex1=new ExtensionFilter("JSON files","*.json");
			
			
			FileChooser file_chooser=new FileChooser();
			file_chooser.setTitle("Open JSON file");
			file_chooser.getExtensionFilters().add(ex1);
			File selectedFile=file_chooser.showOpenDialog(primaryStage);
			
			table= JSON_parser.load_table(selectedFile);
			table_gui=Table.generate_gui(table);
			layout.setCenter(table_gui);
			primaryStage.show();
			
			
			new_file.setDisable(true);
			open_file.setDisable(true);
		});
		
		
		csv_save.setOnAction(e->{
			
			if (table!=null)
			{
				CSV_save_file=CSV_parser.save_file(table);
			}
			else
			{
				System.out.println("Error");
			}
			
			
			
		});
		json_save.setOnAction(e->{
			
			if (table!=null)
			{
				JSON_save_file=JSON_parser.save_file(table);
			}
			else
			{
				System.out.println("Error");
			}
			
			
		});
		

		ret.getItems().add(new_file);
		ret.getItems().add(new SeparatorMenuItem());
		ret.getItems().add(open_file);
		ret.getItems().add(new SeparatorMenuItem());
		ret.getItems().add(save_file);
		
		
		return ret;
	}

	public TableView<?> create_table(String c,String r)
	{
		
		
		//nekakav UI koji ce da uzme broj kolona+redova
		
		int number_c=Integer.parseInt(c);
		int number_r=Integer.parseInt(r);
		table=new Table(number_c,number_r);
		table_gui=Table.generate_gui(table);
		
		
		return table_gui;
		
	}



	public static void main(String[] args)
	{
		launch(args);
	}
	


}
