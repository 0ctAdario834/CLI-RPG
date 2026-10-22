public class Game {
    /* Utilities */
    static java.util.Scanner scanner = new java.util.Scanner(System.in);
    static String userName = "Player";
    static int playerChoice = 0;
    static int enemyChoice = 0;
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
    /*  Player stats */
        static int playerLevel = 1;
        static int playerHealth = 100;
        static int playerDmg = 10;
        static double playerDmgMultiplier = 1;
        static int playerSpeed = 1;
        static int playerDefenseHeal = 5;
    /*  Enemy stats */
        static int enemyHealth = 100;
        static int enemyDmg = 10;
        static double enemyDmgMultiplier = 1;
        static int enemySpeed = 1;
        static int enemyDefenseHeal = 5;
    /* This probably makes VSCode stop complaining about Thread.sleep() in loops by making it technically an indirect Thread.sleep() */
    public static void sleep(long milliseconds) throws InterruptedException{
        try{Thread.sleep(milliseconds);}
        catch(InterruptedException e){}
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
        for (int i = 0; i < 50; i++) {System.out.println();}}
    }
    //
    public static void actionDelay(int otherChoice) throws InterruptedException {
        Thread.sleep(otherChoice == 0 ? 1000 : 2000);}
    //
    public static void dialoguePrint(String msg, long ms) throws InterruptedException {
        char Char;
        for(int i = 0; i < msg.length(); i++){
            Char = msg.charAt(i);
            System.out.print(Char);
            sleep(ms);}
    }
    /*
    Turn logic blocks
    */
    public static void playerAttacks(){
        if(playerDmgMultiplier != 1){System.out.println("\t(Dmg Amp.: " + playerDmgMultiplier + " -> 1)");}
        if(playerSpeed != 1){System.out.println("\t(Speed: " + playerSpeed + " -> 1)");}
    }
    public static void playerStatReset(){
        playerDmgMultiplier = (double)(9+playerLevel)/10; playerSpeed = 1;
    }
    public static void enemyAttacks(){
        if(enemyDmgMultiplier != 1){System.out.println("\t(Dmg Amp.: " + enemyDmgMultiplier + " -> 1)");}
        if(enemySpeed != 1){System.out.println("\t(Speed: " + enemySpeed + " -> 1)");}
    }
    public static void enemyStatReset(){
        enemyDmgMultiplier = 1; enemySpeed = 1;
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
    public static void LvlUpPlayerHeal(){
        playerDefenseHeal = playerDefenseHeal+(playerLevel-1);
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
    public static void main(String[] args) throws InterruptedException {
        clearScreen();
        dialoguePrint("\nPlease Enter your UserName (leave blank to be unnamed)\n\t(press ENTER when you're ready)\n", 10);
        userName = scanner.nextLine();
        if(userName.equals("")){userName = "Player";}
        System.out.flush();
        actionMenu();
    }
    //
    public static void actionMenu() throws InterruptedException {
        while (true) {    
            clearScreen();
            playerChoice = 0;
            enemyChoice = 0;
            if(playerHealth <= 0){
                lose();}
            else if(enemyHealth <= 0 && playerLevel != 100){
                enemyDefeat();
                }else if(enemyHealth <= 0 && playerLevel == 100){
                    win();}
            //
            System.out.println("Current Objective: Reach Level 100");
            System.out.println("\t Level: " + playerLevel);
            System.out.println(">--------------------------------<");
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
            System.out.println("\n     { 4. Run }  { 5. Spare }\n");
            playerChoice = scanner.nextInt();
            scanner.nextLine();
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
    public static void turnEvent() throws InterruptedException {
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
                System.out.println(userName + ": ");
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
                System.out.println(userName + ": ");
                playerTakesNoDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyDefends();
                //
                playerStatReset();
            }
            case PlayerAttackEnemyDefendFailure -> {
                System.out.println(userName + " attacked faster than enemy could defend!\n");
                System.out.println(userName + ": ");
                playerTakesNoDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyTakesDmg();
                //
                playerStatReset();
            }
            case EnemyAttackPlayerDefendSuccess -> {
                System.out.println("Enemy attack was blocked by " + userName + "!\n");
                System.out.println(userName + ": ");
                playerDefends();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyAttacks();
                //
                enemyStatReset();
            }
            case EnemyAttackPlayerDefendFailure -> {
                System.out.println("Enemy attacked faster than " + userName + " could defend!\n");
                System.out.println(userName + ": ");
                playerTakesDmg();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyAttacks();
                //
                enemyStatReset();
            }
            case bothDefend -> {
                System.out.println(userName + ": ");
                playerDefends();
                System.out.println("\n Enemy:");
                enemyDefends();
            }
            case PlayerAttackEnemyChargeUp -> {
                System.out.println(userName + ": ");
                playerTakesNoDmg();
                playerAttacks();
                System.out.println("\n Enemy:");
                enemyTakesDmg();
                enemyChargesUp();
                //
                playerStatReset();
            }
            case PlayerDefendEnemyChargeUp -> {
                System.out.println(userName + ": ");
                playerDefends();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyChargesUp();
            }
            case EnemyAttackPlayerChargeUp -> {
                System.out.println(userName + ": ");
                playerTakesDmg();
                playerChargesUp();
                System.out.println("\n Enemy:");
                enemyTakesNoDmg();
                enemyAttacks();
                //
                enemyStatReset();
            }
            case EnemyDefendPlayerChargeUp -> {
                System.out.println(userName + ": ");
                playerTakesNoDmg();
                playerChargesUp();
                System.out.println("\n Enemy:");
                enemyDefends();
            }
            case bothChargeUp -> {
                System.out.println(userName + ": ");
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
        scanner.nextLine();
    }
    public static int playerAction(int playerChoice) throws InterruptedException {
        switch (playerChoice){
                case 1 -> battlePlayer();
                case 2 -> chargeUpPlayer();
                case 3 -> defendPlayer();
                case 4 -> run();
            default -> {}}
        return playerChoice;
    }
    //
    public static void battlePlayer() throws InterruptedException {
        dialoguePrint("\n" + userName + " is attacking!\n", 25);
        playerIsAttacking = true;
        //
        actionDelay(enemyChoice);
    }
    public static void chargeUpPlayer() throws InterruptedException {
        dialoguePrint("\n" + userName + " is charging up!\n", 25);
        playerIsChargingUp = true;
        playerDmgMultiplier += 1;
        playerSpeed += 1;
        //
        actionDelay(enemyChoice);
    }
    public static void defendPlayer() throws InterruptedException {
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
    public static void run() throws InterruptedException {
        dialoguePrint("\n" + userName + " is running away!", 25);
        Thread.sleep(3000);
        clearScreen();
        dialoguePrint("\n\tYou fucking coward!\n", 25);
        Thread.sleep(3000);
        System.exit(0);
    }
    //
    public static int enemyAction() throws InterruptedException {
        enemyChoice = (int)(Math.random()*3+1);
        switch(enemyChoice){
            case 1 -> battleEnemy();
            case 2 -> chargeUpEnemy();
            case 3 -> defendEnemy();}
        return enemyChoice;
    }
    //
    public static void battleEnemy() throws InterruptedException {
        dialoguePrint("\nEnemy is attacking!\n", 25);
        enemyIsAttacking = true;
        //
        actionDelay(playerChoice);
    }
    public static void chargeUpEnemy() throws InterruptedException {
        dialoguePrint("\nEnemy is charging up!\n", 25);
        enemyIsChargingUp = true;
        enemyDmgMultiplier += 1;
        enemySpeed += 1;
        //
        actionDelay(playerChoice);
    }
    public static void defendEnemy() throws InterruptedException {
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
    public static void enemyDefeat() throws InterruptedException {
        clearScreen();
        dialoguePrint("\nYou defeated the enemy!", 25);
        Thread.sleep(1000);
        dialoguePrint(" (moving on to the next level)\n", 25);
        Thread.sleep(3000);
        playerLevel++;
        dialoguePrint("\nLevel -> " + playerLevel + " (+1)", 36);
        playerHealth += 50;
        dialoguePrint("\nHealth -> " + playerHealth + " (+50)", 36);
        enemyHealth = 100;
        LvlUpPlayerHeal();
        playerIsAttacking = false;
        playerFailedDefense = false;
        playerIsDefending = false;
        enemyFailedDefense = false;
        enemyIsAttacking = false;
        enemyIsDefending = false;
        playerIsChargingUp = false;
        enemyIsChargingUp = false;
        playerStatReset(); enemyStatReset();
        Thread.sleep(2000);
        actionMenu();
    }
    //
    public static void win() throws InterruptedException {
        clearScreen();
        dialoguePrint("\nYou won! (GGWP)", 25);
        Thread.sleep(3000);
        System.exit(0);
    }
    public static void lose() throws InterruptedException {
        clearScreen();
        dialoguePrint("\nYou lost! (womp womp)", 25);
        Thread.sleep(3000);
        System.exit(0);
    }
    //
    public static void spare() throws InterruptedException {
        Thread.sleep(2500); clearScreen();
        dialoguePrint("\nEnemy:", 25);
        Thread.sleep(3000); System.out.print("\n\n"); Thread.sleep(250);
        dialoguePrint("\tYou", 100); dialoguePrint(" ... ", 500); dialoguePrint("spared me?", 100);
        Thread.sleep(3000); clearScreen();
        for(int i = 1; i <= 3; i++){
            System.out.print(".");
            sleep(1500);}
        clearScreen(); Thread.sleep(1000); System.out.print("\n");
        do { 
            System.out.print("\033[2K\r\tPlayer Health: " + playerHealth);
            System.out.flush();
            sleep(((playerHealth*27)/100));
            playerHealth--;
        } while (playerHealth >= 0);
        Thread.sleep(2000);
        dialoguePrint("\n\nYou died", 25); Thread.sleep(1000); dialoguePrint(" (LOL)", 25);
        Thread.sleep(2000); clearScreen(); Thread.sleep(1000);
        dialoguePrint("\n\tGET DUNKED OOOOOONNN!!!", 49);
        Thread.sleep(2000); System.out.print("\n");
        System.exit(0);
    }
}