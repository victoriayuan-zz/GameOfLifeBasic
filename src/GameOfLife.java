import java.awt.*;

import javax.swing.*;

import java.awt.event.*;  // Needed for ActionListener
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

////////////////////////////////////////////////////////// class TemperatureConverter3
class GameOfLife extends JFrame 
{
    static Colony colony;
    static Timer t;
    private DrawArea board;
    private JButton run, stop, eradicate, populate, save, load;
    private int startx, starty, endx, endy;

    //======================================================== constructor
    public GameOfLife ()
    {
        // 1... Create/initialize components
        run = new JButton ("Run");
        stop = new JButton ("Stop");
        eradicate = new JButton ("Eradicate");
        populate = new JButton ("Populate");
        save = new JButton ("Save");
        load = new JButton ("Load");
        
        //Set location of buttons
        run.setBounds(0, 15, 100, 30);
        stop.setBounds(100, 15, 100, 30);
        eradicate.setBounds(200, 15, 100, 30);
        populate.setBounds(300, 15, 100, 30);
        save.setBounds(400, 15, 100, 30);
        load.setBounds(500, 15, 100, 30);
        
        //Add to listeners
        run.addActionListener (new ShowBtnListener ()); // Connect button to listener class
        stop.addActionListener (new StopBtnListener());
        eradicate.addActionListener (new EradicateListener());
        populate.addActionListener (new PopulateListener());
        save.addActionListener(new SaveListener());
        load.addActionListener (new LoadListener());
          
        colony = new Colony (0.3); // create colony with 60% density

        // Code to add Timer
        Movement moveColony = new Movement (colony); // ActionListener
        t = new Timer (500, moveColony); // set up timer
        
        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (null); // Use BorderLayout for panel
   
        //Add to mouselistener
        board = new DrawArea (550, 510);
        board.addMouseListener(new MListener());
        board.setBounds(50,45,600,510);
        
        // 3... Add the components to the output area.
        content.add (run);             // Add button
        content.add (stop);
        content.add (eradicate);
        content.add(populate);
        content.add (save);
        content.add (load);
        content.add (board); // Output area

        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("Life Demo");
        setSize (600, 580);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }


    //Action Listeners for each button
    class ShowBtnListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            t.start (); // start simulation
        }
    }
    
    class StopBtnListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
        	t.stop();
        }
    }
    
    class EradicateListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
        	colony.eradicate(startx,starty,endx,endy);
    		colony.show(board.getGraphics());
        }
    }
    
    class PopulateListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
        	colony.populate(startx,starty,endx,endy);
    		colony.show(board.getGraphics());
        }
    }
    
    class SaveListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
        	colony.save();
        }
    }
    
    class LoadListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
        	colony.load();
        	repaint();
        }
    }

    //MouseListener for eradicate/populate
    class MListener implements MouseListener {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		// TODO Auto-generated method stub
    	}

    	@Override
    	public void mousePressed(MouseEvent e) {
    		// TODO Auto-generated method stub
    		//Get x and y coordinates when clicked
    		startx = e.getX();
    		starty = e.getY();
    	}


    	@Override
    	public void mouseReleased(MouseEvent e) {
    		// TODO Auto-generated method stub
    		//Get x and y coordinates when mouse is released
    		endx = e.getX();
    		endy = e.getY();
    		int temp;
    		//If the drag starts from right or from below, swap
    		if (starty>endy) {
    			temp = starty;
    			starty = endy;
    			endy = temp;
    		}
    		if (startx>endx) {
    			temp = startx;
    			startx = endx;
    			endx = temp;
    		}
    	}


    	@Override
    	public void mouseEntered(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}


    	@Override
    	public void mouseExited(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}
    }

    //Draw area for the life simulation
    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void fillRect(int i, int j, int k, int l) {
			// TODO Auto-generated method stub
			
		}

		public void paintComponent (Graphics g)
        {
            colony.show (g);
        }
    }


    //Calls advance to continue
    class Movement implements ActionListener
    {
        private Colony colony;

        public Movement (Colony col)
        {
            colony = col;
        }


        public void actionPerformed (ActionEvent event)
        {
        	colony.advance (); // update life status
            repaint ();
        }
    }


    //======================================================== method main
    public static void main (String[] args)
    {
        GameOfLife window = new GameOfLife ();
        window.setVisible (true);
    }

}

class Colony
{
    private boolean grid[] [];
    private String filename;
    private boolean temp[][];

