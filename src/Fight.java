public class Fight {
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

    public String perform_attack(Warrior warrior) {
        String message;
        int num = (int) (Math.floor(Math.random() * 100) + 1);
        if ((warrior.getAgility() * 10) > num) {
            message = "Attack succesfully performed";
        }
        else {
            message = "Failed attack";
        }
        return message;
    }

//	Method that determines if a warrior can dodge an attack or, in case of receiving it, how much damage recieves

    public String dodge_attack(Warrior attacker, Weapon weapon, Warrior defender) {
        String message;
        int damage = attacker.getStrenght() + weapon.getStrenght() - defender.getDefense();
        int num = (int) (Math.floor(Math.random() * 50) + 1);
        if ((defender.getAgility() * 10) > num) {
            message = "The defender missed the attack";
        }
        else {
            defender.setHealth(defender.getHealth() - damage);
            message = "The defender has recieved " + damage + " damage points";
        }
        return message;
    }

//	Method that determines if a warrior repeats an attack or gives his turn to his opponent

    public boolean repeat_attack(Warrior attacker, Weapon attackerWeapon, Warrior defender, Weapon defenderWeapon) {
        boolean repeatAttack;
        int num = (int) (Math.floor(Math.random() * 100) + 1);
        if (total_speed(attacker, attackerWeapon) <= total_speed(defender, defenderWeapon)) {
            repeatAttack = false;
        }
        else {
            if (((total_speed(attacker, attackerWeapon) - total_speed(defender, defenderWeapon)) * 10) > num) {
                repeatAttack = true;
            }
            else {
                repeatAttack = false;
            }
        }
        return repeatAttack;
    }

//	Method that determines who's the warrior that have won

    public String winner(Warrior warrior1, Warrior warrior2) {
        String message = "";
        if (warrior1.getHealth() == 0) {
            message = warrior1.getName() + " wins!";
        }
        else if (warrior2.getHealth() == 0) {
            message = warrior2.getName() + " wins!";
        }
        return message;
    }

//	Method that determines the fight logic

    public void combat(Warrior player, Weapon playerWeapon, Warrior bot, Weapon botWeapon) {

        Warrior attacker = null;
        Weapon attackerWeapon = null;
        Warrior defender = null;
        Weapon defenderWeapon = null;
        int num = (int) (Math.floor(Math.random() * 2) + 1);

        if (total_speed(player, playerWeapon) > total_speed(bot, botWeapon)) {
            player = attacker;
            bot = defender;
        }
        else if (total_speed(bot, botWeapon) > total_speed(player, playerWeapon)) {
            bot = attacker;
            player = defender;
        }
        else {
            if (total_agility(player) > total_agility(bot)) {
                player = attacker;
                bot = defender;
            }
            else if (total_agility(bot) > total_agility(player)) {
                bot = attacker;
                player = defender;
            }
            else {
                if (num == 1) {
                    player = attacker;
                    bot = defender;
                }
                else{
                    bot = attacker;
                    player = defender;
                }
            }
        }
        while (true) {
            while (true) {
                perform_attack(attacker);
                dodge_attack(attacker, attackerWeapon, defender);
                if (repeat_attack(attacker, attackerWeapon, defender, defenderWeapon) == false) {
                    break;
                }
            }
            if (attacker.getHealth() == 0 || defender.getHealth() == 0) {
                //new Continue();
                break;
            }
            while (true) {
                perform_attack(defender);
                dodge_attack(defender, defenderWeapon, attacker);
                if (repeat_attack(defender, defenderWeapon, attacker, attackerWeapon) == false) {
                    break;
                }
            }
            if (attacker.getHealth() == 0 || defender.getHealth() == 0) {
                //new Continue();
                break;
            }
        }
    }

}
