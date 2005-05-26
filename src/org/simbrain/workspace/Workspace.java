/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2003 Jeff Yoshimi <www.jeffyoshimi.net>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.workspace;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.simbrain.network.NetworkFrame;
import org.simbrain.network.UserPreferences;
import org.simbrain.util.SFileChooser;
import org.simbrain.world.WorldFrame;

public class Workspace extends JFrame implements ActionListener{

	private JDesktopPane desktop;
	private static final String FS = System.getProperty("file.separator");
	// File separator.  For platfrom independence.
	private static final String defaultFile = "." + FS + "simulations" + FS + "sims" + FS + "default.xml";
	
	File current_file = null;
	//TODO: Make default window size settable, sep for net, world, gauge
	int width = 450;
	int height = 450;

	private ArrayList networkList = new ArrayList();
	private ArrayList worldList = new ArrayList();


	//TODO: Window closing events remove networks from list
	
	/**
	 * Default constructor
	 */
	public Workspace()
	{
	
		super("Simbrain");

		//Make the big window be indented 50 pixels from each edge
		//of the screen.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
							screenSize.width  - inset*2,
							screenSize.height - inset*2);

		//Set up the GUI.
		desktop = new JDesktopPane(); //a specialized layered pane
		createFrame(); //create first "window"
		setContentPane(desktop);
		setJMenuBar(createMenuBar());

	    //Make dragging a little faster but perhaps uglier.
	    //desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	}
	
	/**
	 * Build the menu bar
	 * 
	 * @return the menu bar
	 */
	protected JMenuBar createMenuBar() {
			JMenuBar menuBar = new JMenuBar();

			//Set up the lone menu.
			JMenu menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_D);
			menuBar.add(menu);

			//Set up the first  item.
			JMenuItem menuItem = new JMenuItem("Open Workspace");
			menuItem.setMnemonic(KeyEvent.VK_O);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_O,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.setActionCommand("openWorkspace");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			
			menuItem = new JMenuItem("Save Workspace");
			menuItem.setMnemonic(KeyEvent.VK_S);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_S,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.setActionCommand("saveWorkspace");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			
			menuItem = new JMenuItem("New Network");
			menuItem.setMnemonic(KeyEvent.VK_N);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.setActionCommand("newNetwork");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			
			
