import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class CrosswordB    {
    static int globdimensions;
    static char[][] CW;
    static DictInterface D;
	static int numsolution;
    static final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static void main(String[] args) throws IOException
    {
		String st;
		StringBuilder sbz;
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		String dictType = args[1];
		
		
		if (dictType.equals("DLB"))
			//D = new MyDictionary();
		D = new DLB(); //should be the 'if' statement
		else
			D = new MyDictionary();
		
		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.add(st);
		}
        String dicFile = "dict8.txt";
		numsolution = 0;
        String word = null;

        //open dictionary file for read
        try {
            FileReader fr = new FileReader(dicFile);
            BufferedReader bufferedReader = new BufferedReader(fr);
            D = new DLB();
            while((word = bufferedReader.readLine()) != null)
            {
                D.add(word);//adds in dictionary
			
              
            }
			     bufferedReader.close(); 
            fr.close();
        } 
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                dicFile + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + dicFile + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }

        FileReader txtFile;
        BufferedReader genericTxtFile;

        //read a crossword board in from a file 
        try
        {
            txtFile = new FileReader(args[0]);
            genericTxtFile = new BufferedReader(txtFile);

            int dimensions = (genericTxtFile.read() - 48); //value of 0 is 48
            globdimensions = dimensions;
            genericTxtFile.readLine();

            CW = new char[dimensions][dimensions];
            System.out.print(dimensions);
            System.out.println();
            for(int i = 0; i < dimensions; i++)
            {
                for (int j = 0; j <  dimensions; j++)
                {
                    CW[i][j] = (char)genericTxtFile.read();//populates crossword
                    System.out.print(CW[i][j]);

                }
                genericTxtFile.readLine();
                System.out.println();
            }
            System.out.println("done reading board layout...");

            txtFile.close();
            genericTxtFile.close();//up to here takes care of part 2

            //part 3 create a legal crossword puzzle

           solveCrossword();
		
		   System.out.println("found " + numsolution + " solutions");

        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                dicFile + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + dicFile + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }

        /*public int solveCrossword()
        {
        boolean solved = false;

        this 1 will call checksolution
        //if (solved !=   )
        {
        //solved --no pluses remain 

        return true;
        }
        else if ()
        {
        //pluses remain and prefixes valid
        }
        else if()
        {
        //pluses remain + prefixes invalid, letters remain
        }
        else 
        {
        return false;
        }

        }*/
        //public int checkSolution()
        //{
        //calls checkPrefixes   
        //}

    }

    public static boolean checkPrefixes()
    {

       
       
        StringBuilder sb;
        int k;
        int result;
        boolean valid = true;

        for (int i = 0; i < globdimensions; i++) //for rows 
        {

            for (int j = 0; j < globdimensions; j++)
            {
                //Horizontal Prefix Checks
                if(j == 0){

                    if(CW[i][j] != '+' && CW[i][j] != '-')
                    {
                
                        sb = new StringBuilder();
                        sb.append(CW[i][j]);

                        k = j; // j , k all references to the columns
                        while(k+1<globdimensions)
                        {

                            if ((CW[i][k+1] == '+') || (CW[i][k+1] =='-'))
                            {
                                break;
                            }
                            else
                            {
                                sb.append(CW[i][k+1]);
                                k++;
                            }
                        }
                        //System.out.println(sb);
                        result = D.searchPrefix(sb);
                      
                        valid = false;
                        switch (result)
                        {
                            case 0:  
                            valid = false;
                            break;
                            case 1: 

                            if (k < globdimensions - 1)
                            {
                                if(CW[i][k+1] == '+')
                                {
                                   
                                    valid = true;

                                }
                            }
                            break;
                            case 2:
                            if (k == globdimensions - 1)
                            {
                                valid = true;
                            }
                            else if(CW[i][k+1] == '-') 
                            {
                                valid = true;
                            }
                            break;

                            case 3: 

                            valid = true;
                            break;

                        }
						if(valid == false)
							{
								
								return valid;
							}			

                    }
                }

                if (j != 0){
                    if (CW[i][j-1] == '-'){
                        if(CW[i][j] != '+' && CW[i][j] != '-')
                        {

                          
                            sb = new StringBuilder();
                            sb.append(CW[i][j]);

                            k = j; //i, j , k all references to the columns
                            while(k+1<globdimensions)
                            {
                                if ((CW[i][k+1] == '+') || (CW[i][k+1] =='-'))
                                {
                                    break;
                                }
                                else
                                {
                                    sb.append(CW[i][k+1]);
                                    k++;
                                }
                            }
                            //System.out.println(sb);

                            result = D.searchPrefix(sb);
                        

                            valid = false;
                            switch (result)
                            {
                                case 0:  
                                valid = false;
                                break;
                                case 1: 
                                if (k < globdimensions - 1)
                                {
                                    if(CW[i][k+1] == '+') //here
                                    {
                                        valid = true;

                                    }
                                }
                                break;
                                case 2:
                                if (k == globdimensions - 1)
                                {
                                    valid = true;
                                }
                                else if(CW[i][k+1] == '-') //here
                                {
                                    valid = true;
                                }
                                break;

                                case 3: 

                                valid = true;
                                break;

                            }   
							if(valid == false)
							{
								
								return valid;
							}			
                        }
                    }
                }


                //Vertical Prefix Checks
                if(i == 0){
                    if(CW[i][j] != '+' && CW[i][j] != '-')
                    {
                
                        sb = new StringBuilder();
                        sb.append(CW[i][j]);

                        k = i; //i, j , k all references to the columns
                        while(k+1<globdimensions)
                        {

                            if ((CW[k+1][j] == '+') || (CW[k+1][j] =='-'))
                            {
								
                                break;
                            }
                            else
                            {
								
                                sb.append(CW[k+1][j]);
                                k++;
                            }
                        }
                        //System.out.println(sb);
					 result = D.searchPrefix(sb);
						
                        valid = false;
                        switch (result)
                        {
                            case 0:  
                            valid = false;
                            break;
                            case 1: 
                            if (k < globdimensions - 1)
                            {
                                if(CW[k+1][j] == '+') 
                                {
                                    valid = true;

                                }
                            }
                            break;
                            case 2:
                            if (k == globdimensions - 1)
                            {
                                valid = true;
                            }
                            else if(CW[i+1][k] == '-') 
                            {
                                valid = true;
                            }
                            break;

                            case 3: 

                            valid = true;
                            break;

                        }   
					if(valid == false)
							{
							
								return valid;
							}			
                    }
                }
                if (i != 0){
                    if (CW[i-1][j] == '-'){

                        if(CW[i][j] != '+' && CW[i][j] != '-')
                        {
                           
                            sb = new StringBuilder();
                            sb.append(CW[i][j]);

                            k = i; //i, j , k all references to the columns
                            while(k+1<globdimensions)
                            {

                                if ((CW[k+1][j] == '+') || (CW[k+1][j] =='-'))
                                {
                                    break;
                                }
                                else
                                {
                                    sb.append(CW[k+1][j]);
                                    k++;
                                }
                            }
							
							
                            result = D.searchPrefix(sb);
                            
							
                            valid = false;
                            switch (result)
                            {
                                case 0:  
                                valid = false;
                                break;
                                case 1: 
                                if (k < globdimensions - 1)
                                {
                                    if(CW[k+1][j] == '+')
                                    {
                                        valid = true;

                                    }
                                }
                                break;
                                case 2:
                                if (k == globdimensions - 1)
                                {
                                    valid = true;
                                }
                                else if(CW[k+1][j] == '-') 
                                {
                                    valid = true;
                                }
                                break;

                                case 3: 

                                valid = true;
                                break;

                            }
							if(valid == false)
							{
								
								return valid;
							}								
                            
                        }
                    }

                }
            }                               
        }
		
        return valid;
    }
    
	
	
	
	
	
	
	public static boolean solveCrossword()
	{
		
		int x=0;
		int y=0;
		boolean marked = false;
		for (int i = 0; i < globdimensions; i++)
		{
			for (int j = 0; j < globdimensions; j++)
			{
				if (CW[i][j] == '+')
				{
					x = i;
					y = j;
					CW[x][y] = 'a';
					marked = true;
					break;
				}
			}
			if (marked == true)
			{
				break;
			}
		}//end of for
		
		
		if(marked == false)
		{
			if(numsolution % 10000 == 0)
			{
			for (int i = 0; i < globdimensions; i++)
			{
				for (int j = 0; j < globdimensions; j++)
				{
					System.out.print(CW[i][j]);
				}
				System.out.println();
				
			}
			System.out.println();
			}
			numsolution++;
			
			
			
			
			return false;
			
		}
	while(true){	
			if (checkPrefixes() == true)
				{
					
					boolean solved = solveCrossword();
					
					if(solved)
					{
						
						
						
						
						
						
						return true;
					
					}
					else
					{
						if (CW[x][y] != 'z')
						{
							
							CW[x][y] = (char)(CW[x][y]+1);
						}
						else
						{
							
							CW[x][y] = '+';
							return false; //backtrack when all letters at current position are gone
						}
					
						
					}
				}
			else
				{
				
					if (CW[x][y] != 'z')
					{
						
						CW[x][y] = (char)(CW[x][y]+1);
						//System.out.println(CW[x][y]);
						
					}
					else
					{
						
						CW[x][y] = '+';
						return false; //backtrack when all letters at current position are gone
					}
				
					
				}
				
			}
		}
		
	}
	
	
	
	
	



    