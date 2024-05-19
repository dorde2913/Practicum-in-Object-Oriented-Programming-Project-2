package kjhkhkjh;



public class Cell {
	private String value;
	private int format=0; //0-string, 1-double, 2-date
	public Cell(String value) {
		super();
		this.value = value;
	}
	public Cell()
	{
		super();
		this.value="";
	}
	public String getValue() {
		return value;
	}
	public int get_format()
	{
		return format;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int set_format(int format)//return values: 0=>success, -1=>
	{
		Boolean dot=false;
		switch(format)
		{
		case 0:
			this.format=0;
			return 0;
		case 1:
			for (int i=0;i<value.length();i++)
			{
				if (!Character.isDigit(value.charAt(i)) && value.charAt(i)!='.')
				{
					return -1;
				}
				if (value.charAt(i)=='.' && !dot)
				{
					dot=true;
				}
				else if (dot && value.charAt(i)=='.')
				{
					return -1;
				}
			}
			this.format=1;
			return 0;
			
		case 2:
			if (value.length()!=11 )
			{
				return -2;
			}
			for (int i=0;i<value.length();i++)
			{
				if (!Character.isDigit(value.charAt(i)) && value.charAt(i)!='.')return -1;
				else if (value.charAt(i)=='.' && i!=2 && i!=5 && i!=10) return -1;
			}
			this.format=2;
			return 0;
		}
		return -2;
	}

	


}
