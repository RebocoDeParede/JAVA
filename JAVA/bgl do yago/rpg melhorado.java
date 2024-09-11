import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Herói", 40000, 25, 10);
        Boss boss = new Boss(150, 30, 15);

        System.out.println("Bem-vindo ao RPG simples!");
        System.out.println("Você encontrou um boss!");
        System.out.println("Prepare-se para a batalha!\n");

        Battle battle = new Battle(player, boss);
        battle.startBattle();
    }
}

class Player {
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private boolean isDefending;

    public Player(String name, int hp, int attack, int defense) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.isDefending = false;
    }

    public void takeDamage(int damage) {
        if (isDefending) {
            damage -= defense;
            if (damage < 0) damage = 0;
        }
        hp -= damage;
        if (hp < 0) hp = 0;
        isDefending = false; // Reseta a defesa após receber dano
    }

    public int calculateDamage() {
        // Cálculo de dano para o jogador com base no ataque
        Random rand = new Random();
        int damage = rand.nextInt(attack / 2) + (attack / 2); // Danos entre attack/2 e attack
        return damage;
    }

    public void defend() {
        isDefending = true;
        System.out.println("Você está se defendendo.");
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public String getName() {
        return name;
    }

    public int getHP() {
        return hp;
    }
}

class Boss {
    private int hp;
    private int attack;
    private int defense;

    public Boss(int hp, int attack, int defense) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    public int calculateDamage() {
        // Cálculo de dano para o boss com base no ataque
        Random rand = new Random();
        int damage = rand.nextInt(attack / 2) + (attack / 2); // Danos entre attack/2 e attack
        return damage;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getHP() {
        return hp;
    }
}

class Battle {
    private Player player;
    private Boss boss;
    private Scanner scanner;

    public Battle(Player player, Boss boss) {
        this.player = player;
        this.boss = boss;
        this.scanner = new Scanner(System.in);
    }

    public void startBattle() {
        boolean playerTurn = true;
        int turn = 1;

        while (player.isAlive() && boss.isAlive()) {
            System.out.println("------------------------");
            System.out.println("Turno " + turn + ":");
            System.out.println("------------------------");

            System.out.println("Status atual:");
            System.out.println("Jogador: " + player.getName() + " | HP: " + player.getHP());
            System.out.println("Boss | HP: " + boss.getHP());
            System.out.println("------------------------\n");

            if (playerTurn) {
                playerTurn(player);
            } else {
                bossTurn(boss);
            }

            playerTurn = !playerTurn;
            turn++;
        }

        System.out.println("------------------------");
        if (player.isAlive()) {
            System.out.println("Você derrotou o boss!");
        } else {
            System.out.println("Você foi derrotado pelo boss!");
        }
        System.out.println("Fim do jogo.");

        scanner.close();
    }

    private void playerTurn(Player player) {
        // Turno do jogador
        System.out.println("É o seu turno, o que você deseja fazer?");
        System.out.println("1. Atacar o boss");
        System.out.println("2. Defender-se");
        System.out.println("3. Esquivar-se");
        System.out.print("Escolha uma opção (1, 2 ou 3): ");

        int choice = getUserChoice(1, 3); // Obtém escolha válida do usuário

        switch (choice) {
            case 1:
                int playerDamage = player.calculateDamage();
                boss.takeDamage(playerDamage);
                System.out.println("Você ataca o boss causando " + playerDamage + " de dano.");
                break;
            case 2:
                player.defend();
                break;
            case 3:
                System.out.println("Você tenta se esquivar do ataque.");
                // Implementar lógica de esquiva, aqui vamos considerar que não afeta o turno do jogador
                break;
            default:
                System.out.println("Opção inválida. Você perdeu seu turno.");
                break;
        }
    }

    private void bossTurn(Boss boss) {
        // Turno do boss
        int bossDamage = boss.calculateDamage();
        player.takeDamage(bossDamage);
        System.out.println("O boss ataca você causando " + bossDamage + " de dano.");
    }

    private int getUserChoice(int min, int max) {
        int choice;
        do {
            choice = getIntInput();
            if (choice < min || choice > max) {
                System.out.println("Opção inválida. Escolha novamente.");
            }
        } while (choice < min || choice > max);
        return choice;
    }

    private int getIntInput() {
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Tente novamente.");
        }
        return input;
    }
}