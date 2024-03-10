package userpack;
import java.security.SecureRandom;
import org.mindrot.jbcrypt.BCrypt;

public class Credentials {
    private String email;
    private String hashed_password;
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+{}[]|;:,.<>?";
    public Credentials(String email, String password) {
        this.email = email;
        this.hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, hashed_password);
    }
    static public String suggestStrongPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        //adauga o bucla while dupa ce finalizezi restul claselor ca sa te asiguri ca
        //parola nu exista deja in baza de date
        for (int i = 0; i < 4; i++) {
            password.append(getRandomChar(UPPERCASE_LETTERS, random));
            password.append(getRandomChar(LOWERCASE_LETTERS, random));
            password.append(getRandomChar(NUMBERS, random));
            password.append(getRandomChar(SPECIAL_CHARACTERS, random));
        }
        return password.toString();
    }
    private static char getRandomChar(String char_pool, SecureRandom random) {
        int random_index = random.nextInt(char_pool.length());
        return char_pool.charAt(random_index);
    }
}
