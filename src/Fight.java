import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.*;

public class Fight extends JFrame {
	private boolean win;
	private Weapon weapon_1, weapon_2;
	private JTextArea text, healthPlayer, healthBot;
	private JScrollPane console;
	private JLabel playerIcon, botIcon;
	private JButton characterButton, weaponButton, rankingButton, fightButton, clearButton;
	private JPanel buttonPanel_1, charactersPanel, characterPanel_1, characterPanel_2, weaponsPanel, statsPanel, statsLvlPanel, buttonPanel_2, consolePanel, mainPanel;
	private int battleScore = 0;
	private int totalScore = 0;
	private int enemiesDefeated = 0;
	private int injuriesCaused = 0;
	private int injuriesSuffered = 0;
	private String username;
	Warrior newBot;
	Warrior newWarrior;

	public Fight(Warrior characterChosen, Warrior randomBot) throws SQLException {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		characterButton = new JButton("Choose Character");
		characterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WarriorsFrame newSelectWarrior = new WarriorsFrame();
					newSelectWarrior.addWindowListener(
							new WindowAdapter() {
								@Override
								public void windowClosed(WindowEvent e) {
									newWarrior = newSelectWarrior.getWarrior();
									characterChosen.setId(newWarrior.getId());
									characterChosen.setWeapon(null);
									characterChosen.setHealth(newWarrior.getHealth());
									characterChosen.setAgility(newWarrior.getAgility());
									characterChosen.setName(newWarrior.getName());
									characterChosen.setSpeed(newWarrior.getSpeed());
									characterChosen.setUrl(newWarrior.getUrl());
									characterChosen.setDefense(newWarrior.getDefense());
									characterChosen.setPoints(newWarrior.getPoints());
									characterChosen.setStrenght(newWarrior.getStrenght());
								}
							});
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		weaponButton = new JButton("Choose weapon");
		weaponButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WeaponsFrame newWeapon = new WeaponsFrame(characterChosen);
					newWeapon.addWindowListener(
							new WindowAdapter() {
								@Override
								public void windowClosed(WindowEvent e) {
									characterChosen.setWeapon(newWeapon.getWeapon());
								}
							});
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		rankingButton = new JButton("Ranking");
		rankingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Ranking();
			}
		});
		buttonPanel_1 = new JPanel();
		buttonPanel_1.add(characterButton);
		buttonPanel_1.add(weaponButton);
		buttonPanel_1.add(rankingButton);

		healthPlayer = new JTextArea(3, 32);
		healthPlayer.setEditable(false);
		healthPlayer.setText("100%");
		healthPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		healthPlayer.setAlignmentY(Component.CENTER_ALIGNMENT);

		characterPanel_1 = new JPanel();
		characterPanel_1.add(healthPlayer);

		healthBot = new JTextArea(3, 32);
		healthBot.setText("100%");
		characterPanel_2 = new JPanel();
		characterPanel_2.add(healthBot);

		charactersPanel = new JPanel();
		charactersPanel.add(characterPanel_1);
		charactersPanel.add(characterPanel_2);

		fightButton = new JButton("Fight");
		fightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(characterChosen.getName() + " ++ " + characterChosen.getWeapon());
					System.out.println(randomBot.getName() + " ++ " + randomBot.getWeapon());
					combat(characterChosen, randomBot);
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		clearButton = new JButton("Clear Console");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("");
			}
		});
		buttonPanel_2 = new JPanel();
		buttonPanel_2.add(fightButton);
		buttonPanel_2.add(clearButton);

		text = new JTextArea(10, 67);
		text.setEditable(false);
		console = new JScrollPane(text);
		console.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		console.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consolePanel = new JPanel();
		consolePanel.add(console);
		console.setSize(700, 100);

		mainPanel = new JPanel();
		mainPanel.add(buttonPanel_1);
		mainPanel.add(charactersPanel);
		mainPanel.add(buttonPanel_2);
		mainPanel.add(consolePanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.add(mainPanel);

		characterButton.setEnabled(false);
		weaponButton.setEnabled(false);

		this.setTitle("Menu");
		this.setLocation(300, 0);
		this.setSize(800, 700);
		this.setResizable(false);
		this.setVisible(true);
	}

	//	Method that counts a warrior's total speed
	public int total_speed(Warrior warrior, Weapon weapon) {
		int count_speed;
		count_speed = warrior.getSpeed() + weapon.getSpeed();
		return count_speed;
	}

	//	Method that counts a warrior's total agility
	public int total_agility(Warrior warrior) {
		int count_agility;
		count_agility = warrior.getAgility();
		return count_agility;
	}

	//	Method that determines if a warrior can perform an attack or not
	public boolean perform_attack(Warrior warrior) {
		String message;
		int num = (int) (Math.floor(Math.random() * 100) + 1);
		if ((warrior.getAgility() * 10) > num) {
			message = "\n" + warrior.getName() + ": Attack successfully performed.";
			text.append(message);
			return true;
		} else {
			message = "\n" + warrior.getName() + ": Attack failed.";
			text.append(message);
			return false;
		}
	}
	//	Method that determines if a warrior can dodge an attack or, in case of receiving it, how much damage recieves
	public String dodge_attack(Warrior attacker, Weapon weapon, Warrior defender) {
		String message;
		int damage = attacker.getStrenght() + weapon.getStrenght() - defender.getDefense();
		int num = (int) (Math.floor(Math.random() * 50) + 1);
		if ((defender.getAgility()) > num) {
			message = defender.getName() + " dodged the attack.";
		} else {
			defender.setHealth(defender.getHealth() - damage);
			message = defender.getName() + " has recieved " + damage + " damage points.";

		}
		return message;
	}

	//	Method that determines if a warrior repeats an attack or gives his turn to his opponent
	public boolean repeat_attack(Warrior attacker, Weapon attackerWeapon, Warrior defender, Weapon defenderWeapon) {
		boolean repeatAttack;
		int num = (int) (Math.floor(Math.random() * 100) + 1);
		if (total_speed(attacker, attackerWeapon) <= total_speed(defender, defenderWeapon)) {
			repeatAttack = false;
		} else {
			if (((total_speed(attacker, attackerWeapon) - total_speed(defender, defenderWeapon)) * 10) > num) {
				repeatAttack = true;
				text.append("\n" + attacker.getName() + " attacks again.");
			} else {
				repeatAttack = false;
			}
		}
		return repeatAttack;
	}

	//	Method that determines who's the warrior that have won
	public String winner(Warrior warrior1, Warrior warrior2) {
		String message = "";
		if (warrior1.getHealth() <= 0) {
			message = warrior2.getName() + " wins!";
		} else if (warrior2.getHealth() <= 0) {
			message = warrior1.getName() + " wins!";
		}
		return message;
	}

	//	Method that determines the fight logic
	public void combat(Warrior player, Warrior bot) throws SQLException {

		Warrior attacker;
		Warrior defender;
		int initialHealthPlayer = player.getHealth();
		int initialHealthBot = bot.getHealth();

		int num = (int) (Math.floor(Math.random() * 2) + 1); // Random num in case both speed and agility are equal

		if (total_speed(player, player.getWeapon()) > total_speed(bot, bot.getWeapon())) {
			attacker = player;
			defender = bot;
		} else if (total_speed(bot, bot.getWeapon()) > total_speed(player, player.getWeapon())) {
			attacker = bot;
			defender = player;
		} else {
			if (total_agility(player) > total_agility(bot)) {
				attacker = player;
				defender = bot;
			} else if (total_agility(bot) > total_agility(player)) {
				attacker = bot;
				defender = player;
			} else {
				if (num == 1) {
					attacker = player;
					defender = bot;
				} else {
					attacker = bot;
					defender = player;
				}
			}
		}
		while (true) {
			while (true) {
				text.append("\n\n" + attacker.getName() + " turn:");
				//perform_attack(attacker);
				if (perform_attack(attacker)) {
					text.append("\n" + dodge_attack(attacker, attacker.getWeapon(), defender));
				}
				text.append("\n" + attacker.getName() + " remaining health: " + attacker.getHealth());
				text.append("\n" + defender.getName() + " remaining health: " + defender.getHealth());
				if (!repeat_attack(attacker, attacker.getWeapon(), defender, defender.getWeapon())) {
					break;
				}

			}
			if (attacker.getHealth() <= 0 || defender.getHealth() <= 0) {
				text.append("\n*******************************\n" + winner(attacker, defender));
				if (winner(attacker, defender).equals(player.getName() + " wins!")) {
					win = true;
					enemiesDefeated += 1;
				}
				// Calculate the points, damage taken & done and enemies slayed
				battleScore += calculateScore(bot);
				totalScore += battleScore;
				injuriesSuffered += initialHealthPlayer - player.getHealth();
				injuriesCaused += initialHealthBot - bot.getHealth();

				fightButton.setEnabled(false);
				characterButton.setEnabled(false);
				weaponButton.setEnabled(false);
				int selectContinue = JOptionPane.showOptionDialog(mainPanel, "Do you want to continue fighting?", "Continue",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (selectContinue == 0) {
					// Button "Yes" from joptionpane
					fightButton.setEnabled(true);
					characterButton.setEnabled(true);
					weaponButton.setEnabled(true);

					//create new bot
					//New bot
					WarriorContainer warriorList = new WarriorContainer();
					warriorList.createList();
					newBot = warriorList.getWarrior((int) (Math.random()*warriorList.getList().size()));
					WeaponContainer weaponList = new WeaponContainer();
					weaponList.createList(newBot.getId());

					bot.setWeapon(weaponList.getWeapon((int) (Math.random()*weaponList.getList().size())));
					bot.setId(newBot.getId());
					bot.setHealth(newBot.getHealth());
					bot.setAgility(newBot.getAgility());
					bot.setName(newBot.getName());
					bot.setSpeed(newBot.getSpeed());
					bot.setUrl(newBot.getUrl());
					bot.setDefense(newBot.getDefense());
					bot.setPoints(newBot.getPoints());
					bot.setStrenght(newBot.getStrenght());

					player.setHealth(initialHealthPlayer);
					bot.setHealth(initialHealthBot);

					//Set points for new round
					totalScore += battleScore;
					battleScore = 0;
					enemiesDefeated += 1;
					break;

				} else if (selectContinue == 1) {
					// Button "No" from joptionpane
					//Username check
					int maxUserLength = 5; // Maximum number of characters allowed
					boolean userValid = false;
					while (!userValid) { // Window asking for a username
						username = JOptionPane.showInputDialog(mainPanel, "Enter your username (max. 5 characters):");
						if (username != null && username.length() <= maxUserLength) {
							userValid = true;
						} else {
							JOptionPane.showMessageDialog(mainPanel, "Invalid username. Enter a name with a maximum of 5 characters.");
						}
					}
					// Save user data from this battle
					BBDDConnection.insertPlayer(username,totalScore,enemiesDefeated);
					int playerID = BBDDConnection.getPlayerID("Select PLAYER_ID from players WHERE PLAYER_NAME = '"+username+"'");
					// Save battle data
					JOptionPane.showMessageDialog(mainPanel, "Score saved!");
					BBDDConnection.insertBattle(String.valueOf(playerID), player.getId(),player.getWeapon().getId(),
							bot.getId(),bot.getWeapon().getId(),injuriesCaused,injuriesSuffered,totalScore);
					System.exit(0);
				} else {
					break;
				}
			}
			//Change attacker to defender
			Warrior aux = attacker;
			attacker = defender;
			defender = aux;
		}
	}
	//Method to calculate the user score
	public int calculateScore(Warrior bot) {
		int total = 0;
		total += bot.getPoints() + bot.getWeapon().getPoints();
		return total;
	}
}


