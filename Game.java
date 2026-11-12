
import java.io.IOException;

public class Game {
    /* Utilities */
    static java.util.Scanner scanner = new java.util.Scanner(System.in);
    static boolean fastMode = false;
    static boolean debugMode = false;
    static String userName = "Player";
    static int playerChoice = 0;
    static byte enemyChoice = 0;
    static boolean playerIsAttacking = false;
    static boolean enemyIsAttacking = false;
    static boolean playerIsDefending = false;
    static boolean playerFailedDefense = false;
    static boolean enemyIsDefending = false;
    static boolean enemyFailedDefense = false;
    static boolean playerIsChargingUp = false;
    static boolean enemyIsChargingUp = false;
    static enum turnScenarios{
        bothAttack,
        bothDefend,
        bothChargeUp,
        PlayerAttackEnemyDefendSuccess,
        EnemyAttackPlayerDefendSuccess,
        PlayerAttackEnemyDefendFailure,
        EnemyAttackPlayerDefendFailure,
        PlayerAttackEnemyChargeUp,
        EnemyAttackPlayerChargeUp,
        PlayerDefendEnemyChargeUp,
        EnemyDefendPlayerChargeUp,
        noneAttack,
    }static turnScenarios turnScenario;
    /* Level */
        static byte level = 1;
    /*  Player stats */
        static int playerHealth = 100;
        static int playerDmg = 10;
        static double playerDmgMultiplier = 1;
        static double playerChargeBonus = 1;
        static double playerDmgMultCurrLvl = 1;
        static byte playerSpeed = 1;
        static int playerDefenseHeal = 5;
    /*  Enemy stats */
        static int enemyHealth = 100;
        static int enemyDmg = 10;
        static double enemyDmgMultiplier = 1;
        static double enemyChargeBonus = 1;
        static double enemyDmgMultCurrLvl = 1;
        static byte enemySpeed = 1;
        static int enemyDefenseHeal = 5;
    /* This probably makes VSCode stop complaining about Thread.sleep() in loops by making it technically an indirect Thread.sleep() */
    public static void sleep(long milliseconds) {
        try{Thread.sleep(milliseconds);}
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
    /**/
    public static void clearScreen() {
    try {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            // Windows clear
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();}
            else{
            // Unix clear
            new ProcessBuilder("clear").inheritIO().start().waitFor();}}
    catch (java.io.IOException | InterruptedException e){
        // Fallback clear
        /*for (int i = 0; i < 50; i++) {System.out.println();}}*/
        System.out.print("\033[H\033[2J");
        }
    }
    public static void clearInputBuffer() {
        try {
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (IOException ignored) {}
        System.out.flush();
    }
    //
    public static void actionDelay(int otherChoice) {
        if(!fastMode){
            sleep(otherChoice == 0 ? 1000 : 2000);
        }else{
            sleep(500);
        }
    }
    //
    public static void dialoguePrint(String msg, long ms) {
        if(!fastMode){
            char Char;
            for(int i = 0; i < msg.length(); i++){
                Char = msg.charAt(i);
                System.out.print(Char);
                sleep(ms);
            }
        }else{
            char Char;
            for(int i = 0; i < msg.length(); i++){
                Char = msg.charAt(i);
                System.out.print(Char);
            }
        }
    }
    //
    public static double round(double value){
        return Math.round(value * 100.0) / 100.0;
    }
    /*
    Turn logic blocks
    */
    public static void playerAttacks(){
        if(playerDmgMultiplier != playerDmgMultCurrLvl){System.out.println("\t(Dmg Amp.: " + playerDmgMultiplier + " -> " + playerDmgMultCurrLvl + ")");}
        if(playerSpeed != 1){System.out.println("\t(Speed: " + playerSpeed + " -> 1)");}
    }
    public static void playerStatReset(){
        playerDmgMultiplier = playerDmgMultCurrLvl; playerSpeed = 1;
    }
    public static void enemyAttacks(){
        if(enemyDmgMultiplier != enemyDmgMultCurrLvl){System.out.println("\t(Dmg Amp.: " + enemyDmgMultiplier + " -> " + enemyDmgMultCurrLvl + ")");}
        if(enemySpeed != 1){System.out.println("\t(Speed: " + enemySpeed + " -> 1)");}
    }
    public static void enemyStatReset(){
        enemyDmgMultiplier = enemyDmgMultCurrLvl;
        enemySpeed = 1;
    }
    public static void playerTakesDmg(){
        int eDmg = (int)(enemyDmg*enemyDmgMultiplier);
        playerHealth = playerHealth-eDmg;
            if(playerHealth <= 0){playerHealth = 0;}
            System.out.println("Player Health: " + playerHealth + " (- " + eDmg + ")");
    }
    public static void playerTakesNoDmg(){
        System.out.println("Player Health: " + playerHealth);
    }
    public static void enemyTakesDmg(){
        int pDmg = (int)(playerDmg*playerDmgMultiplier);
        enemyHealth = enemyHealth-pDmg;
            if(enemyHealth <= 0){enemyHealth = 0;}
            System.out.println("Enemy Health: " + enemyHealth + " (- " + pDmg + ")");
    }
    public static void enemyTakesNoDmg(){
        System.out.println("Enemy Health: " + enemyHealth);
    }
    public static void LvlUpPlayer(){
        playerDmgMultiplier = 1.0+((level-1)/5)*0.5;
        playerDefenseHeal = 5+((level-1)*2);
        playerDmgMultCurrLvl = playerDmgMultiplier;
        playerChargeBonus = Math.min(20, 1+((level-1)/5));
    }
    public static void LvlUpEnemy(){
        if(level%2 == 0){
            enemyDmgMultiplier = Math.max(1, 1.0+((level-6)/5)*0.5);
            enemyDefenseHeal = 5+15*level/8;
        }
        enemyDmgMultCurrLvl = enemyDmgMultiplier;
        if(level%10 == 0){
            enemyHealth = 100+3*level/2;
            enemyDmgMultiplier = (double)(10+level)/10;
            enemySpeed = 2;
        }else{
            enemyHealth = 100+level;
            enemySpeed = 1;
        }
        if(level == 100){
            enemyHealth = 250;
            enemyDmgMultiplier = (double)(10+level)/10;
            enemySpeed = 3;
        }
        enemyChargeBonus = Math.max(1, 1+((level-21)/5));
    }
    public static void playerDefends(){
        System.out.println("Player Defended!");
        System.out.println("Player Health: " + playerHealth + " (+" + playerDefenseHeal + ")");
    }
    public static void enemyDefends(){
        System.out.println("Enemy Defended!");
        System.out.println("Enemy Health: " + enemyHealth + " (+" + enemyDefenseHeal + ")");
    }
    public static void playerChargesUp(){
        System.out.println("Player Charged up!");
        System.out.println("\t(Dmg Amp.: " + playerDmgMultiplier + ")");
        System.out.println("\t(Speed: " + playerSpeed + ")");
    }
    public static void enemyChargesUp(){
        System.out.println("Enemy Charged up!");
        System.out.println("\t(Dmg Amp.: " + enemyDmgMultiplier +")");
        System.out.println("\t(Speed: " + enemySpeed + ")");
    }
    //
    public static void main(String[] args) {
        fastMode = (args.length == 1);
        debugMode = (args.length == 2);
        if(args.length >= 3){debugMode = fastMode = true;}
        clearScreen();
        dialoguePrint("\nPlease Enter your UserName (leave blank to be unnamed)\n\t(press ENTER when you're ready)\n", 10);
        clearInputBuffer();
        userName = scanner.nextLine();
        if(userName.equals("")){userName = "Player";}
        System.out.flush();
        while(true){actionMenu();}
    }
    //
    public static void debugMenu() {
        clearScreen();
        clearInputBuffer();
        System.out.println(
            """
            Options:\n
            1. Level\n
            2. Player Health
            3. PLayer Dmg Amp
            4. Player Defense Heal
            5. Player Speed\n
            6. Enemy Health
            7. Enemy Dmg Amp
            8. Enemy Defense Heal
            9. Enemy Speed\n""");
        byte choice = scanner.nextByte();
        switch(choice){
            case 1 -> {
                System.out.print("\nEnter your desired Level (byte): ");
                level = scanner.nextByte();}
            case 2 -> {
                System.out.print("\nEnter your desired Player Health (int): ");
                playerHealth = scanner.nextInt();}
            case 3 -> {
                System.out.print("\nEnter your desired Player Dmg Amp (double): ");
                playerDmgMultiplier = scanner.nextDouble();}
            case 4 -> {
                System.out.print("\nEnter your desired Player Heal Amount (int): ");
                playerDefenseHeal = scanner.nextInt();}
            case 5 -> {
                System.out.print("\nEnter your desired Player Speed (byte): ");
                playerSpeed = scanner.nextByte();}
            case 6 -> {
                System.out.print("\nEnter your desired Enemy Health (int): ");
                enemyHealth = scanner.nextInt();}
            case 7 -> {
                System.out.print("\nEnter your desired Enemy Dmg Amp (double): ");
                enemyDmgMultiplier = scanner.nextDouble();}
            case 8 -> {
                System.out.print("\nEnter your desired Enemy Heal Amout (int): ");
                enemyDefenseHeal = scanner.nextInt();}
            case 9 -> {
                System.out.print("\nEnter your desired Enemy Speed (byte): ");
                enemySpeed = scanner.nextByte();}
            case 0 -> {}
            default -> {}
        }
    }
    //
    public static void actionMenu() {
        while (true) {    
            System.out.flush();
            clearScreen();
            playerChoice = 0;
            enemyChoice = 0;
            if(playerHealth <= 0){
                lose();}
            else if(enemyHealth <= 0 && level != 100){
                enemyDefeat();
                continue;
                }else if(enemyHealth <= 0 && level == 100){
                    win();}
            //
            if(level == 100){
                System.out.println("\t FINAL BOSS");
            }else if(level % 10 == 0){
                System.out.println("Current Objective: Reach Level 100");
                if(debugMode){System.out.print("DEBUG\t Level: " + level + "(Boss)");
                }else{System.out.print("\t Level: " + level + "(Boss)");}
                if(fastMode){System.out.print("\tFAST");}
            }else{
                System.out.println("Current Objective: Reach Level 100");
                if(debugMode){System.out.print("DEBUG\t Level: " + level);
                }else{System.out.print("\t Level: " + level);}
                if(fastMode){System.out.print("\tFAST");}
            }
            System.out.println("\n>--------------------------------<");
            System.out.println("\t" + userName + ": ");
            System.out.println("\t Health: " + playerHealth);
            System.out.println("\t  Dmg Amp.: " + playerDmgMultiplier);
            System.out.println("\t   Speed: " + playerSpeed);
            System.out.print("\n");
            System.out.println("\tEnemy: ");
            System.out.println("\t Health: " + enemyHealth);
            System.out.println("\t  Dmg Amp.: " + enemyDmgMultiplier);
            System.out.println("\t   Speed: " + enemySpeed);
            System.out.println(">--------------------------------<\n");
            System.out.println("\t1. Attack");
            System.out.println("\t2. Charge up");
            System.out.println("\t3. Defend");
            System.out.println("\n     { 4. Run }  { 5. Spare }");
            if(debugMode) {System.out.println("\t0. DEBUG MENU");}
            clearInputBuffer();
            playerChoice = scanner.nextInt();
            scanner.nextLine();
            //
            if(playerChoice == 834) {debugMode = !debugMode; return;}
                if(debugMode && playerChoice == 0) {debugMenu(); return;}
            if(playerChoice == 712) {fastMode = !fastMode; return;}
            //
            if(playerChoice == 5){spare();}
            if(playerSpeed>enemySpeed){
                playerAction(playerChoice);
                enemyAction();
            }else if(playerSpeed<enemySpeed){
                enemyAction();
                playerAction(playerChoice);
            }else{
                switch ((int)(Math.random()*2+1)) {
                    case 1 -> {
                        playerAction(playerChoice);
                        enemyAction();
                    }
                    case 2 -> {
                        enemyAction();
                        playerAction(playerChoice);
                    }
                }
            }
            //
            turnEvent();
        }
    }
    public static void turnEvent() {
        turnScenario = turnScenarios.noneAttack;
        clearScreen();
        //
            if(playerIsAttacking){
                if(enemyIsAttacking){turnScenario = turnScenarios.bothAttack;
                }else if(enemyIsDefending){
                    if(enemyFailedDefense){
                        turnScenario = turnScenarios.PlayerAttackEnemyDefendFailure;
                    }else{turnScenario = turnScenarios.PlayerAttackEnemyDefendSuccess;}
                }else if(enemyIsChargingUp){turnScenario = turnScenarios.PlayerAttackEnemyChargeUp;}
            }else if(playerIsDefending){
                if(enemyIsAttacking){
                    if(playerFailedDefense){
                        turnScenario = turnScenarios.EnemyAttackPlayerDefendFailure;
                    }else{turnScenario = turnScenarios.EnemyAttackPlayerDefendSuccess;}
                }else if(enemyIsDefending){turnScenario = turnScenarios.bothDefend;
                }else if(enemyIsChargingUp){turnScenario = turnScenarios.PlayerDefendEnemyChargeUp;}
            }else if(playerIsChargingUp){
                if(enemyIsAttacking){turnScenario = turnScenarios.EnemyAttackPlayerChargeUp;
                }else if(enemyIsDefending){turnScenario = turnScenarios.EnemyDefendPlayerChargeUp;
                }else if(enemyIsChargingUp){turnScenario = turnScenarios.bothChargeUp;}
            }
        //
        switch (turnScenario) {
            case bothAttack -> {
                System.out.println(" " + userName + ": ");
                playerTakesDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyTakesDmg();
                enemyAttacks();
                //
                playerStatReset();
                enemyStatReset();
            }
            case PlayerAttackEnemyDefendSuccess -> {
                System.out.println(userName + "'s attack was blocked by enemy!\n");
                System.out.println(" " + userName + ": ");
                playerTakesNoDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyDefends();
                //
                playerStatReset();
            }
            case PlayerAttackEnemyDefendFailure -> {
                System.out.println(userName + " attacked faster than enemy could defend!\n");
                System.out.println(" " + userName + ": ");
                playerTakesNoDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyTakesDmg();
                //
                playerStatReset();
            }
            case EnemyAttackPlayerDefendSuccess -> {
                System.out.println("Enemy attack was blocked by " + userName + "!\n");
                System.out.println(" " + userName + ": ");
                playerDefends();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyAttacks();
                //
                enemyStatReset();
            }
            case EnemyAttackPlayerDefendFailure -> {
                System.out.println("Enemy attacked faster than " + userName + " could defend!\n");
                System.out.println(" " + userName + ": ");
                playerTakesDmg();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyAttacks();
                //
                enemyStatReset();
            }
            case bothDefend -> {
                System.out.println(" " + userName + ": ");
                playerDefends();
                System.out.println("\n Enemy:");
                enemyDefends();
            }
            case PlayerAttackEnemyChargeUp -> {
                System.out.println(" " + userName + ": ");
                playerTakesNoDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyTakesDmg();
                enemyChargesUp();
                //
                playerStatReset();
            }
            case PlayerDefendEnemyChargeUp -> {
                System.out.println(" " + userName + ": ");
                playerDefends();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyChargesUp();
            }
            case EnemyAttackPlayerChargeUp -> {
                System.out.println(" " + userName + ": ");
                playerTakesDmg();
                playerChargesUp();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyAttacks();
                //
                enemyStatReset();
            }
            case EnemyDefendPlayerChargeUp -> {
                System.out.println(" " + userName + ": ");
                playerTakesNoDmg();
                playerChargesUp();
                System.out.println("\n Enemy:");
                enemyDefends();
            }
            case bothChargeUp -> {
                System.out.println(" " + userName + ": ");
                playerTakesNoDmg();
                playerChargesUp();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyChargesUp();
            }
            case noneAttack -> {
                System.out.println(userName + " Health: " + playerHealth);
                System.out.println("Enemy Health: " + enemyHealth);
            }
            default -> {}
        }
        if(playerHealth <= 0){
            System.out.println("\n" + userName + "was defeated!");}
        if(enemyHealth <= 0){
            System.out.println("\nEnemy was defeated!");}
        //
        playerIsAttacking = false;
        playerFailedDefense = false;
        playerIsDefending = false;
        enemyFailedDefense = false;
        enemyIsAttacking = false;
        enemyIsDefending = false;
        playerIsChargingUp = false;
        enemyIsChargingUp = false;
        //
        System.out.println("\nPress ENTER to continue...");
        clearInputBuffer();
        scanner.nextLine();
    }
    public static int playerAction(int playerChoice) {
        switch (playerChoice){
                case 1 -> battlePlayer();
                case 2 -> chargeUpPlayer();
                case 3 -> defendPlayer();
                case 4 -> run();
            default -> {}}
        return playerChoice;
    }
    //
    public static void battlePlayer() {
        dialoguePrint("\n" + userName + " is attacking!\n", 25);
        playerIsAttacking = true;
        //
        actionDelay(enemyChoice);
    }
    public static void chargeUpPlayer() {
        dialoguePrint("\n" + userName + " is charging up!\n", 25);
        playerIsChargingUp = true;
        playerDmgMultiplier += playerChargeBonus;
        //playerDmgMultiplier = round(playerDmgMultiplier);
        playerSpeed = (byte)Math.min(10, ++playerSpeed);
        //
        actionDelay(enemyChoice);
    }
    public static void defendPlayer() {
        if(playerSpeed < enemySpeed && enemyChoice == 1){
            dialoguePrint("\n" + userName + " is too slow to defend!\n", 25);
            playerIsDefending = true;
            playerFailedDefense = true;}
        else{
            dialoguePrint("\n" + userName + " is defending!\n", 25);
            playerIsDefending = true;
            playerFailedDefense = false;
            playerHealth += playerDefenseHeal;}
        //
        actionDelay(enemyChoice);
    }
    //
    public static void run() {
        dialoguePrint("\n" + userName + " is running away!", 25);
        sleep(3000);
        clearScreen();
        dialoguePrint("\n\tYou fucking coward!\n", 25);
        sleep(3000);
        System.exit(0);
    }
    //
    public static boolean enemyCanKill() {
        return (playerHealth <= enemyDmg*enemyDmgMultiplier && enemySpeed >= playerSpeed);
    }
    public static boolean playerCouldKill() {
        return (enemyHealth <= playerDmg*playerDmgMultiplier);
    }
    public static boolean enemyOutSped() {
        return (enemySpeed < playerSpeed);
    }
    public static int enemyAction() {
        if(enemyCanKill()){
            enemyChoice = 1;
            battleEnemy();
        }else if (playerCouldKill()) {
            enemyChoice = (byte)(Math.random()*3+1);
            switch(enemyChoice){
                case 1 -> battleEnemy();
                case 2,3 -> defendEnemy();}
        }else if(enemyOutSped()){
            enemyChoice = (byte)(Math.random()*6+1);
            switch(enemyChoice){
                case 1,2 -> battleEnemy();
                case 3,4,5 -> chargeUpEnemy();
                case 6 -> defendEnemy();}
        }else{
            enemyChoice = (byte)(Math.random()*3+1);
            switch(enemyChoice){
                case 1 -> battleEnemy();
                case 2 -> chargeUpEnemy();
                case 3 -> defendEnemy();}
        }
        return enemyChoice;
    }
    //
    public static void battleEnemy() {
        dialoguePrint("\nEnemy is attacking!\n", 25);
        enemyIsAttacking = true;
        //
        actionDelay(playerChoice);
    }
    public static void chargeUpEnemy() {
        dialoguePrint("\nEnemy is charging up!\n", 25);
        enemyIsChargingUp = true;
        enemyDmgMultiplier += enemyChargeBonus;
        //enemyDmgMultiplier = round(enemyDmgMultiplier);
        enemySpeed = (byte)Math.min(10, ++enemySpeed);
        //
        actionDelay(playerChoice);
    }
    public static void defendEnemy() {
        if(enemySpeed < playerSpeed && playerChoice == 1){
            dialoguePrint("\nEnemy is too slow to defend!\n", 25);
            enemyIsDefending = true;
            enemyFailedDefense = true;}
        else{
            dialoguePrint("\nEnemy is defending!\n", 25);
            enemyIsDefending = true;
            enemyFailedDefense = false;
            enemyHealth += enemyDefenseHeal;}
        //
        actionDelay(playerChoice);
    }
    //
    public static void enemyDefeat() {
        clearScreen();
        dialoguePrint("\nYou defeated the enemy!", 25);
        sleep(1000);
        dialoguePrint(" (moving on to the next level)\n", 25);
        sleep(3000);
        level++;
        dialoguePrint("\nLevel -> " + level + " (+1)", 36);
        int levelUpHeal = Math.max((100-(level-2))/2,1);
        playerHealth += levelUpHeal;
        dialoguePrint("\nHealth -> " + playerHealth + " (+" + levelUpHeal + ")", 36);
        enemyHealth = 100;
        playerStatReset(); enemyStatReset();
        LvlUpPlayer();
        LvlUpEnemy();
        playerIsAttacking = false;
        playerFailedDefense = false;
        playerIsDefending = false;
        enemyFailedDefense = false;
        enemyIsAttacking = false;
        enemyIsDefending = false;
        playerIsChargingUp = false;
        enemyIsChargingUp = false;
        sleep(2000);
    }
    //
    public static void win() {
        clearScreen();
        dialoguePrint("\nYou won! (GGWP)\n", 25);
        sleep(3000);
        System.exit(0);
    }
    public static void lose() {
        clearScreen();
        dialoguePrint("\nYou lost! (womp womp)\n", 25);
        sleep(3000);
        System.exit(0);
    }
    //
    public static void spare() {
        sleep(2500); clearScreen();
        dialoguePrint("\nEnemy:", 25);
        sleep(3000); System.out.print("\n\n"); sleep(250);
        dialoguePrint("\tYou", 100); dialoguePrint(" ... ", 500); dialoguePrint("spared me?", 100);
        sleep(3000); clearScreen();
        for(int i = 1; i <= 3; i++){
            System.out.print(".");
            sleep(1500);}
        clearScreen(); sleep(1000); System.out.print("\n");
        do { 
            System.out.print("\033[2K\r\tPlayer Health: " + playerHealth);
            System.out.flush();
            sleep(((playerHealth*27)/100));
            playerHealth--;
        } while (playerHealth >= 0);
        sleep(2000);
        dialoguePrint("\n\nYou died", 25); sleep(1000); dialoguePrint(" (LOL)", 25);
        sleep(2000); clearScreen(); sleep(1000);
        dialoguePrint("\n\tGET DUNKED OOOOOONNN!!!", 49);
        sleep(2000); System.out.print("\n");
        System.exit(0);
    }
}