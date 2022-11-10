import java.util.Locale;
import java.util.Scanner;
import java.io.*;

public class Game_Engine {
	
	// Defined but no value assigned until its being used
	private Player hero;
	private Battle_Engine battle;
	
	// For input
	private Scanner input = new Scanner(System.in);

	// Assigned save path - if compiled on linux, the path will be different
	private final String SAVEPATH = "c:\\savegames\\";
	// Stores save file name
	private String filename;
	
	// Version number
	private final double version = 0.10;
	
	// Constant variables for main menu
	private final int NEWGAME = 1;
	private final int LOADGAME = 2;
	private final int QUIT = 0;
	
	// Constant variables for game menu
	private final String UP_LEVEL = "1";
	private final String CURRENT_LEVEL = "2";
	private final String DISPLAY_PLAYER = "3";
	private final String CHANGE_NAME = "4"; 
	private final String HEAL_PLAYER = "5";
	private final String SAVE_GAME = "6";
	private final String GO_TO_MAIN = "7";
	
	// Constants for level
	private int CURRENTLEVEL = 1;
	private final int MAXLEVEL = 100;

	// Default constructor
	public Game_Engine() {}
	
	// Display main menu
	public void main_Menu()
	{
		int choice;
		
		do
		{
			System.out.println("Tower Of Death - version " + version + "\n");
			System.out.println("1 - New Game");
			System.out.println("2 - Load Game");
			System.out.println("\nEnter a choice (or enter 0 to exit):");
		
			choice = input.nextInt();
			input.nextLine();
		
			switch(choice)
			{
				case NEWGAME: // Create new game
					hero = new Player();
					changeName();
					game_Menu();
					break;
				case LOADGAME:
					// Check if save file exists
					if(load_game())
					{
						System.out.println(filename + " has been loaded\n");
						game_Menu();
					}
					break;
				case QUIT:
					System.out.println("Exiting Tower Of Death");
					break;
				default:
					System.out.println("Invalid option\n");
			}
		}
		while(choice != QUIT);
	}
	
	// Display game menu
	public void game_Menu()
	{
		String choice;
		
		do
		{
			System.out.println("\nGame Menu\n");
			if(CURRENTLEVEL == 100) // Display different option if player reaches level 100
			{
				System.out.println("1 - Go back to Level 1");
			}
			else
			{
				System.out.println("1 - Go up to level " + (CURRENTLEVEL + 1));
			}
			System.out.println("2 - Enter level " + CURRENTLEVEL);
			System.out.println("3 - Show player stats");
			System.out.println("4 - Change player name"); 
			System.out.println("5 - Heal player");
			System.out.println("6 - Save game");
			System.out.println("7 - Go back to main menu");
			System.out.println("\nEnter option (1 - 7):");
			
			choice = input.next();
			
			switch(choice)
			{
				case UP_LEVEL:
					// Give player the choice to either stay at the top level or go back to level 1
					if(CURRENTLEVEL == MAXLEVEL)
					{
						System.out.println("Player has reached the top level and sees a portal to go back to Level 1");
						System.out.println("Do you want to go back to Level 1?(Y/N):");
						if(input.next().equalsIgnoreCase("y"))
						{
							System.out.println("Player enters portal to Level 1\n");
							CURRENTLEVEL = 1;
						}
					}
					else
						CURRENTLEVEL++;
					break;
				case CURRENT_LEVEL:
					// Enter current level
					System.out.println("Entering level " + CURRENTLEVEL + "\n");
					battle = new Battle_Engine();
					hero = battle.battle_loader(hero, CURRENTLEVEL);
					break;
				case DISPLAY_PLAYER:
					// Display current stats of player
					System.out.println(hero.playerStats() + "\n");
					break;
				case CHANGE_NAME:
					// Change name of player
					changeName();
					break;
				case HEAL_PLAYER:
					// Heal wounded player to full HP
					hero.healHP();
					System.out.println("Player has been healed\n");
					break;
				case SAVE_GAME:
					// Save game progress
					save_game();
					break;
				case GO_TO_MAIN:
					// Exit to main main
					System.out.println("Going back to main menu\n\n");
					break;
				default:
					System.out.println("Invalid option, Please try again\n\n");
			}
		}
		while(!choice.contentEquals(GO_TO_MAIN));
	}

	// Prompts user to change name of player
	private void changeName()
	{
		System.out.println("Enter new name: ");
		String newname = input.next();
		hero.setName(newname);
	}
	
	// Load saved file
	public boolean load_game()
	{
		try
		{
			System.out.println("Enter player name: ");
			filename = input.next();
			filename = SAVEPATH + filename;
			File loadgame = new File(filename);
			
			// Set up file buffer
			Scanner filereader = new Scanner(loadgame);
		
			// Load player data from file to Player object
			String name = filereader.next();
			long exp = filereader.nextLong();
			int lvl = filereader.nextInt();
			int hp = filereader.nextInt();
			int mp = filereader.nextInt();
			int str = filereader.nextInt();
			int agl = filereader.nextInt();
			int Int = filereader.nextInt();
			int sta = filereader.nextInt();
			int lck = filereader.nextInt();
			int atk = filereader.nextInt();
			int def = filereader.nextInt();
			CURRENTLEVEL = filereader.nextInt();
			
			hero = new Player(name, exp, lvl, hp, mp, str, agl, Int, sta, lck, atk, def);
			
			filereader.close();
				        
			return true;
		}
		catch(FileNotFoundException fnf )
		{
			System.out.println(filename + " not found\n");
			return false;
		}	
	}
	
	// Saves game to file
	public void save_game()
	{
		// Set filename to player name
		filename = SAVEPATH + hero.getName();
		BufferedWriter out;
		
		try // Save player to file
		{
			// Open file writter
			out = new BufferedWriter(new FileWriter(filename));
			// Save all player stats to file
			out.write(hero.getName() + " ");
			out.write(hero.getEXP() + " ");
			out.write(hero.getLVL() + " ");
			out.write(hero.getHP() + " ");
			out.write(hero.getMP() + " ");
			out.write(hero.getSTR() + " ");
			out.write(hero.getAGL() + " ");
			out.write(hero.getINT() + " ");
			out.write(hero.getSTA() + " ");
			out.write(hero.getLCK() + " ");
			out.write(hero.getATK() + " ");
			out.write(hero.getDEF() + " ");
			// Save current level to file
			out.write(CURRENTLEVEL + " ");
			// Close file to finish
			out.close();
			System.out.println(filename + " has been saved\n");
		} 
		catch (IOException e) // Only gets triggered if unable to write to file
		{
			System.out.println(SAVEPATH + " not found\n");
			//e.printStackTrace();
		}
	}
	
}