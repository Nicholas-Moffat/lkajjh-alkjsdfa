package project.model;

import java.util.Random;

import project.view.ImageLoader;

/**
 * Lead Author(s):
 * @author Jordan Spencer
 * @author Nicholas Moffat
 * 
 * References:
 * Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented Problem Solving.
 * Retrieved from https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 *  
 * Version/date: 11/18/2023
 * 
 * Responsibilities of class: Handles the data logic of the program
 * 
 */

public class WorldModel {
	
	private static Player player;
	private static Room currentRoom;
	private static Enemy[] currentEnemies;
	private Random random;
	private LevelDesign level;
	
	
	public WorldModel() {
		random = new Random();
		level = new LevelDesign();
		currentRoom = level.LEVELARRAY[0];
		player = new Player ("player", currentRoom.getXStartPos(), currentRoom.getYStartPos());
		currentEnemies = currentRoom.getEnemies();
	}
	
	
	
	public static void initializeWorld() {
		ImageLoader.initializeSprites();
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	private static Tile getTileInFront(Organism organism, int dirX, int dirY) {
		return currentRoom.getTileAt(organism.getPosX() + dirX, organism.getPosY() + dirY);
	}
	
	public static Room getCurrentRoom() {
		return currentRoom;
	}
	
	public static Enemy[] getCurrentEnemies() {
		return currentEnemies;
	}
	
	public void movePlayer(int dirX, int dirY) {
		boolean playerMoved = false;
		if (currentRoom.enemyInRoom(getTileInFront(player, dirX, dirY).getPosX(), getTileInFront(player, dirX, dirY).getPosY()))
		{
			Enemy enemy = currentRoom.getEnemyAt(getTileInFront(player, dirX, dirY).getPosX(), getTileInFront(player, dirX, dirY).getPosY());
			int defense = enemy.getDefense();
			int damage = player.getStrength();
			int amount = damage - defense;
			enemy.damage(amount);
			switch(player.getFacing()) {
			case "up":
				enemy.setFacing("down");
				break;
			case "down":
				enemy.setFacing("up");
				break;
			case "left":
				enemy.setFacing("right");
				break;
			case "right":
				enemy.setFacing("left");
				break;
			}
			System.out.println("fightEnemy");
		}
		else {
			switch(getTileInFront(player, dirX, dirY).getName()) {
			
			case "floor":
				player.setPosition(player.getPosX() + dirX, player.getPosY() + dirY);
				playerMoved = true;
				break;
			case "open":
				player.setPosition(player.getPosX() + dirX, player.getPosY() + dirY);
				playerMoved = true;
				break;
			case "skeleton":
				break;
			case "gate":
				break;
			case "chest":
				break;
			case "wall":
				break;
			case "stairs":
				nextLevel();
				break;
			case "door":			
				nextLevel();
				break;
			default:
				break;
			}
			if (playerMoved == true) {
				moveEnemies();
			}
		}
	}
	
	public void moveEnemies() {
		for (Enemy enemy : currentEnemies) {
			String name;
			switch(random.nextInt(4)) {
			case 0:
				name = getTileInFront(enemy, 1, 0).getName();
				if(currentRoom.enemyInRoom(enemy.getPosX() + 1, enemy.getPosY())) {
					return;
				}
				else if(enemy.getPosX() + 1 == player.getPosX() && enemy.getPosY() == player.getPosY()) {
					int defense = player.getDefense();
					int damage = enemy.getStrength();
					int amount = damage - defense;
					player.damage(amount);
					System.out.println("fightPlayer");
					break;
				}	
				if(name == "floor" || name == "open") {
					enemy.setPosition(enemy.getPosX() + 1, enemy.getPosY());
					enemy.setFacing("right");
					break;
				}
			case 1:
				name = getTileInFront(enemy, -1, 0).getName();
				if(currentRoom.enemyInRoom(enemy.getPosX() - 1, enemy.getPosY())) {
					return;
				}
				else if(enemy.getPosX() - 1 == player.getPosX() && enemy.getPosY() == player.getPosY()) {
					int defense = player.getDefense();
					int damage = enemy.getStrength();
					int amount = damage - defense;
					player.damage(amount);
					System.out.println("fightPlayer");
					break;
				}	
				if(name == "floor" || name == "open") {
					enemy.setPosition(enemy.getPosX() - 1, enemy.getPosY());
					enemy.setFacing("left");
					break;
				}
			case 2:
				name = getTileInFront(enemy, 0, 1).getName();
				if(currentRoom.enemyInRoom(enemy.getPosX(), enemy.getPosY() + 1)) {
					return;
				}
				else if(enemy.getPosX() == player.getPosX() && enemy.getPosY() + 1 == player.getPosY()) {
					int defense = player.getDefense();
					int damage = enemy.getStrength();
					int amount = damage - defense;
					player.damage(amount);
					System.out.println("fightPlayer");
					break;
				}	
				if(name == "floor" || name == "open") {
					enemy.setPosition(enemy.getPosX(), enemy.getPosY() + 1);
					enemy.setFacing("down");
					break;
				}
			case 3:
				name = getTileInFront(enemy, 0, -1).getName();
				if(currentRoom.enemyInRoom(enemy.getPosX(), enemy.getPosY() - 1)) {
					return;
				}
				else if(enemy.getPosX() == player.getPosX() && enemy.getPosY() - 1 == player.getPosY()) {
					int defense = player.getDefense();
					int damage = enemy.getStrength();
					int amount = damage - defense;
					player.damage(amount);
					System.out.println("fightPlayer");
					break;
				}	
				if(name == "floor" || name == "open") {
					enemy.setPosition(enemy.getPosX(), enemy.getPosY() - 1);
					enemy.setFacing("up");
					break;
				}
			}
		}
		
	}
			
	public LevelDesign getLevel() {
		return level;
	}
	
	public void nextLevel() {
		int levelsCompleted = player.getLevelsCompleted();
		currentRoom = level.LEVELARRAY[levelsCompleted + 1];
		currentEnemies = currentRoom.getEnemies();
		player.setPosition(currentRoom.getXStartPos(), currentRoom.getYStartPos());
		// player.levelCompleted();

	}
}
