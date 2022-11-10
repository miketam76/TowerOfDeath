import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Battle_Engine {

	// Constants for battle menu
	private final int ATTACK = 1;
	private final int DEFEND = 2;
	private final int HEAL = 3;
	private final int RUN_AWAY = 4;

	// Arraylists for spawned enemies
	private ArrayList<Enemy_REG> EnemySpawn = new ArrayList<Enemy_REG>();
	private ArrayList<Enemy_Jackal> Jackal = new ArrayList<Enemy_Jackal>();

	// For input
	private Scanner input = new Scanner(System.in);

	private final int MAXLEVEL = 100; // Maximum level of game
	private int lastEnemyRecord = 0; // Used to record how many regular enemies spawned

	// Default constructor
	public Battle_Engine() {}

	// Load battle when player enters a level
	public Player battle_loader(Player hero, int lvl)
	{

		// Spawn Jackal based on level and hero stats
		if(lvl == 10)
		{
			spawnJackal(1, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 20)
		{
			spawnJackal(1, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 30)
		{
			spawnJackal(2, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 40)
		{
			spawnJackal(2, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 50)
		{
			spawnJackal(3, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 60)
		{
			spawnJackal(3, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 70)
		{
			spawnJackal(4, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 80)
		{
			spawnJackal(4, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == 90)
		{
			spawnJackal(5, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		else if(lvl == MAXLEVEL)
		{
			spawnJackal(6, hero.getHP(), hero.getSTR(), hero.getAGL(), hero.getSTA(), hero.getATK(), hero.getDEF());
		}
		// For all other levels, load regular enemies
		else
		{
			spawnEnemyREG(lvl);
		}

		hero = battle_menu(hero, lvl);

		return hero;

	}

	//  Display Battle menu interface
	private Player battle_menu(Player hero, int lvl)
	{
		// EXP gained for  # enemies defeated
		long enemyEXP_Gain = 1000 * lvl * hero.getLVL() * lastEnemyRecord;
		long jackalEXP_Gain = 10000 * lvl * hero.getLVL();
		
		int choice;
				
		// Determine which enemy the player will face
		if(!EnemySpawn.isEmpty()) // For regular enemies
		{
			do
			{
				// TODO: add player current HP and MP to display to screen
				System.out.println("\nBattle Menu\n");
				System.out.println("1 - Attack");
				System.out.println("2 - Defend");
				System.out.println("3 - Heal");
				System.out.println("4 - Run Away\n");
				System.out.println("Choose an option (1 - 4):");
				choice = input.nextInt();
				switch(choice)
				{
					case ATTACK:
						if(lastEnemyRecord > 0) // Track how many remaining
						{	
							EnemySpawn.get(0).setHP(hero.attack(EnemySpawn.get(0).getHP()));
							if(EnemySpawn.get(0).getHP() > 0)
							hero.setHP(EnemySpawn.get(0).attack(hero.getHP()));
							else
							{	
								defeatEnemy();	
								System.out.println("Remaining enemies " + lastEnemyRecord + "\n");
							}
						}
						break;
					// Player defends - nothing happens
					//  TODO: needs to be fixed in which the hero defence and gear will determine how much damage
					// an enemy can do to the player
					case DEFEND:
						System.out.println("Player defends, no damage\n");
						break;
					// TODO: Fix heal which heal HP depends on player level
					case HEAL:
						hero.healHP();
						System.out.println("Player has healed self\n");
						break;
						// TODO: Add a scan enemy option for player level 20 and up
					case RUN_AWAY: // Run away option, clear enemy buffer
						EnemySpawn.clear();
						break;
					default:
				}
			}	
			while(choice != RUN_AWAY && hero.getHP() > 0 && lastEnemyRecord > 0);

			// All enemies defeated, player gains EXP and levels up if applicable
			if (lastEnemyRecord == 0)
			{
				System.out.println("All enemies have been defeated!\n");
				hero = calculateEXP(hero, enemyEXP_Gain);
			}
			// Player perished - HP <= 0
			else if(hero.getHP() <= 0)
				System.out.println("Player has perished!\n\n");
			else
				// Player ran away
				System.out.println("Player has run away!\n\n");
			return hero;
		}
		// You fight Jackal - only applies to level 10, 20, 30, 40, 50, 60, 70, 80, 90, 100
		else if(!Jackal.isEmpty())
		{
			do
			{
				System.out.println("\nBattle Menu\n");
				System.out.println("1 - Attack");
				System.out.println("2 - Defend");
				System.out.println("3 - Heal");
				System.out.println("4 - Run Away\n");
				System.out.println("Choose an option (1 - 4):");
				choice = input.nextInt();
				switch(choice)
				{
					case ATTACK:
						if(!Jackal.isEmpty())
						{	
							Jackal.get(0).setHP(hero.attack(Jackal.get(0).getHP()));
							if(Jackal.get(0).getHP() > 0)
								hero.setHP(Jackal.get(0).attack(hero.getHP()));
							else
							{	
								defeatJackal();		
							}
						}
						break;
					case DEFEND:
						System.out.println("Player defends, no damage\n");
						break;
					case HEAL:
						hero.healHP();
						System.out.println("Player has healed self\n");
						break;
					case RUN_AWAY:
						Jackal.clear(); // Clears enemy buffer		
						break;
					default:
				}
			}	
			while(choice != RUN_AWAY && hero.getHP() > 0 && !Jackal.isEmpty());
	
			if(Jackal.isEmpty() && choice != RUN_AWAY)
			{
				System.out.println("Jackal has been defeated!\n");
				hero = calculateEXP(hero, jackalEXP_Gain);
			}
			else if(hero.getHP() <= 0)
				System.out.println("Player has perished!\n\n");
			else
				System.out.println("Player has run away!\n\n");
			return hero;
		}
		return hero;
	}

	// Levels up player based on the amount of EXP gained per battle
	private Player calculateEXP(Player hero, long exp)
	{
		System.out.println(hero.getName() + " has gained " + exp + " EXP!\n");
		hero.setEXP(exp);
		if(hero.getEXP() >= 1000 && hero.getEXP() < 10000)// level 2
		{
			hero.setLVL();
			hero.levelHP();
			hero.setMP();
			hero.setSTR();
			hero.setAGL();
			hero.setINT();
			hero.setSTA();
			hero.setLCK();
			hero.setATK();
			hero.setDEF();
		}
		else if(hero.getEXP() >  10000 * hero.getLVL()) // Level 3 
		{
			hero.setLVL();
			hero.levelHP();
			hero.setMP();
			hero.setSTR();
			hero.setAGL();
			hero.setINT();
			hero.setSTA();
			hero.setLCK();
			hero.setATK();
			hero.setDEF();
		}
		
		else if(hero.getEXP() >  20000 * hero.getLVL()) // Level 4
		{
			hero.setLVL();
			hero.levelHP();
			hero.setMP();
			hero.setSTR();
			hero.setAGL();
			hero.setINT();
			hero.setSTA();
			hero.setLCK();
			hero.setATK();
			hero.setDEF();
		}
		else if(hero.getEXP() >  30000 * hero.getLVL()) // Level 4 and beyond
		{
			hero.setLVL();
			hero.levelHP();
			hero.setMP();
			hero.setSTR();
			hero.setAGL();
			hero.setINT();
			hero.setSTA();
			hero.setLCK();
			hero.setATK();
			hero.setDEF();
		}
			
		return(hero);
	}
	// Creates a random number of enemies to defeat based on the lvl it assigns to it
	private void spawnEnemyREG(int lvl)
	{
		Random rnd = new Random();
		int spawn = rnd.nextInt(5) + 1;
		for(int i = 0; i < spawn; i++)
		{
			EnemySpawn.add(new Enemy_REG(lvl));
			lastEnemyRecord++;
		}
		System.out.println(lastEnemyRecord + " enemies have appeared!\n");
	}
		
	private void spawnJackal(int lvl, int hp, int str, int agl, int sta, int atk, int def)
	{
		Jackal.add(new Enemy_Jackal(lvl, hp, str, agl, sta, atk, def));
		System.out.println("Jackal has appeared!\n");
	}
		
	private void defeatJackal()
	{
		Jackal.remove(0);
	}
		
	// Determines if all regular enemies have been defeated
	private boolean defeatEnemy()
	{
		// Check if the enemy queue is not empty
		if(lastEnemyRecord > 0)
		{
			// Remove the defeated one off the list
			EnemySpawn.remove(0);
			lastEnemyRecord--;
			return false;
		}
		else
		{
			return true;
		}
	}

	public ArrayList <Enemy_REG> getEnemy_Reg()
	{
		return EnemySpawn;
	}
	
	public ArrayList <Enemy_Jackal> getJackal()
	{
		return Jackal;
	}

}