			menuItem = new JMenuItem("New World");
			menuItem.setMnemonic(KeyEvent.VK_W);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.setActionCommand("newWorld");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			//Set up the second menu item.
			menuItem = new JMenuItem("Quit");
			menuItem.setMnemonic(KeyEvent.VK_Q);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.setActionCommand("quit");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			return menuBar;
	}

	/**
	 *  React to menu selections
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand(); 
		
		if (cmd.equals("newNetwork"))
		{
			addNetwork(new NetworkFrame(this));
		} else if (cmd.equals("newWorld")) {
			addWorld(new WorldFrame(this));
		}  else if (cmd.equals("openWorkspace")) {
			showOpenFileDialog();
		}  else if (cmd.equals("saveWorkspace")) {
			showSaveFileDialog();
		} else if (cmd.equals("quit")) {
			quit();
		}
	}

	/**
	 * Create a new internal frame
	 */
	protected void createFrame() {
		WorkspaceSerializer.readWorkspace(this, new File(defaultFile));
	}

	/**
	 * Add a network to the workspace
	 * 
	 * @param network the networkFrame to add
	 */
	public void addNetwork(NetworkFrame network) {
		desktop.add(network);
		networkList.add(network);
		network.setResizable(true);
		network.setMaximizable(true);
		network.setIconifiable(true);
		network.setClosable(true);		
		
		if(networkList.size() == 1) {
			network.setBounds(5, 35, width, height);
		} else {
			int newx = ((NetworkFrame)networkList.get(networkList.size() - 2)).getBounds().x + 40;
			int newy = ((NetworkFrame)networkList.get(networkList.size() - 2)).getBounds().y + 40;	
			network.setBounds(newx, newy, width, height);
		}			
		
		network.setVisible(true); //necessary as of 1.3
		try {
			network.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {}

	}
	
	/**
	 * Add a world to the workspace
	 * 
	 * @param world the worldFrame to add
	 */
	public void addWorld(WorldFrame world) {
		desktop.add(world);
		worldList.add(world);
		world.setResizable(true);
		world.setMaximizable(true);
		world.setIconifiable(true);
		world.setClosable(true);
		if(worldList.size() == 1) {
			world.setBounds(505, 35, width, height);
		} else {
			int newx = ((WorldFrame)worldList.get(worldList.size() - 2)).getBounds().x + 40;
			int newy = ((WorldFrame)worldList.get(worldList.size() - 2)).getBounds().y + 40;	
			world.setBounds(newx, newy, width, height);
		}
		// Temporary until coupling designeds
		if(networkList.size() != 0) {
			getLastNetwork().setWorld(world);
		}
		
		world.setVisible(true);
		try {
			world.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {}

	}

	/**
	 * @return reference to the last network added to this workspace
	 */
	public NetworkFrame getLastNetwork() {
		if (networkList.size() > 0)
			return (NetworkFrame)networkList.get(networkList.size()-1);
		else return null;
	}

	/**
	 * @return reference to the last world added to this workspace
	 */
	public WorldFrame getLastWorld() {
		if (worldList.size() > 0)
			return (WorldFrame)worldList.get(networkList.size()-1);
		else return null;
	}
	
	/**
	 * Remove all items (networks, worlds, etc.) from this workspace
	 */
	public void clearWorkspace() {
		
		for(int i = 0; i < networkList.size(); i++) {
			try {
				((NetworkFrame)networkList.get(i)).setClosed(true);
			} catch (java.beans.PropertyVetoException e) {}
		}
		networkList.clear();

		for(int i = 0; i < worldList.size(); i++) {
			try {
				((WorldFrame)worldList.get(i)).setClosed(true);
			} catch (java.beans.PropertyVetoException e) {}
		}		
		worldList.clear();
	}
	

	/**
	 * Shows the dialog for opening a simulation file
	 */
	public void showOpenFileDialog() {
	    SFileChooser simulationChooser = new SFileChooser("." + FS 
	        	        + "simulations"+ FS + "sims", "xml");
		File simFile = simulationChooser.showOpenDialog();
		if(simFile != null){
		    WorkspaceSerializer.readWorkspace(this, simFile);
		    current_file = simFile;
		}
	}

	/**
	 * Shows the dialog for saving a simulation file
	 */
	public void showSaveFileDialog(){
	    SFileChooser simulationChooser = new SFileChooser("." + FS 
    	        + "simulations"+ FS + "sims", "xml");
	    File simFile = simulationChooser.showSaveDialog();
	    if(simFile != null){
	    		WorkspaceSerializer.writeWorkspace(this, simFile);
	    		current_file = simFile;
	    }
	}


	
	/**
	 * Quit the application	 
	 */
	protected void quit() {
			UserPreferences.saveAll(); // Save all user preferences
			System.exit(0);
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
			//Make sure we have nice window decorations.
			//JFrame.setDefaultLookAndFeelDecorated(true);

			//Create and set up the window.
			Workspace sim = new Workspace();
			sim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			//Display the window.
			sim.setVisible(true);
	}
	

	/**
	 * Simbrain main method.  Creates a single instance of the Simulation class
	 * 
	 * @param args currently not used
	 */
	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel(new MetalLookAndFeel());
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
						createAndShowGUI();
				}
			});
		} catch (Exception e) {
			System.err.println("Couldn't set look and feel!");
		}
	}
	

	/**
	 * @return Returns the networkList.
	 */
	public ArrayList getNetworkList() {
		return networkList;
	}
	/**
	 * @param networkList The networkList to set.
	 */
	public void setNetworkList(ArrayList networkList) {
		this.networkList = networkList;
	}
	/**
	 * @return Returns the worldList.
	 */
	public ArrayList getWorldList() {
		return worldList;
	}
	/**
	 * @param worldList The worldList to set.
	 */
	public void setWorldList(ArrayList worldList) {
		this.worldList = worldList;
	}
}