    public Colony (double density)
    {
        grid = new boolean [100] [100];
        temp = new boolean [100] [100];

        //Random life forms, create temp array that is identical to grid
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++) { 	
                grid [row] [col] = Math.random () < density; // fill array with life forms
        		temp[row][col] = grid[row][col];
            }
    }

    //Follows rules of Conway's Game of Life; use temp array to change values completely
    public void advance() {
    	boolean temp[][] = new boolean [100][100];
    	for (int i=0; i<temp.length; i++) {
    		for (int j=0; j<temp[i].length; j++) {
    			if (live(i,j) < 2) {
    				temp[i][j]=false;
    			}
    			else if (live(i,j) == 2 && grid[i][j]==true) {
    				temp[i][j]=true;
    			}
    			else if (live(i,j) == 3) {
    				temp[i][j]=true;
    			}
    			else if (live(i,j) > 3) {
    				temp[i][j]=false;
    			}
    		}
    	}
    	grid=temp;
	}
    
    //Determines number of neighbours
    public int live(int i, int j) {
    	int nbour = 0;
    	//Relative coordinate arrays
    	int row[] = {-1,0,1,-1,1,-1,0,1};
    	int column[] = {1,1,1,0,0,-1,-1,-1};
    	
    	for (int a=0; a<8; a++) {
    		if ((i+row[a])>=0 && (j+column[a])>=0 && (i+row[a])<=99 && (j+column[a])<=99) {
    			if (grid[i+row[a]][j+column[a]]) {
        			nbour++;
        		}
    		}	
    	}
    	return nbour;
    }
    
    //Eradicates an area
    public void eradicate (int startx, int starty, int endx, int endy){
    	int startcol = (startx-2)/5;
    	int startrow = (starty-2)/5;
    	int endcol = (endx-2)/5;
    	int endrow = (endy-2)/5;
    	
    	for (int i=startrow; i<endrow; i++) {
    		for (int j=startcol; j<endcol; j++) {
    			int random = (int)(Math.random()*10);
    			try {
    				//70% success rate
    				if(random<7 && grid[i][j]==true) {
    					grid[i][j]=false;
    				}
    			}
    			catch (Exception e) {
    				
    			}
    		}
    	}
    }
    
    //Populates an area from mouselistener
    public void populate (int startx, int starty, int endx, int endy){
    	int startcol = (startx-2)/5;
    	int startrow = (starty-2)/5;
    	int endcol = (endx-2)/5;
    	int endrow = (endy-2)/5;
    	
    	for (int i=startrow; i<endrow; i++) {
    		for (int j=startcol; j<endcol; j++) {
    			int random = (int)(Math.random()*10);
    			try {
    				if(random<7 && grid[i][j]==false) {
    					grid[i][j]=true;
    				}
    			}
    			catch (Exception e) {
    				
    			}
    		}
    	}
    }
    
    public void save () {
		filename = JOptionPane.showInputDialog (null, "Name of File", "Save File", JOptionPane.QUESTION_MESSAGE);
		if (filename != null) {
		    try {
				PrintStream out = new PrintStream (new FileOutputStream (
					    filename + ".txt"));
				for (int row = 0 ; row < grid.length ; row++)
				{
				    for (int col = 0 ; col < grid [0].length ; col++) {
						if (grid [row] [col]) {
						    out.println ("0");
						}
						else {
						    out.println ("1");
						}
				    }
				}
				out.close ();
				JOptionPane.showMessageDialog (null, "Your file has been saved as " + filename + ".txt", "Save File", JOptionPane.INFORMATION_MESSAGE);
		
		    }
		    catch (FileNotFoundException e) {
		    	e.printStackTrace ();
		    }
		}
    }

    //Loads a file
    public void load () {
		JFrame frame = new JFrame ();
		JFileChooser fc = new JFileChooser ();
		fc.setCurrentDirectory (new File (System.getProperty ("user.home")));
		int returnVal = fc.showOpenDialog (frame);
		File tempfile = fc.getSelectedFile();
		int a = 0;
	
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
		    BufferedReader file = null;
		    try {
				file = new BufferedReader (new FileReader (tempfile.getName ()));
				String line;
				for (int i = 0 ; i < grid.length ; i++) {
				    for (int y = 0 ; y < grid [0].length ; y++) {
						line = file.readLine ();
						{
						    try {
						    	a = Integer.parseInt (line);
						    }
						    catch (Exception e){
						    }
						    if (a == 0) {
								grid [i] [y] = true;
								temp [i] [y] = true;
						    }
						    else {
								if (a == 1) {
								    grid [i] [y] = false;
								    temp [i] [y] = false;
								}
						    }
						}
				    }
				}
		    }
		    catch (IOException e) {
		    }
		    finally {
				try {
				    if (file != null)
					file.close ();
				}
				catch (IOException ex) {
				    ex.printStackTrace ();
				}
		    }
		}
		else {
		
		}
		filename = tempfile.getName ();
    }

    //Saves a file
	public void show (Graphics g)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                if (grid [row] [col]) // life form present
                    g.setColor (Color.green);
                else // no life form
                    g.setColor (Color.white);
                g.fillOval (col * 5 + 2, row * 5 + 2, 5, 5); // draw life form (or erase previous one)
            }
    }
}